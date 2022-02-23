package edu.wpi.GoldenGandaberundas.controllers.EquipmentControllers;

import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editEquipmentReqFormController {

  @FXML TextField equipReqIDField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField patientIDField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML TextField equipmentField;

  RequestTable requests = RequestTable.getInstance();
  MedEquipRequestTbl equipReq = MedEquipRequestTbl.getInstance();

  public void editForm(Request equipRequest) throws IOException {
    equipReqIDField.setText(String.valueOf(equipRequest.getRequestID()));
    locField.setText(equipRequest.getLocationID());
    subTimeField.setText(String.valueOf(equipRequest.getTimeStart()));
    finishTimeField.setText(String.valueOf(equipRequest.getTimeEnd()));
    patientIDField.setText(String.valueOf(equipRequest.getPatientID()));
    requesterIDField.setText(String.valueOf(equipRequest.getEmpInitiated()));
    completerIDField.setText(String.valueOf(equipRequest.getEmpCompleter()));
    statusField.setText(equipRequest.getRequestStatus());
  }

  private boolean equipmentRequestExists() {
    return requests.entryExists(Integer.parseInt(equipReqIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (equipmentRequestExists()) {
        try {
          String locationID = locField.getText();
          Integer pkID = Integer.parseInt(equipReqIDField.getText());
          Integer empInitiated = Integer.parseInt(requesterIDField.getText());
          Integer empCompleter = Integer.parseInt(completerIDField.getText());
          long timeStart = Integer.parseInt(subTimeField.getText());
          long timeEnd = Integer.parseInt(finishTimeField.getText());
          Integer patientID = Integer.parseInt(patientIDField.getText());
          String requestStatus = statusField.getText();

          requests.editEntry(pkID, "locationID", locationID);
          requests.editEntry(pkID, "empInitiated", empInitiated);
          requests.editEntry(pkID, "empCompleter", empCompleter);
          requests.editEntry(pkID, "timeStart", timeStart);
          requests.editEntry(pkID, "timeEnd", timeEnd);
          requests.editEntry(pkID, "patientID", patientID);
          requests.editEntry(pkID, "requestStatus", requestStatus);
          Stage stage = (Stage) locField.getScene().getWindow();
          stage.close();
          // TODO Need refresh table here
        } catch (Exception e) {
          e.printStackTrace();
          locField.setText("Invalid input");
          equipReqIDField.setText("Invalid input");
          requesterIDField.setText("Invalid input");
          completerIDField.setText("Invalid input");
          subTimeField.setText("Invalid input");
          finishTimeField.setText("Invalid input");
          patientIDField.setText("Invalid input");
          statusField.setText("Invalid input");
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
