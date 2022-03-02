package edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService;

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

public class ComputerRequestTbl implements TableController<ComputerRequest, ArrayList<Integer>> {

  // **
  // created instance for singleton
  private static ComputerRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<ComputerRequest> objList;
  /** relative path to the database file */
  TableController<ComputerRequest, ArrayList<Integer>> embeddedTable = null;

  TableController<ComputerRequest, ArrayList<Integer>> clientServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  // created constructor for the table
  private ComputerRequestTbl() throws SQLException {
    String[] cols = {"reqID", "computerID", "problemType", "priority"};
    tbName = "ComputerRequests";
    pkCols = "reqID, computerID";
    colNames = Arrays.asList(cols);
    masterTable = RequestTable.getInstance();
    objList = new ArrayList<ComputerRequest>();
    embeddedTable =
        new ComputerRequestEmbedded(tbName, colNames.toArray(new String[4]), pkCols, objList);
    clientServerTable =
        new ComputerRequestClientServer(tbName, colNames.toArray(new String[4]), pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    masterTable = RequestTable.getInstance();
    createTable();
    objList = readTable();
  }

  // **
  // created getInstance method for singleton implementation
  public static ComputerRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new ComputerRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  private TableController<ComputerRequest, ArrayList<Integer>> getCurrentTable() {
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

  // Creates readTable method and returns an array list of Computer Requests

  @Override
  public ArrayList<ComputerRequest> readTable() {
    return this.getCurrentTable().readTable();
  }

  @Override
  public boolean addEntry(ComputerRequest obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  @Override
  public ArrayList<ComputerRequest> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  @Override
  public ComputerRequest getEntry(ArrayList<Integer> pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<ComputerRequest> objList) {
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
  public ArrayList<ComputerRequest> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(ArrayList<Integer> pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<ComputerRequest> getObjList() {
    return objList;
  }
}
