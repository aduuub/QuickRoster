package com.example.adam.quickroster;

/**
 * Created by Adam on 30/04/16.
 */
public class User {
    private String userName;
    private String password;
    private String name;
    private boolean isManager;

    /**
     *
     */
    public User(String un, String pw, String name, boolean manager ){
        this.userName = un;
        this.password = pw;
        this.name = name;
        this.isManager = manager;
    }

    public boolean validLogin(String un, String pw){
        return un.equals(userName) && pw.equals(password);
    }
}
