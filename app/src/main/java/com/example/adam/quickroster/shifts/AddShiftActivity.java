package com.example.adam.quickroster.shifts;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.adam.quickroster.menu.Menu;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.R;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * This has options for the user to create a new shift. It also puts the shift into Parse.
 */
public class AddShiftActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    // UI
    private TextView date;
    private TextView fromTime;
    private TextView toTime;
    private TextView acceptedText;
    private Button submit;
    private TextView details;
    private Spinner selectUsers;

    private ParseUser currentUser;
    private ParseUser staffForShift;
    private List<ParseUser> allUsers;

    private TimePickerDialog.OnTimeSetListener fromListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String text = formatter.format(cal.getTime());
            fromTime.setText(text);
        }
    };

    private TimePickerDialog.OnTimeSetListener toListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
            cal.set(Calendar.MINUTE, minute);
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
            String text = formatter.format(cal.getTime());
            toTime.setText(text);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setFieldsAndListeners();
        allUsers = ParseQueryUtil.getAllUsers(currentUser);


        // create the the spinner lists
        final List<ParseUser> spinnerArray = new ArrayList<ParseUser>();
        List<String> spinnerArrayNames = new ArrayList<String>();

        for (ParseUser u : allUsers) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getString("firstName") + " " + u.getString("lastName"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectUsers.setAdapter(adapter);
        selectUsers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                staffForShift = spinnerArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.dateChooser:
                DatePickerDialog datePicker = new DatePickerDialog(this, this, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
                break;
            case R.id.pickStartTime:
                TimePickerDialog timePicker = new TimePickerDialog(this, fromListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker.show();
                break;
            case R.id.pickEndTime:
                TimePickerDialog timePicker2 = new TimePickerDialog(this, toListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker2.show();
                break;
            case R.id.addNewShiftSubmitButton:
                createShift();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        cal.add(Calendar.DATE, 1);
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String text = formatter.format(cal.getTime());
        this.date.setText(text);
    }

    public void createShift() {
        if (staffForShift == null) {
            acceptedText.setText("Please Choose a Staff Member First");
            return;
        }

        List<TextView> widgets = Arrays.asList(date, fromTime, toTime);
        for (TextView v : widgets) {
            if (v.toString().equals("")) {
                v.setError("Please Fill in this Field");
                return;
            }
        }

        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");

        try {
            // Convert dates
            String formatText = date.getText().toString();
            Date date = dateFormatter.parse(formatText);

            formatText = fromTime.getText().toString();
            Date startTime = timeFormatter.parse(formatText);

            formatText = toTime.getText().toString();
            Date endTime = timeFormatter.parse(formatText);

            // add the new shift to the currentUser
            ParseObject shift = ParseBusiness.create("Shift");
            shift.put("staff", staffForShift);
            shift.put("startTime", combineDateTime(date, startTime));
            shift.put("endTime", combineDateTime(date, endTime));
            shift.put("details", details.getText().toString());
            shift.save();

        } catch (ParseException | com.parse.ParseException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        Toast.makeText(getApplicationContext(), "Successfully Added Shift", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, Menu.class);
        startActivity(intent);
    }

    /**
     * Sets fields to be widgets and sets On Click listeners
     */
    public void setFieldsAndListeners() {
        // Fields
        date = (TextView) findViewById(R.id.dateChooser);
        fromTime = (TextView) findViewById(R.id.pickStartTime);
        toTime = (TextView) findViewById(R.id.pickEndTime);
        selectUsers = (Spinner) findViewById(R.id.addStaffSelector);
        submit = (Button) findViewById(R.id.addNewShiftSubmitButton);
        details = (TextView) findViewById(R.id.addShiftDetails);
        currentUser = ParseUser.getCurrentUser();

        // On click listeners
        submit.setOnClickListener(this);
        date.setOnClickListener(this);
        fromTime.setOnClickListener(this);
        toTime.setOnClickListener(this);
    }

    /**
     * Combine the date
     * @param date
     * @param time
     * @return
     */
    public Date combineDateTime(Date date, Date time){
        long millis = date.getTime() + time.getTime();
        return new Date(millis);
    }
}



