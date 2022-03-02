package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.ComputerRequest;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testComputerRequestTbl {

  String tbName = "ComputerRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a ComputerRequestTblController call this function before each
   * test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupComputerRequestTbl() {
    TableController tbControl = null;
    tbControl = ComputerRequestTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/ComputerRequestForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<ComputerRequest> getRefList() { // **
    ArrayList<ComputerRequest> refObjList = new ArrayList<ComputerRequest>();
    // NEED ALL ENTRIES IN CSV
    ComputerRequest m1 =
        new ComputerRequest(
            60, "FDEPT00101", 123, 999, 0, 864000, 111, "Submitted", "note1", 111, "power", "high");
    ComputerRequest m2 =
        new ComputerRequest(
            61, "FDEPT00201", 456, 888, 0, 864000, 222, "Completed", "note2", 222, "off", "low");
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
    tbControl = setupComputerRequestTbl(); // **

    // create test Object
    ComputerRequest testObj =
        new ComputerRequest(
            62,
            "FDEPT00201",
            456,
            888,
            0,
            864000,
            222,
            "Completed",
            "note2",
            333,
            "off",
            "low"); // **
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
    tbControl = setupComputerRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    ComputerRequest refObj =
        new ComputerRequest(
            60,
            "FDEPT00101",
            123,
            999,
            0,
            864000,
            111,
            "Submitted",
            "note1",
            111,
            "power",
            "high"); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(60);
    test.add(111);
    ComputerRequest tbObj = (ComputerRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupMedicineRequestsTbl(); // **
    tbControl = ComputerRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<ComputerRequest> tbObjList = new ArrayList<ComputerRequest>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/ComputerRequestForTesting.csv"); // **

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
    tbControl = setupComputerRequestTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "computerID", "problemType", "priority"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupComputerRequestTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<ComputerRequest> refList = getRefList(); // **
    ArrayList<ComputerRequest> tbList = tbControl.readTable(); // **

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
    ComputerRequestTbl L1 = ComputerRequestTbl.getInstance(); // **
    ComputerRequestTbl L2 = ComputerRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
