package edu.wpi.CS3733.c22.teamG.controllers.EquipmentControllers;
// Previously EquipmentFormController

import edu.wpi.CS3733.c22.teamG.App;
import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.controllers.validators.AddEquipmentRequestValidator;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.io.IOException;
import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEquipmentRequestController {
  @FXML private Label testLabel;
  @FXML private Button submitBtn;
  @FXML private Button clearBtn;
  @FXML private Button backButton;
  @FXML private TextField personID;
  @FXML private TextField equipmentID;
  @FXML private TextField destination;
  @FXML private TextField reqStatus;
  @FXML private TextArea addNotes;
  private TableController reqTable = MedEquipRequestTbl.getInstance();
  private TableController empTable = MedEquipmentTbl.getInstance();
  private TableController locTable = LocationTbl.getInstance();
  private TableController equipTable = MedEquipmentTbl.getInstance();

  public AddEquipmentRequestController() throws SQLException {}

  // Method ran when scene is loaded
  @FXML
  public void initialize() {
    submitBtn.setDisable(true);
    clearBtn.setDisable(true);
  }

  // When B button is pressed we go back to Equipment List
  @FXML
  public void goBack(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.setScene(
        new Scene(FXMLLoader.load(App.class.getResource("views/equipmentDelivery.fxml"))));
  }

  // TODO Stub for what happens when submitting the the info into the table
  @FXML
  public void submit(ActionEvent actionEvent) throws IOException {
    String empID_str = personID.getText();
    String eqpID_str = equipmentID.getText();
    String dest = destination.getText();
    String status = reqStatus.getText();
    String notes = addNotes.getText();

    // Create new Validation object
    AddEquipmentRequestValidator newEqpReq =
        new AddEquipmentRequestValidator(empID_str, eqpID_str, dest, status, notes);

    // If each field passes validation, perform submit function
    if (newEqpReq.validateTextFields()) {
      testLabel.setText(empID_str + " " + eqpID_str + " " + dest + " " + status + " " + notes);
      int reqIDF = reqTable.readTable().size() + 1;
      int empIDF = Integer.parseInt(personID.getText());
      int medIDF = Integer.parseInt(equipmentID.getText());
      //      MedEquipRequest newRequest = new MedEquipRequest(reqIDF, empIDF, medIDF, dest, notes,
      // status);
      //      if (checkIDExists()) {
      //        reqTable.addEntry(newRequest);
      //        //        clearAll();
      //        submitBtn.setDisable(true);
      //      }
    } else {
      testLabel.setText(newEqpReq.traceValidationError());
    }
  }

  // Checks if there is an employee ID, equipment ID and destination ID in the databases
  private boolean checkIDExists() {
    boolean allExist = false;
    // Checks employee ID exists
    if (!empTable.entryExists(Integer.parseInt(personID.getText()))) {
      testLabel.setText("Employee ID Doesn't Exist");
      allExist = false;
    }
    // Checks equipment ID exists
    else if (!equipTable.entryExists(Integer.parseInt(equipmentID.getText()))) {
      testLabel.setText("Equipment ID Doesn't Exist");
      allExist = false;
    }
    // Checks if Destination ID Exists
    else if (!locTable.entryExists(destination.getText())) {
      testLabel.setText("Destination ID Doesn't Exist");
      allExist = false;
    } else {
      allExist = true;
    }
    return allExist;
  }

  // When clear is pressed all textFields are set to empty
  @FXML
  public void clearAll() {
    personID.setText("");
    equipmentID.setText("");
    destination.setText("");
    reqStatus.setText("");
    addNotes.setText("");
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
  }

  @FXML
  public void validateButton() {
    // if all the texts have field in them then set the sumbit button to true
    if (personID.getText().isEmpty()
        || equipmentID.getText().isEmpty()
        || destination.getText().isEmpty()
        || reqStatus.getText().isEmpty()) {
      submitBtn.setDisable(true);
    } else {
      submitBtn.setDisable(false);
    }

    if (personID.getText().isEmpty()
        && equipmentID.getText().isEmpty()
        && destination.getText().isEmpty()
        && reqStatus.getText().isEmpty()) {
      clearBtn.setDisable(true);
    } else {
      clearBtn.setDisable(false);
    }
  }
}
