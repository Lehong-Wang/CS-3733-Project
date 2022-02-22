package edu.wpi.GoldenGandaberundas.controllers.GiftFloralControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class GiftFloralController implements Initializable {

  @FXML private TextField giftFlowersField;
  @FXML private SearchableComboBox<String> locationSearchBox;
  @FXML private TextField quantityField;
  @FXML private TextField patientIDField;
  @FXML private TextField requesterIDField;
  @FXML private TextField reqIDField;
  @FXML SearchableComboBox<Integer> giftSearchBox;
  @FXML SearchableComboBox<Integer> patientSearchBox;
  @FXML TextArea notesField;

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

    ArrayList<String> locs = new ArrayList<>();
    for (Object l : LocationTbl.getInstance().readTable()) {
      locs.add(((Location) l).getNodeID());
    }
    ObservableList<String> locations = FXCollections.observableArrayList(locs);
    locationSearchBox.setItems(locations);

    ArrayList<Integer> patientIDs = new ArrayList<>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientSearchBox.setItems(FXCollections.observableArrayList(patientIDs));
    refresh();

    ArrayList<Integer> giftIDs = new ArrayList<>();
    for (Gift g : GiftTbl.getInstance().readTable()) {
      giftIDs.add(g.getGiftID());
    }
    giftSearchBox.setItems(FXCollections.observableArrayList(giftIDs));

    GiftOrderRequestTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });
  }

  void onEdit() {
    if (GiftOrderRequestTable.getSelectionModel().getSelectedItem() != null) {
      GiftRequest selectedItem =
          (GiftRequest) GiftOrderRequestTable.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editGiftFloralReqForm.fxml"));
        AnchorPane editForm = load.load();
        editGiftFloralReqFormController edit = load.getController();
        edit.editForm(RequestTable.getInstance().getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(selectedItem.getPK());
    }
  }

  public void submit() {
    int idCounter =
        RequestTable.getInstance().readTable().size() - 1 < 0
            ? 0
            : requestTableController
                    .readTable()
                    .get(requestTableController.readTable().size() - 1)
                    .getRequestID()
                + 1;
    String nodeID = locationSearchBox.getValue();
    int submittedTime;
    int completedTime;
    int patientID = patientSearchBox.getValue();
    int requesterID = CurrentUser.getUser().getEmpID();
    int completerID;

    String rowID; // placeholder for now = giftID + reqID
    int giftID = giftSearchBox.getValue();
    // String reqID;
    int quantity = Integer.parseInt(quantityField.getText());

    System.out.println(nodeID);
    GiftRequest giftRequest =
        new GiftRequest(
            idCounter,
            nodeID,
            requesterID,
            123,
            0,
            0,
            patientID,
            "Submitted",
            "",
            giftID,
            quantity);
    GiftRequestTbl.getInstance().addEntry(giftRequest);
    //    OrderedGift orderedGIft = new OrderedGift(giftID, reqID, quantity);
    //    requestTableController.addEntry(giftRequest);
    //    orderTableController.addEntry((orderedGIft));
    refresh();

    giftMenuTable.getItems().setAll(menuTableController.readTable());
  }

  public void refresh() {

    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<GiftRequest> grs = new ArrayList<>();
      for (GiftRequest gr : GiftRequestTbl.getInstance().readTable()) {
        if (gr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          grs.add(gr);
        }
      }
      GiftOrderRequestTable.getItems().setAll(grs);
    } else {
      GiftOrderRequestTable.getItems().setAll(reqs.readTable());
    }
    giftMenuTable.getItems().setAll(menuTableController.readTable());
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

  @FXML
  public void backupGift() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Gifts File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      GiftTbl.getInstance().createBackup(new File(selectedFile.toString() + "\\laundryBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void backupRequests() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Requests File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      GiftRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\medLaundryRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDBGifts() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Gift File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      GiftTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Gifts Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      GiftRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }
}
