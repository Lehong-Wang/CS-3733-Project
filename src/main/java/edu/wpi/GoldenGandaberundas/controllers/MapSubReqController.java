package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.SearchableComboBox;

import java.util.ArrayList;

public class MapSubReqController {
  @FXML private TextField reqIDText;
  @FXML private SearchableComboBox<String> locSearch;
  @FXML private SearchableComboBox<Employee> empStartSearch;
  @FXML private SearchableComboBox<Employee> empEndSearch;
  @FXML private SearchableComboBox<Patient> patientIDSearch;
  @FXML private SearchableComboBox<String> reqStatusSearch;
  @FXML private SearchableComboBox<String> reqTypeSearch;

  @FXML private JFXButton submitBtn;
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton deleteBtn;
  @FXML private JFXButton closeBtn;

  @FXML private Label errorLabel;

  @FXML private AnchorPane anchorPane;

  private boolean edit = false;

  private MapController mapController = null;
  private TableController locationTbl = null;

  public void initialize() {
    submitBtn.setDisable(true);
    clearBtn.setDisable(true);
    deleteBtn.setVisible(false);

    closeBtn.setOnMouseReleased(
        e -> {
          anchorPane.getParent().setManaged(false);
          anchorPane.getParent().setVisible(false);
          System.out.println("alt click");
        });

    ObservableList<String> locList = FXCollections.observableArrayList();
    ArrayList<Location> locs =LocationTbl.getInstance().readTable();
    for(Location l : locs){
      locList.add(l.getNodeID());
    }
    locSearch.setItems(locList);
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
    locSearch.setPromptText(req.getLocationID());
    Employee temp = EmployeeTbl.getInstance().getEntry(req.getEmpInitiated());
    empStartSearch.setPromptText(temp.getFName() + " " + temp.getLName());
    temp = EmployeeTbl.getInstance().getEntry(req.getEmpCompleter());
    empEndSearch.setPromptText(temp.getFName() + " " + temp.getLName());
    patientIDSearch.setPromptText(req.getPatientID().toString());

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
}
