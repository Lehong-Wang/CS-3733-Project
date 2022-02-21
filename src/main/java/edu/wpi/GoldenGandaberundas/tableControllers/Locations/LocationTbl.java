package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocationTbl extends TableController<Location, String> {

  private static LocationTbl instance = null;

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
    return (LocationTbl) instance;
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
    if (!this.getEmbedded()) {
      return addEntryOnline(loc);
    }
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

  private boolean addEntryOnline(Location loc) {
    try {

      PreparedStatement s =
          connection.prepareStatement(
              " IF NOT EXISTS (SELECT 1 FROM "
                  + tbName
                  + " WHERE "
                  + colNames.get(0)
                  + " = ?)"
                  + "BEGIN"
                  + "    INSERT INTO "
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
  public void createTable() {
    if (!this.getEmbedded()) {
      createTableOnline();
      return;
    }
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
        return;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    try {
      // check for SQL driver
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return;
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

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createTableOnline() {
    try {
      PreparedStatement s1 =
          connection.prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
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
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // get the Object with given pkID
  public Location getEntry(String pkID) {
    Location loc = new Location("", 0, 0, "", "", "", "", "");
    try {
      if (!this.entryExists(pkID)) {
        return null;
      }
      // System.out.println(pkID);
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

  public void writeTable() {

    for (Location obj : objList) {

      this.addEntry(obj);
    }
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
    //    if (pkid instanceof ArrayList) {
    //      return editEntryComposite((ArrayList<Integer>) pkid, colName, value);
    //    }
    try {

      PreparedStatement s =
          connection.prepareStatement(
              "UPDATE "
                  + tbName
                  + " SET "
                  + colName
                  + " = ? WHERE ("
                  + colNames.get(0)
                  + ") =(?);");
      s.setObject(1, value);
      s.setObject(2, pkid);
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * removes a row from the database
   *
   * @param pkid primary key of row to be removed
   * @return true if successful, false otherwise
   */
  public boolean deleteEntry(String pkid) {
    //    if (pkid instanceof ArrayList) {
    //      return deleteEntryComposite((ArrayList<Integer>) pkid);
    //    }
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
      s.setObject(1, pkid);
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    if (objList.isEmpty()) {
      return;
    }
    /* Instantiate the writer */
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    /* Get the class type of the objects in the array */
    final Class<?> type = objList.get(0).getClass();

    /* Get the name of all the attributes */
    final ArrayList<Field> classAttributes = new ArrayList<>(List.of(type.getDeclaredFields()));

    boolean doesExtend = Request.class.isAssignableFrom(type);
    if (doesExtend) {
      final Class<?> superType = objList.get(0).getClass().getSuperclass();
      classAttributes.addAll(0, (List.of(superType.getDeclaredFields())));
    }

    /* Write the parsed attributes to the file */
    writer.println(classAttributes.stream().map(Field::getName).collect(Collectors.joining(",")));

    /* For each object, read each attribute and append it to the file with a comma separating */
    PrintWriter finalWriter = writer;
    objList.forEach(
        obj -> {
          finalWriter.println(
              classAttributes.stream()
                  .map(
                      attribute -> {
                        attribute.setAccessible(true);
                        String output = "";
                        try {
                          output = attribute.get(obj).toString();
                        } catch (IllegalAccessException | ClassCastException e) {
                          System.err.println("[CSVUtil] Object attribute access error.");
                        }
                        return output;
                      })
                  .collect(Collectors.joining(",")));
          finalWriter.flush();
        });
    writer.close();
  }

  // drop current table and enter data from CSV
  public ArrayList<Location> loadBackup(String fileName) {
    createTable();
    ArrayList<Location> listObjs = readBackup(fileName);

    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
      this.objList = listObjs;
      this.writeTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listObjs;
  }

  // checks if an entry exists
  public boolean entryExists(String pkID) {
    //    if (pkID instanceof ArrayList) {
    //      return entryExistsComposite((ArrayList<Integer>) pkID);
    //    }
    boolean exists = false;
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");

      s.setObject(1, pkID);

      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        exists = true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return exists;
  }
}
