package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.ConnectionType;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class LocationTbl extends TableController<Location, String> {

  private static LocationTbl instance = null;
  private static LocationTable currentTable = null;

  private LocationTbl() throws SQLException {
    super(
        "Locations",
        Arrays.asList(
            new String[] {
              "nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"
            }),
        "nodeID");

    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    createTable();
    tbName = "Locations";
    String[] cols = {
      "nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"
    };
    colNames = Arrays.asList(cols);
    objList = readTable();
  }

  public static LocationTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new LocationTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    if (TableController.getConnectionType() == ConnectionType.embedded) {
      currentTable = iEmbeddedLocationTbl.getInstance();
    } else if (TableController.getConnectionType() == ConnectionType.clientServer) {
      currentTable = iClientServerLocationTbl.getInstance();
    } else if (TableController.getConnectionType() == ConnectionType.cloud) {

    } else {
      System.err.println("Connection type error Location Table");
    }
    return (LocationTbl) instance;
  }

  // method to read the table and output an ArrayList of Locations
  public ArrayList readTable() {
    return currentTable.readTable();
  }

  // add new object ONLY to DB
  // DO NOT add this to objList
  // this function is used for putting objList into table
  // SO DO NOT DO THAT
  public boolean addEntry(Location loc) {
    return currentTable.addEntry(loc);
  }

  // create list of objects from CSV file
  public ArrayList<Location> readBackup(String fileName) {
    return currentTable.readBackup(fileName);
  }

  // initiialize table
  public boolean createTable() {
    return currentTable.createTable();
  }

  // get the Object with given pkID
  public Location getEntry(String pkID) {
    return currentTable.getEntry(pkID);
  }

  /**
   * Creates a list of points with for each location with their x and y coordinates
   *
   * @return the list of points
   */
  public ArrayList<Point> getNodes() {
    return currentTable.getNodes();
  }
}
