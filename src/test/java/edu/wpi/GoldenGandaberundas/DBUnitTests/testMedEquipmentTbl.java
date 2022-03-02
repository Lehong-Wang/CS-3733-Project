package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testMedEquipmentTbl {

  String tbName = "MedEquipment"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a MedEquipmentController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupMedEquipment() {
    TableController tbControl = null;
    tbControl = MedEquipmentTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/MedEquipmentForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<MedEquipment> getRefList() { // **
    ArrayList<MedEquipment> refObjList = new ArrayList<MedEquipment>();
    // NEED ALL ENTRIES IN CSV
    MedEquipment L1 = new MedEquipment(111, "X-ray", "In-use", "FDEPT00301");
    MedEquipment L2 = new MedEquipment(222, "Recliner", "Stored", "FDEPT00101");
    MedEquipment L3 = new MedEquipment(333, "Infusion Pump", "Dirty", "FDEPT00201");
    MedEquipment L4 = new MedEquipment(444, "X-ray", "Clean", "FDEPT00101");
    MedEquipment L5 = new MedEquipment(555, "Bed", "Dirty", "FDEPT00201");
    MedEquipment L6 = new MedEquipment(666, "Recliner", "In-use", "FDEPT00101");
    refObjList.add(L1);
    refObjList.add(L2);
    refObjList.add(L3);
    refObjList.add(L4);
    refObjList.add(L5);
    refObjList.add(L6);

    return refObjList;
  }

  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupMedEquipment(); // **

    // create test Object
    MedEquipment testObj = new MedEquipment(777, "X-ray", "In-use", "FDEPT00301"); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(777); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupMedEquipment(); // **
    // create object from first object in csv and get that object with getEntry
    MedEquipment refObj = new MedEquipment(111, "X-ray", "In-use", "FDEPT00301"); // **
    MedEquipment tbObj = (MedEquipment) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupMedEquipment(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<MedEquipment> tbObjList = new ArrayList<>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/MedEquipmentForTesting.csv"); // **

    boolean isSame = getRefList().equals(tbObjList);
    Assertions.assertTrue(isSame);
  }

  /**
   * test for createTable() need to fix SQLite database locked error
   *
   * @throws SQLException
   */
  @Test
  public void testCreateTable() throws SQLException {
    // create a new connection object to access db
    Connection testConnection = DriverManager.getConnection(dbPath);
    TableController tbControl;
    tbControl = setupMedEquipment(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"medID", "medEquipmentType", "status", "currLoc"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupMedEquipment(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<MedEquipment> refList = getRefList(); // **
    ArrayList<MedEquipment> tbList = tbControl.readTable(); // **

    Boolean isSame = refList.equals(tbList);
    Assertions.assertTrue(isSame);
  }

  /**
   * test for getInstance()
   *
   * @throws SQLException
   */
  @Test
  public void testGetInstance() throws SQLException {

    // create two instance with getInstance
    // see if they are same object in memory
    MedEquipmentTbl L1 = MedEquipmentTbl.getInstance(); // **
    MedEquipmentTbl L2 = MedEquipmentTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
