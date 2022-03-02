package edu.wpi.CS3733.c22.teamG.tableControllers.Requests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionType;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RequestTable implements TableController<Request, Integer> {
  private static RequestTable instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Request> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  TableController<Request, Integer> embeddedTable = null;

  TableController<Request, Integer> clientServerTable = null;

  TableController<Request, Integer> cloudServerTable = null;

  /**
   * creates an instance of the master request table this table holds common and meta data across
   * all data objects
   *
   * @throws SQLException
   */
  private RequestTable() throws SQLException {

    String[] cols = {
      "requestID", "locationID", "empInitiated", "empCompleter",
      "timeStart", "timeEnd", "patientID", "requestType",
      "requestStatus", "notes"
    };
    tbName = "Requests";
    colNames = Arrays.asList(cols);
    pkCols = "requestID";

    objList = new ArrayList<>();
    embeddedTable = new RequestEmbedded(tbName, cols, pkCols, objList);
    clientServerTable = new RequestClientServer(tbName, cols, pkCols, objList);
    cloudServerTable = new RequestCloud(objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    connectionHandler.addTable(cloudServerTable, ConnectionType.cloud);
    createTable();
    objList = readTable();
    objList = readTable();
  }

  /**
   * gives access to a static instance of the class so that only 1 instance of the class exists at
   * one time Implementation of singleton design pattern
   *
   * @return returns either a new instance if no previous calls, or reference to existing instance
   */
  public static RequestTable getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new RequestTable();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    instance.readTable();
    return instance;
  }

  private TableController<Request, Integer> getCurrentTable() {
    switch (ConnectionHandler.getInstance().getCurrentConnectionType()) {
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

  /**
   * Generates an array list of request objects that represents the set of data stored in the DB
   * table
   *
   * @return arraylist that holds data stored in DB table, null if error
   */
  @Override
  public ArrayList<Request> readTable() {
    return this.getCurrentTable().readTable();
  }

  /**
   * adds request object into master request table and corresponding specific service request table
   *
   * @param obj a request type object or one of its child classes
   * @return true on success, false otherwise.
   */
  @Override
  public boolean addEntry(Request obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  @Override
  public ArrayList<Request> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  @Override
  public Request getEntry(Integer pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Request> objList) {
    return this.getCurrentTable().loadFromArrayList(objList);
  }

  /**
   * Internal method to get the specifc service table controller given a request object
   *
   * @param specific request from a specifc service component
   * @return
   */
  private TableController getSpecificServiceTable(Request specific) {
    TableController table = null;
    switch (specific.getRequestType()) {
      case "MedEquipDelivery":
        return MedEquipRequestTbl.getInstance();
      case "MedicineDelivery":
        return MedicineRequestTbl.getInstance();
      case "LaundryService":
        return LaundryRequestTbl.getInstance();
      case "GiftFloral":
        return GiftRequestTbl.getInstance();
      case "Food":
        return FoodRequestTbl.getInstance();
      case "Computer":
        return ComputerRequestTbl.getInstance();
      case "AudioVisual":
        return AudioVisualRequestTbl.getInstance();
      default:
        return null;
    }
  }

  /**
   * to edit a specifc service request
   *
   * @param req an arraylist consisting of the composite key of the specific request
   * @param colName the attribute to be changed
   * @param value the value that the attribute will be set to
   * @return true on success, false otherwise
   */
  public boolean editEntry(Request req, String colName, Object value) {
    if (colNames.contains(colName)) {
      return this.editEntry(req.getRequestID(), colName, value);
    } else {
      return this.getSpecificServiceTable(req).editEntry(req.getPK(), colName, value);
    }
  }

  public void writeTable() {
    this.getCurrentTable().writeTable();
  }

  public ArrayList<Request> getObjList() {
    return objList;
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
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    this.createBackup(f);
  }

  // drop current table and enter data from CSV
  public ArrayList<Request> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(Integer pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }
}
