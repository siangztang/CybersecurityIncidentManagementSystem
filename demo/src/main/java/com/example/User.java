package com.example;

import com.example.CSVRelatedClass.CSVHandler;
import com.example.CSVRelatedClass.CSVPath;
import com.example.CSVRelatedClass.ParameterTypes;

import javafx.collections.ObservableList;

public class User {

    private String username;
    private String password;

    CSVHandler csvHandler = new CSVHandler();

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { 
        return username; 
    }

    public String getPassword() { 
        return password; 
    }

    public int isValidLogin(){
        int valid = 0;

        
        // Load the list of response team members from CSV
        ObservableList<ResponseTeamUser> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_USER_PATH, ResponseTeamUser.class, ParameterTypes.RESPONSE_TEAM_USER_PARAMETER_TYPES);

        // Check if the user is an admin
        if (username.equals(Admin.ADMINID) && password.equals(Admin.ADMINPASS)) {
            valid = 1;
        } else {
            // Check if the user is a response team member
            for (ResponseTeamUser teamMember : listData) {
                if (teamMember.getUsername().equals(username) && teamMember.getPassword().equals(password)) {
                    valid = 2;
                    break; // Exit the loop if a match is found
                }
            }
        }
        
        return valid;
      
    }

    
}
