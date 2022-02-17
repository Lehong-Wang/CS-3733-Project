package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.controllers.validators.AddLocationValidator;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddLocationController implements Initializable {

  @FXML TextField nodeIdField;
  @FXML TextField xField;
  @FXML TextField yField;
  @FXML TextField floorField;
  @FXML TextField buildingField;
  @FXML TextField nodeTypeField;
  @FXML TextField longNameField;
  @FXML TextField shortNameField;

  @FXML JFXButton homeBtn;
  @FXML JFXButton submitBtn;
  @FXML JFXButton clearBtn;
  @FXML private Label errorLabel;
  private TableController tbCont = null;

  @FXML
  public void goHome(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) homeBtn.getScene().getWindow();
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    submitBtn.setDisable(true);
    tbCont = LocationTbl.getInstance();
  }

  @FXML
  public void submit() {
    String nodeId = nodeIdField.getText();
    String y_str = yField.getText();
    String x_str = xField.getText();
    String floor = floorField.getText();
    String building = buildingField.getText();
    String nodeType = nodeTypeField.getText();
    String longName = longNameField.getText();
    String shortName = shortNameField.getText();

    // Create a new AddLocationVlaidator using Strings from TextFields
    AddLocationValidator newLoc =
        new AddLocationValidator(
            nodeId, x_str, y_str, floor, building, nodeType, longName, shortName);

    // If each field passes validation, perform submit function
    if (newLoc.validateTextFields()) {
      errorLabel.setText(nodeId + " " + x_str + " " + y_str + " " + floor + " " + building);
      int y = Integer.parseInt(yField.getText());
      int x = Integer.parseInt(xField.getText());
      Location curLoc = new Location(nodeId, x, y, floor, building, nodeType, longName, shortName);
      if (!checkLocExists()) {
        tbCont.addEntry(curLoc);
      }

    }
    // If a field fails validation, run the traceError method and set the label to the returned
    // error msg
    else {
      errorLabel.setText(newLoc.traceValidationError());
    }
  }

  @FXML
  public void clearFields() {
    nodeIdField.setText("");
    yField.setText("");
    xField.setText("");
    floorField.setText("");
    buildingField.setText("");
    nodeTypeField.setText("");
    longNameField.setText("");
    shortNameField.setText("");
    errorLabel.setText("");
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
  }

  @FXML
  private void validateButton() {
    if (nodeIdField.getText().isEmpty()
        || yField.getText().isEmpty()
        || xField.getText().isEmpty()
        || floorField.getText().isEmpty()
        || buildingField.getText().isEmpty()
        || nodeTypeField.getText().isEmpty()
        || longNameField.getText().isEmpty()
        || shortNameField.getText().isEmpty()) {
      submitBtn.setDisable(true);
    } else {
      submitBtn.setDisable(false);
    }

    if (nodeIdField.getText().isEmpty()
        && yField.getText().isEmpty()
        && xField.getText().isEmpty()
        && floorField.getText().isEmpty()
        && buildingField.getText().isEmpty()
        && nodeTypeField.getText().isEmpty()
        && longNameField.getText().isEmpty()
        && shortNameField.getText().isEmpty()) {
      clearBtn.setDisable(true);
    } else {
      clearBtn.setDisable(false);
    }
  }

  /** Ensures that the location we are trying to write doesn't already exist */
  private boolean checkLocExists() {
    try {
      if (tbCont.entryExists(nodeIdField.getText().trim().toUpperCase())) {
        errorLabel.setText("NodeID Already Exists");
        return true;
      } else {
        return false;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return true;
    }
  }
}
