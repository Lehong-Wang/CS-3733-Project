package edu.wpi.GoldenGandaberundas.controllers.ComputerServiceControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.Computer;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
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

public class computerServiceController implements Initializable {

  @FXML SearchableComboBox<Integer> deviceSearchBox;
  @FXML ComboBox<String> problemTypeBox;
  @FXML TextArea problemField;
  @FXML TextField priorityField;

  @FXML TableView computersTable;
  @FXML TableColumn<Computer, Integer> computerID;
  @FXML TableColumn<Computer, String> computerType;
  @FXML TableColumn<Computer, String> os;
  @FXML TableColumn<Computer, String> processor;
  @FXML TableColumn<Computer, String> hostName;
  @FXML TableColumn<Computer, String> model;
  @FXML TableColumn<Computer, String> manufacturer;
  @FXML TableColumn<Computer, String> serialNumber;

  @FXML TableView computerRequestsTbl;
  @FXML TableColumn<ComputerRequest, Integer> requestID;
  @FXML TableColumn<ComputerRequest, String> locationID;
  @FXML TableColumn<ComputerRequest, Integer> timeStart;
  @FXML TableColumn<ComputerRequest, Integer> timeEnd;
  @FXML TableColumn<ComputerRequest, Integer> patientID;
  @FXML TableColumn<ComputerRequest, Integer> empInitiated;
  @FXML TableColumn<ComputerRequest, Integer> empCompleter;
  @FXML TableColumn<ComputerRequest, String> requestStatus;
  @FXML TableColumn<ComputerRequest, String> notes;
  @FXML TableColumn<ComputerRequest, Integer> compID;
  @FXML TableColumn<ComputerRequest, String> problemType;
  @FXML TableColumn<ComputerRequest, String> priority;

  @FXML private SearchableComboBox<String> locationSearchBox; // Location drop box

  // Admin Buttons
  @FXML private JFXButton backupMenuButton;
  @FXML private JFXButton backupRequestsButton;
  @FXML private JFXButton loadMenuButton;
  @FXML private JFXButton loadRequestButton;
  @FXML private JFXButton refreshButton;

  TableController locationTableController = LocationTbl.getInstance();
  ComputerTbl computers = ComputerTbl.getInstance();
  ComputerRequestTbl computerRequests = ComputerRequestTbl.getInstance();

  String locations = ""; // String for location of request, value is gotten from location drop down

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    /**
     * Sets the cell value factories for the computers "menu" table each of the cell value factories
     * make a new property value factory which takes in the exact name of the value in the class
     */
    computerID.setCellValueFactory(new PropertyValueFactory<Computer, Integer>("computerID"));
    computerType.setCellValueFactory(new PropertyValueFactory<Computer, String>("computerType"));
    os.setCellValueFactory(new PropertyValueFactory<Computer, String>("os"));
    processor.setCellValueFactory(new PropertyValueFactory<Computer, String>("processor"));
    hostName.setCellValueFactory(new PropertyValueFactory<Computer, String>("hostName"));
    model.setCellValueFactory(new PropertyValueFactory<Computer, String>("model"));
    manufacturer.setCellValueFactory(new PropertyValueFactory<Computer, String>("manufacturer"));
    serialNumber.setCellValueFactory(new PropertyValueFactory<Computer, String>("serialNumber"));

    /**
     * Sets the cell value factories for the requests table each of the cell value factories make a
     * new property value factory which takes in the exact name of the value in the class
     */
    requestID.setCellValueFactory(new PropertyValueFactory<ComputerRequest, Integer>("requestID"));
    locationID.setCellValueFactory(new PropertyValueFactory<ComputerRequest, String>("locationID"));
    timeStart.setCellValueFactory(new PropertyValueFactory<ComputerRequest, Integer>("timeStart"));
    timeEnd.setCellValueFactory(new PropertyValueFactory<ComputerRequest, Integer>("timeEnd"));
    // patientID.setCellValueFactory(new PropertyValueFactory<ComputerRequest,
    // Integer>("patientID"));
    empInitiated.setCellValueFactory(
        new PropertyValueFactory<ComputerRequest, Integer>("empInitiated"));
    empCompleter.setCellValueFactory(
        new PropertyValueFactory<ComputerRequest, Integer>("empCompleter"));
    requestStatus.setCellValueFactory(
        new PropertyValueFactory<ComputerRequest, String>("requestStatus"));
    notes.setCellValueFactory(new PropertyValueFactory<ComputerRequest, String>("notes"));
    compID.setCellValueFactory(new PropertyValueFactory<ComputerRequest, Integer>("computerID"));
    problemType.setCellValueFactory(
        new PropertyValueFactory<ComputerRequest, String>("problemType"));
    priority.setCellValueFactory(new PropertyValueFactory<ComputerRequest, String>("priority"));

