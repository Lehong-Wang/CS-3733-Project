package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.stream.Collectors;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import net.kurobako.gesturefx.GesturePane;

public class TowerSideViewController {

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane(imagePane);

  @FXML private StackPane mapStack;
  @FXML private GesturePane mapPane = new GesturePane(mapStack);
  private Group mapGroup = new Group();
  private Group equipGroup = new Group();

  private ImageView towerImage = new ImageView(floorMaps.towerSideviewCropped);
  private Group imageGroup = null;

  private ImageView mapImage = new ImageView(floorMaps.firstFloor);

  @FXML private Label cleanBedLabel;
  @FXML private Label dirtyBedLabel;
  @FXML private Label cleanReclinerLabel;
  @FXML private Label dirtyReclinerLabel;
  @FXML private Label cleanPumpsLabel;
  @FXML private Label dirtyPumpsLabel;
  @FXML private Label xrayLabel;
  @FXML private Label floorLabel;

  @FXML TableView medEquipmentTable;
  @FXML private TableColumn<MedEquipment, String> type;
  @FXML private TableColumn<MedEquipment, Integer> equipmentID;
  @FXML private TableColumn<MedEquipment, String> location2;
  @FXML private TableColumn<MedEquipment, String> status;

  private TableController menuTableController = MedEquipmentTbl.getInstance();

  // Floor 3 Icon
  private ImageView floor3Icon = new ImageView(floorMaps.thirdFloorIcon);
  private IconDisplay dirtyBedIcon3;
  private IconDisplay cleanBedIcon3;
  private IconDisplay dirtyReclinerIcon3;
  private IconDisplay cleanReclinerIcon3;
  private IconDisplay dirtyInfusionIcon3;
  private IconDisplay cleanInfusionIcon3;
  private IconDisplay xRayIcon3;

  // Floor 1 Icon
  private ImageView floor1Icon = new ImageView(floorMaps.firstFloorIcon);
  private IconDisplay dirtyBedIcon1;
  private IconDisplay cleanBedIcon1;
  private IconDisplay dirtyReclinerIcon1;
  private IconDisplay cleanReclinerIcon1;
  private IconDisplay dirtyInfusionIcon1;
  private IconDisplay cleanInfusionIcon1;
  private IconDisplay xRayIcon1;

  // Lower Floor 1 Icon
  private ImageView floorL1Icon = new ImageView(floorMaps.lowerFirstFloorIcon);
  private IconDisplay dirtyBedIconL1;
  private IconDisplay cleanBedIconL1;
  private IconDisplay dirtyReclinerIconL1;
  private IconDisplay cleanReclinerIconL1;
  private IconDisplay dirtyInfusionIconL1;
  private IconDisplay cleanInfusionIconL1;
  private IconDisplay xRayIconL1;

  ArrayList<MedEquipment> filteredEquipments3 = new ArrayList<>();
  int dirtyBed3 = 0;
  int cleanBed3 = 0;
  int dirtyRecliner3 = 0;
  int cleanRecliner3 = 0;
  int dirtyPump3 = 0;
  int cleanPump3 = 0;
  int xray3 = 0;

  ArrayList<MedEquipment> filteredEquipments1 = new ArrayList<>();
  int dirtyBed1 = 0;
  int cleanBed1 = 0;
  int dirtyRecliner1 = 0;
  int cleanRecliner1 = 0;
  int dirtyPump1 = 0;
  int cleanPump1 = 0;
  int xray1 = 0;

  ArrayList<MedEquipment> filteredEquipmentsL1 = new ArrayList<>();
  int dirtyBedL1 = 0;
  int cleanBedL1 = 0;
  int dirtyReclinerL1 = 0;
  int cleanReclinerL1 = 0;
  int dirtyPumpL1 = 0;
  int cleanPumpL1 = 0;
  int xrayL1 = 0;

  mainController main = null;
  private String currentFloor = "1";
  ImageView thirdFloorMap = new ImageView(floorMaps.thirdFloor);
  ImageView firstFloorMap = new ImageView(floorMaps.firstFloor);
  ImageView lowerFirstFloorMap = new ImageView(floorMaps.lower1Floor);
  ImageView floorMap = firstFloorMap;

  @FXML
  public void setMainController(mainController realMain) {
    this.main = realMain;
  }

