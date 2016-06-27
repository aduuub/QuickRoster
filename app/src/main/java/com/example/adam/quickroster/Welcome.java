package com.example.adam.quickroster;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.parse.ParseAnonymousUtils;
import com.parse.ParseObject;
import com.parse.ParseUser;

public class Welcome extends AppCompatActivity {

    Button registerBuisnessButton;
    Button loginAsUserButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())){
           // need to sign up

        }else{
            // already logged in

            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser != null){
                //login successful
                Intent intent = new Intent(Welcome.this, LoginSuccessful.class);
                startActivity(intent);
            }
        }

        registerBuisnessButton = (Button) findViewById(R.id.registerBuisnessButton);
        registerBuisnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, RegisterBuisness.class);
                startActivity(intent);

            }
        });

        loginAsUserButton = (Button) findViewById(R.id.loginAsUserButton);
        loginAsUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Welcome.this, LoginAsUser.class);
                startActivity(intent);
            }
        });




    }

}
