package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * Represents a User object in Parse
 */
@ParseClassName("_User")
public class ParseStaffUser extends ParseUser {

    private ParseBusiness business;
    private boolean manager;

    public ParseStaffUser() {
        // Requires empty constructor
    }

    // Setters

    public void setLocalManager(boolean manager) {this.manager = manager; }

    public void setFirstName(String name) { put("firstName", name);}
    public void setLastName(String name) { put("lastName", name);}
    public void setManager(boolean isManager) { put("isManager", isManager);}
    public void setBusiness(ParseObject business) {put("Business", business); }

    public void setPassword(String password) {
        put("password", password);
        saveInBackground();
    }

    public void setHourlyWage(String wage) {
        put("wage", wage);
        saveEventually();
    }


    public void setAutoAddToCalendar(boolean autoAdd) {
         put("autoAddToCal", autoAdd);
        saveEventually();
    }


    // Getters

    public boolean isManager() {
        return manager;
    }
    public String getHourlyWage() {
        return getString("wage");
    }
    public ParseBusiness getBusiness() { return (ParseBusiness) getParseObject("business"); }
    public boolean getAutoAddToCalendar() {
        return getBoolean("autoAddToCal");
    }


    public String getBusinessName() {
        try {
            return getParseObject("Business").fetchIfNeeded().getString("Name");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFullName() {
        try {
            return fetchIfNeeded().getString("firstName") + " " + getString("lastName");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public static ParseUser getUserFromId(String id){
        ParseQuery<ParseUser> query = new ParseQuery<>("_User");
        try {
            return query.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("User not found");
    }


    public String getMobileNumber() {
        return getString("mobileNumber");
    }
}

