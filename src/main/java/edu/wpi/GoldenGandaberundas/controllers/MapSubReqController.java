package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.util.ArrayList;
import java.util.Locale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.SearchableComboBox;

public class MapSubReqController {
  @FXML private TextField reqIDText;
  @FXML private SearchableComboBox<String> locSearch;
  @FXML private SearchableComboBox<String> empStartSearch;
  @FXML private SearchableComboBox<String> empEndSearch;
  @FXML private SearchableComboBox<String> patientIDSearch;
  @FXML private SearchableComboBox<String> reqStatusSearch;

  @FXML private JFXButton submitBtn;
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton deleteBtn;
  @FXML private JFXButton closeBtn;

  @FXML private Label errorLabel;

  @FXML private AnchorPane anchorPane;

  private boolean edit = false;

  private MapController mapController = null;
  private TableController locationTbl = null;

  Request reqs = null;

  public void initialize() {
    submitBtn.setDisable(true);
    clearBtn.setDisable(true);
    deleteBtn.setVisible(false);

    closeBtn.setOnMouseReleased(
        e -> {
          anchorPane.getParent().setManaged(false);
          anchorPane.getParent().setVisible(false);
        });

    ObservableList<String> locList = FXCollections.observableArrayList();
    ArrayList<Location> locs = LocationTbl.getInstance().readTable();
    for (Location l : locs) {
      locList.add(l.getNodeID());
    }
    locSearch.setItems(locList);

    ObservableList<String> empList = FXCollections.observableArrayList();
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      empList.add(e.getEmpID() + "");
    }
    empStartSearch.setItems(empList);
    empEndSearch.setItems(empList);

    ObservableList<String> patList = FXCollections.observableArrayList();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patList.add(String.valueOf(p.getPatientID()));
    }
    patientIDSearch.setItems(patList);

    ObservableList<String> reqStatusList = FXCollections.observableArrayList();
    reqStatusList.add("Submitted");
    reqStatusList.add("In_Progress");
    reqStatusList.add("Completed");
    reqStatusSearch.setItems(reqStatusList);
  }

  public void setMapController(MapController mapCont) {
    mapController = mapCont;
    locationTbl = mapCont.locations;
  }

  public void setText(Request req) {
    if (req == null) {
      System.out.println("NULL");
      System.out.println();
      return;
    }
    submitBtn.setText("Submit");
    reqIDText.setDisable(true);
    reqIDText.setText(req.getRequestID().toString());
    reqStatusSearch.setValue(req.getRequestStatus());
    System.out.println(req.getRequestStatus());
    locSearch.setValue(req.getLocationID());
    Employee temp = EmployeeTbl.getInstance().getEntry(req.getEmpInitiated());
    empStartSearch.setValue(temp.getEmpID() + "");
    temp = EmployeeTbl.getInstance().getEntry(req.getEmpCompleter());
    empEndSearch.setValue(temp.getEmpID() + "");
    patientIDSearch.setValue(req.getPatientID().toString());

    reqs = req;

    edit = true;
    clearBtn.setDisable(false);
    submitBtn.setDisable(false);
    deleteBtn.setVisible(true);
  }

  /**
   * This method modifies or creates new nodes If the node has been clicked on, the node is edited
   * if all fields of the table have been filled, a new
   */
  public void editLocation() {}

  /**
   * checks the form fields to verify that they have been filled enables the submit only if all
   * fields are filled enables the clear button if any field is filled
   */
  @FXML
  private void submitLock() {
    //    for (TextField t : nodeFields) {
    //      if (t.getText().isEmpty()) {
    //        submitBtn.setDisable(true);
    //        return;
    //      }
    //      clearBtn.setDisable(false);
    //    }
    //    submitBtn.setDisable(false);
  }

  /** sets the node data form to blank */
  @FXML
  private void clear() {
    //    for (TextField t : nodeFields) {
    //      t.setText("");
    //    }
    reqIDText.setDisable(false);
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
    deleteBtn.setVisible(false);
    edit = false;
  }

  /** deletes selected node */
  @FXML
  private void delete() {
    //    System.out.println(
    //        locationTbl.deleteEntry(reqIDText.getText().trim().toUpperCase(Locale.ROOT)));
    //    System.out.println(
    //        locationTbl.entryExists(reqIDText.getText().trim().toUpperCase(Locale.ROOT)));
    //    mapController.refreshMap();
    clear();
  }

  public void submit() {

    if (reqStatusSearch.getValue() != null || !reqStatusSearch.getValue().equals("")) {
      System.out.println(reqStatusSearch.getValue().trim());
      if (reqStatusSearch.getValue().equals("")) {
        reqStatusSearch.setValue(reqs.getRequestStatus());
      }
      RequestTable.getInstance()
          .editEntry(reqs, "requestStatus", reqStatusSearch.getValue().trim());
    }
    if (locSearch.getValue() != null) {
      if (locSearch.getValue().equals("")) {
        locSearch.setValue(reqs.getLocationID());
      }
      RequestTable.getInstance()
          .editEntry(reqs, "locationID", locSearch.getValue().trim().toUpperCase(Locale.ROOT));
      System.out.println(RequestTable.getInstance().getEntry(reqs.getRequestID()));
    }
    if (empStartSearch.getValue() != null) {
      if (empStartSearch.getValue().equals("")) {
        empStartSearch.setValue(String.valueOf(reqs.getEmpInitiated()));
      }
      RequestTable.getInstance()
          .editEntry(
              reqs,
              "empInitiated",
              Integer.parseInt(empStartSearch.getValue().trim().toUpperCase(Locale.ROOT)));
    }
    if (empEndSearch.getValue() != null) {
      if (empEndSearch.getValue().equals("")) {
        empEndSearch.setValue(String.valueOf(reqs.getEmpCompleter()));
      }
      RequestTable.getInstance()
          .editEntry(
              reqs,
              "empCompleter",
              Integer.parseInt(empEndSearch.getValue().trim().toUpperCase(Locale.ROOT)));
    }
    if (patientIDSearch.getValue() != null) {
      if (patientIDSearch.getValue().equals("")) {
        patientIDSearch.setValue(String.valueOf(reqs.getPatientID()));
      }
      RequestTable.getInstance()
          .editEntry(reqs, "patientID", Integer.parseInt(patientIDSearch.getValue().trim()));
    }
    mapController.setRequest();
  }
}
