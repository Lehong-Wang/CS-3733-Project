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
import java.util.*;
import java.util.List;
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
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polyline;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
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
  @FXML private GesturePane towerPane = new GesturePane();

  @FXML private Pane nodeDataPane;
  @FXML private Pane sideViewPane;

  @FXML GridPane gridPane = new GridPane();
  @FXML private Label traveledID = new Label();
  @FXML private Label traveledNum = new Label();
  @FXML private Label mostTraveledID = new Label();
  @FXML private Label mostTraveledNum = new Label();
  @FXML private Label most2TraveledID = new Label();
  @FXML private Label most2TraveledNum = new Label();
  @FXML private Label most3TraveledID = new Label();
  @FXML private Label most3TraveledNum = new Label();
  @FXML private Label most4TraveledID = new Label();
  @FXML private Label most4TraveledNum = new Label();
  @FXML private Label most5TraveledID = new Label();
  @FXML private Label most5TraveledNum = new Label();
  @FXML private VBox statsBox = new VBox();
  @FXML private Label statsTitle = new Label();

  public TableController<Location, String> locations = null;
  private ImageView mapImage = null;
  private Group imageGroup = null;
  private ImageView sideTower = null;
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
  private boolean start = true;
  private boolean next = false;
  private Integer simVal = 0;
  CategoryAxis nodes = new CategoryAxis();
  NumberAxis mostTraveled = new NumberAxis();

  private Rectangle rect;

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
  private TableController menuTableController = MedEquipmentTbl.getInstance();

  // IconDisplays for the side tower view
  IconDisplay bed3;
  IconDisplay recliner3;
  IconDisplay pump3;
  IconDisplay xray3;

  IconDisplay bed1;
  IconDisplay recliner1;
  IconDisplay pump1;
  IconDisplay xray1;

  IconDisplay bedL1;
  IconDisplay reclinerL1;
  IconDisplay pumpL1;
  IconDisplay xrayL1;

  ArrayList<MedEquipment> filteredEquipments3 = new ArrayList<>();
  ArrayList<MedEquipment> filteredEquipments1 = new ArrayList<>();
  ArrayList<MedEquipment> filteredEquipmentsL1 = new ArrayList<>();

  @FXML
  public void initialize() {
    traveledID.setText("Location ID");
    traveledID.setStyle("-fx-text-fill: white; -fx-underline: true");
    traveledNum.setText("Occurrences");
    traveledNum.setStyle("-fx-text-fill: white; -fx-underline: true");
    mostTraveledID.setStyle("-fx-text-fill: white");
    mostTraveledNum.setStyle("-fx-text-fill: white");

    most2TraveledID.setStyle("-fx-text-fill: white");
    most2TraveledNum.setStyle("-fx-text-fill: white");

    most3TraveledID.setStyle("-fx-text-fill: white");
    most3TraveledNum.setStyle("-fx-text-fill: white");

    most4TraveledID.setStyle("-fx-text-fill: white");
    most4TraveledNum.setStyle("-fx-text-fill: white");

    most5TraveledID.setStyle("-fx-text-fill: white");
    most5TraveledNum.setStyle("-fx-text-fill: white");

    gridPane.setMaxSize(200, 100);
    gridPane.add(traveledID, 1, 1);
    gridPane.add(traveledNum, 2, 1);
    gridPane.add(mostTraveledID, 1, 2);
    gridPane.add(mostTraveledNum, 2, 2);
    gridPane.add(most2TraveledID, 1, 3);
    gridPane.add(most2TraveledNum, 2, 3);
    gridPane.add(most3TraveledID, 1, 4);
    gridPane.add(most3TraveledNum, 2, 4);
    gridPane.add(most4TraveledID, 1, 5);
    gridPane.add(most4TraveledNum, 2, 5);
    gridPane.add(most5TraveledID, 1, 6);
    gridPane.add(most5TraveledNum, 2, 6);
    gridPane.setStyle("-fx-text-fill: white");
    gridPane.alignmentProperty().set(Pos.CENTER);
    gridPane.setHgap(20);
    gridPane.setVgap(4);
    gridPane.setPadding(new Insets(2, 12, 2, 0));
    gridPane.setCenterShape(true);

    statsTitle.setText("Most Locations Traveled");
    statsTitle.setStyle("-fx-text-fill: black;-fx-text-fill: white; -fx-font-size: 16");
    statsTitle.alignmentProperty().setValue(Pos.CENTER);

    statsBox.getChildren().add(statsTitle);
    statsBox.getChildren().add(gridPane);
    statsBox.setStyle(
        "-fx-background-color: #002D59;-fx-background-radius: 5px;-fx-border-radius: 5px");
    statsBox.setMaxSize(200, 120);
    statsBox.alignmentProperty().set(Pos.CENTER);

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
    animatedPathNodeGroup.setStyle("-fx-background-color: red");
    imageGroup.getChildren().add(animatedPathNodeGroup);

    // creates button to select visible floor
    HBox floorSelect = createFloorSelector();
    floorSelect.setMaxHeight(25);
    imagePane.getChildren().add(floorSelect);
    // aligns floor selector to the bottom left
    imagePane.setAlignment(floorSelect, Pos.BOTTOM_LEFT);

    // attempts to center the pane on launch
    gesturePane.zoomBy(0.005, 0.005, new Point2D(2500, 1700));

    // Sets the side image
    sideViewPane = new Pane();
    sideTower = new ImageView(floorMaps.towerSideview);
    sideTower.setScaleX(.75);
    sideTower.setScaleY(.75);
    towerPane.setContent(sideTower);
    sideViewPane.getChildren().add(towerPane);
    sideViewPane.setMaxSize(0, 0);
    sideViewPane.setTranslateX(350);
    sideViewPane.setTranslateY(-420);
    rect = new Rectangle(310, 38);
    rect.setFill(Color.LIGHTGREEN);
    sideViewPane.getChildren().add(rect);
    rect.setX(50);
    rect.setY(282);

    // Sorts equipments into their floors for display later
    sortEquipment(menuTableController.readTable());
    // Adding icons on tower
    // floor 3 icons
    int[] equipmentNum = getEquipNum(filteredEquipments3);
    bed3 = new IconDisplay(1, equipmentNum[0] + equipmentNum[1]);
    bed3.setTranslateX(102);
    bed3.setTranslateY(160);
    bed3.setOnMouseEntered(
        e -> {
          bed3.hideIcon();
        });
    bed3.setOnMouseExited(
        e -> {
          bed3.showIcon();
        });
    recliner3 = new IconDisplay(2, equipmentNum[2] + equipmentNum[3]);
    recliner3.setTranslateX(162);
    recliner3.setTranslateY(160);
    recliner3.setOnMouseEntered(
        e -> {
          recliner3.hideIcon();
        });
    recliner3.setOnMouseExited(
        e -> {
          recliner3.showIcon();
        });

    pump3 = new IconDisplay(3, equipmentNum[4] + equipmentNum[5]);
    pump3.setTranslateX(222);
    pump3.setTranslateY(160);
    pump3.setOnMouseEntered(
        e -> {
          pump3.hideIcon();
        });
    pump3.setOnMouseExited(
        e -> {
          pump3.showIcon();
        });

    xray3 = new IconDisplay(4, equipmentNum[6]);
    xray3.setTranslateX(282);
    xray3.setTranslateY(160);
    xray3.setOnMouseEntered(
        e -> {
          xray3.hideIcon();
        });
    xray3.setOnMouseExited(
        e -> {
          xray3.showIcon();
        });

    // floor 1 icons
    equipmentNum = getEquipNum(filteredEquipments1);
    bed1 = new IconDisplay(1, equipmentNum[0] + equipmentNum[1]);
    bed1.setTranslateX(102);
    bed1.setTranslateY(238);
    bed1.setOnMouseEntered(
        e -> {
          bed1.hideIcon();
        });
    bed1.setOnMouseExited(
        e -> {
          bed1.showIcon();
        });

    recliner1 = new IconDisplay(2, equipmentNum[2] + equipmentNum[3]);
    recliner1.setTranslateX(162);
    recliner1.setTranslateY(238);
    recliner1.setOnMouseEntered(
        e -> {
          recliner1.hideIcon();
        });
    recliner1.setOnMouseExited(
        e -> {
          recliner1.showIcon();
        });

    pump1 = new IconDisplay(3, equipmentNum[4] + equipmentNum[5]);
    pump1.setTranslateX(222);
    pump1.setTranslateY(238);
    pump1.setOnMouseEntered(
        e -> {
          pump1.hideIcon();
        });
    pump1.setOnMouseExited(
        e -> {
          pump1.showIcon();
        });

    xray1 = new IconDisplay(4, equipmentNum[6]);
    xray1.setTranslateX(282);
    xray1.setTranslateY(238);
    xray1.setOnMouseEntered(
        e -> {
          xray1.hideIcon();
        });
    xray1.setOnMouseExited(
        e -> {
          xray1.showIcon();
        });

    // floor L1 icons
    equipmentNum = getEquipNum(filteredEquipmentsL1);
    bedL1 = new IconDisplay(1, equipmentNum[0] + equipmentNum[1]);
    bedL1.setTranslateX(102);
    bedL1.setTranslateY(277);
    bedL1.setOnMouseEntered(
        e -> {
          bedL1.hideIcon();
        });
    bedL1.setOnMouseExited(
        e -> {
          bedL1.showIcon();
        });

    reclinerL1 = new IconDisplay(2, equipmentNum[2] + equipmentNum[3]);
    reclinerL1.setTranslateX(162);
    reclinerL1.setTranslateY(277);
    reclinerL1.setOnMouseEntered(
        e -> {
          reclinerL1.hideIcon();
        });
    reclinerL1.setOnMouseExited(
        e -> {
          reclinerL1.showIcon();
        });

    pumpL1 = new IconDisplay(3, equipmentNum[4] + equipmentNum[5]);
    pumpL1.setTranslateX(222);
    pumpL1.setTranslateY(277);
    pumpL1.setOnMouseEntered(
        e -> {
          pumpL1.hideIcon();
        });
    pumpL1.setOnMouseExited(
        e -> {
          pumpL1.showIcon();
        });

    xrayL1 = new IconDisplay(4, equipmentNum[6]);
    xrayL1.setTranslateX(282);
    xrayL1.setTranslateY(277);
    xrayL1.setOnMouseEntered(
        e -> {
          xrayL1.hideIcon();
        });
    xrayL1.setOnMouseExited(
        e -> {
          xrayL1.showIcon();
        });

    sideViewPane
        .getChildren()
        .addAll(
            bed3,
            recliner3,
            pump3,
            xray3,
            bed1,
            recliner1,
            pump1,
            xray1,
            bedL1,
            reclinerL1,
            pumpL1,
            xrayL1);

    imagePane.getChildren().add(sideViewPane);
    sideTower.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            if (bed3.getDisplay()) {
              bed3.setDisplay(false);
              recliner3.setDisplay(false);
              pump3.setDisplay(false);
              xray3.setDisplay(false);
              bed1.setDisplay(false);
              recliner1.setDisplay(false);
              pump1.setDisplay(false);
              xray1.setDisplay(false);
              bedL1.setDisplay(false);
              reclinerL1.setDisplay(false);
              pumpL1.setDisplay(false);
              xrayL1.setDisplay(false);
            } else {
              bed3.setDisplay(true);
              recliner3.setDisplay(true);
              pump3.setDisplay(true);
              xray3.setDisplay(true);
              bed1.setDisplay(true);
              recliner1.setDisplay(true);
              pump1.setDisplay(true);
              xray1.setDisplay(true);
              bedL1.setDisplay(true);
              reclinerL1.setDisplay(true);
              pumpL1.setDisplay(true);
              xrayL1.setDisplay(true);
            }
          }
        });
    imagePane.getChildren().add(statsBox);
    statsBox.setTranslateX(600);
    statsBox.setTranslateY(50);

    try {
      Node subPane = (Node) subControllerLoader.load();
      nodeDataPane.getChildren().add(subPane);
      subController = subControllerLoader.getController();
      //      subController.setSimulationController(this);
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
    timeLabel.setPrefWidth(60);

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

    // creates the next hour button
    JFXButton nextHour = new JFXButton();
    nextHour.setText("Next Interval");
    nextHour.setStyle("-fx-text-fill: white");
    nextHour.setPrefWidth(110);
    nextHour.setDisable(true);
    currentHourLabel.setFont(nextHour.getFont());

    // previous hour button
    JFXButton prevHour = new JFXButton();
    prevHour.setText("Previous Interval");
    prevHour.setStyle("-fx-text-fill: white");
    prevHour.setPrefWidth(110);
    prevHour.setDisable(true);

    JFXButton simulateLonger = new JFXButton();
    simulateLonger.setText("Simulate");
    simulateLonger.setStyle(
        "-fx-background-color: #002D59; -fx-text-fill: white;-fx-background-radius: 5px;\n"
            + "\n"
            + "    -fx-border-radius: 5px;\n");
    simulateLonger.setDisable(true);
    simulateLonger.setFont(Font.font(14));
    simulateLonger.setPrefWidth(75);
    simulateLonger.setPrefHeight(25);
    simulateLonger.setPadding(new Insets(0, 0, 0, 0));

    toggleStart.setOnMouseReleased(
        e -> {
          if (start) {
            Simulation sim = new Simulation();
            sim.update((int) (timeSlider.getValue() * 4));
            simVal = (int) (timeSlider.getValue() * 4);
          }
          animatedPathNodeGroup.getChildren().clear();
          nextHour.setDisable(false);
          prevHour.setDisable(false);
          simulateLonger.setDisable(false);
          start = true;
          next = false;
          currentHour = 0;
          createPath(currentHour);
          toggleStart.setText("Restart Sim");
          currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
          setEquipment();
          updateStats();
          start = false;
        });

    nextHour.setOnMouseReleased(
        e -> {
          animatedPathNodeGroup.getChildren().clear();
          next = true;
          currentHour++;
          if (currentHour != simVal) {
            createPath(currentHour - 1);
            updateStats();
            setEquipment();
            currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
          } else {
            createPath(simVal - 2);
            nextHour.setDisable(true);
            simulateLonger.setDisable(true);
            currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
          }
        });

    prevHour.setOnMouseReleased(
        e -> {
          animatedPathNodeGroup.getChildren().clear();
          next = false;
          nextHour.setDisable(false);
          simulateLonger.setDisable(false);
          if (currentHour != 0) {
            currentHour--;
          }
          createPath(currentHour - 1);
          updateStats();
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

    simulateLonger.setOnMouseReleased(
        e -> {
          animatedPathNodeGroup.getChildren().clear();
          next = true;
          fasterHour = (int) (moreHourSlider.getValue() * 4);
          if ((fasterHour + currentHour) < simVal) {
            createPathLonger(currentHour, fasterHour);
            currentHour = currentHour + fasterHour;
            setEquipment();
            updateStats();
            currentHourLabel.setText("Current Hour: " + (double) currentHour / 4.0 + " ");
          } else {
            createPathLonger(currentHour - 1, simVal - 1);
            currentHour = simVal;
            setEquipment();
            updateStats();
            currentHourLabel.setText("Current Hour: " + (double) simVal / 4.0 + " ");
            simulateLonger.setDisable(true);
            nextHour.setDisable(true);
          }
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
    buttonHolder.setMargin(moreSim, new Insets(0, 0, 0, 10));
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

    sideViewPane.toFront();
  }

  /**
   * gets an array of the number of each equipment
   *
   * @param filteredEquipments a list of equipment to sort
   * @return int array of the equipment amounts
   */
  public int[] getEquipNum(ArrayList<MedEquipment> filteredEquipments) {
    int cleanBed = 0;
    int dirtyBed = 0;
    int cleanRecliner = 0;
    int dirtyRecliner = 0;
    int cleanPump = 0;
    int dirtyPump = 0;
    int xray = 0;
    for (int i = 0; i < filteredEquipments.size(); i++) {
      String type = filteredEquipments.get(i).getMedEquipmentType().trim();
      String status = filteredEquipments.get(i).getStatus().trim();
      if (type.equals("Bed")) {
        if (status.equals("Stored")) {
          cleanBed++;
        } else {
          dirtyBed++;
        }
      } else if (type.equals("Recliner")) {
        if (status.equals("Stored")) {
          cleanRecliner++;
        } else {
          dirtyRecliner++;
        }
      } else if (type.equals("Infusion Pump")) {
        if (status.equals("Stored")) {
          cleanPump++;
        } else {
          dirtyPump++;
        }
      } else if (type.equals("X-ray")) {
        xray++;
      }
    }
    return new int[] {cleanBed, dirtyBed, cleanRecliner, dirtyRecliner, cleanPump, dirtyPump, xray};
  }

  /**
   * sorts a list of medical equipments into their respective floors
   *
   * @param equipments
   */
  private void sortEquipment(ArrayList<MedEquipment> equipments) {
    filteredEquipments3 = new ArrayList<>();
    filteredEquipments1 = new ArrayList<>();
    filteredEquipmentsL1 = new ArrayList<>();
    for (int i = 0; i < equipments.size(); i++) {
      String nodeID = equipments.get(i).getCurrLoc();
      int nodeLength = nodeID.length();
      if (nodeID.substring(nodeLength - 2, nodeLength).equals("03"))
        filteredEquipments3.add(equipments.get(i));
      if (nodeID.substring(nodeLength - 2, nodeLength).equals("01"))
        filteredEquipments1.add(equipments.get(i));
      if (nodeID.substring(nodeLength - 2, nodeLength).equals("L1"))
        filteredEquipmentsL1.add(equipments.get(i));
    }
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
    HBox.setMargin(floorL2, new Insets(0, 2, 0, 13));
    HBox.setMargin(floor03, new Insets(0, 2, 0, 3));
    floorL2.setOnAction(
        e -> {
          mapImage.setImage(LL2);
          currentFloor = "L2";
          refreshMap();
          rect.setY(322);
        });
    floorL1.setOnAction(
        e -> {
          mapImage.setImage(LL1);
          currentFloor = "L1";
          refreshMap();
          rect.setY(282);
        });
    floor01.setOnAction(
        e -> {
          mapImage.setImage(L1);
          currentFloor = "1";
          refreshMap();
          rect.setY(242);
        });
    floor02.setOnAction(
        e -> {
          mapImage.setImage(L2);
          currentFloor = "2";
          refreshMap();
          rect.setY(204);
        });
    floor03.setOnAction(
        e -> {
          mapImage.setImage(L3);
          currentFloor = "3";
          refreshMap();
          rect.setY(166);
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

  /** refreshes the map when needed */
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
    for (MedEquipment med : (ArrayList<MedEquipment>) MedEquipmentTbl.getInstance().readTable()) {
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
      if (currentHour == 0) {
        PathTbl.zeroPathStats();
      } else if (!start && next) {
        PathTbl.pathStats(astar);
      } else if (!start && !next) {
        PathTbl.pathRemoveStats(astar);
      }
    }
    updateSideView(MedEquipmentTbl.getInstance().readTable());
    sideViewPane.toFront();
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
    for (MedEquipment med : (ArrayList<MedEquipment>) MedEquipmentTbl.getInstance().readTable()) {
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
      if (fasterHour == 0) {
        PathTbl.zeroPathStats();
      } else if (!start && next) {
        PathTbl.pathStats(astar);
      }
    }
    updateSideView(MedEquipmentTbl.getInstance().readTable());
    sideViewPane.toFront();
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
      if (locs.get(i).substring(1, 5).equals("ELEV")
              && locs.get(i + 1).substring(1, 5).equals("ELEV")
          || locs.get(i + 1).substring(1, 5).equals("ELEV")
              && locs.get(i).substring(1, 5).equals("ELEV")) {
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

  public void updateSideView(ArrayList<MedEquipment> equipment) {
    sortEquipment(equipment);
    int[] equipmentNum = getEquipNum(filteredEquipments3);
    bed3.setDisplayNum(equipmentNum[0] + equipmentNum[1]);
    //    bed3.setOnMouseEntered(
    //        e -> {
    //          bed3.hideIcon();
    //        });
    //    bed3.setOnMouseExited(
    //        e -> {
    //          bed3.showIcon();
    //        });
    recliner3.setDisplayNum(equipmentNum[2] + equipmentNum[3]);
    //    recliner3.setOnMouseEntered(
    //        e -> {
    //          recliner3.hideIcon();
    //        });
    //    recliner3.setOnMouseExited(
    //        e -> {
    //          recliner3.showIcon();
    //        });

    pump3.setDisplayNum(equipmentNum[4] + equipmentNum[5]);
    //    pump3.setOnMouseEntered(
    //        e -> {
    //          pump3.hideIcon();
    //        });
    //    pump3.setOnMouseExited(
    //        e -> {
    //          pump3.showIcon();
    //        });

    xray3.setDisplayNum(equipmentNum[6]);
    //    xray3.setOnMouseEntered(
    //        e -> {
    //          xray3.hideIcon();
    //        });
    //    xray3.setOnMouseExited(
    //        e -> {
    //          xray3.showIcon();
    //        });

    // floor 1 icons
    equipmentNum = getEquipNum(filteredEquipments1);
    bed1.setDisplayNum(equipmentNum[0] + equipmentNum[1]);
    //    bed1.setOnMouseEntered(
    //        e -> {
    //          bed1.hideIcon();
    //        });
    //    bed1.setOnMouseExited(
    //        e -> {
    //          bed1.showIcon();
    //        });

    recliner1.setDisplayNum(equipmentNum[2] + equipmentNum[3]);
    //    recliner1.setOnMouseEntered(
    //        e -> {
    //          recliner1.hideIcon();
    //        });
    //    recliner1.setOnMouseExited(
    //        e -> {
    //          recliner1.showIcon();
    //        });

    pump1.setDisplayNum(equipmentNum[4] + equipmentNum[5]);
    //    pump1.setOnMouseEntered(
    //        e -> {
    //          pump1.hideIcon();
    //        });
    //    pump1.setOnMouseExited(
    //        e -> {
    //          pump1.showIcon();
    //        });

    xray1.setDisplayNum(equipmentNum[6]);
    //    xray1.setOnMouseEntered(
    //        e -> {
    //          xray1.hideIcon();
    //        });
    //    xray1.setOnMouseExited(
    //        e -> {
    //          xray1.showIcon();
    //        });

    // floor L1 icons
    equipmentNum = getEquipNum(filteredEquipmentsL1);
    bedL1.setDisplayNum(equipmentNum[0] + equipmentNum[1]);
    //    bedL1.setOnMouseEntered(
    //        e -> {
    //          bedL1.hideIcon();
    //        });
    //    bedL1.setOnMouseExited(
    //        e -> {
    //          bedL1.showIcon();
    //        });

    reclinerL1.setDisplayNum(equipmentNum[2] + equipmentNum[3]);
    //    reclinerL1.setOnMouseEntered(
    //        e -> {
    //          reclinerL1.hideIcon();
    //        });
    //    reclinerL1.setOnMouseExited(
    //        e -> {
    //          reclinerL1.showIcon();
    //        });

    pumpL1.setDisplayNum(equipmentNum[4] + equipmentNum[5]);
    //    pumpL1.setOnMouseEntered(
    //        e -> {
    //          pumpL1.hideIcon();
    //        });
    //    pumpL1.setOnMouseExited(
    //        e -> {
    //          pumpL1.showIcon();
    //        });

    xrayL1.setDisplayNum(equipmentNum[6]);
    //    xrayL1.setOnMouseEntered(
    //        e -> {
    //          xrayL1.hideIcon();
    //        });
    //    xrayL1.setOnMouseExited(
    //        e -> {
    //          xrayL1.showIcon();
    //        });
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

  /** Class to show the equipment with circles */
  private class EquipmentCircle extends Circle {
    private String floor = "00";

    public EquipmentCircle(Location startLoc, Location endLoc) {
      super();
      if (startLoc.getFloor().equals(endLoc.getFloor())) {
        floor = startLoc.getFloor();
      }
    }

    public String getFloor() {
      return floor;
    }
  }
  /** creates a stack pane that displays a number in a navy circle */
  private class EquipLabel extends StackPane {
    private Text text;
    Circle circle;

    public EquipLabel() {
      super();
      circle = new Circle(16, Color.rgb(0, 45, 89));
      text = new Text("");
      text.setFill(Color.WHITE);
      text.setBoundsType(TextBoundsType.VISUAL);
      text.setFont(Font.font("Open Sans", 22));
      super.getChildren().addAll(circle, text);
    }

    public EquipLabel(int display) {
      super();
      circle = new Circle(16, Color.rgb(0, 45, 89));
      text = new Text(display + "");
      text.setFill(Color.WHITE);
      text.setBoundsType(TextBoundsType.VISUAL);
      text.setFont(Font.font("Open Sans", 22));
      super.getChildren().addAll(circle, text);
    }

    /**
     * sets the text of the circle
     *
     * @param text text to display
     */
    public void setText(int text) {
      this.text.setText(text + "");
    }
  }

  /** displays an icon and the amount on each floor with a mouse hover */
  public class IconDisplay extends StackPane {
    private final ImageView BED = new ImageView(floorMaps.bedIcon);
    private final ImageView RECLINER = new ImageView(floorMaps.reclinerIcon);
    private final ImageView PUMP = new ImageView(floorMaps.infusionPumpIcon);
    private final ImageView XRAY = new ImageView(floorMaps.xRayIcon);
    private ImageView icon;
    private boolean displayed = true;

    private EquipLabel label;

    public IconDisplay(int iconNum, int display) {
      super();
      switch (iconNum) {
        case 1:
          icon = BED;
          break;
        case 2:
          icon = RECLINER;
          break;
        case 3:
          icon = PUMP;
          break;
        default:
          icon = XRAY;
          break;
      }
      setIcon(icon);
      label = new EquipLabel(display);
      this.getChildren().addAll(label, icon);
    }

    /**
     * gets the display statu
     *
     * @return true if icon visible
     */
    public boolean getDisplay() {
      return displayed;
    }

    /**
     * sets the display mode
     *
     * @param bool
     */
    public void setDisplay(boolean bool) {
      displayed = bool;
      if (bool) {
        showIcon();
      } else {
        hideIcon();
      }
    }

    /**
     * sets the label to a new number
     *
     * @param num the number to change it to
     */
    public void setDisplayNum(int num) {
      label.setText(num);
    }

    /**
     * sets the Icon to 0.65 the size
     *
     * @param image
     */
    private void setIcon(ImageView image) {
      image.setScaleX(.65);
      image.setScaleY(.65);
    }

    /** makes the icon invisible */
    public void hideIcon() {
      icon.setVisible(false);
    }

    /** makes the icon visible */
    public void showIcon() {
      if (displayed) icon.setVisible(true);
    }
  }

  /** Updates the most traveled through node based on the top five objects in the sorted hash map */
  public void updateStats() {
    Map<String, Integer> hm = sortByValue(PathTbl.getStatsMap());
    int count = 0;
    ArrayList<String> nodes = new ArrayList<>();
    ArrayList<Integer> num = new ArrayList<>();

    for (Map.Entry<String, Integer> en : hm.entrySet()) {
      nodes.add(en.getKey());
      num.add(en.getValue());
      if (count == 4) {
        break;
      }
      count++;
    }

    mostTraveledID.setText(nodes.get(0));
    most2TraveledID.setText(nodes.get(1));
    most3TraveledID.setText(nodes.get(2));
    most4TraveledID.setText(nodes.get(3));
    most5TraveledID.setText(nodes.get(4));

    mostTraveledNum.setText(num.get(0).toString());
    most2TraveledNum.setText(num.get(1).toString());
    most3TraveledNum.setText(num.get(2).toString());
    most4TraveledNum.setText(num.get(3).toString());
    most5TraveledNum.setText(num.get(4).toString());
  }

  /**
   * Organizes a given hashmap in decending order to get the most passed through Node
   *
   * @param hm the unsorted hash map
   * @return the sorted hash map
   */
  public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm) {
    List<Map.Entry<String, Integer>> list =
        new LinkedList<Map.Entry<String, Integer>>(hm.entrySet());

    Collections.sort(
        list,
        new Comparator<Map.Entry<String, Integer>>() {
          public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
            return (obj2.getValue()).compareTo(obj1.getValue());
          }
        });
    HashMap<String, Integer> finHash = new LinkedHashMap<String, Integer>();
    for (Map.Entry<String, Integer> ent : list) {
      finHash.put(ent.getKey(), ent.getValue());
    }
    return finHash;
  }
}
