package edu.wpi.CS3733.c22.teamG.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.CS3733.c22.teamG.App;
import edu.wpi.CS3733.c22.teamG.CurrentUser;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionType;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeePermissionTbl;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class SettingsPageController {
  @FXML JFXButton NormalModeBtn;
  @FXML JFXButton DarkModeButton;
  @FXML JFXButton WatermelonBtn;
  @FXML JFXButton DeveloperModeBtn;
  @FXML JFXButton WPIModeBtn;

  @FXML private Label SwitchDBLabel;
  @FXML private JFXButton ClientServerButton;
  @FXML private JFXButton EmbeddedButton;
  @FXML private JFXButton CloudButton;

  public void initialize() {
    checkPerms();
  }

  public void checkPerms() {
    int currID = CurrentUser.getUser().getEmpID();
    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
    boolean hideShit = true;
    for (int i = 0; i < perms.size(); i++) {
      if (perms.get(i) == 111) {
        hideShit = false;
        break;
      }
    }
    if (hideShit == true) {
      hideAdmin();
    }
  }

  public void hideAdmin() {
    SwitchDBLabel.setManaged(false);
    SwitchDBLabel.setVisible(false);
    ClientServerButton.setManaged(false);
    ClientServerButton.setVisible(false);
    EmbeddedButton.setManaged(false);
    EmbeddedButton.setVisible(false);
    CloudButton.setManaged(false);
    CloudButton.setVisible(false);
  }

  public void switchNormal() {
    NormalModeBtn.getScene().getStylesheets().clear();
    NormalModeBtn.getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/NormalMode.css").toExternalForm());
  }

  public void switchDark() {
    DarkModeButton.getScene().getStylesheets().clear();
    DarkModeButton.getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/DarkMode.css").toExternalForm());
  }

  public void switchWatermelon() {
    WatermelonBtn.getScene().getStylesheets().clear();
    WatermelonBtn.getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/WaterMelon.css").toExternalForm());
  }

  public void switchDeveloper() {
    DeveloperModeBtn.getScene().getStylesheets().clear();
    DeveloperModeBtn.getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/OriginalMode.css").toExternalForm());
  }

  public void switchWPI() {
    WPIModeBtn.getScene().getStylesheets().clear();
    WPIModeBtn.getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/WPIMode.css").toExternalForm());
  }

  public void switchBearBone() {
    WPIModeBtn.getScene().getStylesheets().clear();
    WPIModeBtn.getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/BearBone.css").toExternalForm());
  }

  public void enableClientServer(ActionEvent actionEvent) {
    ConnectionHandler.getInstance().setConnection(ConnectionType.clientServer);
  }

  public void enableEmbedded(ActionEvent actionEvent) {
    ConnectionHandler.getInstance().setConnection(ConnectionType.embedded);
  }

  public void enableCloud(ActionEvent actionEvent) {
    ConnectionHandler.getInstance().setConnection(ConnectionType.cloud);
  }
}
