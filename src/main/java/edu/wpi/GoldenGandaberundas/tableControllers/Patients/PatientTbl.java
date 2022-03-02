package edu.wpi.GoldenGandaberundas.tableControllers.Patients;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PatientTbl implements TableController<Patient, Integer> {

  private static PatientTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Patient> objList;
  /** relative path to the database file */
  TableController<Patient, Integer> embeddedTable = null;

  TableController<Patient, Integer> clientServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
  Connection connection = connectionHandler.getConnection();

  private PatientTbl() throws SQLException {
    String[] cols = {"patientID", "fName", "lName"};
    colNames = Arrays.asList(cols);
    tbName = "Patients";
    pkCols = "patientID";
    objList = new ArrayList<Patient>();
    embeddedTable = new PatientEmbedded(tbName, cols, pkCols, objList);
    clientServerTable = new PatientClientServer(tbName, cols, pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);
    createTable();
    objList = readTable();
  }

  public static PatientTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new PatientTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  private TableController<Patient, Integer> getCurrentTable() {
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

  public ArrayList<Patient> readTable() {
    return this.getCurrentTable().readTable();
  }

  public boolean addEntry(Patient obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  public ArrayList<Patient> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  public void createTable() {
    this.getCurrentTable().createTable();
  }

  public Patient getEntry(Integer pkID) {
    return this.getCurrentTable().getEntry(pkID);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Patient> objList) {
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
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    this.getCurrentTable().createBackup(f);
  }

  // drop current table and enter data from CSV
  public ArrayList<Patient> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(Integer pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<Patient> getObjList() {
    return objList;
  }
}
