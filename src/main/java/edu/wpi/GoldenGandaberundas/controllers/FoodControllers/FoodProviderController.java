package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.MapController;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

public class FoodProviderController {

  @FXML private SearchableComboBox locationSearchBox;
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

  public TableController<Location, String> locations = null;

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  private String currentFloor = "1";
  private ArrayList<Location> currentLocations = null;

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane();
  private ImageView mapImage = null;
  private Group imageGroup = null;
  private Pane locNodePane = null;
  private Pane pathNodePane = null;

  public TableController tableController = FoodRequestTbl.getInstance();
  public LocationTbl locationTableController = LocationTbl.getInstance();

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
    gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    gesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    gesturePane.setMinScale(0.005);
    gesturePane.setFitMode(GesturePane.FitMode.COVER);

    locNodePane = new Pane();

    imageGroup.getChildren().add(locNodePane);

    // Hiding the text changing labels
    requestIDLabel.setText("");
    locationLabel.setText("");
    patientLabel.setText("");
    statusLabel.setText("");
    timeStartLabel.setText("");
    timeCompLabel.setText("");

    // Populating the location search box
    locList();

    foodTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            getRequestInfo();
          }
        });

    Location tloc = locationTableController.getEntry("FDEPT00101");
    createIcon(tloc);
  }

  public void locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();
    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
    }
    ObservableList<String> oList = FXCollections.observableArrayList(locNodeAr);
    locationSearchBox.setItems(oList);
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

  @FXML
  public void createIcon(Location loc) {
    LocationCircle placeHolder = new LocationCircle(loc);
    placeHolder.setLocation(loc);
    placeHolder.setRadius(10);

    locNodePane.getChildren().add(placeHolder);

    placeHolder.setLayoutX(loc.getXcoord());
    placeHolder.setLayoutY(loc.getYcoord());
  }

  public void setLocations(String floor) {
    locNodePane.getChildren().clear();
    currentFloor = floor;
    currentLocations = locations.readTable();
    currentLocations =
        (ArrayList)
            currentLocations.stream()
                .filter(l -> l.getFloor().equals(floor))
                .collect(Collectors.toList());
    for (Location l : currentLocations) {
      createIcon(l);
    }
  }

  public void refreshMap() {
    setLocations(currentFloor);
    refreshPath();
  }

  public void refreshPath() {
    for (Node p : pathNodePane.getChildren()) {
      MapController.PathBar pb = (MapController.PathBar) p;
      if (!pb.getFloor().equals(currentFloor)) {
        pb.setVisible(false);
      } else {
        pb.setVisible(true);
      }
    }
  }

  public HBox createFloorSelector() {
    JFXButton floorL2 = new JFXButton("L2");
    floorL2.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");

    JFXButton floorL1 = new JFXButton("L1");
    floorL1.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");

    JFXButton floor01 = new JFXButton("1");
    floor01.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");
    JFXButton floor02 = new JFXButton("2");
    floor02.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");
    JFXButton floor03 = new JFXButton("3");
    floor03.setStyle(
        "-fx-background-color: #0067B1;-fx-text-fill: white;-fx-border-color: white;-fx-background-radius: 10;-fx-border-radius: 10");

    HBox floorSelect = new HBox(floorL2, floorL1, floor01, floor02, floor03);
    HBox.setMargin(floor01, new Insets(0, 2, 0, 3));
    HBox.setMargin(floor02, new Insets(0, 2, 0, 3));
    HBox.setMargin(floorL1, new Insets(0, 2, 0, 3));
    HBox.setMargin(floorL2, new Insets(0, 2, 0, 3));
    HBox.setMargin(floor03, new Insets(0, 2, 0, 3));
    floorL2.setOnAction(
        e -> {
          mapImage.setImage(LL2);
          currentFloor = "L2";
          refreshMap();
          // rect.setY(322);
          // gridPane.setVisible(false);
        });
    floorL1.setOnAction(
        e -> {
          mapImage.setImage(LL1);
          currentFloor = "L1";
          refreshMap();
          // rect.setY(282);
          // gridPane.setVisible(true);
          // setFloorView(getEquipNum(filteredEquipmentsL1));
          // floorLabel.setText("Lower Floor 1");
        });
    floor01.setOnAction(
        e -> {
          mapImage.setImage(L1);
          currentFloor = "1";
          refreshMap();
          // rect.setY(242);
          // gridPane.setVisible(true);
          // setFloorView(getEquipNum(filteredEquipments1));
          // floorLabel.setText("Floor 1");
        });
    floor02.setOnAction(
        e -> {
          mapImage.setImage(L2);
          currentFloor = "2";
          refreshMap();
          // rect.setY(204);
          // gridPane.setVisible(false);
        });
    floor03.setOnAction(
        e -> {
          mapImage.setImage(L3);
          currentFloor = "3";
          refreshMap();
          // rect.setY(166);
          // gridPane.setVisible(true);
          // setFloorView(getEquipNum(filteredEquipments3));
          // floorLabel.setText("Floor 3");
        });

    return floorSelect;
  }

  private class LocationCircle extends Circle {
    public Location location = null;
    public MedEquipment medEquipment = null;

    public LocationCircle(Location loc) {
      super();
      location = loc;
    }

    void setLocation(Location loc) {
      location = loc;
    }
  }
}
