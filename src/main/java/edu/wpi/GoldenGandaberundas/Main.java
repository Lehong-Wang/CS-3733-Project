package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.SimulateBedsRecs;
import edu.wpi.GoldenGandaberundas.controllers.simulation.SimulatePumps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.SimulateXRay;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Path;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipment.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentSimulation.SimMedEquipmentTbl;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    // Main.run(200, 200, 1280, 800, "CSS/api.css", 1);
  }

  /**
   * Starts the simulation API
   *
   * @param xCoord
   * @param yCoord
   * @param windowWidth width of the spawned JFX window
   * @param windowHeight Height of the spawned JFX window
   * @param cssPath path starting from the resources folder
   */
  public void run(
      int xCoord, int yCoord, int windowWidth, int windowHeight, String cssPath, double frequency) {
    floorMaps.load();

    Scanner readLocs = new Scanner(App.class.getResourceAsStream("locationTbl.csv"));
    if (readLocs.hasNext()) {
      readLocs.nextLine();
    }
    while (readLocs.hasNext()) { // cycles in the while loop until it reaches the end
      String currentLine = readLocs.nextLine();
      if (!currentLine.isEmpty()) { // check if a line is blank
        String[] element = currentLine.split(","); // separates each element based on a comma
        Location loc =
            new Location(
                element[0],
                Integer.parseInt(element[1]),
                Integer.parseInt(element[2]),
                element[3],
                element[4],
                element[5],
                element[6],
                element[7]);
        LocationTbl.getInstance().addEntry(loc);
      }
    }
    readLocs.close();

    Scanner readPaths = new Scanner(App.class.getResourceAsStream("pathTbl.csv"));
    if (readPaths.hasNext()) {
      readPaths.nextLine();
    }
    while (readPaths.hasNext()) { // cycles in the while loop until it reaches the end
      String currentLine = readPaths.nextLine();
      if (!currentLine.isEmpty()) { // check if a line is blank
        String[] element = currentLine.split(","); // separates each element based on a comma
        Path path = new Path(element[0], element[1], element[2]);
        System.out.println(path.toString());
        PathTbl.getInstance().addEntry(path);
      }
    }
    readPaths.close();

    // MedEquipment med = new MedEquipment(Integer.parseInt(ele[0]), ele[1], ele[2], ele[3]);

    Scanner redMeds = new Scanner(App.class.getResourceAsStream("medEquipmentTbl.csv"));
    if (redMeds.hasNext()) {
      redMeds.nextLine();
    }
    while (redMeds.hasNext()) { // cycles in the while loop until it reaches the end
      String currentLine = redMeds.nextLine();
      if (!currentLine.isEmpty()) { // check if a line is blank
        String[] ele = currentLine.split(","); // separates each element based on a comma
        MedEquipment path = new MedEquipment(Integer.parseInt(ele[0]), ele[1], ele[2], ele[3]);
        System.out.println(path.toString());
        SimMedEquipmentTbl.getInstance().addEntry(path);
      }
    }
    readPaths.close();

    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    PathTbl.getInstance().createBranchedLocations(points);
    PathTbl.getInstance(); // .loadBackup("backupsCSVs/pathTbl.csv");
    PathTbl.createStatsMap();

    SimulateBedsRecs.setFrequency(frequency);
    SimulatePumps.setFrequency(frequency);
    SimulateXRay.setFrequency(frequency);

    //    Parent root = FXMLLoader.load(getClass().getResource("views/simulationView.fxml"));
    //    Scene scene = new Scene(root, windowWidth, windowHeight);
    //    // Below line is to set styleSheet, does not maintain styleSheet when switching scenes
    // unless
    //    // stylesheet is added in fxml file
    //    Stage stage2 = new Stage();
    //    scene.getStylesheets().add(getClass().getResource(cssPath).toExternalForm());
    //    stage2.setScene(scene);
    //    // primaryStage.setMaximized(false);
    //    stage2.setX(xCoord);
    //    stage2.setY(yCoord);
    //    stage2.setWidth(windowWidth);
    //    stage2.setHeight(windowHeight);
    //    stage2.show();

    App.launch(
        App.class,
        String.valueOf(xCoord),
        String.valueOf(yCoord),
        String.valueOf(windowWidth),
        String.valueOf(windowHeight),
        cssPath);
  }

  public void run() {
    run(0, 0, 1000, 1000, "", 1);
  }
}
