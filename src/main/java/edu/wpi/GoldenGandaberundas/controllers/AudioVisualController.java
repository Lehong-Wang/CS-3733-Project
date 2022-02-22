package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisual;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryTbl;
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
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class AudioVisualController implements Initializable {

  @FXML private TextField patientField;
  @FXML private ChoiceBox<String> priorityBox;
  @FXML private TextArea descriptionBox;
  @FXML private SearchableComboBox<String> deviceIDBox;
  @FXML private SearchableComboBox<String> deviceTypeBox;
  @FXML private SearchableComboBox<String> locationBox;

  @FXML private TableView deviceMenu;
  @FXML private TableColumn<AudioVisual, Integer> deviceID;
  @FXML private TableColumn<AudioVisual, String> type;
  @FXML private TableColumn<AudioVisual, String> locationID;
  @FXML private TableColumn<AudioVisual, String> description;

  @FXML private TableView avRequests;
  @FXML private TableColumn<AudioVisualRequest, Integer> requestID;
  @FXML private TableColumn<AudioVisualRequest, String> nodeID;
  @FXML private TableColumn<AudioVisualRequest, Integer> requesterID;
  @FXML private TableColumn<AudioVisualRequest, Integer> completerID;
  @FXML private TableColumn<AudioVisualRequest, Integer> submittedTime;
  @FXML private TableColumn<AudioVisualRequest, Integer> completedTime;
  @FXML private TableColumn<AudioVisualRequest, Integer> patientID;
  @FXML private TableColumn<AudioVisualRequest, String> status;
  @FXML private TableColumn<AudioVisualRequest, Integer> device;
  @FXML private TableColumn<AudioVisualRequest, String> notes;

  private RequestTable requestTableController = RequestTable.getInstance();
  private TableController menuTableController = AudioVisualTbl.getInstance();
  private TableController requestAVController = AudioVisualRequestTbl.getInstance();

  private TableController locationTableController = LocationTbl.getInstance();

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    priorityBox.getItems().addAll("low", "medium", "high");

    deviceID.setCellValueFactory(new PropertyValueFactory<AudioVisual, Integer>("avID"));
    type.setCellValueFactory(new PropertyValueFactory<AudioVisual, String>("deviceType"));
    locationID.setCellValueFactory(new PropertyValueFactory<AudioVisual, String>("locID"));
    description.setCellValueFactory(new PropertyValueFactory<AudioVisual, String>("description"));

    requestID.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("requestID"));
    nodeID.setCellValueFactory(new PropertyValueFactory<AudioVisualRequest, String>("locationID"));
    requesterID.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("empCompleter"));
    submittedTime.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("timeStart"));
    completedTime.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("timeEnd"));
    patientID.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("patientID"));
    status.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, String>("requestStatus"));
    device.setCellValueFactory(
        new PropertyValueFactory<AudioVisualRequest, Integer>("audioVisualID"));
    notes.setCellValueFactory(new PropertyValueFactory<AudioVisualRequest, String>("notes"));

    // Populating location choice box
    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationBox.setItems(oList);

    // Populating device ID choice box
    ArrayList<String> findList = deviceIDList();
    ObservableList<String> dList = FXCollections.observableArrayList(findList);
    deviceIDBox.setItems(dList);

    // Populating device type choice box
    ObservableList<String> tList = FXCollections.observableArrayList(deviceTypeList());
    deviceTypeBox.setItems(tList);

    avRequests.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    refresh();
  }

  // refresh tables
  public void refresh() {
    menuTableController = AudioVisualTbl.getInstance();
    requestAVController = AudioVisualRequestTbl.getInstance();
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<AudioVisualRequest> avrs = new ArrayList<>();
      for (AudioVisualRequest avr : AudioVisualRequestTbl.getInstance().readTable()) {
        if (avr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          avrs.add(avr);
        }
      }
      avRequests.getItems().setAll(avrs);
    } else {
      avRequests.getItems().setAll(requestAVController.readTable());
    }

    deviceMenu.getItems().setAll(menuTableController.readTable());
  }

  void onEdit() {
    if (avRequests.getSelectionModel().getSelectedItem() != null) {
      AudioVisualRequest selectedItem =
          (AudioVisualRequest) avRequests.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load =
            new FXMLLoader(App.class.getResource("views/editAudioVisualReqForm.fxml"));
        AnchorPane editForm = load.load();
        editAudioVisualReqFormController edit = load.getController();
        edit.editForm(RequestTable.getInstance().getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(selectedItem.getPK());
    }
  }

  public void submit() {
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
    int deviceID = Integer.parseInt(deviceIDBox.getValue());
    int patientID = Integer.parseInt(patientField.getText());
    String note = descriptionBox.getText();
    String priority = priorityBox.getValue();

    AudioVisualRequest AVrequest =
        new AudioVisualRequest(
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
    System.out.println(AVrequest);
    AudioVisualRequestTbl.getInstance().addEntry(AVrequest);
    clear();
    refresh();
  }

  /*
   * clears the submissions fields
   */
  public void clear() {
    patientField.setText("");
    priorityBox.getItems().clear();
    priorityBox.getItems().addAll("low", "medium", "high");
    descriptionBox.setText("");
    locationBox.getItems().clear();

    // Populating location choice box
    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationBox.setItems(oList);

    // Populating device ID choice box
    deviceIDBox.getItems().clear();
    ArrayList<String> findList = deviceIDList();
    ObservableList<String> dList = FXCollections.observableArrayList(findList);
    deviceIDBox.setItems(dList);

    // Populating device type choice box
    deviceTypeBox.getItems().clear();
    ObservableList<String> tList = FXCollections.observableArrayList(deviceTypeList());
    deviceTypeBox.setItems(tList);
    refresh();
  }

  /** creates backups of the request table */
  @FXML
  public void backupAV() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Audio Visual File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      LaundryTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\audioVisualBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
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
   * populates the deviceID list
   *
   * @return arraylist of the device IDs as strings
   */
  public ArrayList<String> deviceIDList() {
    ArrayList<AudioVisual> deviceArray = new ArrayList<AudioVisual>();
    deviceArray = menuTableController.readTable();
    ArrayList<String> AvArray = new ArrayList<String>();

    for (int i = 0; i < deviceArray.size(); i++) {
      AudioVisual av = deviceArray.get(i);
      AvArray.add(i, av.getAvID() + "");
    }
    return AvArray;
  }

  /**
   * populates the device type search box
   *
   * @return
   */
  public ArrayList<String> deviceTypeList() {
    ArrayList<AudioVisual> deviceArray = new ArrayList<AudioVisual>();
    deviceArray = menuTableController.readTable();
    ArrayList<String> AvArray = new ArrayList<String>();

    for (int i = 0; i < deviceArray.size(); i++) {
      AudioVisual av = deviceArray.get(i);
      AvArray.add(i, av.getDeviceType());
    }
    return AvArray;
  }
}
