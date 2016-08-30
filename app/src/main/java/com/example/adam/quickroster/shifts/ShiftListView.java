package com.example.adam.quickroster.shifts;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.model.ParseShift;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a view that shows all the shifts on the selected day. It retrieves them from Parse
 */
public class ShiftListView extends AppCompatActivity {

    List<ParseShift> shifts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_date);
        ListView list = (ListView) findViewById(R.id.listView);
        shifts = new ArrayList();

        // get date
        int day = getIntent().getIntExtra("Day", 1);
        int month = getIntent().getIntExtra("Month", 0);
        int year = getIntent().getIntExtra("Year", 2016);

        // create date from day, month, year
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, day );
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // get selected date and next day's date
        Date startDateNoon = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date startDateMidnight = cal.getTime();

        // Get all shifts on this day
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Shift");
        query.whereEqualTo("staff", currentUser);
        query.whereGreaterThanOrEqualTo("startTime", startDateNoon);
        query.whereLessThanOrEqualTo("startTime", startDateMidnight);

        Set<ParseObject> shiftsOnDay = new HashSet<>();
        try {
            shiftsOnDay.addAll(query.find()); // get all the users shifts

            if (currentUser.getBoolean("isManager")) {
                ParseObject business = currentUser.getParseObject("Business").fetch();
                shiftsOnDay.addAll(ParseQueryUtil.getAllShifts(business, startDateNoon, startDateMidnight)); // add shifts of everyone in business
            }
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Error loading shifts", Toast.LENGTH_LONG).show();
            return;
        }

        castAllToParseShifts(shiftsOnDay);
        list.setAdapter(new ShiftListViewAdapter(this, shifts));
    }

    /**
     * Casts all ParseObject's to ParseShifts
     *
     * @param objects
     */
    public void castAllToParseShifts(Set<ParseObject> objects) {
        for (ParseObject obj : objects) {
            if (obj instanceof ParseShift) {
                shifts.add((ParseShift) obj);
            }
        }
    }
}
