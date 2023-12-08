package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import com.example.Incident;
import com.example.ResponseTeam;
import com.example.CSVRelatedClass.CSVHandler;
import com.example.CSVRelatedClass.CSVPath;
import com.example.CSVRelatedClass.CustomComparator;
import com.example.CSVRelatedClass.ParameterTypes;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class IncidentListAdminController {

    @FXML
    private Button addBtn;

    @FXML
    private Button deleteBtn;

    @FXML
    private TableColumn<Incident, String> icdAffectedCol;

    @FXML
    private TextField icdAffectedField;

    @FXML
    private ComboBox<String> icdAssignTeamComboBox;

    @FXML
    private TableColumn<Incident, String> icdDescCol;

    @FXML
    private TextField icdDescField;

    @FXML
    private TableColumn<Incident, String> icdHandleCol;

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
    private Button manageResTeamBtn;

    @FXML
    private Button resetBtn;

    @FXML
    private TextField searchField;

    @FXML
    private Label unameLabel;

    @FXML
    private Button updateBtn;

    CSVHandler csvHandler = new CSVHandler();

    @FXML
    void initialize(){
        icdSecLvlComboBox.getItems().addAll("Low", "Medium", "High");
        icdStatusComboBox.getItems().addAll("Open", "Closed");

        ObservableList<ResponseTeam> responseTeamslistData = csvHandler.readCSV(CSVPath.RESPONSETEAM_PATH, ResponseTeam.class, ParameterTypes.RESPONSE_TEAM_PARAMETER_TYPES);

        Set<String> uniqueResponseTeamNames = new HashSet<>();

        for (ResponseTeam responseTeams : responseTeamslistData) {
            String responseTeamsName = responseTeams.getTeamId();

            // Check if the response team name is not already in the Set
            if (!uniqueResponseTeamNames.contains(responseTeamsName)) {
                icdAssignTeamComboBox.getItems().add(responseTeamsName);

                // Add the response team name to the Set to track it
                uniqueResponseTeamNames.add(responseTeamsName);

            }
        }

        manageResTeamBtn.setOnAction(event -> {
            try {
                FXMLLoader loader = new FXMLLoader();
                Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/ManageResponseTeam.fxml"));
                Scene scene = new Scene(root);
                Stage stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                // ManageResTeamController controller = loader.getController();
                // controller.initData(admin);
                stage.show();
                Stage currentStage = (Stage) manageResTeamBtn.getScene().getWindow();
                currentStage.close();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    }

}
