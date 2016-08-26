package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Adam on 19/05/16.
 */
@ParseClassName("Shift")
public class ParseShift extends ParseObject {

    Date startDate;
    Date endDate;
    String details;

    public String getObjectID() {
        return getString("objectID");
    }

    public Date getStartDate() {
        return getDate("startTime");
    }

    public void setStartDate(Date startDate) {
        put("startTime", startDate);
    }

    public Date getEndDate() {
        return getDate("endTime");
    }

    public void setEndDate(Date endDate) {
        put("endTime", endDate);
    }

    public String getDetails() {
        return getString("details");
    }

    public void setDetails(String details) {
        put("details", details);
    }

    public void setStaff(ParseStaffUser user){
        put("staff", user.getObjectID());
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
