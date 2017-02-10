package com.example.adam.quickroster.model;

import com.parse.Parse;
import com.parse.ParseClassName;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

/**
 * Created by Adam on 6/02/17.
 */

@ParseClassName("Notice")
public class ParseNotice extends ParseObject{
    public ParseNotice(){

    }

    public static ParseNotice getNoticeFromID(String id){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Notice");
        try {
            return (ParseNotice) query.get(id);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Notice not found");
    }

    public static List<ParseNotice> getBusinessNotices(ParseObject business){
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Notice");
        query.whereEqualTo("business", business);
        try {
            return (List<ParseNotice>)(Object) query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Notices not found");
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
