package com.example.adam.quickroster.shifts;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.Util;
import com.parse.ParseUser;

import java.util.Calendar;

/**
 * This shows a calendar, where the user can select a date, once they have done this it will transition
 * them to ShiftView activity.
 */
public class CalendarViewActivity extends Fragment implements android.widget.CalendarView.OnDateChangeListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_calendar_view, container, false);
        android.widget.CalendarView cal = (android.widget.CalendarView) view.findViewById(R.id.calendarView);
        cal.setOnDateChangeListener(this);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Calendar");
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            inflater.inflate(R.menu.add_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_add) {
            Intent intentAddStaff = new Intent(getActivity(), AddShiftActivity.class);
            startActivity(intentAddStaff);

        }else {
            // Menu not found
            return false;
        }

        return true;
    }


    @Override
    public void onSelectedDayChange(@NonNull android.widget.CalendarView view, int year, int month, int dayOfMonth) {
        Intent intent = new Intent(getActivity(), ShiftView.class);
        intent.putExtra("Day", dayOfMonth);
        intent.putExtra("Month", month);
        intent.putExtra("Year", year);
        startActivity(intent);
    }

}
