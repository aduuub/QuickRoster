package com.example.adam.quickroster.fragments;

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
import com.example.adam.quickroster.misc.Util;
import com.example.adam.quickroster.model.ParseShift;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.shifts.AddShiftActivity;
import com.example.adam.quickroster.shifts.ShiftAdapter;
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
    private boolean displayOnlyUsersShifts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_shift_view_frag, container, false);
        setHasOptionsMenu(true);

        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.no_shifts_text_view);
        setUser();
        Calendar cal = getCalendar();
        getShiftsAndSetAdapter(cal);
        return view;
    }

    public void setUser() {
        String userObjectId = null;
        if (getArguments() != null) {
            userObjectId = getArguments().getString("objectId", null);
            displayOnlyUsersShifts = true;
        }
        if (userObjectId != null) {
            user = (ParseStaffUser) ParseStaffUser.getUserFromId(userObjectId);
        }else{
            user = (ParseStaffUser) ParseUser.getCurrentUser();
            displayOnlyUsersShifts = false;
        }
    }

    private Calendar getCalendar() {
        Calendar calendar = GregorianCalendar.getInstance();
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar;
    }


    private void getShiftsAndSetAdapter(Calendar cal) {
        // Get upcoming shifts
        Date currentDateTime = cal.getTime();
        List<ParseShift> upcomingShifts = ParseQueryUtil.getNextShifts(user, currentDateTime, 20, displayOnlyUsersShifts);
        List<Object> adapterItems = new ArrayList<>();

        for (int i = 0; i < upcomingShifts.size(); /* see body */) {
            ParseShift shift = upcomingShifts.get(i);
            // Add header and shift
            long millis = shift.getStartDate().getTime();
            String formattedTime = Util.DATE_FORMATTER.format(millis);
            adapterItems.add(formattedTime);

            // Add all remaining shifts on the day
            while (Util.DATE_FORMATTER.format(shift.getStartDate().getTime()).equals(formattedTime)) {
                adapterItems.add(shift);
                i++;

                // Check within index
                if (i == upcomingShifts.size()) {
                    break;
                }
                shift = upcomingShifts.get(i);
            }
        }

        if (adapterItems.size() > 0) {
            // Display shifts
            mListView.setAdapter(new ShiftAdapter(getActivity().getApplicationContext(), adapterItems));
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
            // MenuActivity not found
            return false;
        }

        return true;
    }


}
