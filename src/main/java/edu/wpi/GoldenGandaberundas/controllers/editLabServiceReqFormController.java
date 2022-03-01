package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
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

public class editLabServiceReqFormController {
  @FXML TextField LabServiceRequestIDField;
  @FXML TextField labServiceIDField;
  // @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  // @FXML TextField patientIDField;
  // @FXML TextField requesterIDField;
  // @FXML TextField completerIDField;
  // @FXML TextField statusField;
  @FXML JFXButton editButton;

  @FXML ComboBox<String> statusComboBox;
  @FXML SearchableComboBox<Integer> completerComboBox;
  @FXML SearchableComboBox<Integer> requesterComboBox;
  @FXML SearchableComboBox<Integer> patientComboBox;
  @FXML SearchableComboBox<String> locationComboBox;

  RequestTable requests = RequestTable.getInstance();
  LabServiceRequestTbl LSReqs = LabServiceRequestTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  /**
   * edits the form
   *
   * @param lsReq request
   * @throws IOException oops
   */
  public void editForm(Request lsReq) throws IOException {
    LabServiceRequestIDField.setText(String.valueOf(lsReq.getRequestID()));
    subTimeField.setText(String.valueOf(lsReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(lsReq.getTimeEnd()));

    statusComboBox.setPromptText(lsReq.getRequestStatus());
    statusComboBox.setValue(lsReq.getRequestStatus());
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");
    statusComboBox.setItems(FXCollections.observableArrayList(statusTypes));

    completerComboBox.setPromptText(String.valueOf(lsReq.getEmpCompleter()));
    completerComboBox.setValue(lsReq.getEmpCompleter());
    requesterComboBox.setPromptText(String.valueOf(lsReq.getEmpInitiated()));
    requesterComboBox.setValue(lsReq.getEmpInitiated());
    ArrayList<Integer> employeeIDs = new ArrayList<>();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(e.getEmpID());
    }
    completerComboBox.setItems(FXCollections.observableArrayList(employeeIDs));
    requesterComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    locationComboBox.setPromptText(lsReq.getLocationID());
    locationComboBox.setValue(lsReq.getLocationID());
    ArrayList<String> locations = new ArrayList<>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    patientComboBox.setPromptText(String.valueOf(lsReq.getPatientID()));
    patientComboBox.setValue(lsReq.getPatientID());
    ArrayList<Integer> patientIDs = new ArrayList<>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));
  }

  /**
   * checks is a med request exists
   *
   * @return true if a request exists
   */
  private boolean lsRequestExists() {
    return requests.entryExists(Integer.parseInt(LabServiceRequestIDField.getText()));
  }

  /** edits a requests */
  @FXML
  public void editRequest() {
    try {
      if (lsRequestExists()) {
        try {
          String locationID = locationComboBox.getValue();
          Integer pkID = Integer.parseInt(LabServiceRequestIDField.getText());
          Integer empInitiated = requesterComboBox.getValue();
          Integer empCompleter = completerComboBox.getValue();
          long timeStart = Integer.parseInt(subTimeField.getText());
          long timeEnd = Integer.parseInt(finishTimeField.getText());
          Integer patientID = patientComboBox.getValue();
          String requestStatus = statusComboBox.getValue();

          requests.editEntry(pkID, "locationID", locationID);
          requests.editEntry(pkID, "empInitiated", empInitiated);
          requests.editEntry(pkID, "empCompleter", empCompleter);
          requests.editEntry(pkID, "timeStart", timeStart);
          requests.editEntry(pkID, "timeEnd", timeEnd);
          requests.editEntry(pkID, "patientID", patientID);
          requests.editEntry(pkID, "requestStatus", requestStatus);
          // requests.editEntry(pkID, "notes", "");
          Stage stage = (Stage) editButton.getScene().getWindow();
          stage.close();
          // TODO Need refresh table here
        } catch (Exception e) {
          e.printStackTrace();
          locationComboBox.setPromptText(locationComboBox.getValue());
          LabServiceRequestIDField.setText("Invalid input");
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
      LabServiceRequestIDField.setText("Invalid input");
    }
  }

  /** deletes a request */
  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    if (LabServiceRequestIDField.getText() == null
        || labServiceIDField.getText() == null
        || labServiceIDField.getText().isEmpty()
        || LabServiceRequestIDField.getText().isEmpty()) {
      LabServiceRequestIDField.setText("Invalid input");
      labServiceIDField.setText("Invalid input");
      return;
    }
    try {
      pkIDs.add(Integer.parseInt(LabServiceRequestIDField.getText()));
      pkIDs.add(Integer.parseInt(labServiceIDField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      LabServiceRequestIDField.setText("Invalid input");
      labServiceIDField.setText("Invalid input");
    }
    if (LSReqs.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + LabServiceRequestIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        LSReqs.deleteEntry(pkIDs);
        labServiceIDField.setText("Request Deleted!");
        LabServiceRequestIDField.setText("Request Deleted!");
      }
    } else {
      labServiceIDField.setText("Invalid Request!");
      LabServiceRequestIDField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
