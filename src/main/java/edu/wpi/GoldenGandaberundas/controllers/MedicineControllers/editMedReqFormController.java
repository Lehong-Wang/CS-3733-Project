package edu.wpi.GoldenGandaberundas.controllers.MedicineControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineTbl;
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

public class editMedReqFormController {

  @FXML TextField medRequestIDField;
  // @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  // @FXML TextField patientIDField;
  // @FXML TextField requesterIDField;
  // @FXML TextField completerIDField;
  // @FXML TextField statusField;
  @FXML TextField medicineField;
  @FXML JFXButton editButton;

  @FXML ComboBox<String> statusComboBox;
  @FXML SearchableComboBox<Integer> completerComboBox;
  @FXML SearchableComboBox<Integer> requesterComboBox;
  @FXML SearchableComboBox<Integer> patientComboBox;
  @FXML SearchableComboBox<String> locationComboBox;

  RequestTable requests = RequestTable.getInstance();
  MedicineRequestTbl medReq = MedicineRequestTbl.getInstance();
  MedicineTbl medicines = MedicineTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  public void editForm(Request medReq) throws IOException {
    medRequestIDField.setText(String.valueOf(medReq.getRequestID()));
    subTimeField.setText(String.valueOf(medReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(medReq.getTimeEnd()));

    statusComboBox.setPromptText(medReq.getRequestStatus());
    statusComboBox.setValue(medReq.getRequestStatus());
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");
    statusComboBox.setItems(FXCollections.observableArrayList(statusTypes));

    completerComboBox.setPromptText(String.valueOf(medReq.getEmpCompleter()));
    completerComboBox.setValue(medReq.getEmpCompleter());
    requesterComboBox.setPromptText(String.valueOf(medReq.getEmpInitiated()));
    requesterComboBox.setValue(medReq.getEmpInitiated());
    ArrayList<Integer> employeeIDs = new ArrayList<>();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(e.getEmpID());
    }
    completerComboBox.setItems(FXCollections.observableArrayList(employeeIDs));
    requesterComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    locationComboBox.setPromptText(medReq.getLocationID());
    locationComboBox.setValue(medReq.getLocationID());
    ArrayList<String> locations = new ArrayList<>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    patientComboBox.setPromptText(String.valueOf(medReq.getPatientID()));
    patientComboBox.setValue(medReq.getPatientID());
    ArrayList<Integer> patientIDs = new ArrayList<>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));
  }

  private boolean medRequestExists() {
    return requests.entryExists(Integer.parseInt(medRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (medRequestExists()) {
        try {
          String locationID = locationComboBox.getValue();
          Integer pkID = Integer.parseInt(medRequestIDField.getText());
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
          Stage stage = (Stage) editButton.getScene().getWindow();
          stage.close();
        } catch (Exception e) {
          locationComboBox.setPromptText(locationComboBox.getValue());
          medRequestIDField.setText("Invalid input");
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
      medRequestIDField.setText("Invalid input");
    }
  }

  @FXML
  public void deleteRequest() {
    if (medRequestIDField.getText() == null
        || medicineField.getText() == null
        || medRequestIDField.getText().isEmpty()
        || medicineField.getText().isEmpty()) {
      medRequestIDField.setText("Invalid input");
      medicineField.setText("Invalid input");
      return;
    }
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    try {
      pkIDs.add(Integer.parseInt(medRequestIDField.getText()));
      pkIDs.add(Integer.parseInt(medicineField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      medRequestIDField.setText("Invalid input");
      medicineField.setText("Invalid input");
    }
    if (medReq.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + medRequestIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        medReq.deleteEntry(pkIDs);
        medicineField.setText("Request Deleted!");
        medRequestIDField.setText("Request Deleted!");
      }
    } else {
      medicineField.setText("Invalid Request!");
      medRequestIDField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
