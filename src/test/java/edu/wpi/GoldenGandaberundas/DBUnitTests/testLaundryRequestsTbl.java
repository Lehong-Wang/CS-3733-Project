package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.LaundryRequest;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testLaundryRequestsTbl {

  String tbName = "LaundryRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a LaundryRequestTblController call this function before each
   * test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupLaundryRequestsTbl() {
    TableController tbControl = null;
    tbControl = LaundryRequestTbl.getInstance(); // **
    tbControl.loadBackup("backups/laundryRequests.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<LaundryRequest> getRefList() { // **
    ArrayList<LaundryRequest> refObjList = new ArrayList<LaundryRequest>();
    // NEED ALL ENTRIES IN CSV
    LaundryRequest L1 =
        new LaundryRequest(111, "FDEPT00101", 123, 456, 604800, 864000, "complete", "please", 1);
    LaundryRequest L2 =
        new LaundryRequest(222, "FDEPT00101", 456, 123, 604800, 864000, "incomplete", "urgent", 2);
    LaundryRequest L3 =
        new LaundryRequest(333, "FDEPT00201", 123, 456, 604800, 864000, "in progress", "need", 3);
    LaundryRequest L4 =
        new LaundryRequest(
            444, "FDEPT00201", 456, 123, 604800, 864000, "complete", "not urgent", 1);
    LaundryRequest L5 =
        new LaundryRequest(555, "FDEPT00301", 123, 456, 604800, 864000, "incomplete", "need", 2);
    LaundryRequest L6 =
        new LaundryRequest(666, "FDEPT00301", 456, 123, 604800, 864000, "in progress", "fast", 3);
    //    refObjList.add(L1);
    //    refObjList.add(L2);
    //    refObjList.add(L3);
    //    refObjList.add(L4);
    //    refObjList.add(L5);
    //    refObjList.add(L6);

    LaundryRequest m1 = new LaundryRequest(149, "FDEPT00101", 123, 456, 0, 0, "Submitted", "", 111);
    LaundryRequest m2 = new LaundryRequest(150, "FDEPT00101", 123, 456, 0, 0, "Submitted", "", 222);

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
    tbControl = setupLaundryRequestsTbl(); // **

    // create test Object
    LaundryRequest testObj =
        new LaundryRequest(
            777, "FDEPT00301", 456, 123, 604800, 864000, "In_Progress", "need", 444); // **
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
    tbControl = setupLaundryRequestsTbl(); // **
    // create object from first object in csv and get that object with getEntry
    LaundryRequest refObj =
        new LaundryRequest(149, "FDEPT00101", 123, 456, 0, 0, "Submitted", "", 111); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(149);
    test.add(111);
    LaundryRequest tbObj = (LaundryRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    //    tbControl = setupLaundryRequestsTbl(); // **
    tbControl = LaundryRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<LaundryRequest> tbObjList = new ArrayList<LaundryRequest>(); // **
    tbObjList = tbControl.readBackup("backups/laundryRequests.csv"); // **

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
    tbControl = setupLaundryRequestsTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "laundryID"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupLaundryRequestsTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<LaundryRequest> refList = getRefList(); // **
    ArrayList<LaundryRequest> tbList = tbControl.readTable(); // **

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
    LaundryRequestTbl L1 = LaundryRequestTbl.getInstance(); // **
    LaundryRequestTbl L2 = LaundryRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
