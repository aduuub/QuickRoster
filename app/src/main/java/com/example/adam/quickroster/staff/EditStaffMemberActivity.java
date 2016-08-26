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

import com.example.adam.quickroster.R;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.Map;

public class EditStaffMemberActivity extends AppCompatActivity {

    private Button saveButton;
    private Button deleteButton;

    // widgets
    private TextView userName;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private Switch isManager;
    private TextView password;


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
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    /**
     * Updates the selected staff member
     */
    public void updateStaffMember() {
        // Create new user and set values
        if (staffMember == null) {
            Toast.makeText(getApplicationContext(), "Error: Can't find user to update", Toast.LENGTH_LONG).show();
            return;
        }

        // Add params to hashmap so we can parse to cloud code
        Map<String, Object> params = new HashMap<>();
        params.put("ObjectId", staffMember.getObjectId());
        params.put("isManager", !isManager.isPressed());
        params.put("userName", userName.getText().toString());
        params.put("email", email.getText().toString());
        params.put("firstName", firstName.getText().toString());
        params.put("lastName", lastName.getText().toString());
        String passwordText = password.getText().toString();
        params.put("password", passwordText);

        ParseCloud.callFunctionInBackground("updateUser", params, new FunctionCallback<String>() {
            @Override
            public void done(String object, ParseException e) {
                if(object != null)
                    Toast.makeText(getApplicationContext(), object, Toast.LENGTH_LONG);
                finish();
            }
        });
    }

    /**
     * Deletes the selected user using cloud code
     */
    public void deleteUser() {
        Map<String, String> params = new HashMap<>();
        params.put("ObjectId", staffMember.getObjectId());
        ParseCloud.callFunctionInBackground("deleteUser", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                Toast.makeText(getApplicationContext(), object.toString(), Toast.LENGTH_LONG);
                finishActivity(0);
            }
        });
    }

    /**
     * Called on create, sets the text edits to have the current values of the staff member.
     * This is passed in as an extra to this intent.
     */
    public void fillOutTextFields() {
        // Get values
        String usernameText = getIntent().getStringExtra("username");
        String firstNameText = getIntent().getStringExtra("firstName");
        String lastNameText = getIntent().getStringExtra("lastName");
        String emailText = getIntent().getStringExtra("email");
        String userID = getIntent().getStringExtra("staffID");
        boolean isManagerText = getIntent().getBooleanExtra("isManager", false);

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
        isManager.setChecked(isManagerText);
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
        password = (TextView) findViewById(R.id.editStaffPassword);
        deleteButton = (Button) findViewById(R.id.delete_staff_member_button);
        saveButton = (Button) findViewById(R.id.save_staff_member_button);
    }
}