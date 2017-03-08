package com.example.adam.quickroster.staff;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.example.adam.quickroster.shifts.AddShiftActivity;
import com.example.adam.quickroster.shifts.ShiftViewFragment;
import com.parse.ParseUser;


/**
 * Created by Adam on 8/03/17.
 */
public class ViewStaffMemberActivity extends AppCompatActivity {

    private ParseStaffUser selectedUser;
    private String email;
    private String mobileNumber;
    private String fullName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_staff_member);

        // Get user
        String objectId = getIntent().getStringExtra("objectId");
        selectedUser = (ParseStaffUser) ParseStaffUser.getUserFromId(objectId);

        // Set toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        fullName = selectedUser.getFullName();
        mToolbar.setTitle(fullName);
        setSupportActionBar(mToolbar);

        // Set view
        setTextViewsAndListeners();
        displayShiftFragment();
    }

    private void setTextViewsAndListeners() {
        // Get important data
        mobileNumber = selectedUser.getMobileNumber();
        email = selectedUser.getEmail();

        // Set text views
        ((TextView) findViewById(R.id.name_text_view)).setText(fullName);
        ((TextView) findViewById(R.id.user_name_text_view)).setText(selectedUser.getUsername());
        String permissionLevelText = selectedUser.isManager() ? "Manager" : "Staff Member";
        ((TextView) findViewById(R.id.permission_level_text_view)).setText(permissionLevelText);
        ((TextView) findViewById(R.id.mobile_number_text_view)).setText(mobileNumber);
        ((TextView) findViewById(R.id.mobile_number_text_view_2)).setText(mobileNumber);
        ((TextView) findViewById(R.id.email_text_view)).setText(email);

        LinearLayout callLinearLayout = (LinearLayout) findViewById(R.id.call_linear_layout);
        callLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber();
            }
        });

        LinearLayout emailLinearLayout = (LinearLayout) findViewById(R.id.email_linear_layout);
        emailLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        LinearLayout textLinearLayout = (LinearLayout) findViewById(R.id.text_linear_layout);
        textLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textNumber();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (ParseUser.getCurrentUser().getBoolean("isManager")) {
            getMenuInflater().inflate(R.menu.edit_menu, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.menu_icon_edit) {
            Intent intentAddStaff = new Intent(ViewStaffMemberActivity.this, EditStaffMemberActivity.class);
            intentAddStaff.putExtra("objectId", selectedUser.getObjectId());
            startActivity(intentAddStaff);

        }else {
            // Menu not found
            return false;
        }
        return true;
    }

    private void displayShiftFragment() {
        Fragment fragment = new ShiftViewFragment();

        // Set extras
        Bundle args = new Bundle();
        args.putString("objectId", selectedUser.getObjectId());
        fragment.setArguments(args);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.staff_shift_view, fragment);
        ft.commit();
    }

    private void callNumber() {
        Intent sendIntent = new Intent(Intent.ACTION_DIAL);
        sendIntent.setData(Uri.parse("tel:0123456789"));
        startActivity(sendIntent);
    }

    private void textNumber() {
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.setType("vnd.android-dir/mms-sms");
        sendIntent.putExtra("address", mobileNumber);
        startActivity(sendIntent);
    }

    private void sendEmail() {
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("message/rfc822");
        sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        startActivity(Intent.createChooser(sendIntent, "Send mail to " + fullName));
    }
}