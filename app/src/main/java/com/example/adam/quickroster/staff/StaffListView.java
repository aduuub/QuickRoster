package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StaffListView extends Fragment {

    private ListView staffList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_staff_member_list_view, container, false);
        staffList = (ListView) view.findViewById(R.id.staffListView);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Staff");
        populateList();
        setHasOptionsMenu(true);
        return view;
    }


    /**
     * Populates the list view with all staff members in the current managers business. Adds managers
     * at the top, and the staff at the bottom
     */
    public void populateList(){
        // Get all staff of the business
        final List<ParseUser> allStaff = ParseQueryUtil.getAllUsers(ParseUser.getCurrentUser());

        staffList.setAdapter(new StaffListViewAdapter(getActivity(), allStaff));
        staffList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                staffMemberSelected(allStaff.get(position));
            }
        });
    }


    @Override
    public void onCreateOptionsMenu(
            Menu menu, MenuInflater inflater) {
        if(ParseUser.getCurrentUser().getBoolean("isManager")) {
            inflater.inflate(R.menu.add_staff_menu, menu);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_staff_member){
            Intent intentAddStaff = new Intent(getActivity(), AddStaffMemberActivity.class);
            ParseUser user = ParseUser.getCurrentUser();
            ParseObject business = user.getParseObject("Business");
            String businessID = business.getObjectId().toString();
            intentAddStaff.putExtra("BusinessID", businessID);
            startActivity(intentAddStaff);
            return true;
        }
        return false;
    }


    /**
     * Called when a staff member has been clicked on, transitions to the edit staff member activity.
     * @param user
     */
    public void staffMemberSelected(ParseUser user) {
        Intent modifyUserIntent = new Intent(getActivity().getApplicationContext(), EditStaffMemberActivity.class);
        modifyUserIntent.putExtra("username", user.getString("username"));
        modifyUserIntent.putExtra("firstName", user.getString("firstName"));
        modifyUserIntent.putExtra("lastName", user.getString("lastName"));
        modifyUserIntent.putExtra("email", user.getEmail());
        modifyUserIntent.putExtra("staffID", user.getObjectId());
        modifyUserIntent.putExtra("isManager", user.getBoolean("isManager"));
        startActivity(modifyUserIntent);
        populateList();
    }
}
