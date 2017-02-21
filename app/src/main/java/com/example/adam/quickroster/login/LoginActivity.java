package com.example.adam.quickroster.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.*;


import com.example.adam.quickroster.menu.Menu;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.CalendarShiftController;
import com.example.adam.quickroster.misc.ParseUtil;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.staff.StaffHomeActivity;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * This is where the staff member logs into the application using there user name and password
 */
public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView username;
    private TextView password;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressView = findViewById(R.id.login_user_progress);
        mLoginFormView = findViewById(R.id.login_user_progress);

        login = (Button) findViewById(R.id.loginAsUserButton);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
    }

    /**
     * Attempt to log the user in
     */
    public void login() {
        password.setError(null);
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();

        if (!isPasswordValid(passwordString)) {
            password.setError("Password must have 6 characters or more");
        }
        showProgress(true);
        try {
            // login
            ParseUser.logIn(usernameString, passwordString);
            ParseStaffUser user = (ParseStaffUser) ParseUser.getCurrentUser();

            if (user.isAuthenticated())
                ParseUtil.getInstance();
            boolean manager = user.isManager();

            // TODO update manager in case of change

            // Add new shifts to the calendar
            new UpdateShifts().execute(this);

            Intent intent = new Intent(LoginActivity.this, Menu.class);
            startActivity(intent);
            finish();

        } catch (ParseException e) {
            showProgress(false);
            e.printStackTrace();
            password.setError(e.getMessage());
            return;
        }
    }


    /**
     * Validate the password to ensure its correctly formatted
     *
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        return password.length() > 6;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    class UpdateShifts extends AsyncTask<LoginActivity, Void, Void> {

        @Override
        protected Void doInBackground(LoginActivity... params) {
            CalendarShiftController csc = new CalendarShiftController(params[0], getApplicationContext());
            csc.addNewShiftsToCalendar(getApplicationContext());
            return null;
        }
    }
}
