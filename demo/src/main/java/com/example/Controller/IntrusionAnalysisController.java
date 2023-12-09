package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;

import com.example.Admin;
import com.example.AlertMessage;
import com.example.Analysis;
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

public class IntrusionAnalysisController {

    @FXML
    private Button addBtn;

    @FXML
    private TableColumn<Analysis, String> analysisDetailsCol;

    @FXML
    private TextArea analysisDetailsField;

    @FXML
    private TableColumn<Analysis, String> analysisIdCol;

    @FXML
    private TableColumn<Analysis, String> attackSignatureCol;

    @FXML
    private TextField attackSignatureField;

    @FXML
    private TableColumn<Analysis, String> attackVectorCol;

    @FXML
    private TextField attackVectorField;

    @FXML
    private TableColumn<Analysis, String> compromisedDataCol;

    @FXML
    private TextField compromisedDataField;

    @FXML
    private Button deleteBtn;

    @FXML
    private TextField icdInfoAffected;

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
    private StackPane instrusion_analysis_page;

    @FXML
    private Button resetBtn;

    @FXML
    private TableView<Analysis> analysisTable;

    @FXML
    private TextField searchField;

    @FXML
    private Label unameLabel;

    @FXML
    private Button updateBtn;

    @FXML
    void initialize(){
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
                    controller.initData(admin, user);
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
                    // IncidentListTeamController controller = loader.getController();
                    // controller.initData(admin, user);
                    Node node = (Node) event.getSource();
                    Stage currentStage = (Stage) node.getScene().getWindow();
                    currentStage.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        attackVectorField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextFieldKeyPress);
        attackSignatureField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextFieldKeyPress);
        compromisedDataField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextFieldKeyPress);
        analysisDetailsField.addEventFilter(KeyEvent.KEY_PRESSED, this::handleTextAreaKeyPress);

        unFocusAll();

        analysisTable.getColumns().forEach(e -> e.setReorderable(false));
        analysisTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1){
                Analysis selectedAnalysis = analysisTable.getSelectionModel().getSelectedItem();
                if (selectedAnalysis != null) {
                    // Do something with the selected patient data
                    System.out.println("Selected analysis ID: " + selectedAnalysis.getAnalysisId());
                    attackVectorField.setText(selectedAnalysis.getAttackVector());
                    attackSignatureField.setText(selectedAnalysis.getAttackSignature());
                    compromisedDataField.setText(selectedAnalysis.getCompromisedData());
                    analysisDetailsField.setText(selectedAnalysis.getAnalysisDetails());
                }
            } else if (event.getClickCount() == 2){
                Analysis selectedAnalysis = analysisTable.getSelectionModel().getSelectedItem();
                if (selectedAnalysis != null) {
                    // Do something with the selected patient data
                    System.out.println("Selected analysis ID: " + selectedAnalysis.getAnalysisId());
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/ResponseActions.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        // ResponseActionsController controller = loader.getController();
                        // controller.initData(admin, user, incident_info, selectedAnalysis.getAnalysisId());
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
    private Analysis checkInput = new Analysis();

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
        if (attackVectorField.isFocused()) {
            attackSignatureField.requestFocus();
        } else if (attackSignatureField.isFocused()) {
            compromisedDataField.requestFocus();
        } else if (compromisedDataField.isFocused()) {
            analysisDetailsField.requestFocus();
        } else if (analysisDetailsField.isFocused()) {
            attackVectorField.requestFocus();
        }
    }

    private Admin admin;
    private User user;
    private Incident incident_info;

    public void initData(Admin admin, User user, Incident incident_info){
        this.admin = admin;
        this.user = user;
        this.incident_info = incident_info;
        unameLabel.setText(admin.getUsername());
        icdInfoId.setText(incident_info.getIncidentId());
        icdInfoTimestamp.setText(incident_info.getTimestamp());
        icdInfoDesc.setText(incident_info.getDescription());
        icdInfoSecLvl.setText(incident_info.getSecurityLevel());
        icdInfoAffected.setText(incident_info.getAffectedSystem());
        icdInfoStatus.setText(incident_info.getIncidentStatus());
        analysisShowListData();
        searchFilter();
    }

    public void resetBtnAction(){
        attackVectorField.setText("");
        attackSignatureField.setText("");
        compromisedDataField.setText("");
        analysisDetailsField.setText("");
        analysisTable.getSelectionModel().clearSelection();
        analysisTable.setItems(refreshData());
        analysisShowListData();
    }

    public void unFocusAll(){
        attackVectorField.setFocusTraversable(false);
        attackSignatureField.setFocusTraversable(false);
        compromisedDataField.setFocusTraversable(false);
        analysisDetailsField.setFocusTraversable(false);
        addBtn.setFocusTraversable(false);
        updateBtn.setFocusTraversable(false);
        deleteBtn.setFocusTraversable(false);
        resetBtn.setFocusTraversable(false);
        icdListBtn.setFocusTraversable(false);
        analysisTable.setFocusTraversable(false);
    }

    public ObservableList<Analysis> refreshData(){
        String incidentId = incident_info.getIncidentId();
        ObservableList<Analysis> listData = csvHandler.readCSV(CSVPath.ANALYSIS_PATH, Analysis.class, "incidentId", incidentId, CustomComparator.createComparator(Analysis::getAnalysisId, 4), ParameterTypes.ANALYSIS_PARAMETER_TYPES);
        return listData;
    }

    public ObservableList<Analysis> refreshAllData(){
        ObservableList<Analysis> listData = csvHandler.readCSV(CSVPath.ANALYSIS_PATH, Analysis.class, CustomComparator.createComparator(Analysis::getAnalysisId, 4), ParameterTypes.ANALYSIS_PARAMETER_TYPES);
        return listData;
    }

    public void analysisShowListData(){

        analysisIdCol.setCellValueFactory(new PropertyValueFactory<>("analysisId"));
        attackVectorCol.setCellValueFactory(new PropertyValueFactory<>("attackVector"));
        attackSignatureCol.setCellValueFactory(new PropertyValueFactory<>("attackSignature"));
        compromisedDataCol.setCellValueFactory(new PropertyValueFactory<>("compromisedData"));
        analysisDetailsCol.setCellValueFactory(new PropertyValueFactory<>("analysisDetails"));

        analysisTable.setItems(refreshData());

    }

    private void searchFilter(){
        FilteredList<Analysis> filteredData = new FilteredList<>(refreshData(), e -> true);

        if (searchField.getText() != null){
            searchField.setOnKeyReleased(e->{
        
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(Analysis -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();

                        if(Analysis.getAnalysisId().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Analysis.getAttackVector().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Analysis.getAttackSignature().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Analysis.getCompromisedData().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }else if(Analysis.getAnalysisDetails().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }
                        

                return false;

                });
            });
                final SortedList<Analysis> analysis_list = new SortedList<>(filteredData);
                analysis_list.comparatorProperty().bind(analysisTable.comparatorProperty());
                analysisTable.setItems(analysis_list);
            });
        }
    }

    private boolean checkEmpty(){
        if (attackVectorField.getText().isEmpty() || attackSignatureField.getText().isEmpty() || compromisedDataField.getText().isEmpty() || analysisDetailsField.getText().isEmpty()) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    private boolean checkSelected(){
        if (analysisTable.getSelectionModel().isEmpty()) {
            // show error message
            alert.errorMessage("Please select a analysis");
            return true;
        }
        return false;
    }

    public void addBtnAction(){
        String incident_id = incident_info.getIncidentId();
        String attackVector = attackVectorField.getText();
        String attackSignature = attackSignatureField.getText();
        String compromisedData = compromisedDataField.getText();
        String analysisDetails = analysisDetailsField.getText();

        if (!checkEmpty()){
            if (checkInput.validationAnalysis(incident_id, attackVector, attackSignature, compromisedData, analysisDetails) == 1) {

                //generate new analysis id
                String analysis_id = "ANLY" + String.format("%d", csvHandler.getMaxId(refreshAllData(), Analysis::getAnalysisId, "ANLY") + 1);

                //create new analysis object
                Analysis analysis = new Analysis(analysis_id, incident_id, attackVector, attackSignature, compromisedData, analysisDetails);

                //write to csv
                csvHandler.writeCSV(CSVPath.ANALYSIS_PATH, analysis);

                //show success message
                alert.successMessage("Analysis added successfully");

                //refresh data
                refreshData();

                //refresh table
                analysisShowListData();

                // search filter reset
                searchFilter();

                // unfocus all
                unFocusAll();

                //reset fields
                resetBtnAction();

            } else {
                alert.errorMessage("Please fill in all the fields");
            }
        }

    }

    public void updateBtnAction(){
        if (!checkSelected()){
            //get selected analysis
            String analysis_id = analysisTable.getSelectionModel().getSelectedItem().getAnalysisId();
            String incident_id = incident_info.getIncidentId();
            String attackVector = attackVectorField.getText();
            String attackSignature = attackSignatureField.getText();
            String compromisedData = compromisedDataField.getText();
            String analysisDetails = analysisDetailsField.getText();

            if (!checkEmpty()){
                if(checkInput.validationAnalysis(incident_id, attackVector, attackSignature, compromisedData, analysisDetails) == 1){
                    //create new analysis object
                    Analysis updatedAnalysis = new Analysis(analysis_id, incident_id, attackVector, attackSignature, compromisedData, analysisDetails);

                    //update csv
                    csvHandler.updateCSV(CSVPath.ANALYSIS_PATH, 0, analysis_id, updatedAnalysis);
                    
                    //show success message
                    alert.successMessage("Analysis updated successfully");

                    //refresh data
                    refreshData();

                    //refresh table
                    analysisShowListData();

                    // search filter reset
                    searchFilter();

                    // unfocus all
                    unFocusAll();

                    //reset fields
                    resetBtnAction();

                } else {
                    alert.errorMessage("Please fill in all the fields");
                }
            }
        }

    }

    public void deleteBtnAction(){
        if (!checkSelected()){
            //get selected analysis
            String analysis_id = analysisTable.getSelectionModel().getSelectedItem().getAnalysisId();

            //delete csv
            csvHandler.deleteCSV(CSVPath.ANALYSIS_PATH, 0, analysis_id);

            //show success message
            alert.successMessage("Analysis deleted successfully");

            //refresh data
            refreshData();

            //refresh table
            analysisShowListData();

            // search filter reset
            searchFilter();

            // unfocus all
            unFocusAll();

            //reset fields
            resetBtnAction();
        }
    }






}
