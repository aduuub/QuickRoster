package com.example.adam.quickroster;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ManagerView extends AppCompatActivity implements View.OnClickListener {

    Button add;
    Button remove;
    Button showCal;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_view);

        add = (Button) findViewById(R.id.add);
        remove = (Button) findViewById(R.id.remove);
        showCal = (Button) findViewById(R.id.view);
        this.user = getIntent().getParcelableExtra("User");
        add.setOnClickListener(this);
        remove.setOnClickListener(this);
        showCal.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add:
                Intent intentAdd = new Intent(this, AddActivity.class);
                intentAdd.putExtra("User", user);
                startActivity(intentAdd);
                break;
            case R.id.remove:
                Intent intentRemove = new Intent(this, RemoveActivity.class);
                intentRemove.putExtra("User", user);
                startActivity(intentRemove);
                break;

            case R.id.view:
                Intent intentView = new Intent(this, calendarView.class); // TODO
                intentView.putExtra("User", user);
                startActivity(intentView);
                break;
        }

    }


}
