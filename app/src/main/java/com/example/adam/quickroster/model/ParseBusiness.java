package com.example.adam.quickroster.model;

import com.parse.ParseClassName;
import com.parse.ParseObject;

/**
 * Represents a 'Business' object in the Parse.com database
 *
 * @author Adam Wareing
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
