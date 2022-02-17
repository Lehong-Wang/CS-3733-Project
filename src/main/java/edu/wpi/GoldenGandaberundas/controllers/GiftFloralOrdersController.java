package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class GiftFloralOrdersController implements Initializable {
  @FXML private Button homeBtn; // home btn with icon and text
  @FXML private JFXDrawer drawer; // sliding side menu
  @FXML private JFXButton deliveriesBtn; // Cool Sliding Button
  @FXML private VBox drawerBox; // Cool Sliding Table
  @FXML private TableView servicesTbl; // Service Table

  @FXML TableView GiftOrderRequestTable;
  @FXML private TableColumn<GiftRequest, Integer> reqID;
  @FXML private TableColumn<GiftRequest, String> nodeID;
  @FXML private TableColumn<GiftRequest, Integer> submittedTime;
  @FXML private TableColumn<GiftRequest, Integer> completedTime;
  @FXML private TableColumn<GiftRequest, Integer> patientID;
  @FXML private TableColumn<GiftRequest, Integer> requesterID;
  @FXML private TableColumn<GiftRequest, Integer> completerID;
  @FXML private TableColumn<GiftRequest, String> status;

  private GiftRequestTbl tableMenuController = GiftRequestTbl.getInstance();
  private TableController objectTableController = null;

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    drawer.setSidePane(drawerBox);
    drawer.close();
    drawer.setMiniDrawerSize(36);
    // drawerBox.setStyle("-fx-background-image: url(Brigham-Womens.jpg)");
    // Any button Added to the slide panel must have its style set using this function
    buttonStyle(deliveriesBtn);
    // Setting buttons to empty for closed slider, could also delete their names in SceneBuilder
    // for now this is fine.
    deliveriesBtn.setText("");

    reqID.setCellValueFactory((new PropertyValueFactory<GiftRequest, Integer>("requestID")));
    nodeID.setCellValueFactory((new PropertyValueFactory<GiftRequest, String>("locationID")));
    submittedTime.setCellValueFactory(
        (new PropertyValueFactory<GiftRequest, Integer>("timeStart")));
    completedTime.setCellValueFactory((new PropertyValueFactory<GiftRequest, Integer>("timeEnd")));
    patientID.setCellValueFactory((new PropertyValueFactory<GiftRequest, Integer>("patientID")));
    requesterID.setCellValueFactory(
        (new PropertyValueFactory<GiftRequest, Integer>("empInitiated")));
    completerID.setCellValueFactory(
        (new PropertyValueFactory<GiftRequest, Integer>("empCompleter")));
    status.setCellValueFactory((new PropertyValueFactory<GiftRequest, String>("requestStatus")));

    GiftOrderRequestTable.setOnMouseClicked(
        event -> {
          // Make sure the user clicked on a populated item
          if (GiftOrderRequestTable.getSelectionModel().getSelectedItem() != null) {
            System.out.println(
                "You clicked on "
                    + GiftOrderRequestTable.getSelectionModel().getSelectedItem().getClass());
          }
        });
  }

  public void loadRequest() {
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
      tableMenuController.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  public void loadOrder() {
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
      tableMenuController.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  public void backup() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up File");

    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {

      System.out.println(selectedFile.toString());
      objectTableController.createBackup(
          new File(selectedFile.toString() + "\\giftRequestBackup.csv"));

    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  public void refresh() {
    // GiftOrderRequestTable.getItems().setAll(objectTableController.readTable());
    RequestTable gifts = RequestTable.getInstance();
    GiftOrderRequestTable.getItems().setAll(gifts.readTable());
    // System.out.println(tableMenuController.readTable());
  }

  public void goHome(ActionEvent actionEvent) throws IOException {
    // gets the current stage that the button exists on
    Stage stage = (Stage) deliveriesBtn.getScene().getWindow();

    // sets the current scene back to the home screen
    // !!!!!!!!!! NOTE: you can only access FXML files stored in a directory with the same name as
    // the package!!!!!!!!!
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  public void goGiftRequest(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) deliveriesBtn.getScene().getWindow();
    stage.setScene(
        new Scene(FXMLLoader.load(App.class.getResource("views/giftFloralService.fxml"))));
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
      deliveriesBtn.setText("  Current Deliveries");
    }
  }

  // Method for closing slider when mouse leaves. TODO Button text must be set to empty in here
  public void slideClose() {
    if (drawer.isOpened()) {
      drawer.close();
      deliveriesBtn.setText("");
    }
  }
}
