package edu.wpi.GoldenGandaberundas.controllers.EquipmentControllers;

import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

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

  private boolean medRequestExists() {
    return requests.entryExists(Integer.parseInt(equipReqIDField.getText()));
  }

  @FXML
  public void editRequest() {
    if (medRequestExists()) {
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
    }
  }

  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    pkIDs.add(Integer.parseInt(equipReqIDField.getText()));
    pkIDs.add(Integer.parseInt(equipmentField.getText()));
    if (equipReq.entryExists(pkIDs)) {
      equipReq.deleteEntry(pkIDs);
      equipmentField.setText("Request Deleted!");
      equipReqIDField.setText("Request Deleted!");
    } else {
      equipmentField.setText("Invalid Request!");
      equipReqIDField.setText("Invalid Request!");
    }
  }
}
