package edu.wpi.CS3733.c22.teamG.controllers.EquipmentControllers;

import edu.wpi.CS3733.c22.teamG.App;
import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.controllers.validators.AddEquipmentInventoryValidator;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class AddEquipmentInventoryController implements Initializable {
  @FXML private Label errorLabel;
  @FXML private Button submitBtn;
  @FXML private Button clearBtn;
  @FXML private Button backButton;
  @FXML private TextField medID;
  @FXML private MenuButton equipTypeMenu;
  @FXML private TextField currLocate;
  @FXML private MenuButton equipStateMenu;
  @FXML private TextArea addNotes;

  private TableController equipTable;
  private TableController locTable;
  private String equipTypeSelection = null;
  private String statusSelected = null;

  // Method ran when scene is loaded

  // When B button is pressed we go back to Equipment List
  @FXML
  public void goBack(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.setScene(
        new Scene(FXMLLoader.load(App.class.getResource("views/equipmentDelivery.fxml"))));
  }

  // TODO Stub for what happens when submitting the the info into the table
  @FXML
  public void submit(ActionEvent actionEvent) {
    String medID_str = medID.getText();
    String type = equipTypeSelection;
    String status = statusSelected;
    String currLoc = currLocate.getText();

    // Create Validator Object
    AddEquipmentInventoryValidator newEqpInv =
        new AddEquipmentInventoryValidator(medID_str, type, status, currLoc);

    // If each field passes validation, test if entry already exists
    if (newEqpInv.validateTextFields()) {
      errorLabel.setText(medID_str + " " + type + " " + status + " " + currLoc);
      int id = Integer.parseInt(medID.getText());
      // Create MedEquipment Object for Adding to DB
      MedEquipment medEquipment = new MedEquipment(id, type, status, currLoc);
      // Ensure that the entry doesn't already exist
      if (checkExists()) {
        equipTable.addEntry(medEquipment);
      }

    }
    // If fields fail validation, trace the error
    else {
      errorLabel.setText(newEqpInv.traceValidationError());
    }
  }

  // When clear is pressed all textFields are set to empty
  @FXML
  public void clearAll() {
    medID.setText("");
    equipTypeMenu.setText("Select equipment type");
    equipTypeSelection = null;
    currLocate.setText("");
    equipStateMenu.setText("Select equipment status");
    statusSelected = null;
    addNotes.setText("");
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
  }

  @FXML
  public void validateButton() {
    if (medID.getText().isEmpty()
        || statusSelected == null
        || equipTypeSelection == null
        || currLocate.getText().isEmpty()) {
      submitBtn.setDisable(true);
    } else {
      submitBtn.setDisable(false);
    }

    if (medID.getText().isEmpty()
        && equipTypeMenu.getText().isEmpty()
        && equipStateMenu.getText().isEmpty()
        && currLocate.getText().isEmpty()) {
      clearBtn.setDisable(true);
    } else {
      clearBtn.setDisable(false);
    }
  }

  public void typeSelected(ActionEvent actionEvent) {
    equipTypeSelection = ((MenuItem) actionEvent.getSource()).getText();
    equipTypeMenu.setText(equipTypeSelection);
    validateButton();
    actionEvent.consume();
  }

  public void StatusSelected(ActionEvent actionEvent) {
    statusSelected = ((MenuItem) actionEvent.getSource()).getText();
    equipStateMenu.setText(statusSelected);
    validateButton();
    actionEvent.consume();
  }

  /**
   * Ensures that the location we are trying to write exists Ensures the MedID is not already in-use
   */
  private boolean checkExists() {
    try {
      Integer id = Integer.parseInt(medID.getText());
      if (!locTable.entryExists(currLocate.getText())) {
        errorLabel.setText("Location does not exist");
        return false;
      } else {
        if (equipTable.entryExists(id)) {
          errorLabel.setText("Equipment ID Already Exists");
          return false;
        }
        return true;
      }
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    submitBtn.setDisable(true);
    clearBtn.setDisable(true);
    equipTable = MedEquipmentTbl.getInstance();
    locTable = LocationTbl.getInstance();
  }
}
