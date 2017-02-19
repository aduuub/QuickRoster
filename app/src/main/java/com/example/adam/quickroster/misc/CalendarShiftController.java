package com.example.adam.quickroster.misc;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.security.Permission;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import static java.security.AccessController.getContext;

/**
 * Created by Adam on 6/09/16.
 */
public class CalendarShiftController {

    private static final String DEBUG_TAG = "CalendarActivity";
    private Activity activityObj;
    private Context context;
    String eventUriString = "content://com.android.calendar/events";

    public CalendarShiftController(Activity actObj, Context context) {
        this.activityObj = actObj;
        this.context = context;
    }

    public void makeNewEntry(String title, String description, String location, long startTime, long endTime) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        if(description.equals(""))
            values.put(CalendarContract.Events.TITLE, "Work");
        else
            values.put(CalendarContract.Events.TITLE, "Work: " + description);

        //values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        // Get current timezone
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Log.i(DEBUG_TAG, "Timezone retrieved: " + TimeZone.getDefault().getID());

        // Check permission
        int permissionCheck = ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            activityObj.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), values);
        }
    }


    /**
     * Adds the new shifts to the users calendar
     */
    public void addNewShiftsToCalendar(Context ctx) {
        // remove two days from last updated date and find all the updated shifts
        Date lastUpdated = getCalLastUpdated();
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastUpdated);
        cal.add(Calendar.DATE, -2);
        lastUpdated = cal.getTime();
        List<ParseObject> usersShifts;
        try {
            usersShifts = ParseQueryUtil.getAllUsersShiftsAfterDate(ParseUser.getCurrentUser(), lastUpdated);
        } catch (ParseException e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);
            return;
        }

        // Put updated edit time
        setCalLastUpdatedNow();

        for (ParseObject shift : usersShifts) {
            String details = shift.getString("details");
            Date start = shift.getDate("startTime");
            Date end = shift.getDate("endTime");
            makeNewEntry("Work", details, "Default Location", start.getTime(), end.getTime());
        }
    }

    /**
     * Sets the field 'calendarLastSync' in the shared preferences to be the current time
     */
    public void setCalLastUpdatedNow() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        SharedPreferences sharedPref = activityObj.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("calendarLastSync", currentDate);
        editor.commit();
    }


    /**
     *
     * @return
     */
    public Date getCalLastUpdated() {
        SharedPreferences sharedPref = activityObj.getPreferences(Context.MODE_PRIVATE);
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        String dateString = sharedPref.getString(activityObj.getString(R.string.cal_last_synced_at), currentDate);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return dateFormatter.parse(dateString);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


}

