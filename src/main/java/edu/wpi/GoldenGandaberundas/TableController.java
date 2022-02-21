package edu.wpi.GoldenGandaberundas;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.sqlite.SQLiteConfig;

public abstract class TableController<T, T1> {

  /** name of table */
  protected String tbName;

  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<T> objList;
  /** relative path to the database file */
  private static String dbPath = new String("jdbc:sqlite:hospitalData.db");

  private static String clientServerPath =
      new String("jdbc:sqlserver://130.215.250.187:58910;databaseName=gandaberunda");
  /** connection object used to access the database */
  protected static Connection connection;
  /** single object allowed to be instantiated in subclasses */
  private static boolean embedded = true;

  private static ArrayList<TableController> allActiveTables = new ArrayList<TableController>();

  /**
   * procedure to connect to the database APP NEEDS TO FAIL IF THIS DOES NOT CONNECT IT IS ESSENTIAL
   * THAT THIS EXECUTES CORRECTLY
   */
  static {
    try {
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      Class.forName("org.sqlite.JDBC"); // check if we have the drive
      connection =
          DriverManager.getConnection(dbPath, config.toProperties()); // create a connection object
      PreparedStatement s = connection.prepareStatement("PRAGMA foreign_keys = ON;");
      s.execute();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  protected TableController(String tableName, List<String> colNames) throws SQLException {
    this.tbName = tableName;
    this.colNames = colNames;
    this.pkCols = colNames.get(0);
    allActiveTables.add(this);
  }

  protected TableController(String tableName, List<String> colNames, String pkCols)
      throws SQLException {
    this.tbName = tableName;
    this.colNames = colNames;
    this.pkCols = pkCols;
    allActiveTables.add(this);
  }

  public String getTableName() {
    return tbName;
  }

  public static void setConnection(boolean embed) {
    embedded = embed;

    try {
      if (embedded) {
        connection.close();
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        connection = DriverManager.getConnection(dbPath, config.toProperties());
        TransferAllData();
      } else {
        connection.close();
        connection = DriverManager.getConnection(clientServerPath, "admin", "admin");
        TransferAllData();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean getEmbedded() {
    return embedded;
  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  public abstract ArrayList<T> readTable();

  // method to write objList to table
  public abstract void writeTable();

  private boolean editEntryComposite(ArrayList<Integer> pkid, String colName, Object value) {
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkid.size() - 1; i++) {
      pkString.append(pkid.get(i)).append(",");
    }
    pkString.append(pkid.get(pkid.size() - 1));
    try {

      PreparedStatement s =
          connection.prepareStatement(
              "UPDATE "
                  + tbName
                  + " SET "
                  + colName
                  + " = ? WHERE ("
                  + pkCols
                  + ") = ("
                  + pkString
                  + ");");
      s.setObject(1, value);
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public abstract boolean deleteEntry(T1 pkid);

  /**
   * Removes the entry from the Object Request table
   *
   * @param pkid The Array List of the interger pkid
   * @return true if completed
   */
  private boolean deleteEntryComposite(ArrayList<Integer> pkid) {
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkid.size() - 1; i++) {
      pkString.append(pkid.get(i)).append(",");
    }
    pkString.append(pkid.get(pkid.size() - 1));
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "DELETE FROM " + tbName + " WHERE (" + pkCols + ") = (" + pkString + ");");
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  public abstract boolean addEntry(T obj);

  public abstract void createBackup(File f);

  /**
   * Loads a CSV file in to memory, parses to find the attributes of the objects stored in the table
   *
   * @param fileName location of the CSV file
   * @return arraylist containing n number of T objects, null if error
   */
  public abstract ArrayList<T> readBackup(String fileName);

  public abstract ArrayList<T> loadBackup(String fileName);

  public abstract boolean editEntry(T1 pkid, String colName, Object value);

  /** runs SQL commands to create the table in the hospitalData.db file */
  public abstract void createTable();

  public abstract boolean entryExists(T1 pkID);

  private boolean entryExistsComposite(ArrayList<Integer> pkid) {
    boolean exists = false;
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkid.size() - 1; i++) {
      pkString.append(pkid.get(i)).append(",");
    }
    pkString.append(pkid.get(pkid.size() - 1));

    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM "
                  + tbName
                  + " WHERE ("
                  + pkCols
                  + ") = ("
                  + pkString.toString()
                  + ");");
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        exists = true;
      }
      return exists;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Retrieves a specific object stored in the database
   *
   * @param pkID primary key value that identifies the desired object
   * @return object stored using PKID or null on error
   */
  public abstract T getEntry(T1 pkID);

  //  /**
  //   * set the objList to the list passed in only used for testing
  //   *
  //   * @param list
  //   * @return the new objList
  //   */
  //  public ArrayList<T> setObjList(ArrayList<T> list) {
  //    objList = list;
  //    return objList;
  //  }

  public void backUpAllTables() {
    File backupDir = new File("Backups");
    if (!backupDir.exists()) {
      backupDir.mkdir();
    }
    for (TableController table : allActiveTables) {
      table.createBackup(
          new File(backupDir.getAbsoluteFile().toString() + "/" + table.tbName + ".csv"));
    }
  }

  public void loadAllTables() {
    File backupDir = new File("Backups");
    if (!backupDir.exists()) {
      System.err.println("load dir fails");
      return;
    }
    for (TableController table : allActiveTables) {
      table.loadBackup(backupDir.getAbsoluteFile().toString() + "/" + table.tbName + ".csv");
    }
  }

  public static void TransferAllData() {
    for (TableController table : allActiveTables) {
      table.createTable();
      table.deleteTableData();
    }
    for (TableController table : allActiveTables) {
      TableController temp = table;
      if (temp.tbName.equals("Requests")) {
        continue;
      }
      for (Object entry : table.objList) {
        table.addEntry(entry);
      }
      table.readTable();
    }
  }

  private void deleteTableData() {
    PreparedStatement s = null;
    try {
      s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
