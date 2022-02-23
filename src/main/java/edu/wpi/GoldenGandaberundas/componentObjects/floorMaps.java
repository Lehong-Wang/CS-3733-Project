package edu.wpi.GoldenGandaberundas.componentObjects;

import edu.wpi.GoldenGandaberundas.App;
import javafx.scene.image.Image;

public class floorMaps {
  public static Image thirdFloor;
  public static Image secondFloor;
  public static Image firstFloor;
  public static Image lower1Floor;
  public static Image lower2Floor;
  public static Image reclinerIcon;
  public static Image xRayIcon;
  public static Image infusionPumpIcon;
  public static Image hospital;
  public static Image computer;
  public static Image medicalEquipmentGreen;
  public static Image medicineGreen;
  public static Image laundry;
  public static Image gift;
  public static Image towerSideview;
  public static Image towerSideviewCropped;
  public static Image bedIcon;
  public static Image redBed;
  public static Image greenBed;
  public static Image redRecliner;
  public static Image greenRecliner;
  public static Image redInfusionPump;
  public static Image greenInfusionPump;
  public static Image firstFloorIcon;
  public static Image thirdFloorIcon;
  public static Image lowerFirstFloorIcon;
  public static Image equlaizer;

  public static void load() {
    System.out.println("IMAGES LOADED");
    thirdFloor = new Image(App.class.getResourceAsStream("images/thirdFloorCropped.png"));
    secondFloor = new Image(App.class.getResourceAsStream("images/secondFloorCropped.png"));
    firstFloor = new Image(App.class.getResourceAsStream("images/firstFloorCropped.png"));
    // groundFloor = new Image(App.class.getResourceAsStream("images/groundFloorCropped.png"));
    lower1Floor = new Image(App.class.getResourceAsStream("images/Lower1Cropped.png"));
    lower2Floor = new Image(App.class.getResourceAsStream("images/Lower2Cropped.png"));
    bedIcon = new Image(App.class.getResourceAsStream("images/bed.png"));
    reclinerIcon = new Image(App.class.getResourceAsStream("images/recliner.png"));
    xRayIcon = new Image(App.class.getResourceAsStream("images/xray.png"));
    infusionPumpIcon = new Image(App.class.getResourceAsStream("images/infusion pumps.png"));
    hospital = new Image(App.class.getResourceAsStream("images/hospital.jpg"));
    computer = new Image(App.class.getResourceAsStream("images/computer.png"));
    laundry = new Image(App.class.getResourceAsStream("images/laundry.png"));
    medicineGreen = new Image(App.class.getResourceAsStream("images/medicineGREEN.png"));
    medicalEquipmentGreen =
        new Image(App.class.getResourceAsStream("images/medicalEquipmentGREEN.png"));
    gift = new Image(App.class.getResourceAsStream("images/giftFloral.png"));

    towerSideview = new Image(App.class.getResourceAsStream("images/TowerSideView.png"));
    towerSideviewCropped =
        new Image(App.class.getResourceAsStream("images/TowerSideViewCropped.png"));
    redBed = new Image(App.class.getResourceAsStream("images/bedRED.png"));
    greenBed = new Image(App.class.getResourceAsStream("images/bedGREEN.png"));
    redRecliner = new Image(App.class.getResourceAsStream("images/reclinerRED.png"));
    greenRecliner = new Image(App.class.getResourceAsStream("images/reclinerGREEN.png"));
    redInfusionPump = new Image(App.class.getResourceAsStream("images/infusionPumpsRED.png"));
    greenInfusionPump = new Image(App.class.getResourceAsStream("images/infusionPumpsGREEN.png"));
    firstFloorIcon = new Image(App.class.getResourceAsStream("images/floor1Icon.png"));
    thirdFloorIcon = new Image(App.class.getResourceAsStream("images/floor3Icon.png"));
    lowerFirstFloorIcon = new Image(App.class.getResourceAsStream("images/lower1Icon.png"));
    equlaizer = new Image(App.class.getResourceAsStream("images/Simulation-equalizer-line .png"));
  }
}
