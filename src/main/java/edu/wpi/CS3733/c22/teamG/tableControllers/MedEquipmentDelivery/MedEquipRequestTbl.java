package edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionType;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedEquipRequestTbl implements TableController<MedEquipRequest, ArrayList<Integer>> {
  private static MedEquipRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null; // **
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<MedEquipRequest> objList;
  /** relative path to the database file */
  TableController<MedEquipRequest, ArrayList<Integer>> embeddedTable = null;

  TableController<MedEquipRequest, ArrayList<Integer>> clientServerTable = null;
  TableController<MedEquipRequest, ArrayList<Integer>> cloudServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  // first entry in column name is primary key
  private MedEquipRequestTbl() throws SQLException { // creates the table
    tbName = "MedEquipRequests";
    pkCols = "reqID, medEquipID";
    colNames = Arrays.asList(new String[] {"reqID", "medEquipID"});
    objList = new ArrayList<MedEquipRequest>(); // object list
    embeddedTable =
        new MedEquipRequestEmbedded(tbName, colNames.toArray(new String[4]), pkCols, objList);
    clientServerTable =
        new MedEquipRequestClientServer(tbName, colNames.toArray(new String[4]), pkCols, objList);
    cloudServerTable = new MedEquipRequestCloud(objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    connectionHandler.addTable(cloudServerTable, ConnectionType.cloud);
    masterTable = RequestTable.getInstance(); // **
    createTable();
    objList = readTable(); // reads table
  }

  public static MedEquipRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedEquipRequestTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return (MedEquipRequestTbl) instance;
  }

  public MedEquipRequestTbl getOrdersTable() {
    return this.getInstance();
  }

  private TableController<MedEquipRequest, ArrayList<Integer>> getCurrentTable() {
    switch (connectionHandler.getCurrentConnectionType()) {
      case embedded:
        return embeddedTable;
      case clientServer:
        return clientServerTable;
      case cloud:
        return cloudServerTable;
    }
    System.out.println(connectionHandler.getCurrentConnectionType());
    return null;
  }

  @Override
  public ArrayList<MedEquipRequest> readTable() {
    return this.getCurrentTable().readTable();
  }

  @Override
  public boolean addEntry(MedEquipRequest obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  @Override
  public ArrayList<MedEquipRequest> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  @Override
  public MedEquipRequest getEntry(ArrayList<Integer> pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedEquipRequest> objList) {
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
  public ArrayList<MedEquipRequest> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(ArrayList<Integer> pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<MedEquipRequest> getObjList() {
    return objList;
  }
}
