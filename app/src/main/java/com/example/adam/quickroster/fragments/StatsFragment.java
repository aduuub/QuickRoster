package com.example.adam.quickroster.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StatsFragment extends Fragment {

    private GraphView mGraph;

    private String timeViewingOptions = "Week"; // Week, Month, Year
    private String viewingOptions = "Pay"; // Count, Hours, Pay

    private final long ONE_DAY_OFFSET = (24 * 60 * 60 * 1000);
    private final long ONE_WEEK_OFFSET = ONE_DAY_OFFSET * 7; // 7 Days
    private final long ONE_MONTH_OFFSET = ONE_WEEK_OFFSET * 4; // 28 Days

    private final int MILLIS_TO_HOURS = (1000 * 60 * 60);


    public StatsFragment() {
        // Requires empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Statistics");
        View view = inflater.inflate(R.layout.activity_stats, container, false);

        mGraph = (GraphView) view.findViewById(R.id.statistics_graph);
        setSpinnersAndListeners(view);
        return view;
    }


    /**
     * Gets the appropriate data from the Parse Cloud Code. It finds all the users shifts worked/ hours
     * and then calls convertFormatting(..) and setGraph(..) to show the new data.
     */
    private void getDataAndUpdateGraph() {
        Map<String, Object> params = new HashMap<>();
        ParseStaffUser currentUser = (ParseStaffUser) ParseUser.getCurrentUser();

        params.put("yAxis", viewingOptions); // Hour, Count
        params.put("objectId", currentUser.getObjectId());
        params.put("businessId", currentUser.getBusiness().getObjectId());
        ParseCloud.callFunctionInBackground((timeViewingOptions+ "ShiftSummary"), params, new FunctionCallback<List<Integer>>() {
            @Override
            public void done(List<Integer> object, ParseException e) {
                if (e == null) {
                    // Convert formatting and update the graph
                    List<Double> data = new ArrayList<>();
                    for (Integer i : object) {
                        data.add(i.doubleValue());
                    }
                    updateGraph(data);
                } else {
                    Log.e("Parse error", e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error " + e.getMessage());
                }
            }
        });
    }

    private void updateGraph(List<Double> data) {
        data = convertFormatting(data);
        setGraph(data);
    }


    /**
     * Converts the formatting for the yAxis. If the current viewingOptions is pay we need to convert
     * it my multiplying by the staffs hourly rate. If the current viewingOptions is hours we need
     * to convert it to hours from millis, otherwise we don't need to do anything to it.
     *
     * @param data
     * @return
     */
    private List<Double> convertFormatting(List<Double> data) {
        if (viewingOptions.equals("Pay")) {
            // We need to multiply hours worked by the users hourly rate
            String wage = ((ParseStaffUser) ParseUser.getCurrentUser()).getHourlyWage();

            // Check errors
            if (wage == null || wage.equals("0")) {
                displayInputAlert("You need to set your pay in Account to use this feature");
            }
            double pay = Double.valueOf(wage);
            for (int i = 0; i < data.size(); i++) {
                double hours = data.get(i).doubleValue() / MILLIS_TO_HOURS; // convert to hours
                hours *= pay; // calculate hourly rate
                data.set(i, hours);
            }
        } else if (viewingOptions.equals("Hours")) {
            for (int i = 0; i < data.size(); i++) {
                double hours = data.get(i).doubleValue() / MILLIS_TO_HOURS; // convert to hours
                data.set(i, hours);
            }
        }
        return data;
    }


    /**
     * Displays the graph with the set values.
     *
     * @param data list of hours or shifts worked.
     */
    private void setGraph(List<Double> data) {
        long offset = getTimeOffset();

        DataPoint[] points = new DataPoint[data.size()];
        Date date = new Date();
        date.setHours(0);
        date.setMinutes(0);
        date.setSeconds(0);
        date.setTime(date.getTime() + offset);

        for (int i = data.size() - 1; i >= 0; i--) {
            date.setTime(date.getTime() - offset);
            long time = date.getTime();
            double value = data.get(i);
            points[i] = new DataPoint(i, value);
            double x = points[i].getX();
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        // remove old series and add new one
        mGraph.removeAllSeries();
        mGraph.addSeries(series);

        // set date label formatter
        final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat(getFormatter());

        String[] dates = new String[points.length];
        for (int i = data.size() - 1; i >= 0; i--) {
         dates[i] = dateTimeFormatter.format((long)points[i].getX());
        }

        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    String formattedLabel = dateTimeFormatter.format(new Date((long) value));
                    if(timeViewingOptions.equals("Month")){
                        formattedLabel = "Week " + formattedLabel;
                    }
                    return formattedLabel;

                } else {
                    return super.formatLabel(value, isValueX);
                }
            }
        });

        //mGraph.getGridLabelRenderer().setHumanRounding(true);
        mGraph.getGridLabelRenderer().setNumHorizontalLabels(points.length);
        mGraph.getGridLabelRenderer().setTextSize(40);
       // mGraph.getGridLabelRenderer().setLabelsSpace(20);

        //mGraph.getViewport().setXAxisBoundsManual(true);
    }

    private long getTimeOffset(){
        switch (timeViewingOptions) {
            case "Week":
                return ONE_DAY_OFFSET;
            case "Month":
                return ONE_WEEK_OFFSET;
            case "Year":
                return ONE_MONTH_OFFSET;
            default:
                throw new RuntimeException("Unknown formatter");
        }
    }


    /**
     * Returns a formatter for the xAxis. Based on the current timeViewingOptions.
     *
     * @return
     */
    private String getFormatter() {
        switch (timeViewingOptions) {
            case "Week":
                return "E";
            case "Month":
                return "m";
            case "Year":
                return "MMM";
            default:
                throw new RuntimeException("Unknown formatter");
        }
    }

    /**
     * Alerts the user that the input is invalid.
     *
     * @param message
     */
    private void displayInputAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.invalid_input_title));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    /**
     * Sets the spinners for the time viewing options and viewing options. Also handles listening
     * for a click to happen
     *
     * @param view
     */
    private void setSpinnersAndListeners(View view) {
        Spinner mTimeViewingOptionsSpinner = (Spinner) view.findViewById(R.id.time_view_options_spinner);
        Spinner mViewingOptionsSpinner = (Spinner) view.findViewById(R.id.viewing_options_spinner);

        mTimeViewingOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        timeViewingOptions = "Week";
                        break;
                    case 1:
                        timeViewingOptions = "Month";
                        break;
                    case 2:
                        timeViewingOptions = "Year";
                        break;
                }
                getDataAndUpdateGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mViewingOptionsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        viewingOptions = "Hours";
                        break;
                    case 1:
                        viewingOptions = "Pay";
                        break;
                    case 2:
                        viewingOptions = "Shifts";
                        break;
                }
                getDataAndUpdateGraph();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }
}
