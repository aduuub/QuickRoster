package com.example.adam.quickroster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.*;
import java.util.ArrayList;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginAsUser extends AppCompatActivity {

    private Button login;
    private TextView username;
    private TextView password;
    private TextView invalid;

    private static ArrayList<User> users;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        login = (Button) findViewById(R.id.login);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }


    public void login() {
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        if (!isPasswordValid(passwordString)) {
            password.setError("Password must have 6 characters or more");
        }

        ParseUser.logInInBackground(usernameString, passwordString, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    // Hooray! The user is logged in.
                    boolean manager = false;
                    if (user.containsKey("isManager")) {
                        if (user.getBoolean("isManager")) {
                            manager = true;
                        }
                    }

                    User loggedInUser = new User("", "", "", false, null);

                    if (manager) {
                        Intent intent = new Intent(LoginAsUser.this, Welcome.class);
                        intent.putExtra("User", loggedInUser);
                        intent.putParcelableArrayListExtra("allUsers", users);
                        startActivity(intent);

                    } else {
                        Intent intent = new Intent(LoginAsUser.this, Welcome.class);
                        intent.putExtra("User", loggedInUser);
                        startActivity(intent);
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "An error occured trying to login", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

//    public void saveToFile() {
//        Gson gson = new Gson();
//        String text = gson.toJson(users);
//
//        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        defaultSharedPreferences.edit().putString("Users", text).apply();
//    }
//
//    public void loadFile() {
//
//        Gson gson = new Gson();
//        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String read = defaultSharedPreferences.getString("Users", null);
//
//        this.users = gson.fromJson(read, new TypeToken<ArrayList<User>>() {
//        }.getType());
//    }

    public static ArrayList<User> getAllUsers() {
        return users;
    }

    private boolean isEmailValid(String email) {
        return email.contains("@") && email.contains(".");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }
}
