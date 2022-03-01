package edu.wpi.GoldenGandaberundas.controllers.EquipmentControllers;

import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class editEquipmentReqFormController {

  @FXML TextField equipReqIDField;
  // @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  // @FXML TextField patientIDField;
  // @FXML TextField requesterIDField;
  // @FXML TextField completerIDField;
  // @FXML TextField statusField;
  @FXML TextField equipmentField;

  @FXML ComboBox<String> statusComboBox;
  @FXML SearchableComboBox<Integer> completerComboBox;
  @FXML SearchableComboBox<Integer> requesterComboBox;
  @FXML SearchableComboBox<Integer> patientComboBox;
  @FXML SearchableComboBox<String> locationComboBox;

  RequestTable requests = RequestTable.getInstance();
  MedEquipRequestTbl equipReq = MedEquipRequestTbl.getInstance();

  public void editForm(Request equipRequest) throws IOException {
    equipReqIDField.setText(String.valueOf(equipRequest.getRequestID()));
    subTimeField.setText(String.valueOf(equipRequest.getTimeStart()));
    finishTimeField.setText(String.valueOf(equipRequest.getTimeEnd()));

    statusComboBox.setPromptText(equipRequest.getRequestStatus());
    statusComboBox.setValue(equipRequest.getRequestStatus());
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");
    statusComboBox.setItems(FXCollections.observableArrayList(statusTypes));

    completerComboBox.setPromptText(String.valueOf(equipRequest.getEmpCompleter()));
    completerComboBox.setValue(equipRequest.getEmpCompleter());
    requesterComboBox.setPromptText(String.valueOf(equipRequest.getEmpInitiated()));
    requesterComboBox.setValue(equipRequest.getEmpInitiated());
    ArrayList<Integer> employeeIDs = new ArrayList<>();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(e.getEmpID());
    }
    completerComboBox.setItems(FXCollections.observableArrayList(employeeIDs));
    requesterComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    locationComboBox.setPromptText(equipRequest.getLocationID());
    locationComboBox.setValue(equipRequest.getLocationID());
    ArrayList<String> locations = new ArrayList<>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    patientComboBox.setPromptText(String.valueOf(equipRequest.getPatientID()));
    patientComboBox.setValue(equipRequest.getPatientID());
    ArrayList<Integer> patientIDs = new ArrayList<>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));
  }

  private boolean equipmentRequestExists() {
    return requests.entryExists(Integer.parseInt(equipReqIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (equipmentRequestExists()) {
        try {
          String locationID = locationComboBox.getValue();
          Integer pkID = Integer.parseInt(equipReqIDField.getText());
          Integer empInitiated = requesterComboBox.getValue();
          Integer empCompleter = completerComboBox.getValue();
          long timeStart = Long.parseLong(subTimeField.getText());
          long timeEnd = Long.parseLong(finishTimeField.getText());
          Integer patientID = patientComboBox.getValue();
          String requestStatus = statusComboBox.getValue();

          requests.editEntry(pkID, "locationID", locationID);
          requests.editEntry(pkID, "empInitiated", empInitiated);
          requests.editEntry(pkID, "empCompleter", empCompleter);
          requests.editEntry(pkID, "timeStart", timeStart);
          requests.editEntry(pkID, "timeEnd", timeEnd);
          requests.editEntry(pkID, "patientID", patientID);
          requests.editEntry(pkID, "requestStatus", requestStatus);
          Stage stage = (Stage) subTimeField.getScene().getWindow();
          stage.close();
          // TODO Need refresh table here
        } catch (Exception e) {
          e.printStackTrace();
          locationComboBox.setPromptText(locationComboBox.getValue());
          equipReqIDField.setText("Invalid input");
          requesterComboBox.setPromptText(String.valueOf(requesterComboBox.getValue()));
          completerComboBox.setPromptText(String.valueOf(completerComboBox.getValue()));
          subTimeField.setText("Invalid input");
          finishTimeField.setText("Invalid input");
          patientComboBox.setPromptText(String.valueOf(patientComboBox.getValue()));
          statusComboBox.setPromptText(statusComboBox.getValue());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      equipReqIDField.setText("Invalid input");
    }
  }

  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    if (equipReqIDField.getText() == null
        || equipmentField.getText() == null
        || equipReqIDField.getText().isEmpty()
        || equipmentField.getText().isEmpty()) {
      equipReqIDField.setText("Invalid input");
      equipmentField.setText("Invalid input");
      return;
    }
    try {
      pkIDs.add(Integer.parseInt(equipReqIDField.getText()));
      pkIDs.add(Integer.parseInt(equipmentField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      equipReqIDField.setText("Invalid input");
      equipmentField.setText("Invalid input");
    }
    if (equipReq.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + equipReqIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        equipReq.deleteEntry(pkIDs);
        equipmentField.setText("Request Deleted!");
        equipReqIDField.setText("Request Deleted!");
      }
    } else {
      equipmentField.setText("Invalid Request!");
      equipReqIDField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
