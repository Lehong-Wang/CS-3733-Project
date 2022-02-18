package edu.wpi.GoldenGandaberundas.controllers.EquipmentControllers;

import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.validators.AddEquipmentRequestValidator;
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
import javafx.event.ActionEvent;
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

public class EquipmentDeliveryController {

  @FXML TableView medEqTable;
  @FXML TableColumn<MedEquipRequest, Integer> reqID;
  @FXML TableColumn<MedEquipRequest, Integer> empID;
  @FXML TableColumn<MedEquipRequest, Integer> medID;
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

  @FXML TextField reqField;
  @FXML TextField itemField;
  @FXML TextField destinationField;
  @FXML TextField notesField;

  @FXML private StackPane mapStackPane;
  private GesturePane mapGesture = null;
  private ImageView mapView = null;
  private Pane iconPane = null;
  private String currentFloor = null;

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  private TableController medEquipRequestTbl = MedEquipRequestTbl.getInstance();
  private RequestTable reqTable = RequestTable.getInstance();
  private MedEquipmentTbl medEquipmentTable = MedEquipmentTbl.getInstance();

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
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void refreshTable() {
    // if (medEquipRequestTbl.readTable() != null) {
    medEqTable.getItems().setAll(medEquipRequestTbl.readTable());
    equipmentTable.getItems().setAll(medEquipmentTable.readTable());
    // }
  }

  @FXML
  public void refreshTable(ActionEvent actionEvent) throws IOException {
    medEqTable.getItems().setAll(medEquipRequestTbl.readTable());
  }

  @FXML
  public void submit() {

    AddEquipmentRequestValidator valid =
        new AddEquipmentRequestValidator(
            reqField.getText(),
            itemField.getText(),
            destinationField.getText(),
            notesField.getText(),
            "yes");
    if (valid.validateTextFields()) {

      int requestNum = reqTable.readTable().size() + 1;
      int requesterID = Integer.parseInt(reqField.getText());
      int itemID = Integer.parseInt(itemField.getText());
      String node = destinationField.getText();
      String notes = notesField.getText();
      String requestStatus = "not done";

      MedEquipRequest request =
          new MedEquipRequest(
              requestNum, node, requesterID, 123, 0, 0, 111, "Submitted", notes, itemID);
      reqTable.addEntry(request);
      refreshTable();
    } else {
      reqField.setText("Invalid Data");
      itemField.setText("Invalid Data");
      destinationField.setText("Invalid Data");
      notesField.setText("Invalid Data");
    }
    refreshTable();
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
