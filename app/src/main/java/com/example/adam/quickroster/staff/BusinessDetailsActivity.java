package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.login.LoginActivity;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

public class BusinessDetailsActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText businessName;
    boolean allowedToEdit;
    ParseObject business;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        username = (EditText) findViewById(R.id.staffOwnUserName);
        password = (EditText) findViewById(R.id.editStaffOwnPassword);
        businessName = (EditText) findViewById(R.id.staffBusinessName);

        ParseUser currentUser = ParseUser.getCurrentUser();
        business = ParseQueryUtil.getParseUsersBusiness(currentUser);
        allowedToEdit = currentUser.getBoolean("isManager");

        if(!allowedToEdit)
            businessName.setEnabled(false);

        // Populate text edits
        username.setText(currentUser.getString("username"));
        password.setText("******");
        businessName.setText(business.getString("Name"));

        // Save button
        Button save = (Button)findViewById(R.id.saveStaffDetails);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    public void save(){
        String unText = username.getText().toString();
        String pwText = password.getText().toString();
        String businessNameText = businessName.getText().toString();

        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.put("username", unText);
        if(!pwText.equals("******"))
           currentUser.put("password", pwText);

        if(allowedToEdit) {
            business.put("Name", businessNameText);
        }
        currentUser.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null) {
                    Toast.makeText(getApplicationContext(), "Successfully updated details, " +
                            "please log in again", Toast.LENGTH_LONG).show();

                    // Request user to log in again
                    Intent intent = new Intent(BusinessDetailsActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }


            }
        });
    }
}
