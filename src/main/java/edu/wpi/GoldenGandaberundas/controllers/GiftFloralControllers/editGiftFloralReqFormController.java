package edu.wpi.GoldenGandaberundas.controllers.GiftFloralControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editGiftFloralReqFormController {

  @FXML TextField giftRequestIDField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField patientIDField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML TextField giftField;
  @FXML JFXButton editButton;

  @FXML JFXButton locBtn;
  @FXML JFXButton subBtn;
  @FXML JFXButton finishBtn;
  @FXML JFXButton requestBtn;
  @FXML JFXButton completeBtn;
  @FXML JFXButton statusBtn;

  RequestTable requests = RequestTable.getInstance();
  GiftRequestTbl giftReqs = GiftRequestTbl.getInstance();
  GiftTbl gifts = GiftTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  public void editForm(Request medReq) throws IOException {
    giftRequestIDField.setText(String.valueOf(medReq.getRequestID()));
    locField.setText(medReq.getLocationID());
    subTimeField.setText(String.valueOf(medReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(medReq.getTimeEnd()));
    patientIDField.setText(String.valueOf(medReq.getPatientID()));
    requesterIDField.setText(String.valueOf(medReq.getEmpInitiated()));
    completerIDField.setText(String.valueOf(medReq.getEmpCompleter()));
    statusField.setText(medReq.getRequestStatus());
  }

  private boolean medRequestExists() {
    return requests.entryExists(Integer.parseInt(giftRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    if (medRequestExists()) {
      String locationID = locField.getText();
      Integer pkID = Integer.parseInt(giftRequestIDField.getText());
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
    pkIDs.add(Integer.parseInt(giftRequestIDField.getText()));
    pkIDs.add(Integer.parseInt(giftField.getText()));
    if (giftReqs.entryExists(pkIDs)) {
      giftReqs.deleteEntry(pkIDs);
      giftField.setText("Request Deleted!");
      giftRequestIDField.setText("Request Deleted!");
    } else {
      giftField.setText("Invalid Request!");
      giftRequestIDField.setText("Invalid Request!");
    }
  }
}
