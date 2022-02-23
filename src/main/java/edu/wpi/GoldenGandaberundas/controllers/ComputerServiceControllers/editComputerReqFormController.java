package edu.wpi.GoldenGandaberundas.controllers.ComputerServiceControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class editComputerReqFormController {
  @FXML TextField computerRequestIDField;
  @FXML TextField locField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField requesterIDField;
  @FXML TextField completerIDField;
  @FXML TextField statusField;
  @FXML TextField computerField;
  @FXML JFXButton editButton;

  RequestTable requests = RequestTable.getInstance();
  ComputerRequestTbl compRequests = ComputerRequestTbl.getInstance();

  /**
   * The fucntion to call for the controller of the editForm so that you can set the textField text
   * to the request's info.
   *
   * @param computerReq A computer request from the master request table
   * @throws IOException
   */
  public void editForm(Request computerReq) throws IOException {
    computerRequestIDField.setText(String.valueOf(computerReq.getRequestID()));
    locField.setText(computerReq.getLocationID());
    subTimeField.setText(String.valueOf(computerReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(computerReq.getTimeEnd()));
    requesterIDField.setText(String.valueOf(computerReq.getEmpInitiated()));
    completerIDField.setText(String.valueOf(computerReq.getEmpCompleter()));
    statusField.setText(computerReq.getRequestStatus());
  }

  private boolean computerRequestExists() {
    return requests.entryExists(Integer.parseInt(computerRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (computerRequestExists()) {
        try {
          String locationID = locField.getText();
          Integer pkID = Integer.parseInt(computerRequestIDField.getText());
          Integer empInitiated = Integer.parseInt(requesterIDField.getText());
          Integer empCompleter = Integer.parseInt(completerIDField.getText());
          long timeStart = Integer.parseInt(subTimeField.getText());
          long timeEnd = Integer.parseInt(finishTimeField.getText());
          String requestStatus = statusField.getText();

          requests.editEntry(pkID, "locationID", locationID);
          requests.editEntry(pkID, "empInitiated", empInitiated);
          requests.editEntry(pkID, "empCompleter", empCompleter);
          requests.editEntry(pkID, "timeStart", timeStart);
          requests.editEntry(pkID, "timeEnd", timeEnd);
          requests.editEntry(pkID, "requestStatus", requestStatus);
          Stage stage = (Stage) editButton.getScene().getWindow();
          stage.close();
          // TODO Need refresh table here
        } catch (Exception e) {
          e.printStackTrace();
          locField.setText("Invalid input");
          completerIDField.setText("Invalid input");
          computerRequestIDField.setText("Invalid input");
          requesterIDField.setText("Invalid input");
          subTimeField.setText("Invalid input");
          finishTimeField.setText("Invalid input");
          statusField.setText("Invalid input");
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      completerIDField.setText("Invalid input");
    }
  }

  /**
   * Takes in the text for the Computer and Computer Request text fields and deletes the request if
   * the PKIDs exist
   */
  @FXML
  public void deleteRequest() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    if (computerRequestIDField.getText() == null
        || computerField.getText() == null
        || computerField.getText().isEmpty()
        || computerRequestIDField.getText().isEmpty()) {
      computerRequestIDField.setText("Invalid input");
      computerField.setText("Invalid input");
      return;
    }
    try {
      pkIDs.add(Integer.parseInt(computerRequestIDField.getText()));
      pkIDs.add(Integer.parseInt(computerField.getText()));
    } catch (Exception e) {
      e.printStackTrace();
      computerRequestIDField.setText("Invalid input");
      computerField.setText("Invalid input");
    }
    if (compRequests.entryExists(pkIDs)) {
      Alert alert =
          new Alert(
              Alert.AlertType.CONFIRMATION,
              "Delete Request: " + computerRequestIDField.getText() + " ?",
              ButtonType.YES,
              ButtonType.NO);
      alert.showAndWait();
      if (alert.getResult() == ButtonType.YES) {
        compRequests.deleteEntry(pkIDs);
        computerRequestIDField.setText("Request Deleted!");
        computerField.setText("Request Deleted!");
      }
    } else {
      computerRequestIDField.setText("Invalid Request!");
      computerField.setText("Invalid Request!");
    }
    // TODO Need refresh table here
  }
}
