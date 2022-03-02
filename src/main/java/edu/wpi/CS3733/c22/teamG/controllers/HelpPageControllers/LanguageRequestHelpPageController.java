package edu.wpi.CS3733.c22.teamG.controllers.HelpPageControllers;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

public class LanguageRequestHelpPageController {
  @FXML Button homeBtn;
  @FXML JFXButton secretBtn;
  @FXML ImageView secretImg;

  public void secretAction(ActionEvent actionEvent) {
    secretImg.setVisible(true);
  }
}
