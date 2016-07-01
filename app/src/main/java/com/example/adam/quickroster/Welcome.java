package com.example.adam.quickroster;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.*;

import com.parse.ParseAnonymousUtils;
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

        if(!ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())){
           // already logged in
            ParseUser currentUser = ParseUser.getCurrentUser();
            if(currentUser != null){
                Intent intent = new Intent(Welcome.this, LoginSuccessful.class);
                startActivity(intent);
                finish();
            }
        }

        // need to sign up or login
        registerBuisnessButton = (Button) findViewById(R.id.registerBusinessButton);
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
                Intent intent = new Intent(Welcome.this, LoginActivity.class);
                startActivity(intent);
            }
        });




    }

}
