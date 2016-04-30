package com.example.adam.quickroster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class ViewDateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_date);

        ListView list = (ListView) findViewById(R.id.listView);
        User user = getIntent().getParcelableExtra("User");
        long time = getIntent().getLongExtra("Time", 0);

        if(user == null || user.getShiftsOnDay(time).isEmpty())
            return;

        ArrayList<Shift> shifts = user.getShiftsOnDay(time);
        list.setAdapter(new CalendarListViewAdapter(this, shifts));


    }
}
