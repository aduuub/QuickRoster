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

/**
 * This fragment displays statistics for the employee.
 *
 * @author Adam Wareing
 */
public class StatsFragment extends Fragment {

    private GraphView mGraph;

    private String timeViewingOptions = "Week"; // Week, Month, Year
    private String viewingOptions = "Pay"; // Count, Hours, Pay

    private final int MILLIS_TO_HOURS = (1000 * 60 * 60);
    private final long ONE_DAY_OFFSET = (24 * 60 * 60 * 1000);
    private final long ONE_WEEK_OFFSET = ONE_DAY_OFFSET * 7; // 7 Days


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

        params.put("yAxis", viewingOptions); // Hour, Count\
        params.put("xAxis", timeViewingOptions); // Hour, Count
        params.put("objectId", currentUser.getObjectId());
        params.put("businessId", currentUser.getBusiness().getObjectId());
        ParseCloud.callFunctionInBackground(("ShiftSummary"), params, new FunctionCallback<List<Integer>>() {
            @Override
            public void done(List<Integer> object, ParseException e) {
                if (e == null) {
                    // Convert formatting and update the graph
                    List<Double> data = new ArrayList<>();
                    for (Integer i : object) {
                        data.add(i.doubleValue());
                    }
                    data = convertFormatting(data);
                    setGraph(data);
                } else {
                    Log.e("Parse error", e.getMessage());
                    e.printStackTrace();
                    throw new RuntimeException("Error " + e.getMessage());
                }
            }
        });
    }


    /**
     * Converts the formatting for the y values of the data. If the current <code> viewingOptions </code> is 'Pay' it converts
     * it by multiplying it by the staffs hourly rate. If the current viewOptions is hours we need
     * to convert it to hours from millis, otherwise we don't need to do anything to it.
     */
    private List<Double> convertFormatting(List<Double> data) {
        if (viewingOptions.equals("Pay")) {
            // We need to multiply hours worked by the users hourly rate
            String wage = ((ParseStaffUser) ParseUser.getCurrentUser()).getHourlyWage();

            // Check errors
            if (wage == null || wage.equals("0")) {
                displayInputAlert();
            }
            double pay = Double.valueOf(wage);
            for (int i = 0; i < data.size(); i++) {
                double hours = data.get(i) / MILLIS_TO_HOURS; // convert to hours
                hours *= pay; // calculate hourly rate
                data.set(i, hours);
            }

        } else if (viewingOptions.equals("Hours")) {
            // Need to convert it to hours
            for (int i = 0; i < data.size(); i++) {
                double hours = data.get(i) / MILLIS_TO_HOURS; // convert to hours
                data.set(i, hours);
            }
        }
        return data;
    }


    /**
     * Displays the graph with the provided values.
     *
     * Coverts the data to DataPoints, removes the old series and displays the new one. It also formats the x-axis appropriately.
     * Week - 7 data points (each day)
     * Month - 4 data points (each week- 28 day month)
     * Year - 12 data points (each month - 28 day month)
     *
     * @param data - Data for 'Hours', 'Shift', or 'Pay'. This is displayed along the y-axis. The x-axis data is calculate automatically based on
     *             <code> timeViewingOptions </code>
     */
    private void setGraph(List<Double> data) {
        DataPoint[] points = new DataPoint[data.size()];
        for (int i = 0; i < data.size(); i++) {
            points[i] = new DataPoint(i, data.get(i));
        }

        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
        // remove old series and add new one
        mGraph.removeAllSeries();
        mGraph.addSeries(series);

        mGraph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter() {
            @Override
            public String formatLabel(double value, boolean isValueX) {
                if (isValueX) {
                    switch (timeViewingOptions) {
                        case "Week":
                            return getWeekFormatter(value);

                        case "Month":
                            return getMonthFormatter(value);

                        case "Year":
                            return getYearFormatter(value);
                    }

                    return super.formatLabel(value, isValueX);

                } else {
                    String yLabel = super.formatLabel(value, isValueX);
                    if (viewingOptions.equals("Pay")) {
                        yLabel = "$" + yLabel;
                    }
                    return yLabel;
                }
            }
        });

        mGraph.getGridLabelRenderer().setNumHorizontalLabels(Math.min(points.length, 6));
        mGraph.getGridLabelRenderer().setTextSize(40);
    }


    /***
     * Get the formatter for the week. 0..6 (0 being today, 6 being 7 days ago)
     * @param xValue
     * @return - the day formatted e.g. "Mon"
     */
    private String getWeekFormatter(double xValue) {
        final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("E");
        Date date = new Date();
        int daySubtraction = (int) (6 - xValue);
        return dateTimeFormatter.format(date.getTime() - (ONE_DAY_OFFSET * daySubtraction));
    }


    /***
     * Get the formatter for the month. 0..3 (0 being this week, 3 being 4 weeks ago)
     * @param xValue
     * @return - the day formatted e.g. "Week 12"
     */
    private String getMonthFormatter(double xValue) {
        final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("w");
        Date date = new Date();
        int daySubtraction = (int) (3 - xValue);
        return "Week " + dateTimeFormatter.format(date.getTime() - (ONE_WEEK_OFFSET * daySubtraction));
    }

    /***
     * Get the formatter for the year. 0..11 (0 being this month, 11 being 12 months ago)
     * @param xValue
     * @return - the day formatted e.g. "Jan"
     */
    private String getYearFormatter(double xValue) {
        final SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MMM");
        Date date = new Date();
        int daySubtraction = (int) (12 - xValue);
        long ONE_MONTH_OFFSET = ONE_WEEK_OFFSET * 4;
        return dateTimeFormatter.format(date.getTime() - (ONE_MONTH_OFFSET * daySubtraction));
    }


    /**
     * Alerts the user that they need to set there hourly rate under Account in order to display information on there pay.
     */
    private void displayInputAlert() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(getString(R.string.invalid_input_title));
        alertDialog.setMessage(getString(R.string.hourly_rate_not_set_message));
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
