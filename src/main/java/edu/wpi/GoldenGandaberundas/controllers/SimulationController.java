package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.Main;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Popup;
import net.kurobako.gesturefx.GesturePane;

/** Controller class for template file. Template FXML file is templatetemplate.fxml */

// TODO in template FXML file, change manu bar to button bar. Fix SVG Icons spacing on buttons
// TODO possibly add logo above buttons on side panel

public class SimulationController {

  @FXML JFXButton homeBtn; // home btn with icon and text
  @FXML public JFXButton btn;

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane(imagePane);

  @FXML private Pane nodeDataPane;

  public TableController<Location, String> locations = null;
  private ImageView mapImage = null;
  private Group imageGroup = null;
  private Pane locNodePane = null;
  private Pane pathNodePane = null;
  private MapSubController subController = null;
  private String currentFloor = "1";
  private Group equipGroup = null;
  private Group requestGroup = null;
  private List<String> astar = null;
  private String startTemp = null;
  private String endTemp = null;
  private Integer currentHour = 0;

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE = "-fx-background-color: #002D59;";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59";

  FXMLLoader subControllerLoader =
      new FXMLLoader(Main.class.getResource("views/mapViewDataEntry.fxml"));

  private final Image LL2 = floorMaps.lower2Floor;
  private final Image LL1 = floorMaps.lower1Floor;
  private final Image L1 = floorMaps.firstFloor;
  private final Image L2 = floorMaps.secondFloor;
  private final Image L3 = floorMaps.thirdFloor;

  private ArrayList<Location> currentLocations = null;

  private PathTbl path = PathTbl.getInstance();
  private TableController locationTableController = LocationTbl.getInstance();

  @FXML
  public void initialize() {

    locations = LocationTbl.getInstance();

    // initializes the map views
    // mapImage = new ImageView(floorMaps.groundFloor);
    mapImage = new ImageView(floorMaps.firstFloor);
    // initializes the map group to hold all of the nodes related to the map images
    imageGroup = new Group();
    imageGroup.getChildren().add(mapImage);

    // adds the group and gesture pane to the parent pane
    imagePane.getChildren().add(imageGroup);
    imagePane.getChildren().add(gesturePane);

    // sets the gesture pane to wrap around the image group
    gesturePane.setContent(imageGroup);
    // disables visible scrollbar
    gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    // sets scroll to zoom
    gesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    // the most you can zoom out is 0.005 times the original size
    gesturePane.setMinScale(0.005);
    // fits the image to cover the visible pane
    gesturePane.setFitMode(GesturePane.FitMode.COVER);

    // creates new layer to add the interactive nodes to
    locNodePane = new Pane();

    pathNodePane = new Pane();
    imageGroup.getChildren().add(pathNodePane);

    imageGroup.getChildren().add(locNodePane);

    equipGroup = new Group();
    imageGroup.getChildren().add(equipGroup);

    requestGroup = new Group();
    imageGroup.getChildren().add(requestGroup);

    // creates button to select visible floor
    HBox floorSelect = createFloorSelector();
    floorSelect.setMaxHeight(25);
    imagePane.getChildren().add(floorSelect);
    // aligns floor selector to the bottom left
    imagePane.setAlignment(floorSelect, Pos.BOTTOM_LEFT);

    // attempts to center the pane on launch
    gesturePane.zoomBy(0.005, 0.005, new Point2D(2500, 1700));

    try {
      Node subPane = (Node) subControllerLoader.load();
      nodeDataPane.getChildren().add(subPane);
      subController = subControllerLoader.getController();
      //            subController.setSimulationController(this);
    } catch (IOException e) {
      e.printStackTrace();
    }
    // subController.setText(locations.getEntry("FDEPT00101"));
    //creates the toggle start simulation button
    JFXButton toggleStart = new JFXButton();
    toggleStart.setText("Start Simulation");
    toggleStart.setStyle("-fx-background-color: #0063a9; -fx-text-fill: white");
    toggleStart.setPrefWidth(110);
    toggleStart.setOnMouseReleased(
        e -> {
          Simulation.update();
          currentHour = 0;
          createPath(currentHour);
          toggleStart.setText("Rerun Simulation");
          setEquipment();
        });
    //creates the next hour button
    JFXButton nextHour = new JFXButton();
    nextHour.setText("Next Hour");
    nextHour.setStyle("-fx-background-color: #0063a9; -fx-text-fill: white");
    nextHour.setPrefWidth(110);
    nextHour.setOnMouseReleased(
        e -> {
          pathNodePane.getChildren().clear();
          currentHour++;
          createPath(currentHour);
          setEquipment();
        });
    //previous hour button
    JFXButton prevHour = new JFXButton();
    prevHour.setText("Previous Hour");
    prevHour.setStyle("-fx-background-color: #0063a9; -fx-text-fill: white");
    prevHour.setPrefWidth(110);
    prevHour.setOnMouseReleased(
        e -> {
          pathNodePane.getChildren().clear();
          currentHour--;
          createPath(currentHour);
          setEquipment();
        });

    HBox buttonHolder = new HBox(toggleStart, nextHour, prevHour);
    buttonHolder.setMargin(buttonHolder, new Insets(0, 0, 0, 0));
    buttonHolder.setAlignment(Pos.TOP_LEFT);
    buttonHolder.setSpacing(6);
    Group buttonGroup = new Group();
    buttonGroup.getChildren().add(buttonHolder);
    imagePane.getChildren().add(buttonGroup);
    buttonGroup.setTranslateY(10);
    imagePane.setAlignment(buttonGroup, Pos.TOP_LEFT);

    currentFloor = "1";
    setLocations(currentFloor);
    setEquipment();
    setRequest();
    mapImage.setImage(L1);

    nodeDataPane.setManaged(false);
    nodeDataPane.setVisible(false);
  }

