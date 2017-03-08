package com.example.adam.quickroster.staff;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.adam.quickroster.menu.Menu;
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
 */
public class AddStaffMemberActivity extends AppCompatActivity {

    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;

    private String businessId;
    private boolean registeringBusinessAsWell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Staff Member");
        setSupportActionBar(toolbar);

        // Get the business ID
        businessId = getIntent().getStringExtra("BusinessID");
        registeringBusinessAsWell = getIntent().getBooleanExtra("registeringBusinessAsWell", false);

        if (businessId == null) {
            // Cant add a user without business id
            Log.e("Error", "Cant find the businessID passed to this intent");
            throw new RuntimeException("Cant find the businessID passed to this intent");
        }
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

        // Get current values from widgets
        userName = ((TextView) findViewById(R.id.staffUserName)).getText().toString();
        password = ((TextView) findViewById(R.id.staffPassword)).getText().toString();
        firstName = ((TextView) findViewById(R.id.staffFirstName)).getText().toString();
        lastName = ((TextView) findViewById(R.id.staffLastName)).getText().toString();
        email = ((TextView) findViewById(R.id.email)).getText().toString();
        boolean isManager = !(findViewById(R.id.isManagerSwitch)).isPressed();

        // Check valid input
        String errorMessage = inputIsValid();
        if (errorMessage != null) {
            displayInputAlert(errorMessage);
            return;
        }

        // Create new user and set values
        final ParseStaffUser user = new ParseStaffUser();
        user.setUsername(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setManager(isManager);
        user.setAutoAddToCalendar(true);
        user.setBusiness(business); // put the business pointer in the user

        // Check
        try {
            user.signUp();
        } catch (ParseException e) {
            displayInputAlert(e.getMessage());
        }

        if (registeringBusinessAsWell) {
            Intent intent = new Intent(AddStaffMemberActivity.this, Menu.class);
            startActivity(intent);
        }
        finish();
    }

    private boolean exceedingMaxStaffLimit(ParseObject business) {
        int currentStaff = ParseQueryUtil.countStaffMembersInBusiness(business);
        int maxStaff = business.getInt("maxEmployees");

        return currentStaff >= maxStaff;
    }


    /**
     * @return - null if free of errors. Else it returns the error message
     */
    private String inputIsValid() {
        List<String> fields = Arrays.asList(userName, password, firstName, lastName, email);
        for (String s : fields) {
            if (s == null || s.equals("")) {
                return getString(R.string.invalid_input_message);
            }
        }
        // Check password
        return Util.isPasswordValid(password);
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
     *
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


