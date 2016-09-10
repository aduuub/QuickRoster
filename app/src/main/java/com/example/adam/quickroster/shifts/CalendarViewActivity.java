package com.example.adam.quickroster.shifts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.adam.quickroster.R;

/**
 * This shows a calendar, where the user can select a date, once they have done this it will transition
 * them to ShiftListView activity.
 */
public class CalendarViewActivity extends AppCompatActivity implements android.widget.CalendarView.OnDateChangeListener {

    private android.widget.CalendarView cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cal = (android.widget.CalendarView) findViewById(R.id.calendarView);
        cal.setOnDateChangeListener(this);
        
    }

    @Override
    public void onSelectedDayChange(android.widget.CalendarView view, int year, int month, int dayOfMonth) {
        Intent intent = new Intent(CalendarViewActivity.this, ShiftListView.class);
        intent.putExtra("Day", dayOfMonth);
        intent.putExtra("Month", month);
        intent.putExtra("Year", year);
        startActivity(intent);
    }
}
