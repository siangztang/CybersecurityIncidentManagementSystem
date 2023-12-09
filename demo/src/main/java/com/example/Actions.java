package com.example;

public class Actions {
    String actionId;
    String analysisId;
    String actionType;
    String actionDesc;
    String actionStatus;

    public Actions(String actionId, String analysisId, String actionType, String actionDesc, String actionStatus) {
        this.actionId = actionId;
        this.analysisId = analysisId;
        this.actionType = actionType;
        this.actionDesc = actionDesc;
        this.actionStatus = actionStatus;
    }

    public Actions() {

    }

    //getter
    public String getActionId() {
        return actionId;
    }

    public String getAnalysisId() {
        return analysisId;
    }

    public String getActionType() {
        return actionType;
    }

    public String getActionDesc() {
        return actionDesc;
    }

    public String getActionStatus() {
        return actionStatus;
    }

    public int validationActions(String analysisId, String actionType, String actionDesc, String actionStatus) {
        // validate analysisId
        if (analysisId == null || analysisId.isEmpty() || analysisId.length() == 0) {
            return 0;
        }
        
        // validate actionType
        if (actionType == null || actionType.isEmpty() || actionType.length() == 0) {
            return 0;
        }
        // validate actionDesc
        if (actionDesc == null || actionDesc.isEmpty() || actionDesc.length() == 0) {
            return 0;
        }
        // validate actionStatus
        if (actionStatus == null || actionStatus.isEmpty() || actionStatus.length() == 0) {
            return 0;
        }
        return 1;
    }
}
