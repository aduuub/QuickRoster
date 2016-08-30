package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.List;

/**
 * Represents a Business object in Parse
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
