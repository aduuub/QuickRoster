package com.example.adam.quickroster.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.*;

import com.example.adam.quickroster.R;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseUser;

/**
 * This welcomes the user when they first come to the application. It allows them to transition to
 * login as a user, or register a new business.
 */
public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    Button registerBuisnessButton;
    Button loginAsUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_welcome);

        if (!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            // already logged in
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(WelcomeActivity.this, LoginSuccessfulActivity.class);
                startActivity(intent);
                finish();
            }
        }

        // Buttons
        registerBuisnessButton = (Button)findViewById(R.id.registerBusinessButton);
        loginAsUserButton = (Button)findViewById(R.id.loginAsUserButton);
        registerBuisnessButton.setOnClickListener(this);
        loginAsUserButton.setOnClickListener(this);
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