  public void createIcon(Location loc) {
    //    LocationPane placeHolder = new LocationPane(loc);
    //    placeHolder.setMinWidth(10);
    //    placeHolder.setMinHeight(10);

    LocationCircle placeHolder = new LocationCircle(loc);
    placeHolder.setRadius(10);

    locNodePane.getChildren().add(placeHolder);

    placeHolder.setLayoutX(loc.getXcoord());
    placeHolder.setLayoutY(loc.getYcoord());
    //    placeHolder.setStyle("-fx-background-color: black");
    placeHolder.setOnMouseEntered(
        e -> {
          //          placeHolder.setStyle("-fx-background-color: blue");
          placeHolder.setFill(Color.BLUE);
        });
    placeHolder.setOnMouseExited(
        e -> {
          //          placeHolder.setStyle("-fx-background-color: black");
          placeHolder.setFill(Color.BLACK);
        });
    placeHolder.setOnMouseDragged(
        e -> {
          gesturePane.setGestureEnabled(false);
          placeHolder.setLayoutX(placeHolder.getLayoutX() + e.getX());
          placeHolder.setLayoutY(placeHolder.getLayoutY() + e.getY());
        });
    placeHolder.setOnMouseReleased(
        e -> {
          gesturePane.setGestureEnabled(true);
          LocationTbl.getInstance()
              .editEntry(
                  placeHolder.location.getNodeID(), "xCoord", (int) placeHolder.getLayoutX());
          LocationTbl.getInstance()
              .editEntry(
                  placeHolder.location.getNodeID(), "yCoord", (int) placeHolder.getLayoutY());
          placeHolder.location.setXcoord((int) placeHolder.getLayoutX());
          placeHolder.location.setYcoord((int) placeHolder.getLayoutY());
          System.out.println("drag done");
          subController.setText(placeHolder.location);
          System.out.println("clicked");
        });
    placeHolder.setOnContextMenuRequested(
        e -> {
          nodeDataPane.setManaged(true);
          nodeDataPane.setVisible(true);
          nodeDataPane.getChildren().clear();
          FXMLLoader controllerLoader =
              new FXMLLoader(Main.class.getResource("views/mapViewDataEntry.fxml"));
          nodeDataPane.setManaged(true);
          nodeDataPane.setVisible(true);
          try {
            Node subPane = (Node) controllerLoader.load();
            nodeDataPane.getChildren().add(subPane);
            MapSubController subController = controllerLoader.getController();
            //                        subController.setSimulationController(this);
            subController.setText(placeHolder.location);
          } catch (IOException exc) {
            exc.printStackTrace();
          }
          System.out.println("alt click");
        });
  }

  public void createRequestIcon(Request req, Location loc) {
    ReqImageView reqIcon = new ReqImageView(loc, req);
    switch (req.getRequestType()) {
      case "MedEquipDelivery":
        reqIcon.setImage(floorMaps.medicalEquipmentGreen);
        break;
      case "MedicineDelivery":
        reqIcon.setImage(floorMaps.medicineGreen);
        break;
      case "GiftFloral":
        reqIcon.setImage(floorMaps.gift);
        break;
      case "LaundryService":
        reqIcon.setImage(floorMaps.laundry);
        break;
      case "CompServ":
        reqIcon.setImage(floorMaps.computer);
        break;
    }
    reqIcon.setFitHeight(20);
    reqIcon.setFitWidth(20);
    requestGroup.getChildren().add(reqIcon);
    reqIcon.setLayoutX(loc.getXcoord());
    reqIcon.setLayoutY(loc.getYcoord());
    reqIcon.setOnContextMenuRequested(
        e -> {
          nodeDataPane.getChildren().clear();
          FXMLLoader controllerLoader =
              new FXMLLoader(Main.class.getResource("views/mapViewRequestEntry.fxml"));
          nodeDataPane.setManaged(true);
          nodeDataPane.setVisible(true);
          try {
            Node subPane = (Node) controllerLoader.load();
            nodeDataPane.getChildren().add(subPane);
            MapSubReqController subController = controllerLoader.getController();
            //                        subController.setMapController(this);

            subController.setText(reqIcon.request);
          } catch (IOException exc) {
            exc.printStackTrace();
          }
        });
  }

