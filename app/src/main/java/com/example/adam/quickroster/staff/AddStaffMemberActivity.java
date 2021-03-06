package com.example.adam.quickroster.staff;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Switch;
import android.widget.TextView;

import com.example.adam.quickroster.menu.MenuActivity;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.misc.Util;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseException;
import com.parse.ParseObject;

import java.util.Arrays;
import java.util.List;

/**
 * This is used for adding a new staff member to a business in Parse.
 *
 * @author Adam Wareing
 */
public class AddStaffMemberActivity extends AppCompatActivity {

    private TextView mUserNameTextView;
    private TextView mPasswordTextView;
    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mEmailTextView;
    private Switch mIsManagerSwitch;


    private String businessId;

    /**
     * Passed in as a string extra if coming from RegisterBusinessActivity. Takes the user to the home page instead going back to the last activity.
     */
    private boolean registeringBusinessAsWell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Staff Member");
        setSupportActionBar(toolbar);
        setFieldsAndGetExtras();
    }


    private void setFieldsAndGetExtras(){
        registeringBusinessAsWell = getIntent().getBooleanExtra("registeringBusinessAsWell", false);

        // Get the business ID
        businessId = getIntent().getStringExtra("BusinessID");
        if (businessId == null) {
            // Cant add a user without business id
            throw new RuntimeException("Cant find the businessID passed to this intent");
        }

        // Set fields
        mUserNameTextView = (TextView) findViewById(R.id.staff_user_name);
        mPasswordTextView = (TextView) findViewById(R.id.staff_password);
        mFirstNameTextView = (TextView) findViewById(R.id.staff_first_name);
        mLastNameTextView = (TextView) findViewById(R.id.staff_last_name);
        mEmailTextView = (TextView) findViewById(R.id.email);
        mIsManagerSwitch = (Switch) findViewById(R.id.is_manager_switch);
    }

    /**
     * Creates a new staff member
     */
    private void createStaff() {
        ParseObject business = ParseObject.createWithoutData("Business", businessId);
        try {
            business.fetch();
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
            return;
        }

        // Check not exceeding max limit of staff
        if (exceedingMaxStaffLimit(business)) {
            displayInputAlert(getString(R.string.exceeding_max_staff_message));
            return;
        }

        // Check valid input
        String errorMessage = inputIsValid();
        if (errorMessage != null) {
            displayInputAlert(errorMessage);
            return;
        }

        // Create new user and set values
        final ParseStaffUser user = new ParseStaffUser();
        user.setUsername(mUserNameTextView.getText().toString());
        user.setEmail(mEmailTextView.getText().toString());
        user.setPassword(mPasswordTextView.getText().toString());
        user.setFirstName(mFirstNameTextView.getText().toString());
        user.setLastName(mLastNameTextView.getText().toString());
        user.setManager(mIsManagerSwitch.isSelected());
        user.setAutoAddToCalendar(true);
        user.setBusiness(business); // put the business pointer in the user

        try {
            user.signUp();
        } catch (ParseException e) {
            displayInputAlert(e.getMessage());
        }

        if (registeringBusinessAsWell) {
            Intent intent = new Intent(AddStaffMemberActivity.this, MenuActivity.class);
            startActivity(intent);
        }
        finish();
    }


    /**
     * Checks to see if adding a staff member will exceed the maximum staff allowed for the business
     * @param business
     * @return - true if they will exceed the limit
     */
    private boolean exceedingMaxStaffLimit(ParseObject business) {
        int currentStaff = ParseQueryUtil.countStaffMembersInBusiness(business);
        int maxStaff = business.getInt("maxEmployees");

        return currentStaff >= maxStaff;
    }


    /**
     * Checks the username, password, first and last names and email has been filled out
     * @return - null if free of errors. Else it returns the error message
     */
    private String inputIsValid() {
        List<TextView> fields = Arrays.asList(mUserNameTextView, mPasswordTextView, mFirstNameTextView, mLastNameTextView, mEmailTextView);
        for (TextView s : fields) {
            if (s == null || s.getText().toString() == null) {
                return getString(R.string.invalid_input_message);
            }
        }
        // Check password
        return Util.isPasswordValid(mPasswordTextView.getText().toString());
    }


    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_done) {
            createStaff();
        }
        return true;
    }

    /**
     * Alerts the user that the input is invalid.
     * @param message - message to display
     */
    private void displayInputAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(AddStaffMemberActivity.this).create();
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


