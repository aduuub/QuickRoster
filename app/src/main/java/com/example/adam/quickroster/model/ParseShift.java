package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Represents a Shift object in Parse
 */
@ParseClassName("Shift")
public class ParseShift extends ParseObject {

    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("E, dd MMM, yyyy");


    public ParseShift(){
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

    public ParseUser getStaff(){
        ParseUser staff = null;
        try {
            staff = fetchIfNeeded().getParseUser("staff");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return staff;
    }

    public String getFormattedDate(){
        Date date = getDate("startTime");
        return DATE_FORMATTER.format(date);
    }
}
