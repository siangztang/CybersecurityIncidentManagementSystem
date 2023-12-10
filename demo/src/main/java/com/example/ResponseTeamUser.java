package com.example;

public class ResponseTeamUser extends User{

    private String teamId;
    private String memberId;
    private String memberName;
    private String contactInfo;

    public ResponseTeamUser(String teamId, String memberId, String memberName, String contactInfo, String username, String password) {
        super(username, password);
        this.teamId = teamId;
        this.memberId = memberId;
        this.memberName = memberName;
        this.contactInfo = contactInfo;
    }

    public ResponseTeamUser() {
        super("", "");
    }


    public String getTeamId() { 
        return teamId; 
    }

    public String getMemberId() { 
        return memberId; 
    }

    public String getMemberName() { 
        return memberName; 
    }

    public String getContactInfo() { 
        return contactInfo; 
    }

    public int validationResponseTeamUser(String team_id, String memberName, String contactInfo, String username, String password){

        // validate team_id
        if (team_id == null || team_id.isEmpty() || team_id.length() == 0) {
            return 0;
        }
        // validate memberName
        if (memberName == null || memberName.isEmpty() || memberName.length() == 0) {
            return 0;
        }
        // validate contactInfo
        if (contactInfo == null || !contactInfo.matches("^(\\+?6?01)[02-46-9]-*[0-9]{7}$|^(\\+?6?01)[1]-*[0-9]{8}$")){ 
            return 0;
        }
        // validate username
        if (username == null || username.isEmpty() || username.length() < 5) {
            return 0;
        }
        // validate password
        if (password == null || password.isEmpty() || password.length() < 5) {
            return 0;
        }
        
        return 1;

    }
}
