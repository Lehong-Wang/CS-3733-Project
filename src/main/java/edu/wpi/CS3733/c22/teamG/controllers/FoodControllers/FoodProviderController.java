package edu.wpi.CS3733.c22.teamG.controllers.FoodControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.CS3733.c22.teamG.CurrentUser;
import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.componentObjects.floorMaps;
import edu.wpi.CS3733.c22.teamG.controllers.MapController;
import edu.wpi.CS3733.c22.teamG.controllers.mainController;
import edu.wpi.CS3733.c22.teamG.tableControllers.AStar.PathTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodRequest;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.Location;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.util.Duration;
import net.kurobako.gesturefx.GesturePane;
import org.controlsfx.control.SearchableComboBox;

public class FoodProviderController {

  @FXML private SearchableComboBox<String> locationSearchBox;
  @FXML private Label requestIDLabel;
  @FXML private Label locationLabel;
  @FXML private Label patientLabel;
  @FXML private Label statusLabel;
  @FXML private Label timeStartLabel;
  @FXML private Label timeCompLabel;
  @FXML private Label foodIDLabel;

  @FXML private TableView foodTable;
  @FXML private TableColumn<FoodRequest, Integer> reqID;
  @FXML private TableColumn<FoodRequest, String> locID;
  @FXML private TableColumn<FoodRequest, Integer> tStartCol;
  @FXML private TableColumn<FoodRequest, Integer> tEndCol;
  @FXML TableColumn<FoodRequest, Integer> patientID;
  @FXML TableColumn<FoodRequest, String> requestStatus;

  @FXML private JFXButton pathButton;
  @FXML private JFXButton clearButton;
  @FXML private ComboBox statusBox;

  public TableController<Location, String> locations = null;

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  private String currentFloor = "1";
  private ArrayList<Location> currentLocations = null;
  private ArrayList<ArrayList<String>> coord = null;
  private String startTemp = null;
  private String endTemp = null;

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane();
  private ImageView mapImage = null;
  private Group imageGroup = null;
  private Pane locNodePane = null;
  private Pane pathNodePane = null;
  private Group animatedPathNodeGroup = null;
  private List<String> astar = null;

  mainController main = null;

  private TableController tableController = FoodRequestTbl.getInstance();
  private LocationTbl locationTableController = LocationTbl.getInstance();
  private PathTbl path = PathTbl.getInstance();

