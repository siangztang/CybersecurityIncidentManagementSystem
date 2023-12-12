package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.example.AlertMessage;
import com.example.Incident;
import com.example.ResponseTeam;
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
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ResponseTeamDetailsController {

    @FXML
    private Button backBtn;

    @FXML
    private Button icdAddBtn;

    @FXML
    private TableColumn<Incident, String> icdAffectedCol;

    @FXML
    private TextField icdAffectedField;

    @FXML
    private ComboBox<String> icdAssignTeamComboBox;

    @FXML
    private Button icdDeleteBtn;

    @FXML
    private TableColumn<Incident, String> icdDescCol;

    @FXML
    private TextField icdDescField;

    @FXML
    private TableColumn<Incident, String> icdIdCol;

    @FXML
    private TableView<Incident> icdListTable;

    @FXML
    private Button icdResetBtn;

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
    private Button icdUpdateBtn;

    @FXML
    private TextField resTeamIdInfo;

    @FXML
    private TableColumn<ResponseTeamUser, String> resTeamContactCol;

    @FXML
    private TableColumn<ResponseTeamUser, String> resTeamUsernameCol;

    @FXML
    private TextField resTeamContactField;

    @FXML
    private TableView<ResponseTeamUser> resTeamInfoTable;

    @FXML
    private TableColumn<ResponseTeamUser, String> resTeamMemberIdCol;

    @FXML
    private TableColumn<ResponseTeamUser, String> resTeamMemberCol;

    @FXML
    private TextField resTeamMemberNameField;

    @FXML
    private TextField resTeamUsernameField;

    @FXML
    private PasswordField resTeamPasswordField;

    @FXML
    private Button responseTeamInfoAddBtn;

    @FXML
    private Button responseTeamInfoDeleteBtn;

    @FXML
    private Button responseTeamInfoResetBtn;

    @FXML
    private Button responseTeamInfoUpdateBtn;

    @FXML
    private ComboBox<String> resAssignTeamComboBox;

    @FXML
    private StackPane response_team_info_page;

    @FXML
    private TextField searchFieldIncident;

    @FXML
    private TextField searchFieldTeamInfo;

    @FXML
    private Label unameLabel;

    CSVHandler csvHandler = new CSVHandler();
    AlertMessage alert = new AlertMessage();

    @FXML
    void initialize(){
        icdSecLvlComboBox.getItems().addAll("Low", "Medium", "High");
        icdStatusComboBox.getItems().addAll("Open", "Closed");
        icdAssignTeamComboBox.getItems().add("N/A");

        ObservableList<ResponseTeam> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_PATH, ResponseTeam.class, ParameterTypes.RESPONSE_TEAM_PARAMETER_TYPES);

        for (ResponseTeam responseTeams : listData) {
            String responseTeamsName = responseTeams.getTeamName();
            icdAssignTeamComboBox.getItems().add(responseTeamsName);
            resAssignTeamComboBox.getItems().add(responseTeamsName);
        }

        icdAddBtn.setOnAction(event -> {
            icdAddBtnAction();
        });

        icdDeleteBtn.setOnAction(event -> {
            icdDeleteBtnAction();
        });

        icdUpdateBtn.setOnAction(event -> {
            icdUpdateBtnAction();
        });

        icdResetBtn.setOnAction(event -> {
            icdResetBtnAction();
        });

        responseTeamInfoAddBtn.setOnAction(event -> {
            resTeamInfoAddBtnAction();
        });

        responseTeamInfoUpdateBtn.setOnAction(event -> {
            resTeamInfoUpdateBtnAction();
        });

        responseTeamInfoDeleteBtn.setOnAction(event -> {
            resTeamInfoDeleteBtnAction();
        });

        responseTeamInfoResetBtn.setOnAction(event -> {
            resTeamRestBtnAction();
        });

        backBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/ManageResponseTeam.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                ManageResTeamController controller = loader.getController();
                controller.initData(user);
                stage.show();
                Stage currentStage = (Stage) backBtn.getScene().getWindow();
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
            handleTextFieldKeyPress(event, icdAssignTeamComboBox);
        });

        icdAssignTeamComboBox.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, icdDescField);
        });

        resTeamMemberNameField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, resTeamContactField);
        });

        resTeamContactField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, resTeamUsernameField);
        });

        resTeamUsernameField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, resTeamPasswordField);
        });

        resTeamPasswordField.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            handleTextFieldKeyPress(event, resTeamMemberNameField);
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
                    icdAssignTeamComboBox.setValue(String.valueOf(selectedIncident.getHandleBy()));
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

        resTeamInfoTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1){
                ResponseTeamUser selectedResponseTeamUser = resTeamInfoTable.getSelectionModel().getSelectedItem();
                if (selectedResponseTeamUser != null) {
                    resTeamMemberNameField.setText(selectedResponseTeamUser.getMemberName());
                    resTeamContactField.setText(selectedResponseTeamUser.getContactInfo());
                    resTeamUsernameField.setText(selectedResponseTeamUser.getUsername());
                    resTeamPasswordField.setText(selectedResponseTeamUser.getPassword());
                    resAssignTeamComboBox.setValue(selectedResponseTeamUser.getTeamId());
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

    private Incident icdCheckInput = new Incident();
    private ResponseTeamUser rtucheckInput = new ResponseTeamUser();
    private User user;
    private String manageTeamId;

    public void initData(User user, String manageTeamId){
        this.user = user;
        this.manageTeamId = manageTeamId;
        unameLabel.setText(user.getUsername());
        resTeamIdInfo.setText(manageTeamId);
        incidentShowListData();
        searchFieldIncident();
        responseTeamInfoShowListData();
        searchFieldTeamInfo();
        icdAssignTeamComboBox.setValue(resTeamIdInfo.getText());
        resAssignTeamComboBox.setValue(resTeamIdInfo.getText());
    }

    public void icdResetBtnAction(){
        icdDescField.setText("");
        icdSecLvlComboBox.setValue("");
        icdAffectedField.setText("");
        icdStatusComboBox.setValue("");
        icdAssignTeamComboBox.setValue("");
        icdListTable.getSelectionModel().clearSelection();
        icdListTable.setItems(icdrefreshData());
        incidentShowListData();
        searchFieldIncident();
    }

    public void resTeamRestBtnAction(){
        resTeamMemberNameField.setText("");
        resTeamUsernameField.setText("");
        resTeamPasswordField.setText("");
        resTeamContactField.setText("");
        resTeamInfoTable.getSelectionModel().clearSelection();
        resTeamInfoTable.setItems(resTeamRefreshData());
        responseTeamInfoShowListData();
        searchFieldTeamInfo();
    }

    public void unFocusAll(){
        backBtn.setFocusTraversable(false);
        icdListTable.setFocusTraversable(false);
        resTeamInfoTable.setFocusTraversable(false);
        icdAddBtn.setFocusTraversable(false);
        icdUpdateBtn.setFocusTraversable(false);
        icdDeleteBtn.setFocusTraversable(false);
        icdResetBtn.setFocusTraversable(false);
        searchFieldIncident.setFocusTraversable(false);
        searchFieldTeamInfo.setFocusTraversable(false);
        resTeamMemberNameField.setFocusTraversable(false);
        resTeamUsernameField.setFocusTraversable(false);
        resTeamPasswordField.setFocusTraversable(false);
        resTeamContactField.setFocusTraversable(false);
        responseTeamInfoAddBtn.setFocusTraversable(false);
        responseTeamInfoUpdateBtn.setFocusTraversable(false);
        responseTeamInfoDeleteBtn.setFocusTraversable(false);
        responseTeamInfoResetBtn.setFocusTraversable(false);
        resTeamIdInfo.setFocusTraversable(false);
        resAssignTeamComboBox.setFocusTraversable(false);
        icdDescField.setFocusTraversable(false);
        icdSecLvlComboBox.setFocusTraversable(false);
        icdAffectedField.setFocusTraversable(false);
        icdStatusComboBox.setFocusTraversable(false);
        icdAssignTeamComboBox.setFocusTraversable(false);
        icdListTable.setFocusTraversable(false);
    }

    private ObservableList<Incident> icdrefreshData(){
        ObservableList<Incident> listData = csvHandler.readCSV(CSVPath.INCIDENT_PATH, Incident.class, "handleBy", manageTeamId, CustomComparator.createComparator(Incident::getIncidentId, 3), ParameterTypes.INCIDENT_PARAMETER_TYPES);
        return listData;
    }

    public void incidentShowListData(){
        icdIdCol.setCellValueFactory(new PropertyValueFactory<>("incidentId"));
        icdTimestampCol.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        icdDescCol.setCellValueFactory(new PropertyValueFactory<>("description"));
        icdSecLvlCol.setCellValueFactory(new PropertyValueFactory<>("securityLevel"));
        icdAffectedCol.setCellValueFactory(new PropertyValueFactory<>("affectedSystem"));
        icdStatusCol.setCellValueFactory(new PropertyValueFactory<>("incidentStatus"));
        icdDescCol.setCellFactory(tc -> {
            TableCell<Incident, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(icdDescCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        icdAffectedCol.setCellFactory(tc -> {
            TableCell<Incident, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(icdAffectedCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        icdListTable.setItems(icdrefreshData());
    }

    private void searchFieldTeamInfo(){
        FilteredList<ResponseTeamUser> filteredData = new FilteredList<>(resTeamRefreshData(), e -> true);

        if (searchFieldTeamInfo.getText() != null){
            searchFieldTeamInfo.setOnKeyReleased(e->{
        
                searchFieldTeamInfo.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(ResponseTeamUser -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();
                        if (ResponseTeamUser.getMemberId().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(ResponseTeamUser.getMemberName().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(ResponseTeamUser.getContactInfo().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(ResponseTeamUser.getUsername().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }

                    
                return false;

                });
            });
                final SortedList<ResponseTeamUser> team_list = new SortedList<>(filteredData);
                team_list.comparatorProperty().bind(resTeamInfoTable.comparatorProperty());
                resTeamInfoTable.setItems(team_list);
            });
        }
    }

    private void searchFieldIncident(){
        FilteredList<Incident> filteredData = new FilteredList<>(icdrefreshData(), e -> true);

        if (searchFieldIncident.getText() != null){
            searchFieldIncident.setOnKeyReleased(e->{
        
                searchFieldIncident.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Incident -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();
                    String securityLevel = String.valueOf(Incident.getSecurityLevel()).toLowerCase();
                    String incidentStatus = String.valueOf(Incident.getIncidentStatus()).toLowerCase();

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

    private boolean icdCheckEmpty(){
        if (icdDescField.getText().isEmpty() || icdSecLvlComboBox.getValue() == null || icdAffectedField.getText().isEmpty() || icdStatusComboBox.getValue() == null || icdAssignTeamComboBox.getValue() == null) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    private boolean icdCheckSelected(){
        if (icdListTable.getSelectionModel().getSelectedItem() == null) {
            // show error message
            alert.errorMessage("Please select an incident");
            return true;
        }
        return false;
    }

    public void icdAddBtnAction(){

        String icdDesc = icdDescField.getText().trim();
        String icdSecLvl = icdSecLvlComboBox.getValue();
        String icdAffected = icdAffectedField.getText().trim();
        String icdStatus = icdStatusComboBox.getValue();
        String icdAssignTeam = icdAssignTeamComboBox.getValue();

        if(!icdCheckEmpty()){
            if (icdCheckInput.validationIncident(icdDesc, icdSecLvl, icdAffected, icdStatus, icdAssignTeam) == 1) {

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
                icdrefreshData();

                //refresh table
                incidentShowListData();

                // search filter reset
                searchFieldIncident();

                // unfocus all
                unFocusAll();

                //reset fields
                icdResetBtnAction();

            }else{

                // show error message
                alert.errorMessage("Please fill in all the fields");                
              
            }
        }
        
    }

    public void icdUpdateBtnAction(){
        if(!icdCheckSelected()){
            //get selected incident
            String icdID = icdListTable.getSelectionModel().getSelectedItem().getIncidentId();
            String icdTimestamp = icdListTable.getSelectionModel().getSelectedItem().getTimestamp();
            String icdDesc = icdDescField.getText().trim();
            String icdSecLvl = icdSecLvlComboBox.getValue();
            String icdAffected = icdAffectedField.getText().trim();
            String icdStatus = icdStatusComboBox.getValue();
            String icdAssignTeam = icdAssignTeamComboBox.getValue();

            if(!icdCheckEmpty()){
                if(icdCheckInput.validationIncident(icdDesc, icdSecLvl, icdAffected,icdStatus, icdAssignTeam) == 1){

                    //create new incident object
                    Incident updatedIncident = new Incident(icdID, icdTimestamp, icdDesc, icdSecLvl, icdAffected, icdStatus, icdAssignTeam);

                    //update incident in csv file
                    csvHandler.updateCSV(CSVPath.INCIDENT_PATH, 0, icdID, updatedIncident);

                    //show success message
                    alert.successMessage("Incident updated successfully");

                    //refresh data
                    icdrefreshData();

                    //refresh table
                    incidentShowListData();

                    // search filter reset
                    searchFieldIncident();

                    // unfocus all
                    unFocusAll();

                    //reset fields
                    icdResetBtnAction();

                }else{
                    // show error message
                    alert.errorMessage("Please fill in all the fields");
                }
            }
        }

    }

    public void icdDeleteBtnAction(){
        if(!icdCheckSelected()){
            //get selected incident
            String icdID = icdListTable.getSelectionModel().getSelectedItem().getIncidentId();
            
            //delete incident in csv file
            csvHandler.deleteCSV(CSVPath.INCIDENT_PATH, 0, icdID);

            //show success message
            alert.successMessage("Incident deleted successfully");

            //refresh data
            icdrefreshData();

            //refresh table
            incidentShowListData();

            // search filter reset
            searchFieldIncident();

            // unfocus all
            unFocusAll();

            //reset fields
            icdResetBtnAction();
        }
    }

    private ObservableList<ResponseTeamUser> resTeamRefreshData(){
        ObservableList<ResponseTeamUser> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_USER_PATH, ResponseTeamUser.class, "teamId", manageTeamId, ParameterTypes.RESPONSE_TEAM_USER_PARAMETER_TYPES);
        return listData;
    }

    public ObservableList<ResponseTeamUser> resTeamRefreshAllData(){
        ObservableList<ResponseTeamUser> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_USER_PATH, ResponseTeamUser.class, ParameterTypes.RESPONSE_TEAM_USER_PARAMETER_TYPES);
        return listData;
    }

    public void responseTeamInfoShowListData(){
        resTeamMemberIdCol.setCellValueFactory(new PropertyValueFactory<>("memberId"));
        resTeamMemberCol.setCellValueFactory(new PropertyValueFactory<>("memberName"));
        resTeamContactCol.setCellValueFactory(new PropertyValueFactory<>("contactInfo"));
        resTeamUsernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        resTeamInfoTable.setItems(resTeamRefreshData());
    }

    public boolean resTeamCheckEmpty(){
        if (resTeamMemberNameField.getText().isEmpty() || resTeamUsernameField.getText().isEmpty() || resTeamPasswordField.getText().isEmpty() || resTeamContactField.getText().isEmpty()) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    public boolean resTeamCheckSelected(){
        if (resTeamInfoTable.getSelectionModel().getSelectedItem() == null) {
            // show error message
            alert.errorMessage("Please select a response team user");
            return true;
        }
        return false;
    }

    public void resTeamInfoAddBtnAction(){
        String teamId = resAssignTeamComboBox.getValue();
        String resMemberName = resTeamMemberNameField.getText().trim();
        String resTeamContact = resTeamContactField.getText().trim();
        String resTeamUsername = resTeamUsernameField.getText().trim();
        String resTeamPassword = resTeamPasswordField.getText().trim();

        

        if(!resTeamCheckEmpty()){
            if (rtucheckInput.validationResponseTeamUser(teamId, resMemberName, resTeamContact, resTeamUsername, resTeamPassword) == 1) {

                // check if username already exists
                for (ResponseTeamUser responseTeamUser : resTeamRefreshAllData()){
                    if (responseTeamUser.getUsername().equals(resTeamUsername)){
                        alert.errorMessage("Username already exists");
                        return;
                    }
                }

                //generate response team user id
                String resTeamUserID = "RTU" + String.format("%d", csvHandler.getMaxId(resTeamRefreshAllData(), ResponseTeamUser::getMemberId, "RTU") + 1);

                //crete new response team user object
                ResponseTeamUser new_users = new ResponseTeamUser(teamId, resTeamUserID, resMemberName, resTeamContact, resTeamUsername, resTeamPassword);
                
                //add new response team user to csv file
                csvHandler.writeCSV(CSVPath.RESPONSETEAM_USER_PATH, new_users);

                //show success message
                alert.successMessage("Response team user added successfully");

                //refresh data
                resTeamRefreshData();

                //refresh table
                responseTeamInfoShowListData();

                // search filter reset
                searchFieldTeamInfo();

                // unfocus all
                unFocusAll();

                //reset fields
                resTeamRestBtnAction();

            }else{

                // show error message
                alert.errorMessage("Please fill in all the fields");                
              
            }
        }
    }

    public void resTeamInfoUpdateBtnAction(){
        if(!resTeamCheckSelected()){
            String teamId = resAssignTeamComboBox.getValue();
            String resTeamUserId = resTeamInfoTable.getSelectionModel().getSelectedItem().getMemberId();
            String resMemberName = resTeamMemberNameField.getText().trim();
            String resTeamContact = resTeamContactField.getText().trim();
            String resTeamUsername = resTeamUsernameField.getText().trim();
            String resTeamPassword = resTeamPasswordField.getText().trim();

            if(!resTeamCheckEmpty()){
                if(rtucheckInput.validationResponseTeamUser(teamId, resMemberName, resTeamContact, resTeamUsername, resTeamPassword) == 1){
                    
                    // check if username already exists
                    for (ResponseTeamUser responseTeamUser : resTeamRefreshAllData()){
                        if (responseTeamUser.getUsername().equals(resTeamUsername)){
                            alert.errorMessage("Username already exists");
                            return;
                        }
                    }

                    //creata new response team user object
                    ResponseTeamUser updatedResponseTeamUsers = new ResponseTeamUser(teamId, resTeamUserId, resMemberName, resTeamContact, resTeamUsername, resTeamPassword);

                    //update response team user in csv file
                    csvHandler.updateCSV(CSVPath.RESPONSETEAM_USER_PATH, 1, resTeamUserId, updatedResponseTeamUsers);

                    //show success message
                    alert.successMessage("Response team user updated successfully");

                    //refresh data
                    resTeamRefreshData();

                    //refresh table
                    responseTeamInfoShowListData();

                    // search filter reset
                    searchFieldTeamInfo();

                    // unfocus all
                    unFocusAll();

                    //reset fields
                    resTeamRestBtnAction();

                }
            }
        }
    }

    public void resTeamInfoDeleteBtnAction(){
        if(!resTeamCheckSelected()){
            //get selected response team user
            String resTeamUserId = resTeamInfoTable.getSelectionModel().getSelectedItem().getMemberId();
            
            //delete response team user in csv file
            csvHandler.deleteCSV(CSVPath.RESPONSETEAM_USER_PATH, 1, resTeamUserId);

            //show success message
            alert.successMessage("Response team user deleted successfully");

            //refresh data
            resTeamRefreshData();

            //refresh table
            responseTeamInfoShowListData();

            // search filter reset
            searchFieldTeamInfo();

            // unfocus all
            unFocusAll();

            //reset fields
            resTeamRestBtnAction();
        }
    }

}
