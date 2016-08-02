package com.example.adam.quickroster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class RemoveStaffActivity extends AppCompatActivity implements View.OnClickListener {

    // XML
    private Spinner selectStaffSpinner;
    private Button removeButton;

    private ParseUser selectedUser;
    private List<ParseUser> allUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_remove_staff);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectStaffSpinner = (Spinner) findViewById(R.id.selectStaffRemove);
        removeButton = (Button) findViewById(R.id.removeStaffButton);
        removeButton.setOnClickListener(this);
        createStaffSpinner();
    }

    /**
     * Creates the spinner to select the staff member. This calls createShiftSpinner() once a user
     * is selected
     */
    public void createStaffSpinner() {
        final List<ParseUser> spinnerArray = new ArrayList<ParseUser>();
        List<String> spinnerArrayNames = new ArrayList<String>();
        allUsers = AddShiftActivity.getAllUsers(ParseUser.getCurrentUser());

        for (ParseUser u : allUsers) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getString("firstName") + " " + u.getString("lastName"));
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStaffSpinner.setAdapter(adapter);
        selectStaffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setSelectedStaff(spinnerArray.get(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });
    }

    public void setSelectedStaff(ParseUser user){
        selectedUser = user;
    }

    /**
     * On button click, delete the shift in the background
     *
     * @param v
     */
    public void onClick(View v) {

        if (selectedUser == null) {
            Toast.makeText(getApplicationContext(), "Please select a staff member to delete", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            selectedUser.delete();
        } catch (ParseException e) {
            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            return;
        }
        createStaffSpinner();
        Toast.makeText(getApplicationContext(), "Successfully Deleted Shift", Toast.LENGTH_SHORT).show();
    }
}
