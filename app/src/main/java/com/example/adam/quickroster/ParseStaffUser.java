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
public class ParseStaffUser extends ParseObject {

    public String getObjectID() {
        return getString("objectID");
    }

    public String getName() {
        return getString("firstName");
    }

    public void setName(String name) {
        put("firstName", name);
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

    // TODO get / set shifts

    public static ParseQuery<ParseStaffUser> getQuery() {
        return ParseQuery.getQuery(ParseStaffUser.class);
    }
}

