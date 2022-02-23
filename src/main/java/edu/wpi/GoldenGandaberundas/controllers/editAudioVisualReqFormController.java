package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editAudioVisualReqFormController {
  @FXML TextField AVRequestIDField;
  @FXML TextField deviceField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField patientIDField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML JFXButton editButton;

  RequestTable requests = RequestTable.getInstance();
  AudioVisualRequestTbl AVReqs = AudioVisualRequestTbl.getInstance();
  ArrayList<Integer> pkIDs = new ArrayList<Integer>();

  /**
   * edits the form
   *
   * @param medReq request
   * @throws IOException oops
   */
  public void editForm(Request medReq) throws IOException {
    AVRequestIDField.setText(String.valueOf(medReq.getRequestID()));
    locField.setText(medReq.getLocationID());
    subTimeField.setText(String.valueOf(medReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(medReq.getTimeEnd()));
    patientIDField.setText(String.valueOf(medReq.getPatientID()));
    completerIDField.setText(String.valueOf(medReq.getEmpCompleter()));
    requesterIDField.setText(String.valueOf(medReq.getEmpInitiated()));
    statusField.setText(medReq.getRequestStatus());
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
          String locationID = locField.getText();
          Integer pkID = Integer.parseInt(AVRequestIDField.getText());
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
          // requests.editEntry(pkID, "notes", "");
          Stage stage = (Stage) editButton.getScene().getWindow();
          stage.close();
          // TODO Need refresh table here
        } catch (Exception e) {
          e.printStackTrace();
          locField.setText("Invalid input");
          AVRequestIDField.setText("Invalid input");
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
