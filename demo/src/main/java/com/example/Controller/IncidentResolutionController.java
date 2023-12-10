package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;

import com.example.AlertMessage;
import com.example.Incident;
import com.example.Resolution;
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
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class IncidentResolutionController {

    @FXML
    private TableColumn<Resolution, String> resolutionIdCol;

    @FXML
    private Button addBtn;

    @FXML
    private Button backBtn;

    @FXML
    private TableColumn<Resolution, String> closureNotesCol;

    @FXML
    private TextArea closureNotesField;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField icdInfoActionId;

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
    private TableView<Resolution> incidentResolutionTable;

    @FXML
    private StackPane incident_resolution_page;

    @FXML
    private TableColumn<Resolution, String> lessonsLearnedCol;

    @FXML
    private TextArea lessonsLearnedField;

    @FXML
    private Button resetBtn;

    @FXML
    private TableColumn<Resolution, String> resolutionStatusCol;

    @FXML
    private ComboBox<String> resolutionStatusComboBox;

    @FXML
    private TextField searchField;

    @FXML
    private Label unameLabel;

    @FXML
    private Button updateBtn;

    @FXML
    void initialize(){

        resolutionStatusComboBox.getItems().addAll("Resolved", "Partially Resolved", "Not Resolved", "False Positive", "Reoccurred", "Closed without action", "Mitigated and Closed", "Pending Verification", "Awaiting Closure", "Escalated");
        
        backBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/ResponseActions.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.show();
                ResponseActionsController controller = loader.getController();
                controller.initData(user, incident_info, analysis_id);
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


        resetBtn.setOnAction(event -> {
            resetBtnAction();
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

        lessonsLearnedField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextAreaKeyPress);
        closureNotesField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextAreaKeyPress);
        resolutionStatusComboBox.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextFieldKeyPress);

        unFocusAll();

        incidentResolutionTable.getColumns().forEach(e -> e.setReorderable(false));
        incidentResolutionTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1){
                Resolution selectedResolution = incidentResolutionTable.getSelectionModel().getSelectedItem();
                if (selectedResolution != null) {
                    // Do something with the selected resolution data
                    System.out.println("Selected resolution ID: " + selectedResolution.getResolutionId());
                    lessonsLearnedField.setText(selectedResolution.getLessonsLearned());
                    closureNotesField.setText(selectedResolution.getClosureNotes());
                    resolutionStatusComboBox.setValue(selectedResolution.getResolutionStatus());
                }
            }
        });
    }

    private CSVHandler csvHandler = new CSVHandler();
    private AlertMessage alert = new AlertMessage();
    private Resolution checkInput = new Resolution();

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
        if (lessonsLearnedField.isFocused()) {
            closureNotesField.requestFocus();
        } else if (closureNotesField.isFocused()) {
            resolutionStatusComboBox.requestFocus();
        } else if (resolutionStatusComboBox.isFocused()) {
            lessonsLearnedField.requestFocus();
        }
    }

    private User user;
    private Incident incident_info;
    private String analysis_id;

    public void initData(User user, Incident incident_info, String analysis_id, String action_id){
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
        icdInfoActionId.setText(action_id);
        incidentResolutionShowListData();
    }

    public void resetBtnAction(){
        lessonsLearnedField.setText("");
        closureNotesField.setText("");
        resolutionStatusComboBox.setValue("");
        incidentResolutionTable.getSelectionModel().clearSelection();
        incidentResolutionTable.setItems(refreshData());
        incidentResolutionShowListData();
    }

    public void unFocusAll(){
        lessonsLearnedField.setFocusTraversable(false);
        closureNotesField.setFocusTraversable(false);
        resolutionStatusComboBox.setFocusTraversable(false);
        incidentResolutionTable.setFocusTraversable(false);
        backBtn.setFocusTraversable(false);
        addBtn.setFocusTraversable(false);
        updateBtn.setFocusTraversable(false);
        deleteBtn.setFocusTraversable(false);
        resetBtn.setFocusTraversable(false);
        icdListBtn.setFocusTraversable(false);
        searchField.setFocusTraversable(false);
    }

    public ObservableList<Resolution> refreshData(){
        String actionId = icdInfoActionId.getText();
        ObservableList<Resolution> listData = csvHandler.readCSV(CSVPath.RESOLUTION_PATH, Resolution.class, "actionId", actionId, CustomComparator.createComparator(Resolution::getResolutionId, 1), ParameterTypes.RESOLUTION_PARAMETER_TYPES);
        return listData;
    }

    public ObservableList<Resolution> refreshAllData(){
        ObservableList<Resolution> listData = csvHandler.readCSV(CSVPath.RESOLUTION_PATH, Resolution.class, CustomComparator.createComparator(Resolution::getResolutionId, 1), ParameterTypes.RESOLUTION_PARAMETER_TYPES);
        return listData;
    }


    public void incidentResolutionShowListData(){
        resolutionIdCol.setCellValueFactory(new PropertyValueFactory<>("resolutionId"));
        lessonsLearnedCol.setCellValueFactory(new PropertyValueFactory<>("lessonsLearned"));
        closureNotesCol.setCellValueFactory(new PropertyValueFactory<>("closureNotes"));
        resolutionStatusCol.setCellValueFactory(new PropertyValueFactory<>("resolutionStatus"));
        
        closureNotesCol.setCellFactory(tc -> {
            TableCell<Resolution, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(closureNotesCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        lessonsLearnedCol.setCellFactory(tc -> {
            TableCell<Resolution, String> cell = new TableCell<>();
            Text text = new Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(lessonsLearnedCol.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        
        incidentResolutionTable.setItems(refreshData());
    }

    private void searchFilter(){
        FilteredList<Resolution> filteredData = new FilteredList<>(refreshData(), e -> true);

        if (searchField.getText() != null){
            searchField.setOnKeyReleased(e->{
        
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Resolution -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();
                    String resolutionStatus = String.valueOf(Resolution.getResolutionStatus()).toLowerCase();

                        if(Resolution.getResolutionId().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Resolution.getLessonsLearned().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Resolution.getClosureNotes().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(resolutionStatus.equals(toLowerCaseNewValue)){
                            return true;
                        }
                return false;

                });
            });
                final SortedList<Resolution> resolution_list = new SortedList<>(filteredData);
                resolution_list.comparatorProperty().bind(incidentResolutionTable.comparatorProperty());
                incidentResolutionTable.setItems(resolution_list);
            });
        }
    }

    private boolean checkEmpty(){
        if (lessonsLearnedField.getText().isEmpty() || closureNotesField.getText().isEmpty() || resolutionStatusComboBox.getValue() == null) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    private boolean checkSelected(){
        if (incidentResolutionTable.getSelectionModel().getSelectedItem() == null) {
            // show error message
            alert.errorMessage("Please select a resolution");
            return true;
        }
        return false;
    }

    public void addBtnAction(){
        String actionId = icdInfoActionId.getText();
        String lessonsLeadned = lessonsLearnedField.getText();
        String closureNotes = closureNotesField.getText();
        String resolutionStatus = resolutionStatusComboBox.getValue();

        if (!checkEmpty()){
            if(checkInput.validationResolution(actionId, lessonsLeadned, closureNotes, resolutionStatus) == 1){

                // generate resolution id
                String resolution_id = "R" + String.format("%d", csvHandler.getMaxId(refreshAllData(), Resolution::getResolutionId, "R") + 1);

                // create resolution object
                Resolution resolution = new Resolution(resolution_id, actionId, lessonsLeadned, closureNotes, resolutionStatus);

                // write to csv
                csvHandler.writeCSV(CSVPath.RESOLUTION_PATH, resolution);
                
                //show success message
                alert.successMessage("Resolution added successfully");

                //refresh data
                refreshData();
                
                // refresh table
                incidentResolutionShowListData();

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
            // get selected resolution
            String resolutionId = incidentResolutionTable.getSelectionModel().getSelectedItem().getResolutionId();
            String actionId = icdInfoActionId.getText();
            String lessonsLeadned = lessonsLearnedField.getText();
            String closureNotes = closureNotesField.getText();
            String resolutionStatus = resolutionStatusComboBox.getValue();

            if (!checkEmpty()){
                if(checkInput.validationResolution(actionId, lessonsLeadned, closureNotes, resolutionStatus) == 1){

                    // create resolution object
                    Resolution updatedresolution = new Resolution(resolutionId, actionId, lessonsLeadned, closureNotes, resolutionStatus);

                    // update csv
                    csvHandler.updateCSV(CSVPath.RESOLUTION_PATH, 0, resolutionId, updatedresolution);

                    //show success message
                    alert.successMessage("Resolution updated successfully");

                    //refresh data
                    refreshData();

                    // refresh table
                    incidentResolutionShowListData();

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
        

        if(!checkSelected()){
            // get selected resolution
            String resolutionId = incidentResolutionTable.getSelectionModel().getSelectedItem().getResolutionId();

            // delete csv
            csvHandler.deleteCSV(CSVPath.RESOLUTION_PATH, 0, resolutionId);

            //show success message
            alert.successMessage("Resolution deleted successfully");

            //refresh data
            refreshData();

            // refresh table
            incidentResolutionShowListData();

            // search filter reset
            searchFilter();

            // unfocus all
            unFocusAll();

            // reset fields
            resetBtnAction();
        }
    }

}
