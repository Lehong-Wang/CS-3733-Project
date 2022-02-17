package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class dummyController {
  @FXML Button homeBtn;
  @FXML JFXButton secretBtn;
  @FXML ImageView secretImg;

  public void goHome(ActionEvent actionEvent) throws IOException {
    // gets the current stage that the button exists on
    Stage stage = (Stage) homeBtn.getScene().getWindow();

    // sets the current scene back to the home screen
    // !!!!!!!!!! NOTE: you can only access FXML files stored in a directory with the same name as
    // the package!!!!!!!!!
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  public void secretAction(ActionEvent actionEvent) {
    secretImg.setVisible(true);
  }
}
