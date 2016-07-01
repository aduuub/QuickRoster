package com.example.adam.quickroster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class LoginSuccessful extends AppCompatActivity implements View.OnClickListener {

    Button continueButton;
    Button logoutButton;
    TextView displayWelcomeText;
    boolean isManager = false;
    String name = "default";
    private ParseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_successful);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        continueButton = (Button) findViewById(R.id.continueButton2);
        logoutButton = (Button) findViewById(R.id.logOut);
        displayWelcomeText = (TextView) findViewById(R.id.loggedInText2);
        continueButton.setOnClickListener(this);
        logoutButton.setOnClickListener(this);
        user = ParseUser.getCurrentUser();

        if (user != null) {
            if (user.containsKey("isManager")) {
                try {
                    user.fetch();
                } catch (ParseException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                    return;
                }
                // Success!
                this.isManager = user.getBoolean("isManager");
                this.name = user.getString("firstName");
                setWelcomeText();

            } else {
                // Failure!
                Toast.makeText(getApplicationContext(), "Error loading", Toast.LENGTH_LONG).show();
            }
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.continueButton2:
                if (isManager) {
                    Intent intent = new Intent(LoginSuccessful.this, DisplayManagerOptions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginSuccessful.this, StaffHomeActivity.class);
                    startActivity(intent);
                }
                break;

            case R.id.logOut:
                ParseUser.logOut();
                Intent intent = new Intent(LoginSuccessful.this, Welcome.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * Sets the welcome text. Takes the default message and adds the users name and staff type
     */
    public void setWelcomeText() {
        String userType = this.isManager ? "Manager" : "Staff Member";
        StringBuilder welcomeText = new StringBuilder();
        if (name != null)
            welcomeText.append("Welcome: " + name + ". \n \n");

        welcomeText.append("Thank you for choosing QuickRoster. We know you are going to love it. Please continue to get started. \n \n");
        welcomeText.append("You are logged in as: " + userType + ". \n");
        displayWelcomeText.append(welcomeText.toString());
    }

    public void setManager(boolean b) {
        this.isManager = b;
    }

    public void setName(String s) {
        this.name = s;
    }


}
