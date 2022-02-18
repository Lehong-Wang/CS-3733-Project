package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.Main;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class mainTwoController {

  @FXML AnchorPane nodeDataPane;
  @FXML JFXDrawer drawer;
  @FXML VBox drawerBox;
  @FXML FXMLLoader loader = new FXMLLoader();

  @FXML JFXButton ComputerServiceButton;
  @FXML JFXButton EmployeeDBButton;
  @FXML JFXButton AudioVisualButton;
  @FXML JFXButton FoodButton;
  @FXML JFXButton GiftFloralButton;
  @FXML JFXButton LanguageButton;
  @FXML JFXButton LaundryButton;
  @FXML JFXButton MedicineDeliveryButton;
  @FXML JFXButton MedicalEquipmentButton;
  @FXML JFXButton PatientTransportButton;
  @FXML JFXButton ReligiousButton;
  @FXML JFXButton MapViewButton;
  @FXML Button SideViewButton;
  @FXML JFXButton HomeButton;

  @FXML ImageView mainView;
  @FXML BorderPane homePageNode;

  boolean disabledAuthors = true;

  // CSS Style strings, used to style drawer buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59;-fx-alignment: center-left;";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left;";

  public void initialize() {
    // BUTTONS HIDDEN TILL IMPLEMENTATION
    FoodButton.setManaged(false);
    FoodButton.setVisible(false);
    PatientTransportButton.setManaged(false);
    PatientTransportButton.setVisible(false);
    ReligiousButton.setManaged(false);
    ReligiousButton.setVisible(false);
    // Add in drawer lines
    drawer.setSidePane(drawerBox);
    drawer.setMiniDrawerSize(36);
    buttonStyle(ComputerServiceButton);
    buttonStyle(EmployeeDBButton);
    buttonStyle(AudioVisualButton);
    buttonStyle(FoodButton);
    buttonStyle(GiftFloralButton);
    buttonStyle(LanguageButton);
    buttonStyle(LaundryButton);
    buttonStyle(MedicineDeliveryButton);
    buttonStyle(MedicalEquipmentButton);
    buttonStyle(PatientTransportButton);
    buttonStyle(ReligiousButton);
    buttonStyle(MapViewButton);
    buttonStyle(HomeButton);
    ComputerServiceButton.setText("");
    EmployeeDBButton.setText("");
    AudioVisualButton.setText("");
    FoodButton.setText("");
    GiftFloralButton.setText("");
    LanguageButton.setText("");
    LaundryButton.setText("");
    MedicineDeliveryButton.setText("");
    MedicalEquipmentButton.setText("");
    PatientTransportButton.setText("");
    ReligiousButton.setText("");
    MapViewButton.setText("");
    HomeButton.setText("");
    SideViewButton.setText("");

    FoodButton.setManaged(false);
    FoodButton.setVisible(false);
    AudioVisualButton.setManaged(false);
    AudioVisualButton.setVisible(false);
    ComputerServiceButton.setManaged(false);
    ComputerServiceButton.setVisible(false);
    LanguageButton.setManaged(false);
    LanguageButton.setVisible(false);
    ReligiousButton.setManaged(false);
    ReligiousButton.setVisible(false);
    PatientTransportButton.setManaged(false);
    PatientTransportButton.setVisible(false);

    mainView.setFitHeight(1080);
    mainView.setFitWidth(1920);
    mainView.setImage(floorMaps.hospital);
  }

  public void buttonStyle(JFXButton buttonO) {
    buttonO.setStyle(IDLE_BUTTON_STYLE);
    buttonO.setOnMouseEntered(
        e -> {
          buttonO.setStyle(HOVERED_BUTTON_STYLE);
        });
    buttonO.setOnMouseExited(
        e -> {
          buttonO.setStyle(IDLE_BUTTON_STYLE);
        });
  }

  /*
  Center node switching works for:

   */

  public void nodeSwitch(String fxmlFile) {
    FXMLLoader subControllerLoader = new FXMLLoader(Main.class.getResource(fxmlFile));
    try {
      BorderPane subPane = (BorderPane) subControllerLoader.load();
      AnchorPane.setTopAnchor(subPane, 0.0);
      AnchorPane.setBottomAnchor(subPane, 0.0);
      AnchorPane.setLeftAnchor(subPane, 0.0);
      AnchorPane.setRightAnchor(subPane, 0.0);
      nodeDataPane.setPadding(new Insets(0, 0, 0, 0));

      subPane.setPrefHeight(nodeDataPane.getHeight());
      subPane.setPrefWidth(nodeDataPane.getWidth());

      // nodeDataPane.getLayoutBounds().getHeight();
      nodeDataPane.getChildren().clear();
      nodeDataPane.getChildren().add(subPane);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void goHome(ActionEvent actionEvent) {
    nodeSwitch("views/homePage.fxml");
  }

  // goes to the Map viewer
  public void switchMapView(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/mapViewer.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 110));
  }

  // goes to the Computer Services page
  public void switchCompService(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/computerServiceRequest.fxml");
    nodeDataPane.setPadding(new Insets(0, 50, 0, 0));
  }

  // goes to the Employee Database page
  public void switchEmployeeDB(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/employeeDB.fxml");
  }

  // goes to the Food Delivery page
  public void switchFood(ActionEvent actionEvent) throws IOException {
    //    Stage stage = (Stage) FoodButton.getScene().getWindow();
    //    stage.setScene(new Scene(loader.load(App.class.getResource("views/foodDelivery.fxml"))));
    nodeSwitch("views/foodDelivery.fxml");
  }

  // goes to the Gift/Floral Delivery  page
  public void switchGiftFloral(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/giftFloralService.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  // goes to the Language Interpreter page
  public void switchLanguage(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/languageRequest.fxml");
    nodeDataPane.setPadding(new Insets(0, 50, 0, 0));
  }

  // goes to the Laundry Services page
  public void switchLaundry(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/laundryServiceChecks.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  // goes to the Medical Equipment  page
  public void switchMedicalEquipment(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/equipmentDelivery.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  // goes to the Medicine Delivery page
  public void switchMedicineDelivery(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/medicineDelivery.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  // goes to the Patient Transportation page
  public void switchPatientTransport(ActionEvent actionEvent) throws IOException {
    //    Stage stage = (Stage) PatientTransportButton.getScene().getWindow();
    //    stage.setScene(
    //        new Scene(loader.load(App.class.getResource("views/patientTransportService.fxml"))));
    nodeSwitch("views/patientTransportService");
  }

  // goes to the Religious Request page
  public void switchReligious(ActionEvent actionEvent) throws IOException {
    //    Stage stage = (Stage) ReligiousButton.getScene().getWindow();
    //    stage.setScene(new
    // Scene(loader.load(App.class.getResource("views/religiousServices.fxml"))));
    nodeSwitch("views/religiousServices.fxml");
  }

  public void switchAudioVisual(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/audioVisual.fxml");
    nodeDataPane.setPadding(new Insets(0, 50, 0, 0));
  }

  public void switchSideView() {
    nodeSwitch("views/towerSideView.fxml");
    nodeDataPane.setPadding(new Insets(0, 200, 0, 90));
  }

  // Goes to the location table
  //  public void switchLocationTable(ActionEvent actionEvent) throws IOException {
  //    //    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
  //    //    stage.setScene(new
  // Scene(loader.load(App.class.getResource("views/locationTable.fxml"))));
  //    nodeSwitch("views/locationTable.fxml");
  //  }

  public void enableAuthor() {
    disabledAuthors = false;
  }

  public void disableAuthor() {
    disabledAuthors = true;
  }

  public void slideOpen() {
    if (drawer.isClosed()) {
      drawer.open();

      // Im sorry I never got around to changing this, for now button text is still removed and
      // added based on the drawer state
      if (disabledAuthors) {
        ComputerServiceButton.setText("Computer Services");
        EmployeeDBButton.setText("Employee Database");
        AudioVisualButton.setText("Audio Visual");
        FoodButton.setText("Food Delivery");
        GiftFloralButton.setText("Gift & Floral Delivery");
        LanguageButton.setText("Language Interpreter");
        LaundryButton.setText("Laundry Services");
        MedicineDeliveryButton.setText("Medicine Delivery");
        MedicalEquipmentButton.setText("Medical Equipment");
        PatientTransportButton.setText("Patient Transportation");
        ReligiousButton.setText("Religious Request");
        MapViewButton.setText("Map Viewer");
        HomeButton.setText("Home");
        SideViewButton.setText("Side View");
      } else {
        ComputerServiceButton.setText("Joshua Moy");
        EmployeeDBButton.setText("Paul Godinez");
        AudioVisualButton.setText("Neena Xiang");
        FoodButton.setText("Eli Hoffberg");
        GiftFloralButton.setText("Neena Xiang");
        LanguageButton.setText("Mason Figler");
        LaundryButton.setText("Lehong Wang");
        MedicineDeliveryButton.setText("Paul Godinez");
        MedicalEquipmentButton.setText("Will BC");
        PatientTransportButton.setText("Jason Martino");
        ReligiousButton.setText("Will BC");
        MapViewButton.setText("Michael O'Connor");
        HomeButton.setText("Neena Xiang");
        SideViewButton.setText("Neena Xiang");
      }
    }
  }

  // Method for closing slider when mouse leaves. TODO Button text must be set to empty in here
  public void slideClose() {
    if (drawer.isOpened()) {
      drawer.close();
      // btn3.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

      // Im sorry I never got around to changing this, for now button text is still removed and
      // added based on the drawer state
      ComputerServiceButton.setText("");
      EmployeeDBButton.setText("");
      AudioVisualButton.setText("");
      FoodButton.setText("");
      GiftFloralButton.setText("");
      LanguageButton.setText("");
      LaundryButton.setText("");
      MedicineDeliveryButton.setText("");
      MedicalEquipmentButton.setText("");
      PatientTransportButton.setText("");
      ReligiousButton.setText("");
      MapViewButton.setText("");
      HomeButton.setText("");
      SideViewButton.setText("");
    }
  }

  @FXML
  public void exitProgram() {
    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
    stage.close();
  }

  public void enableClientServer(ActionEvent actionEvent) {
    TableController.setConnection(false);
  }

  public void enableEmbedded(ActionEvent actionEvent) {
    TableController.setConnection(true);
  }
}
