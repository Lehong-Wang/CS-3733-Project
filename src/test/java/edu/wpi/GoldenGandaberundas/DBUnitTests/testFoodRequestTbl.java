package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodRequest;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testFoodRequestTbl {

  String tbName = "FoodRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a FoodRequestTblController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupFoodRequestTbl() {
    TableController tbControl = null;
    tbControl = FoodRequestTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/FoodRequestForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<FoodRequest> getRefList() { // **
    ArrayList<FoodRequest> refObjList = new ArrayList<FoodRequest>();
    // NEED ALL ENTRIES IN CSV
    FoodRequest m1 =
        new FoodRequest(70, "FDEPT00101", 123, 999, 0, 864000, 111, "Submitted", "note1", 111, 10);
    FoodRequest m2 =
        new FoodRequest(71, "FDEPT00201", 456, 888, 0, 864000, 222, "Completed", "note2", 222, 11);
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
    tbControl = setupFoodRequestTbl(); // **

    // create test Object
    FoodRequest testObj =
        new FoodRequest(
            72, "FDEPT00201", 456, 888, 0, 864000, 333, "Completed", "note3", 333, 12); // **
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
    tbControl = setupFoodRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    FoodRequest refObj =
        new FoodRequest(
            70, "FDEPT00101", 123, 999, 0, 864000, 111, "Submitted", "note1", 111, 10); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(70);
    test.add(111);
    FoodRequest tbObj = (FoodRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupMedicineRequestsTbl(); // **
    tbControl = FoodRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<FoodRequest> tbObjList = new ArrayList<FoodRequest>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/FoodRequestForTesting.csv"); // **

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
    tbControl = setupFoodRequestTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "foodID", "quantity"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupFoodRequestTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<FoodRequest> refList = getRefList(); // **
    ArrayList<FoodRequest> tbList = tbControl.readTable(); // **

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
    FoodRequestTbl L1 = FoodRequestTbl.getInstance(); // **
    FoodRequestTbl L2 = FoodRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
