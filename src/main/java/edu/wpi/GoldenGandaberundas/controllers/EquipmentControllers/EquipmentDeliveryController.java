package edu.wpi.GoldenGandaberundas.controllers.EquipmentControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

public class EquipmentDeliveryController {

  @FXML TableView medEqTable;
  @FXML TableColumn<MedEquipRequest, Integer> reqID;
  @FXML TableColumn<MedEquipRequest, Integer> empID;
  @FXML TableColumn<MedEquipRequest, String> destination;
  @FXML TableColumn<MedEquipRequest, Integer> submitTime;
  @FXML TableColumn<MedEquipRequest, Integer> completeTime;
  @FXML TableColumn<MedEquipRequest, Integer> completerID;
  @FXML TableColumn<MedEquipRequest, Integer> patientID;

  @FXML TableColumn<MedEquipRequest, String> status;
  @FXML TableColumn<MedEquipRequest, String> notes;
  @FXML TableColumn<MedEquipRequest, Integer> equipmentID;

  @FXML TableView equipmentTable;
  // @FXML TableColumn<MedEquipment, Integer> medID;
  @FXML TableColumn<MedEquipment, Integer> equipID;
  @FXML TableColumn<MedEquipment, String> type;
  @FXML TableColumn<MedEquipment, String> equipStatus;
  @FXML TableColumn<MedEquipment, String> loc;

  @FXML SearchableComboBox<Integer> equipmentSearchBox;
  private int selectedEquipment;
  @FXML SearchableComboBox<String> locationSearchBox;
  private String selectedLocation;
  @FXML TextField notesField;

  @FXML private StackPane mapStackPane;
  private GesturePane mapGesture = null;
  private ImageView mapView = null;
  private Pane iconPane = null;
  private String currentFloor = null;

  @FXML private JFXButton backupMenuButton;
  @FXML private JFXButton backupRequestsButton;
  @FXML private JFXButton loadMenuButton;
  @FXML private JFXButton loadRequestButton;
  @FXML private JFXButton refreshButton;

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  private TableController medEquipRequestTbl = MedEquipRequestTbl.getInstance();
  private RequestTable reqTable = RequestTable.getInstance();
  private MedEquipmentTbl medEquipmentTable = MedEquipmentTbl.getInstance();
  private TableController locationTableController = LocationTbl.getInstance();

  //  private TableController employees = EmployeeTbl.getInstance();
  //  private TableController equipment = MedEquipmentTbl.getInstance();
  //  private PatientTbl patients = PatientTbl.getInstance();
  //  private LocationTbl loc = LocationTbl.getInstance();

  // public EquipmentDeliveryController() throws SQLException {}

