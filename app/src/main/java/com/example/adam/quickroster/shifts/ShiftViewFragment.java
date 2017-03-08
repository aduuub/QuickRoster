package com.example.adam.quickroster.shifts;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This is a view that shows all the shifts on the selected day. It retrieves them from Parse
 */
public class ShiftViewFragment extends Fragment {

    private ListView mListView;
    private TextView mTextView;
    private ParseStaffUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_shift_view_frag, container, false);
        setHasOptionsMenu(true);

        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.no_shifts_text_view);
        user = null;
        Calendar cal = getExtras();
        getShiftsAndSetAdapter(cal);
        return view;
    }

    private Calendar getExtras() {
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        String userObjectId = null;
        if (getArguments() != null) {
            userObjectId = getArguments().getString("objectId", null);
        }

        if (userObjectId != null) {
            user = (ParseStaffUser) ParseStaffUser.getUserFromId(userObjectId);
        }

        if (user == null) {
            // Must be current user
            user = (ParseStaffUser) ParseUser.getCurrentUser();
        }
        return cal;
    }


    private void getShiftsAndSetAdapter(Calendar cal) {
        // get selected date and next day's date
        Date startDateNoon = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date startDateMidnight = cal.getTime();

        // Get all shifts on this day and set adapter

        // Today
        List<Object> shifts = new ArrayList<>();
        shifts.add("Today");
        shifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime(user, startDateNoon,
                startDateMidnight));
        if (shifts.size() == 1) {
            shifts.clear();
        }

        // Tomorrow
        startDateNoon = cal.getTime();
        cal.add(Calendar.DATE, 1);
        List<Object> newShifts = new ArrayList<>();
        startDateMidnight = cal.getTime();
        newShifts.add("Tomorrow");
        newShifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime(user, startDateNoon,
                startDateMidnight));
        if (newShifts.size() != 1) {
            shifts.addAll(newShifts);
        }


        // Upcoming
        newShifts.clear();
        newShifts.add("Upcoming");
        newShifts.addAll(ParseQueryUtil.getNextShifts(user, startDateMidnight, 5));
        if (newShifts.size() != 1) {
            shifts.addAll(newShifts);
        }

        if (shifts.size() > 0) {
            // display shifts
            mListView.setAdapter(new ShiftViewAdapterFragment(getActivity().getApplicationContext(), shifts));
        } else {
            // No shifts to display, so display text
            mListView.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (((ParseStaffUser) ParseUser.getCurrentUser()).isManager()) {
            inflater.inflate(R.menu.add_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_icon_add) {
            Intent intentAddStaff = new Intent(getActivity(), AddShiftActivity.class);
            startActivity(intentAddStaff);

        } else {
            // Menu not found
            return false;
        }

        return true;
    }
}
