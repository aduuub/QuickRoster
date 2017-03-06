package com.example.adam.quickroster.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.*;
import java.util.Arrays;
import java.util.List;


import com.example.adam.quickroster.menu.Menu;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.Util;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

/**
 * This is where the staff member logs into the application using there user name and mPasswordTextView
 */
public class LoginActivity extends AppCompatActivity {

    private Button login;
    private TextView mUserNameTextView;
    private TextView mPasswordTextView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mProgressView = findViewById(R.id.login_user_progress);
        mLoginFormView = findViewById(R.id.login_form_view);

        login = (Button) findViewById(R.id.loginAsUserButton);
        mUserNameTextView = (EditText) findViewById(R.id.username);
        mPasswordTextView = (EditText) findViewById(R.id.password);
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
        String usernameString = mUserNameTextView.getText().toString();
        String passwordString = mPasswordTextView.getText().toString();

        // Check input is correct
        String errorMessage = isInputValid();
        if (errorMessage != null) {
            displayInputAlert(errorMessage);
            return;
        }

        showProgress(true);

        // login
        ParseUser.logInInBackground(usernameString, passwordString, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    // Error
                    showProgress(false);
                    displayInputAlert(e.getMessage());
                    return;
                }

                Intent intent = new Intent(LoginActivity.this, Menu.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        showProgress(false);
    }

    /**
     * @return - null if free of errors. Else it returns the error message
     */
    private String isInputValid() {
        List<String> fields = Arrays.asList(mUserNameTextView.getText().toString(),
                mPasswordTextView.getText().toString());
        for (String s : fields) {
            if (s == null || s.equals("")) {
                return getString(R.string.invalid_input_message);
            }
        }
        // Check mPasswordTextView
        return Util.isPasswordValid(mPasswordTextView.getText().toString());
    }


    /**
     * Alerts the user that the input is invalid.
     *
     * @param message - message to display
     */
    private void displayInputAlert(String message) {
        AlertDialog alertDialog = new AlertDialog.Builder(LoginActivity.this).create();
        alertDialog.setTitle(getString(R.string.invalid_input_title));
        alertDialog.setMessage(message);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }


    /**
     * Shows the progress UI and hides the login form.
     * @param show - sets the progress view to be visible or gone
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
//                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//                @Override
//                public void onAnimationEnd(Animator animation) {
//                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
//                }
//            });a

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
}
