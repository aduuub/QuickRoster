package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * Represents a User object in Parse
 */
@ParseClassName("User")
public class ParseStaffUser extends ParseUser {

    public String getObjectID() {
        return getString("objectID");
    }

    public void setPassword(String password) {
        put("password", password);
    }

}

