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

import java.util.HashMap;
import java.util.Map;

/**
 * Edit a staff members details or delete the staff member. This calls functions in Cloud Code to perform theses actions as they require the master key
 * in order to perform the actions, as the current staff member isn't logged in.
 *
 * @author Adam Wareing
 */
public class EditStaffMemberActivity extends AppCompatActivity {

    private TextView mUserNameTextView;
    private TextView mFirstNameTextView;
    private TextView mLastNameTextView;
    private TextView mEmailTextView;
    private TextView mPhoneTextView;
    private TextView mPasswordTextView;
    private Switch mIsManagerSwitch;

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
     * Updates the selected staff member. It puts the staff member details as a parameter for the "updateUser" Parse Cloud function, which updates
     * the user using the master key. Once it updates the staff member it finishes the activity.
     */
    private void updateStaffMember() {
        // Create new user and set values
        if (selectedStaffMember == null) {
            return;
        }

        // Add params to hash map so we can parse to cloud code
        Map<String, Object> params = new HashMap<>();
        params.put("objectId", selectedStaffMember.getObjectId());
        params.put("isManager", !mIsManagerSwitch.isPressed());
        params.put("username", mUserNameTextView.getText().toString());
        params.put("email", mEmailTextView.getText().toString());
        params.put("firstName", mFirstNameTextView.getText().toString());
        params.put("lastName", mLastNameTextView.getText().toString());
        params.put("mobileNumber", mPhoneTextView.getText().toString());
        params.put("password", mPasswordTextView.getText().toString());

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
        params.put("objectId", selectedStaffMember.getObjectId());
        ParseCloud.callFunctionInBackground("deleteUser", params, new FunctionCallback<Object>() {
            @Override
            public void done(Object object, ParseException e) {
                if(e != null){
                    Log.e("Parse error", e.getMessage());
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
        String phoneText = selectedStaffMember.getMobileNumber();
        boolean isManagerText = selectedStaffMember.isManager();

        // Set values
        mUserNameTextView.setText(usernameText == null ? "" : usernameText);
        mFirstNameTextView.setText(firstNameText == null ? "" : firstNameText);
        mLastNameTextView.setText(lastNameText == null ? "" : lastNameText);
        mEmailTextView.setText(emailText == null ? "" : emailText);
        mPhoneTextView.setText(phoneText == null ? "" : phoneText);
        mIsManagerSwitch.setChecked(isManagerText);
        mPasswordTextView.setText("******");
    }

    /**
     * Sets the fields for the components and sets an on click listener for the delete button.
     */
    private void setFields() {
        mUserNameTextView = (TextView) findViewById(R.id.staff_user_name);
        mFirstNameTextView = (TextView) findViewById(R.id.staff_first_name);
        mLastNameTextView = (TextView) findViewById(R.id.staff_last_name);
        mEmailTextView = (TextView) findViewById(R.id.email);
        mPhoneTextView = (TextView) findViewById(R.id.phone);
        mIsManagerSwitch = (Switch) findViewById(R.id.is_manager_switch);
        mPasswordTextView = (TextView) findViewById(R.id.editStaffPassword);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.done_delete_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_done) {
            updateStaffMember();
        }else if(item.getItemId() == R.id.menu_icon_done){
            deleteUser();
            finish();
        }else{
            return false; // Error
        }
        return true;
    }
}