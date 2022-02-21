package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editFoodReqFormController {

  @FXML TextField foodRequestIDField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField patientIDField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML TextField foodField;
  @FXML JFXButton editButton;

  RequestTable requests = RequestTable.getInstance();
  FoodRequestTbl foodReqs = FoodRequestTbl.getInstance();

  public void editForm(Request foodReq) throws IOException {
    foodRequestIDField.setText(String.valueOf(foodReq.getRequestID()));
    locField.setText(foodReq.getLocationID());
    subTimeField.setText(String.valueOf(foodReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(foodReq.getTimeEnd()));
    patientIDField.setText(String.valueOf(foodReq.getPatientID()));
    requesterIDField.setText(String.valueOf(foodReq.getEmpInitiated()));
    completerIDField.setText(String.valueOf(foodReq.getEmpCompleter()));
    statusField.setText(foodReq.getRequestStatus());
  }

  private boolean foodRequestExists() {
    return requests.entryExists(Integer.parseInt(foodRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    if (foodRequestExists()) {
      String locationID = locField.getText();
      Integer pkID = Integer.parseInt(foodRequestIDField.getText());
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
    pkIDs.add(Integer.parseInt(foodRequestIDField.getText()));
    pkIDs.add(Integer.parseInt(foodField.getText()));
    if (foodReqs.entryExists(pkIDs)) {
      foodReqs.deleteEntry(pkIDs);
      foodField.setText("Request Deleted!");
      foodRequestIDField.setText("Request Deleted!");
    } else {
      foodField.setText("Invalid Request!");
      foodRequestIDField.setText("Invalid Request!");
    }
  }
}
