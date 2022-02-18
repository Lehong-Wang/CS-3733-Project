package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.Computer;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class computerServiceController implements Initializable {

  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML JFXButton currProblemsBtn; // Cool Sliding Button
  @FXML SearchableComboBox<Integer> deviceSearchBox;

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

  @FXML private SearchableComboBox locationSearchBox; // Location drop box

  TableController locationTableController = LocationTbl.getInstance();
  ComputerTbl computers = ComputerTbl.getInstance();
  ComputerRequestTbl computerRequests = ComputerRequestTbl.getInstance();

  String locations = ""; // String for location of request, value is gotten from location drop down

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    computerID.setCellValueFactory(new PropertyValueFactory<Computer, Integer>("computerID"));
    computerType.setCellValueFactory(new PropertyValueFactory<Computer, String>("computerType"));
    os.setCellValueFactory(new PropertyValueFactory<Computer, String>("os"));
    processor.setCellValueFactory(new PropertyValueFactory<Computer, String>("processor"));
    hostName.setCellValueFactory(new PropertyValueFactory<Computer, String>("hostName"));
    model.setCellValueFactory(new PropertyValueFactory<Computer, String>("model"));
    manufacturer.setCellValueFactory(new PropertyValueFactory<Computer, String>("manufacturer"));
    serialNumber.setCellValueFactory(new PropertyValueFactory<Computer, String>("serialNumber"));

    locationSearchBox.setOnAction(
        (event) -> {
          String selectedItem = (String) locationSearchBox.getSelectionModel().getSelectedItem();
          locations = selectedItem;
        });

    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationSearchBox.setItems(oList);

    ArrayList<Integer> computerIDs = new ArrayList<>();
    for (Computer c : ComputerTbl.getInstance().readTable()) {
      computerIDs.add(c.getComputerID());
    }
    ObservableList<Integer> ocomputerIDs = FXCollections.observableArrayList(computerIDs);
    deviceSearchBox.setItems(ocomputerIDs);

    refresh();
  }

  public void refresh() {
    computersTable.getItems().setAll(computers.readTable());
    computerRequestsTbl.getItems().setAll(computerRequests.readTable());
  }

  public void backupDB() {}

  public void load() {}

  public void submit() {}

  public void clear() {}

  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
      // locationSearchBox.getItems().add(locArray.get(i).getNodeID());
      System.out.println(locNodeAr.get(i));
    }
    return locNodeAr;
  }
}
