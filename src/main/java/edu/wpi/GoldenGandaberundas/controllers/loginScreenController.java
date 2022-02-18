package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Credential;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class loginScreenController {
  @FXML AnchorPane anchor;
  @FXML TextField userField;
  @FXML TextField passField;
  @FXML JFXButton loginBtn;
  private TableController empTable = EmployeeTbl.getInstance();
  private TableController credTable = CredentialsTbl.getInstance();

  public loginScreenController() throws SQLException {}

  @FXML
  public void login(ActionEvent actionEvent) throws IOException {

    Integer userID = Integer.parseInt(userField.getText());
    String password = passField.getText();
    if (credTable.entryExists(userID)) {
      if (((Credential) credTable.getEntry(userID)).checkPassword(password)) {
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
      } else {
        loginBtn.setText("Try Again!");
      }
      loginBtn.setText("Try Again!");
    }
  }
}