  @FXML
  public void initialize() {
    // Sets all the ways for the tables to get the value from a list of requests
    reqID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("requestID"));
    empID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("empInitiated"));
    // medID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("medID"));
    destination.setCellValueFactory(
        new PropertyValueFactory<MedEquipRequest, String>("locationID"));
    submitTime.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("timeStart"));
    completeTime.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("timeEnd"));
    empID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(
        new PropertyValueFactory<MedEquipRequest, Integer>("empCompleter"));
    patientID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("patientID"));
    status.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, String>("requestStatus"));
    notes.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, String>("notes"));
    equipmentID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("medID"));

    equipID.setCellValueFactory(new PropertyValueFactory<MedEquipment, Integer>("medID"));
    type.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("medEquipmentType"));
    equipStatus.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("status"));
    loc.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("currLoc"));
    refreshTable();

    // Calling methods to set up search boxes
    equipmentList();
    locList();
    setupComboListeners();

    // Calling methods to set up search boxes
    equipmentList();
    locList();
    setupComboListeners();

    mapGesture = new GesturePane();

    mapView = new ImageView(floorMaps.firstFloor);

    Group mapGroup = new Group();
    mapGroup.getChildren().add(mapView);

    iconPane = new Pane();
    mapGroup.getChildren().add(iconPane);
    //    iconPane.setStyle("-fx-background-color: red");
    //  mapStackPane.setStyle("-fx-background-color: blue");

    mapStackPane.getChildren().add(mapGesture);
    mapGesture.setContent(mapGroup);
    mapGesture.setMinScale(0.05);
    mapGesture.setScrollMode(GesturePane.ScrollMode.ZOOM);
    mapGesture.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    mapGesture.setFitMode(GesturePane.FitMode.FIT);

    MenuButton floorSelector = createFloorSelector(mapView);
    mapStackPane.getChildren().add(floorSelector);
    mapStackPane.setAlignment(floorSelector, Pos.BOTTOM_LEFT);

    medEqTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    // Setting up user permissions
    checkPerms();
  }

  /** Methods that populates the equipment search box with equipment from Equipment Table */
  public void equipmentList() {
    ArrayList<MedEquipment> equipmentArray = new ArrayList<MedEquipment>();
    equipmentArray = medEquipmentTable.readTable();
    ArrayList<Integer> equipIDAr = new ArrayList<Integer>();
    for (int i = 0; i < equipmentArray.size(); i++) {
      equipIDAr.add(i, equipmentArray.get(i).getMedID());
    }
    ObservableList<Integer> oList = FXCollections.observableArrayList(equipIDAr);
    equipmentSearchBox.setItems(oList);
  }

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

  /** Method that sets up the event listeners for the searchable combo boxes Called in initialize */
  public void setupComboListeners() {
    //    equipmentSearchBox.setOnAction(
    //        (event) -> {
    //          System.out.println("Error here!!!!!!!!!!!!!!");
    //          System.out.println((Integer)
    // equipmentSearchBox.getSelectionModel().getSelectedItem());
    //          int selectedItem = (Integer)
    // equipmentSearchBox.getSelectionModel().getSelectedItem();
    //          selectedEquipment = selectedItem;
    //        });

    locationSearchBox.setOnAction(
        (event) -> {
          String selectedItem = (String) locationSearchBox.getSelectionModel().getSelectedItem();
          selectedLocation = selectedItem;
        });
  }

  public void onEdit() {
    if (medEqTable.getSelectionModel().getSelectedItem() != null) {
      MedEquipRequest selectedItem =
          (MedEquipRequest) medEqTable.getSelectionModel().getSelectedItem();
      FXMLLoader load = new FXMLLoader((App.class.getResource("views/editEquipmentReqForm.fxml")));
      try {
        AnchorPane editForm = load.load();
        editEquipmentReqFormController edit = load.getController();
        edit.editForm(reqTable.getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();
        stage.setOnCloseRequest(e -> refreshTable());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  private void refreshTable() {
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<MedEquipRequest> mer = new ArrayList<>();
      for (MedEquipRequest mr : MedEquipRequestTbl.getInstance().readTable()) {
        if (mr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          mer.add(mr);
        }
      }
      medEqTable.getItems().setAll(mer);
    } else {
      medEqTable.getItems().setAll(medEquipRequestTbl.readTable());
    }
    equipmentTable.getItems().setAll(medEquipmentTable.readTable());
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
      if (notesField.getText().toUpperCase().contains(s)) {
        return false;
      }
    }
    return (notesField.getText().matches("[\\w\\d\\s\\d.]+") || notesField.getText().isBlank())
        && locationSearchBox.getValue() != null
        && equipmentSearchBox.getValue() != null;
  }

  @FXML
  public void submit() {

    if (validateDataSafe()) {
      int requestNum =
          RequestTable.getInstance().readTable().size() - 1 < 0
              ? 0
              : reqTable.readTable().get(reqTable.readTable().size() - 1).getRequestID() + 1;
      int requesterID = CurrentUser.getUser().getEmpID();
      int itemID =
          (Integer)
              equipmentSearchBox
                  .getSelectionModel()
                  .getSelectedItem(); // equipmentSearchBox.getValue();
      String node = locationSearchBox.getValue();
      String notes = notesField.getText();
      String requestStatus = "not done";

      MedEquipRequest request =
          new MedEquipRequest(
              requestNum, node, requesterID, null, 0, 0, null, "Submitted", notes, itemID);
      reqTable.addEntry(request);
      refreshTable();

      refreshTable();
    } else {
      notesField.setText("Invalid Input");
    }
  }

  /** Method that populates the equipment combo box from the EquipmentTbl Called */
  public void equipList() {
    ArrayList<MedEquipment> medArray = new ArrayList<MedEquipment>();
    medArray = MedEquipmentTbl.getInstance().readTable();
    ArrayList<Integer> medIDAr = new ArrayList<Integer>();
    for (int i = 0; i < medArray.size(); i++) {
      medIDAr.add(i, medArray.get(i).getMedID());
    }
    ObservableList<Integer> oList = FXCollections.observableArrayList(medIDAr);
    equipmentSearchBox.setItems(oList);
  }

  @FXML
  public void backupEquipment() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Equipment File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      medEquipmentTable.createBackup(
          new File(selectedFile.toString() + "\\medEquipmentBackUp.csv"));
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
      MedEquipRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\medEquipmentRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDBEquipment() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Equipment File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      medEquipmentTable.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refreshTable();
  }

  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Equipment Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      MedEquipRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refreshTable();
  }

  @FXML
  public void backup() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up File");

    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {

      System.out.println(selectedFile.toString());
      medEquipRequestTbl.createBackup(
          new File(selectedFile.toString() + "\\medEquipReqBackup.csv"));

    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDB() {
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
      medEquipRequestTbl.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refreshTable();
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

  public MenuButton createFloorSelector(ImageView mapImage) {
    MenuItem floorL2 = new MenuItem("L2");

    MenuItem floorL1 = new MenuItem("L1");

    MenuItem floor01 = new MenuItem("1");
    MenuItem floor02 = new MenuItem("2");
    MenuItem floor03 = new MenuItem("3");

    MenuButton floorSelect =
        new MenuButton("Floor", null, floorL2, floorL1, floor01, floor02, floor03);

    floorL2.setOnAction(
        e -> {
          mapImage.setImage(LL2);
          floorSelect.setText(((MenuItem) e.getSource()).getText());
          setLocations("L2");
        });
    floorL1.setOnAction(
        e -> {
          mapImage.setImage(LL1);
          floorSelect.setText(((MenuItem) e.getSource()).getText());
          setLocations("L1");
        });
    floor01.setOnAction(
        e -> {
          mapImage.setImage(L1);
          floorSelect.setText(((MenuItem) e.getSource()).getText());
          setLocations("1");
        });
    floor02.setOnAction(
        e -> {
          mapImage.setImage(L2);
          floorSelect.setText(((MenuItem) e.getSource()).getText());
          setLocations("2");
        });
    floor03.setOnAction(
        e -> {
          mapImage.setImage(L3);
          floorSelect.setText(((MenuItem) e.getSource()).getText());
          setLocations("3");
        });

    return floorSelect;
  }

  public void setLocations(String floor) {
    iconPane.getChildren().clear();
    currentFloor = floor;

    TableController<Location, String> locations = LocationTbl.getInstance();

    ArrayList<MedEquipRequest> reqList = medEquipRequestTbl.readTable();
    reqList =
        (ArrayList)
            reqList.stream()
                .filter(
                    l -> {
                      String nodeID = reqTable.getEntry(l.getReqID()).getLocationID();
                      Location loc = locations.getEntry(nodeID);
                      if (loc != null) {
                        return (loc.getFloor().equals(floor));
                      }
                      return false;
                    })
                .collect(Collectors.toList());
    System.out.println(reqList);
    for (MedEquipRequest mer : reqList) {
      createIcon(mer);
    }
  }

  public void createIcon(MedEquipRequest mer) {
    MedEquipReqPane placeHolder = new MedEquipReqPane(mer);
    placeHolder.setMinWidth(10);
    placeHolder.setMinHeight(10);

    iconPane.getChildren().add(placeHolder);

    TableController<Location, String> locations = LocationTbl.getInstance();
    Location loc = locations.getEntry(reqTable.getEntry(mer.getReqID()).getLocationID());

    placeHolder.setLayoutX(loc.getXcoord() - 5);
    placeHolder.setLayoutY(loc.getYcoord() - 5);
    placeHolder.setStyle("-fx-background-color: #0063a9");
    placeHolder.setOnMouseEntered(
        e -> {
          placeHolder.setStyle("-fx-background-color: #F6BD39");
          System.out.println(placeHolder.getLayoutX() + " " + placeHolder.getLayoutY());
        });
    placeHolder.setOnMouseExited(
        e -> {
          placeHolder.setStyle("-fx-background-color: black");
        });
    placeHolder.setOnMouseReleased(
        e -> {
          // subController.setText(placeHolder.location);
          System.out.println("clicked");
        });
    // placeHolder.setOnMouseClicked(e -> generateInfo());
  }

  private class MedEquipReqPane extends Pane {
    public MedEquipRequest location = null;

    public MedEquipReqPane(MedEquipRequest mer) {
      super();
      location = mer;
    }

    void setLocation(MedEquipRequest mer) {
      location = mer;
    }
  }
}
