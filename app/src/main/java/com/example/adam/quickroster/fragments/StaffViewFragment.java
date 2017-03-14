package com.example.adam.quickroster.fragments;

import android.content.Intent;
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

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.example.adam.quickroster.staff.AddStaffMemberActivity;
import com.example.adam.quickroster.staff.StaffAdapter;
import com.example.adam.quickroster.staff.StaffViewActivity;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

public class StaffViewFragment extends Fragment {

    private ListView staffList;


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
    private void populateList() {
        // Get all staff of the business
        final List<ParseUser> allStaff = ParseQueryUtil.getAllUsers(ParseUser.getCurrentUser());

        staffList.setAdapter(new StaffAdapter(getActivity(), allStaff));
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
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            inflater.inflate(R.menu.add_menu, menu);
        }
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_icon_add) {
            Intent intentAddStaff = new Intent(getActivity(), AddStaffMemberActivity.class);
            ParseUser user = ParseUser.getCurrentUser();
            ParseObject business = user.getParseObject("Business");
            String businessID = business.getObjectId();
            intentAddStaff.putExtra("BusinessID", businessID);
            startActivity(intentAddStaff);
            return true;
        }
        return false;
    }


    /**
     * Called when a staff member has been clicked on, transitions to the edit staff member activity.
     */
    private void staffMemberSelected(ParseUser user) {
        Intent modifyUserIntent = new Intent(getActivity().getApplicationContext(), StaffViewActivity.class);
        modifyUserIntent.putExtra("objectId", user.getObjectId());
        startActivity(modifyUserIntent);
    }
}
