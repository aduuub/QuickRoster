package com.example.adam.quickroster.model;

import com.example.adam.quickroster.misc.Util;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Represents a 'Shift' object in the Parse.com database
 *
 * @author Adam Wareing
 */
@ParseClassName("Shift")
public class ParseShift extends ParseObject {

    /**
     * Create a new ParseShift
     */
    public ParseShift(){
        // Requires empty constructor
    }


    /**
     * Gets the ParseUser associated with this shift.
     *
     * @return
     */
    public ParseUser getStaff(){
        ParseUser staff = null;
        try {
            staff = fetchIfNeeded().getParseUser("staff");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return staff;
    }


    /**
     * Gets the formatted start time of the shift.
     *
     * E.g. "Mon, 12 Mar, 2017
     * @return
     */
    public String getFormattedDate(){
        Date date = getDate("startTime");
        return Util.DATE_FORMATTER.format(date);
    }

    public Date getStartDate() {
        return getDate("startTime");
    }

    public Date getEndDate() {
        return getDate("endTime");
    }

    public String getDetails() {
        return getString("details");
    }

    public void setStaff(ParseStaffUser user) {
        put("staff", user.getObjectId());
    }
}
