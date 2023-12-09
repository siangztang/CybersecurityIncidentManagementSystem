package com.example;

public class Analysis {
    String analysisId;
    String incidentId;
    String attackVector;
    String attackSignature;
    String compromisedData;
    String analysisDetails;


    public Analysis(String analysisId, String incidentId, String attackVector, String attackSignature, String compromisedData, String analysisDetails) {
        this.analysisId = analysisId;
        this.incidentId = incidentId;
        this.attackVector = attackVector;
        this.attackSignature = attackSignature;
        this.compromisedData = compromisedData;
        this.analysisDetails = analysisDetails;
    }

    public Analysis() {

    }

    //getter
    public String getAnalysisId() {
        return analysisId;
    }

    public String getIncidentId() {
        return incidentId;
    }

    public String getAttackVector() {
        return attackVector;
    }

    public String getAttackSignature() {
        return attackSignature;
    }

    public String getCompromisedData() {
        return compromisedData;
    }

    public String getAnalysisDetails() {
        return analysisDetails;
    }

    public int validationAnalysis(String incident_id, String attackVector, String attackSignature, String compromisedData, String analysisDetails) {
        // validate incident_id
        if (incident_id == null || incident_id.isEmpty() || incident_id.length() == 0) {
            return 0;
        }
        
        // validate attackVector
        if (attackVector == null || attackVector.isEmpty() || attackVector.length() == 0) {
            return 0;
        }
        // validate attackSignature
        if (attackSignature == null || attackSignature.isEmpty() || attackSignature.length() == 0) {
            return 0;
        }
        // validate compromisedData
        if (compromisedData == null || compromisedData.isEmpty() || compromisedData.length() == 0) {
            return 0;
        }
        // validate analysisDetails
        if (analysisDetails == null || analysisDetails.isEmpty() || analysisDetails.length() == 0) {
            return 0;
        }
        return 1;
    }

}
