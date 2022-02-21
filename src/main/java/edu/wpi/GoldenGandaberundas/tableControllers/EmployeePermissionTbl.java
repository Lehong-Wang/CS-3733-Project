package edu.wpi.GoldenGandaberundas.tableControllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeePermission;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class EmployeePermissionTbl extends TableController<EmployeePermission, ArrayList<Integer>> {
  // creates the instance for the table
  private static EmployeePermissionTbl instance = null;

  private EmployeePermissionTbl() throws SQLException {
    super("EmployeePermissions", Arrays.asList(new String[] {"empID", "permID"}));
    String[] cols = {"empID", "permID"};
    createTable();

    objList = new ArrayList<EmployeePermission>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static EmployeePermissionTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new EmployeePermissionTbl();
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
  public ArrayList<EmployeePermission> readTable() {
    // creates Array List to return
    ArrayList tableInfo = new ArrayList<EmployeePermission>(); // **
    try {
      // selects all from the table
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new EmployeePermission(r.getInt(1), r.getInt(2)));
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
  public boolean addEntry(EmployeePermission obj) {
    // creates permission object to return
    EmployeePermission empPerm = (EmployeePermission) obj; // **
    PreparedStatement s = null;
    try {
      // inserts the object in the table
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?,?);");

      // **
      s.setInt(1, empPerm.getEmpID());
      s.setInt(2, empPerm.getPermID());
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
  public ArrayList<EmployeePermission> readBackup(String fileName) {
    // creates the array list for the object
    ArrayList<EmployeePermission> empPermList = new ArrayList<EmployeePermission>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      // checks of the attributes are the same with the col
      if (!currentLine.toLowerCase(Locale.ROOT).trim().equals(new String("empID,permID"))) { // **
        System.err.println("employee permission backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        EmployeePermission empPerm = // **
            new EmployeePermission(
                Integer.parseInt(element[0]), Integer.parseInt(element[1])); // **
        empPermList.add(empPerm); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return empPermList; // **
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
          "CREATE TABLE IF NOT EXISTS  EmployeePermissions("
              + "empID INTEGER NOT NULL, "
              + "permID INTEGER NOT NULL, "
              + "CONSTRAINT EmployeePermissionsPK PRIMARY KEY (empID,permID)"
              + "CONSTRAINT EmplyoeePermissionsFK1 FOREIGN KEY (empID) REFERENCES Employees (empID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT EmplyoeePermissionsFK2 FOREIGN KEY (permID) REFERENCES Permissions (permID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE "
              + ");");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return true;
    }
  }

  /**
   * Returns the objects the matches the primary key
   *
   * @param pkID primary key value that identifies the desired object
   * @return the object with the pkid
   */
  @Override
  public EmployeePermission getEntry(ArrayList<Integer> pkID) {
    // creates the object to return
    EmployeePermission empPerm = new EmployeePermission(); // **
    if (this.entryExists(pkID)) {
      try {
        // returns the selected object that matches the pkid
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next(); // **

        if (entryExists(pkID)) {
          empPerm.setEmpID(r.getInt(1));
          empPerm.setPermID(r.getInt(2));
        }
        return empPerm; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return empPerm; // **
  }
}
