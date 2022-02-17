package edu.wpi.GoldenGandaberundas.controllers.MedicineControllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MedicineOrderController implements Initializable {

  @FXML JFXButton homeBtn;
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML TableView medicineTbl; // Service Table
  @FXML JFXButton toRequests;
  @FXML JFXDrawer drawer;
  @FXML TableView ordersTbl;

  @FXML TableColumn<MedicineRequest, Integer> medicineID;
  @FXML TableColumn<MedicineRequest, Integer> reqID;
  @FXML TableColumn<MedicineRequest, Integer> quantity;

  private RequestTable medicine = RequestTable.getInstance();
  private MedicineRequestTbl medReq = MedicineRequestTbl.getInstance();
  TableController tbCont;

  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  public void initialize(URL location, ResourceBundle resources) {

    reqID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("requestID"));
    medicineID.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, Integer>("medicineID"));
    quantity.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("dosage"));

    // medicine = MedicineTbl.getInstance();
    // MedicineRequest = OrderedMedicineTbl.getInstance();

    Medicine drug =
        new Medicine(
            165,
            "Aspirin",
            "Aspirin is known as a salicylate and a nonsteroidal anti-inflammatory drug (NSAID). It works by blocking a certain natural substance in your body to reduce pain and swelling. Consult your doctor before treating a child younger than 12 years.Your doctor may direct you to take a low dose of aspirin to prevent blood clots. This effect reduces the risk of stroke and heart attack. If you have recently had surgery on clogged arteries (such as bypass surgery, carotid endarterectomy, coronary stent), your doctor may direct you to use aspirin in low doses as a \"blood thinner\" to prevent blood clots.",
            10.0,
            true);
    // MedicineRequest order = new OrderedMedicine("DRUGS", "bruh", 60);

    MedicineTbl.getInstance().addEntry(drug);
    // medicine.addEntry(order);
    refresh();
    drawer.setSidePane(drawerBox);
    drawer.close();
    drawer.setMiniDrawerSize(36);
    // drawerBox.setStyle("-fx-background-image: url(Brigham-Womens.jpg)");
    // Any button Added to the slide panel must have its style set using this function
    buttonStyle(homeBtn);
    buttonStyle(toRequests);
    // Setting buttons to empty for closed slider, could also delete their names in SceneBuilder but
    // for now this is fine.
    homeBtn.setText("");
    toRequests.setText("");
  }

  @FXML
  public void refresh() {
    //    orderedMedicine = OrderedMedicineTbl.getInstance();
    ordersTbl.getItems().setAll(medReq.readTable());
  }

  // Method for opening slider when mouse over. TODO Button text must be repopulated in here
  public void slideOpen() {
    if (drawer.isClosed()) {
      drawer.open();
      homeBtn.setText("  Home");
      toRequests.setText("  Go To Requests");
    }
  }

  // Method for closing slider when mouse leaves. TODO Button text must be set to empty in here
  public void slideClose() {
    if (drawer.isOpened()) {
      drawer.close();
      homeBtn.setText("");
      toRequests.setText("");
    }
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

  // Method that sets the scene back to the home page
  public void goHome(ActionEvent actionEvent) throws IOException {
    // gets the current stage that the button exists on
    Stage stage = (Stage) homeBtn.getScene().getWindow();

    // sets the current scene back to the home screen
    // !!!!!!!!!! NOTE: you can only access FXML files stored in a directory with the same name as
    // the package!!!!!!!!!
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  public void goToRequests(ActionEvent actionEvent) throws IOException {
    // gets the current stage that the button exists on
    Stage stage = (Stage) homeBtn.getScene().getWindow();

    // sets the current scene back to the home screen
    // !!!!!!!!!! NOTE: you can only access FXML files stored in a directory with the same name as
    // the package!!!!!!!!!
    stage.setScene(
        new Scene(FXMLLoader.load(App.class.getResource("views/medicineDelivery.fxml"))));
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
    refresh();
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
      tbCont.createBackup(new File(selectedFile.toString() + "\\medicineOrderBackup.csv"));

    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }
}
