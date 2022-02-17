package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import net.kurobako.gesturefx.GesturePane;

public class TowerSideViewController {

  @FXML private StackPane imagePane;
  @FXML private GesturePane gesturePane = new GesturePane(imagePane);

  private ImageView towerImage = new ImageView(floorMaps.towerSideviewCropped);
  private Group imageGroup = null;

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
  private ImageView dirtyBedIcon3 = new ImageView(floorMaps.redBed);
  private ImageView cleanBedIcon3 = new ImageView(floorMaps.greenBed);
  private ImageView dirtyReclinerIcon3 = new ImageView(floorMaps.redRecliner);
  private ImageView cleanReclinerIcon3 = new ImageView(floorMaps.greenRecliner);
  private ImageView dirtyInfusionIcon3 = new ImageView(floorMaps.redInfusionPump);
  private ImageView cleanInfusionIcon3 = new ImageView(floorMaps.greenInfusionPump);
  private ImageView xRayIcon3 = new ImageView(floorMaps.xRayIcon);

  // Floor 1 Icon
  private ImageView floor1Icon = new ImageView(floorMaps.firstFloorIcon);
  private ImageView dirtyBedIcon1 = new ImageView(floorMaps.redBed);
  private ImageView cleanBedIcon1 = new ImageView(floorMaps.greenBed);
  private ImageView dirtyReclinerIcon1 = new ImageView(floorMaps.redRecliner);
  private ImageView cleanReclinerIcon1 = new ImageView(floorMaps.greenRecliner);
  private ImageView dirtyInfusionIcon1 = new ImageView(floorMaps.redInfusionPump);
  private ImageView cleanInfusionIcon1 = new ImageView(floorMaps.greenInfusionPump);
  private ImageView xRayIcon1 = new ImageView(floorMaps.xRayIcon);

  // Lower Floor 1 Icon
  private ImageView floorL1Icon = new ImageView(floorMaps.lowerFirstFloorIcon);
  private ImageView dirtyBedIconL1 = new ImageView(floorMaps.redBed);
  private ImageView cleanBedIconL1 = new ImageView(floorMaps.greenBed);
  private ImageView dirtyReclinerIconL1 = new ImageView(floorMaps.redRecliner);
  private ImageView cleanReclinerIconL1 = new ImageView(floorMaps.greenRecliner);
  private ImageView dirtyInfusionIconL1 = new ImageView(floorMaps.redInfusionPump);
  private ImageView cleanInfusionIconL1 = new ImageView(floorMaps.greenInfusionPump);
  private ImageView xRayIconL1 = new ImageView(floorMaps.xRayIcon);

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

    setLocation(cleanBedIcon3, 65, 153);
    if (cleanBed3 == 0) {
      hideIcon(cleanBedIcon3);
    } else {
      setIcon(cleanBedIcon3);
    }

    setLocation(cleanReclinerIcon3, 107, 153);
    if (cleanRecliner3 == 0) {
      hideIcon(cleanReclinerIcon3);
    } else {
      setIcon(cleanReclinerIcon3);
    }

    setLocation(cleanInfusionIcon3, 145, 153);
    if (cleanPump3 == 0) {
      hideIcon(cleanInfusionIcon3);
    } else {
      setIcon(cleanInfusionIcon3);
    }

    setLocation(xRayIcon3, 195, 153);
    if (xray3 == 0) {
      hideIcon(xRayIcon3);
    } else {
      setIcon(xRayIcon3);
    }

    setLocation(dirtyBedIcon3, 240, 153);
    if (dirtyBed3 == 0) {
      hideIcon(dirtyBedIcon3);
    } else {
      setIcon(dirtyBedIcon3);
    }

    setLocation(dirtyReclinerIcon3, 278, 153);
    if (dirtyRecliner3 == 0) {
      hideIcon(dirtyReclinerIcon3);
    } else {
      setIcon(dirtyReclinerIcon3);
    }

    setLocation(dirtyInfusionIcon3, 315, 153);
    if (dirtyPump3 == 0) {
      hideIcon(dirtyInfusionIcon3);
    } else {
      setIcon(dirtyInfusionIcon3);
    }

    // First Floor Icons
    setFloorIcon(floor1Icon);
    setLocation(floor1Icon, 330, 226);

    if (cleanBed1 == 0) {
      hideIcon(cleanBedIcon1);
    } else {
      setIcon(cleanBedIcon1);
    }
    setLocation(cleanBedIcon1, 65, 256);

    if (cleanRecliner1 == 0) {
      hideIcon(cleanReclinerIcon1);
    } else {
      setIcon(cleanReclinerIcon1);
    }
    setLocation(cleanReclinerIcon1, 107, 256);

    if (cleanPump1 == 0) {
      hideIcon(cleanInfusionIcon1);
    } else {
      setIcon(cleanInfusionIcon1);
    }
    setLocation(cleanInfusionIcon1, 145, 256);

    if (xray1 == 0) {
      hideIcon(xRayIcon1);
    } else {
      setIcon(xRayIcon1);
    }
    setLocation(xRayIcon1, 195, 256);

    if (dirtyBed1 == 0) {
      hideIcon(dirtyBedIcon1);
    } else {
      setIcon(dirtyBedIcon1);
    }
    setLocation(dirtyBedIcon1, 240, 256);

