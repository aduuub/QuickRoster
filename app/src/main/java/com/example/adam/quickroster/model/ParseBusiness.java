package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Adam on 27/06/16.
 */
@ParseClassName("Business")
public class ParseBusiness extends ParseObject {

    public String getName() {
        return getString("Name");
    }

    public void setName(String name) {
        put("Name", name);
    }

    public String getEmail() {
        return getString("Email");
    }

    public void setEmail(String email) {
        put("Email", email);
    }

}
