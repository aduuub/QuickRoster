package com.example.adam.quickroster;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.UUID;

/**
 * Created by Adam on 19/05/16.
 */
@ParseClassName("User")
public class ParseStaffUser extends ParseUser {

    public String getObjectID() {
        return getString("objectID");
    }

    public String getFirstName() {
        return getString("firstName");
    }

    public void setFirstName(String name) {
        put("firstName", name);
    }

    public void setBusinessID(String name) {
        put("Business", name);
    }

    public String getLastName() {
        return getString("lastName");
    }

    public void setLastName(String name) {
        put("lastName", name);
    }

    public String getUserName() {
        return getString("username");
    }

    public void setUserName(String name) {
        put("username", name);
    }

    public void setPassword(String password) {
        put("password", password);
    }

    public boolean isManager() {
        return getBoolean("isManager");
    }

    public void setIsManager(boolean isManager) {
        put("isManager", isManager);
    }

    public void setUuidString() {
        UUID uuid = UUID.randomUUID();
        put("uuid", uuid.toString());
    }

    public String getUuidString() {
        return getString("uuid");
    }
}

