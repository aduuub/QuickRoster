package com.example.adam.quickroster.misc;

import android.app.Activity;
import android.os.AsyncTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Miscellaneous static methods. This class should not be initialised.
 * Currently it checks that the password is valid, and has a class that updates the users shifts in the background
 *
 * @author Adam Wareing
 */
public class Util {

    public static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("E  dd MMM");

    /**
     * Cant create an instance of this class.
     */
    private Util() {
        // Can't be initialised
    }


    /**
     * Checks to see if the password is valid. Checks that it is longer than 6 characters and contains one upper case character.
     *
     * @param password - null if valid password. Otherwise it will be an error message explaining whats wrong with it.
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


    /**
     * A class used to update the users shifts in the background.
     *
     */
    public static class UpdateShifts extends AsyncTask<Activity, Void, Void> {
        @Override
        public Void doInBackground(Activity... params) {
            CalendarShiftController csc = new CalendarShiftController(params[0], params[0].getApplicationContext());
            csc.addAllNewShifts();
            return null;
        }
    }

}
