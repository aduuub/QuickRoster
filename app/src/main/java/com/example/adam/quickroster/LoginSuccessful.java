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

public class LoginSuccessful extends AppCompatActivity {

    Button continueButton;
    Button logoutButton;
    TextView displayWelcomeText;
    boolean isManager = false;
    String name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int content_login_successful = R.layout.content_login_successful;
        setContentView(content_login_successful);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        continueButton = (Button) findViewById(R.id.continueButton2);
        logoutButton = (Button) findViewById(R.id.logOut);
        displayWelcomeText = (TextView) findViewById(R.id.loggedInText2);

        final ParseUser user = ParseUser.getCurrentUser();
        if (user != null) {
            if (user.containsKey("isManager")) {
                user.fetchInBackground(new GetCallback<ParseObject>() {
                    public void done(ParseObject object, ParseException e) {
                        if (e == null) {
                            // Success!
                            boolean isManager = user.getBoolean("isManager");
                            if (name == null)
                                name = user.getString("lastName");

                            setManager(isManager);
                            setName(name);
                        } else {
                            // Failure!
                        }
                    }
                });
            }
        }

        String name = user.getString("firstName");
        String lastName = user.getString("lastName");
        String userType = this.isManager ? "Manager" : "Staff Member";


        ParseUser parseUser = ParseUser.getCurrentUser();
        String userObj = parseUser.getObjectId();
        ParseQuery<ParseStaffUser> query = new ParseQuery<ParseStaffUser>("User");
        //query.whereEqualTo("details", "a");

        query.findInBackground(new FindCallback<ParseStaffUser>() {
                                   @Override
                                   public void done(List<ParseStaffUser> objects, ParseException e) {
                                       if (e != null) {
                                           Toast.makeText(getApplicationContext(), "Error loading shifts " + e, Toast.LENGTH_LONG).show();
                                       }
                                   }
                               }
        );

        displayWelcomeText.append(userType);
        if (name != null) {
            String welcomeUser = "\n Current user: " + name != null ? name : ""
                    + " " + lastName != null ? lastName : "";
            displayWelcomeText.append(welcomeUser);
        }
        // TODO Fix welcome text
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isManager) {
                    Intent intent = new Intent(LoginSuccessful.this, DisplayManagerOptions.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(LoginSuccessful.this, StaffHomeActivity.class);
                    startActivity(intent);
                }
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ParseUser.logOut();
                Intent intent = new Intent(LoginSuccessful.this, Welcome.class);
                startActivity(intent);
            }
        });
    }

    public void setManager(boolean b) {
        this.isManager = b;
    }

    public void setName(String s) {
        this.name = s;
    }


}
