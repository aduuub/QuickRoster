package com.example.adam.quickroster.misc;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

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
            ParseObject business = currentUser.getParseObject("Business").fetch();
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
    public static Set<ParseObject> getAllShifts(ParseObject business, Date start, Date end) throws ParseException {
        // Query to get all users of the business
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        queryUsers.whereEqualTo("Business", business);
        List<ParseUser> users = queryUsers.find();

        // Get all shifts from the users
        ParseQuery<ParseObject> queryShifts = new ParseQuery<ParseObject>("Shift");
        queryShifts.whereContainedIn("staff", users);
        queryShifts.whereGreaterThanOrEqualTo("startTime", start);
        queryShifts.whereLessThanOrEqualTo("startTime", end);

        Set s = new HashSet<>();
        s.addAll(queryShifts.find());
        return s;
    }
}
