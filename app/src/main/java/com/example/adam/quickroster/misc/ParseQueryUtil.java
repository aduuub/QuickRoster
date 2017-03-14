package com.example.adam.quickroster.misc;

import android.util.Log;

import com.example.adam.quickroster.model.ParseShift;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * This is a class that provides static methods for querying the Parse.com database.
 *
 * @author Adam Wareing
 */
public class ParseQueryUtil {

    /**
     * Cant create an instance of this class.
     */
    private ParseQueryUtil() {
        // Can't be initialised
    }


    /**
     * Get all users that belong to the same belong to the <code>user</code>.
     */
    public static List<ParseUser> getAllUsers(ParseUser user) {
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        try {
            ParseObject business = ((ParseStaffUser) user).getBusiness();
            queryUsers.whereEqualTo("business", business);
            return queryUsers.find();

        } catch (com.parse.ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves all shifts of all employees from the business that are between the start and end date
     *
     * @return List of ParseObject, which are ParseShifts
     */
    private static List<ParseObject> getAllShiftsBetweenDate(ParseObject business, Date start, Date end) throws ParseException {
        // Get all shifts from the users
        ParseQuery<ParseObject> queryShifts = new ParseQuery<>("Shift");
        queryShifts.whereEqualTo("business", business);
        queryShifts.whereGreaterThanOrEqualTo("startTime", start);
        queryShifts.whereLessThanOrEqualTo("startTime", end);

        return queryShifts.find();
    }


    /**
     * Retrieves all shifts of the user from the business that are modified after the date
     *
     * @return List of ParseObject, which are ParseShifts
     */
    public static List<ParseObject> getShiftsModifiedAfter(ParseUser user, Date dateAfter) throws ParseException {
        ParseQuery<ParseObject> queryUserShifts = ParseQuery.getQuery("Shift");
        queryUserShifts.whereEqualTo("staff", user);
        queryUserShifts.whereGreaterThanOrEqualTo("updatedAt", dateAfter);
        return queryUserShifts.find();
    }


    /**
     * Gets all the shifts that are between the startDateNoon and startDateMidnight
     */
    public static List<ParseObject> getAllStaffsShiftBetweenTime(ParseStaffUser currentUser, Date startDateNoon, Date startDateMidnight) {
        ParseQuery<ParseObject> query = new ParseQuery<>("Shift");

        query.whereEqualTo("staff", currentUser);
        query.whereGreaterThanOrEqualTo("startTime", startDateNoon);
        query.whereLessThanOrEqualTo("startTime", startDateMidnight);

        try {
            if (currentUser.getBoolean("isManager")) {
                // Add shifts of everyone in business
                ParseObject business = currentUser.getBusiness();
                return ParseQueryUtil.getAllShiftsBetweenDate(business, startDateNoon, startDateMidnight);
            } else {
                // Add just the users shifts
                return query.find();
            }
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    /**
     * Gets the next shifts of user after the specified date. It will return all shifts after dateAfter for the user if they are not a manager.
     * If they are a manager it will return all shifts (taking the limit into account) of employees belonging to the business.
     *
     * @param user
     * @param dateAfter
     * @param limit - the maximum limit of shifts.
     * @return
     */
    public static List<ParseShift> getNextShifts(ParseStaffUser user, Date dateAfter, int limit, boolean getOnlyUsersShifts) {
        ParseQuery<ParseObject> query = new ParseQuery<>("Shift");
        if (getOnlyUsersShifts) {
            query.whereEqualTo("staff", user);
        }
        query.whereEqualTo("business", user.getBusiness());
        query.whereGreaterThanOrEqualTo("startTime", dateAfter);
        query.orderByAscending("startTime");
        query.setLimit(limit);

        try {
            return (List<ParseShift>)(Object) query.find();
        } catch (ParseException e) {
            e.printStackTrace();
            throw new RuntimeException("Error trying to find shifts");
        }
    }


    /**
     * Counts the number of users belonging to the business
     *
     * @param business
     * @return
     */
    public static int countStaffMembersInBusiness(ParseObject business) {
        ParseQuery<ParseUser> queryUsers = ParseUser.getQuery();
        queryUsers.whereEqualTo("business", business);
        try {
            return queryUsers.count();
        } catch (ParseException e) {
            Log.e("Parse error", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
