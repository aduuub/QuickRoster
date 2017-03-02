package com.example.adam.quickroster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class StatsFragment extends Fragment implements View.OnClickListener {

    private GraphView mGraph;
    private Spinner mXSpinner;
    private Spinner mYSpinner;


    private String xFormat; // Week, Month, Year
    private String yFormat; // Pay, Hours


    public StatsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xFormat = "Week";
        yFormat = "Pay";

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Statistics");
        View view = inflater.inflate(R.layout.activity_stats, container, false);

        mGraph = (GraphView) view.findViewById(R.id.statistics_graph);
        setSpinnersAndListeners(view);
        setGraph();
        return view;
    }

    /**
     * Inits the graph with the set values.
     */
    private void setGraph(){
        DataPoint[] points = new DataPoint[10];
        for(int i=0; i < 10; i++){
            double val = Math.random()*20;
            points[i] = new DataPoint(i*86400, val);
        }

        // TODO get data from server

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        mGraph.addSeries(series);

        // set date label formatter
        // graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(HRVViewer.this));
        final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(getFormatter());

        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {

            @Override
            public String formatLabel(double value, boolean isValueX) {
                if(isValueX) // dateTimeFormatter.format(new Date((long) value));
                    return dateTimeFormatter.format(new Date((long) value)); // super.formatLabel(value, isValueX);
                else
                    return super.formatLabel(value, isValueX);
            }
        });

        mGraph.getGridLabelRenderer().setHumanRounding(true);

        mGraph.getGridLabelRenderer().setNumHorizontalLabels(points.length); // only 4 because of the space
        mGraph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        mGraph.getGridLabelRenderer().setTextSize(30);

        mGraph.getViewport().setXAxisBoundsManual(true);
    }

    private void setSpinnersAndListeners(View view){
        mXSpinner = (Spinner) view.findViewById(R.id.time_view_options_spinner);
        mYSpinner = (Spinner) view.findViewById(R.id.viewing_options_spinner);

        mXSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mYSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getData(){
        Map<String, Object> params = new HashMap<>();
        ParseStaffUser currentUser = ParseUtil.getCurrentUser();

        params.put("timeViewingOptions", "Week");
        params.put("objectId", currentUser.getObjectId());
        params.put("businessId", currentUser.getBusiness().getObjectId());

        ParseCloud.callFunctionInBackground("testFunction", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e == null){
                    object.toString();
                }else{
                    Log.e("Parse error", e.getMessage());
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     *
     * @return
     */
    private String getFormatter(){
        switch (xFormat){
            case "Week":
                return "E";
            case "Month":
                return "M";
            case "Year":
                return "y";
            default:
                throw new RuntimeException("Unknown formatter");
        }
    }

    @Override
    public void onClick(View v) {

    }
}
