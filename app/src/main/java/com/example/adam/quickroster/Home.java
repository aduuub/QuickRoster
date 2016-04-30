package com.example.adam.quickroster;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.format.Time;
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
import java.util.ArrayList;


import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Button login;
    private TextView userName;
    private TextView passWord;
    private TextView invalid;

    private ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        toolbar.setTitle("Quick Roster");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        login = getLoginButton();
        userName = getUserName();
        passWord = getPassword();
        invalid = getInvalid();

        Shift exampleShift1 = new Shift(1461931200000l,1461931200320l, 1461931200300l, "Work details" );
        Shift exampleShift2 = new Shift(1461931200000l,1461931200320l, 1461931200320l, "Work details2" );

        ArrayList<Shift> shifts = new ArrayList<Shift>();
        shifts.add(exampleShift1);
        shifts.add(exampleShift2);
        // generate user
        this.users = new ArrayList<User>();
        User manager1 = new User("haydn01", "haydn01", "Haydn_Banister", true, shifts);
        User manager2 = new User("a", "a", "Adam_Wareing", true, shifts);
        User staff = new User("elf01", "elf01", "Elf_Eldridge", false, shifts);
        this.users.add(manager1);
        this.users.add(manager2);
        this.users.add(staff);

        login.setOnClickListener(new View.OnClickListener() {
                                     @Override
                                     public void onClick(View view) {
                                         String userName = getUserName().getText().toString();
                                         String passWord = getPassword().getText().toString();

                                         User loggedInUser = null;
                                          // check valid users
                                         for(User u : users){
                                             if(u.validLogin(userName, passWord)) {
                                                 // we are now logged in?
                                                 loggedInUser = u;
                                                 break;
                                             }
                                         }
                                         if(loggedInUser != null){
                                             // transition to new view
                                             Intent intent = new Intent(Home.this, calendarView.class);
                                             intent.putExtra("User", loggedInUser);
                                             startActivity(intent);

                                         }else{
                                             invalid.setText("Invalid Username or Password");
                                         }
                                     }
                                 }

        );

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
