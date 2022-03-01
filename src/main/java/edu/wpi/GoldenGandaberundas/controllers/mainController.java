package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.Main;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import java.io.IOException;
import java.util.ArrayList;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class mainController {

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
  @FXML JFXButton SideViewButton;
  @FXML JFXButton HomeButton;
  @FXML JFXButton allRequestsButton;
  @FXML JFXButton SimulationButton;
  @FXML JFXButton SettingsButton;

  @FXML JFXButton serviceRequestButton;
  @FXML JFXButton mapViewButton;

  @FXML Group medGroup;

  @FXML ImageView mainView;
  @FXML BorderPane homePageNode;

  int count;
  boolean disabledAuthors = true;

  // CSS Style strings, used to style drawer buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59;-fx-alignment: center-left;";
  //  private static final String HOVERED_BUTTON_STYLE =
  //      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border,
  // -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left;";

  public void initialize() {
    slideShow();
    // BUTTONS HIDDEN TILL IMPLEMENTATION
    // FoodButton.setManaged(false);
    // FoodButton.setVisible(false);
    PatientTransportButton.setManaged(false);
    PatientTransportButton.setVisible(false);
    ReligiousButton.setManaged(false);
    ReligiousButton.setVisible(false);
    // Add in drawer lines
    drawer.setSidePane(drawerBox);
    drawer.setMiniDrawerSize(36);
    drawer.setResizableOnDrag(true);
    // buttonStyle(ComputerServiceButton);
    // buttonStyle(EmployeeDBButton);
    // buttonStyle(AudioVisualButton);
    // buttonStyle(FoodButton);
    // buttonStyle(GiftFloralButton);
    // buttonStyle(LanguageButton);
    // buttonStyle(LaundryButton);
    // buttonStyle(MedicineDeliveryButton);
    // buttonStyle(MedicalEquipmentButton);
    // buttonStyle(PatientTransportButton);
    // buttonStyle(ReligiousButton);
    // buttonStyle(MapViewButton);
    // buttonStyle(HomeButton);
    // buttonStyle(SideViewButton);
    // buttonStyle(allRequestsButton);
    // buttonStyle(SimulationButton);
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
    HomeButton.setText("");
    SideViewButton.setText("");
    allRequestsButton.setText("");
    SimulationButton.setText("");
    SettingsButton.setText("");
    serviceRequestButton.setText("");
    mapViewButton.setText("");

    // Hiding buttons until service is fully implemented

    LanguageButton.setManaged(false);
    LanguageButton.setVisible(false);
    ReligiousButton.setManaged(false);
    ReligiousButton.setVisible(false);
    PatientTransportButton.setManaged(false);
    PatientTransportButton.setVisible(false);

    // Checking current users permissions
    checkPerms();

    mainView.setFitHeight(1080);
    mainView.setFitWidth(1920);
    mainView.setImage(floorMaps.hospital);
    toggleRequestButtons(false);
    toggleMapButtons(false);
    EmployeeDBButton.setManaged(false);
    EmployeeDBButton.setVisible(false);
  }

  /**
   * Toggles all the buttons that the map viewer uses
   *
   * @param toggle True to toggle the buttons on, false to turn them off
   */
  public void toggleMapButtons(Boolean toggle) {
    SideViewButton.setManaged(toggle);
    SimulationButton.setManaged(toggle);

    SideViewButton.setVisible(toggle);
    SimulationButton.setVisible(toggle);
  }

  /**
   * TOggles all the buttons that the request buttons use
   *
   * @param toggle True to toggle the buttons on, false to turn them off
   */
  public void toggleRequestButtons(Boolean toggle) {
    ComputerServiceButton.setManaged(toggle);
    AudioVisualButton.setManaged(toggle);
    FoodButton.setManaged(toggle);
    GiftFloralButton.setManaged(toggle);
    LaundryButton.setManaged(toggle);
    MedicineDeliveryButton.setManaged(toggle);
    MedicalEquipmentButton.setManaged(toggle);
    allRequestsButton.setManaged(toggle);

    ComputerServiceButton.setVisible(toggle);
    AudioVisualButton.setVisible(toggle);
    FoodButton.setVisible(toggle);
    GiftFloralButton.setVisible(toggle);
    LaundryButton.setVisible(toggle);
    MedicineDeliveryButton.setVisible(toggle);
    MedicalEquipmentButton.setVisible(toggle);
    allRequestsButton.setVisible(toggle);

    EmployeeDBButton.setVisible(toggle);
    EmployeeDBButton.setManaged(toggle);

    checkPerms();
  }

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

  /**
   * Method that iterates through a users permissions and hides elements they dont have access too
   */
  public void checkPerms() {
    int currID = CurrentUser.getUser().getEmpID();
    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
    boolean hideShit = true;
    for (int i = 0; i < perms.size(); i++) {
      if (perms.get(i) == 111) {
        hideShit = false;
        break;
      }
    }
    if (hideShit == true) {
      hideAdmin();
    }
  }

  /** Helper method for checking perms which uses a switch case to hide elements */
  public void hideAdmin() {
    EmployeeDBButton.setVisible(false);
    EmployeeDBButton.setManaged(false);
  }

  public void goHome(ActionEvent actionEvent) {
    serviceRequestButton.setManaged(true);
    serviceRequestButton.setVisible(true);
    mapViewButton.setManaged(true);
    mapViewButton.setVisible(true);
    SettingsButton.setManaged(true);
    SettingsButton.setVisible(true);

    toggleRequestButtons(false);
    toggleMapButtons(false);
    nodeSwitch("views/homePage.fxml");
    slideShow();
  }

  // goes to the Map viewer

  /**
   * switches to the map view
   *
   * @throws IOException error
   */
  public void switchMapView() throws IOException {
    toggleMapButtons(true);
    toggleRequestButtons(false);
    SettingsButton.setManaged(false);
    SettingsButton.setVisible(false);
    serviceRequestButton.setManaged(false);
    serviceRequestButton.setVisible(false);
    FXMLLoader subControllerLoader = new FXMLLoader(App.class.getResource("views/mapViewer.fxml"));

    try {

      BorderPane subPane = subControllerLoader.load();
      MapController master = subControllerLoader.getController();
      master.setMainController(this);
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
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  public void switchAllRequests() {
    FXMLLoader subControllerLoader =
        new FXMLLoader(App.class.getResource("views/masterRequestTable.fxml"));

    try {

      BorderPane subPane = subControllerLoader.load();
      masterRequestTableController master = subControllerLoader.getController();
      master.setMainControler(this);
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
    nodeDataPane.setPadding(new Insets(0, 0, 10, 100));
  }

  @FXML
  public void switchRequestPage() {
    toggleRequestButtons(true);
    toggleMapButtons(false);
    SettingsButton.setManaged(false);
    SettingsButton.setVisible(false);
    mapViewButton.setManaged(false);
    mapViewButton.setVisible(false);
    FXMLLoader subControllerLoader =
        new FXMLLoader(App.class.getResource("views/serviceRequestPage.fxml"));

    try {
      BorderPane subPane = subControllerLoader.load();
      serviceRequestPageController master = subControllerLoader.getController();
      master.setMainControler(this);
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
    nodeDataPane.setPadding(new Insets(0, 0, 10, 110));
  }

  // goes to the Computer Services page
  public void switchCompService(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/computerServiceRequest.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  // goes to the Employee Database page
  public void switchEmployeeDB(ActionEvent actionEvent) throws IOException {
    nodeSwitch("views/employeeDB.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  // goes to the Food Delivery page
  public void switchFood(ActionEvent actionEvent) throws IOException {
    //    Stage stage = (Stage) FoodButton.getScene().getWindow();
    //    stage.setScene(new Scene(loader.load(App.class.getResource("views/foodDelivery.fxml"))));
    nodeSwitch("views/foodDeliveryForm.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
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
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  /** switches to the tower side view */
  public void switchSideView() {
    FXMLLoader subControllerLoader =
        new FXMLLoader(App.class.getResource("views/towerSideView.fxml"));

    try {

      BorderPane subPane = subControllerLoader.load();
      TowerSideViewController master = subControllerLoader.getController();
      master.setMainController(this);
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
    nodeDataPane.setPadding(new Insets(0, 0, 0, 90));
  }






  public void switchSimulationView() {
    nodeSwitch("views/simulationView.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 90));
  }

  public void switchSettings() {
    mapViewButton.setVisible(false);
    mapViewButton.setManaged(false);
    serviceRequestButton.setVisible(false);
    serviceRequestButton.setManaged(false);
    nodeSwitch("views/settingsPage.fxml");
    nodeDataPane.setPadding(new Insets(0, 0, 0, 100));
  }

  @FXML
  public void switchAVHelpPage() {
    nodeSwitch("views/aVHelpPage.fxml");
  }

  @FXML
  public void switchComputerServiceHelpPage() {
    nodeSwitch("views/computerServiceHelpPage.fxml");
  }

  @FXML
  public void switchemployeeDatabaseAndRequestsViewHelpPage() {
    nodeSwitch("views/employeeDatabaseAndRequestsViewHelpPage.fxml");
  }

  @FXML
  public void switchequipmentDeliveryHelpPage() {
    nodeSwitch("views/equipmentDeliveryHelpPage.fxml");
  }

  @FXML
  public void switchfoodDeliveryHelpPage() {
    nodeSwitch("views/foodDeliveryHelpPage.fxml");
  }

  @FXML
  public void switchgiftFloralHelpPage() {
    nodeSwitch("views/giftFloralHelpPage.fxml");
  }

  @FXML
  public void switchLanguageRequestHelpPage() {
    nodeSwitch("views/LanguageRequestHelpPage.fxml");
  }

  @FXML
  public void switchlaundryHelpPage() {
    nodeSwitch("views/laundryHelpPage.fxml");
  }

  @FXML
  public void switchmapViewerHelpPage() {
    nodeSwitch("views/mapViewerHelpPage.fxml");
  }

  @FXML
  public void switchmedicineDeliveryHelpPage() {
    nodeSwitch("views/medicineDeliveryHelpPage.fxml");
  }

  @FXML
  public void switchsideViewHelpPage() {
    nodeSwitch("views/sideViewHelpPage.fxml");
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
        HomeButton.setText("Home");
        SideViewButton.setText("Side View");
        allRequestsButton.setText("All Requests View");
        SimulationButton.setText("Simulation");
        SettingsButton.setText("Settings");
        serviceRequestButton.setText("Service Request Page");
        mapViewButton.setText("Map Views");

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
        HomeButton.setText("Neena Xiang");
        SideViewButton.setText("Neena Xiang");
        allRequestsButton.setText("Paul Godinez");
        SimulationButton.setText("Mason Figler");
        SettingsButton.setText("Lehong Wang");
        serviceRequestButton.setText("Paul Godinez");
        mapViewButton.setText("Michael O'Connor");
      }
    }
  }

  // Method for closing slider when mouse leaves.
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
      HomeButton.setText("");
      SideViewButton.setText("");
      allRequestsButton.setText("");
      SimulationButton.setText("");
      SettingsButton.setText("");
      serviceRequestButton.setText("");
      mapViewButton.setText("");
    }
  }

  /**
   * This method logouts the user of the application and takes them to the login screen
   *
   * @throws IOException
   */
  public void logoutProgram() throws IOException {
    Stage stage = (Stage) ComputerServiceButton.getScene().getWindow();
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/loginScreen.fxml"))));
    CurrentUser.clearUser();
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

  /** Slide show function to display an assortment of images that fade in an out */
  public void slideShow() {
    ArrayList<Image> images = new ArrayList<>();
    images.add(floorMaps.hospital);
    images.add(floorMaps.peopleHospital);
    images.add(floorMaps.sideBuildingHospital);
    images.add(floorMaps.PeopleWalkingBWH);
    images.add(floorMaps.BWH_BridgeInside);
    images.add(floorMaps.lookingAtXrays);

    Timeline timeline = new Timeline();
    KeyValue transparent = new KeyValue(mainView.opacityProperty(), 0.4);
    KeyValue opaque = new KeyValue(mainView.opacityProperty(), 1.0);

    KeyFrame startFadeIn = new KeyFrame(Duration.millis(500), transparent);
    KeyFrame endFadeIn = new KeyFrame(Duration.seconds(1), opaque);
    KeyFrame startFadeOut = new KeyFrame(Duration.seconds(25), opaque);
    KeyFrame endFadeOut =
        new KeyFrame(
            Duration.millis(500),
            e -> {
              if (count < images.size()) {
                mainView.setImage(images.get(count));
                if (count == images.size() - 1) {
                  count = 0;
                } else {
                  count++;
                }
              }
            },
            transparent);

    timeline.getKeyFrames().addAll(startFadeIn, endFadeIn, startFadeOut, endFadeOut);

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
