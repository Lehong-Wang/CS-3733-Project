package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
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
  @FXML TextField notesField;
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
    notesField.setText(medReq.getNotes());
  }

  /**
   * checks is a med request exists
   *
   * @return true if a request exists
   */
  private boolean medRequestExists() {
    return requests.entryExists(Integer.parseInt(AVRequestIDField.getText()));
  }

  /** edits a requests */
  @FXML
  public void editRequest() {
    if (medRequestExists()) {
      String locationID = locField.getText();
      Integer pkID = Integer.parseInt(AVRequestIDField.getText());
      Integer empInitiated = Integer.parseInt(requesterIDField.getText());
      Integer empCompleter = Integer.parseInt(completerIDField.getText());
      long timeStart = Integer.parseInt(subTimeField.getText());
      long timeEnd = Integer.parseInt(finishTimeField.getText());
      Integer patientID = Integer.parseInt(patientIDField.getText());
      String requestStatus = statusField.getText();
      String notes = notesField.getText();

      requests.editEntry(pkID, "locationID", locationID);
      requests.editEntry(pkID, "empInitiated", empInitiated);
      requests.editEntry(pkID, "empCompleter", empCompleter);
      requests.editEntry(pkID, "timeStart", timeStart);
      requests.editEntry(pkID, "timeEnd", timeEnd);
      requests.editEntry(pkID, "patientID", patientID);
      requests.editEntry(pkID, "requestStatus", requestStatus);
      requests.editEntry(pkID, "notes", notes);
    }
    Stage stage = (Stage) editButton.getScene().getWindow();
    stage.close();
  }

  /** deletes a request */
  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    pkIDs.add(Integer.parseInt(AVRequestIDField.getText()));
    pkIDs.add(Integer.parseInt(deviceField.getText()));
    if (AVReqs.entryExists(pkIDs)) {
      AVReqs.deleteEntry(pkIDs);
      deviceField.setText("Request Deleted!");
      AVRequestIDField.setText("Request Deleted!");
    } else {
      deviceField.setText("Invalid Request!");
      AVRequestIDField.setText("Invalid Request!");
    }
  }
}
