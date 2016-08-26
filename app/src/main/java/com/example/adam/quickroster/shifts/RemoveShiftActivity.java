package com.example.adam.quickroster.shifts;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.shifts.AddShiftActivity;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RemoveShiftActivity extends AppCompatActivity implements View.OnClickListener {

    // XML
    private Spinner selectStaffSpinner;
    private Spinner selectShiftSpinner;
    private Button submit;

    private ParseUser selectedUser;
    private ParseObject selectedShift;
    private List<ParseUser> allUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectStaffSpinner = (Spinner) findViewById(R.id.selectStaffToModify);
        selectShiftSpinner = (Spinner) findViewById(R.id.selectShiftToModify);
        submit = (Button) findViewById(R.id.removeShiftButton);
        submit.setOnClickListener(this);
        createShiftSpinner(ParseUser.getCurrentUser());
        createStaffSpinner();
    }

    /**
     * Creates the spinner to select the staff member. This calls createShiftSpinner() once a user
     * is selected
     */
    public void createStaffSpinner() {
        final List<ParseUser> spinnerArray = new ArrayList<ParseUser>();
        List<String> spinnerArrayNames = new ArrayList<String>();
        allUsers = AddShiftActivity.getAllUsers(ParseUser.getCurrentUser());

        for (ParseUser u : allUsers) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getString("firstName") + " " + u.getString("lastName"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStaffSpinner.setAdapter(adapter);
        selectStaffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                createShiftSpinner(spinnerArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
    }

    /**
     * Creates a selector to choose one of the users shifts
     *
     * @param user
     */
    public void createShiftSpinner(ParseUser user) {
        this.selectedUser = user;

        // Query and get all the users shifts
        ParseQuery shiftQuery = ParseQuery.getQuery("Shift");
        shiftQuery.include("staff");
        shiftQuery.whereEqualTo("staff", user);
        final List<ParseObject> spinnerArray;
        try {
            spinnerArray = shiftQuery.find();
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        // Add all of the shift details to the spinner array names
        List<String> spinnerArrayNames = new ArrayList<String>();
        for (ParseObject shift : spinnerArray) {
            String details = shift.getString("details");
            spinnerArrayNames.add(details == null ? "Default Shift" : details);
        }

        // Set adapters
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectShiftSpinner.setAdapter(adapter);
        selectShiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedShift(spinnerArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
    }

    /**
     * On button click, delete the shift in the background
     *
     * @param v
     */
    public void onClick(View v) {

        if (selectedShift == null || selectedUser == null)
            return;

        selectedShift.deleteInBackground();
        createShiftSpinner(selectedUser);
        Toast.makeText(getApplicationContext(), "Successfully Deleted Shift", Toast.LENGTH_SHORT).show();
    }

    public void setSelectedShift(ParseObject shift) {
        this.selectedShift = shift;
    }
}