  public void createMedEquipIcon(MedEquipment med, Location loc) {
    MedEqpImageView medIcon = new MedEqpImageView(loc, med);
    medIcon.setFitWidth(20);
    medIcon.setFitHeight(20);

    equipGroup.getChildren().add(medIcon);

    medIcon.setLayoutX(loc.getXcoord() - 5);
    medIcon.setLayoutY(loc.getYcoord() - 5);

    if (medIcon.medEquipment.getMedEquipmentType().trim().toUpperCase(Locale.ROOT).equals("BED")) {
      medIcon.setImage(floorMaps.bedIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("X-RAY")) {
      medIcon.setImage(floorMaps.xRayIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("INFUSION PUMP")) {
      medIcon.setImage(floorMaps.infusionPumpIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("RECLINER")) {
      medIcon.setImage(floorMaps.reclinerIcon);
    } else {
      medIcon.setImage(floorMaps.bedIcon);
    }

    medIcon.setOnMouseEntered(
        e -> {
          medIcon.setStyle("-fx-background-color: green");
        });
    medIcon.setOnMouseExited(
        e -> {
          medIcon.setStyle("-fx-background-color: cyan");
        });

    medIcon.setOnContextMenuRequested(
        e -> {
          Popup popup = new Popup();

          popup.setAnchorX(e.getSceneX());
          popup.setAnchorY(e.getSceneY());
          var popUpLoader = new FXMLLoader(Main.class.getResource("views/mapViewMedEquipLoc.fxml"));
          try {
            AnchorPane popupAnchor = popUpLoader.load();
            popup.getContent().add(popupAnchor);
            popup.show(imagePane.getScene().getWindow());
          } catch (IOException ex) {
            ex.printStackTrace();
          }
          EquipLocEditor popupController = popUpLoader.getController();
          //                    popupController.setMedEquipment(med, this);
          subController.setText(medIcon.location);
        });
    medIcon.setOnMouseDragged(
        e -> {
          gesturePane.setGestureEnabled(false);
          medIcon.setLayoutX(medIcon.getLayoutX() + e.getX());
          medIcon.setLayoutY(medIcon.getLayoutY() + e.getY());
        });
    //
    medIcon.setOnMouseReleased(
        e -> {
          System.out.println("DRAG RELEASE");
          gesturePane.setGestureEnabled(true);
          double mouseX = medIcon.getLayoutX();
          double mouseY = medIcon.getLayoutY();
          double minDist = 100;
          Location snapTo = loc;
          for (Location location : currentLocations) {
            double temp =
                java.awt.geom.Point2D.distance(
                    mouseX, mouseY, location.getXcoord(), location.getYcoord());
            if (temp < minDist) {
              minDist = temp;
              snapTo = location;
            }
          }
          if (!snapTo.getNodeID().equals(loc.getNodeID())) {
            FXMLLoader popupLoader =
                new FXMLLoader(Main.class.getResource("views/confirmationBox.fxml"));
            Popup reqQuestion = new Popup();
            try {
              System.out.println("SNAP TO: " + snapTo);
              reqQuestion.getContent().add(popupLoader.load());
              reqQuestion.show(medIcon, e.getScreenX(), e.getScreenY());
              MapViewConfirmationButtons subController = popupLoader.getController();
              //                            subController.setMainController(this, med, snapTo);
              medIcon.setLayoutX(snapTo.getXcoord() - 5);
              medIcon.setLayoutY(snapTo.getYcoord() - 5);

            } catch (IOException ioException) {
              ioException.printStackTrace();
            }
          } else {
            System.out.println(loc);
            System.out.println(snapTo);
            System.out.println(minDist);
          }
        });
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
        });
    floorL1.setOnAction(
        e -> {
          mapImage.setImage(LL1);
          currentFloor = "L1";
          refreshMap();
        });
    floor01.setOnAction(
        e -> {
          mapImage.setImage(L1);
          currentFloor = "1";
          refreshMap();
        });
    floor02.setOnAction(
        e -> {
          mapImage.setImage(L2);
          currentFloor = "2";
          refreshMap();
        });
    floor03.setOnAction(
        e -> {
          mapImage.setImage(L3);
          currentFloor = "3";
          refreshMap();
        });

    return floorSelect;
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

  public void setEquipment() {
    equipGroup.getChildren().clear();

    TableController<Location, String> locations = LocationTbl.getInstance();
    TableController<MedEquipment, Integer> reqTable = MedEquipmentTbl.getInstance();
    ArrayList<MedEquipment> reqList = reqTable.readTable();
    reqList =
        (ArrayList)
            reqList.stream()
                .filter(
                    l -> {
                      if (locations.getEntry(l.getCurrLoc().trim()) != null) {
                        return (locations.getEntry(l.getCurrLoc().trim()))
                            .getFloor()
                            .equals(currentFloor);
                      }
                      return false;
                    })
                .collect(Collectors.toList());
    for (MedEquipment mer : reqList) {
      createMedEquipIcon(mer, locations.getEntry(mer.getCurrLoc().trim()));
    }
  }

  public void setRequest() {
    requestGroup.getChildren().clear();

    TableController<Location, String> locations = LocationTbl.getInstance();
    TableController<Request, Integer> reqTable = RequestTable.getInstance();
    ArrayList<Request> reqList = reqTable.readTable();
    if (reqList == null || reqList.isEmpty()) {
      return;
    }
    reqList =
        (ArrayList)
            reqList.stream()
                .filter(
                    l -> {
                      if (locations.getEntry(l.getLocationID().trim()) != null) {
                        return (locations.getEntry(l.getLocationID().trim()))
                            .getFloor()
                            .equals(currentFloor);
                      }
                      return false;
                    })
                .collect(Collectors.toList());
    for (Request mer : reqList) {
      createRequestIcon(mer, locations.getEntry(mer.getLocationID().trim()));
    }
  }

  public String getCurrentFloor() {
    return currentFloor;
  }

  public void refreshMap() {
    setLocations(currentFloor);
    setEquipment();
    setRequest();
    refreshPath();
  }

  /** Function for populating the location choice box, called in initialize */
  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
      // locationSearchBox.getItems().add(locArray.get(i).getNodeID());
      // System.out.println(locNodeAr.get(i));
    }
    return locNodeAr;
  }

