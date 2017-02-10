package com.example.adam.quickroster.misc;

import android.app.Application;

import com.example.adam.quickroster.model.ParseBusiness;
import com.example.adam.quickroster.model.ParseNotice;
import com.example.adam.quickroster.model.ParseShift;
import com.example.adam.quickroster.model.ParseStaffUser;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import com.parse.*;

/**
 * This is the entry point of the application. It initialises the Parse database.
 */
public class App extends Application {

    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseShift.class);
        ParseUser.registerSubclass(ParseStaffUser.class);
        ParseUser.registerSubclass(ParseBusiness.class);
        ParseUser.registerSubclass(ParseNotice.class);

        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("HU35CHA4t0ebOb7AwQR3l8XmqE82oKC8QfoHu2Ed")
                .clientKey("N84KATdzjuYIln0zKB1k3dDpcrl0Ev0BxRJSDtf1")
                .server("https://parseapi.back4app.com/").build()
        );

        ParseUser.enableAutomaticUser();

        // Set current user
        ParseUtil.getInstance();
    }
}
