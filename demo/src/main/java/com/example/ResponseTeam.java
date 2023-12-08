package com.example;

public class ResponseTeam extends User{

    private String teamId;
    private String memberName;
    private String contactInfo;

    public ResponseTeam(String teamId, String memberName, String contactInfo, String username, String password) {
        super(username, password);
        this.teamId = teamId;
        this.memberName = memberName;
        this.contactInfo = contactInfo;
    }

    public String getTeamId() { 
        return teamId; 
    }

    public String getMemberName() { 
        return memberName; 
    }

    public String getContactInfo() { 
        return contactInfo; 
    }

    

}
