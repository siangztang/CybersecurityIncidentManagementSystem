package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;

import com.example.AlertMessage;
import com.example.ResponseTeam;
import com.example.User;
import com.example.CSVRelatedClass.CSVHandler;
import com.example.CSVRelatedClass.CSVPath;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class ManageResTeamController {

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableView<ResponseTeam> responseTeamTable;

    @FXML
    private Button manageIcdBtn;

    @FXML
    private Button manageResTeamBtn;

    @FXML
    private StackPane manage_response_team_page;

    @FXML
    private TableColumn<ResponseTeam, String> resTeamIDCol;

    @FXML
    private TableColumn<ResponseTeam, String> resTeamNameCol;

    @FXML
    private TextField resTeamNameField;

    @FXML
    private Button resetBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Label unameLabel;

    @FXML
    private Button updateBtn;


    @FXML
    void initialize(){
        manageIcdBtn.setOnAction(event -> {
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

        unFocusAll();

        responseTeamTable.getColumns().forEach(e -> e.setReorderable(false));
        responseTeamTable.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1){
                ResponseTeam selectedResponseTeam = responseTeamTable.getSelectionModel().getSelectedItem();
                if (selectedResponseTeam != null) {
                    // Do something with the selected Response Team data
                    System.out.println("Selected Response Team ID: " + selectedResponseTeam.getTeamId());
                    resTeamNameField.setText(selectedResponseTeam.getTeamName());
                }
            } else if (event.getClickCount() == 2){
                ResponseTeam selectedResponseTeam = responseTeamTable.getSelectionModel().getSelectedItem();
                if (selectedResponseTeam != null) {
                    // Do something with the selected Response Team data
                    System.out.println("Selected Response Team ID: " + selectedResponseTeam.getTeamId());
                    try {
                        FXMLLoader loader = new FXMLLoader();
                        Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/ResponseTeamDetails.fxml"));
                        Scene scene = new Scene(root);
                        Stage stage = new Stage();
                        stage.setScene(scene);
                        stage.setResizable(false);
                        stage.show();
                        // ResponseTeamDetailsController controller = loader.getController();
                        // controller.initData(user, incident_info, analysis_id, selectedActions.getActionId());
                        Node node = (Node) event.getSource();
                        Stage currentStage = (Stage) node.getScene().getWindow();
                        currentStage.close();
                        
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        responseTeamTableShowListData();
        searchFilter();
    }

    private CSVHandler csvHandler = new CSVHandler();
    private AlertMessage alert = new AlertMessage();

    private User user;

    public void initData(User user){
        System.out.println("test");
        this.user = user;
        unameLabel.setText(user.getUsername());
        responseTeamTableShowListData();
    }

    public void resetBtnAction(){
        resTeamNameField.setText("");
        responseTeamTable.getSelectionModel().clearSelection();
        responseTeamTable.setItems(refreshData());
        responseTeamTableShowListData();
    }

    public void unFocusAll(){
        manageIcdBtn.setFocusTraversable(false);
        manageResTeamBtn.setFocusTraversable(false);
        addBtn.setFocusTraversable(false);
        updateBtn.setFocusTraversable(false);
        deleteBtn.setFocusTraversable(false);
        resetBtn.setFocusTraversable(false);
        searchField.setFocusTraversable(false);
        responseTeamTable.setFocusTraversable(false);
        resTeamNameField.setFocusTraversable(false);
    }

    public ObservableList<ResponseTeam> refreshData(){

        ObservableList<ResponseTeam> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_PATH, ResponseTeam.class, ParameterTypes.RESPONSE_TEAM_PARAMETER_TYPES);

        return listData;
    }

    public void responseTeamTableShowListData(){

        resTeamIDCol.setCellValueFactory(new PropertyValueFactory<>("teamId"));
        resTeamNameCol.setCellValueFactory(new PropertyValueFactory<>("teamName"));

        responseTeamTable.setItems(refreshData());

    }

    private void searchFilter(){
        FilteredList<ResponseTeam> filteredData = new FilteredList<>(refreshData(), e -> true);

        if (searchField.getText() != null){
            searchField.setOnKeyReleased(e->{
        
            searchField.textProperty().addListener((observable, oldValue, newValue) -> {
                filteredData.setPredicate(ResponseTeam -> {
                    if (newValue == null || newValue.isEmpty()) {
                        return true;
                    }
                    String toLowerCaseNewValue = newValue.toLowerCase();

                        if(ResponseTeam.getTeamId().toLowerCase().contains(toLowerCaseNewValue)){
                            return true;
                        }

                    
                return false;

                });
            });
                final SortedList<ResponseTeam> response_team_list = new SortedList<>(filteredData);
                response_team_list.comparatorProperty().bind(responseTeamTable.comparatorProperty());
                responseTeamTable.setItems(response_team_list);
            });
        }
    }

    private boolean checkEmpty(){
        if (resTeamNameField.getText().isEmpty()) {
            // show error message
            alert.errorMessage("Please fill in all the fields");
            return true;
        }
        return false;
    }

    private boolean checkSelected(){
        if (responseTeamTable.getSelectionModel().getSelectedItem() == null) {
            // show error message
            alert.errorMessage("Please select an response team");
            return true;
        }
        return false;
    }

    public void addBtnAction(){
        String teamName = resTeamNameField.getText();

        if(!checkEmpty()){
            //get team id
            String teamId = "T" + String.format("%d", csvHandler.getMaxId(refreshData(), ResponseTeam::getTeamId, "T") + 1);

            //create new analysis object
            ResponseTeam team = new ResponseTeam(teamId, teamName);

            //write to csv
            csvHandler.writeCSV(CSVPath.RESPONSETEAM_PATH, team);

            //show success message
            alert.successMessage("Response Team added successfully");

            //refresh data
            refreshData();

            //refresh table
            responseTeamTableShowListData();

            // search filter reset
            searchFilter();

            // unfocus all
            unFocusAll();

            //reset fields
            resetBtnAction();

        }
    }

    public void updateBtnAction(){
        if(!checkSelected()){
            String teamId = responseTeamTable.getSelectionModel().getSelectedItem().getTeamId();
            String teamName = resTeamNameField.getText();

            if(!checkEmpty()){

                //create new incident object
                ResponseTeam updatedTeam = new ResponseTeam(teamId, teamName);

                //update incident in csv file
                csvHandler.updateCSV(CSVPath.RESPONSETEAM_PATH, 0, teamId, updatedTeam);

                //show success message
                alert.successMessage("Response Team updated successfully");

                //refresh data
                refreshData();

                //refresh table
                responseTeamTableShowListData();

                // search filter reset
                searchFilter();

                // unfocus all
                unFocusAll();

                //reset fields
                resetBtnAction();

            } else {
                // show error message
                alert.errorMessage("Please fill in all the fields");
            }
        }

        
    }

    public void deleteBtnAction(){
        
        if(!checkSelected()){
            String teamId = responseTeamTable.getSelectionModel().getSelectedItem().getTeamId();

            //delete incident in csv file
            csvHandler.deleteCSV(CSVPath.RESPONSETEAM_PATH, 0, teamId);

            //show success message
            alert.successMessage("Response Team deleted successfully");

            //refresh data
            refreshData();

            //refresh table
            responseTeamTableShowListData();

            // search filter reset
            searchFilter();

            // unfocus all
            unFocusAll();

            //reset fields
            resetBtnAction();

        }
    }



}
