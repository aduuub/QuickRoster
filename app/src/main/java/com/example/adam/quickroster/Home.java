package com.example.adam.quickroster;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.lang.*;
import java.lang.reflect.Array;
import java.util.ArrayList;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class Home extends AppCompatActivity {

    private Button login;
    private TextView userName;
    private TextView passWord;
    private TextView invalid;

    private static ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        loadFile();

        toolbar.setTitle("Quick Roster");

        login = getLoginButton();
        userName = getUserName();
        passWord = getPassword();
        invalid = getInvalid();

        Shift exampleShift1 = new Shift(1461931200000l, 1461960000000l, 1461992400000l, "Adam Wareing's Shift");
        Shift exampleShift2 = new Shift(1461931200000l, 1461960000000l, 1461992400000l, "Adam Wareing's Shift");

        Shift exampleShift3 = new Shift(1461931200000l, 1461960000000l, 1461992400000l, "Elf's Shift");
        Shift exampleShift4 = new Shift(1461931200000l, 1461960000000l, 1461992400000l, "Elf's Shift");

        ArrayList<Shift> shifts = new ArrayList<Shift>();
        shifts.add(exampleShift1);
        shifts.add(exampleShift2);


        ArrayList<Shift> shifts2 = new ArrayList<Shift>();
        shifts2.add(exampleShift3);
        shifts2.add(exampleShift4);


        // generate user
        this.users = new ArrayList<User>();
        User manager1 = new User("haydn", "haydn", "Haydn Banister: Manager", true, null);
        User manager2 = new User("a", "a", "Adam_Wareing: Manager", true, shifts);
        User staff = new User("elf", "elf", "Elf: Staff", false, shifts2);
        this.users.add(manager1);
        this.users.add(manager2);
        this.users.add(staff);

        saveToFile();

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {

                                         String userName = getUserName().getText().toString();
                                         String passWord = getPassword().getText().toString();

                                         User loggedInUser = null;
                                         // check valid users
                                         for (User u : users) {
                                             if (u.validLogin(userName, passWord)) {
                                                 // we are now logged in?
                                                 loggedInUser = u;
                                                 break;
                                             }
                                         }
                                         if (loggedInUser != null) {
                                             // transition to new view

                                             if (loggedInUser.isManager()) {
                                                 Intent intent = new Intent(Home.this, ManagerView.class);
                                                 intent.putExtra("User", loggedInUser);
                                                 intent.putParcelableArrayListExtra("allUsers", users);

                                                 startActivity(intent);

                                             } else {
                                                 Intent intent = new Intent(Home.this, calendarView.class);
                                                 intent.putExtra("User", loggedInUser);
                                                 startActivity(intent);
                                             }
                                         } else {
                                             invalid.setText("Invalid Username or Password");
                                         }
                                     }
                                 }

        );

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void saveToFile() {
        Gson gson = new Gson();
        String text = gson.toJson(users);

        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        defaultSharedPreferences.edit().putString("Users", text).apply();
    }

    public void loadFile() {

        Gson gson = new Gson();
        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String read = defaultSharedPreferences.getString("Users", null);

        this.users = gson.fromJson(read, new TypeToken<ArrayList<User>>() {
        }.getType());
    }


    public Button getLoginButton() {
        return (Button) findViewById(R.id.login);
    }

    public TextView getUserName() {
        return (TextView) findViewById(R.id.username);
    }

    public TextView getPassword() {
        return (TextView) findViewById(R.id.password);
    }

    public TextView getInvalid() {
        return (TextView) findViewById(R.id.invalid);
    }

    public static ArrayList<User> getAllUsers() {
        return users;
    }

}
