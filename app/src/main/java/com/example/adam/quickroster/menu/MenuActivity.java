package com.example.adam.quickroster.menu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.adam.quickroster.fragments.AccountFragment;
import com.example.adam.quickroster.R;
import com.example.adam.quickroster.fragments.StatsFragment;
import com.example.adam.quickroster.login.WelcomeActivity;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.fragments.NoticeViewFragment;
import com.example.adam.quickroster.shifts.CalendarViewActivity;
import com.example.adam.quickroster.fragments.ShiftViewFragment;
import com.example.adam.quickroster.fragments.StaffViewFragment;
import com.parse.ParseUser;

/**
 * This is the Home view. Its where the user is taken once they have logged in, or on application launch and they are already authorised with a valid
 * session token. It contains the login for the <code>Navigation Drawer</code> and controls the fragments that are displayed on screen. Fragments are
 * changed by the user selecting options in the drawer.
 *
 * @author Adam Wareing
 */
public class MenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setToolbarAndNavDrawer();
        displaySelectedFragment(R.id.nav_home);
    }


    /**
     * Sets the toolbar up (including adding the toggle for the menu) and inits the navigation view as well as setting the users name and business in
     * the navigation views header.
     */
    private void setToolbarAndNavDrawer(){
        // Set Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the drawer layout, toggle and listener
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Set the navigation view listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get name and business text views
        View header = navigationView.getHeaderView(0);
        TextView nameTextField = (TextView) header.findViewById(R.id.nav_header_name);
        TextView businessTextField = (TextView) header.findViewById(R.id.nav_header_business);

        // Set name and business in the header
        ParseStaffUser currentUser = (ParseStaffUser) ParseUser.getCurrentUser();
        nameTextField.setText(currentUser.getFullName());
        businessTextField.setText(currentUser.getBusinessName());
    }


    /**
     * Log out the current Parse User
     */
    private void logout() {
        ParseUser.logOut();
        Intent intent = new Intent(MenuActivity.this, WelcomeActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Creates a fragment corresponding to the menu items id. Calls <code> displayFragment(Fragment) </code> to display
     *
     * @param id - menu items id
     */
    private void displaySelectedFragment(int id){
        Fragment fragment = null;
        switch(id){
            case R.id.nav_home:
                fragment = new ShiftViewFragment();
                break;

            case R.id.nav_view_shifts:
                fragment = new CalendarViewActivity();
                break;

            case R.id.nav_manage_staff:
                fragment = new StaffViewFragment();
                break;

            case R.id.nav_stats:
                fragment = new StatsFragment();
                break;

            case R.id.nav_notices:
                fragment = new NoticeViewFragment();
                break;

            case R.id.nav_account:
                fragment = new AccountFragment();
                break;

            case R.id.nav_logout:
                logout();
                break;
        }
        displayFragment(fragment);
    }


    /**
     * Displays the fragment on the screen in the <code>content_frame</code>
     *
     * It doesn't add it to the back stack, as the user shouldn't be able to navigate back through the fragments.
     *
     * @param fragment
     */
    public void displayFragment(Fragment fragment){
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
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        displaySelectedFragment(id);
        return true;
    }
}
