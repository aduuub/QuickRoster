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

import java.security.Permission;
import java.util.TimeZone;

import static java.security.AccessController.getContext;

/**
 * Created by Adam on 6/09/16.
 */
public class CalendarShiftController {

    //Remember to initialize this activityObj first, by calling initActivityObj(this) from
    //your activity
    
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

        // maybe need to do this? initActivityObj(this)
        // Check permission
        int permissionCheck = ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.WRITE_CALENDAR);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Uri eventUri = activityObj.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(eventUriString), values);
            //Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            //Log.i(DEBUG_TAG, "Uri returned: " + uri.toString());

            // get the event ID that is the last element in the Uri
           // long eventID = Long.parseLong(uri.getLastPathSegment());

        }else{
            Toast.makeText(this.context, "Invalid permission to write to calendar", Toast.LENGTH_LONG).show();
        }



    }
}
