package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ClientServerLocationTblI implements iLocationTable {

  private static ClientServerLocationTblI instance = null;
  String tbName = null;
  private static Connection connection = null;

  private ClientServerLocationTblI() throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    createTable();
    tbName = "Locations";
  }

  public static ClientServerLocationTblI getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new ClientServerLocationTblI();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
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

  // add new object ONLY to DB
  // DO NOT add this to objList
  // this function is used for putting objList into table
  // SO DO NOT DO THAT
  public boolean addEntry(Location loc) {
    try {

      PreparedStatement s =
          connection.prepareStatement(
              " IF NOT EXISTS (SELECT 1 FROM "
                  + tbName
                  + " WHERE "
                  + colNames.get(0)
                  + " = ?)"
                  + "BEGIN"
                  + " INSERT INTO "
                  + tbName
                  + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                  + "end");
      s.setString(1, loc.getNodeID());
      s.setString(2, loc.getNodeID());
      s.setInt(3, loc.getXcoord());
      s.setInt(4, loc.getYcoord());
      s.setString(5, loc.getFloor());
      s.setString(6, loc.getBuilding());
      s.setString(7, loc.getNodeType());
      s.setString(8, loc.getLongName());
      s.setString(9, loc.getShortName());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
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

  // initiialize table
  public boolean createTable() {
    try {
      PreparedStatement s1 =
          connection.prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return false;
      }
      Statement s = connection.createStatement();
      s.execute(
          "CREATE TABLE Locations("
              + "nodeID VARCHAR(50) NOT NULL ,"
              + "xcoord INTEGER NOT NULL, "
              + "ycoord INTEGER NOT NULL, "
              + "floor TEXT NOT NULL, "
              + "building TEXT NOT NULL,"
              + "nodeType TEXT NOT NULL,"
              + "longName TEXT NOT NULL,"
              + "shortName TEXT NOT NULL, "
              + "PRIMARY KEY (nodeID));");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
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
