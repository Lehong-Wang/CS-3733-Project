package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import javafx.fxml.FXML;

public class SettingsPageController {
  @FXML JFXButton NormalModeBtn;
  @FXML JFXButton DarkModeButton;
  @FXML JFXButton WatermelonBtn;
  @FXML JFXButton developerModeBtn;

  public void switchNormal() {
    developerModeBtn.getScene().getStylesheets().clear();
    developerModeBtn
        .getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/NormalMode.css").toExternalForm());
  }

  public void switchDark() {
    developerModeBtn.getScene().getStylesheets().clear();
    developerModeBtn
        .getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/DarkMode.css").toExternalForm());
  }

  public void switchWatermelon() {
    developerModeBtn.getScene().getStylesheets().clear();
    developerModeBtn
        .getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/WaterMelon.css").toExternalForm());
  }

  public void switchDeveloper() {
    developerModeBtn.getScene().getStylesheets().clear();
    developerModeBtn
        .getScene()
        .getStylesheets()
        .add(App.class.getResource("styleSheets/OriginalMode.css").toExternalForm());
  }
}
