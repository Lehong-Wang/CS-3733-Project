package edu.wpi.GoldenGandaberundas.tableControllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Permission;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class PermissionTbl extends TableController<Permission, Integer> {

  // creates the instance for the table
  private static PermissionTbl instance = null;

  private PermissionTbl() throws SQLException {
    super("Permissions", Arrays.asList(new String[] {"permID", "type", "permDescription"}));
    String[] cols = {"permID", "type", "permDescription"};
    createTable();

    objList = new ArrayList<Permission>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static PermissionTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new PermissionTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance; // returns instance
  }

  /**
   * Reads the current table for the object
   *
   * @return the Array List of the objects in the table
   */
  @Override
  public ArrayList<Permission> readTable() {
    // creates Array List to return
    ArrayList tableInfo = new ArrayList<Permission>(); // **
    try {
      // selects all from the table
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new Permission(r.getInt(1), r.getString(2), r.getString(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo; // returns the Array List of objects
  }

  /**
   * Adds the object to the table
   *
   * @param obj the incoming object to add to the table
   * @return true if the object is added
   */
  @Override
  public boolean addEntry(Permission obj) {
    // creates permission object to return
    Permission permission = (Permission) obj; // **
    PreparedStatement s = null;
    try {
      // inserts the object in the table
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?,?,?);");

      // **
      s.setInt(1, permission.getPermID());
      s.setString(2, permission.getType());
      s.setString(3, permission.getPermDescription());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * reads a backup CSV file to create a list of the object
   *
   * @param fileName location of the CSV file
   * @return Array List of the of object
   */
  @Override
  public ArrayList<Permission> readBackup(String fileName) {
    // creates the array list for the object
    ArrayList<Permission> permList = new ArrayList<Permission>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      // checks of the attributes are the same with the col
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("permID,type,permDescription"))) { // **
        System.err.println("permission backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Permission perm = // **
            new Permission(Integer.parseInt(element[0]), element[1], element[2]); // **
        permList.add(perm); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return permList; // **
  }

  /** Method to create the Table for with the proper attributes */
  @Override
  public boolean createTable() {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return false;
    }

    System.out.println("SQLite driver registered!");

    Statement s = null;
    try {
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON"); // **
      // creates the table
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Permissions("
              + "permID INTEGER NOT NULL, "
              + "type TEXT NOT NULL, "
              + "permDescription TEXT, "
              + "CONSTRAINT PermissionsPK PRIMARY KEY (permID));");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Returns the objects the matches the primary key
   *
   * @param pkID primary key value that identifies the desired object
   * @return the object with the pkid
   */
  @Override
  public Permission getEntry(Integer pkID) {
    // creates the object to return
    Permission perm = new Permission(); // **
    if (this.entryExists(pkID)) {
      try {
        // returns the selected object that matches the pkid
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next(); // **
        perm.setPermID(r.getInt(1));
        perm.setType(r.getString(2));
        perm.setPermDescription(r.getString(3));
        return perm; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return perm; // **
  }
}
