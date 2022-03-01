package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabService;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class LabServiceRequestController implements Initializable {

  @FXML private TextField patientField;
  @FXML private TextField priorityField;
  @FXML private ChoiceBox<String> priorityBox;
  @FXML private TextArea descriptionBox;
  @FXML private SearchableComboBox<String> labTypeBox;
  @FXML private SearchableComboBox<String> deviceTypeBox;
  @FXML private SearchableComboBox<String> locationBox;
  @FXML private SearchableComboBox<Integer> patientComboBox;

  @FXML private TableView labMenu;
  @FXML private TableColumn<LabService, Integer> labID;
  @FXML private TableColumn<LabService, String> type;
  @FXML private TableColumn<LabService, String> locationID;
  @FXML private TableColumn<LabService, String> description;

  @FXML private TableView labRequests;
  @FXML private TableColumn<LabServiceRequest, Integer> requestID;
  @FXML private TableColumn<LabServiceRequest, String> nodeID;
  @FXML private TableColumn<LabServiceRequest, Integer> requesterID;
  @FXML private TableColumn<LabServiceRequest, Integer> completerID;
  @FXML private TableColumn<LabServiceRequest, Integer> submittedTime;
  @FXML private TableColumn<LabServiceRequest, Integer> completedTime;
  @FXML private TableColumn<LabServiceRequest, Integer> patientID;
  @FXML private TableColumn<LabServiceRequest, String> status;
  @FXML private TableColumn<LabServiceRequest, Integer> labService;
  @FXML private TableColumn<LabServiceRequest, String> notes;

  // Admin Buttons
  @FXML private JFXButton backupMenuButton;
  @FXML private JFXButton backupRequestsButton;
  @FXML private JFXButton loadMenuButton;
  @FXML private JFXButton loadRequestButton;
  @FXML private JFXButton refreshButton;

  private RequestTable requestTableController = RequestTable.getInstance();
  private TableController menuTableController = LabServiceTbl.getInstance();
  private TableController requestLabController = LabServiceRequestTbl.getInstance();

  private TableController locationTableController = LocationTbl.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    labID.setCellValueFactory(new PropertyValueFactory<LabService, Integer>("labID"));
    type.setCellValueFactory(new PropertyValueFactory<LabService, String>("labType"));
    locationID.setCellValueFactory(new PropertyValueFactory<LabService, String>("locID"));
    description.setCellValueFactory(new PropertyValueFactory<LabService, String>("description"));

    requestID.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("requestID"));
    nodeID.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("locationID"));
    requesterID.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("empCompleter"));
    submittedTime.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("timeStart"));
    completedTime.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("timeEnd"));
    patientID.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("patientID"));
    status.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, String>("requestStatus"));
    labService.setCellValueFactory(
        new PropertyValueFactory<LabServiceRequest, Integer>("labServiceID"));
    notes.setCellValueFactory(new PropertyValueFactory<LabServiceRequest, String>("notes"));

    // Populating location choice box
    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationBox.setItems(oList);

    // Populating Lab Type choice box
    ArrayList<String> findList = labTypeList();
    ObservableList<String> tList = FXCollections.observableArrayList(findList);
    labTypeBox.setItems(tList);

    //    // Populating device type choice box
    //    ObservableList<String> tList = FXCollections.observableArrayList(deviceTypeList());

    ArrayList<Integer> patientIDs = new ArrayList<>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));

    labRequests.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    // Setting up user permissionsR
    checkPerms();

    refresh();
  }

  // refresh tables
  public void refresh() {
    menuTableController = LabServiceTbl.getInstance();
    requestLabController = LabServiceRequestTbl.getInstance();
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<LabServiceRequest> lsrs = new ArrayList<>();
      for (LabServiceRequest lsr : LabServiceRequestTbl.getInstance().readTable()) {
        if (lsr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          lsrs.add(lsr);
        }
      }
      labRequests.getItems().setAll(lsrs);
    } else {
      labRequests.getItems().setAll(requestLabController.readTable());
    }

    labMenu.getItems().setAll(menuTableController.readTable());
  }

  void onEdit() {
    if (labRequests.getSelectionModel().getSelectedItem() != null) {
      LabServiceRequest selectedItem =
          (LabServiceRequest) labRequests.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editLabServiceReqForm.fxml"));
        AnchorPane editForm = load.load();
        editLabServiceReqFormController edit = load.getController();
        edit.editForm(RequestTable.getInstance().getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();
        stage.setOnCloseRequest(e -> refresh());
      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(selectedItem.getPK());
    }
  }

  /**
   * Checks if the data all has values and makes sure there are no semicolons or the such that check
   * code
   *
   * @return Boolean (If Data is Safe Returns True, Else false)
   */
  public boolean validateDataSafe() {
    String[] sqlComs = {
      "ALTER",
      "CREATE",
      "DELETE",
      "DROP",
      "DROP TABLE",
      "EXEC",
      "EXECUTE",
      "INSERT",
      "INSERT INTO",
      "INTO",
      "MERGE",
      "SELECT",
      "UPDATE",
      "UNION",
      "UNION ALL",
      "ALL"
    };
    for (String s : sqlComs) {
      if (descriptionBox.getText().toUpperCase().contains(s)
          || priorityField.getText().toUpperCase().contains(s)) {
        return false;
      }
    }
    return (descriptionBox.getText().matches("[\\w\\d\\s\\d.]+")
            || descriptionBox.getText().isBlank())
        && locationBox.getValue() != null
        && patientComboBox.getValue() != null
        && labTypeBox.getValue() != null
        && (priorityField.getText().matches("[\\w\\d\\s\\d.]+")
            || priorityField.getText().isBlank());
  }

  public void submit() {
    try {
      int idCounter =
          RequestTable.getInstance().readTable().size() - 1 < 0
              ? 0
              : requestTableController
                      .readTable()
                      .get(requestTableController.readTable().size() - 1)
                      .getRequestID()
                  + 1;
      int requesterID = CurrentUser.getUser().getEmpID();
      String location = locationBox.getValue();
      int deviceID = Integer.parseInt(labTypeBox.getValue());
      int patientID = patientComboBox.getValue();
      String note = "";
      if (descriptionBox.getText() != null || !descriptionBox.getText().isEmpty()) {
        note = descriptionBox.getText();
      }
      String priority = "";
      if (!priorityField.getText().isEmpty() || priorityField.getText() != null) {
        priority = priorityField.getText();
      }
      LabServiceRequest LSrequest =
          new LabServiceRequest(
              idCounter,
              location,
              requesterID,
              123,
              0,
              0,
              patientID,
              "Submitted",
              note,
              deviceID,
              priority);
      System.out.println(LSrequest);
      LabServiceRequestTbl.getInstance().addEntry(LSrequest);
      clear();
      refresh();
    } catch (Exception e) {
      // Do nothing
    }
  }

  /*
   * clears the submissions fields
   */
  public void clear() {
    descriptionBox.setText("");
    if (locationBox.getSelectionModel().getSelectedItem() != null) {
      locationBox.setValue(null);
    }

    // Populating location choice box
    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationBox.setItems(oList);

    // Populating device ID choice box
    if (labTypeBox.getSelectionModel().getSelectedItem() != null) {
      labTypeBox.setValue(null);
    }
    ArrayList<String> findList = labTypeList();
    ObservableList<String> tList = FXCollections.observableArrayList(findList);
    labTypeBox.setItems(tList);

    refresh();
  }

  /** creates backups of the Lab Service Request table in the users file system */
  @FXML
  public void backupLS() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Lab Service File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      LabServiceTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "/labServiceRequestBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  /** Creates a backup in the users file system of the LabServiceREquestTbl */
  public void backupRequests() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Lab Service Request File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      LabServiceRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "/labServiceRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  /** Method that loads a csv file to replace the LabServiceTbl */
  @FXML
  public void loadLS() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Laundry File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      LabServiceTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  /** Method that loads a csv file to replace the LabServiceRequestTbl */
  @FXML
  public void loadRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Lab Service Request File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      LabServiceRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  /** Function for populating the location choice box, called in initialize */
  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
    }
    return locNodeAr;
  }

  /**
   * populates the labType list
   *
   * @return arraylist of the possible types of lab procedures
   */
  public ArrayList<String> labTypeList() {
    ArrayList<String> labType = new ArrayList<>();
    labType.add("CBC");
    labType.add("BMP");
    labType.add("COAG");
    labType.add("URINE");
    labType.add("BLOOD TYPE");
    labType.add("PCR COVID");
    labType.add("RAPID COVID");

    return labType;
  }

  /**
   * Method that iterates through a users permissions and hides elements they dont have access too
   */
  public void checkPerms() {
    int currID = CurrentUser.getUser().getEmpID();
    //
    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
    System.out.println(perms);
    boolean hideShit = true;
    for (int i = 0; i < perms.size(); i++) {
      if (perms.get(i) == 111) {
        hideShit = false;
        break;
      }
    }
    if (hideShit == true) {
      hideAdmin();
    }
  }

  /** Helper method for checking perms which uses a switch case to hide elements */
  public void hideAdmin() {
    backupMenuButton.setVisible(false);
    backupMenuButton.setManaged(false);
    backupRequestsButton.setVisible(false);
    backupRequestsButton.setManaged(false);
    loadMenuButton.setVisible(false);
    loadMenuButton.setManaged(false);
    loadRequestButton.setVisible(false);
    loadRequestButton.setManaged(false);
    refreshButton.setVisible(false);
    refreshButton.setManaged(false);
  }
}
