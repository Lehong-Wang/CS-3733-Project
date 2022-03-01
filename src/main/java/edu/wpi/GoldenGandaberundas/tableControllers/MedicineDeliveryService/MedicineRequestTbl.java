package edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedicineRequestTbl implements TableController<MedicineRequest, ArrayList<Integer>> {

  private static MedicineRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<MedicineRequest> objList;
  /** relative path to the database file */
  TableController<MedicineRequest, ArrayList<Integer>> embeddedTable = null;

  TableController<MedicineRequest, ArrayList<Integer>> clientServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  private MedicineRequestTbl() throws SQLException {
    String[] cols = {"reqID", "medicineID", "dosage", "quantity"};
    tbName = "MedicineRequests";
    pkCols = "reqID, medicineID";
    colNames = Arrays.asList(cols);
    objList = new ArrayList<MedicineRequest>();
    embeddedTable =
        new MedicineRequestEmbedded(tbName, colNames.toArray(new String[4]), pkCols, objList);
    clientServerTable =
        new MedicineRequestClientServer(tbName, colNames.toArray(new String[4]), pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    masterTable = RequestTable.getInstance();
    createTable();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static MedicineRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedicineRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  private TableController<MedicineRequest, ArrayList<Integer>> getCurrentTable() {
    System.out.println("Connection Type: " + connectionHandler.getCurrentConnectionType());
    switch (connectionHandler.getCurrentConnectionType()) {
      case embedded:
        return embeddedTable;
      case clientServer:
        return clientServerTable;
      case cloud:
        return null;
    }
    System.out.println(connectionHandler.getCurrentConnectionType());
    return null;
  }

  @Override
  public ArrayList<MedicineRequest> readTable() {
    return this.getCurrentTable().readTable();
  }

  @Override
  public boolean addEntry(MedicineRequest obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  @Override
  public ArrayList<MedicineRequest> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  @Override
  public MedicineRequest getEntry(ArrayList<Integer> pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedicineRequest> objList) {
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
  public ArrayList<MedicineRequest> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(ArrayList<Integer> pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<MedicineRequest> getObjList() {
    return objList;
  }
}
