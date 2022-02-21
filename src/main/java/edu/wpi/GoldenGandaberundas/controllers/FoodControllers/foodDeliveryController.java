package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.Food;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.SearchableComboBox;

public class foodDeliveryController {

  @FXML TableView menuTable;
  @FXML TableColumn<Food, String> foodName;

  @FXML TableView foodMenuTable;
  @FXML TableColumn<Food, Integer> foodID;
  @FXML TableColumn<Food, String> foodType;
  @FXML TableColumn<Food, Double> price;
  @FXML TableColumn<Food, Boolean> inStock;

  @FXML TableView foodRequestsTable;
  @FXML TableColumn<FoodRequest, Integer> requestID;
  @FXML TableColumn<FoodRequest, String> locationID;
  @FXML TableColumn<FoodRequest, Integer> timeStart;
  @FXML TableColumn<FoodRequest, Integer> timeEnd;
  @FXML TableColumn<FoodRequest, Integer> patientID;
  @FXML TableColumn<FoodRequest, Integer> empInitiated;
  @FXML TableColumn<FoodRequest, Integer> empCompleter;
  @FXML TableColumn<FoodRequest, String> requestStatus;
  @FXML TableColumn<FoodRequest, String> notes;
  @FXML TableColumn<FoodRequest, Integer> foodTblID;
  @FXML TableColumn<FoodRequest, Integer> quantity;

  @FXML SearchableComboBox<Integer> patientComboBox;
  @FXML SearchableComboBox<String> locationComboBox;
  @FXML TextArea noteField;

  @FXML
  public void initialize() {

    foodID.setCellValueFactory(new PropertyValueFactory<Food, Integer>("foodID"));
    foodType.setCellValueFactory(new PropertyValueFactory<Food, String>("foodType"));
    //price.setCellValueFactory(new PropertyValueFactory<Food, Double>("price"));
    inStock.setCellValueFactory(new PropertyValueFactory<Food, Boolean>("inStock"));

    /**
     * Sets the cell value factories for the requests table each of the cell value factories make a
     * new property value factory which takes in the exact name of the value in the class of
     * foodrequest
     */
    requestID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("requestID"));
    locationID.setCellValueFactory(new PropertyValueFactory<FoodRequest, String>("locationID"));
    timeStart.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("timeStart"));
    timeEnd.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("patientID"));
    empInitiated.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, Integer>("empInitiated"));
    empCompleter.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, Integer>("empCompleter"));
    requestStatus.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, String>("requestStatus"));
    notes.setCellValueFactory(new PropertyValueFactory<FoodRequest, String>("notes"));
    foodTblID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("foodID"));
    quantity.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("quantity"));

    ArrayList<Integer> patientIDs = new ArrayList<Integer>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));

    ArrayList<String> locations = new ArrayList<String>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    refresh();
  }

  public void refresh() {
    foodMenuTable.getItems().setAll(FoodTbl.getInstance().readTable());
    foodRequestsTable.getItems().setAll(FoodRequestTbl.getInstance().readTable());
  }

  public void addToCart() {
    if (foodMenuTable.getSelectionModel().getSelectedItem() != null) {
      Food addFood = (Food) foodMenuTable.getSelectionModel().getSelectedItem();
    }
  }
}
