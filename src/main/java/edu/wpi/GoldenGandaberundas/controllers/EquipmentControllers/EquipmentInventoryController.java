package edu.wpi.GoldenGandaberundas.controllers.EquipmentControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EquipmentInventoryController implements Initializable {
  @FXML JFXButton homeBtn; // home btn with icon and text
  @FXML Button homeIconBtn; // home btn with just an icon
  @FXML JFXHamburger hamburger; // hamburger menu btn
  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox sideMenuMax; // container for the menu with icon a nd text
  @FXML VBox sideMenuMin; // container for the menu with just icons
  @FXML VBox sideMenuCont; // container for the entire set of side menus
  @FXML TableView equipmentTable; // Table object
  @FXML JFXButton backBtn; // Back button in hamburger slider
  @FXML TableColumn<MedEquipment, Integer> medID;
  @FXML TableColumn<MedEquipment, String> type;
  @FXML TableColumn<MedEquipment, String> status;
  @FXML TableColumn<MedEquipment, String> currentLoc;
  @FXML TableController tbCont;
  @FXML VBox drawerBox;

  @FXML TableView equipOrdersTbl;
  @FXML TableColumn<MedEquipRequest, Integer> reqID;
  @FXML TableColumn<MedEquipRequest, Integer> medEquipID;

  @FXML JFXButton backBtn1;
  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  public void goHome(ActionEvent actionEvent) throws IOException {
    // gets the current stage that the button exists on
    Stage stage = (Stage) homeBtn.getScene().getWindow();

    // sets the current scene back to the home screen
    // !!!!!!!!!! NOTE: you can only access FXML files stored in a directory with the same name as
    // the package!!!!!!!!!
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  @FXML
  public void backup() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up File");

    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {

      System.out.println(selectedFile.toString());
      tbCont.createBackup(new File(selectedFile.toString() + "\\medEquipBackup.csv"));

    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDB() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up File");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));

    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      tbCont.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refreshTable();
  }

  // Method to set buttons style, used in initialize method with slide panel buttons as params
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

  // Method for opening slider when mouse over. TODO Button text must be repopulated in here
  public void slideOpen() {
    if (drawer.isClosed()) {
      drawer.open();
      homeBtn.setText("  Home");
      backBtn.setText("  Add Equipment");
      backBtn1.setText("  Go To Requests");
    }
  }

  // Method for closing slider when mouse leaves. TODO Button text must be set to empty in here
  public void slideClose() {
    if (drawer.isOpened()) {
      drawer.close();
      homeBtn.setText("");
      backBtn.setText("");
      backBtn1.setText("");
    }
  }

  public void populateTable(List<Employee> empList) {
    equipmentTable.getItems().setAll();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    medID.setCellValueFactory(new PropertyValueFactory<MedEquipment, Integer>("medID"));
    type.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("type"));
    status.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("status"));
    currentLoc.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("currLoc"));

    reqID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("reqID"));
    medEquipID.setCellValueFactory(new PropertyValueFactory<MedEquipRequest, Integer>("medID"));

    // Uncomment when DB works
    //    populateTable(eqpDB.readTable());
    refreshTable();

    drawer.setSidePane(drawerBox);
    drawer.close();
    drawer.setMiniDrawerSize(36);
    // drawerBox.setStyle("-fx-background-image: url(Brigham-Womens.jpg)");
    // Any button Added to the slide panel must have its style set using this function
    buttonStyle(homeBtn);
    buttonStyle(backBtn);
    buttonStyle(backBtn1);
    // Setting buttons to empty for closed slider, could also delete their names in SceneBuilder but
    // for now this is fine.
    homeBtn.setText("");
    backBtn1.setText("");
    backBtn.setText("");
  }

  private void refreshTable() {
    tbCont = MedEquipmentTbl.getInstance();
    equipmentTable.getItems().setAll(tbCont.readTable());
    tbCont = MedEquipRequestTbl.getInstance();
    equipOrdersTbl.getItems().setAll(tbCont.readTable());
  }

  public void backToReq(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) backBtn.getScene().getWindow();
    stage.setScene(
        new Scene(FXMLLoader.load(App.class.getResource("views/equipmentDelivery.fxml"))));
  }

  public void addEquipment(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) backBtn.getScene().getWindow();
    stage.setScene(
        new Scene(FXMLLoader.load(App.class.getResource("views/AddEquipmentInventory.fxml"))));
  }
}
