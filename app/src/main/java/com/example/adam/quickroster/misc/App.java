package com.example.adam.quickroster.misc;

import android.app.Application;

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
        ParseObject.registerSubclass(ParseStaffUser.class);
        Parse.enableLocalDatastore(getApplicationContext());

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("HU35CHA4t0ebOb7AwQR3l8XmqE82oKC8QfoHu2Ed")
                .clientKey("N84KATdzjuYIln0zKB1k3dDpcrl0Ev0BxRJSDtf1")
                .server("https://parseapi.back4app.com/").build()
        );

        ParseUser.enableAutomaticUser();
        //ParseACL defauAc1 = new ParseACL();
        //defauAc1.setPublicReadAccess(true);
        //ParseACL.setDefaultACL(defauAc1,true);
    }
}
