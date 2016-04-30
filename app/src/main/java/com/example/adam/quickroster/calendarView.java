package com.example.adam.quickroster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CalendarView;

import java.util.Date;

public class calendarView extends AppCompatActivity implements CalendarView.OnDateChangeListener {

    private CalendarView cal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        cal = (CalendarView) findViewById(R.id.calendarView);
        cal.setOnDateChangeListener(this);

    }


    @Override
    public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
        Date d = new Date(year, month, dayOfMonth);
        long time = d.getTime();

        Intent intent = new Intent(calendarView.this, ViewDateActivity.class);
        User user = getIntent().getParcelableExtra("User");

        intent.putExtra("Time", time);
        intent.putExtra("User", user);

        startActivity(intent);

    }
}
