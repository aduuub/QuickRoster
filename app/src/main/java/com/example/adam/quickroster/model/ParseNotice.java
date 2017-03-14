package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Represents a 'Notice' object in the Parse.com database
 *
 * @author Adam Wareing
 */
@ParseClassName("Notice")
public class ParseNotice extends ParseObject{

    /**
     * Create a new ParseNotice
     */
    public ParseNotice(){
        // Requires empty constructor
    }


    /**
     * Gets the ParseNotice from its objectId
     *
     * @param objectId
     * @return
     */
    public static ParseNotice getNoticeFromId(String objectId){
        ParseQuery<ParseObject> query = new ParseQuery<>("Notice");
        try {
            return (ParseNotice) query.get(objectId);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Notice not found");
        }
    }


    /**
     * Gets all the ParseNotices belonging to the business
     *
     * @param business
     * @return
     */
    public static List<ParseNotice> getBusinessNotices(ParseObject business){
        ParseQuery<ParseObject> query = new ParseQuery<>("Notice");
        query.whereEqualTo("business", business);
        try {
            return (List<ParseNotice>)(Object) query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Notices not found");
        }
    }


    public String getMessage() {
        return getString("message");
    }

    public String getTitle() {
        return getString("title");
    }

    public void setBusiness(ParseObject business){
        put("business", business);
    }

    public void setTitle(String title){
        put("title", title);
    }

    public void setMessage(String message){
        put("message", message);
    }
}
