package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.Food;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class foodDeliveryController {

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

  @FXML
  public void initialize() {

    foodID.setCellValueFactory(new PropertyValueFactory<Food,Integer>("foodID"));

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
  }
}
