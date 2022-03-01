package edu.wpi.GoldenGandaberundas.controllers.ComputerServiceControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class editComputerReqFormController {
  @FXML TextField computerRequestIDField;
  @FXML TextField subTimeField;
  @FXML TextField finishTimeField;
  @FXML TextField computerField;
  @FXML JFXButton editButton;
  @FXML ComboBox<String> statusComboBox;
  @FXML SearchableComboBox<Integer> completerComboBox;
  @FXML SearchableComboBox<Integer> requesterComboBox;
  @FXML SearchableComboBox<String> locationComboBox;

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
    subTimeField.setText(String.valueOf(computerReq.getTimeStart()));
    finishTimeField.setText(String.valueOf(computerReq.getTimeEnd()));

    statusComboBox.setPromptText(computerReq.getRequestStatus());
    statusComboBox.setValue(computerReq.getRequestStatus());
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");
    statusComboBox.setItems(FXCollections.observableArrayList(statusTypes));

    completerComboBox.setPromptText(String.valueOf(computerReq.getEmpCompleter()));
    completerComboBox.setValue(computerReq.getEmpCompleter());
    requesterComboBox.setPromptText(String.valueOf(computerReq.getEmpInitiated()));
    requesterComboBox.setValue(computerReq.getEmpInitiated());
    ArrayList<Integer> employeeIDs = new ArrayList<>();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(e.getEmpID());
    }
    completerComboBox.setItems(FXCollections.observableArrayList(employeeIDs));
    requesterComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    locationComboBox.setPromptText(computerReq.getLocationID());
    locationComboBox.setValue(computerReq.getLocationID());
    ArrayList<String> locations = new ArrayList<>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));
  }

  private boolean computerRequestExists() {
    return requests.entryExists(Integer.parseInt(computerRequestIDField.getText()));
  }

  @FXML
  public void editRequest() {
    try {
      if (computerRequestExists()) {
        try {
          String locationID = locationComboBox.getValue();
          Integer pkID = Integer.parseInt(computerRequestIDField.getText());
          Integer empInitiated = requesterComboBox.getValue();
          Integer empCompleter = completerComboBox.getValue();
          long timeStart = Long.parseLong(subTimeField.getText());
          long timeEnd = Long.parseLong(finishTimeField.getText());
          String requestStatus = statusComboBox.getValue();

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
          locationComboBox.setPromptText(locationComboBox.getValue());
          completerComboBox.setPromptText(String.valueOf(completerComboBox.getValue()));
          computerRequestIDField.setText("Invalid input");
          requesterComboBox.setPromptText(String.valueOf(requesterComboBox.getValue()));
          subTimeField.setText("Invalid input");
          finishTimeField.setText("Invalid input");
          statusComboBox.setPromptText(statusComboBox.getValue());
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
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
