package edu.wpi.GoldenGandaberundas.tableControllers.LaundryService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LaundryTbl implements TableController<Laundry, Integer> {

  private static LaundryTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Laundry> objList;
  /** relative path to the database file */
  TableController<Laundry, Integer> embeddedTable = null;

  TableController<Laundry, Integer> clientServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  private LaundryTbl() throws SQLException {
    tbName = "Laundry";
    pkCols = "laundryID";
    colNames = Arrays.asList(new String[] {"laundryID", "laundryType", "description", "inStock"});

    objList = new ArrayList<Laundry>();

    embeddedTable = new LaundryEmbedded(tbName, colNames.toArray(new String[4]), pkCols, objList);
    clientServerTable =
        new LaundryClientServer(tbName, colNames.toArray(new String[4]), pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);

    createTable();

    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static LaundryTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new LaundryTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  private TableController<Laundry, Integer> getCurrentTable() {
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

  @Override
  public ArrayList<Laundry> readTable() {
    return this.getCurrentTable().readTable();
  }

  @Override
  public ArrayList<Laundry> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  @Override
  public Laundry getEntry(Integer pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Laundry> objList) {
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
  public boolean editEntry(Integer pkid, String colName, Object value) {
    return this.getCurrentTable().editEntry(pkid, colName, value);
  }

  /**
   * removes a row from the database
   *
   * @param pkid primary key of row to be removed
   * @return true if successful, false otherwise
   */
  public boolean deleteEntry(Integer pkid) {
    return this.getCurrentTable().deleteEntry(pkid);
  }

  /**
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  @Override
  public boolean addEntry(Laundry obj) {
    return this.getCurrentTable().addEntry(obj);
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
  public ArrayList<Laundry> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(Integer pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<Laundry> getObjList() {
    return objList;
  }
}
