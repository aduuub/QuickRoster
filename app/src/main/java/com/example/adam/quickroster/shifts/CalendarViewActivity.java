package com.example.adam.quickroster.shifts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.quickroster.R;

/**
 * This shows a calendar, where the user can select a date, once they have done this it will transition
 * them to ShiftListView activity.
 */
public class CalendarViewActivity extends Fragment implements android.widget.CalendarView.OnDateChangeListener {

    private android.widget.CalendarView cal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_calendar_view, container, false);
        cal = (android.widget.CalendarView) view.findViewById(R.id.calendarView);
        cal.setOnDateChangeListener(this);
        return view;
    }

    @Override
    public void onSelectedDayChange(android.widget.CalendarView view, int year, int month, int dayOfMonth) {
        Intent intent = new Intent(getActivity(), ShiftListView.class);
        intent.putExtra("Day", dayOfMonth);
        intent.putExtra("Month", month);
        intent.putExtra("Year", year);
        startActivity(intent);
    }

}
