package edu.wpi.GoldenGandaberundas.controllers.LaundryControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editLaundryReqFormController {
  @FXML TextField laundryRequestIDField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField patientIDField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML TextField laundryField;
  @FXML JFXButton editButton;

  @FXML JFXButton locBtn;
  @FXML JFXButton subBtn;
  @FXML JFXButton finishBtn;
  @FXML JFXButton requestBtn;
  @FXML JFXButton completeBtn;
  @FXML JFXButton statusBtn;

  RequestTable requests = RequestTable.getInstance();
  LaundryRequestTbl laundryReqs = LaundryRequestTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  public void editForm(Request medReq) throws IOException {
    laundryRequestIDField.setText(String.valueOf(medReq.getRequestID()));
    locField.setText(medReq.getLocationID());
    subTimeField.setText(String.valueOf(medReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(medReq.getTimeEnd()));
    patientIDField.setText(String.valueOf(medReq.getPatientID()));
    requesterIDField.setText(String.valueOf(medReq.getEmpInitiated()));
    completerIDField.setText(String.valueOf(medReq.getEmpCompleter()));
    statusField.setText(medReq.getRequestStatus());
  }

  private boolean medRequestExists() {

    return requests.entryExists(Integer.parseInt(laundryRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (medRequestExists()) {
        try {
          String locationID = locField.getText();
          Integer pkID = Integer.parseInt(laundryRequestIDField.getText());
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
          // TODO Need refresh table here
        } catch (Exception e) {
          e.printStackTrace();
          locField.setText("Invalid input");
          laundryRequestIDField.setText("Invalid input");
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
      laundryRequestIDField.setText("Invalid input");
    }
  }

  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    if (laundryRequestIDField.getText() == null
        || laundryField.getText() == null
        || laundryRequestIDField.getText().isEmpty()
        || laundryField.getText().isEmpty()) {
      laundryRequestIDField.setText("Invalid input");
      laundryField.setText("Invalid input");
      return;
    }
    try {
      pkIDs.add(Integer.parseInt(laundryRequestIDField.getText()));
      pkIDs.add(Integer.parseInt(laundryField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      laundryRequestIDField.setText("Invalid input");
      laundryField.setText("Invalid input");
    }
    if (laundryReqs.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + laundryRequestIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        laundryReqs.deleteEntry(pkIDs);
        laundryField.setText("Request Deleted!");
        laundryRequestIDField.setText("Request Deleted!");
      }
    } else {
      laundryField.setText("Invalid Request!");
      laundryRequestIDField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
