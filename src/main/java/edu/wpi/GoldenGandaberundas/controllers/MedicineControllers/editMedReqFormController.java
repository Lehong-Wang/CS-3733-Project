package edu.wpi.GoldenGandaberundas.controllers.MedicineControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editMedReqFormController {

  @FXML TextField medRequestIDField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField patientIDField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML TextField medicineField;
  @FXML JFXButton editButton;

  @FXML JFXButton locBtn;
  @FXML JFXButton subBtn;
  @FXML JFXButton finishBtn;
  @FXML JFXButton requestBtn;
  @FXML JFXButton completeBtn;
  @FXML JFXButton statusBtn;

  RequestTable requests = RequestTable.getInstance();
  MedicineRequestTbl medReq = MedicineRequestTbl.getInstance();
  MedicineTbl medicines = MedicineTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  public void editForm(Request medReq) throws IOException {
    medRequestIDField.setText(String.valueOf(medReq.getRequestID()));
    locField.setText(medReq.getLocationID());
    subTimeField.setText(String.valueOf(medReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(medReq.getTimeEnd()));
    patientIDField.setText(String.valueOf(medReq.getPatientID()));
    requesterIDField.setText(String.valueOf(medReq.getEmpInitiated()));
    completerIDField.setText(String.valueOf(medReq.getEmpCompleter()));
    statusField.setText(medReq.getRequestStatus());
  }

  private boolean medRequestExists() {
    return requests.entryExists(Integer.parseInt(medRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    if (medRequestExists()) {
      String locationID = locField.getText();
      Integer pkID = Integer.parseInt(medRequestIDField.getText());
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
    Stage stage = (Stage) editButton.getScene().getWindow();
    stage.close();
  }

  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    pkIDs.add(Integer.parseInt(medRequestIDField.getText()));
    pkIDs.add(Integer.parseInt(medicineField.getText()));
    if (medReq.entryExists(pkIDs)) {
      medReq.deleteEntry(pkIDs);
      medicineField.setText("Request Deleted!");
      medRequestIDField.setText("Request Deleted!");
    } else {
      medicineField.setText("Invalid Request!");
      medRequestIDField.setText("Invalid Request!");
    }
  }
}
