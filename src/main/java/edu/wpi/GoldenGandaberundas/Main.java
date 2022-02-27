package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipment.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipment.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentSimulation.SimMedEquipmentTbl;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {

  public static void main(String[] args) throws SQLException {

    System.out.println(PathTbl.getInstance().objList);

    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    PathTbl.getInstance().createBranchedLocations(points);
    PathTbl.getInstance(); // .loadBackup("backupsCSVs/pathTbl.csv");
    PathTbl.createStatsMap();

    MedEquipmentTbl.getInstance().loadBackup("BackupsCSVs/medEquipmentTbl.csv");
    ArrayList<MedEquipment> medEquipments = new ArrayList<>();
    for (MedEquipment medEquipment : MedEquipmentTbl.getInstance().readTable()) {
      medEquipments.add(medEquipment);
    }

    // SimMedEquipmentTbl.getInstance();//.loadFromArrayList(MedEquipmentTbl.getInstance().readTable());

    floorMaps.load();

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
    Simulation.setTeamLetter(teamLetter);
    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    SimMedEquipmentTbl.getInstance().loadFromArrayList(medEquipmentList);
    App.launch(
        App.class,
        new String[] {
          String.valueOf(xCoord),
          String.valueOf(yCoord),
          String.valueOf(windowWidth),
          String.valueOf(windowLength),
          cssPath
        });
  }
}
