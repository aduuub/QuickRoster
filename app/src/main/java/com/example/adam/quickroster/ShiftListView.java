package com.example.adam.quickroster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ShiftListView extends AppCompatActivity {

    List<ParseShift> shifts;
    private final double MillisInDay = 8.64e+7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_date);
        ListView list = (ListView) findViewById(R.id.listView);
        shifts = new ArrayList();

        // get date
        int day = getIntent().getIntExtra("Day", 0);
        int month = getIntent().getIntExtra("Month", 0);
        int year = getIntent().getIntExtra("Year", 0);

        // create date from day, month, year
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        Date startDate = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date endDate = cal.getTime();

        // Get all shifts on this day
        ParseUser currentUser = ParseUser.getCurrentUser();
        String userObj = currentUser.getObjectId();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Shift");
        query.whereEqualTo("staff", currentUser);
        query.whereGreaterThanOrEqualTo("startTime", startDate);
        query.whereLessThanOrEqualTo("endTime", endDate);

        List<ParseObject> shiftsOnDay = null;
        try {
            shiftsOnDay = query.find();
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Error loading shifts", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO add all shifts for manager
        addShift(shiftsOnDay);
        list.setAdapter(new ShiftListViewAdapter(this, shifts));
    }

    public void addShift(List<ParseObject> objects) {
        for (ParseObject obj : objects) {
            if (obj instanceof ParseShift) {
                shifts.add((ParseShift) obj);
            }
        }
    }
}
