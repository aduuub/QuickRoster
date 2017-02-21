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
        View view = inflater.inflate(R.layout.activity_shift_view, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.no_shifts_text_view);
        Calendar cal = getExtras();
        getShiftsAndSetAdapter(cal);
        return view;
    }

    private Calendar getExtras() {
        Bundle bundle = this.getArguments();
        int year = 2016;
        int month = 0;
        int day = 1;
        if (bundle != null) {
            year = bundle.getInt("Year");
            month = bundle.getInt("Month");
            day = bundle.getInt("Day");
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        return cal;
    }


    private void getShiftsAndSetAdapter(Calendar cal) {
        // get selected date and next day's date
        Date startDateNoon = cal.getTime();
        String currentDateString = new SimpleDateFormat("EEEE d MMMM y").format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        Date startDateMidnight = cal.getTime();

        // Get all shifts on this day and set adapter
        shifts = new ArrayList<>();
        shifts.add("Today");
        shifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime(ParseUtil.getCurrentUser(), startDateNoon,
                startDateMidnight));
        if(shifts.size() == 1)
            shifts.clear();

        startDateNoon = cal.getTime();
        cal.add(Calendar.DATE, 1);
        startDateMidnight = cal.getTime();
        shifts.add("Tomorrow");
        shifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime(ParseUtil.getCurrentUser(), startDateNoon,
                startDateMidnight));

        if (shifts.size() > 0) {
            mListView.setAdapter(new ShiftViewAdapterFragment(getActivity().getApplicationContext(), shifts));
        } else {
            mListView.setVisibility(View.INVISIBLE);
            mTextView.setVisibility(View.VISIBLE);
        }
    }
}
