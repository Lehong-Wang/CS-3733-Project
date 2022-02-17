package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.App;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class foodDeliveryController {

  @FXML JFXButton homeBtn;
  @FXML ChoiceBox restaurantCB;
  @FXML Label menuLabel;

  @FXML TextField timeField;
  @FXML TextField orderField;
  @FXML TextField costField;

  @FXML JFXButton currentOrdersBtn; // Cool Sliding Button
  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML TableView ordersTbl; // Service Table

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  @FXML
  public void initialize() {
    drawer.setSidePane(drawerBox);
    drawer.close();
    drawer.setMiniDrawerSize(36);
    // drawerBox.setStyle("-fx-background-image: url(Brigham-Womens.jpg)");
    // Any button Added to the slide panel must have its style set using this function
    buttonStyle(currentOrdersBtn);

    // Setting buttons to empty for closed slider, could also delete their names in SceneBuilder but
    // for now this is fine.
    currentOrdersBtn.setText("");
  }

  public void goHome(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) currentOrdersBtn.getScene().getWindow();

    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  // Method to set buttons style, used in initialize method with slide panel buttons as params
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
      currentOrdersBtn.setText("  Current Orders");
    }
  }

  // Method for closing slider when mouse leaves. TODO Button text must be set to empty in here
  public void slideClose() {
    if (drawer.isOpened()) {
      drawer.close();
      currentOrdersBtn.setText("");
    }
  }
}
