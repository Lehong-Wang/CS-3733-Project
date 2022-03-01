package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

import java.io.IOException;
import java.util.ArrayList;

public class editLabServiceReqFormController {
  @FXML TextField AVRequestIDField;
  @FXML TextField deviceField;
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
  AudioVisualRequestTbl AVReqs = AudioVisualRequestTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  /**
   * edits the form
   *
   * @param avReq request
   * @throws IOException oops
   */
  public void editForm(Request avReq) throws IOException {
    AVRequestIDField.setText(String.valueOf(avReq.getRequestID()));
    subTimeField.setText(String.valueOf(avReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(avReq.getTimeEnd()));

    statusComboBox.setPromptText(avReq.getRequestStatus());
    statusComboBox.setValue(avReq.getRequestStatus());
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");
    statusComboBox.setItems(FXCollections.observableArrayList(statusTypes));

    completerComboBox.setPromptText(String.valueOf(avReq.getEmpCompleter()));
    completerComboBox.setValue(avReq.getEmpCompleter());
    requesterComboBox.setPromptText(String.valueOf(avReq.getEmpInitiated()));
    requesterComboBox.setValue(avReq.getEmpInitiated());
    ArrayList<Integer> employeeIDs = new ArrayList<>();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(e.getEmpID());
    }
    completerComboBox.setItems(FXCollections.observableArrayList(employeeIDs));
    requesterComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    locationComboBox.setPromptText(avReq.getLocationID());
    locationComboBox.setValue(avReq.getLocationID());
    ArrayList<String> locations = new ArrayList<>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    patientComboBox.setPromptText(String.valueOf(avReq.getPatientID()));
    patientComboBox.setValue(avReq.getPatientID());
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
  private boolean avRequestExists() {
    return requests.entryExists(Integer.parseInt(AVRequestIDField.getText()));
  }

  /** edits a requests */
  @FXML
  public void editRequest() {
    try {
      if (avRequestExists()) {
        try {
          String locationID = locationComboBox.getValue();
          Integer pkID = Integer.parseInt(AVRequestIDField.getText());
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
          AVRequestIDField.setText("Invalid input");
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
      AVRequestIDField.setText("Invalid input");
    }
  }

  /** deletes a request */
  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    if (AVRequestIDField.getText() == null
        || deviceField.getText() == null
        || deviceField.getText().isEmpty()
        || AVRequestIDField.getText().isEmpty()) {
      AVRequestIDField.setText("Invalid input");
      deviceField.setText("Invalid input");
      return;
    }
    try {
      pkIDs.add(Integer.parseInt(AVRequestIDField.getText()));
      pkIDs.add(Integer.parseInt(deviceField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      AVRequestIDField.setText("Invalid input");
      deviceField.setText("Invalid input");
    }
    if (AVReqs.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + AVRequestIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        AVReqs.deleteEntry(pkIDs);
        deviceField.setText("Request Deleted!");
        AVRequestIDField.setText("Request Deleted!");
      }
    } else {
      deviceField.setText("Invalid Request!");
      AVRequestIDField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
