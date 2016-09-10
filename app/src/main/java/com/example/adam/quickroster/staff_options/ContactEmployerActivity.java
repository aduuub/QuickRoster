package com.example.adam.quickroster.staff_options;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.adam.quickroster.R;
import com.example.adam.quickroster.misc.ParseQueryUtil;
import com.parse.ParseObject;
import com.parse.ParseUser;

/**
 * Used for the employee to contact there employer, as well as provide details on there business.
 */
public class ContactEmployerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_employer);
        sendEmail();
    }


    private void setup(){

    }

    private void sendEmail() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        ParseObject business = ParseQueryUtil.getParseUsersBusiness(currentUser);
        String email = business.getString("email");


        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

        //  Fill it the with extra data
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"to@email.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Text");

        startActivity(Intent.createChooser(emailIntent, "Send Employer Email"));
    }
}
