package com.example.adam.quickroster;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StatsFragment extends Fragment implements View.OnClickListener {

    private GraphView mGraph;
    private String xFormat; // week, month, year

    public StatsFragment() {
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        xFormat = "week";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Statistics");
        View view = inflater.inflate(R.layout.activity_stats, container, false);

        mGraph = (GraphView) view.findViewById(R.id.statistics_graph);
        setGraph();
        return view;
    }

    private void setGraph(){
        DataPoint[] points = new DataPoint[10];
        for(int i=0; i < 10; i++){
            points[i] = new DataPoint(i, i*2);
        }
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

        // graph.getGridLabelRenderer().setNumHorizontalLabels(Math.min(8, points.length)); // only 4 because of the space
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(points.length); // only 4 because of the space
        mGraph.getGridLabelRenderer().setHorizontalLabelsAngle(90);
        mGraph.getGridLabelRenderer().setTextSize(30);

        // set manual x bounds to have nice steps
//        mGraph.getViewport().setMinX(min_d);
//        mGraph.getViewport().setMaxX(max_d+1);
        mGraph.getViewport().setXAxisBoundsManual(true);
    }


    private String getFormatter(){
        switch (xFormat){
            case "week":
                return "E";
            case "month":
                return "M";
            case "year":
                return "y";
            default:
                throw new RuntimeException("Unknown formatter");

        }
    }

    @Override
    public void onClick(View v) {

    }
}
