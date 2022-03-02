package edu.wpi.CS3733.c22.teamG.controllers;

import edu.wpi.CS3733.c22.teamG.CurrentUser;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Employee;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class masterRequestTableController {

  CategoryAxis statuses = new CategoryAxis();
  NumberAxis totalRequests = new NumberAxis();
  @FXML VBox adminViewVBox;
  @FXML ComboBox<String> requestComboBox;
  @FXML ComboBox<String> floorComboBox;
  @FXML SearchableComboBox<String> employeeComboBox;

  @FXML TableView masterRequestsTable;
  @FXML TableColumn<Request, Integer> requestID;
  @FXML TableColumn<Request, String> locationID;
  @FXML TableColumn<Request, Integer> timeStart;
  @FXML TableColumn<Request, Integer> timeEnd;
  @FXML TableColumn<Request, Integer> patientID;
  @FXML TableColumn<Request, Integer> empInitiated;
  @FXML TableColumn<Request, Integer> empCompleter;
  @FXML TableColumn<Request, String> requestStatus;
  @FXML TableColumn<Request, String> notes;
  @FXML TableColumn<Request, String> requestType;
  @FXML BarChart<String, Number> barChart = new BarChart<String, Number>(statuses, totalRequests);
  @FXML PieChart pieChart;

  mainController main = null;
  String selectedFloor = "00";
  String currentRequestFilter = "All Requests";
  Integer selectedEmployee = -1;
  boolean[] currentChartFilter = {false, false, false};

  /**
   * The "constructor" for the masterRequestTable page, required to be run upon initialization so
   * that the page can switch to other pages
   *
   * @param realMain - The main controller for the main page
   */
  @FXML
  public void setMainControler(mainController realMain) {
    this.main = realMain;
  }

  @FXML
  public void initialize() {
    //    barChart.setAnimated(false);
    //    pieChart.setAnimated(false);
    adminViewVBox.setViewOrder(0.0);
    barChart.setTitle("Request Statuses");
    statuses.setLabel("Request Status");
    totalRequests.setLabel("Number of Requests");
    pieChart.setTitle("Request Statuses");

    requestID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("requestID"));
    locationID.setCellValueFactory(new PropertyValueFactory<Request, String>("locationID"));
    timeStart.setCellValueFactory(new PropertyValueFactory<Request, Integer>("timeStart"));
    timeEnd.setCellValueFactory(new PropertyValueFactory<Request, Integer>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("patientID"));
    empInitiated.setCellValueFactory(new PropertyValueFactory<Request, Integer>("empInitiated"));
    empCompleter.setCellValueFactory(new PropertyValueFactory<Request, Integer>("empCompleter"));
    requestStatus.setCellValueFactory(new PropertyValueFactory<Request, String>("requestStatus"));
    notes.setCellValueFactory(new PropertyValueFactory<Request, String>("notes"));
    requestType.setCellValueFactory(new PropertyValueFactory<Request, String>("requestType"));

    ArrayList<String> requestTypes = new ArrayList<String>();
    requestTypes.add("All Requests");
    requestTypes.add("MedEquipDelivery");
    requestTypes.add("MedicineDelivery");
    requestTypes.add("GiftFloral");
    requestTypes.add("LaundryService");
    requestTypes.add("Food");
    requestTypes.add("Computer");
    requestTypes.add("AudioVisual");
    requestComboBox.setItems(FXCollections.observableArrayList(requestTypes));

    ArrayList<String> floorTypes = new ArrayList<>();
    floorTypes.add("All Floors");
    floorTypes.add("L2");
    floorTypes.add("L1");
    floorTypes.add("01");
    floorTypes.add("02");
    floorTypes.add("03");
    floorComboBox.setItems(FXCollections.observableArrayList(floorTypes));

    ArrayList<String> employeeIDs = new ArrayList<>();
    employeeIDs.add("All Employees");
    for (Employee e : EmployeeTbl.getInstance().readTable()) {
      employeeIDs.add(String.valueOf(e.getEmpID()));
    }

    employeeComboBox.setItems(FXCollections.observableArrayList(employeeIDs));

    masterRequestsTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            goToOtherPage();
          }
        });

    refresh();
  }

  /** Function used when the filter button is pressed in order to filter all of the data. */
  @FXML
  public void updateFilter() {
    updateFloors();
    updateEmployees();
    updateRequests();
    refresh();
  }

  /** The function used by the filter button to check the employeeComboBox for a value */
  @FXML
  public void updateEmployees() {
    if (employeeComboBox.getValue() != null) {
      if (employeeComboBox.getValue().equals("All Employees")) {
        selectedEmployee = -1;
        currentChartFilter[2] = false;
      } else {
        selectedEmployee = Integer.parseInt(employeeComboBox.getValue());
        currentChartFilter[2] = true;
      }
    }
  }

  /** The function used by the filter button to check the floorComboBox for a value */
  @FXML
  public void updateFloors() {
    if (floorComboBox.getValue() != null) {
      if (floorComboBox.getValue().equals("All Floors")) {
        selectedFloor = "00";
        currentChartFilter[1] = false;
      } else {
        selectedFloor = floorComboBox.getValue();
        currentChartFilter[1] = true;
      }
    }
  }

  /** The function used by the filter button to check the requestComboBox for a value */
  @FXML
  public void updateRequests() {
    if (requestComboBox.getValue() != null) {
      if (requestComboBox.getValue().equals("All Requests")) {
        currentChartFilter[0] = false;
      } else {
        currentChartFilter[0] = true;
      }
      currentRequestFilter = requestComboBox.getValue();
    } else {
      currentRequestFilter = "All Requests";
    }
  }

  @FXML
  public void refresh() {
    barChart.getData().clear();
    pieChart.getData().clear();
    barChart.setAnimated(true);
    pieChart.setAnimated(true);

    // Filters the request by person if its not by admin.
    ArrayList<Request> filteredRequests = new ArrayList<>();
    if (CurrentUser.getUser().getEmpID() != 0) {
      for (Request r : RequestTable.getInstance().readTable()) {
        if (r.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          filteredRequests.add(r);
        }
      }
      masterRequestsTable
          .getItems()
          .setAll(filterByEmployee(filterByRequest(filterByFloor(filteredRequests))));
      employeeComboBox.setManaged(false);
    } else {
      masterRequestsTable
          .getItems()
          .setAll(
              filterByEmployee(
                  filterByRequest(filterByFloor(RequestTable.getInstance().readTable()))));
    }

    int total = 0;

    // An array list of the different requests types to filter by
    ArrayList<String> requestTypes = new ArrayList<String>();
    requestTypes.add("MedEquipDelivery");
    requestTypes.add("MedicineDelivery");
    requestTypes.add("GiftFloral");
    requestTypes.add("LaundryService");
    requestTypes.add("Food");
    requestTypes.add("Computer");
    requestTypes.add("AudioVisual");

    // An array list of the different floors to compare each request to
    ArrayList<String> locationTypes = new ArrayList<String>();
    locationTypes.add("L2");
    locationTypes.add("L1");
    locationTypes.add("01");
    locationTypes.add("02");
    locationTypes.add("03");

    // An array lst of status types to compare each request to
    ArrayList<String> statusTypes = new ArrayList<String>();
    statusTypes.add("Submitted");
    statusTypes.add("In_Progress");
    statusTypes.add("Completed");

    XYChart.Series series = new XYChart.Series();
    series.setName("Request Status");
    ArrayList<PieChart.Data> pieData = new ArrayList<>();

    //    FILTERING REQUEST         FILTERING LOCATION        FILTERING EMPLOYEE
    if (!currentChartFilter[0] && !currentChartFilter[1] && !currentChartFilter[2]) { // no filter
      for (String s : requestTypes) {
        total = 0;
        for (Request r : RequestTable.getInstance().readTable()) {
          if (r.getRequestType().equalsIgnoreCase(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    } else if (!currentChartFilter[0]
        && currentChartFilter[1]
        && !currentChartFilter[2]) { // filter by floor
      for (String s : requestTypes) {
        total = 0;
        for (Request r : filterByFloor(RequestTable.getInstance().readTable())) {
          if (r.getRequestType().equalsIgnoreCase(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    } else if (!currentChartFilter[0]
        && !currentChartFilter[1]
        && currentChartFilter[2]) { // filter by employee
      for (String s : requestTypes) {
        total = 0;
        for (Request r : filterByEmployee(RequestTable.getInstance().readTable())) {
          if (r.getRequestType().equalsIgnoreCase(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    } else if (!currentChartFilter[0]
        && currentChartFilter[1]
        && currentChartFilter[2]) { // filter by floor and employee
      for (String s : requestTypes) {
        total = 0;
        for (Request r : filterByFloor(filterByEmployee(RequestTable.getInstance().readTable()))) {
          if (r.getRequestType().equalsIgnoreCase(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    } else if (currentChartFilter[0]
        && !currentChartFilter[1]
        && !currentChartFilter[2]) { // filter by request
      System.out.println("Here");
      for (String s : locationTypes) {
        total = 0;
        for (Request r : filterByRequest(RequestTable.getInstance().readTable())) {
          if (r.getLocationID().endsWith(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    } else if (currentChartFilter[0]
        && !currentChartFilter[1]
        && currentChartFilter[2]) { // filter by request and employee
      for (String s : locationTypes) {
        total = 0;
        for (Request r :
            filterByEmployee(filterByRequest(RequestTable.getInstance().readTable()))) {
          if (r.getLocationID().endsWith(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    } else if (currentChartFilter[0]
        && currentChartFilter[1]
        && !currentChartFilter[2]) { // filter by request and floor
      for (Employee e : EmployeeTbl.getInstance().readTable()) {
        total = 0;
        for (Request r : filterByFloor(filterByRequest(RequestTable.getInstance().readTable()))) {
          if (r.getEmpInitiated().equals(e.getEmpID())) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(String.valueOf(e.getEmpID()), total));
          pieData.add(new PieChart.Data(String.valueOf(e.getEmpID()) + " " + total, total));
        }
      }
    } else { // filtered by request, location and employee
      for (String s : statusTypes) {
        total = 0;
        for (Request r :
            filterByFloor(
                filterByEmployee(filterByRequest(RequestTable.getInstance().readTable())))) {
          if (r.getRequestStatus().equals(s)) {
            total++;
          }
        }
        if (total != 0) {
          series.getData().add(new XYChart.Data(s, total));
          pieData.add(new PieChart.Data(s + " " + total, total));
        }
      }
    }

    //    for (String s : requestTypes) {
    //      total = 0;
    //      for (Request r : RequestTable.getInstance().readTable()) {
    //        if (selectedFloor.equals("00")) {
    //          if (r.getRequestType().equalsIgnoreCase(s)) {
    //            total++;
    //          }
    //        } else {
    //          if (r.getRequestType().equalsIgnoreCase(s) &&
    // r.getLocationID().endsWith(selectedFloor)) {
    //            total++;
    //          }
    //        }
    //      }
    //      series.getData().add(new XYChart.Data(s, total));
    //      pieData.add(new PieChart.Data(s + " " + total, total));
    //    }

    barChart.getData().setAll(series);
    pieChart.setData(FXCollections.observableArrayList(pieData));
    barChart.setAnimated(false);
    pieChart.setAnimated(false);

    for (Node n : barChart.lookupAll(".default-color0.chart-bar")) {
      n.setStyle("-fx-bar-fill: #5762FF;");
    }

    for (Node n : barChart.lookupAll("Label.chart-legend-item")) {
      Label tempLabel = (Label) n;
      if (tempLabel.getText().equals("Request Status")) {
        tempLabel.getGraphic().setStyle("-fx-bar-fill: #5762FF;");
      }
    }
  }

  /**
   * Takes in an arraylist of requests and filters it by the current employee chosen. If
   * selectedEmployee is -1, treats it as no filter and returns the inputted arraylist
   *
   * @param requests - An arraylist of requests
   * @return The inputted arraylist filtered by employees
   */
  private ArrayList<Request> filterByEmployee(ArrayList<Request> requests) {
    ArrayList<Request> filter = new ArrayList<Request>();
    if (selectedEmployee != -1) {
      for (Request r : requests) {
        if (Objects.equals(r.getEmpInitiated(), selectedEmployee)) {
          filter.add(r);
          System.out.println(r);
        }
      }
      return filter;
    } else return requests;
  }

  /**
   * Takes in an arraylist of requests and filters it by the current request chosen. If
   * currentRequestFilter is "All Requests", treats it as no filter and returns the inputted
   * arraylist
   *
   * @param requests - An arraylist of requests
   * @return The inputted arraylist filtered by request type
   */
  private ArrayList<Request> filterByRequest(ArrayList<Request> requests) {
    ArrayList<Request> filter = new ArrayList<Request>();
    if (!currentRequestFilter.equals("All Requests")) {
      for (Request r : requests) {
        if (r.getRequestType().equals(currentRequestFilter)) filter.add(r);
      }
      return filter;
    } else return requests;
  }

  /**
   * Takes in an arraylist of requests and filters it by the current floor chosen. If selectedFloor
   * is 00, treats it as no filter and returns the inputted arraylist
   *
   * @param requests - An arraylist of requests
   * @return The inputted arraylist filtered by floor type
   */
  private ArrayList<Request> filterByFloor(ArrayList<Request> requests) {
    ArrayList<Request> filter = new ArrayList<Request>();
    if (!selectedFloor.equals("00")) {
      for (Request r : requests) {
        if (r.getLocationID().endsWith(selectedFloor)) filter.add(r);
      }
      return filter;
    } else return requests;
  }

  /** Changes the page based on what request was doubled clicked in the masters request table */
  private void goToOtherPage() {
    if (masterRequestsTable.getSelectionModel().getSelectedItem() != null) {
      Request currRequest = (Request) masterRequestsTable.getSelectionModel().getSelectedItem();
      String service = currRequest.getRequestType();
      System.out.println(service);
      try {
        switch (service) {
          case "MedEquipDelivery":
            main.switchMedicalEquipment(null);
            break;
          case "MedicineDelivery":
            main.switchMedicineDelivery(null);
            break;
          case "GiftFloral":
            main.switchGiftFloral(null);
            break;
          case "LaundryService":
            main.switchLaundry(null);
            break;
          case "Food":
            main.switchFood(null);
            break;
          case "Computer":
            main.switchCompService(null);
            break;
          case "AudioVisual":
            main.switchAudioVisual(null);
            break;
          default:
            main.goHome(null);
            break;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
