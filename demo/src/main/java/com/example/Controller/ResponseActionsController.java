package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;

import com.example.Actions;
import com.example.AlertMessage;
import com.example.Incident;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ResponseActionsController {

    @FXML
    private TableColumn<Actions, String> actionDescCol;

    @FXML
    private TextArea actionDescField;

    @FXML
    private TableColumn<Actions, String> actionIdCol;

    @FXML
    private TableColumn<Actions, String> actionStatusCol;

    @FXML
    private ComboBox<String> actionStatusComboBox;

    @FXML
    private TableColumn<Actions, String> actionTypeCol;

    @FXML
    private ComboBox<String> actionTypeComboBox;

    @FXML
    private Button addBtn;

    @FXML
    private Button backBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField icdInfoAffected;

    @FXML
    private TextField icdInfoAnalysisId;

    @FXML
    private TextField icdInfoDesc;

    @FXML
    private TextField icdInfoId;

    @FXML
    private TextField icdInfoSecLvl;

    @FXML
    private TextField icdInfoStatus;

    @FXML
    private TextField icdInfoTimestamp;

    @FXML
    private Button icdListBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private TableView<Actions> responseActionTable;

    @FXML
    private TextField searchField;

    @FXML
    private StackPane response_actions_page;

    @FXML
    private Label unameLabel;

    @FXML
    private Button updateBtn;

    @FXML
    void initialize(){
        
        actionTypeComboBox.getItems().addAll("Isolation", "Patching");
        actionStatusComboBox.getItems().addAll("Not Started", "In Progress", "Pending", "Completed", "Failed", "Deferred", "Cancelled", "Reopened", "Reviewe Required", "Awaiting Verification", "On Hold");

        backBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IntrusionAnalysis.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                IntrusionAnalysisController controller = loader.getController();
                controller.initData(user, incident_info);
                Node node = (Node) event.getSource();
                Stage currentStage = (Stage) node.getScene().getWindow();
                currentStage.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        icdListBtn.setOnAction(event -> {
            if ("admin".equals(user.getUsername())){
                try {
                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IncidentListsAdmin.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    IncidentListAdminController controller = loader.getController();
                    controller.initData(user);
                    Node node = (Node) event.getSource();
                    Stage currentStage = (Stage) node.getScene().getWindow();
                    currentStage.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IncidentListsTeam.fxml"));
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    stage.show();
                    IncidentListTeamController controller = loader.getController();
                    controller.initData(user);
                    Node node = (Node) event.getSource();
                    Stage currentStage = (Stage) node.getScene().getWindow();
                    currentStage.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        addBtn.setOnAction(event -> {
            addBtnAction();
        });

        updateBtn.setOnAction(event -> {
            updateBtnAction();
        });

        deleteBtn.setOnAction(event -> {
            deleteBtnAction();
        });

        resetBtn.setOnAction(event -> {
            resetBtnAction();
        });

        actionTypeComboBox.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextFieldKeyPress);
        actionDescField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextAreaKeyPress);
        actionStatusComboBox.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextFieldKeyPress);

        unFocusAll();

        responseActionTable.getColumns().forEach(e -> e.setReorderable(false));
        responseActionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1){
                Actions selectedActions = responseActionTable.getSelectionModel().getSelectedItem();
                if (selectedActions != null) {
                    // Do something with the selected actions data
                    System.out.println("Selected actions ID: " + selectedActions.getActionId());
                    actionTypeComboBox.setValue(selectedActions.getActionType());
                    actionDescField.setText(selectedActions.getActionDesc());
                    actionStatusComboBox.setValue(selectedActions.getActionStatus());
                }
            } else if (event.getClickCount() == 2){
                Actions selectedActions = responseActionTable.getSelectionModel().getSelectedItem();
                if (selectedActions != null) {
                    // Do something with the selected actions data
                    System.out.println("Selected actions ID: " + selectedActions.getActionId());
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IncidentResolution.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        IncidentResolutionController controller = loader.getController();
                        controller.initData(user, incident_info, analysis_id, selectedActions.getActionId());
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

    private CSVHandler csvHandler = new CSVHandler();
    private AlertMessage alert = new AlertMessage();
    private Actions checkInput = new Actions();

    private void handleTextFieldKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            event.consume();
            jumpToNextField();
        }
    }

    private void handleTextAreaKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            event.consume();
            jumpToNextField();
        }
    }

    private void jumpToNextField() {
        if (actionTypeComboBox.isFocused()) {
            actionDescField.requestFocus();
        } else if (actionDescField.isFocused()) {
            actionStatusComboBox.requestFocus();
        } else if (actionStatusComboBox.isFocused()) {
            actionTypeComboBox.requestFocus();
        }
    }

    private User user;
    private Incident incident_info;
    private String analysis_id;

    public void initData(User user, Incident incident_info, String analysis_id){
        this.user = user;
        this.incident_info = incident_info;
        this.analysis_id = analysis_id;
        unameLabel.setText(user.getUsername());
        icdInfoId.setText(incident_info.getIncidentId());
        icdInfoDesc.setText(incident_info.getDescription());
        icdInfoTimestamp.setText(incident_info.getTimestamp());
        icdInfoSecLvl.setText(incident_info.getSecurityLevel());
        icdInfoAffected.setText(incident_info.getAffectedSystem());
        icdInfoStatus.setText(incident_info.getIncidentStatus());
        icdInfoAnalysisId.setText(analysis_id);
        responseActionShowListData();
    }

    public void resetBtnAction(){
        actionTypeComboBox.setValue("");
        actionDescField.setText("");
        actionStatusComboBox.setValue("");
        responseActionTable.getSelectionModel().clearSelection();
        responseActionTable.setItems(refreshData());
        responseActionShowListData();
    }

    public void unFocusAll(){
        actionTypeComboBox.setFocusTraversable(false);
        actionDescField.setFocusTraversable(false);
        actionStatusComboBox.setFocusTraversable(false);
        responseActionTable.setFocusTraversable(false);
        backBtn.setFocusTraversable(false);
        addBtn.setFocusTraversable(false);
        updateBtn.setFocusTraversable(false);
        deleteBtn.setFocusTraversable(false);
        resetBtn.setFocusTraversable(false);
        icdListBtn.setFocusTraversable(false);
        searchField.setFocusTraversable(false);
    }

    public ObservableList<Actions> refreshData(){
        String analysisId = icdInfoAnalysisId.getText();
        ObservableList<Actions> listData = csvHandler.readCSV(CSVPath.ACTION_PATH, Actions.class, "analysisId", analysisId, CustomComparator.createComparator(Actions::getActionId, 3), ParameterTypes.ACTIONS_PARAMETER_TYPES);
        return listData;
    }

    public ObservableList<Actions> refreshAllData(){
        ObservableList<Actions> listData = csvHandler.readCSV(CSVPath.ACTION_PATH, Actions.class, CustomComparator.createComparator(Actions::getActionId, 3), ParameterTypes.ACTIONS_PARAMETER_TYPES);
        return listData;
    }

    public void responseActionShowListData(){
        actionIdCol.setCellValueFactory(new PropertyValueFactory<>("actionId"));
        actionTypeCol.setCellValueFactory(new PropertyValueFactory<>("actionType"));
        actionDescCol.setCellValueFactory(new PropertyValueFactory<>("actionDesc"));
        actionStatusCol.setCellValueFactory(new PropertyValueFactory<>("actionStatus"));
        responseActionTable.setItems(refreshData());
    }

    private void searchFilter(){
        FilteredList<Actions> filteredData = new FilteredList<>(refreshData(), e -> true);

        if (searchField.getText() != null){
            searchField.setOnKeyReleased(e->{
        
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Actions -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();
                    String actionType = String.valueOf(Actions.getActionType()).toLowerCase();
                    String actionStatus = String.valueOf(Actions.getActionStatus()).toLowerCase();

                        if(Actions.getActionId().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(actionType.equals(toLowerCaseNewValue)){
                            return true;
                        }else if(Actions.getActionDesc().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(actionStatus.equals(toLowerCaseNewValue)){
                            return true;
                        }
                        
                return false;

                });
            });
                final SortedList<Actions> actions_list = new SortedList<>(filteredData);
                actions_list.comparatorProperty().bind(responseActionTable.comparatorProperty());
                responseActionTable.setItems(actions_list);
            });
        }
    }


    private boolean checkEmpty(){
        if (actionTypeComboBox.getValue() == null || actionDescField.getText().isEmpty() || actionStatusComboBox.getValue() == null) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    private boolean checkSelected(){
        if (responseActionTable.getSelectionModel().getSelectedItem() == null) {
            // show error message
            alert.errorMessage("Please select an action");
            return true;
        }
        return false;
    }

    public void addBtnAction(){

        String analysisId = icdInfoAnalysisId.getText();
        String actionType = actionTypeComboBox.getValue();
        String actionDesc = actionDescField.getText();
        String actionStatus = actionStatusComboBox.getValue();

        if(!checkEmpty()){
            if(checkInput.validationActions(analysisId, actionType, actionDesc, actionStatus) == 1){

                // generate action id
                String action_id = "ACT" + String.format("%d", csvHandler.getMaxId(refreshAllData(), Actions::getActionId, "ACT") + 1);

                // create new actions object
                Actions actions = new Actions(action_id, analysisId, actionType, actionDesc, actionStatus);

                // write to csv
                csvHandler.writeCSV(CSVPath.ACTION_PATH, actions);

                // show success message
                alert.successMessage("Action added successfully");

                // refresh data
                refreshData();

                // refresh table
                responseActionShowListData();

                // search filter reset
                searchFilter();

                // unfocus all
                unFocusAll();

                // reset fields
                resetBtnAction();

            } else {
                alert.errorMessage("Please fill in all the fields");
            }
        }
    }

    public void updateBtnAction(){
        if (!checkSelected()){
            // get selected actions
            String action_id = responseActionTable.getSelectionModel().getSelectedItem().getActionId();
            String analysisId = icdInfoAnalysisId.getText();
            String actionType = actionTypeComboBox.getValue();
            String actionDesc = actionDescField.getText();
            String actionStatus = actionStatusComboBox.getValue();

            if (!checkEmpty()){
                if (checkInput.validationActions(analysisId, actionType, actionDesc, actionStatus) == 1){

                    // create new actions object
                    Actions updatedActions = new Actions(action_id, analysisId, actionType, actionDesc, actionStatus);

                    // update csv
                    csvHandler.updateCSV(CSVPath.ACTION_PATH, 0, action_id, updatedActions);

                    // show success message
                    alert.successMessage("Action updated successfully");

                    // refresh data
                    refreshData();

                    // refresh table
                    responseActionShowListData();

                    // search filter reset
                    searchFilter();

                    // unfocus all
                    unFocusAll();

                    // reset fields
                    resetBtnAction();

                } else {
                    alert.errorMessage("Please fill in all the fields");
                }
            }


        }
    }

    public void deleteBtnAction(){
        if (!checkSelected()){
            // get selected actions
            String action_id = responseActionTable.getSelectionModel().getSelectedItem().getActionId();

            // delete csv
            csvHandler.deleteCSV(CSVPath.ACTION_PATH, 0, action_id);

            // show success message
            alert.successMessage("Action deleted successfully");

            // refresh data
            refreshData();

            // refresh table
            responseActionShowListData();

            // search filter reset
            searchFilter();

            // unfocus all
            unFocusAll();

            // reset fields
            resetBtnAction();
        }
    }

    
}
