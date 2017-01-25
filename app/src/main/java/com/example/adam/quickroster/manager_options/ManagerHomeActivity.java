package com.example.adam.quickroster.manager_options;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.adam.quickroster.NoticeBoardActivity;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.login.WelcomeActivity;
import com.example.adam.quickroster.shifts.AddShiftActivity;
import com.example.adam.quickroster.shifts.CalendarViewActivity;
import com.example.adam.quickroster.shifts.RemoveShiftActivity;
import com.example.adam.quickroster.staff.StaffListView;
import com.parse.ParseUser;

/**
 * This is the managers home view. Its where they are taken once they have been logged in,
 * and also provides buttons to the various tasks they can perform
 */
public class ManagerHomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button showCal;
    Button addShift;
    Button removeShift;
    Button viewStaff;
    Button noticeBoardButton;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set buttons
//        addShift = (Button) findViewById(R.id.addNewShiftButton);
//        showCal = (Button) findViewById(R.id.viewAllShifts);
//        removeShift = (Button) findViewById(R.id.managerRemoveShiftsButton);
//        viewStaff = (Button) findViewById(R.id.viewStaffButton);
//        noticeBoardButton = (Button) findViewById(R.id.noticeBoardButton);
//        logout = (Button) findViewById(R.id.logOutManagerButton);

        // Set onClick listeners
//        addShift.setOnClickListener(this);
//        removeShift.setOnClickListener(this);
//        showCal.setOnClickListener(this);
//        viewStaff.setOnClickListener(this);
//        noticeBoardButton.setOnClickListener(this);
//        logout.setOnClickListener(this);
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.addNewShiftButton:
//                Intent intentAdd = new Intent(this, AddShiftActivity.class);
//                startActivity(intentAdd);
//                break;
//
//            case R.id.managerRemoveShiftsButton:
//                Intent intentRemove = new Intent(this, RemoveShiftActivity.class);
//                startActivity(intentRemove);
//                break;
//
//            case R.id.viewAllShifts:
//                Intent intentView = new Intent(this, CalendarViewActivity.class);
//                startActivity(intentView);
//                break;
//
//            case R.id.viewStaffButton:
//                Intent viewStaff = new Intent(this, StaffListView.class);
//                startActivity(viewStaff);
//                break;
//
//            case R.id.noticeBoardButton:
//                Intent noticeBoard = new Intent(this, NoticeBoardActivity.class);
//                startActivity(noticeBoard);
//                break;
//
//            case R.id.logOutManagerButton:
//                logout();
//        }
//    }


    /**
     * Logs out the current Parse User
     */
    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(ManagerHomeActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        return false;
    }
}
