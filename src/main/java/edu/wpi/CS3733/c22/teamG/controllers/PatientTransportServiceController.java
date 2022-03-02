package edu.wpi.CS3733.c22.teamG.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class PatientTransportServiceController implements Initializable {
  @FXML Button homeBtn; // home btn with icon and text
  @FXML Button homeIconBtn; // home btn with just an icon
  @FXML JFXHamburger hamburger; // hamburger menu btn
  @FXML VBox sideMenuMax; // container for the menu with icon and text
  @FXML VBox sideMenuMin; // container for the menu with just icons
  @FXML VBox sideMenuCont; // container for the entire set of side menus\\

  @FXML TextField nameField;
  @FXML TextField toField;
  @FXML TextField fromField;

  @FXML JFXDrawer drawer; // sliding side menu
  @FXML JFXButton currTransportsBtn; // Cool Sliding Button
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML TableView transportsTbl; // Service Table

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    drawer.setSidePane(drawerBox);
    drawer.close();
    drawer.setMiniDrawerSize(36);
    // drawerBox.setStyle("-fx-background-image: url(Brigham-Womens.jpg)");
    // Any button Added to the slide panel must have its style set using this function
    buttonStyle(currTransportsBtn);

    // Setting buttons to empty for closed slider, could also delete their names in SceneBuilder but
    // for now this is fine.
    currTransportsBtn.setText("");
  }

  public void buttonStyle(JFXButton buttonO) {
    buttonO.setStyle(IDLE_BUTTON_STYLE);
    buttonO.setOnMouseEntered(
        e -> {
          buttonO.setStyle(HOVERED_BUTTON_STYLE);
        });
    buttonO.setOnMouseExited(
        e -> {
          buttonO.setStyle(IDLE_BUTTON_STYLE);
        });
  }

  // Method for opening slider when mouse over. TODO Button text must be repopulated in here
  public void slideOpen() {
    if (drawer.isClosed()) {
      drawer.open();
      currTransportsBtn.setText("  Current Transports");
    }
  }

  // Method for closing slider when mouse leaves. TODO Button text must be set to empty in here
  public void slideClose() {
    if (drawer.isOpened()) {
      drawer.close();
      currTransportsBtn.setText("");
    }
  }
}
