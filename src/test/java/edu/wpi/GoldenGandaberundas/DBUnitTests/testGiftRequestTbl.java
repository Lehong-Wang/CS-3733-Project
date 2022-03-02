package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftRequest;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testGiftRequestTbl {

  String tbName = "GiftRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a GiftRequestController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupGiftRequestTbl() {
    TableController tbControl = null;
    tbControl = GiftRequestTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/GiftRequestsForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<GiftRequest> getRefList() { // **
    ArrayList<GiftRequest> refObjList = new ArrayList<GiftRequest>();
    // NEED ALL ENTRIES IN CSV
    GiftRequest L1 =
        new GiftRequest(
            111, "FDEPT00101", 123, 456, 604800, 864000, 123, "complete", "stuff", 111, 1);
    GiftRequest L2 =
        new GiftRequest(
            222, "FDEPT00101", 456, 123, 604800, 864000, 123, "incomplete", "need more", 222, 2);
    GiftRequest L3 =
        new GiftRequest(
            333, "FDEPT00201", 123, 123, 604800, 864000, 123, "in progress", "please", 333, 2);
    GiftRequest L4 =
        new GiftRequest(
            444, "FDEPT00201", 456, 456, 604800, 864000, 123, "complete", "deliver", 444, 5);
    GiftRequest L5 =
        new GiftRequest(
            555, "FDEPT00301", 123, 123, 604800, 864000, 123, "incomplete", "need fase", 555, 4);
    GiftRequest L6 =
        new GiftRequest(
            666, "FDEPT00301", 456, 456, 604800, 864000, 123, "in progress", "secret", 666, 2);
    //    refObjList.add(L1);
    //    refObjList.add(L2);
    //    refObjList.add(L3);
    //    refObjList.add(L4);
    //    refObjList.add(L5);
    //    refObjList.add(L6);
    GiftRequest m1 =
        new GiftRequest(130, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111, 5);
    GiftRequest m2 =
        new GiftRequest(131, "FDEPT00101", 123, 456, 0, 100, 222, "Submitted", "", 222, 6);
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
    tbControl = setupGiftRequestTbl(); // **

    // create test Object
    GiftRequest testObj =
        new GiftRequest(
            999, "FDEPT00301", 123, 456, 604800, 864000, 333, "In_Progress", "smile", 444, 2); // **
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
    tbControl = setupGiftRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    GiftRequest refObj =
        new GiftRequest(130, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111, 5); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(130);
    test.add(111);
    GiftRequest tbObj = (GiftRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupGiftRequestTbl(); // **
    tbControl = GiftRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<GiftRequest> tbObjList = new ArrayList<GiftRequest>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/GiftRequestsForTesting.csv"); // **

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
    tbControl = setupGiftRequestTbl(); // **

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
    tbControl = setupGiftRequestTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<GiftRequest> refList = getRefList(); // **
    ArrayList<GiftRequest> tbList = tbControl.readTable(); // **

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
    GiftRequestTbl L1 = GiftRequestTbl.getInstance(); // **
    GiftRequestTbl L2 = GiftRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
