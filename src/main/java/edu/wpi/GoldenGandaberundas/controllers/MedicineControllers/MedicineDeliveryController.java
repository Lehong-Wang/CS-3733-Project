package edu.wpi.GoldenGandaberundas.controllers.MedicineControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class MedicineDeliveryController {

  @FXML Button submitBtn;
  @FXML private SearchableComboBox<String> locationSearchBox;
  // String that holds the location selected in search box used for object creation
  private String locations;
  private int currentPatientID;
  private int currentMedicineID;
  @FXML private SearchableComboBox<Integer> medicineSearchBox;
  @FXML private SearchableComboBox<Integer> patientSearchBox;
  @FXML private TextField dosageField;
  @FXML private TextField quantityField;
  @FXML private TextArea notesField;

  @FXML TableView medicineTbl; // Service Table

  @FXML TableColumn<MedicineRequest, String> reqID;
  @FXML TableColumn<MedicineRequest, String> nodeID;
  @FXML TableColumn<MedicineRequest, Integer> submitTime;
  @FXML TableColumn<MedicineRequest, Long> completeTime;
  @FXML TableColumn<MedicineRequest, Integer> patientID;
  @FXML TableColumn<MedicineRequest, Integer> requesterID;
  @FXML TableColumn<MedicineRequest, Integer> completerID;
  @FXML TableColumn<MedicineRequest, String> status;

  @FXML TableColumn<MedicineRequest, Integer> medID;
  @FXML TableColumn<MedicineRequest, Integer> quantity;
  @FXML TableColumn<MedicineRequest, Integer> dosage;
  @FXML TableColumn<MedicineRequest, String> priority;

  @FXML TableView medicineMenuTable;
  @FXML TableColumn<Medicine, String> medName;
  @FXML TableColumn<Medicine, String> medicineID;
  @FXML TableColumn<Medicine, String> description;
  @FXML TableColumn<Medicine, Double> price;
  @FXML TableColumn<Medicine, Boolean> inStock;

  @FXML private JFXButton backupMenuButton;
  @FXML private JFXButton backupRequestsButton;
  @FXML private JFXButton loadMenuButton;
  @FXML private JFXButton loadRequestButton;
  @FXML private JFXButton refreshButton;

  private PatientTbl patients;
  private MedicineTbl medicine;
  private MedicineRequestTbl requestTable;
  private RequestTable reqTable = RequestTable.getInstance();
  private TableController locationTableController = LocationTbl.getInstance();
  private TableController patientTableController = PatientTbl.getInstance();
  private TableController medicineTableController = MedicineTbl.getInstance();

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  // Runs when scene loads
  @FXML
  public void initialize() {

    reqID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("requestID"));
    nodeID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("locationID"));
    submitTime.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("timeStart"));
    completeTime.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Long>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("patientID"));
    requesterID.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, Integer>("empCompleter"));
    status.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("requestStatus"));
    medID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("medicineID"));
    quantity.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("quantity"));
    dosage.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("dosage"));
    // priority.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("priority"));

    medName.setCellValueFactory(new PropertyValueFactory<Medicine, String>("medName"));
    medicineID.setCellValueFactory(new PropertyValueFactory<Medicine, String>("medicineID"));
    description.setCellValueFactory(new PropertyValueFactory<Medicine, String>("description"));
    price.setCellValueFactory(new PropertyValueFactory<Medicine, Double>("price"));
    inStock.setCellValueFactory(new PropertyValueFactory<Medicine, Boolean>("inStock"));

    Patient patient = new Patient(123, "FDEPT00201", "bruh", "moment");

    // MedicineRequest testReq = new MedicineRequest("bruh", "FDEPT00101", 0, 0, 123, 123, 123,
    // "ok");

    // Populating location choice box
    locList();
    patientList();
    medList();
    // Setting up choice box Listeners
    setupComboListeners();

    // Populating the Medicine choice box with
    // patients = PatientTbl.getInstance();
    // patients.addEntry(patient);

    requestTable = MedicineRequestTbl.getInstance();
    // orderedMedicine = OrderedMedicineTbl.getInstance();
    // requestTable.addEntry(testReq);
    refresh();

    medicineTbl.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    // Setting up the user permissions
    checkPerms();
  }

  /** Method that populates the location combo box from the locationTbl Called in initialize */
  public void locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();
    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
    }
    ObservableList<String> oList = FXCollections.observableArrayList(locNodeAr);
    locationSearchBox.setItems(oList);
  }

  /** Method that populates the patient combo box from the patientTbl Called in initialize */
  public void patientList() {
    ArrayList<Patient> patientArray = new ArrayList<Patient>();
    patientArray = patientTableController.readTable();
    ArrayList<Integer> patientIDAr = new ArrayList<Integer>();
    for (int i = 0; i < patientArray.size(); i++) {
      patientIDAr.add(i, patientArray.get(i).getPatientID());
    }
    ObservableList<Integer> oList = FXCollections.observableArrayList(patientIDAr);
    patientSearchBox.setItems(oList);
  }

  /** Method that populates the medicine combo box from the MedicineTbl Called in initialize */
  public void medList() {
    ArrayList<Medicine> medArray = new ArrayList<Medicine>();
    medArray = medicineTableController.readTable();
    ArrayList<Integer> medIDAr = new ArrayList<Integer>();
    for (int i = 0; i < medArray.size(); i++) {
      medIDAr.add(i, medArray.get(i).getMedicineID());
    }
    ObservableList<Integer> oList = FXCollections.observableArrayList(medIDAr);
    medicineSearchBox.setItems(oList);
  }

  /** Method that sets up the event listeners for the searchable combo boxes Called in initialize */
  public void setupComboListeners() {
    locationSearchBox.setOnAction(
        (event) -> {
          String selectedItem = (String) locationSearchBox.getSelectionModel().getSelectedItem();
          locations = selectedItem;
        });

    medicineSearchBox.setOnAction(
        (event) -> {
          Integer selectedItem = (Integer) medicineSearchBox.getSelectionModel().getSelectedItem();
          currentMedicineID = selectedItem;
        });

    patientSearchBox.setOnAction(
        (event) -> {
          Integer selectedItem = (Integer) patientSearchBox.getSelectionModel().getSelectedItem();
          currentPatientID = selectedItem;
        });
  }

  void onEdit() {
    if (medicineTbl.getSelectionModel().getSelectedItem() != null) {
      MedicineRequest selectedItem =
          (MedicineRequest) medicineTbl.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editMedReqForm.fxml"));
        AnchorPane editForm = load.load();
        editMedReqFormController edit = load.getController();
        edit.editForm(reqTable.getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(selectedItem.getPK());
    }
  }

  @FXML
  public void submit() {
    requestTable = MedicineRequestTbl.getInstance();
    int requestID = 0;
    String notes = notesField.getText();
    if (reqTable.readTable().size() == 0) {
      requestID = 0;
    } else {
      requestID = reqTable.readTable().get(reqTable.readTable().size() - 1).getRequestID() + 1;
    }
    String nodeID = locations;
    int patientID = currentPatientID;
    int requesterID = CurrentUser.getUser().getEmpID();
    int medicineID = currentMedicineID;
    //    int dosage = 0;
    //    int quantity = 0;
    try {
      int dosage = Integer.parseInt(dosageField.getText());
      int quantity = Integer.parseInt(quantityField.getText());
      MedicineRequest medicineRequest2 =
          new MedicineRequest(
              requestID,
              nodeID,
              requesterID,
              123,
              000,
              000,
              patientID,
              "Submitted",
              notes,
              medicineID,
              dosage,
              quantity);
      RequestTable.getInstance().addEntry(medicineRequest2);
    } catch (NumberFormatException e) {
      dosageField.setText("Invalid Input");
      quantityField.setText("Invalid Input");
      System.out.println("Invalid input, dosage and quantity must be integers");
      e.printStackTrace();
    }

    refresh();
  }

  @FXML
  public void loadRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up File");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));

    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      requestTable.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void backupMedicine() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Medicine File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      medicine.createBackup(new File(selectedFile.toString() + "\\medicineBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void backupRequests() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Requests File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      MedicineRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\medMedicineRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDBMedicine() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Medicine File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      medicine.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Medicine Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      MedicineRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void refresh() {
    requestTable = MedicineRequestTbl.getInstance();
    medicineTableController = MedicineTbl.getInstance();
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<MedicineRequest> mrs = new ArrayList<>();
      for (MedicineRequest mr : MedicineRequestTbl.getInstance().readTable()) {
        if (mr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          mrs.add(mr);
        }
      }
      medicineTbl.getItems().setAll(mrs);
    } else {
      medicineTbl.getItems().setAll(requestTable.readTable());
    }
    medicineMenuTable.getItems().setAll(medicineTableController.readTable());
  }

  public void editEntry() throws IOException {
    //    Stage stage = new Stage();
    //    stage.setScene(new
    // Scene(FXMLLoader.load(App.class.getResource("views/editMedReqForm.fxml"))));
    //    stage.show();

    Stage stage = new Stage();
    Scene scene = new Scene(FXMLLoader.load(App.class.getResource("views/editMedReqForm.fxml")));
    stage.setScene(scene);
    stage.show();
  }

  /**
   * Method that iterates through a users permissions and hides elements they dont have access too
   */
  public void checkPerms() {
    int currID = CurrentUser.getUser().getEmpID();
    //
    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
    System.out.println(perms);
    for (int i = 0; i < perms.size(); i++) {
      setPerms(perms.get(i));
      // For type and perm description
      // PermissionTbl.getInstance().getEntry(perms.get(i));
    }
  }

  /**
   * Helper method for checking perms which uses a switch case to hide elements
   *
   * @param permID
   */
  public void setPerms(int permID) {
    switch (permID) {
      case (111):
        break;
      case (222):
        break;
      case (333):
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
        break;
      case (444):
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
        break;
      case (555):
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
        break;
      case (666):
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
        break;
      default:
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
        break;
    }
  }
}
