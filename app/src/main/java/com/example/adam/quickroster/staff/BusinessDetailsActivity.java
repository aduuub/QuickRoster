package com.example.adam.quickroster.staff;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.adam.quickroster.R;

public class BusinessDetailsActivity extends AppCompatActivity {

    EditText username;
    EditText password;
    EditText businessName;

    boolean allowedToEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        allowedToEdit = getIntent().getBooleanExtra("editable", false);
        username = (EditText) findViewById(R.id.staffOwnUserName);
        password = (EditText) findViewById(R.id.editStaffOwnPassword);
        businessName = (EditText) findViewById(R.id.staffBusinessName);

        Button save = (Button)findViewById(R.id.saveStaffDetails);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
    }

    public void save(){
        String unText = username.getText().toString();
        String pwText = password.getText().toString();
        String buisName = businessName.getText().toString();

    }
}
