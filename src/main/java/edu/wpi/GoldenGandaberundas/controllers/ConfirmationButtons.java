package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class ConfirmationButtons {
  @FXML private JFXButton noBtn;
  @FXML private JFXButton yesBtn;
  @FXML Text questionTitle;
  @FXML Text questionText;
  @FXML ImageView icon;

  private Boolean selection = null;

  public void initialize() {
    noBtn.setOnMouseReleased(
        e -> {
          selection = false;
        });
    yesBtn.setOnMouseReleased(
        e -> {
          selection = true;
        });
  }

  public Boolean getChoice() {
    return selection;
  }
}
