package com.example.adam.quickroster;

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

    public String getInfo() {
        return getString("Information");
    }

    public void setInfo(String info) {
        put("Information", info);
    }

    public String getEmail() {
        return getString("Email");
    }

    public void setEmail(String email) {
        put("Email", email);
    }

    public List<ParseUser> getManagers() {
        return getList("Managers");
    }

    public void addManager(ParseUser manager) {
        add("Managers", manager);
    }

    public void removeManager(ParseUser user) {
        user.deleteInBackground();
    }

    public List<ParseUser> getUsers() {
        return getList("Users");
    }

    public void addUser(ParseUser user) {
        add("Users", user);
    }

    public void removeUser(ParseUser user) {
        user.deleteInBackground();
    }

}
