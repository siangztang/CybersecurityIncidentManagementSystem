package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.AlertMessage;
import com.example.Incident;
import com.example.ResponseTeamUser;
import com.example.User;
import com.example.CSVRelatedClass.CSVHandler;
import com.example.CSVRelatedClass.CSVPath;
import com.example.CSVRelatedClass.CustomComparator;
import com.example.CSVRelatedClass.ParameterTypes;

import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IncidentListTeamController {

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Incident, String> icdAffectedCol;

    @FXML
    private TextField icdAffectedField;

    @FXML
    private TableColumn<Incident, String> icdDescCol;

    @FXML
    private TextField icdDescField;

    @FXML
    private TableColumn<Incident, String> icdIdCol;

    @FXML
    private TableView<Incident> icdListTable;

    @FXML
    private TableColumn<Incident, String> icdSecLvlCol;

    @FXML
    private ComboBox<String> icdSecLvlComboBox;

    @FXML
    private TableColumn<Incident, String> icdStatusCol;

    @FXML
    private ComboBox<String> icdStatusComboBox;

    @FXML
    private TableColumn<Incident, String> icdTimestampCol;

    @FXML
    private StackPane incident_list_page;

    @FXML
    private Button logOutBtn;

    @FXML
    private TextField resTeamIdInfo;

    @FXML
    private Button resetBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Label unameLabel;

    @FXML
    private Button updateBtn;

    CSVHandler csvHandler = new CSVHandler();
    AlertMessage alert = new AlertMessage();

    @FXML
    void initialize(){
        icdSecLvlComboBox.getItems().addAll("Low", "Medium", "High");
        icdStatusComboBox.getItems().addAll("Open", "Closed");

        addBtn.setOnAction(event -> {
            addBtnAction();
        });

        deleteBtn.setOnAction(event -> {
            deleteBtnAction();
        });

        updateBtn.setOnAction(event -> {
            updateBtnAction();
        });

        resetBtn.setOnAction(event -> {
            resetBtnAction();
        });

        logOutBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/Login.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setTitle("Cybersecurity Incident Management");
                stage.centerOnScreen();
                stage.show();
                Stage currentStage = (Stage) logOutBtn.getScene().getWindow();
                currentStage.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        icdDescField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, icdSecLvlComboBox);
        });

        icdSecLvlComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, icdAffectedField);
        });

        icdAffectedField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, icdStatusComboBox);
        });

        icdStatusComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, icdDescField);
        });

        unFocusAll();
        icdListTable.getColumns().forEach(column -> column.setReorderable(false));
        icdListTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1){
                Incident selectedIncident = icdListTable.getSelectionModel().getSelectedItem();
                if (selectedIncident != null) {
                    icdDescField.setText(selectedIncident.getDescription());
                    icdSecLvlComboBox.setValue(String.valueOf(selectedIncident.getSecurityLevel()));
                    icdAffectedField.setText(selectedIncident.getAffectedSystem());
                    icdStatusComboBox.setValue(String.valueOf(selectedIncident.getIncidentStatus()));
                }
            } else if (event.getClickCount() == 2){
                Incident selectedIncident = icdListTable.getSelectionModel().getSelectedItem();
                if (selectedIncident != null) {
                    // Do something with the selected patient data
                    System.out.println("Selected incident ID: " + selectedIncident.getIncidentId());
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IntrusionAnalysis.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        IntrusionAnalysisController controller = loader.getController();
                        controller.initData(user, selectedIncident);
                        Node node = (Node) event.getSource();
                        Stage currentStage = (Stage) node.getScene().getWindow();
                        currentStage.close();
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        
    }

    private void handleTextFieldKeyPress(javafx.scene.input.KeyEvent event, javafx.scene.control.Control nextControl) {
        if (event.getCode() == KeyCode.TAB) {
            nextControl.requestFocus();
            event.consume();
        }
    }

    private Incident checkInput = new Incident();
    private User user;
    private String teamId;
    public void initData(User user){
        this.user = user;
        unameLabel.setText(user.getUsername());

        ObservableList<ResponseTeamUser> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_USER_PATH, ResponseTeamUser.class, ParameterTypes.RESPONSE_TEAM_USER_PARAMETER_TYPES);
        for (ResponseTeamUser responseTeamUser : listData) {
            if (responseTeamUser.getUsername().equals(user.getUsername())) {
                resTeamIdInfo.setText(responseTeamUser.getTeamId());
                this.teamId = responseTeamUser.getTeamId();
            }
        }
        incidentShowListData();
        searchFilter();
    }

    public void resetBtnAction(){
        icdDescField.setText("");
        icdSecLvlComboBox.setValue("");
        icdAffectedField.setText("");
        icdStatusComboBox.setValue("");
        icdListTable.getSelectionModel().clearSelection();
        icdListTable.setItems(refreshData());
        incidentShowListData();
    }

    public void unFocusAll(){
        addBtn.setFocusTraversable(false);
        deleteBtn.setFocusTraversable(false);
        updateBtn.setFocusTraversable(false);
        resetBtn.setFocusTraversable(false);
        logOutBtn.setFocusTraversable(false);
        searchField.setFocusTraversable(false);
        icdDescField.setFocusTraversable(false);
        icdSecLvlComboBox.setFocusTraversable(false);
        icdAffectedField.setFocusTraversable(false);
        icdStatusComboBox.setFocusTraversable(false);
        icdListTable.setFocusTraversable(false);
        resTeamIdInfo.setFocusTraversable(false);
    }

    private ObservableList<Incident> refreshData(){
        ObservableList<Incident> listData = csvHandler.readCSV(CSVPath.INCIDENT_PATH, Incident.class, "handleBy", teamId, CustomComparator.createComparator(Incident::getIncidentId, 3), ParameterTypes.INCIDENT_PARAMETER_TYPES);
        return listData;
    }

    public void incidentShowListData(){
        icdIdCol.setCellValueFactory(new PropertyValueFactory<>("incidentId"));
        icdTimestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        icdDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        icdSecLvlCol.setCellValueFactory(new PropertyValueFactory<>("securityLevel"));
        icdAffectedCol.setCellValueFactory(new PropertyValueFactory<>("affectedSystem"));
        icdStatusCol.setCellValueFactory(new PropertyValueFactory<>("incidentStatus"));

        icdListTable.setItems(refreshData());
    }

    private void searchFilter(){
        FilteredList<Incident> filteredData = new FilteredList<>(refreshData(), e -> true);

        if (searchField.getText() != null){
            searchField.setOnKeyReleased(e->{
        
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Incident -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();
                    String securityLevel = String.valueOf(Incident.getSecurityLevel()).toLowerCase();
                    String incidentStatus = String.valueOf(Incident.getIncidentStatus()).toLowerCase();
                    String assignResponseTeam = String.valueOf(Incident.getHandleBy()).toLowerCase();

                        if(Incident.getIncidentId().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Incident.getTimestamp().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Incident.getDescription().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(securityLevel.equals(toLowerCaseNewValue)){
                            return true;
                        }else if(Incident.getAffectedSystem().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(incidentStatus.equals(toLowerCaseNewValue)){
                            return true;
                        }else if(assignResponseTeam.equals(toLowerCaseNewValue)){
                            return true;
                        }

                    
                return false;

                });
            });
                final SortedList<Incident> incidents_list = new SortedList<>(filteredData);
                incidents_list.comparatorProperty().bind(icdListTable.comparatorProperty());
                icdListTable.setItems(incidents_list);
            });
        }
    }

    private boolean checkEmpty(){
        if (icdDescField.getText().isEmpty() || icdSecLvlComboBox.getValue() == null || icdAffectedField.getText().isEmpty() || icdStatusComboBox.getValue() == null) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    private boolean checkSelected(){
        if (icdListTable.getSelectionModel().getSelectedItem() == null) {
            // show error message
            alert.errorMessage("Please select an incident");
            return true;
        }
        return false;
    }

    public void addBtnAction(){

        String icdDesc = icdDescField.getText().trim();
        String icdSecLvl = icdSecLvlComboBox.getValue();
        String icdAffected = icdAffectedField.getText().trim();
        String icdStatus = icdStatusComboBox.getValue();
        String icdAssignTeam = "N/A";

        if(!checkEmpty()){
            if (checkInput.validationIncident(icdDesc, icdSecLvl, icdAffected, icdStatus, icdAssignTeam) == 1) {

                //generate incident id
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());

                String icdID = "ICD" + (timestamp.getTime()/1000);

                // get current date and time in a specific format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime currentDateTime = LocalDateTime.now();
                String formattedDateTime = currentDateTime.format(formatter);

                //crete new incident object
                Incident incident = new Incident(icdID, formattedDateTime, icdDesc, icdSecLvl, icdAffected, icdStatus, icdAssignTeam);
                
                //add new incident to csv file
                csvHandler.writeCSV(CSVPath.INCIDENT_PATH, incident);

                //show success message
                alert.successMessage("Incident added successfully");

                //refresh data
                refreshData();

                //refresh table
                incidentShowListData();

                // search filter reset
                searchFilter();

                // unfocus all
                unFocusAll();

                //reset fields
                resetBtnAction();

            }else{

                // show error message
                alert.errorMessage("Please fill in all the fields");                
              
            }
        }
        
    }

    public void updateBtnAction(){
        if(!checkSelected()){
            //get selected incident
            String icdID = icdListTable.getSelectionModel().getSelectedItem().getIncidentId();
            String icdTimestamp = icdListTable.getSelectionModel().getSelectedItem().getTimestamp();
            String icdDesc = icdDescField.getText().trim();
            String icdSecLvl = icdSecLvlComboBox.getValue();
            String icdAffected = icdAffectedField.getText().trim();
            String icdStatus = icdStatusComboBox.getValue();
            String icdAssignTeam = icdListTable.getSelectionModel().getSelectedItem().getHandleBy();

            if(!checkEmpty()){
                if(checkInput.validationIncident(icdDesc, icdSecLvl, icdAffected,icdStatus, icdAssignTeam) == 1){

                    //create new incident object
                    Incident updatedIncident = new Incident(icdID, icdTimestamp, icdDesc, icdSecLvl, icdAffected, icdStatus, icdAssignTeam);

                    //update incident in csv file
                    csvHandler.updateCSV(CSVPath.INCIDENT_PATH, 0, icdID, updatedIncident);

                    //show success message
                    alert.successMessage("Incident updated successfully");

                    //refresh data
                    refreshData();

                    //refresh table
                    incidentShowListData();

                    // search filter reset
                    searchFilter();

                    // unfocus all
                    unFocusAll();

                    //reset fields
                    resetBtnAction();

                }else{
                    // show error message
                    alert.errorMessage("Please fill in all the fields");
                }
            }
        }

    }

    public void deleteBtnAction(){
        if(!checkSelected()){
            //get selected incident
            String icdID = icdListTable.getSelectionModel().getSelectedItem().getIncidentId();
            
            //delete incident in csv file
            csvHandler.deleteCSV(CSVPath.INCIDENT_PATH, 0, icdID);

            //show success message
            alert.successMessage("Incident deleted successfully");

            //refresh data
            refreshData();

            //refresh table
            incidentShowListData();

            // search filter reset
            searchFilter();

            // unfocus all
            unFocusAll();

            //reset fields
            resetBtnAction();
        }
    }

}
