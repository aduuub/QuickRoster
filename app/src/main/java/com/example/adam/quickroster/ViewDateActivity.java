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
        int day = getIntent().getIntExtra("Day", 0);
        int month = getIntent().getIntExtra("Month", 0);
        int year = getIntent().getIntExtra("Year", 0);

        if(user == null)
            return;

        ArrayList<Shift> shifts = user.getShiftsOnDay(day, month, year);

        if(user.isManager()){
            for(User u : Home.getAllUsers()){
                for(Shift s : u.getShiftsOnDay(day, month, year))
                    shifts.add(s);
            }
        }

        list.setAdapter(new CalendarListViewAdapter(this, shifts));


    }
}
