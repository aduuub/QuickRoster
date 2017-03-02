package com.example.adam.quickroster.login;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.menu.Menu;
import com.example.adam.quickroster.misc.CalendarShiftController;
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.misc.Util;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

/**
 * This welcomes the user when they first come to the application. It allows them to transition to
 * login as a user, or register a new business.
 */
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mRegisterBusinessButton;
    private Button mLoginAsUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Welcome!");
        setSupportActionBar(toolbar);
        ParseUser currentUser = ParseUser.getCurrentUser();

        new Util.UpdateShifts().execute(this);


        if (!ParseAnonymousUtils.isLinked(currentUser)) {
            // Already logged in and not an anon user
            if (ParseUser.getCurrentUser().isAuthenticated()){
                // Update current user
                ParseUtil.getInstance();
                ParseStaffUser parseStaffUser = ParseUtil.getCurrentUser();
                parseStaffUser.fetchInBackground();
                parseStaffUser.setLocalManager(parseStaffUser.getBoolean("isManager"));
            }

            Intent intent = new Intent(WelcomeActivity.this, Menu.class);
            startActivity(intent);
            finish();

        }

        // Buttons
        mRegisterBusinessButton = (Button) findViewById(R.id.registerBusinessButton);
        mLoginAsUserButton = (Button) findViewById(R.id.loginAsUserButton);
        mRegisterBusinessButton.setOnClickListener(this);
        mLoginAsUserButton.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.registerBusinessButton:
                intent = new Intent(WelcomeActivity.this, RegisterBusinessActivity.class);
                startActivity(intent);
                break;

            case R.id.loginAsUserButton:
                intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


}
