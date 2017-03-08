package com.example.adam.quickroster.shifts;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.model.ParseStaffUser;
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
public class ShiftView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shift_view);
        ListView listView = (ListView) findViewById(R.id.listView);
        TextView textView = (TextView) findViewById(R.id.no_shifts_text_view);


        // create date from day, month, year
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(getIntent().getIntExtra("Year", 2016), getIntent().getIntExtra("Month", 0),
                getIntent().getIntExtra("Day", 1));
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);

        // get selected date and next day's date
        Date startDateNoon = cal.getTime();
        String currentDateString = new SimpleDateFormat("EEEE d MMMM y").format(cal.getTime());
        cal.add(Calendar.DATE, 1);
        Date startDateMidnight = cal.getTime();

        // Set action bar's title for date
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(currentDateString);

        // Get all shifts on this day and set adapter
        List<Object> shifts = new ArrayList<>();
        shifts.addAll(ParseQueryUtil.getAllStaffsShiftBetweenTime((ParseStaffUser) ParseUser.getCurrentUser(), startDateNoon,
                startDateMidnight));
        if (shifts.size() > 0) {
            listView.setAdapter(new ShiftViewAdapter(this, shifts));
        } else {
            listView.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.VISIBLE);
        }
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
