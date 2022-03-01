package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MedEquipmentTbl implements TableController<MedEquipment, Integer> {

  private static MedEquipmentTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<MedEquipment> objList;
  /** relative path to the database file */
  TableController<MedEquipment, Integer> embeddedTable = null;

  TableController<MedEquipment, Integer> clientServerTable = null;

  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
  Connection connection = connectionHandler.getConnection();

  private MedEquipmentTbl() throws SQLException {
    tbName = "MedEquipment";
    pkCols = "medID";
    colNames = Arrays.asList(new String[] {"medID", "medEquipmentType", "status", "currLoc"});

    objList = new ArrayList<MedEquipment>();
    embeddedTable =
        new MedEquipmentEmbedded(tbName, colNames.toArray(new String[4]), pkCols, objList);
    clientServerTable =
        new MedEquipmentClientServer(tbName, colNames.toArray(new String[4]), pkCols, objList);
    connectionHandler.addTable(embeddedTable, ConnectionType.embedded);
    connectionHandler.addTable(clientServerTable, ConnectionType.clientServer);

    createTable();

    objList = readTable();
  }

  public static MedEquipmentTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedEquipmentTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return (MedEquipmentTbl) instance;
  }

  private TableController<MedEquipment, Integer> getCurrentTable() {
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
  public ArrayList readTable() {
    return this.getCurrentTable().readTable();
  }

  public boolean deleteEntry(Integer pkid) {
    return this.getCurrentTable().deleteEntry(pkid);
  }

  @Override
  public boolean addEntry(MedEquipment obj) {
    return this.getCurrentTable().addEntry(obj);
  }

  @Override
  public ArrayList<MedEquipment> readBackup(String fileName) {
    return this.getCurrentTable().readBackup(fileName);
  }

  @Override
  public void createTable() {
    this.getCurrentTable().createTable();
  }

  public MedEquipment getEntry(Integer pkid) {
    return this.getCurrentTable().getEntry(pkid);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedEquipment> objList) {
    return false;
  }

  public MedEquipment createMedEquipment(String[] ele) {
    MedEquipment med = new MedEquipment(Integer.parseInt(ele[0]), ele[1], ele[2], ele[3]);
    return med;
  }

  public void writeTable() {

    for (MedEquipment obj : objList) {

      this.addEntry(obj);
    }
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
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    this.getCurrentTable().createBackup(f);
  }

  // drop current table and enter data from CSV
  public ArrayList<MedEquipment> loadBackup(String fileName) {
    return this.getCurrentTable().loadBackup(fileName);
  }

  // checks if an entry exists
  public boolean entryExists(Integer pkID) {
    return this.getCurrentTable().entryExists(pkID);
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<MedEquipment> getObjList() {
    return objList;
  }
}
