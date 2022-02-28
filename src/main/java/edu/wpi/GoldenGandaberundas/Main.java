package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Path;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipment.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentSimulation.SimMedEquipmentTbl;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) throws SQLException {
    MedEquipment med1 = new MedEquipment(1, "Bed", "In_Use", "FDEPT00101");
    MedEquipment xRay = new MedEquipment(2, "X-Ray", "In_Use", "FDEPT00101");
    ArrayList<MedEquipment> medEquipments = new ArrayList<>();
    medEquipments.add(med1);
    medEquipments.add(xRay);
    Main.run(200, 200, 1280, 800, "CSS/api.css", medEquipments, 'G');
  }

  /**
   * Starts the simulation API
   *
   * @param xCoord
   * @param yCoord
   * @param windowWidth width of the spawned JFX window
   * @param windowLength length of the spawned JFX window
   * @param cssPath path starting from the resources folder
   * @param medEquipmentList list of medical equipment objects
   */
  public static void run(
      int xCoord,
      int yCoord,
      int windowWidth,
      int windowLength,
      String cssPath,
      ArrayList<MedEquipment> medEquipmentList,
      Character teamLetter) {
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

    Simulation.setTeamLetter(teamLetter);
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    PathTbl.getInstance().createBranchedLocations(points);
    PathTbl.getInstance(); // .loadBackup("backupsCSVs/pathTbl.csv");
    PathTbl.createStatsMap();
    SimMedEquipmentTbl.getInstance().loadFromArrayList(medEquipmentList);
    App.launch(
        App.class,
        String.valueOf(xCoord),
        String.valueOf(yCoord),
        String.valueOf(windowWidth),
        String.valueOf(windowLength),
        cssPath);
  }
}
