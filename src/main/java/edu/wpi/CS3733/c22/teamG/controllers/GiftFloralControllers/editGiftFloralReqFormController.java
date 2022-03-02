package edu.wpi.CS3733.c22.teamG.controllers.GiftFloralControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Employee;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.Location;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Patients.Patient;
import edu.wpi.CS3733.c22.teamG.tableControllers.Patients.PatientTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
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

public class editGiftFloralReqFormController {

  @FXML TextField giftRequestIDField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField giftField;
  @FXML JFXButton editButton;

  @FXML ComboBox<String> statusComboBox;
  @FXML SearchableComboBox<Integer> completerComboBox;
  @FXML SearchableComboBox<Integer> requesterComboBox;
  @FXML SearchableComboBox<Integer> patientComboBox;
  @FXML SearchableComboBox<String> locationComboBox;

  RequestTable requests = RequestTable.getInstance();
  GiftRequestTbl giftReqs = GiftRequestTbl.getInstance();
  GiftTbl gifts = GiftTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  public void editForm(Request giftReq) throws IOException {
    giftRequestIDField.setText(String.valueOf(giftReq.getRequestID()));
    subTimeField.setText(String.valueOf(giftReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(giftReq.getTimeEnd()));

    statusComboBox.setPromptText(giftReq.getRequestStatus());
    statusComboBox.setValue(giftReq.getRequestStatus());
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");
    statusComboBox.setItems(FXCollections.observableArrayList(statusTypes));

    completerComboBox.setPromptText(String.valueOf(giftReq.getEmpCompleter()));
    completerComboBox.setValue(giftReq.getEmpCompleter());
    requesterComboBox.setPromptText(String.valueOf(giftReq.getEmpInitiated()));
    requesterComboBox.setValue(giftReq.getEmpInitiated());
    ArrayList<Integer> employeeIDs = new ArrayList<>();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(e.getEmpID());
    }
    completerComboBox.setItems(FXCollections.observableArrayList(employeeIDs));
    requesterComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    locationComboBox.setPromptText(giftReq.getLocationID());
    locationComboBox.setValue(giftReq.getLocationID());
    ArrayList<String> locations = new ArrayList<>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    patientComboBox.setPromptText(String.valueOf(giftReq.getPatientID()));
    patientComboBox.setValue(giftReq.getPatientID());
    ArrayList<Integer> patientIDs = new ArrayList<>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));
  }

  private boolean giftRequestExists() {
    return requests.entryExists(Integer.parseInt(giftRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (giftRequestExists()) {
        try {
          String locationID = locationComboBox.getValue();
          Integer pkID = Integer.parseInt(giftRequestIDField.getText());
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
          e.printStackTrace();
          locationComboBox.setPromptText(locationComboBox.getValue());
          giftRequestIDField.setText("Invalid input");
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
      giftRequestIDField.setText("Invalid input");
    }
  }

  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    if (giftRequestIDField.getText() == null
        || giftField.getText() == null
        || giftRequestIDField.getText().isEmpty()
        || giftField.getText().isEmpty()) {
      giftRequestIDField.setText("Invalid input");
      giftField.setText("Invalid input");
      return;
    }
    try {
      pkIDs.add(Integer.parseInt(giftRequestIDField.getText()));
      pkIDs.add(Integer.parseInt(giftField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      giftRequestIDField.setText("Invalid input");
      giftField.setText("Invalid input");
    }
    if (giftReqs.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + giftRequestIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        giftReqs.deleteEntry(pkIDs);
        giftField.setText("Request Deleted!");
        giftRequestIDField.setText("Request Deleted!");
      }
    } else {
      giftField.setText("Invalid Request!");
      giftRequestIDField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
