package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabService;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testLabServiceTbl {

  String tbName = "LabServices"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a LabServiceRequestController call this function before each
   * test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupLabServiceRequestTbl() {
    TableController tbControl = null;
    tbControl = LabServiceTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/LabServiceRequestsForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<LabService> getObjList() { // **
    ArrayList<LabService> objList = new ArrayList<LabService>();
    // NEED ALL ENTRIES IN CSV
    LabService L1 = new LabService(111, "TEST1", "TEST");
    LabService L2 = new LabService(222, "TEST2", "TEST");
    LabService L3 = new LabService(333, "TEST3", "TEST");
    LabService L4 = new LabService(444, "TEST4", "TEST");
    LabService L5 = new LabService(555, "TEST5", "TEST");
    LabService L6 = new LabService(666, "TEST6", "TEST");
    objList.add(L1);
    objList.add(L2);
    objList.add(L3);
    objList.add(L4);
    objList.add(L5);
    objList.add(L6);

    return objList;
  }

  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupLabServiceRequestTbl(); // **

    // create test Object
    LabService testObj = new LabService(999, "TEST8", "TEST"); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(testObj.getLabID()); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupLabServiceRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    LabService refObj = new LabService(130, "TEST8", "TEST"); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(130);
    test.add(111);
    LabService tbObj = (LabService) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupLabServiceRequestTbl(); // **
    tbControl = LabServiceTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<LabService> tbObjList = new ArrayList<LabService>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/LabServiceRequestsForTesting.csv"); // **

    boolean isSame = getObjList().equals(tbObjList);
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
    tbControl = setupLabServiceRequestTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "giftID", "quantity"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupLabServiceRequestTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<LabService> refList = getObjList(); // **
    ArrayList<LabService> tbList = tbControl.readTable(); // **

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
    LabServiceTbl L1 = LabServiceTbl.getInstance(); // **
    LabServiceTbl L2 = LabServiceTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