  @FXML
  public void initialize() {
    // Sorts equipments into their floors for display later
    medEquipmentTable.getItems().setAll(menuTableController.readTable());
    ArrayList<MedEquipment> equipments = menuTableController.readTable();
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

    // Sorts equipment for floor 3
    for (int i = 0; i < filteredEquipments3.size(); i++) {
      String type = filteredEquipments3.get(i).getMedEquipmentType().trim();
      String status = filteredEquipments3.get(i).getStatus().trim();
      if (type.equals("Bed")) {
        if (status.equals("Stored")) {
          cleanBed3++;
        } else {
          dirtyBed3++;
        }
      } else if (type.equals("Recliner")) {
        if (status.equals("Stored")) {
          cleanRecliner3++;
        } else {
          dirtyRecliner3++;
        }
      } else if (type.equals("Infusion Pump")) {
        if (status.equals("Stored")) {
          cleanPump3++;
        } else {
          dirtyPump3++;
        }
      } else if (type.equals("X-ray")) {
        xray3++;
      }
    }

    // Sorts equipment for floor 1
    for (int i = 0; i < filteredEquipments1.size(); i++) {
      String type = filteredEquipments1.get(i).getMedEquipmentType().trim();
      String status = filteredEquipments1.get(i).getStatus().trim();
      if (type.equals("Bed")) {
        if (status.equals("Stored")) {
          cleanBed1++;
        } else {
          dirtyBed1++;
        }
      } else if (type.equals("Recliner")) {
        if (status.equals("Stored")) {
          cleanRecliner1++;
        } else {
          dirtyRecliner1++;
        }
      } else if (type.equals("Infusion Pump")) {
        if (status.equals("Stored")) {
          cleanPump1++;
        } else {
          dirtyPump1++;
        }
      } else if (type.equals("X-ray")) {
        xray1++;
      }
    }

    // Sorts equipment for lower floor 1
    for (int i = 0; i < filteredEquipmentsL1.size(); i++) {
      String type = filteredEquipmentsL1.get(i).getMedEquipmentType().trim();
      String status = filteredEquipmentsL1.get(i).getStatus().trim();
      if (type.equals("Bed")) {
        if (status.equals("Stored")) {
          cleanBedL1++;
        } else {
          dirtyBedL1++;
        }
      } else if (type.equals("Recliner")) {
        if (status.equals("Stored")) {
          cleanReclinerL1++;
        } else {
          dirtyReclinerL1++;
        }
      } else if (type.equals("Infusion Pump")) {
        if (status.equals("Stored")) {
          cleanPumpL1++;
        } else {
          dirtyPumpL1++;
        }
      } else if (type.equals("X-ray")) {
        xrayL1++;
      }
    }

    cleanBedLabel.setText((cleanBed1 + cleanBed3 + cleanBedL1) + "");
    dirtyBedLabel.setText((dirtyBed1 + dirtyBed3 + dirtyBedL1) + "");
    cleanReclinerLabel.setText((cleanRecliner1 + cleanRecliner3 + cleanReclinerL1) + "");
    dirtyReclinerLabel.setText((dirtyRecliner1 + dirtyRecliner3 + dirtyReclinerL1) + "");
    cleanPumpsLabel.setText((cleanPump1 + cleanPump3 + cleanPumpL1) + "");
    dirtyPumpsLabel.setText((dirtyPump1 + dirtyPump3 + dirtyPumpL1) + "");
    xrayLabel.setText((xray1 + xray3 + xrayL1) + "");
    floorLabel.setText("Building Summary");

    type.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("medEquipmentType"));
    equipmentID.setCellValueFactory(new PropertyValueFactory<MedEquipment, Integer>("medID"));
    location2.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("currLoc"));
    status.setCellValueFactory(new PropertyValueFactory<MedEquipment, String>("status"));

    // Sets the location of all the icons
    // third floor icons
    setFloorIcon(floor3Icon);
    setLocation(floor3Icon, 330, 123);

    dirtyBedIcon3 = new IconDisplay(1, dirtyBed3, 1);
    dirtyBedIcon3.setLocation(240, 153);
    dirtyBedIcon3.setOnMouseEntered(
        e -> {
          dirtyBedIcon3.hideIcon();
        });
    dirtyBedIcon3.setOnMouseExited(
        e -> {
          dirtyBedIcon3.showIcon();
        });

    cleanBedIcon3 = new IconDisplay(2, cleanBed3, 2);
    cleanBedIcon3.setLocation(65, 153);
    cleanBedIcon3.setOnMouseEntered(
        e -> {
          cleanBedIcon3.hideIcon();
        });
    cleanBedIcon3.setOnMouseExited(
        e -> {
          cleanBedIcon3.showIcon();
        });

    cleanReclinerIcon3 = new IconDisplay(4, cleanRecliner3, 2);
    cleanReclinerIcon3.setLocation(107, 153);
    cleanReclinerIcon3.setOnMouseEntered(
        e -> {
          cleanReclinerIcon3.hideIcon();
        });
    cleanReclinerIcon3.setOnMouseExited(
        e -> {
          cleanReclinerIcon3.showIcon();
        });

    cleanInfusionIcon3 = new IconDisplay(6, cleanPump3, 2);
    cleanInfusionIcon3.setLocation(145, 153);
    cleanInfusionIcon3.setOnMouseEntered(
        e -> {
          cleanInfusionIcon3.hideIcon();
        });
    cleanInfusionIcon3.setOnMouseExited(
        e -> {
          cleanInfusionIcon3.showIcon();
        });

    xRayIcon3 = new IconDisplay(7, xray3, 3);
    xRayIcon3.setLocation(195, 153);
    xRayIcon3.setOnMouseEntered(
        e -> {
          xRayIcon3.hideIcon();
        });
    xRayIcon3.setOnMouseExited(
        e -> {
          xRayIcon3.showIcon();
        });

    dirtyReclinerIcon3 = new IconDisplay(3, dirtyRecliner3, 1);
    dirtyReclinerIcon3.setLocation(278, 153);
    dirtyReclinerIcon3.setOnMouseEntered(
        e -> {
          dirtyReclinerIcon3.hideIcon();
        });
    dirtyReclinerIcon3.setOnMouseExited(
        e -> {
          dirtyReclinerIcon3.showIcon();
        });

    dirtyInfusionIcon3 = new IconDisplay(5, dirtyPump3, 1);
    dirtyInfusionIcon3.setLocation(315, 153);
    dirtyInfusionIcon3.setOnMouseEntered(
        e -> {
          dirtyInfusionIcon3.hideIcon();
        });
    dirtyInfusionIcon3.setOnMouseExited(
        e -> {
          dirtyInfusionIcon3.showIcon();
        });

    // First Floor Icons
    setFloorIcon(floor1Icon);
    setLocation(floor1Icon, 330, 226);

    dirtyBedIcon1 = new IconDisplay(1, dirtyBed1, 1);
    dirtyBedIcon1.setLocation(240, 256);
    dirtyBedIcon1.setOnMouseEntered(
        e -> {
          dirtyBedIcon1.hideIcon();
        });
    dirtyBedIcon1.setOnMouseExited(
        e -> {
          dirtyBedIcon1.showIcon();
        });

    cleanBedIcon1 = new IconDisplay(2, cleanBed1, 2);
    cleanBedIcon1.setLocation(65, 256);
    cleanBedIcon1.setOnMouseEntered(
        e -> {
          cleanBedIcon1.hideIcon();
        });
    cleanBedIcon1.setOnMouseExited(
        e -> {
          cleanBedIcon1.showIcon();
        });

    cleanReclinerIcon1 = new IconDisplay(4, cleanRecliner1, 2);
    cleanReclinerIcon1.setLocation(107, 256);
    cleanReclinerIcon1.setOnMouseEntered(
        e -> {
          cleanReclinerIcon1.hideIcon();
        });
    cleanReclinerIcon1.setOnMouseExited(
        e -> {
          cleanReclinerIcon1.showIcon();
        });

    cleanInfusionIcon1 = new IconDisplay(6, cleanPump1, 2);
    cleanInfusionIcon1.setLocation(145, 256);
    cleanInfusionIcon1.setOnMouseEntered(
        e -> {
          cleanInfusionIcon1.hideIcon();
        });
    cleanInfusionIcon1.setOnMouseExited(
        e -> {
          cleanInfusionIcon1.showIcon();
        });

    xRayIcon1 = new IconDisplay(7, xray1, 3);
    xRayIcon1.setLocation(195, 256);
    xRayIcon1.setOnMouseEntered(
        e -> {
          xRayIcon1.hideIcon();
        });
    xRayIcon1.setOnMouseExited(
        e -> {
          xRayIcon1.showIcon();
        });

    dirtyReclinerIcon1 = new IconDisplay(3, dirtyRecliner1, 1);
    dirtyReclinerIcon1.setLocation(278, 256);
    dirtyReclinerIcon1.setOnMouseEntered(
        e -> {
          dirtyReclinerIcon1.hideIcon();
        });
    dirtyReclinerIcon1.setOnMouseExited(
        e -> {
          dirtyReclinerIcon1.showIcon();
        });

    dirtyInfusionIcon1 = new IconDisplay(5, dirtyPump1, 1);
    dirtyInfusionIcon1.setLocation(315, 256);
    dirtyInfusionIcon1.setOnMouseEntered(
        e -> {
          dirtyInfusionIcon1.hideIcon();
        });
    dirtyInfusionIcon1.setOnMouseExited(
        e -> {
          dirtyInfusionIcon1.showIcon();
        });

    // Lower 1 Floor Icons
    setFloorIcon(floorL1Icon);
    setLocation(floorL1Icon, 330, 280);

    dirtyBedIconL1 = new IconDisplay(1, dirtyBedL1, 1);
    dirtyBedIconL1.setLocation(240, 310);
    dirtyBedIconL1.setOnMouseEntered(
        e -> {
          dirtyBedIconL1.hideIcon();
        });
    dirtyBedIconL1.setOnMouseExited(
        e -> {
          dirtyBedIconL1.showIcon();
        });

    cleanBedIconL1 = new IconDisplay(2, cleanBedL1, 2);
    cleanBedIconL1.setLocation(65, 310);
    cleanBedIconL1.setOnMouseEntered(
        e -> {
          cleanBedIconL1.hideIcon();
        });
    cleanBedIconL1.setOnMouseExited(
        e -> {
          cleanBedIconL1.showIcon();
        });

    cleanReclinerIconL1 = new IconDisplay(4, cleanReclinerL1, 2);
    cleanReclinerIconL1.setLocation(107, 310);
    cleanReclinerIconL1.setOnMouseEntered(
        e -> {
          cleanReclinerIconL1.hideIcon();
        });
    cleanReclinerIconL1.setOnMouseExited(
        e -> {
          cleanReclinerIconL1.showIcon();
        });

    cleanInfusionIconL1 = new IconDisplay(6, cleanPumpL1, 2);
    cleanInfusionIconL1.setLocation(145, 310);
    cleanInfusionIconL1.setOnMouseEntered(
        e -> {
          cleanInfusionIconL1.hideIcon();
        });
    cleanInfusionIconL1.setOnMouseExited(
        e -> {
          cleanInfusionIconL1.showIcon();
        });

    xRayIconL1 = new IconDisplay(7, xrayL1, 3);
    xRayIconL1.setLocation(195, 310);
    xRayIconL1.setOnMouseEntered(
        e -> {
          xRayIconL1.hideIcon();
        });
    xRayIconL1.setOnMouseExited(
        e -> {
          xRayIconL1.showIcon();
        });

    dirtyReclinerIconL1 = new IconDisplay(3, dirtyReclinerL1, 1);
    dirtyReclinerIconL1.setLocation(278, 310);
    dirtyReclinerIconL1.setOnMouseEntered(
        e -> {
          dirtyReclinerIconL1.hideIcon();
        });
    dirtyReclinerIconL1.setOnMouseExited(
        e -> {
          dirtyReclinerIconL1.showIcon();
        });

    dirtyInfusionIconL1 = new IconDisplay(5, dirtyPumpL1, 1);
    dirtyInfusionIconL1.setLocation(315, 310);
    dirtyInfusionIconL1.setOnMouseEntered(
        e -> {
          dirtyInfusionIconL1.hideIcon();
        });
    dirtyInfusionIconL1.setOnMouseExited(
        e -> {
          dirtyInfusionIconL1.showIcon();
        });

    // initializes the map group to hold all of the nodes related to the map images
    imageGroup = new Group();
    imageGroup
        .getChildren()
        .addAll(
            towerImage,
            floor3Icon,
            cleanBedIcon3,
            cleanReclinerIcon3,
            cleanInfusionIcon3,
            xRayIcon3,
            dirtyBedIcon3,
            dirtyReclinerIcon3,
            dirtyInfusionIcon3,
            floor1Icon,
            cleanBedIcon1,
            cleanReclinerIcon1,
            cleanInfusionIcon1,
            xRayIcon1,
            dirtyBedIcon1,
            dirtyReclinerIcon1,
            dirtyInfusionIcon1,
            floorL1Icon,
            cleanBedIconL1,
            cleanReclinerIconL1,
            cleanInfusionIconL1,
            xRayIconL1,
            dirtyBedIconL1,
            dirtyReclinerIconL1,
            dirtyInfusionIconL1);

    // adds the group and gesture pane to the parent pane
    imagePane.getChildren().add(imageGroup);
    imagePane.getChildren().add(gesturePane);

    // sets the gesture pane to wrap around the image group
    gesturePane.setContent(imageGroup);
    // fits the image to cover the visible pane
    gesturePane.setFitMode(GesturePane.FitMode.FIT);
    gesturePane.setScrollMode(GesturePane.ScrollMode.PAN);
    gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    gesturePane.setMinScale(1);

    // map image to switch between
    mapGroup.getChildren().add(mapImage);

    mapStack.getChildren().add(mapGroup);
    mapStack.getChildren().add(mapPane);
    mapPane.setContent(mapGroup);
    mapPane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollMode(GesturePane.ScrollMode.ZOOM);
    mapPane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    mapPane.setMinScale(0.001);
    mapPane.setMaxSize(500, 300);
    mapPane.zoomBy(0.22, 0.18, new Point2D(2500, 1700));
    mapStack.setMaxSize(500, 300);
    equipGroup = new Group();
    mapGroup.getChildren().add(equipGroup);

    setEquipment();

    mapPane.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            try {
              main.switchMapView();
            } catch (IOException ex) {
              ex.printStackTrace();
            }
          }
        });

    // event click to pull up floor equipment summary
    floor3Icon.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            setFloor3();
          }
          currentFloor = "3";
          floorMap = thirdFloorMap;
          setEquipment();
        });
    floor1Icon.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            setFloor1();
          }
          currentFloor = "1";
          floorMap = firstFloorMap;
          setEquipment();
        });

    floorL1Icon.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            setFloorL1();
          }
          currentFloor = "L1";
          floorMap = lowerFirstFloorMap;
          setEquipment();
        });
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
  }

  // sets the view to the third floor
  private void setFloor3() {
    medEquipmentTable.getItems().setAll(filteredEquipments3);
    floorLabel.setText("Floor 3:");
    cleanBedLabel.setText(cleanBed3 + "");
    dirtyBedLabel.setText(dirtyBed3 + "");
    cleanReclinerLabel.setText(cleanRecliner3 + "");
    dirtyReclinerLabel.setText(dirtyRecliner3 + "");
    cleanPumpsLabel.setText(cleanPump3 + "");
    dirtyPumpsLabel.setText(dirtyPump3 + "");
    xrayLabel.setText(xray3 + "");
    mapImage.setImage(floorMaps.thirdFloor);
  }

  // sets the view to the first floor
  private void setFloor1() {
    medEquipmentTable.getItems().setAll(filteredEquipments1);
    floorLabel.setText("Floor 1:");
    cleanBedLabel.setText(cleanBed1 + "");
    dirtyBedLabel.setText(dirtyBed1 + "");
    cleanReclinerLabel.setText(cleanRecliner1 + "");
    dirtyReclinerLabel.setText(dirtyRecliner1 + "");
    cleanPumpsLabel.setText(cleanPump1 + "");
    dirtyPumpsLabel.setText(dirtyPump1 + "");
    xrayLabel.setText(xray1 + "");
    mapImage.setImage(floorMaps.firstFloor);
  }

  // sets the view to the lower first floor
  private void setFloorL1() {
    medEquipmentTable.getItems().setAll(filteredEquipmentsL1);
    floorLabel.setText("Lower Floor 1:");
    cleanBedLabel.setText(cleanBedL1 + "");
    dirtyBedLabel.setText(dirtyBedL1 + "");
    cleanReclinerLabel.setText(cleanReclinerL1 + "");
    dirtyReclinerLabel.setText(dirtyReclinerL1 + "");
    cleanPumpsLabel.setText(cleanPumpL1 + "");
    dirtyPumpsLabel.setText(dirtyPumpL1 + "");
    xrayLabel.setText(xrayL1 + "");
    mapImage.setImage(floorMaps.lower1Floor);
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

  // sets the icon sizes to 1.5 times their current
  /**
   * sets the icons to 0.65 their size
   *
   * @param image the icon
   */
  private void setIcon(ImageView image) {
    image.setScaleX(.65);
    image.setScaleY(.65);
  }

  // hides icons

  /**
   * turns off visibility for an icon
   *
   * @param image the icon
   */
  private void hideIcon(ImageView image) {
    image.setScaleX(0);
    image.setScaleY(0);
  }

  // sets the floor number icons tp 0.35 their size

  /**
   * sets a floor icon to .3 their size
   *
   * @param image the floor icon
   */
  private void setFloorIcon(ImageView image) {
    image.setScaleX(.30);
    image.setScaleY(.30);
  }

  // sets the location of the icons

  /**
   * sets the location of an icon
   *
   * @param image icon to be set
   * @param x x corrdinate
   * @param y y coordinate
   */
  private void setLocation(ImageView image, int x, int y) {
    image.setLayoutX(x);
    image.setLayoutY(y);
  }

  /**
   * makes starting positions for icons
   *
   * @param image icon to set
   * @param x the x coordinate of the icon
   * @param y the y coordinate of the icon
   * @param num the number of the equipment the icon represents
   */
  private void startIcon(ImageView image, int x, int y, int num) {
    if (num == 0) {
      hideIcon(image);
    } else {
      setIcon(image);
    }
    setLocation(image, x, y);
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

    public EquipLabel(int display, int circle) {
      super();
      if (circle == 1) {
        this.circle = new Circle(16, Color.rgb(113, 0, 0));
      } else if (circle == 2) {
        this.circle = new Circle(16, Color.rgb(117, 218, 122));
      } else {
        this.circle = new Circle(16, Color.rgb(0, 45, 89));
      }
      text = new Text(display + "");
      text.setFill(Color.WHITE);
      text.setBoundsType(TextBoundsType.VISUAL);
      text.setFont(Font.font("Open Sans", 22));
      super.getChildren().addAll(this.circle, text);
    }

    /**
     * sets the text of the circle
     *
     * @param text text to display
     */
    public void setText(String text) {
      this.text.setText(text + "");
    }

    /**
     * sets the location of the pane
     *
     * @param x x coordinate
     * @param y y coordinate
     */
    public void setLocation(int x, int y) {
      super.setTranslateX(x);
      super.setTranslateY(y);
    }
  }

  /** displays an icon and the amount on each floor with a mouse hover */
  public class IconDisplay extends StackPane {
    private final ImageView BED_RED = new ImageView(floorMaps.redBed);
    private final ImageView BED_GREEN = new ImageView(floorMaps.greenBed);
    private final ImageView RECLINER_RED = new ImageView(floorMaps.redRecliner);
    private final ImageView RECLINER_GREEN = new ImageView(floorMaps.greenRecliner);
    private final ImageView PUMP_RED = new ImageView(floorMaps.redInfusionPump);
    private final ImageView PUMP_GREEN = new ImageView(floorMaps.greenInfusionPump);
    private final ImageView XRAY = new ImageView(floorMaps.xRayIcon);
    private ImageView icon;

    private EquipLabel label;

    public IconDisplay(int iconNum, int display, int color) {
      super();
      switch (iconNum) {
        case 1:
          icon = BED_RED;
          break;
        case 2:
          icon = BED_GREEN;
          break;
        case 3:
          icon = RECLINER_RED;
          break;
        case 4:
          icon = RECLINER_GREEN;
          break;
        case 5:
          icon = PUMP_RED;
          break;
        case 6:
          icon = PUMP_GREEN;
          break;
        default:
          icon = XRAY;
          break;
      }
      setIcon(icon);
      label = new EquipLabel(display, color);
      this.getChildren().addAll(label, icon);
      if (display == 0) {
        this.setVisible(false);
      } else {
        this.setVisible(true);
      }
    }

    /**
     * sets the icon scale
     *
     * @param image the icon image
     */
    private void setIcon(ImageView image) {
      image.setScaleX(.65);
      image.setScaleY(.65);
    }

    /** hides the icon */
    public void hideIcon() {
      icon.setVisible(false);
    }

    /** shows the icon */
    public void showIcon() {
      icon.setVisible(true);
    }

    /**
     * sets the location of the icon
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public void setLocation(int x, int y) {
      this.setLayoutX(x);
      this.setLayoutY(y);
    }
  }
}
