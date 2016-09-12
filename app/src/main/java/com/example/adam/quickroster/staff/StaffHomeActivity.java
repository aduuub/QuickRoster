package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.example.adam.quickroster.NoticeBoardActivity;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.staff_options.ContactEmployerActivity;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.login.WelcomeActivity;
import com.example.adam.quickroster.shifts.CalendarViewActivity;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class StaffHomeActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button viewShifts = (Button) findViewById(R.id.viewShiftsButton);
        Button contact = (Button) findViewById(R.id.messageEmployerButton);
        Button statistics = (Button) findViewById(R.id.staticsButton);
        Button noticeBoard = (Button) findViewById(R.id.staffNoticeBoardButton);
        Button accountDetails = (Button) findViewById(R.id.accountOptionsButton);
        Button logout = (Button) findViewById(R.id.logOutButton);

        contact.setOnClickListener(this);
        statistics.setOnClickListener(this);
        noticeBoard.setOnClickListener(this);
        accountDetails.setOnClickListener(this);
        viewShifts.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    /**
     * Log out the current user
     */
    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(StaffHomeActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {

            case R.id.viewShiftsButton:
                intent = new Intent(StaffHomeActivity.this, CalendarViewActivity.class);
                startActivity(intent);
                break;

            case R.id.messageEmployerButton:
                intent = new Intent(StaffHomeActivity.this, ContactEmployerActivity.class);
                startActivity(intent);
                break;

            case R.id.staticsButton:
                // TODO
                break;

            case R.id.staffNoticeBoardButton:
                intent = new Intent(this, NoticeBoardActivity.class);
                startActivity(intent);
                break;

            case R.id.accountOptionsButton:
                intent = new Intent(this, BusinessDetailsActivity.class);
                startActivity(intent);
                break;

            case R.id.logOutButton:
                logout();
                break;
        }
    }

    /**
     * Sends an email to to the current parse users business
     */
    private void sendEmail() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject business = ParseQueryUtil.getParseUsersBusiness(currentUser);
        String email = business.getString("email");


        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        //  Fill it the with extra data
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Example Text");
        startActivity(Intent.createChooser(emailIntent, "Send Employer Email"));
    }
}
