package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Permission;
import edu.wpi.CS3733.c22.teamG.tableControllers.PermissionTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testPermissionTbl {

  String tbName = "Permissions"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a PermissionTblController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupPermissionTbl() {
    TableController tbControl = null;
    tbControl = PermissionTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/PermissionsForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Permission> getRefList() { // **
    ArrayList<Permission> refObjList = new ArrayList<Permission>();
    // NEED ALL ENTRIES IN CSV
    Permission L1 = new Permission(111, "admin", "Does all");
    Permission L2 = new Permission(222, "IT", "Tech");
    Permission L3 = new Permission(333, "Nurse", "Healthcare");
    Permission L4 = new Permission(444, "Doctor", "Healthcare");
    Permission L5 = new Permission(555, "Janitor", "Laundry");
    Permission L6 = new Permission(666, "Delivered", "Delivers");
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
    tbControl = setupPermissionTbl(); // **

    // create test Object
    Permission testObj = new Permission(777, "Joe", "Smith"); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(testObj.getPermID()); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupPermissionTbl(); // **
    // create object from first object in csv and get that object with getEntry
    Permission refObj = new Permission(111, "admin", "Does all"); // **
    Permission tbObj = (Permission) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupPermissionTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Permission> tbObjList = new ArrayList<Permission>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/PermissionsForTesting.csv"); // **

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
    tbControl = setupPermissionTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"permID", "type", "permDescription"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupPermissionTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Permission> refList = getRefList(); // **
    ArrayList<Permission> tbList = tbControl.readTable(); // **

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
    PermissionTbl L1 = PermissionTbl.getInstance(); // **
    PermissionTbl L2 = PermissionTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
