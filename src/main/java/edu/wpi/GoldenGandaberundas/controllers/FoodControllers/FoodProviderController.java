package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

public class FoodProviderController {

  @FXML private Label requestIDLabel;
  @FXML private Label locationLabel;
  @FXML private Label patientLabel;
  @FXML private Label statusLabel;
  @FXML private Label timeStartLabel;
  @FXML private Label timeCompLabel;

  @FXML private TableView foodTable;
  @FXML private TableColumn<FoodRequest, Integer> reqID;
  @FXML private TableColumn<FoodRequest, String> locID;
  @FXML private TableColumn<FoodRequest, Integer> tStartCol;
  @FXML private TableColumn<FoodRequest, Integer> tEndCol;
  @FXML TableColumn<FoodRequest, Integer> patientID;
  @FXML TableColumn<FoodRequest, String> requestStatus;

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane();
  private ImageView mapImage = null;
  private Group imageGroup = null;

  public TableController tableController = FoodRequestTbl.getInstance();
  public TableController locTableController = LocationTbl.getInstance();

  @FXML
  public void initialize() {
    reqID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("requestID"));
    locID.setCellValueFactory(new PropertyValueFactory<FoodRequest, String>("locationID"));
    tStartCol.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("timeStart"));
    tEndCol.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("patientID"));
    requestStatus.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, String>("requestStatus"));

    refresh();

    mapImage = new ImageView(floorMaps.firstFloor);

    imageGroup = new Group();
    imageGroup.getChildren().add(mapImage);

    // imagePane.getChildren().add(imageGroup);
    imagePane.getChildren().add(gesturePane);
    gesturePane.setContent(imageGroup);

    requestIDLabel.setText("");
    locationLabel.setText("");
    patientLabel.setText("");
    statusLabel.setText("");
    timeStartLabel.setText("");
    timeCompLabel.setText("");

    foodTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            getRequestInfo();
          }
        });
  }

  @FXML
  public void getRequestInfo() {
    if (foodTable.getSelectionModel().getSelectedItem() != null) {
      FoodRequest selectedItem = (FoodRequest) foodTable.getSelectionModel().getSelectedItem();
      try {
        // tableController.getEntry(selectedItem.getPK())
        Integer id = selectedItem.getRequestID();
        requestIDLabel.setText(Integer.toString(id));

        // This should get the long name from the location object and display that
        String loc = selectedItem.getLocationID();
        locationLabel.setText(loc);

        Integer patient = selectedItem.getPatientID();
        patientLabel.setText(Integer.toString(patient));

        String status = selectedItem.getRequestStatus();
        statusLabel.setText(status);

        long tStart = selectedItem.getTimeStart();
        timeStartLabel.setText(String.valueOf(tStart));

        long tEnd = selectedItem.getTimeEnd();
        timeCompLabel.setText("    " + String.valueOf(tEnd));

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Method that updates the table displaying a list of requests TODO update this method to check
   * for a list of the current users requests
   */
  @FXML
  public void refresh() {
    tableController = FoodRequestTbl.getInstance();
    foodTable.getItems().setAll(tableController.readTable());
  }
}
