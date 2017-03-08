package com.example.adam.quickroster.staff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.Map;

/**
 * A view that provides a way to edit staff member details
 */
public class EditStaffMemberActivity extends AppCompatActivity {

    // UI
    private TextView userName;
    private TextView firstName;
    private TextView lastName;
    private TextView email;
    private Switch isManager;
    private TextView password;

    private ParseStaffUser selectedStaffMember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff_member);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Staff Member");
        setSupportActionBar(toolbar);

        setFields();
        fillOutTextFields();
    }

    /**
     * Updates the selected staff member
     */
    private void updateStaffMember() {
        // Create new user and set values
        if (selectedStaffMember == null) {
            return;
        }

        // Add params to hash map so we can parse to cloud code
        Map<String, Object> params = new HashMap<>();
        params.put("ObjectId", selectedStaffMember.getObjectId());
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
                if(e != null)
                    Log.e("Parse error", e.getMessage());
            }
        });
        finish();
    }


    /**
     * Deletes the selected user using cloud code
     */
    private void deleteUser() {
        Map<String, String> params = new HashMap<>();
        params.put("ObjectId", selectedStaffMember.getObjectId());
        ParseCloud.callFunctionInBackground("deleteUser", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e != null){

                }
            }
        });
    }


    /**
     * Called on create, sets the text edits to have the current values of the staff member.
     * This is passed in as an extra to this intent.
     */
    private void fillOutTextFields() {
        String objectId = getIntent().getStringExtra("objectId");

        // Get the Parse User
        selectedStaffMember = (ParseStaffUser) ParseStaffUser.getUserFromId(objectId);

        // Get values
        String usernameText = selectedStaffMember.getUsername();
        String firstNameText = selectedStaffMember.getFirstName();
        String lastNameText = selectedStaffMember.getLastName();
        String emailText = selectedStaffMember.getEmail();
        boolean isManagerText = selectedStaffMember.isManager();



        // Set values
        userName.setText(usernameText == null ? "" : usernameText);
        firstName.setText(firstNameText == null ? "" : firstNameText);
        lastName.setText(lastNameText == null ? "" : lastNameText);
        email.setText(emailText == null ? "" : emailText);
        isManager.setChecked(isManagerText);
        password.setText("******");
    }

    /**
     * Sets the fields
     */
    private void setFields() {
        userName = (TextView) findViewById(R.id.staffUserName);
        firstName = (TextView) findViewById(R.id.staffFirstName);
        lastName = (TextView) findViewById(R.id.staffLastName);
        email = (TextView) findViewById(R.id.email);
        isManager = (Switch) findViewById(R.id.isManagerSwitch);
        password = (TextView) findViewById(R.id.editStaffPassword);
        Button deleteButton = (Button) findViewById(R.id.delete_staff_member_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteUser();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_done) {
            updateStaffMember();
        }
        return true;
    }
}