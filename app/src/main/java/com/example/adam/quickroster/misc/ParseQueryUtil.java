package com.example.adam.quickroster.misc;

import android.widget.Toast;

import com.example.adam.quickroster.model.ParseShift;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Adam on 27/08/16.
 */
public class ParseQueryUtil {

    /**
     * Gets all users of the business
     */
    public static List<ParseUser> getAllUsers(ParseUser currentUser) {
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        try {
            ParseObject business = ParseUtil.getCurrentUser().getBusiness();
            queryUsers.whereEqualTo("Business", business);
            return queryUsers.find();

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all shifts of all employees from the business that are between the start
     * and end date
     *
     * @param business
     * @param start
     * @return List of ParseObject, which are ParseShifts
     * @throws ParseException
     */
    public static List<ParseObject> getAllShifts(ParseObject business, Date start, Date end) throws ParseException {

        // Get all shifts from the users
        ParseQuery<ParseObject> queryShifts = new ParseQuery<ParseObject>("Shift");
        queryShifts.whereEqualTo("business", business);
        queryShifts.whereGreaterThanOrEqualTo("startTime", start);
        queryShifts.whereLessThanOrEqualTo("startTime", end);

       return queryShifts.find();
    }

    /**
     * Retrieves all shifts of the user from the business that are after the date
     *
     * @param user
     * @param dateAfter
     * @return List of ParseObject, which are ParseShifts
     * @throws ParseException
     */
    public static List<ParseObject> getAllUsersShiftsAfterDate(ParseUser user, Date dateAfter) throws ParseException {

        // Query to get all shifts
        ParseQuery<ParseObject> queryUserShifts = ParseQuery.getQuery("Shift");
        queryUserShifts.whereEqualTo("staff", user);
        queryUserShifts.whereGreaterThanOrEqualTo("updatedAt", dateAfter);
        return queryUserShifts.find();
    }



    public static List<ParseObject> getAllStaffsShiftBetweenTime(ParseStaffUser currentUser, Date startDateNoon,
                                                            Date startDateMidnight) {
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Shift");
        query.whereEqualTo("staff", currentUser);
        query.whereGreaterThanOrEqualTo("startTime", startDateNoon);
        query.whereLessThanOrEqualTo("startTime", startDateMidnight);

        Set<ParseObject> shiftsOnDay = new HashSet<>();
        try {
            shiftsOnDay.addAll(query.find()); // get all the users shifts

            if (currentUser.getBoolean("isManager")) {
                // add shifts of everyone in business
                ParseObject business = currentUser.getBusiness();
                return  ParseQueryUtil.getAllShifts(business, startDateNoon,
                        startDateMidnight);
            }else{
                // add just the users shifts
                return query.find();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * Probs not correct
     *
     * @param user
     * @return
     */
    public static ParseObject getParseUsersBusiness(ParseUser user) {
        try {
            return user.getParseObject("Business").fetch(); // user business
        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
