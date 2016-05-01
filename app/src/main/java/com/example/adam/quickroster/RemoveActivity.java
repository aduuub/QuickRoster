package com.example.adam.quickroster;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class RemoveActivity extends AppCompatActivity implements View.OnClickListener {

    // XML
    private Spinner selectStaffSpinner;
    private Spinner selectShiftSpinner;
    private Button submit;
    private ArrayList<User> allUsers = Home.getAllUsers();

    private User selectedUser;
    private Shift selectedShift;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selectStaffSpinner = (Spinner) findViewById(R.id.selectStaffToModify);
        selectShiftSpinner = (Spinner) findViewById(R.id.selectShiftToModify);
        submit = (Button) findViewById(R.id.removeShiftButton);
        submit.setOnClickListener(this);
        createStaffSpinner();



    }




    public void createStaffSpinner() {

        // create the the staff selector
        final List<User> spinnerArray = new ArrayList<User>();
        List<String> spinnerArrayNames = new ArrayList<String>();

        for (User u : allUsers) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectStaffSpinner.setAdapter(adapter);
        selectStaffSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // find the user
                User user = spinnerArray.get(position);
                createShiftSpinner(user);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });

    }


    public void createShiftSpinner(User user) {
        this.selectedUser = user;

        if(user.getShifts() == null)
            return;

        // create the the staff selector
        final List<Shift> spinnerArray = new ArrayList<Shift>();
        List<String> spinnerArrayNames = new ArrayList<String>();

        for (Shift u : user.getShifts()) {
            spinnerArray.add(u);
            spinnerArrayNames.add(u.getDetails());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerArrayNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectShiftSpinner.setAdapter(adapter);
        selectShiftSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Shift s = spinnerArray.get(position);
                storeShift(s);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO
            }
        });



    }


    public void onClick(View v){

        if(selectedShift == null && selectedUser == null)
            return;

        selectedUser.removeShift(selectedShift);
        finish();
        startActivity(getIntent());
    }

    public void storeShift(Shift s){
        this.selectedShift = s;
    }



}
