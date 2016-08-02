package com.example.adam.quickroster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class EditStaffMemeberActivity extends AppCompatActivity {

    private Button saveButton;
    private Button deleteButton;

    // widgets
    private TextView userName;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private Switch isManager;

    private ParseUser staffMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff_memeber);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Edit Staff Member");
        setFields();
        fillOutTextFields();
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStaffMember();
            }
        });
    }

    /**
     * Updates the selected staff member
     */
    public void updateStaffMember() {
        // Create new user and set values
        if(staffMember == null){
            Toast.makeText(getApplicationContext(), "Error: Can't find user to update", Toast.LENGTH_LONG).show();
            return;
        }
        ParseUser loggedInUser = ParseUser.getCurrentUser();

        String data = "{ 'isManager' : " + isManager.isPressed() + ", 'userName:" + userName.getText().toString() +
                ", email : " + email.getText().toString() +







        staffMember.setUsername(userName.getText().toString());
        staffMember.setEmail(email.getText().toString());
        staffMember.put("firstName", firstName.getText().toString());
        staffMember.put("lastName", lastName.getText().toString());
        staffMember.put("isManager", !isManager.isPressed());
        staffMember.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                    Toast.makeText(getApplicationContext(), "Staff Updated", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * Called on create, sets the text edits to have the current values of the staff member.
     * This is passed in as an extra to this intent.
     */
    public void fillOutTextFields() {
        // Get values
        String usernameText = getIntent().getStringExtra("userName");
        String firstNameText = getIntent().getStringExtra("firstName");
        String lastNameText = getIntent().getStringExtra("lastName");
        String emailText = getIntent().getStringExtra("email");
        String userID = getIntent().getStringExtra("staffID");

        // Get the Parse User
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userID);
        try {
            staffMember = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }


        // Set values
        userName.setText(usernameText == null ? "" : usernameText);
        firstName.setText(firstNameText == null ? "" : firstNameText);
        lastName.setText(lastNameText == null ? "" : lastNameText);
        email.setText(emailText == null ? "" : emailText);
    }

    /**
     * Sets the fields
     */
    public void setFields() {
        userName = (TextView) findViewById(R.id.staffUserName);
        firstName = (TextView) findViewById(R.id.staffFirstName);
        lastName = (TextView) findViewById(R.id.staffLastName);
        email = (TextView) findViewById(R.id.email);
        isManager = (Switch) findViewById(R.id.isManagerSwitch);
        deleteButton = (Button) findViewById(R.id.delete_staff_member_button);
        saveButton = (Button) findViewById(R.id.save_staff_member_button);
    }
}