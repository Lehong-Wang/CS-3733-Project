package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import javafx.fxml.FXML;

public class SettingsPageController {
  @FXML JFXButton NormalModeBtn;
  @FXML JFXButton DarkModeButton;
  @FXML JFXButton WatermelonBtn;
  @FXML JFXButton DeveloperModeBtn;
  @FXML JFXButton WPIModeBtn;

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
}
