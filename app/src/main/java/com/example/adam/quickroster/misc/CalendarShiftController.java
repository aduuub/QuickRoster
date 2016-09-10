package com.example.adam.quickroster.misc;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.security.Permission;
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


        ContentResolver cr = activityObj.getContentResolver();
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, startTime);
        values.put(CalendarContract.Events.DTEND, endTime);
        values.put(CalendarContract.Events.TITLE, title);
        values.put(CalendarContract.Events.DESCRIPTION, description);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        // Get current timezone
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Log.i(DEBUG_TAG, "Timezone retrieved: " + TimeZone.getDefault().getID());

        // Check permission
        int permissionCheck = ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Uri eventUri = activityObj.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), values);
            Toast.makeText(this.context, "Successfully updated new shifts in calendar", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this.context, "Invalid permission to write to calendar", Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Adds the new shifts to the users calendar
     */
    public void addNewShiftsToCalendar(Context ctx) {
        List<ParseObject> usersShifts;
        try {
            usersShifts = ParseQueryUtil.getAllUsersShiftsAfterDate(ParseUser.getCurrentUser(), new Date(1));
        } catch (ParseException e) {
            Toast.makeText(ctx, e.getMessage(), Toast.LENGTH_LONG);
            return;
        }

        for (ParseObject shift : usersShifts) {
            String details = shift.getString("details");
            Date start = shift.getDate("startTime");
            Date end = shift.getDate("endTime");
            makeNewEntry("Work", details, "Default Location", start.getTime(), end.getTime());
        }
    }
}

