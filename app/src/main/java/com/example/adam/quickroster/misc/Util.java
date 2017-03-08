package com.example.adam.quickroster.misc;

import android.app.Activity;
import android.os.AsyncTask;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Adam on 22/02/17.
 */

public class Util {

    public static Calendar getCurrentDayCalendar(){
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }

    /**
     * Checks to see if the password is valid. Checks it is longer than 6 chars and contains one
     * upper case character.
     * @param password null = valid password. Else the error message explaining whats wrong with it.
     * @return
     */
    public static String isPasswordValid(String password){
        if(password.length() < 6)
            return "Password is too short. It must be at least 6 characters.";

        boolean containsUpper = false;
        for(int i=0; i<password.length(); i++){
            if(Character.isUpperCase(password.charAt(i)))
                containsUpper = true;
        }
        if(!containsUpper)
            return "Password must contain at least one upper case letter.";

        // Must be ok
        return null;
    }


    public static class UpdateShifts extends AsyncTask<Activity, Void, Void> {

        @Override
        public Void doInBackground(Activity... params) {
            CalendarShiftController csc = new CalendarShiftController(params[0], params[0].getApplicationContext());
            csc.addNewShiftsToCalendar();
            return null;
        }
    }
}
