package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Credential;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Locale;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class loginScreenController {
  @FXML AnchorPane anchor;
  @FXML TextField userField;
  @FXML TextField passField;
  @FXML JFXButton loginBtn;
  @FXML ImageView logoImg;
  @FXML Text userText;
  @FXML Text passText;

  private FadeTransition fadeIn = new FadeTransition(Duration.millis(1000), userField);

  private TableController empTable = EmployeeTbl.getInstance();
  private TableController credTable = CredentialsTbl.getInstance();

  public loginScreenController() throws SQLException {}

  /** Initialize method to allow for the enter button to work to log in */
  @FXML
  public void initialize() {
    passField.setOnKeyPressed(
        event -> {
          if (event.getCode() == KeyCode.ENTER) {
            try {
              login();
            } catch (IOException e) {
              e.printStackTrace();
            }
          }
        });
  }

  @FXML
  public void login() throws IOException {

    // logoImg.getParent().toFront();
    System.out.println("LOGGING IN");
    Integer userID = null;
    try {
      userID = Integer.parseInt(userField.getText());
    } catch (NumberFormatException n) {
      if (!userField.getText().trim().toLowerCase(Locale.ROOT).equals("admin")) {
        loginBtn.setText("Please Enter Valid ID");
      } else {
        userID = 000;
      }
    }
    String password = passField.getText();
    if (credTable.entryExists(userID)) {
      if (((Credential) credTable.getEntry(userID)).checkPassword(password)) {
        CurrentUser.setUser(EmployeeTbl.getInstance().getEntry(userID));
        // runs the welcome method
        helloUser();
        userText.setText("Welcome");
        ScaleTransition userScale = new ScaleTransition(Duration.millis(500), userText);
        userScale.setToX(2);
        userScale.setToY(2);
        userScale.play();
        //        userText.setFont(Font.font(64));
        passText.setFont(Font.font(30));
        passText.setText(
            EmployeeTbl.getInstance().getEntry(userID).getFName()
                + " "
                + EmployeeTbl.getInstance().getEntry(userID).getLName());
        // moves the username text
        TranslateTransition moveUser = new TranslateTransition();
        moveUser.setToY(35);
        moveUser.setDuration(Duration.millis(1000));
        moveUser.setNode(userText);
        moveUser.play();

        TranslateTransition moveLogo = new TranslateTransition();
        moveLogo.setToX(loginBtn.localToScene(loginBtn.getBoundsInLocal()).getCenterX());
        moveLogo.setByZ(2);
        moveLogo.setDuration(Duration.millis(1500));
        moveLogo.setNode(logoImg);
        moveLogo.play();

        moveLogo.setOnFinished(
            event -> {
              Stage stage = (Stage) loginBtn.getScene().getWindow();
              try {
                stage.setScene(
                    new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));

                // Jank resizing fix but it works, definitely a better way to do it but none will
                // take
                // less than 10 minutes to do.
                // stage.setMaximized(true);

                stage.setMaximized(false);
                stage.setMaximized(true);
              } catch (IOException e) {
                e.printStackTrace();
              }
            });

      } else {

        loginBtn.setText("Invalid Login");
        tellUserTheyreWrongGraphically();
      }
    } else {
      loginBtn.setText("Invalid Login");
      tellUserTheyreWrongGraphically();
    }
  }

  private void tellUserTheyreWrongGraphically() {
    RotateTransition rotateLogoRight = new RotateTransition();
    rotateLogoRight.setToAngle(20);
    rotateLogoRight.setDuration(Duration.millis(68));
    rotateLogoRight.setNode(logoImg);

    RotateTransition rotateLogoLeft = new RotateTransition();
    rotateLogoLeft.setToAngle(-20);
    rotateLogoLeft.setDuration(Duration.millis(68));
    rotateLogoLeft.setNode(logoImg);

    RotateTransition rotateLogoCenter = new RotateTransition();
    rotateLogoCenter.setToAngle(0);
    rotateLogoCenter.setDuration(Duration.millis(68));
    rotateLogoCenter.setNode(logoImg);

    SequentialTransition sequentialTransition =
        new SequentialTransition(rotateLogoRight, rotateLogoLeft, rotateLogoCenter);
    sequentialTransition.setCycleCount(3);
    sequentialTransition.play();
  }

  /** animates for the welcome screen */
  public void helloUser() {
    FadeTransition fadeUserField = new FadeTransition(Duration.millis(500), userField);
    fadeUserField.setFromValue(1.0);
    fadeUserField.setToValue(0.0);
    fadeUserField.play();

    FadeTransition fadePassField = new FadeTransition(Duration.millis(500), passField);
    fadePassField.setFromValue(1.0);
    fadePassField.setToValue(0.0);
    fadePassField.play();

    FadeTransition fadeLogin = new FadeTransition(Duration.millis(500), loginBtn);
    fadeLogin.setFromValue(1.0);
    fadeLogin.setToValue(0.0);
    fadeLogin.play();
  }
}
