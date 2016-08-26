package com.example.adam.quickroster.manager_options;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ManagerHomeActivity extends AppCompatActivity implements View.OnClickListener {

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

        // Set buttons
        addShift = (Button) findViewById(R.id.addNewShiftButton);
        showCal = (Button) findViewById(R.id.viewAllShifts);
        removeShift = (Button) findViewById(R.id.managerRemoveShiftsButton);
        viewStaff = (Button) findViewById(R.id.viewStaffButton);
        noticeBoardButton = (Button) findViewById(R.id.noticeBoardButton);
        logout = (Button) findViewById(R.id.logOutManagerButton);

        // Set onClick listeners
        addShift.setOnClickListener(this);
        removeShift.setOnClickListener(this);
        showCal.setOnClickListener(this);
        viewStaff.setOnClickListener(this);
        noticeBoardButton.setOnClickListener(this);
        logout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewShiftButton:
                Intent intentAdd = new Intent(this, AddShiftActivity.class);
                startActivity(intentAdd);
                break;

            case R.id.managerRemoveShiftsButton:
                Intent intentRemove = new Intent(this, RemoveShiftActivity.class);
                startActivity(intentRemove);
                break;

            case R.id.viewAllShifts:
                Intent intentView = new Intent(this, CalendarViewActivity.class);
                startActivity(intentView);
                break;

            case R.id.viewStaffButton:
                Intent viewStaff = new Intent(this, StaffListView.class);
                startActivity(viewStaff);
                break;

            case R.id.noticeBoardButton:
                Intent noticeBoard = new Intent(this, NoticeBoardActivity.class);
                startActivity(noticeBoard);
                break;

            case R.id.logOutManagerButton:
                logout();
        }
    }

    /**
     * Logs out the current Parse User
     */
    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(ManagerHomeActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}
