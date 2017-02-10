package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * Represents a User object in Parse
 */
@ParseClassName("_User")
public class ParseStaffUser extends ParseUser {

    private ParseBusiness business;

    public ParseStaffUser(){

    }

    public void setPassword(String password) {
        put("password", password);
    }

    public String getBusinessName() {
        try {
            return getParseObject("Business").fetchIfNeeded().getString("Name");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    public String getFullName(){
        try {
            return fetchIfNeeded().getString("firstName") + " " + getString("lastName");
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }


    public void setBusiness(ParseBusiness business) {
        this.business = business;
    }

    public ParseBusiness getBusiness(){
        return this.business;
    }
}