    locationSearchBox.setOnAction(
        (event) -> {
          String selectedItem = (String) locationSearchBox.getSelectionModel().getSelectedItem();
          locations = selectedItem;
        });

    computerRequestsTbl.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    locList();

    /*
     * Gets the list of computer IDs from ComputersTable and sets
     * the deviceSearchBox to that list
     * Any edits to the computer menu while in this page will not
     * update the combobox
     */
    ArrayList<Integer> computerIDs = new ArrayList<>();
    for (Computer c : ComputerTbl.getInstance().readTable()) {
      computerIDs.add(c.getComputerID());
    }
    ObservableList<Integer> ocomputerIDs = FXCollections.observableArrayList(computerIDs);
    deviceSearchBox.setItems(ocomputerIDs);

    /*
     * Sets the problems List for the problems combobox in fxml.
     * Adding or editing any of these changes the possible problems users
     * can submit in the problem type box.
     */
    ArrayList<String> problemsList = new ArrayList<>();
    problemsList.add("wifi connection");
    problemsList.add("wire connection");
    problemsList.add("software installation");
    problemsList.add("computer won't turn on");
    problemsList.add("email problem");
    problemsList.add("wireless connection laptop");
    problemsList.add("wireless connection handheld");
    problemsList.add("authentication issues");
    problemsList.add("device reset");
    problemsList.add("server outage");
    problemsList.add("printer broken");
    problemsList.add("fax broken");
    problemsList.add("unclassifiable request");
    ObservableList<String> oproblemsList = FXCollections.observableArrayList(problemsList);

    problemTypeBox.setItems(oproblemsList);

    // Setting up user permissions
    checkPerms();

    refresh();
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
      if (problemField.getText().toUpperCase().contains(s)
          || priorityField.getText().toUpperCase().contains(s)) {
        return false;
      }
    }
    return problemField.getText().matches("[\\w\\d\\s\\d.]+")
        && locationSearchBox.getValue() != null
        && problemTypeBox.getValue() != null
        && deviceSearchBox.getValue() != null
        && priorityField.getText().matches("[\\w\\d\\s\\d.]+");
  }

  void onEdit() {
    if (computerRequestsTbl.getSelectionModel().getSelectedItem() != null) {
      try {
        ComputerRequest selectedItem =
            (ComputerRequest) computerRequestsTbl.getSelectionModel().getSelectedItem();
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editComputerReqForm.fxml"));
        AnchorPane editForm = load.load();
        editComputerReqFormController edit = load.getController();
        edit.editForm(RequestTable.getInstance().getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Gets the table from the database for computers and computer requests and sets the table views
   * to those values.
   */
  public void refresh() {
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<ComputerRequest> crs = new ArrayList<>();
      for (ComputerRequest cr : ComputerRequestTbl.getInstance().readTable()) {
        if (cr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          crs.add(cr);
        }
      }
      computerRequestsTbl.getItems().setAll(crs);
    } else {
      computerRequestsTbl.getItems().setAll(computerRequests.readTable());
    }
    computersTable.getItems().setAll(computers.readTable());
  }

  @FXML
  public void backupComputers() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Computers File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      ComputerTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\computerTblBackUp.csv"));
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
      ComputerRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\computerRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDBComputers() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Computers File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      ComputerTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Computer Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      ComputerRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  public void submit() {

    if (validateDataSafe()) {
      int requestID =
          RequestTable.getInstance().readTable().size() - 1 < 0
              ? 0
              : RequestTable.getInstance()
                      .readTable()
                      .get(RequestTable.getInstance().readTable().size() - 1)
                      .getRequestID()
                  + 1;
      String locationID = locationSearchBox.getValue();
      int submitTime = 0;
      int completeTime = 0;
      // Patient ID = null
      int empInitiated = CurrentUser.getUser().getEmpID();
      // Emp Completer = null
      String requestStatus = "Submitted";
      String notes = problemField.getText();
      int compID = deviceSearchBox.getValue();
      String problemType = problemTypeBox.getValue();
      String priority = priorityField.getText();

      ComputerRequest request =
          new ComputerRequest(
              requestID,
              locationID,
              empInitiated,
              null,
              submitTime,
              completeTime,
              null,
              requestStatus,
              notes,
              compID,
              problemType,
              priority);
      ComputerRequestTbl.getInstance().addEntry(request);
      refresh();
    } else {
      problemField.setText("Invalid Input");
      priorityField.setText("Invalid Input");
    }
  }

  public void clear() {}

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
    if (hideShit) {
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
