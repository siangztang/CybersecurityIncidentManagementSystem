package com.example;

public class ResponseTeamUser extends User{

    private String teamId;
    private String memberName;
    private String contactInfo;

    public ResponseTeamUser(String teamId, String memberName, String contactInfo, String username, String password) {
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
