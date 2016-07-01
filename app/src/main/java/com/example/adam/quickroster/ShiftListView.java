package com.example.adam.quickroster;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ActionMode;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.GetCallback;
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
        int day = getIntent().getIntExtra("Day", 1);
        int month = getIntent().getIntExtra("Month", 0);
        int year = getIntent().getIntExtra("Year", 2016);

        // create date from day, month, year
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // get selected date and next day's date
        Date startDate = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date endDate = cal.getTime();

        // Get all shifts on this day
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Shift");
        query.whereEqualTo("staff", currentUser);
        query.whereGreaterThanOrEqualTo("startTime", startDate);
        query.whereLessThanOrEqualTo("endTime", endDate);

        List<ParseObject> shiftsOnDay = null;
        try {
            shiftsOnDay = query.find(); // get all the users shifts

            if (currentUser.getBoolean("isManager")) {
                ParseObject business = currentUser.getParseObject("Business").fetch();
                shiftsOnDay.addAll(getAllShifts(business)); // add shifts of everyone in business
            }
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Error loading shifts", Toast.LENGTH_LONG).show();
            return;
        }

        convertAllToParseShift(shiftsOnDay);
        list.setAdapter(new ShiftListViewAdapter(this, shifts));
    }

    /**
     * Terrible way of doing it, but it casts all ParseObject's to ParseShifts
     *
     * @param objects
     */
    public void convertAllToParseShift(List<ParseObject> objects) {
        for (ParseObject obj : objects) {
            if (obj instanceof ParseShift) {
                shifts.add((ParseShift) obj);
            }
        }
    }

    /**
     * Retrieves all shifts of all employees from the business
     *
     * @param business
     * @return List of ParseObject, which are ParseShifts
     * @throws ParseException
     */
    public static List<ParseObject> getAllShifts(ParseObject business) throws ParseException {
        // Query to get all users of the business
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        queryUsers.whereEqualTo("Business", business);
        List<ParseUser> users = queryUsers.find();

        // Get all shifts from the users
        ParseQuery<ParseObject> queryShifts = new ParseQuery<ParseObject>("Shift");
        queryShifts.whereContainedIn("staff", users);
        return queryShifts.find();
    }
}
