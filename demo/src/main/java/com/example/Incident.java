package com.example;

public class Incident {
    private String incidentId;
    private String timestamp;
    private String description;
    private String securityLevel;
    private String affectedSystem;
    private String incidentStatus;
    private String handleBy;

    // Constructor with all fields
    public Incident(String incidentId, String timestamp, String description, String securityLevel, String affectedSystem, String incidentStatus, String handleBy) {
        this.incidentId = incidentId;
        this.timestamp = timestamp;
        this.description = description;
        this.securityLevel = securityLevel;
        this.affectedSystem = affectedSystem;
        this.incidentStatus = incidentStatus;
        this.handleBy = handleBy;
    }

    // Getters 
    public String getIncidentId() {
        return incidentId;
    }
    
    public String getTimestamp() {
        return timestamp;
    }

    public String getDescription() {
        return description;
    }

    public String getSecurityLevel() {
        return securityLevel;
    }

    public String getAffectedSystem() {
        return affectedSystem;
    }

    public String getIncidentStatus() {
        return incidentStatus;
    }

    public String getHandleBy() {
        return handleBy;
    }

}
