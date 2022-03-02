package edu.wpi.CS3733.c22.teamG.tableControllers;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionType;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.EmployeePermission;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EmployeePermissionTbl
    implements TableController<EmployeePermission, ArrayList<Integer>> {
  // creates the instance for the table
  private static EmployeePermissionTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<EmployeePermission> objList;
  /** relative path to the database file */
  TableController<EmployeePermission, ArrayList<Integer>> embeddedTable = null;

  TableController<EmployeePermission, ArrayList<Integer>> clientServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  private EmployeePermissionTbl() throws SQLException {
    tbName = "EmployeePermissions";
    colNames = Arrays.asList(new String[] {"empID", "permID"});
    pkCols = "empID, permID";
    objList = new ArrayList<EmployeePermission>();
    embeddedTable =
        new EmployeePermissionEmbedded(tbName, colNames.toArray(new String[2]), pkCols, objList);
    clientServerTable =
        new EmployeePermissionClientServer(
            tbName, colNames.toArray(new String[2]), pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    createTable();

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

  private TableController<EmployeePermission, ArrayList<Integer>> getCurrentTable() {
    System.out.println("Connection Type: " + connectionHandler.getCurrentConnectionType());
    switch (connectionHandler.getCurrentConnectionType()) {
      case embedded:
        return embeddedTable;
      case clientServer:
        return clientServerTable;
      case cloud:
        return embeddedTable;
    }
    System.out.println(connectionHandler.getCurrentConnectionType());
    return null;
  }

  /**
   * Reads the current table for the object
   *
   * @return the Array List of the objects in the table
   */
  @Override
  public ArrayList<EmployeePermission> readTable() {
    return this.getCurrentTable().readTable();
  }

  /**
   * Adds the object to the table
   *
   * @param obj the incoming object to add to the table
   * @return true if the object is added
   */
  @Override
  public boolean addEntry(EmployeePermission obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  /**
   * reads a backup CSV file to create a list of the object
   *
   * @param fileName location of the CSV file
   * @return Array List of the of object
   */
  @Override
  public ArrayList<EmployeePermission> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  /** Method to create the Table for with the proper attributes */
  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  /**
   * Returns the objects the matches the primary key
   *
   * @param pkID primary key value that identifies the desired object
   * @return the object with the pkid
   */
  @Override
  public EmployeePermission getEntry(ArrayList<Integer> pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<EmployeePermission> objList) {
    return this.getCurrentTable().loadFromArrayList(objList);
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
  public boolean editEntry(ArrayList<Integer> pkid, String colName, Object value) {
    return this.getCurrentTable().editEntry(pkid, colName, value);
  }

  /**
   * removes a row from the database
   *
   * @param pkid primary key of row to be removed
   * @return true if successful, false otherwise
   */
  public boolean deleteEntry(ArrayList<Integer> pkid) {
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
  public ArrayList<EmployeePermission> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(ArrayList<Integer> pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }
  /**
   * Method that when given an employee ID returns a list of permissions associated with that
   * employee
   *
   * @param empID
   * @return
   */
  public ArrayList<Integer> getPermID(int empID) {
    ArrayList<Integer> permAr = new ArrayList<Integer>();
    try {
      // returns the selected object that matches the pkid
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =(?);");
      s.setInt(1, empID);
      ResultSet r = s.executeQuery();
      while (r.next()) {
        permAr.add(r.getInt(2));
      }
      return permAr; // **
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return permAr; // **
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<EmployeePermission> getObjList() {
    return this.getCurrentTable().getObjList();
  }
}
