package edu.wpi.GoldenGandaberundas.controllers.GiftFloralControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

  RequestTable requests = RequestTable.getInstance();
  GiftRequestTbl giftReqs = GiftRequestTbl.getInstance();
  GiftTbl gifts = GiftTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  public void editForm(Request giftReq) throws IOException {
    giftRequestIDField.setText(String.valueOf(giftReq.getRequestID()));
    locField.setText(giftReq.getLocationID());
    subTimeField.setText(String.valueOf(giftReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(giftReq.getTimeEnd()));
    patientIDField.setText(String.valueOf(giftReq.getPatientID()));
    requesterIDField.setText(String.valueOf(giftReq.getEmpInitiated()));
    completerIDField.setText(String.valueOf(giftReq.getEmpCompleter()));
    statusField.setText(giftReq.getRequestStatus());
  }

  private boolean giftRequestExists() {
    return requests.entryExists(Integer.parseInt(giftRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (giftRequestExists()) {
        try {
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
          Stage stage = (Stage) editButton.getScene().getWindow();
          stage.close();
        } catch (Exception e) {
          e.printStackTrace();
          locField.setText("Invalid input");
          giftRequestIDField.setText("Invalid input");
          requesterIDField.setText("Invalid input");
          completerIDField.setText("Invalid input");
          subTimeField.setText("Invalid input");
          finishTimeField.setText("Invalid input");
          patientIDField.setText("Invalid input");
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
