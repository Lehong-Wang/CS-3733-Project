package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocationClientServer implements TableController<Location, String> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<Location> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public LocationClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<Location> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing

    // createTable();
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<Location> readTable() {
    ArrayList tableInfo = new ArrayList<Location>();
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SElECT * FROM " + tbName + ";");
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
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public void writeTable() {
    for (Location obj : objList) {

      this.addEntry(obj);
    }
  }

  @Override
  public boolean deleteEntry(String pkid) {
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
      s.setObject(1, pkid);
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  @Override
  public boolean addEntry(Location loc) {
    try {

      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
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

  @Override
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

  @Override
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
    createTable();
    ArrayList<Location> listObjs = readBackup(fileName);

    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
      this.objList = listObjs;
      this.writeTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listObjs;
  }

  @Override
  public boolean editEntry(String pkid, String colName, Object value) {
    try {

      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
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

  @Override
  public void createTable() {
    try {
      PreparedStatement s1 =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
      Statement s = ConnectionHandler.getInstance().getConnection().createStatement();
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

  @Override
  public boolean entryExists(String pkID) {
    boolean exists = false;
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
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

  @Override
  public Location getEntry(String pkID) {
    Location loc = new Location("", 0, 0, "", "", "", "", "");
    try {
      if (!this.entryExists(pkID)) {
        return null;
      }
      // System.out.println(pkID);
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SELECT * FROM " + tbName + " WHERE nodeID = ?");
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

  @Override
  public boolean loadFromArrayList(ArrayList<Location> objList) {
    this.createTable();
    deleteTableData();
    System.out.println("LOCATION SWITCH");
    System.out.println(objList);
    System.out.println(ConnectionHandler.getInstance().getConnection());
    for (Location loc : objList) {
      if (!this.addEntry(loc)) {
        return false;
      }
    }
    return true;
  }

  private void deleteTableData() {
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public ArrayList<Location> getObjList() {
    return objList;
  }
}
