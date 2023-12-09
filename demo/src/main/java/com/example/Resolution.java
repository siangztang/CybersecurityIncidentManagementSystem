package com.example;

public class Resolution {

    private String resolutionId;
    private String actionId;
    private String lessonsLearned;
    private String closureNotes;
    private String resolutionStatus;

    public Resolution(String resolutionId, String actionId, String lessonsLearned, String closureNotes, String resolutionStatus) {
        this.resolutionId = resolutionId;
        this.actionId = actionId;
        this.lessonsLearned = lessonsLearned;
        this.closureNotes = closureNotes;
        this.resolutionStatus = resolutionStatus;
    }

    public Resolution() {

    }

    public String getResolutionId() {
        return resolutionId;
    }

    public String getActionId() {
        return actionId;
    }

    public String getLessonsLearned() {
        return lessonsLearned;
    }

    public String getClosureNotes() {
        return closureNotes;
    }

    public String getResolutionStatus() {
        return resolutionStatus;
    }

    public int validationResolution(String actionId, String lessonsLearned, String closureNotes, String resolutionStatus) {
        // validate actionId
        if (actionId == null || actionId.isEmpty() || actionId.length() == 0) {
            return 0;
        }
        
        // validate lessonsLearned
        if (lessonsLearned == null || lessonsLearned.isEmpty() || lessonsLearned.length() == 0) {
            return 0;
        }
        // validate closureNotes
        if (closureNotes == null || closureNotes.isEmpty() || closureNotes.length() == 0) {
            return 0;
        }
        // validate resolutionStatus
        if (resolutionStatus == null || resolutionStatus.isEmpty() || resolutionStatus.length() == 0) {
            return 0;
        }
        return 1;
    }

}
