package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.example.adam.quickroster.staff_options.ContactEmployerActivity;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.login.WelcomeActivity;
import com.example.adam.quickroster.shifts.CalendarViewActivity;
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
                // TODO
                break;

            case R.id.accountOptionsButton:
                // TODO
                break;

            case R.id.logOutButton:
                logout();
                break;
        }
    }
}
