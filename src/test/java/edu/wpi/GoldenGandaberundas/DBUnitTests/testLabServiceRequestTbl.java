package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService.LabServiceRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testLabServiceRequestTbl {

  String tbName = "LabServiceRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a LabServiceRequestController call this function before each
   * test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupLabServiceRequestTbl() {
    TableController tbControl = null;
    tbControl = LabServiceRequestTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/LabServiceRequestsForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<LabServiceRequest> getRefList() { // **
    ArrayList<LabServiceRequest> refObjList = new ArrayList<LabServiceRequest>();
    // NEED ALL ENTRIES IN CSV
    LabServiceRequest L1 =
        new LabServiceRequest(
            111, "FDEPT00101", 123, 456, 604800, 864000, 123, "complete", "stuff", 111, "TEST");
    LabServiceRequest L2 =
        new LabServiceRequest(
            222,
            "FDEPT00101",
            456,
            123,
            604800,
            864000,
            123,
            "incomplete",
            "need more",
            222,
            "TEST");
    LabServiceRequest L3 =
        new LabServiceRequest(
            333, "FDEPT00201", 123, 123, 604800, 864000, 123, "in progress", "please", 333, "TEST");
    LabServiceRequest L4 =
        new LabServiceRequest(
            444, "FDEPT00201", 456, 456, 604800, 864000, 123, "complete", "deliver", 444, "TEST");
    LabServiceRequest L5 =
        new LabServiceRequest(
            555,
            "FDEPT00301",
            123,
            123,
            604800,
            864000,
            123,
            "incomplete",
            "need fase",
            555,
            "TEST");
    LabServiceRequest L6 =
        new LabServiceRequest(
            666, "FDEPT00301", 456, 456, 604800, 864000, 123, "in progress", "secret", 666, "TEST");
    //    refObjList.add(L1);
    //    refObjList.add(L2);
    //    refObjList.add(L3);
    //    refObjList.add(L4);
    //    refObjList.add(L5);
    //    refObjList.add(L6);
    LabServiceRequest m1 =
        new LabServiceRequest(
            130, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111, "TEST");
    LabServiceRequest m2 =
        new LabServiceRequest(
            131, "FDEPT00101", 123, 456, 0, 100, 222, "Submitted", "", 222, "TEST");
    refObjList.add(m1);
    refObjList.add(m2);

    RequestTable.getInstance().addEntry(m1);
    RequestTable.getInstance().addEntry(m2);

    return refObjList;
  }

  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupLabServiceRequestTbl(); // **

    // create test Object
    LabServiceRequest testObj =
        new LabServiceRequest(
            999,
            "FDEPT00301",
            123,
            456,
            604800,
            864000,
            333,
            "In_Progress",
            "smile",
            444,
            "TEST"); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(testObj.getPK()); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupLabServiceRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    LabServiceRequest refObj =
        new LabServiceRequest(
            130, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111, "TEST"); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(130);
    test.add(111);
    LabServiceRequest tbObj = (LabServiceRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupLabServiceRequestTbl(); // **
    tbControl = LabServiceRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<LabServiceRequest> tbObjList = new ArrayList<LabServiceRequest>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/LabServiceRequestsForTesting.csv"); // **

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
    ArrayList<LabServiceRequest> refList = getRefList(); // **
    ArrayList<LabServiceRequest> tbList = tbControl.readTable(); // **

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
    LabServiceRequestTbl L1 = LabServiceRequestTbl.getInstance(); // **
    LabServiceRequestTbl L2 = LabServiceRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
