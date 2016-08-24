package com.example.adam.quickroster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.parse.ParseUser;

public class StaffHomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button logout = (Button) findViewById(R.id.logOutButton);
        Button viewShifts = (Button) findViewById(R.id.viewShiftsButton);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        viewShifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StaffHomeActivity.this, CalendarViewActivity.class);
                startActivity(intent);
            }
        });
    }

    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(StaffHomeActivity.this, WelcomeActivity.class);
        startActivity(intent);
    }

}