  @FXML
  public void initialize() {

    statusBox.getItems().addAll("Submitted", "In_Progress", "Completed");
    coord = new ArrayList<ArrayList<String>>();
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    coord.add(new ArrayList<>());
    locations = LocationTbl.getInstance();

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

    pathNodePane = new Pane();
    imageGroup.getChildren().add(pathNodePane);

    locNodePane = new Pane();
    imageGroup.getChildren().add(locNodePane);

    animatedPathNodeGroup = new Group();
    imageGroup.getChildren().add(animatedPathNodeGroup);

    // Hiding the text changing labels
    requestIDLabel.setText("");
    locationLabel.setText("");
    patientLabel.setText("");
    statusLabel.setText("");
    timeStartLabel.setText("");
    timeCompLabel.setText("");
    foodIDLabel.setText("");

    HBox floorSelect = createFloorSelector();
    floorSelect.setMaxHeight(25);
    imagePane.getChildren().add(floorSelect);
    floorSelect.setMaxHeight(25);
    setLocations(currentFloor);
    imagePane.setAlignment(floorSelect, Pos.TOP_LEFT);

    // Populating the location search box
    locList();

    foodTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            getRequestInfo();
          }
        });

    pathButton.setOnMouseReleased(
        (event) -> {
          FoodRequest selectedItem = (FoodRequest) foodTable.getSelectionModel().getSelectedItem();
          try {
            String start = (String) locationSearchBox.getSelectionModel().getSelectedItem();
            String end = (String) selectedItem.getLocationID();
            // System.out.println(previouslyUsed(start, end));
            if (!previouslyUsed(start, end)) {
              astar = PathTbl.getInstance().createAStarPath(start, end);
              dividePath(astar);
              animatedPath();
              pathNodePane.setVisible(true);
            }
          } catch (Exception e) {
          }
        });

    clearButton.setOnMouseReleased(
        (event) -> {
          pathNodePane.getChildren().clear();
          animatedPathNodeGroup.getChildren().clear();
          startTemp = null;
          endTemp = null;
        });
  }

  /** Method that populates the stating location search box with possible hospital locations */
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

  /**
   * Pathfinding method that splits the path among multiple array lists
   *
   * @param locs
   */
  public void dividePath(List<String> locs) {
    coord.get(0).clear();
    coord.get(1).clear();
    coord.get(2).clear();
    coord.get(3).clear();
    coord.get(4).clear();
    for (int i = 0; i < locs.size(); i++) {
      if (locs.get(i).substring(locs.get(i).length() - 2).equals("L2")) {
        coord.get(0).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 2).equals("L1")) {
        coord.get(1).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("1")) {
        coord.get(2).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("2")) {
        coord.get(3).add(locs.get(i));
      } else {
        coord.get(4).add(locs.get(i));
      }
    }
  }

  /** Generate the path based on the current floor */
  public void animatedPath() {
    animatedPathNodeGroup.getChildren().clear();
    ArrayList<String> locs = new ArrayList<>();
    if (currentFloor.equals("L2")) {
      locs = coord.get(0);
    } else if (currentFloor.equals("L1")) {
      locs = coord.get(1);
    } else if (currentFloor.equals("1")) {
      locs = coord.get(2);
    } else if (currentFloor.equals("2")) {
      locs = coord.get(3);
    } else if (currentFloor.equals("3")) {
      locs = coord.get(4);
    }

    // iterates through the list of locations and adds it to the coords list of the poly line
    ArrayList<Double> coordsList = new ArrayList<>();
    boolean secondLine = false;
    int tempStop = 0;
    for (int i = 0; i < locs.size() - 1; i++) {
      if (locs.get(i).substring(0, 5).equals("WELEV")
              && locs.get(i + 1).substring(0, 5).equals("WELEV")
          || locs.get(i + 1).substring(0, 5).equals("WELEV")
              && locs.get(i).substring(0, 5).equals("WELEV")) {
        secondLine = true;
        tempStop = i + 1;
        break;
      }
      Location loc = LocationTbl.getInstance().getEntry(locs.get(i));
      Location loc1 = LocationTbl.getInstance().getEntry(locs.get(i + 1));
      coordsList.add((double) (loc.getXcoord()));
      coordsList.add((double) (loc.getYcoord()));
      coordsList.add((double) (loc1.getXcoord()));
      coordsList.add((double) (loc1.getYcoord()));
    }

    // creates the polyline bases on the positions coordinates
    Polyline polyline = new Polyline();
    polyline.getPoints().addAll(coordsList);
    polyline.setStroke(Color.web("#006db3"));
    polyline.setStrokeWidth(10);
    polyline.getStrokeDashArray().setAll(20.0, 20.0);
    animatedPathNodeGroup.getChildren().addAll(polyline);

    // animates the line accordingly
    Timeline timeline =
        new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(polyline.strokeDashOffsetProperty(), 1000)),
            new KeyFrame(
                Duration.seconds(15), new KeyValue(polyline.strokeDashOffsetProperty(), 0)));
    timeline.setCycleCount(Timeline.INDEFINITE);
    timeline.play();

    if (secondLine) {
      ArrayList<Double> coordsList2 = new ArrayList<>();
      for (int i = tempStop; i < locs.size() - 1; i++) {
        Location loc = LocationTbl.getInstance().getEntry(locs.get(i));
        Location loc1 = LocationTbl.getInstance().getEntry(locs.get(i + 1));
        coordsList2.add((double) (loc.getXcoord()));
        coordsList2.add((double) (loc.getYcoord()));
        coordsList2.add((double) (loc1.getXcoord()));
        coordsList2.add((double) (loc1.getYcoord()));
      }

      // creates the polyline bases on the positions coordinates
      Polyline polyline2 = new Polyline();
      polyline2.getPoints().addAll(coordsList2);
      polyline2.setStroke(Color.web("#006db3"));
      polyline2.setStrokeWidth(10);
      polyline2.getStrokeDashArray().setAll(20.0, 20.0);
      animatedPathNodeGroup.getChildren().addAll(polyline2);

      // animates the line accordingly
      Timeline timeline2 =
          new Timeline(
              new KeyFrame(Duration.ZERO, new KeyValue(polyline2.strokeDashOffsetProperty(), 1000)),
              new KeyFrame(
                  Duration.seconds(15), new KeyValue(polyline2.strokeDashOffsetProperty(), 0)));
      timeline2.setCycleCount(Timeline.INDEFINITE);
      timeline2.play();
    }
  }

  /**
   * Method that is used to update the status of a request when a status is selected and enter is
   * pressed
   */
  @FXML
  public void updateStatus() {
    try {
      Request selectedItem = (Request) foodTable.getSelectionModel().getSelectedItem();
      String curStatus = (String) statusBox.getSelectionModel().getSelectedItem();
      RequestTable.getInstance().editEntry(selectedItem.getRequestID(), "requestStatus", curStatus);
      if (curStatus == "Completed") {
        RequestTable.getInstance()
            .editEntry(selectedItem.getRequestID(), "timeEnd", System.currentTimeMillis());
        RequestTable.getInstance()
            .editEntry(
                selectedItem.getRequestID(), "empCompleter", CurrentUser.getUser().getEmpID());
        LocalDateTime t = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.MINUTES);
        String timeDisplay = t.toString().replace("T", " ");
        timeCompLabel.setText(timeDisplay);
      } else if (curStatus != "Completed") {
        RequestTable.getInstance().editEntry(selectedItem.getRequestID(), "timeEnd", 0);
        RequestTable.getInstance().editEntry(selectedItem.getRequestID(), "empCompleter", 0);
        timeCompLabel.setText("0");
      }
      statusLabel.setText(curStatus);
      refresh();
    } catch (Exception e) {
      // Nothing
    }
  }

  /** Method that displays information about the selected request above the request table */
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
        LocalDateTime startDate =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(tStart), ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.MINUTES);
        String sTimeDisplay = startDate.toString().replace("T", " ");
        // LocalDateTime t = LocalDateTime.now().plusMinutes(1).truncatedTo(ChronoUnit.MINUTES);
        if (tStart == 0) {
          timeStartLabel.setText("0");
        } else {
          timeStartLabel.setText(sTimeDisplay);
        }

        long tEnd = selectedItem.getTimeEnd();
        LocalDateTime endDate =
            LocalDateTime.ofInstant(Instant.ofEpochMilli(tEnd), ZoneId.systemDefault())
                .truncatedTo(ChronoUnit.MINUTES);
        String eTimeDisplay = endDate.toString().replace("T", " ");
        if (tEnd == 0) {
          timeCompLabel.setText("0");
        } else {
          timeCompLabel.setText(eTimeDisplay);
        }

        Integer foodID = selectedItem.getFoodID();
        foodIDLabel.setText(String.valueOf(foodID));

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

  /**
   * Method to create the icons on the map
   *
   * @param loc
   */
  @FXML
  public void createIcon(Location loc) {
    LocationCircle placeHolder = new LocationCircle(loc);
    placeHolder.setLocation(loc);
    placeHolder.setRadius(10);

    locNodePane.getChildren().add(placeHolder);

    placeHolder.setLayoutX(loc.getXcoord());
    placeHolder.setLayoutY(loc.getYcoord());
  }

  /**
   * Method that get the locations from location table for the nodes
   *
   * @param floor
   */
  public void setLocations(String floor) {
    locNodePane.getChildren().clear();
    currentFloor = floor;
    currentLocations = locationTableController.readTable();
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
    animatedPath();
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
    HBox.setMargin(floorL2, new Insets(0, 2, 0, 12));
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

  /**
   * checks to see if the previous path was already use
   *
   * @param start the initial starting location
   * @param end the initial ending location
   * @return returns a bool if the locations match
   */
  public boolean previouslyUsed(String start, String end) {
    return start.equals(startTemp) && end.equals(endTemp);
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
