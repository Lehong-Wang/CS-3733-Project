package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeePermission;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testEmployeePermissionTbl {

  String tbName = "EmployeePermissions"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a EmployeePermissionTblController call this function before
   * each test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupEmployeePermissionTbl() {
    TableController tbControl = null;
    tbControl = EmployeePermissionTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/EmployeePermissionsForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<EmployeePermission> getRefList() { // **
    ArrayList<EmployeePermission> refObjList = new ArrayList<EmployeePermission>();
    // NEED ALL ENTRIES IN CSV
    EmployeePermission L1 = new EmployeePermission(123, 111);
    EmployeePermission L2 = new EmployeePermission(456, 222);
    EmployeePermission L3 = new EmployeePermission(123, 333);
    refObjList.add(L1);
    refObjList.add(L2);
    refObjList.add(L3);

    return refObjList;
  }

  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupEmployeePermissionTbl(); // **

    // create test Object
    EmployeePermission testObj = new EmployeePermission(456, 444); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    ArrayList<Integer> test = new ArrayList<Integer>();
    test.add(testObj.getEmpID());
    test.add(testObj.getPermID());
    objExist = tbControl.entryExists(test); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupEmployeePermissionTbl(); // **
    // create object from first object in csv and get that object with getEntry
    EmployeePermission refObj = new EmployeePermission(123, 111); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(123);
    test.add(555);
    EmployeePermission tbObj = (EmployeePermission) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupEmployeePermissionTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<EmployeePermission> tbObjList = new ArrayList<EmployeePermission>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/EmployeePermissionsForTesting.csv"); // **

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
    tbControl = setupEmployeePermissionTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"empID", "permID"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupEmployeePermissionTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<EmployeePermission> refList = getRefList(); // **
    ArrayList<EmployeePermission> tbList = tbControl.readTable(); // **

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
    EmployeePermissionTbl L1 = EmployeePermissionTbl.getInstance(); // **
    EmployeePermissionTbl L2 = EmployeePermissionTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
