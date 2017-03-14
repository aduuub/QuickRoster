package com.example.adam.quickroster.misc;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseShift;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * This adds the latest shifts to the users device. It stores when the shifts were last updated in a string in shared preferences. When new shifts are
 * added it updates the time, and adds the new shifts to the users calendar.\
 *
 * @author Adam Wareing
 */
class CalendarShiftController {

    private final Activity activityObj;
    private final Context context;

    private static final String DEBUG_TAG = "CalendarActivity";
    private final String EVENT_URI_STRING = "content://com.android.calendar/events";

    /**
     * Creates a new instance
     * @param actObj
     * @param context
     */
    public CalendarShiftController(Activity actObj, Context context) {
        this.activityObj = actObj;
        this.context = context;
    }


    /**
     * Adds the new shifts to the users calendar
     */
    public void addAllNewShifts() {
        // Remove two days from last updated date and find all the updated shifts
        Date lastUpdated = getDateCalendarLastUpdated();
        Calendar cal = Calendar.getInstance();
        cal.setTime(lastUpdated);
        cal.add(Calendar.DATE, -2);
        lastUpdated = cal.getTime();
        List<ParseObject> usersShifts;
        try {
            ParseUser.getCurrentUser().save();
            usersShifts = ParseQueryUtil.getShiftsModifiedAfter(ParseUser.getCurrentUser(), lastUpdated);
        } catch (ParseException e) {
            Log.e("Parse Error", e.getMessage());
            return;
        }

        // Put updated edit time
        setCalendarUpdatedNow();

        // Update new shifts
        for (ParseObject shift : usersShifts) {
            addNewShiftToCalendar((ParseShift)shift);
        }
    }


    /**
     * Adds a parse shift to the users local calendar.
     *
     * Sets the start and end date as well as ther details.
     * @param shift
     */
    private void addNewShiftToCalendar(ParseShift shift) {
        ContentValues values = new ContentValues();
        values.put(CalendarContract.Events.DTSTART, shift.getStartDate().getTime());
        values.put(CalendarContract.Events.DTEND, shift.getEndDate().getTime());
        values.put(CalendarContract.Events.TITLE, "Work: " + shift.getDetails());
        values.put(CalendarContract.Events.CALENDAR_ID, 1);

        // Get current timezone
        values.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());
        Log.i(DEBUG_TAG, "Timezone retrieved: " + TimeZone.getDefault().getID());

        // Check permission
        int permissionCheck = ContextCompat.checkSelfPermission(this.context,
                Manifest.permission.WRITE_CALENDAR);

        // Add event to calendar
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            activityObj.getApplicationContext()
                    .getContentResolver()
                    .insert(Uri.parse(EVENT_URI_STRING), values);
        }
    }


    /**
     * Sets <code>calendarLastSync</code> in the shared preferences to be the current time.
     */
    private void setCalendarUpdatedNow() {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
        SharedPreferences sharedPref = activityObj.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("calendarLastSync", currentDate);
        editor.apply();
    }


    /**
     * Gets the time that the calendar was last updated. Retrieves from shared preferences.
     *
     * @return - Date/time the calendar was last updated
     */
    private Date getDateCalendarLastUpdated() {
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

