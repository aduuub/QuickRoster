package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adam.quickroster.menu.Menu;
import com.example.adam.quickroster.R;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/**
 * This is used for adding a new staff member to a business in Parse.
 */
public class AddStaffMemberActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_add_staff_member);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add Staff Member");
        setSupportActionBar(toolbar);
    }

    /**
     * Creates a new staff member
     */
    public void createStaff() {
        // Get current values from widgets
        userName = (String) ((TextView) findViewById(R.id.staffUserName)).getText().toString();
        password = (String) ((TextView) findViewById(R.id.staffPassword)).getText().toString();
        firstName = (String) ((TextView) findViewById(R.id.staffFirstName)).getText().toString();
        lastName = (String) ((TextView) findViewById(R.id.staffLastName)).getText().toString();
        email = (String) ((TextView) findViewById(R.id.email)).getText().toString();
        isManager = !((Switch) findViewById(R.id.isManagerSwitch)).isPressed();


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
     *
     * @param user
     */
    public void addStaffToBusiness(final ParseUser user) throws ParseException {
        if (isManager) {
            business.add("Managers", user);

            Toast.makeText(getApplicationContext(), "Successfully added user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddStaffMemberActivity.this, Menu.class);
            startActivity(intent);

        } else {
            business.add("Users", user);
            business.save();

            Toast.makeText(getApplicationContext(), "Successfully added user", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(AddStaffMemberActivity.this, StaffHomeActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(android.view.Menu menu) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            getMenuInflater().inflate(R.menu.done_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_done) {
            createStaff();
        }
        return true;
    }

}


