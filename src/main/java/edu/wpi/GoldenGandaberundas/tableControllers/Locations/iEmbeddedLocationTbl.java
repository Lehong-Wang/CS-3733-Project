package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class EmbeddedLocationTblI implements iLocationTable {
  private static EmbeddedLocationTblI instance = null;
  private String tbName = "Locations";
  private Connection connection;
  private iEmbeddedLocationTbl() throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
//    objList = new ArrayList<Location>();
//    createTable();
//    tbName = "Locations";
//    String[] cols = {
//      "nodeID", "xcoord", "ycoord", "floor", "building", "nodeType", "longName", "shortName"
//    };
//    colNames = Arrays.asList(cols);
//    objList = readTable();
  }

  public static EmbeddedLocationTblI getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new iEmbeddedLocationTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return (iEmbeddedLocationTbl) instance;
  }

  // method to read the table and output an ArrayList of Locations
  public ArrayList readTable() {
    ArrayList tableInfo = new ArrayList<Location>();
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Location(
                r.getString(1),
                r.getInt(2),
                r.getInt(3),
                r.getString(4),
                r.getString(5),
                r.getString(6),
                r.getString(7),
                r.getString(8)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean editEntry(String pkid, String colName, Object value) {
    return false;
  }

  @Override
  public boolean deleteEntry(String pkid) {
    return false;
  }

  // add new object ONLY to DB
  // DO NOT add this to objList
  // this function is used for putting objList into table
  // SO DO NOT DO THAT
  public boolean addEntry(Location loc) {
    if (entryExists(loc.getNodeID())) {
      System.out.println("loc ID exists: " + loc.getNodeID());
      return false;
    }
    PreparedStatement s = null;
    try {
      // check if exists and add to DB
      s =
          connection.prepareStatement(
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

      s.setString(1, loc.getNodeID());
      s.setInt(2, loc.getXcoord());
      s.setInt(3, loc.getYcoord());
      s.setString(4, loc.getFloor());
      s.setString(5, loc.getBuilding());
      s.setString(6, loc.getNodeType());
      s.setString(7, loc.getLongName());
      s.setString(8, loc.getShortName());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    return true;
  }

  @Override
  public void createBackup(File f) {

  }

  // create list of objects from CSV file
  public ArrayList<Location> readBackup(String fileName) {
    ArrayList<Location> locList = new ArrayList<>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      //      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName"))) {
        System.err.println("location backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
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
          locList.add(loc); // adds the location to the list
          //          System.out.println(loc.toString());
        }
        currentLine = buffer.readLine();
      }
      // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return locList;
  }

  @Override
  public ArrayList<Location> loadBackup(String fileName) {
    return null;
  }

  // initiialize table
  public boolean createTable() {
    try {
      // check if there is table with same name
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      // if there is already table, exit
      if (r.getInt(1) != 0) {
        System.out.println("Locations Table Already Exists");
      }
      // check for SQL driver
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return false;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    System.out.println("SQLite driver registered!");

    // create new SQL table
    Statement s = null;
    try {
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Locations("
              + "nodeID TEXT NOT NULL ,"
              + "xcoord INTEGER NOT NULL, "
              + "ycoord INTEGER NOT NULL, "
              + "floor TEXT NOT NULL, "
              + "building TEXT NOT NULL,"
              + "nodeType TEXT NOT NULL,"
              + "longName TEXT NOT NULL,"
              + "shortName TEXT NOT NULL, "
              + "PRIMARY KEY ('nodeID'));");
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean entryExists(String pkID) {
    return false;
  }

  // get the Object with given pkID
  public Location getEntry(String pkID) {
    Location loc = new Location("", 0, 0, "", "", "", "", "");
    try {
      if (!this.entryExists(pkID)) {
        return null;
      }
      System.out.println(pkID);
      PreparedStatement s =
          connection.prepareStatement("SELECT * FROM " + tbName + " WHERE nodeID = ?");
      // s.setString(1, tbName);
      s.setString(1, pkID);
      ResultSet r = s.executeQuery();
      r.next();
      loc =
          new Location(
              r.getString(1),
              r.getInt(2),
              r.getInt(3),
              r.getString(4),
              r.getString(5),
              r.getString(6),
              r.getString(7),
              r.getString(8));

    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return loc;
  }

  /**
   * Creates a list of points with for each location with their x and y coordinates
   *
   * @return the list of points
   */
  public ArrayList<Point> getNodes() {
    ArrayList<Point> points = new ArrayList<>();

    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        points.add(new Point(r.getString(1), r.getInt(2), r.getInt(3)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return points;
  }
}
