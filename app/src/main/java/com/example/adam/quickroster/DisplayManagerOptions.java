package com.example.adam.quickroster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.parse.ParseUser;

public class DisplayManagerOptions extends AppCompatActivity implements View.OnClickListener {

    Button showCal;
    Button addShift;
    Button removeShift;
    Button addStaff;
    Button removeStaff;
    Button viewStaff;
    Button viewBuisInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_view);

        addShift = (Button) findViewById(R.id.addNewShiftButton);
        removeShift = (Button) findViewById(R.id.managerRemoveShiftsButton);
        showCal = (Button) findViewById(R.id.viewAllShifts);
        addStaff = (Button) findViewById(R.id.addStaffMemeberButton);
        removeStaff = (Button) findViewById(R.id.removeStaffMemeberButton);
        viewStaff = (Button) findViewById(R.id.viewCurrentStaffButton);
        viewBuisInfo = (Button) findViewById(R.id.managerViewBuisnessDetailsButton);


        addShift.setOnClickListener(this);
        removeShift.setOnClickListener(this);
        showCal.setOnClickListener(this);
        addStaff.setOnClickListener(this);
        removeStaff.setOnClickListener(this);
        viewStaff.setOnClickListener(this);
        viewBuisInfo.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addNewShiftButton:
                Intent intentAdd = new Intent(this, ManagerAddActivity.class);
                startActivity(intentAdd);
                break;

            case R.id.managerRemoveShiftsButton:
                Intent intentRemove = new Intent(this, ManagerRemoveActivity.class);
                startActivity(intentRemove);
                break;

            case R.id.viewAllShifts:
                Intent intentView = new Intent(this, CalendarView.class);
                startActivity(intentView);
                break;

            case R.id.addStaffMemeberButton:
                Intent intentAddStaff = new Intent(this, AddStaffMemeberActivity.class);
                startActivity(intentAddStaff);
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
        Intent intent = new Intent(DisplayManagerOptions.this, Welcome.class);
        startActivity(intent);
    }


}
