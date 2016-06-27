package com.example.adam.quickroster;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ManagerAddActivity extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    // XML
    private TextView date;
    private TextView fromTime;
    private TextView toTime;
    private TextView acceptedText;
    private Button submit;
    private TextView details;
    private Spinner selectUsers;


    private User user;
    private User staffForShift;
    private ArrayList<User> allUsers;

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
        setContentView(R.layout.activity_add);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user = getIntent().getParcelableExtra("User");
        allUsers = LoginAsUser.getAllUsers();

        date = (TextView) findViewById(R.id.dateChooser);
        fromTime = (TextView) findViewById(R.id.pickStartTime);
        toTime = (TextView) findViewById(R.id.pickEndTime);
        selectUsers = (Spinner) findViewById(R.id.addStaffSelector);
        submit = (Button) findViewById(R.id.addNewShiftSubmitButton);
        details = (TextView) findViewById(R.id.addShiftDetails);
        acceptedText = (TextView) findViewById(R.id.accepted);


        submit.setOnClickListener(this);
        date.setOnClickListener(this);
        fromTime.setOnClickListener(this);
        toTime.setOnClickListener(this);

        // create the the spinner

        final List<User> spinnerArray = new ArrayList<User>();
        List<String> spinnerArrayNames = new ArrayList<String>();

        for (User u : allUsers) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getName());
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
                // TODO
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
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String text = formatter.format(cal.getTime());
        this.date.setText(text);
    }

    public void createShift() {
        if (staffForShift == null) {
            acceptedText.setText("Please Choose a Staff Memeber First");
            return;
        }

        DateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy");
        DateFormat timeFormatter = new SimpleDateFormat("HH:mm");


        try {
            // date to long
            String formatText = date.getText().toString();
            if(formatText.equals("")) {
                acceptedText.setText("Please Fill All Fields");
                return;
            }
            Date selectedDate = dateFormatter.parse(formatText);
            long dateInLong = selectedDate.getTime();

            // start time to long
            formatText = fromTime.getText().toString();
            if(formatText.equals("")){
                acceptedText.setText("Please Fill All Fields");
                return;
            }
            selectedDate = timeFormatter.parse(formatText);
            long startTimeLong = selectedDate.getTime();

            // end time to long
            formatText = toTime.getText().toString();
            if(formatText.equals("")){
                acceptedText.setText("Please Fill All Fields");
                return;
            }
            selectedDate = timeFormatter.parse(formatText);
            long endTimeLong = selectedDate.getTime();

            String shiftDetails = details.getText().toString();

            // add the new shift to the user
            staffForShift.addShift(dateInLong, startTimeLong, endTimeLong, shiftDetails);

            acceptedText.setText("Shift Added");

        } catch (ParseException e) {
            acceptedText.setText("Please Fill All Fields");
            e.printStackTrace();
        }
    }

}



