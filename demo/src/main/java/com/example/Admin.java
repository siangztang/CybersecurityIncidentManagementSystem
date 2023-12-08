package com.example;

public class Admin extends User{

    public static final String ADMINID = "admin";
    public static final String ADMINPASS = "admin123";

    public Admin(String username, String password) {
        super(username, password);
    }

}
