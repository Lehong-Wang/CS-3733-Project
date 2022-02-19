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
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
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
  @FXML ComboBox<String> problemTypeBox;

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
    patientID.setCellValueFactory(new PropertyValueFactory<ComputerRequest, Integer>("patientID"));
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

    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationSearchBox.setItems(oList);

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

    refresh();
  }

  /**
   * Gets the table from the database for computers and computer requests and sets the table views
   * to those values.
   */
  public void refresh() {
    computersTable.getItems().setAll(computers.readTable());
    computerRequestsTbl.getItems().setAll(computerRequests.readTable());
  }

  public void backupDB() {}

  public void load() {}

  public void submit() {
    int requestID = RequestTable.getInstance().readTable().get(
            RequestTable.getInstance().readTable().size()-1
    ).getRequestID();
    
  }

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
