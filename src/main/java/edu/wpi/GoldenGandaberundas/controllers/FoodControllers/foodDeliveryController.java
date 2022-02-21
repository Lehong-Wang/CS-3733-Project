package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.Food;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.controlsfx.control.SearchableComboBox;

public class foodDeliveryController {

  @FXML TextField quantityField;

  @FXML TableView foodMenuTable;
  @FXML TableColumn<Food, Integer> foodID;
  @FXML TableColumn<Food, String> foodNameMenu;
  @FXML TableColumn<Food, Double> price;
  @FXML TableColumn<Food, Boolean> inStock;
  @FXML TableColumn<Food, String> ingredients;
  @FXML TableColumn<Food, Integer> calories;
  @FXML TableColumn<Food, String> allergens;
  @FXML TableColumn<Food, String> foodType;

  @FXML TableView menuTable;
  @FXML TableColumn<FoodMenuItem, String> foodNameCart;
  @FXML TableColumn<FoodMenuItem, Integer> quantityCart;
  ArrayList<FoodMenuItem> currentMenu = new ArrayList<>();

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
    // price.setCellValueFactory(new PropertyValueFactory<Food, Double>("price"));
    inStock.setCellValueFactory(new PropertyValueFactory<Food, Boolean>("inStock"));
    foodNameMenu.setCellValueFactory(new PropertyValueFactory<Food, String>("foodName"));
    ingredients.setCellValueFactory(new PropertyValueFactory<Food, String>("ingredients"));
    calories.setCellValueFactory(new PropertyValueFactory<Food, Integer>("calories"));
    allergens.setCellValueFactory(new PropertyValueFactory<Food, String>("allergens"));

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

    foodNameCart.setCellValueFactory(new PropertyValueFactory<FoodMenuItem, String>("foodName"));
    quantityCart.setCellValueFactory(new PropertyValueFactory<FoodMenuItem, Integer>("quantity"));

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

    menuTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            removeOneFromCart();
          }
        });

    refresh();
  }

  public void refresh() {
    ArrayList<Food> filteredFoods = FoodTbl.getInstance().readTable();

    foodMenuTable.getItems().setAll(filteredFoods);
    foodRequestsTable.getItems().setAll(FoodRequestTbl.getInstance().readTable());
    menuTable.getItems().setAll(currentMenu);
  }

  public void addToCart() {
    if (foodMenuTable.getSelectionModel().getSelectedItem() != null) {
      Food addFood = (Food) foodMenuTable.getSelectionModel().getSelectedItem();
      for (FoodMenuItem fm : currentMenu) {
        if (fm.getFoodItem().equals(addFood)) {
          fm.increaseQuantity();
          refresh();
          return;
        }
      }
      FoodMenuItem menuItem = new FoodMenuItem(addFood, 1);
      currentMenu.add(menuItem);
      refresh();
    }
  }

  public void removeFromCart() {
    if (menuTable.getSelectionModel().getSelectedItem() != null) {
      currentMenu.remove(menuTable.getSelectionModel().getSelectedItem());
    }
    refresh();
  }

  public void removeOneFromCart() {
    if (menuTable.getSelectionModel().getSelectedItem() != null) {
      ((FoodMenuItem) menuTable.getSelectionModel().getSelectedItem()).decreaseQuantity();
    }
    refresh();
  }

  public void submit() {
    int requestNum =
        RequestTable.getInstance().readTable().size() - 1 < 0
            ? 0
            : RequestTable.getInstance()
                    .readTable()
                    .get(RequestTable.getInstance().readTable().size() - 1)
                    .getRequestID()
                + 1;
    int requesterID = CurrentUser.getUser().getEmpID();
    int patientID = patientComboBox.getValue();
    String location = locationComboBox.getValue();
    String notes = noteField.getText();
    for (FoodMenuItem fm : currentMenu) {
      FoodRequest foodRequest =
          new FoodRequest(
              requestNum,
              location,
              requesterID,
              null,
              0,
              0,
              patientID,
              "Submitted",
              notes,
              fm.getFoodItem().getFoodID(),
              fm.getQuantity());
      RequestTable.getInstance().addEntry(foodRequest);
    }
    currentMenu.clear();
    refresh();
  }
}
