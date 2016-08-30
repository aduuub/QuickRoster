package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.quickroster.manager_options.ManagerHomeActivity;
import com.example.adam.quickroster.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * This is used for adding a new staff member to a business in Parse.
 */
public class AddStaffMemeberActivity extends AppCompatActivity {

    private Button createStaff;
    private ParseObject business;

    // UI
    private String userName;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_staff_memeber);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Staff Member");

        createStaff = (Button) findViewById(R.id.createNewStaffMemberButton);
        createStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get current values from widgets
                userName = (String) ((TextView) findViewById(R.id.staffUserName)).getText().toString();
                password = (String) ((TextView) findViewById(R.id.staffPassword)).getText().toString();
                firstName = (String) ((TextView) findViewById(R.id.staffFirstName)).getText().toString();
                lastName = (String) ((TextView) findViewById(R.id.staffLastName)).getText().toString();
                email = (String) ((TextView) findViewById(R.id.email)).getText().toString();
                isManager = !((Switch) findViewById(R.id.isManagerSwitch)).isPressed();
                createStaff();
            }
        });
    }

    /**
     * Creates a new staff member
     */
    public void createStaff() {
        // Create new user and set values
        final ParseUser user = new ParseUser();
        user.setUsername(userName);
        user.setEmail(email);
        user.setPassword(password);
        user.put("firstName", firstName);
        user.put("lastName", lastName);
        user.put("isManager", isManager);

        // Get the business ID
        final String businessID = getIntent().getStringExtra("BusinessID");
        if (businessID == null) {
            Toast.makeText(getApplicationContext(), "Error trying to find that business", Toast.LENGTH_LONG).show();
            return;
        }

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Business");
        try {
            this.business = query.get(businessID);
            user.put("Business", business); // put the business pointer in the user
            user.signUp();
            user.logIn(userName, password);
            addStaffToBusiness(user);
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
            return;
        }
    }


    /**
     * Adds the user to the to Business that it belongs to
     * @param user
     */
    public void addStaffToBusiness(final ParseUser user) throws ParseException {
        if (isManager) {
            business.add("Managers", user);

            Toast.makeText(getApplicationContext(), "Successfully added user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddStaffMemeberActivity.this, ManagerHomeActivity.class);
            startActivity(intent);

        } else {
            business.add("Users", user);
            business.save();

            Toast.makeText(getApplicationContext(), "Successfully added user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddStaffMemeberActivity.this, StaffHomeActivity.class);
            startActivity(intent);
        }
    }
}


