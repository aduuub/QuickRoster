package com.example.adam.quickroster.shifts;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseShift;
import com.jjoe64.graphview.GraphView;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a view that shows all the shifts on the selected day. It retrieves them from Parse
 */
public class ShiftView extends Fragment {

    private List<ParseShift> shifts;
    private ListView mListView;
    private TextView mTextView;
    private Toolbar mToolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Statistics");
        View view = inflater.inflate(R.layout.activity_shift_view, container, false);

        mListView = (ListView) view.findViewById(R.id.listView);
        mTextView = (TextView) view.findViewById(R.id.no_shifts_text_view);
        mToolbar = (Toolbar) view.findViewById(R.id.toolbar);

        Calendar cal = getExtras();
        String message = calculateTimes(cal);
        // Set action bar's title for date
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(message);


        return view;
    }

    private Calendar getExtras(){
        Bundle bundle = this.getArguments();
        int year = 2016;
        int month = 0;
        int day = 1;
        if (bundle != null) {
             year = bundle.getInt("Year");
             month = bundle.getInt("Month");
             day =  bundle.getInt("Year");
        }

        Calendar cal = GregorianCalendar.getInstance();
        cal.set(year, month, day);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
    }


    private String calculateTimes(Calendar cal){
        // get selected date and next day's date
        Date startDateNoon = cal.getTime();
        String currentDateString = new SimpleDateFormat("EEEE d MMMM y").format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        Date startDateMidnight = cal.getTime();

    ;
    }


    private void setShiftListView(){
        // create date from day, month, year
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(getIntent().getIntExtra("Year", 2016), getIntent().getIntExtra("Month", 0),
                getIntent().getIntExtra("Day", 1));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            getMenuInflater().inflate(R.menu.add_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_add) {
            Intent intentAddStaff = new Intent(this, AddShiftActivity.class);
            startActivity(intentAddStaff);
            return true;
        }
        return false;
    }
}
