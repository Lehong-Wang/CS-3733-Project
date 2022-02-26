package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) throws SQLException {

    System.out.println(PathTbl.getInstance().objList);

    LocationTbl.getInstance();
    PathTbl.getInstance();
    LocationTbl.getInstance();
    PathTbl.getInstance();
    PathTbl.getInstance(); // .loadBackup("backups/AllLocationEdges.csv");

    PathTbl.getInstance(); // .loadBackup("BackupsCSVs/pathTbl.csv");

    MedEquipmentTbl.getInstance().loadBackup("TestCSVs/medEquipSimulation.csv");
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    PathTbl.getInstance().createBranchedLocations(points);
    PathTbl.getInstance(); // .loadBackup("backupsCSVs/pathTbl.csv");
    PathTbl.createStatsMap();


    floorMaps.load();

    Main.run(
        0,
        0,
        1280,
        800,
        "CSS/api.css",
        MedEquipmentTbl.getInstance(),
        PathTbl.getInstance(),
        LocationTbl.getInstance());
  }

  public static void run(
      int xCoord,
      int yCoord,
      int windowWidth,
      int windowLength,
      String cssPath,
      MedEquipmentTbl medEquipmentTbl,
      PathTbl pathTbl,
      LocationTbl locationTbl) {
    App.launch(
        App.class,
        new String[] {
          String.valueOf(xCoord), String.valueOf(yCoord), String.valueOf(windowWidth), cssPath
        });
  }
}
