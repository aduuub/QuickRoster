package com.example.adam.quickroster.menu;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.adam.quickroster.HomeFragment;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.login.WelcomeActivity;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.notice_board.NoticeBoardActivity;
import com.example.adam.quickroster.shifts.CalendarViewActivity;
import com.example.adam.quickroster.staff.StaffListView;
import com.parse.ParseUser;

/**
 * This is the managers home view. Its where they are taken once they have been logged in,
 * and also provides buttons to the various tasks they can perform
 */
public class Menu extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    Button showCal;
    Button addShift;
    Button removeShift;
    Button viewStaff;
    Button noticeBoardButton;
    Button logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, new HomeFragment());
        ft.commit();

        // set tool bar and nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Set name and business
        View header = navigationView.getHeaderView(0);
        TextView nameTextField = (TextView) header.findViewById(R.id.nav_header_name);
        TextView businessTextField = (TextView) header.findViewById(R.id.nav_header_business);

        ParseStaffUser currentUser = (ParseStaffUser) ParseUser.getCurrentUser();
        nameTextField.setText(currentUser.getFullName());
        businessTextField.setText(currentUser.getBusinessName());
    }


    /**
     * Logs out the current Parse User
     */
    public void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(Menu.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void displaySelectedScreen(int id){
        Fragment fragment = null;
        switch(id){
            case R.id.nav_view_shifts:
                fragment = new CalendarViewActivity();
                break;

            case R.id.nav_manage:
                fragment = new StaffListView();
                break;

            case R.id.nav_notices:
                fragment = new NoticeBoardActivity();
                break;

            case R.id.nav_logout:
                logout();
                break;
        }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }
}
