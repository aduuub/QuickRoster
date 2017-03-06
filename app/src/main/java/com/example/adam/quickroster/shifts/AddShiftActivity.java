package com.example.adam.quickroster.shifts;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;


import com.example.adam.quickroster.menu.Menu;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.notice_board.NoticeEdit;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * This has options for the user to create a new shift. It also puts the shift into Parse.
 */
public class AddShiftActivity extends AppCompatActivity implements View.OnClickListener {

    // UI
    private TextView mStartDateTextView;
    private TextView mStartTimeTextView;
    private TextView mEndDateTextView;
    private TextView mEndTimeTextView;
    private TextView mDetailsTextView; // Input not required
    private Spinner mSelectStaffSpinner;
    private Button mSubmitButton;

    private ParseUser currentUser;
    private ParseUser staffForShift;
    private List<ParseUser> allUsers;

    private TimePickerDialog.OnTimeSetListener startTimeListener;
    private TimePickerDialog.OnTimeSetListener endTimeListener;
    private DatePickerDialog.OnDateSetListener startDateListener;
    private DatePickerDialog.OnDateSetListener endDateListener;

    private SimpleDateFormat DATE_FORMATTER;
    private SimpleDateFormat TIME_FORMATTER;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shift);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setFieldsAndListeners();
        setTimeListeners();
        setDateListeners();
        setSpinner();
    }


    /**
     * Sets the spinner up with the staff members. It also initialises the listeners
     */
    private void setSpinner(){
        // create the the spinner lists
        final List<ParseUser> spinnerArray = new ArrayList<ParseUser>();
        List<String> spinnerArrayNames = new ArrayList<String>();

        for (ParseUser u : allUsers) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getString("firstName") + " " + u.getString("lastName"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectStaffSpinner.setAdapter(adapter);
        mSelectStaffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                staffForShift = spinnerArray.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    /**
     * Sets the start and end time listeners. When the time has been chosen it sets the text field
     * with the appropriate time.
     */
    private void setTimeListeners() {
        startTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String text = formatter.format(cal.getTime());
                mStartTimeTextView.setText(text);
            }
        };

        endTimeListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
                cal.set(Calendar.MINUTE, minute);
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                String text = formatter.format(cal.getTime());
                mEndTimeTextView.setText(text);
            }
        };
    }

    @Override
    public void onClick(View v) {
        Calendar cal = Calendar.getInstance();
        switch (v.getId()) {
            case R.id.start_date_chooser:
                DatePickerDialog datePicker = new DatePickerDialog(this, startDateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePicker.show();
                break;

            case R.id.start_time_chooser:
                TimePickerDialog timePicker = new TimePickerDialog(this, startTimeListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker.show();
                break;

            case R.id.end_date_chooser:
                DatePickerDialog datePicker2 = new DatePickerDialog(this, endDateListener, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
                datePicker2.show();
                break;

            case R.id.end_time_chooser:
                TimePickerDialog timePicker2 = new TimePickerDialog(this, endTimeListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true);
                timePicker2.show();
                break;

            case R.id.add_shift_button:
                createShift();
        }
    }

    private void setDateListeners(){
        startDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mStartDateTextView.setText(getFormattedDateText(year,
                        monthOfYear, dayOfMonth));
            }
        };

        endDateListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                mEndDateTextView.setText(getFormattedDateText(year,
                        monthOfYear, dayOfMonth));
            }
        };
    }


    public String getFormattedDateText(int year, int monthOfYear, int dayOfMonth) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, monthOfYear, dayOfMonth);
        return DATE_FORMATTER.format(cal.getTime());
    }


    /**
     * Calls isInputValid to check the inputs valid, if so it adds the shift to Parse, otherwises
     * calls displayInputAlert to prompt the user
     */
    public void createShift() {
        try {
            // Convert dates
            String formatText = mStartDateTextView.getText().toString();
            Date startDate = DATE_FORMATTER.parse(formatText);

            formatText = mStartDateTextView.getText().toString();
            Date endDate = DATE_FORMATTER.parse(formatText);

            // Convert time
            formatText = mStartTimeTextView.getText().toString();
            Date startTime = TIME_FORMATTER.parse(formatText);

            formatText = mEndTimeTextView.getText().toString();
            Date endTime = TIME_FORMATTER.parse(formatText);

            Date start = combineDateTime(startDate, startTime);
            Date end = combineDateTime(endDate, endTime);

            // Check dates valid
            String errorMessage = checkValidInputDates(start, end);
            if(errorMessage != null){
                displayInputAlert(errorMessage);
                return;
            }

            // Add the new shift to the currentUser
            ParseObject shift = ParseBusiness.create("Shift");
            shift.put("staff", staffForShift);
            shift.put("startTime", start);
            shift.put("endTime", end);
            shift.put("details", mDetailsTextView.getText().toString());
            shift.put("business", ((ParseStaffUser)ParseUser.getCurrentUser()).getBusiness());
            shift.saveInBackground();

        } catch (ParseException e) {
            // Should never happen
            Log.e("Error passing date", e.getMessage());
            e.printStackTrace();
            return;
        }
        finish();
    }


    /**
     *
     * @param start
     * @param end
     * @return
     */
    private String checkValidInputDates(Date start, Date end){
        if(start.getTime() == end.getTime()){
            return "Shift has no duration. Please check the start and end times";
        }else if(start.getTime() > end.getTime()){
            return "Start time cannot be before end time";
        }
        return null;
    }


    /**
     * Sets fields to be widgets and sets On Click listeners
     */
    public void setFieldsAndListeners() {
        // Fields
        mStartDateTextView = (TextView) findViewById(R.id.start_date_chooser);
        mStartTimeTextView = (TextView) findViewById(R.id.start_time_chooser);
        mEndDateTextView = (TextView) findViewById(R.id.end_date_chooser);
        mEndTimeTextView = (TextView) findViewById(R.id.end_time_chooser);
        mSelectStaffSpinner = (Spinner) findViewById(R.id.select_staff_spinner);
        mSubmitButton = (Button) findViewById(R.id.add_shift_button);
        mDetailsTextView = (TextView) findViewById(R.id.details_edit_text);
        currentUser = ParseUser.getCurrentUser();

        // On click listeners
        mSubmitButton.setOnClickListener(this);
        mStartDateTextView.setOnClickListener(this);
        mStartTimeTextView.setOnClickListener(this);
        mEndTimeTextView.setOnClickListener(this);
        mEndDateTextView.setOnClickListener(this);

        // Date and time formatters
        TIME_FORMATTER = new SimpleDateFormat("HH:mm");
        DATE_FORMATTER = new SimpleDateFormat("E, dd MMM, yyyy");

        // Set start and end date
        Calendar cal = GregorianCalendar.getInstance();
        mStartDateTextView.setText(DATE_FORMATTER.format(cal.getTime()));
        mEndDateTextView.setText(DATE_FORMATTER.format(cal.getTime()));

        // Start and end time
        mStartTimeTextView.setText(TIME_FORMATTER.format(cal.get(Calendar.HOUR)));
        mEndTimeTextView.setText(TIME_FORMATTER.format(cal.get(Calendar.HOUR)));

        allUsers = ParseQueryUtil.getAllUsers(currentUser);
    }


    /**
     * Combines the date and time
     *
     * @param date
     * @param time
     * @return Date of the combined date and time
     */
    private Date combineDateTime(Date date, Date time) {
        long millis = date.getTime() + time.getTime();
        return new Date(millis);
    }

    /**
     * Alerts the user that the input is invalid.
     */
    private void displayInputAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(AddShiftActivity.this).create();
        alertDialog.setTitle(getString(R.string.invalid_input_title));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

}