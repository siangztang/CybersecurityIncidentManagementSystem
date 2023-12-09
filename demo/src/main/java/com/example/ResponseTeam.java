package com.example;

public class ResponseTeam {
    
    private String teamId;
    private String teamName;

    public ResponseTeam(String teamId, String teamName) {
        this.teamId = teamId;
        this.teamName = teamName;
    }

    public String getTeamId() { 
        return teamId; 
    }

    public String getTeamName() { 
        return teamName; 
    }

}
