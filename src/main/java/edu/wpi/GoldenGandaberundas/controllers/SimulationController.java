package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXNodesList;
import edu.wpi.GoldenGandaberundas.Main;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Font;
import javafx.util.Duration;
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
  private Group animatedPathNodeGroup = null;
  private MapSubController subController = null;
  private String currentFloor = "1";
  private Group equipGroup = null;
  private Group requestGroup = null;
  private List<String> astar = null;
  private Integer currentHour = 0;
  private Integer fasterHour = 0;
  private ArrayList<ArrayList<ArrayList<String>>> coord = null;
  private Integer medEquip;

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
    coord = new ArrayList<ArrayList<ArrayList<String>>>();
    coord.add(new ArrayList<ArrayList<String>>());
    coord.add(new ArrayList<ArrayList<String>>());
    coord.add(new ArrayList<ArrayList<String>>());
    coord.add(new ArrayList<ArrayList<String>>());
    coord.add(new ArrayList<ArrayList<String>>());
    int medStuff = MedEquipmentTbl.getInstance().readTable().size();
    for (int i = 0; i < medStuff; i++) {
      coord.get(0).add(new ArrayList<String>());
      coord.get(1).add(new ArrayList<String>());
      coord.get(2).add(new ArrayList<String>());
      coord.get(3).add(new ArrayList<String>());
      coord.get(4).add(new ArrayList<String>());
    }

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

    imageGroup.getChildren().add(locNodePane);

    equipGroup = new Group();
    imageGroup.getChildren().add(equipGroup);

    requestGroup = new Group();
    imageGroup.getChildren().add(requestGroup);

    animatedPathNodeGroup = new Group();
    imageGroup.getChildren().add(animatedPathNodeGroup);

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
    // creates the hour label for the slider
    Label hourLabel = new Label();
    hourLabel.setFont(Font.font(24));
    hourLabel.setText("Hour : ");
    hourLabel.setStyle("-fx-text-fill: white");
    hourLabel.setPadding(new Insets(0, 0, 0, 10));

    // creates the time label to display the value of the slider
    Label timeLabel = new Label();
    timeLabel.setFont(Font.font(24));
    timeLabel.setStyle("-fx-text-fill: white");

    // creates the slider for the hour simulation
    final Slider timeSlider = new Slider(0.0, 100.0, 48.0);
    timeLabel.textProperty().bind(timeSlider.valueProperty().asString("%.2f"));
    timeSlider.centerShapeProperty().set(true);
    timeSlider.centerShapeProperty().set(true);
    timeSlider.setMajorTickUnit(0.25);
    timeSlider.setMinorTickCount(0);
    timeSlider.setSnapToTicks(true);

    // creates the label for the current hour
    Label currentHourLabel = new Label();
    currentHourLabel.setFont(Font.font(16));
    currentHourLabel.setStyle("-fx-text-fill: white");
    currentHourLabel.setText("Current Hour: 0.0");
    currentHourLabel.setPadding(new Insets(0, 10, 0, 0));

    // creates the toggle start simulation button
    JFXButton toggleStart = new JFXButton();
    toggleStart.setText("Start Simulation");
    toggleStart.setStyle("-fx-text-fill: white");
    toggleStart.setPrefWidth(110);
    toggleStart.setOnMouseReleased(
        e -> {
          Simulation sim = new Simulation();
          sim.update((int) (timeSlider.getValue() * 4));
          animatedPathNodeGroup.getChildren().clear();
          currentHour = 0;
          createPath(currentHour);
          toggleStart.setText("Rerun Simulation");
          currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
          setEquipment();
        });
    // creates the next hour button
    JFXButton nextHour = new JFXButton();
    nextHour.setText("Next Interval");
    nextHour.setStyle("-fx-text-fill: white");
    nextHour.setPrefWidth(110);
    currentHourLabel.setFont(nextHour.getFont());
    nextHour.setOnMouseReleased(
        e -> {
          animatedPathNodeGroup.getChildren().clear();
          currentHour++;
          createPath(currentHour);
          setEquipment();
          currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
        });
    // previous hour button
    JFXButton prevHour = new JFXButton();
    prevHour.setText("Previous Interval");
    prevHour.setStyle("-fx-text-fill: white");
    prevHour.setPrefWidth(110);
    prevHour.setOnMouseReleased(
        e -> {
          animatedPathNodeGroup.getChildren().clear();
          if (currentHour != 0) {
            currentHour--;
          }
          createPath(currentHour);
          setEquipment();
          currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
        });

    // uses the image for the more operations
    ImageView settings = new ImageView(floorMaps.equlaizer);
    settings.setFitHeight(20);
    settings.setFitWidth(20);

    // creates the more settings button
    JFXButton moreOperations = new JFXButton("More", settings);
    moreOperations.setStyle("-fx-text-fill: white");

    // creates dots to open the lower nodes list
    JFXButton dotsForMore = new JFXButton();
    dotsForMore.setText("...");
    dotsForMore.setStyle(
        "-fx-background-color: #002D59; -fx-text-fill: white;-fx-background-radius: 5px");
    dotsForMore.setDefaultButton(true);
    dotsForMore.setPrefWidth(75);
    dotsForMore.setPrefHeight(25);

    // creates the more time label to display the value of the slider
    Label moreHourLabel = new Label();
    moreHourLabel.setFont(Font.font(18));
    moreHourLabel.setStyle(
        "-fx-background-color: #002D59; -fx-text-fill: white;-fx-background-radius: 5px;\n"
            + "\n"
            + "   -fx-border-radius: 5px;\n");
    moreHourLabel.setPrefWidth(75);
    moreHourLabel.setPrefHeight(25);
    moreHourLabel.setPadding(new Insets(0, 5, 0, 5));

    // creates the hour label for the slider
    Label moreHourText = new Label();
    moreHourText.setFont(Font.font(18));
    moreHourText.setText("Hour : ");
    moreHourText.setStyle("-fx-text-fill: white");
    moreHourText.setPadding(new Insets(0, 0, 0, 10));
    moreHourText.setStyle(
        "-fx-background-color: #002D59; -fx-text-fill: white;-fx-background-radius: 5px;\n"
            + "\n"
            + "    -fx-border-radius: 5px;\n");
    moreHourText.setPrefWidth(75);
    moreHourText.setPrefHeight(25);

    // creates the slider for more hour simulation
    final Slider moreHourSlider = new Slider(0.0, 2.0, 0.25);
    moreHourLabel.textProperty().bind(moreHourSlider.valueProperty().asString("%.2f"));
    moreHourSlider.centerShapeProperty().set(true);
    moreHourSlider.setMajorTickUnit(0.25);
    moreHourSlider.setMinorTickCount(0);
    moreHourSlider.setSnapToTicks(true);
    moreHourSlider.setStyle(
        "-fx-background-color: #002D59; -fx-text-fill: white;-fx-background-radius: 5px;\n"
            + "\n"
            + "    -fx-border-radius: 5px;\n");
    moreHourSlider.setPrefWidth(75);
    moreHourSlider.setPrefHeight(25);
    moreHourSlider.setPadding(new Insets(5, 7, 5, 5));

    JFXButton simulateLonger = new JFXButton();
    simulateLonger.setText("Simulate");
    simulateLonger.setStyle(
        "-fx-background-color: #002D59; -fx-text-fill: white;-fx-background-radius: 5px;\n"
            + "\n"
            + "    -fx-border-radius: 5px;\n");
    simulateLonger.setFont(Font.font(14));
    simulateLonger.setPrefWidth(75);
    simulateLonger.setPrefHeight(25);
    simulateLonger.setPadding(new Insets(0, 0, 0, 0));

    simulateLonger.setOnMouseReleased(
        e -> {
          animatedPathNodeGroup.getChildren().clear();
          fasterHour = (int) (moreHourSlider.getValue() * 4);
          createPathLonger(currentHour, fasterHour);
          currentHour = currentHour + fasterHour;
          setEquipment();
          currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
        });

    JFXNodesList evenMore = new JFXNodesList();
    evenMore.setRotate(270);
    evenMore.spacingProperty().setValue(50);
    evenMore.addAnimatedNode(dotsForMore);
    evenMore.addAnimatedNode(moreHourText);
    evenMore.addAnimatedNode(moreHourLabel);
    evenMore.addAnimatedNode(moreHourSlider);
    evenMore.addAnimatedNode(simulateLonger);

    JFXNodesList moreSim = new JFXNodesList();
    moreSim.spacingProperty().setValue(10);
    moreSim.addAnimatedNode(moreOperations);
    moreSim.addAnimatedNode(evenMore);

    HBox buttonHolder =
        new HBox(
            moreSim,
            hourLabel,
            timeLabel,
            timeSlider,
            toggleStart,
            nextHour,
            prevHour,
            currentHourLabel);
    buttonHolder.setStyle("-fx-background-color: #002D59");
    buttonHolder.centerShapeProperty().set(true);
    buttonHolder.setAlignment(Pos.CENTER);
    buttonHolder.setSpacing(6);
    Group buttonGroup = new Group();
    buttonGroup.getChildren().add(buttonHolder);
    buttonGroup.setTranslateY(10);
    imagePane.getChildren().add(buttonGroup);
    imagePane.setAlignment(buttonGroup, Pos.TOP_LEFT);

    currentFloor = "1";
    setLocations(currentFloor);
    setEquipment();
    // setRequest();
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

  public void createMedEquipIcon(MedEquipment med, Location loc) {
    MedEqpImageView medIcon = new MedEqpImageView(loc, med);
    medIcon.setFitWidth(20);
    medIcon.setFitHeight(20);

    equipGroup.getChildren().add(medIcon);

    if (medIcon.medEquipment.getMedEquipmentType().trim().toUpperCase(Locale.ROOT).equals("BED")) {
      medIcon.setLayoutX(loc.getXcoord() - 22);
      medIcon.setLayoutY(loc.getYcoord() - 22);
      medIcon.setImage(floorMaps.bedIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("X-RAY")) {
      medIcon.setLayoutX(loc.getXcoord() + 5);
      medIcon.setLayoutY(loc.getYcoord() + 5);
      medIcon.setImage(floorMaps.xRayIcon);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("INFUSION PUMP")) {
      medIcon.setImage(floorMaps.infusionPumpIcon);
      medIcon.setLayoutX(loc.getXcoord() - 10);
      medIcon.setLayoutY(loc.getYcoord() + 10);
    } else if (medIcon
        .medEquipment
        .getMedEquipmentType()
        .trim()
        .toUpperCase(Locale.ROOT)
        .equals("RECLINER")) {
      medIcon.setLayoutX(loc.getXcoord() + 10);
      medIcon.setLayoutY(loc.getYcoord() - 10);
      medIcon.setImage(floorMaps.reclinerIcon);
    } else {
      medIcon.setImage(floorMaps.bedIcon);
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

  public void refreshMap() {
    setLocations(currentFloor);
    setEquipment();
    animateFloor();
  }

  /** Function for populating the location choice box, called in initialize */
  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
    }
    return locNodeAr;
  }

  /**
   * generated that path based on the input hour
   *
   * @param hour given hour for the simulation
   */
  public void createPath(int hour) {
    medEquip = 0;
    animatedPathNodeGroup.getChildren().clear();
    for (MedEquipment med : MedEquipmentTbl.getInstance().readTable()) {
      List<String> current = PathTbl.getPathPoints(med.getMedID(), hour);
      if (current.get(0).equals(current.get(1))) {
        clearMedPath();
        medEquip++;
        continue;
      }
      astar = PathTbl.getInstance().createAStarPath(current.get(0), current.get(1));
      dividePath(astar);
      animatedPath();
      medEquip++;
    }
  }

  /**
   * Creates the path for a longer period of time
   *
   * @param currentHour the initial time interval
   * @param fasterHour the end time interval
   */
  public void createPathLonger(int currentHour, int fasterHour) {
    medEquip = 0;
    animatedPathNodeGroup.getChildren().clear();
    for (MedEquipment med : MedEquipmentTbl.getInstance().readTable()) {
      List<String> current = PathTbl.getPathPointsFaster(med.getMedID(), currentHour, fasterHour);
      if (current.get(0).equals(current.get(1))) {
        clearMedPath();
        medEquip++;
        continue;
      }
      astar = PathTbl.getInstance().createAStarPath(current.get(0), current.get(1));
      dividePath(astar);
      animatedPath();
      medEquip++;
    }
  }

  /**
   * Divides the list of locations from the astar to change between floors
   *
   * @param locs the list of location names from the path
   */
  public void dividePath(List<String> locs) {
    clearMedPath();

    for (int i = 0; i < locs.size(); i++) {
      if (locs.get(i).substring(locs.get(i).length() - 2).equals("L2")) {
        coord.get(0).get(medEquip).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 2).equals("L1")) {
        coord.get(1).get(medEquip).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("1")) {
        coord.get(2).get(medEquip).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("2")) {
        coord.get(3).get(medEquip).add(locs.get(i));
      } else if (locs.get(i).substring(locs.get(i).length() - 1).equals("3")) {
        coord.get(4).get(medEquip).add(locs.get(i));
      }
    }
  }

  /** Generate the path based on the current floor */
  public void animatedPath() {
    ArrayList<String> locs = new ArrayList<>();
    if (currentFloor.equals("L2")) {
      if (coord.get(0).get(medEquip).isEmpty()) {
        return;
      }
      locs = coord.get(0).get(medEquip);

    } else if (currentFloor.equals("L1")) {
      if (coord.get(1).get(medEquip).isEmpty()) {
        return;
      }
      locs = coord.get(1).get(medEquip);

    } else if (currentFloor.equals("1")) {
      if (coord.get(2).get(medEquip).isEmpty()) {
        return;
      }
      locs = coord.get(2).get(medEquip);

    } else if (currentFloor.equals("2")) {
      if (coord.get(3).get(medEquip).isEmpty()) {
        return;
      }
      locs = coord.get(3).get(medEquip);

    } else if (currentFloor.equals("3")) {
      if (coord.get(4).get(medEquip).isEmpty()) {
        return;
      }
      locs = coord.get(4).get(medEquip);
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
    String color;
    if (MedEquipmentTbl.getInstance().getEntry(medEquip + 1).getMedEquipmentType().equals("Bed")) {
      color = "#FF0000";
    } else if (MedEquipmentTbl.getInstance()
        .getEntry(medEquip + 1)
        .getMedEquipmentType()
        .equals("Recliner")) {
      color = "#800080";
    } else if (MedEquipmentTbl.getInstance()
        .getEntry(medEquip + 1)
        .getMedEquipmentType()
        .equals("X-ray")) {
      color = "000000";
    } else {
      color = "#006db3";
    }

    polyline.setStroke(Color.web(color));
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

    // Runs when the edge case is met
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
      String color2 = "";
      if (MedEquipmentTbl.getInstance()
          .getEntry(medEquip + 1)
          .getMedEquipmentType()
          .equals("Bed")) {
        color2 = "#FF0000";
      } else if (MedEquipmentTbl.getInstance()
          .getEntry(medEquip + 1)
          .getMedEquipmentType()
          .equals("Recliner")) {
        color2 = "#800080";
      } else if (MedEquipmentTbl.getInstance()
          .getEntry(medEquip + 1)
          .getMedEquipmentType()
          .equals("X-ray")) {
        color2 = "000000";
      } else {
        color2 = "#006db3";
      }
      polyline2.setStroke(Color.web(color2));
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
   * Iterates through the medical equipment and prints the correct path (used for floor switching)
   */
  public void animateFloor() {
    animatedPathNodeGroup.getChildren().clear();
    for (int i = 0; i < MedEquipmentTbl.getInstance().readTable().size(); i++) {
      medEquip = i;
      animatedPath();
    }
  }

  /** Clears the astar path for based on the current medical equipment */
  public void clearMedPath() {
    coord.get(0).get(medEquip).clear();
    coord.get(1).get(medEquip).clear();
    coord.get(2).get(medEquip).clear();
    coord.get(3).get(medEquip).clear();
    coord.get(4).get(medEquip).clear();
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
