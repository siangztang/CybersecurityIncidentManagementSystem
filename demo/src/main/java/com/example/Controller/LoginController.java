package com.example.Controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.example.Admin;
import com.example.AlertMessage;
import com.example.ResponseTeam;
import com.example.User;
import com.example.CSVRelatedClass.CSVHandler;
import com.example.CSVRelatedClass.CSVPath;
import com.example.CSVRelatedClass.ParameterTypes;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField PasswordField;

    @FXML
    private TextField UsernameField;

    @FXML
    private Button lognbtn;

    @FXML
    void initialize() {
        UsernameField.setFocusTraversable(false);
        PasswordField.setFocusTraversable(false);
        lognbtn.setFocusTraversable(false);
        lognbtn.setOnAction(event -> {
            login();
        });

        UsernameField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                PasswordField.requestFocus();
            }
        });

        PasswordField.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.TAB) {
                login();
                lognbtn.requestFocus();
            }
        });
    }

    private AlertMessage alert = new AlertMessage();
    CSVHandler csvHandler = new CSVHandler();
    private static User user;
    private static Admin admin;
    private static ResponseTeam responseTeam;
    

    public void login(){
        String uname = UsernameField.getText().trim();
        String password = PasswordField.getText().trim();

        user = new User(uname, password);

        switch (user.isValidLogin()) {
            case 0:
                alert.errorMessage("Invalid Username or Password");
                break;
            case 1:
                admin = new Admin(uname, password);
                alert.successMessage("Login Successful");
                try {
                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IncidentListsAdmin.fxml"));
                    
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    // IncidentListAdminController controller = loader.getController();
                    // controller.initData(admin);
                    stage.show();
                    Stage currentStage = (Stage) lognbtn.getScene().getWindow();
                    currentStage.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case 2:
                ObservableList<ResponseTeam> listData = csvHandler.readCSV(CSVPath.RESPONSETEAM_PATH, ResponseTeam.class, ParameterTypes.RESPONSE_TEAM_PARAMETER_TYPES);

                for (ResponseTeam teamMember : listData) {


                    if (teamMember.getUsername().equals(uname) && teamMember.getPassword().equals(password)) {
                        responseTeam = new ResponseTeam(teamMember.getTeamId(), teamMember.getMemberName(), teamMember.getContactInfo(), uname, password);
                        break; // Exit the loop if a match is found
                    }
                }

                alert.successMessage("Login Successful");
                try {
                    FXMLLoader loader = new FXMLLoader();
                    Parent root = loader.load(new FileInputStream("demo/src/main/resources/com/example/IncidentListsTeam.fxml"));
                    
                    Scene scene = new Scene(root);
                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setResizable(false);
                    // IncidentListTeamController controller = loader.getController();
                    // controller.initData(responseTeam);
                    stage.show();
                    Stage currentStage = (Stage) lognbtn.getScene().getWindow();
                    currentStage.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    public static User getUser() {
        return user;
    }

    public static Admin getAdmin() {
        return admin;
    }

    public static ResponseTeam getResponseTeam() {
        return responseTeam;
    }
}
