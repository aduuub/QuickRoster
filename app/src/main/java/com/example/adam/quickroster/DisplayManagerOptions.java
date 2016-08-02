package com.example.adam.quickroster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseUser;

public class DisplayManagerOptions extends AppCompatActivity implements View.OnClickListener {

    Button showCal;
    Button addShift;
    Button removeShift;
    Button addStaff;
    Button removeStaff;
    Button viewStaff;
    Button viewBuisInfo;
    Button logout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_view);

        // Set buttons
        addShift = (Button) findViewById(R.id.addNewShiftButton);
        removeShift = (Button) findViewById(R.id.managerRemoveShiftsButton);
        showCal = (Button) findViewById(R.id.viewAllShifts);
        addStaff = (Button) findViewById(R.id.addStaffMemeberButton);
        removeStaff = (Button) findViewById(R.id.removeStaffMemberButton);
        viewStaff = (Button) findViewById(R.id.viewStaffButton);
        viewBuisInfo = (Button) findViewById(R.id.managerViewBuisnessDetailsButton);
        logout = (Button) findViewById(R.id.logOutManagerButton);

        // Set onClick listeners
        addShift.setOnClickListener(this);
        removeShift.setOnClickListener(this);
        showCal.setOnClickListener(this);
        addStaff.setOnClickListener(this);
        removeStaff.setOnClickListener(this);
        viewStaff.setOnClickListener(this);
        viewBuisInfo.setOnClickListener(this);
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

            case R.id.addStaffMemeberButton:
                Intent intentAddStaff = new Intent(this, AddStaffMemeberActivity.class);
                ParseUser user = ParseUser.getCurrentUser();
                if(user == null){
                    Toast.makeText(getApplicationContext(), "Session Expired: Log in again"
                            ,Toast.LENGTH_LONG);
                    return;
                }
                ParseObject business = user.getParseObject("Business");
                String businessID = business.getObjectId().toString();
                intentAddStaff.putExtra("BusinessID", businessID);
                startActivity(intentAddStaff);
                break;

            case R.id.removeStaffMemberButton:
                Intent removeStaffIntent = new Intent(this, RemoveStaffActivity.class);
                startActivity(removeStaffIntent);
                break;

            case R.id.viewStaffButton:
                Intent viewStaff = new Intent(this, StaffMemberListView.class);
                startActivity(viewStaff);
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
        Intent intent = new Intent(DisplayManagerOptions.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

}