    if (dirtyRecliner1 == 0) {
      hideIcon(dirtyReclinerIcon1);
    } else {
      setIcon(dirtyReclinerIcon1);
    }
    setLocation(dirtyReclinerIcon1, 278, 256);

    if (dirtyPump1 == 0) {
      hideIcon(dirtyInfusionIcon1);
    } else {
      setIcon(dirtyInfusionIcon1);
    }
    setLocation(dirtyInfusionIcon1, 315, 256);

    // Lower 1 Floor Icons
    setFloorIcon(floorL1Icon);
    setLocation(floorL1Icon, 330, 280);

    if (cleanBedL1 == 0) {
      hideIcon(cleanBedIconL1);
    } else {
      setIcon(cleanBedIconL1);
    }
    setLocation(cleanBedIconL1, 65, 310);

    if (cleanReclinerL1 == 0) {
      hideIcon(cleanReclinerIconL1);
    } else {
      setIcon(cleanReclinerIconL1);
    }
    setLocation(cleanReclinerIconL1, 107, 310);

    if (cleanPumpL1 == 0) {
      hideIcon(cleanInfusionIconL1);
    } else {
      setIcon(cleanInfusionIconL1);
    }
    setLocation(cleanInfusionIconL1, 145, 310);

    if (xrayL1 == 0) {
      hideIcon(xRayIconL1);
    } else {
      setIcon(xRayIconL1);
    }
    setLocation(xRayIconL1, 195, 310);

    if (dirtyBedL1 == 0) {
      hideIcon(dirtyBedIconL1);
    } else {
      setIcon(dirtyBedIconL1);
    }
    setLocation(dirtyBedIconL1, 240, 310);

    if (dirtyReclinerL1 == 0) {
      hideIcon(dirtyReclinerIconL1);
    } else {
      setIcon(dirtyReclinerIconL1);
    }
    setLocation(dirtyReclinerIconL1, 278, 310);

    if (dirtyPumpL1 == 0) {
      hideIcon(dirtyInfusionIconL1);
    } else {
      setIcon(dirtyInfusionIconL1);
    }
    setLocation(dirtyInfusionIconL1, 315, 310);

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
    gesturePane.setFitMode(GesturePane.FitMode.COVER);
    gesturePane.setScrollMode(GesturePane.ScrollMode.PAN);
    gesturePane.setScrollBarPolicy(GesturePane.ScrollBarPolicy.NEVER);
    gesturePane.setMinScale(1);

    // event click to pull up floor equipment summary
    floor3Icon.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            setFloor3();
          }
        });
    floor1Icon.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            setFloor1();
          }
        });

    floorL1Icon.setOnMouseClicked(
        e -> {
          if (e.getClickCount() == 1) {
            setFloorL1();
          }
        });
  }

  // sets the view to the third floor
  private void setFloor3() {
    medEquipmentTable.getItems().setAll(filteredEquipments3);
    cleanBedLabel.setText(cleanBed3 + "");
    dirtyBedLabel.setText(dirtyBed3 + "");
    cleanReclinerLabel.setText(cleanRecliner3 + "");
    dirtyReclinerLabel.setText(dirtyRecliner3 + "");
    cleanPumpsLabel.setText(cleanPump3 + "");
    dirtyPumpsLabel.setText(dirtyPump3 + "");
    xrayLabel.setText(xray3 + "");
  }

  // sets the view to the first floor
  private void setFloor1() {
    medEquipmentTable.getItems().setAll(filteredEquipments1);
    cleanBedLabel.setText(cleanBed1 + "");
    dirtyBedLabel.setText(dirtyBed1 + "");
    cleanReclinerLabel.setText(cleanRecliner1 + "");
    dirtyReclinerLabel.setText(dirtyRecliner1 + "");
    cleanPumpsLabel.setText(cleanPump1 + "");
    dirtyPumpsLabel.setText(dirtyPump1 + "");
    xrayLabel.setText(xray1 + "");
  }

  // sets the view to the lower first floor
  private void setFloorL1() {
    medEquipmentTable.getItems().setAll(filteredEquipmentsL1);
    cleanBedLabel.setText(cleanBedL1 + "");
    dirtyBedLabel.setText(dirtyBedL1 + "");
    cleanReclinerLabel.setText(cleanReclinerL1 + "");
    dirtyReclinerLabel.setText(dirtyReclinerL1 + "");
    cleanPumpsLabel.setText(cleanPumpL1 + "");
    dirtyPumpsLabel.setText(dirtyPumpL1 + "");
    xrayLabel.setText(xrayL1 + "");
  }

  // sets the icon sizes to 1.5 times their current
  private void setIcon(ImageView image) {
    image.setScaleX(.65);
    image.setScaleY(.65);
  }

  // hides icons
  private void hideIcon(ImageView image) {
    image.setScaleX(0);
    image.setScaleY(0);
  }

  // sets the floor number icons tp 0.35 their size
  private void setFloorIcon(ImageView image) {
    image.setScaleX(.30);
    image.setScaleY(.30);
  }

  // sets the location of the icons
  private void setLocation(ImageView image, int x, int y) {
    image.setLayoutX(x);
    image.setLayoutY(y);
  }
}
