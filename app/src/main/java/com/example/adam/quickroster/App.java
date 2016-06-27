package com.example.adam.quickroster;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import com.parse.*;

/**
 * Created by Adam on 16/05/16.
 */
public class App extends Application {

    public void onCreate() {
        super.onCreate();
        ParseObject.registerSubclass(ParseShift.class);
        ParseObject.registerSubclass(ParseStaffUser.class);
        Parse.enableLocalDatastore(getApplicationContext());
        Parse.initialize(this, "HU35CHA4t0ebOb7AwQR3l8XmqE82oKC8QfoHu2Ed", "N84KATdzjuYIln0zKB1k3dDpcrl0Ev0BxRJSDtf1");
        ParseUser.enableAutomaticUser();
        ParseACL defauAc1 = new ParseACL();
        defauAc1.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defauAc1,true);
    }
}
