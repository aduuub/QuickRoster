package com.example.adam.quickroster;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StaffMemberListView extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_member_list_view);
        ListView list = (ListView) findViewById(R.id.staffListView);

        // set Floating Action Button
        FloatingActionButton addStaffFab = (FloatingActionButton) findViewById(R.id.add_staff_member_fab);
        addStaffFab.setOnClickListener(this);
        //addStaffFab.setImageResource(R.drawable.add_button);


        // get all staff of the business
        final List<ParseUser> allStaff = AddShiftActivity.getAllUsers(ParseUser.getCurrentUser());
        Collections.sort(allStaff, new Comparator<ParseUser>() {
            @Override
            public int compare(ParseUser lhs, ParseUser rhs) {
                boolean left = lhs.getBoolean("isManager");
                boolean right = rhs.getBoolean("isManager");
                if (left && right)
                    return 0;
                if (left && !right)
                    return 1;
                else
                    return -1;
            }
        });

        list.setAdapter(new StaffListViewAdapter(this, allStaff));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                staffMemberSelected(allStaff.get(position));
            }
        });

        }

        @Override
        public void onClick (View v){

            if (v.getId() == R.id.add_staff_member_fab) {
                Intent intentAddStaff = new Intent(this, AddStaffMemeberActivity.class);
                ParseUser user = ParseUser.getCurrentUser();
                if (user == null) {
                    Toast.makeText(getApplicationContext(), "Session Expired: Log in again"
                            , Toast.LENGTH_LONG);
                    return;
                }
                ParseObject business = user.getParseObject("Business");
                String businessID = business.getObjectId().toString();
                intentAddStaff.putExtra("BusinessID", businessID);
                startActivity(intentAddStaff);
            }
        }

        public void staffMemberSelected(ParseUser user){
            Intent modifyUserIntent = new Intent(getApplicationContext(), EditStaffMemeberActivity.class);
            modifyUserIntent.putExtra("username", user.getString("username"));
            modifyUserIntent.putExtra("firstName", user.getString("firstName"));
            modifyUserIntent.putExtra("lastName", user.getString("lastName"));
            modifyUserIntent.putExtra("email", user.getEmail());
            modifyUserIntent.putExtra("staffID", user.getObjectId());
            startActivity(modifyUserIntent);
        }
    }
