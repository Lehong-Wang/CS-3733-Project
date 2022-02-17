package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testRequestTable {

  String tbName = "Requests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a RequestTableController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupRequestTable() {
    TableController tbControl = null;
    tbControl = RequestTable.getInstance(); // **
    // tbControl.loadBackup("TestCSVs/RequestTableForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Request> getRefList() { // **
    ArrayList<Request> refObjList = new ArrayList<Request>();
    // NEED ALL ENTRIES IN CSV
    Request R1 =
        new Request(
            111,
            "FDEPT00101",
            123,
            999,
            604800,
            864000,
            123,
            "MedEquipDelivery",
            "Submitted",
            "please");
    Request R2 =
        new Request(
            222,
            "FDEPT00101",
            456,
            888,
            604800,
            864000,
            123,
            "MedicineDelivery",
            "Completed",
            "quick");
    Request R3 =
        new Request(
            333,
            "FDEPT00201",
            666,
            777,
            604800,
            864000,
            123,
            "GiftFloral",
            "In_Progress",
            "urgent");
    Request R4 =
        new Request(
            444,
            "FDEPT00201",
            777,
            666,
            604800,
            864000,
            123,
            "LaundryService",
            "Completed",
            "thanks");
    Request R5 =
        new Request(
            555, "FDEPT00301", 888, 456, 604800, 864000, 123, "MedDeliv", "Submitted", "needed");
    Request R6 =
        new Request(
            666, "FDEPT00301", 999, 123, 604800, 864000, 123, "CompServ", "In_Progress", "help");
    refObjList.add(R1);
    refObjList.add(R2);
    refObjList.add(R3);
    refObjList.add(R4);
    refObjList.add(R5);
    refObjList.add(R6);

    return refObjList;
  }

  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupRequestTable(); // **

    // create test Object
    Request testObj =
        new Request(
            777,
            "FDEPT00301",
            456,
            123,
            604800,
            864000,
            123,
            "CompServ",
            "In_Progress",
            "please"); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(testObj.getRequestID()); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupRequestTable(); // **
    // create object from first object in csv and get that object with getEntry
    Request refObj =
        new Request(
            111,
            "FDEPT00101",
            123,
            999,
            604800,
            864000,
            123,
            "MedEquipDelivery",
            "Submitted",
            "please"); // **
    Request tbObj = (Request) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupRequestTable(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Request> tbObjList = new ArrayList<Request>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/RequestTableForTesting.csv"); // **

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
    tbControl = setupRequestTable(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"medicineID", "medName", "description", "price", "inStock"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupRequestTable(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Request> refList = getRefList(); // **
    ArrayList<Request> tbList = tbControl.readTable(); // **

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
    RequestTable L1 = RequestTable.getInstance(); // **
    RequestTable L2 = RequestTable.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
