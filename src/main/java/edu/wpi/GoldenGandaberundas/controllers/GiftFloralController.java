package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class GiftFloralController implements Initializable {

  @FXML private TextField giftFlowersField;
  @FXML private SearchableComboBox locationSearchBox;
  @FXML private TextField quantityField;
  @FXML private TextField patientIDField;
  @FXML private TextField requesterIDField;
  @FXML private TextField reqIDField;

  @FXML TableView giftMenuTable;
  @FXML private TableColumn<Gift, String> giftID;
  @FXML private TableColumn<Gift, String> description;
  @FXML private TableColumn<Gift, Double> price;
  @FXML private TableColumn<Gift, Boolean> inStock;

  @FXML TableView GiftOrderRequestTable;
  @FXML TableColumn<GiftRequest, Integer> reqID;
  @FXML TableColumn<GiftRequest, String> nodeID;
  @FXML TableColumn<GiftRequest, Integer> submittedTime;
  @FXML TableColumn<GiftRequest, Integer> completedTime;
  @FXML TableColumn<GiftRequest, Integer> patientID;
  @FXML TableColumn<GiftRequest, Integer> requesterID;
  @FXML TableColumn<GiftRequest, Integer> completerID;
  @FXML TableColumn<GiftRequest, String> status;
  @FXML TableColumn<GiftRequest, String> notes;
  @FXML TableColumn<GiftRequest, Integer> reqGiftID;
  @FXML TableColumn<GiftRequest, Integer> quantity;
  private GiftRequestTbl reqs = GiftRequestTbl.getInstance();

  private RequestTable requestTableController = RequestTable.getInstance();
  private TableController menuTableController = GiftTbl.getInstance();
  private TableController locationTableController = LocationTbl.getInstance();

  private String locations = ""; // String used to store the location from dropdown

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    TableController patients = PatientTbl.getInstance();
    // drawerBox.setStyle("-fx-background-image: url(Brigham-Womens.jpg)");
    // Any button Added to the slide panel must have its style set using this function

    // menuTableController = GiftTbl.getInstance();

    giftID.setCellValueFactory(new PropertyValueFactory<Gift, String>("giftID"));
    description.setCellValueFactory(new PropertyValueFactory<Gift, String>("description"));
    price.setCellValueFactory(new PropertyValueFactory<Gift, Double>("price"));
    inStock.setCellValueFactory(new PropertyValueFactory<Gift, Boolean>("inStock"));

    reqID.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("requestID"));
    nodeID.setCellValueFactory(new PropertyValueFactory<GiftRequest, String>("locationID"));
    submittedTime.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("timeStart"));
    completedTime.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("patientID"));
    requesterID.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("empCompleter"));
    status.setCellValueFactory(new PropertyValueFactory<GiftRequest, String>("requestStatus"));
    reqGiftID.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("giftID"));
    quantity.setCellValueFactory(new PropertyValueFactory<GiftRequest, Integer>("quantity"));

    refresh();
  }

  public void submit() {
    int idCounter = requestTableController.readTable().size() + 1;
    String nodeID = locations;
    int submittedTime;
    int completedTime;
    int patientID = Integer.parseInt(patientIDField.getText());
    int requesterID = Integer.parseInt(requesterIDField.getText());
    int completerID;

    String rowID; // placeholder for now = giftID + reqID
    String giftID = giftFlowersField.getText();
    // String reqID;
    int quantity = Integer.parseInt(quantityField.getText());

    //    GiftRequest giftRequest =
    //        new GiftRequest(reqID, nodeID, 100, 100, patientID, requesterID, 123, "Initiated");
    //    OrderedGift orderedGIft = new OrderedGift(giftID, reqID, quantity);
    //    requestTableController.addEntry(giftRequest);
    //    orderTableController.addEntry((orderedGIft));

    giftMenuTable.getItems().setAll(menuTableController.readTable());
  }

  public void refresh() {
    giftMenuTable.getItems().setAll(menuTableController.readTable());
    GiftOrderRequestTable.getItems().setAll(reqs.readTable());
  }

  public void load() {
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
      menuTableController.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
      // locationSearchBox.getItems().add(locArray.get(i).getNodeID());
      System.out.println(locNodeAr.get(i));
    }
    return locNodeAr;
  }

  //  public void goGiftOrders(ActionEvent actionEvent) throws IOException {
  //    Stage stage = (Stage) ordersButton.getScene().getWindow();
  //    stage.setScene(
  //        new Scene(FXMLLoader.load(App.class.getResource("views/giftFloralOrders.fxml"))));
  //  }

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
}
