package com.example.adam.quickroster.misc;

import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * Created by Adam on 6/02/17.
 */

public class ParseUtil {

    private static ParseUtil self;
    private static ParseStaffUser currentUser;

    private ParseUtil(){
        currentUser = (ParseStaffUser) ParseUser.getCurrentUser();

        // Set users business
        ParseBusiness business;
        try {
            business = currentUser.getParseObject("Business").fetchIfNeeded();
            currentUser.setLocalBusiness(business);
            currentUser.setLocalManager(currentUser.getBoolean("isManager"));
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage());
        }
    }


    public static ParseUtil getInstance(){
        if(self == null){
            self = new ParseUtil();
        }
        return self;
    }

    public static ParseStaffUser getCurrentUser(){
        return currentUser;
    }
}
