package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.Main;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class mainController {
  @FXML Button ComputerServiceButton;
  @FXML Button EmployeeDBButton;
  @FXML Button FloorEditButton;
  @FXML Button FoodButton;
  @FXML Button GiftFloralButton;
  @FXML Button LanguageButton;
  @FXML Button LaundryButton;
  @FXML Button MedicineDeliveryButton;
  @FXML Button MedicalEquipmentButton;
  @FXML Button PatientTransportButton;
  @FXML Button ReligiousButton;

  @FXML FXMLLoader loader = new FXMLLoader();
  @FXML ImageView mapView;
  @FXML MenuItem floor3;
  @FXML MenuItem floor2;
  @FXML MenuItem floor1;
  @FXML MenuItem groundFloor;
  @FXML MenuItem lower1;
  @FXML MenuItem lower2;
  @FXML MenuButton floorSelectionMenu;
  // @FXML MenuItem exitButton;

  @FXML AnchorPane nodeDataPane;

  public void setFloor3() {

    mapView.setImage(floorMaps.thirdFloor);
  }

  public void setFloor2() {
    mapView.setImage(floorMaps.secondFloor);
  }

  public void setFloor1() {
    mapView.setImage(floorMaps.firstFloor);
  }

  public void setLower1() {
    mapView.setImage(floorMaps.lower1Floor);
  }

  public void setLower2() {
    mapView.setImage(floorMaps.lower2Floor);
  }

  public void nodeSwitch(String fxmlFile) {
    FXMLLoader subControllerLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
    try {
      BorderPane subPane = (BorderPane) subControllerLoader.load();
      AnchorPane.setTopAnchor(subPane, 0.0);
      AnchorPane.setBottomAnchor(subPane, 0.0);
      AnchorPane.setLeftAnchor(subPane, 0.0);
      AnchorPane.setRightAnchor(subPane, 0.0);

      System.out.println(nodeDataPane.getWidth());
      subPane.setPrefHeight(nodeDataPane.getHeight());
      subPane.setPrefWidth(nodeDataPane.getWidth());

      // nodeDataPane.getLayoutBounds().getHeight();
      nodeDataPane.getChildren().add(subPane);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  // gows to the Map viewer
  public void switchMapView(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/mapView.fxml"))));
  }

  // goes to the Computer Services page
  public void switchCompService(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
    stage.setScene(
        new Scene(loader.load(App.class.getResource("views/computerServiceRequest.fxml"))));
  }

  // goes to the Employee Database page
  public void switchEmployeeDB(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) EmployeeDBButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/employeeDB.fxml"))));
  }

  // goes to the Floor Editor page
  public void switchFloorEdit(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) FloorEditButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/locationTable.fxml"))));
  }

  // goes to the Food Delivery page
  public void switchFood(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) FoodButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/foodDelivery.fxml"))));
  }

  // goes to the Gift/Floral Delivery  page
  public void switchGiftFloral(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) GiftFloralButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/giftFloralService.fxml"))));
  }

  // goes to the Language Interpreter page
  public void switchLanguage(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) LanguageButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/languageService.fxml"))));
  }
  // goes to the Laundry Services page
  public void switchLaundry(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) LaundryButton.getScene().getWindow();
    // stage.setScene(new
    // Scene(loader.load(App.class.getResource("views/laundryServiceChecks.fxml"))));
    stage.setScene(
        new Scene(loader.load(App.class.getResource("adminViews/adminLaundryService.fxml"))));
  }

  // goes to the Medical Equipment  page
  public void switchMedicalEquipment(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) MedicalEquipmentButton.getScene().getWindow();
    stage.setScene(
        new Scene(loader.load(App.class.getResource("views/equipmentInventoryTable.fxml"))));
  }

  // goes to the Medicine Delivery page
  public void switchMedicineDelivery(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) MedicineDeliveryButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/medicineDelivery.fxml"))));
  }

  // goes to the Patient Transportation page
  public void switchPatientTransport(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) PatientTransportButton.getScene().getWindow();
    stage.setScene(
        new Scene(loader.load(App.class.getResource("views/patientTransportService.fxml"))));
  }
  // goes to the Religious Request page
  public void switchReligious(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) ReligiousButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/religiousServices.fxml"))));
  }

  // Goes to the location table
  public void switchLocationTable(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
    stage.setScene(new Scene(loader.load(App.class.getResource("views/locationTable.fxml"))));
  }

  public void closeApp() {
    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
    stage.close();
  }
}
