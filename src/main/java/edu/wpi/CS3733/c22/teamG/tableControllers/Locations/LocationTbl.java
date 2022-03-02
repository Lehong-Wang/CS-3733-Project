package edu.wpi.CS3733.c22.teamG.tableControllers.Locations;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.AStar.Point;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionType;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocationTbl implements TableController<Location, String> {

  private static LocationTbl instance = null;
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<Location> objList;
  /** relative path to the database file */
  TableController<Location, String> embeddedTable = null;

  TableController<Location, String> clientServerTable = null;

  TableController<Location, String> cloudServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
  Connection connection = connectionHandler.getConnection();

  private LocationTbl() throws SQLException {

    // create a new table with column names if none table of same name exist
    // if there is one, do nothing

    tbName = "Locations";
    String[] cols = {
      "nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"
    };
    pkCols = "nodeID";
    colNames = Arrays.asList(cols);
    objList = new ArrayList<Location>();
    embeddedTable = new LocationEmbedded(tbName, cols, pkCols, objList);
    clientServerTable = new LocationClientServer(tbName, cols, pkCols, objList);
    cloudServerTable = new LocationCloud(objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    connectionHandler.addTable(cloudServerTable, ConnectionType.cloud);
    createTable();
    objList = readTable();
    System.out.println("After Create: " + objList);
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
    return (LocationTbl) instance;
  }

  private TableController<Location, String> getCurrentTable() {
    switch (connectionHandler.getCurrentConnectionType()) {
      case embedded:
        return embeddedTable;
      case clientServer:
        return clientServerTable;
      case cloud:
        return cloudServerTable;
    }
    return null;
  }
  // method to read the table and output an ArrayList of Locations
  public ArrayList readTable() {
    return this.getCurrentTable().readTable();
  }

  // add new object ONLY to DB
  // DO NOT add this to objList
  // this function is used for putting objList into table
  // SO DO NOT DO THAT
  public boolean addEntry(Location loc) {
    return this.getCurrentTable().addEntry(loc);
  }

  // create list of objects from CSV file
  public ArrayList<Location> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  // initiialize table
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  // get the Object with given pkID
  public Location getEntry(String pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Location> objList) {
    return this.getCurrentTable().loadFromArrayList(objList);
  }

  /**
   * Creates a list of points with for each location with their x and y coordinates
   *
   * @return the list of points
   */
  public ArrayList<Point> getNodes() {
    ArrayList<Point> points = new ArrayList<>();
    var locations = this.getCurrentTable().readTable();
    for (Location loc : locations) {
      points.add(new Point(loc.getNodeID(), loc.getXcoord(), loc.getYcoord()));
    }
    return points;
  }

  public void writeTable() {
    this.getCurrentTable().writeTable();
  }

  /**
   * Modifies the attribute so that it is equal to value MAKE SURE YOU KNOW WHAT DATA TYPE YOU ARE
   * MODIFYING
   *
   * @param pkid the primary key that represents the row you are modifying
   * @param colName column to be modified
   * @param value new value for column
   * @return true if successful, false otherwise
   */
  // public boolean editEntry(T1 pkid, String colName, Object value)
  public boolean editEntry(String pkid, String colName, Object value) {
    return this.getCurrentTable().editEntry(pkid, colName, value);
  }

  /**
   * removes a row from the database
   *
   * @param pkid primary key of row to be removed
   * @return true if successful, false otherwise
   */
  public boolean deleteEntry(String pkid) {
    return this.getCurrentTable().deleteEntry(pkid);
  }

  /**
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    this.getCurrentTable().createBackup(f);
  }

  // drop current table and enter data from CSV
  public ArrayList<Location> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(String pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<Location> getObjList() {
    return this.getCurrentTable().getObjList();
  }
}
