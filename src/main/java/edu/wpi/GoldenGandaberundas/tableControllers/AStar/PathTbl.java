package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathTbl implements TableController<Path, String> {

  private static PathTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Path> objList;
  /** relative path to the database file */
  private static HashMap<String, Integer> statsMap = new HashMap<>();

  TableController<Path, String> embeddedTable = null;

  TableController<Path, String> clientServerTable = null;
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  private PathTbl() throws SQLException {
    tbName = "Paths";
    pkCols = "edgeID";
    colNames = Arrays.asList(new String[] {"edgeID", "startNode", "endNode"});
    objList = new ArrayList<Path>();
    embeddedTable = new PathEmbedded(tbName, colNames.toArray(new String[3]), pkCols, objList);
    clientServerTable =
        new PathClientServer(tbName, colNames.toArray(new String[3]), pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    createTable();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static PathTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new PathTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  private TableController<Path, String> getCurrentTable() {
    System.out.println("Connection Type: " + connectionHandler.getCurrentConnectionType());
    switch (connectionHandler.getCurrentConnectionType()) {
      case embedded:
        return embeddedTable;
      case clientServer:
        return clientServerTable;
      case cloud:
        return null;
    }
    System.out.println(connectionHandler.getCurrentConnectionType());
    return null;
  }

  @Override
  public ArrayList<Path> readTable() {
    return this.getCurrentTable().readTable();
  }

  @Override
  public boolean addEntry(Path obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  @Override
  public ArrayList<Path> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  @Override
  public Path getEntry(String pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Path> objList) {
    return this.getCurrentTable().loadFromArrayList(objList);
  }

  /**
   * searched the path table and returns and creates a list of path points
   *
   * @param startNode finds the starting node
   * @return update list of the string nodes
   */
  public ArrayList<String> getConnectedPoints(String startNode) {
    // return this.getCurrentTable().getConnectedPoints(startNode);
    ArrayList<String> connect = new ArrayList<>();
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(1) + " =?;");
      s.setString(1, startNode);
      ResultSet r = s.executeQuery();
      while (r.next()) {
        connect.add(r.getString(3));
      }

      return connect;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connect;
  }

  /**
   * takes in the path points and creates the proper branches for the points
   *
   * @param points the lsit of every point in the path list
   * @return updates points list
   */
  public ArrayList<Point> createBranchedLocations(ArrayList<Point> points) {
    // return this.getCurrentTable().createBranchedLocations(points);
    for (Point p : points) {
      ArrayList<String> branched = getConnectedPoints(p.loc);
      for (String b : branched) {
        for (Point o : points) {
          if (b.equals(o.loc)) {
            p.addBranch(o);
            break;
          }
        }
      }
    }
    return points;
  }

  /**
   * Takes in the name for the start and ending nodes and creates the appropriate a star path
   *
   * @param start string of the starting node
   * @param end string of the ending node
   * @return list of Strings created from the a star path
   */
  public List<String> createAStarPath(String start, String end) {
    // return this.getCurrentTable().createAStarPath(start, end);
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    if (points.size() != 0) {
      points = PathTbl.getInstance().createBranchedLocations(points);
      int startID = 0;
      int endID = 0;
      for (Point o : points) {
        if (o.loc.equals(start)) {
          startID = points.indexOf(o);
        }
        if (o.loc.equals(end)) {
          endID = points.indexOf(o);
        }
      }
      points.get(startID).g = 0;
      Point test = points.get(startID).aStar(points.get(endID));
      List<String> path = points.get(startID).locationsPath(test);
      return path;
    }
    return null;
  }

  /**
   * Takes in the name for the start and ending nodes and creates the appropriate a star path Keeps
   * Statistics using HashMap statsMap on Nodes visited
   *
   * @param start string of the starting node
   * @param end string of the ending node
   * @return list of Strings created from the a star path
   */
  public List<String> createAStarPathwStats(String start, String end) {
    List<String> path = createAStarPath(start, end);
    if (path != null) {
      for (String node : path) {
        statsMap.put(node, statsMap.get(node) + 1);
      }
      return path;
    } else {
      return null;
    }
  }

  /**
   * updates the statistics hash map based on the astar path
   *
   * @param path the astar path to for the hash map
   */
  public static void pathStats(List<String> path) {
    if (path != null) {
      for (String node : path) {
        statsMap.put(node, statsMap.get(node) + 1);
      }
    }
  }

  /** @param path */
  public static void pathRemoveStats(List<String> path) {
    if (path != null) {
      for (String node : path) {
        if (statsMap.get(node) == 0) {
          statsMap.put(node, 0);
        } else {
          statsMap.put(node, statsMap.get(node) - 1);
        }
      }
    }
  }

  /** clears the statistics hashmap */
  public static void zeroPathStats() {
    for (String key : statsMap.keySet()) {
      statsMap.put(key, 0);
    }
  }

  /** Creates a HashMap to use for keeping node traversal statistics */
  public static void createStatsMap() {
    ArrayList<Location> allLocs = LocationTbl.getInstance().readTable();
    for (int i = 0; i < allLocs.size(); i++) {
      String loc = allLocs.get(i).getNodeID();
      statsMap.put(loc, 0);
    }
  }

  public static void printStatsMap() {
    for (String key : statsMap.keySet()) {
      System.out.println(key + " = " + statsMap.get(key));
    }
  }

  @Override
  public void writeTable() {
    this.getCurrentTable().writeTable();
  }

  @Override
  public ArrayList<Path> getObjList() {
    return objList;
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

  /*
   * Returns the starting and ending points of a MedEquip in the SimulationList
   *
   * @param medID - Piece of Equipment
   * @param hour - Beginning hour of path
   * @return List<String> where index 0 = starting point, index 1 = ending point
   */

  public static List<String> getPathPoints(int medID, int hour) {
    List<String> retVal = new ArrayList<>();
    retVal.add(0, Simulation.pathList[medID][hour]);
    retVal.add(1, Simulation.pathList[medID][hour + 1]);
    //    System.out.println("Eqp #" + medID + ": Starts: " + retVal.get(0) + " Ends: " +
    // retVal.get(1));
    MedEquipmentTbl.getInstance().editEntry(medID, "currLoc", retVal.get(1));
    return retVal;
  }

  public boolean deleteEntry(String pkid) {
    return this.getCurrentTable().deleteEntry(pkid);
  }

  public void createBackup(File f) {
    this.getCurrentTable().createBackup(f);
  }

  // drop current table and enter data from CSV
  public ArrayList<Path> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(String pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  /**
   * Returns the starting and ending points of a MedEquip in the SimulationList for a longer
   * duration
   *
   * @param medID - Piece of Equipment
   * @param hour - Beginning time of path
   * @param fasterHour - End of lonnher duration time
   * @return List<String> where index 0 = starting point, index 1 = ending point
   */
  public static List<String> getPathPointsFaster(int medID, int hour, int fasterHour) {
    List<String> retVal = new ArrayList<>();
    retVal.add(0, Simulation.pathList[medID][hour]);
    retVal.add(1, Simulation.pathList[medID][fasterHour]);
    MedEquipmentTbl.getInstance().editEntry(medID, "currLoc", retVal.get(1));
    return retVal;
  }

  public String getTableName() {
    return tbName;
  }

  public static HashMap<String, Integer> getStatsMap() {
    return statsMap;
  }
}