  /**
   * creates the path for using the a star path
   *
   * @param locs the list of String location node ids
   */
  public void buildPath(List<String> locs) {
    // pathNodePane.getChildren().clear();
    for (int i = 0; i < locs.size() - 1; i++) {
      Location loc = LocationTbl.getInstance().getEntry(locs.get(i));
      Location loc1 = LocationTbl.getInstance().getEntry(locs.get(i + 1));
      PathBar path = new PathBar(loc, loc1);
      pathNodePane.getChildren().add(path);
      if (!path.floor.equals(currentFloor)) {
        path.setVisible(false);
      }
    }
  }

  /** refreshes the map to load the path to the appropriate floor (inspired by Will) */
  public void refreshPath() {
    for (Node p : pathNodePane.getChildren()) {
      PathBar pb = (PathBar) p;
      if (!pb.floor.equals(currentFloor)) {
        pb.setVisible(false);
      } else {
        pb.setVisible(true);
      }
    }
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

  private class MedEqpImageView extends ImageView {
    public Location location = null;
    public MedEquipment medEquipment = null;

    public MedEqpImageView(Location loc, MedEquipment med) {
      super();
      location = loc;
      medEquipment = med;
    }
  }

  private class ReqImageView extends ImageView {
    public Location location = null;
    public Request request = null;

    public ReqImageView(Location loc, Request req) {
      super();
      location = loc;
      request = req;
    }
  }

  private class LocationPane extends Pane {
    public Location location = null;
    public MedEquipment medEquipment = null;

    public LocationPane(Location loc) {
      super();
      location = loc;
    }

    public LocationPane(Location loc, MedEquipment med) {
      super();
      location = loc;
      medEquipment = med;
    }

    void setLocation(Location loc) {
      location = loc;
    }
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

  /** Creates a path bar class for path to follow */
  private class PathBar extends Line {
    private String floor = "00";

    public PathBar(Location startLoc, Location endLoc) {
      super(startLoc.getXcoord(), startLoc.getYcoord(), endLoc.getXcoord(), endLoc.getYcoord());
      this.setStrokeWidth(15);
      this.setStroke(Color.rgb(7, 16, 115));
      if (startLoc.getFloor().equals(endLoc.getFloor())) {
        floor = startLoc.getFloor();
      }
    }

    public String getFloor() {
      return floor;
    }
  }

  /**
   * generated that path based on the input hour
   *
   * @param hour given hour for the simulation
   */
  public void createPath(int hour) {
    for (MedEquipment med : MedEquipmentTbl.getInstance().readTable()) {
      List<String> current = PathTbl.getPathPoints(med.getMedID(), hour);
      astar = PathTbl.getInstance().createAStarPath(current.get(0), current.get(1));
      buildPath(astar);
    }
    pathNodePane.setVisible(true);
  }
}
