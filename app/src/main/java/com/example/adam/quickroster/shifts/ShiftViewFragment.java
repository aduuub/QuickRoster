package com.example.adam.quickroster.shifts;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseShift;
import com.example.adam.quickroster.staff.StaffView;
import com.parse.ParseUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This is a view that shows all the shifts on the selected day. It retrieves them from Parse
 */
public class ShiftViewFragment extends Fragment {

    private List<Object> shifts;
    private ListView mListView;
    private TextView mTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_shift_view_frag, container, false);
        setHasOptionsMenu(true);

        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.no_shifts_text_view);

        Calendar cal = getExtras();
        getShiftsAndSetAdapter(cal);
        return view;
    }

    private Calendar getExtras() {
        Calendar cal = GregorianCalendar.getInstance();
        //cal.set(year, month, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }


    private void getShiftsAndSetAdapter(Calendar cal) {
        // get selected date and next day's date
        Date startDateNoon = cal.getTime();
        cal.add(Calendar.DATE, 1);
        Date startDateMidnight = cal.getTime();

        // Get all shifts on this day and set adapter

        // Today
        shifts = new ArrayList<>();
        shifts.add("Today");
        shifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime(ParseUtil.getCurrentUser(), startDateNoon,
                startDateMidnight));
        if(shifts.size() == 1)
            shifts.clear();

        // Tomorrow
        startDateNoon = cal.getTime();
        cal.add(Calendar.DATE, 1);
        List<Object> newShifts = new ArrayList<>();
        startDateMidnight = cal.getTime();
        newShifts.add("Tomorrow");
        newShifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime(ParseUtil.getCurrentUser(), startDateNoon,
                startDateMidnight));
        if(newShifts.size() == 1)
            newShifts.clear();
        else
            shifts.addAll(newShifts);


        // Upcoming
        shifts.add("Upcoming");
        shifts.addAll(ParseQueryUtil.getNextShifts(ParseUtil.getCurrentUser(), startDateMidnight, 5));

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
        if (ParseUtil.getCurrentUser().isManager()) {
            inflater.inflate(R.menu.calendar_list_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_icon_add) {
            Intent intentAddStaff = new Intent(getActivity(), AddShiftActivity.class);
            startActivity(intentAddStaff);

        } else if (item.getItemId() == R.id.menu_icon_calendar) {
            displayCalView();

        }else {
            // Menu not found
            return false;
        }

        return true;
    }


    private void displayCalView() {
        Fragment fragment = new CalendarViewActivity();
        ((com.example.adam.quickroster.menu.Menu) getActivity()).displayFragment(fragment);
    }
}
