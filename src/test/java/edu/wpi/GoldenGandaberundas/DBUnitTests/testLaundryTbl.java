package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.Laundry;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.LaundryTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testLaundryTbl {

  String tbName = "Laundry"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a LaundryTblController call this function before each test //**
   * setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupLaundryTbl() {
    TableController tbControl = null;
    tbControl = LaundryTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/LaundryForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Laundry> getRefList() { // **
    ArrayList<Laundry> refObjList = new ArrayList<Laundry>();
    // NEED ALL ENTRIES IN CSV
    Laundry L1 = new Laundry(111, "towels", "this is the first gift", true);
    Laundry L2 = new Laundry(222, "napkins", "this is the second gift", false);
    Laundry L3 = new Laundry(333, "pillows", "this is the third gift", true);
    Laundry L4 = new Laundry(444, "sheets", "this is the fourth gift", false);
    Laundry L5 = new Laundry(555, "comforter", "this is the fifth gift", true);
    Laundry L6 = new Laundry(666, "blankets", "this is the sixth gift", false);
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
    tbControl = setupLaundryTbl(); // **

    // create test Object
    Laundry testObj = new Laundry(777, "pillows", "this is the fourth gift", true); // **
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
    tbControl = setupLaundryTbl(); // **
    // create object from first object in csv and get that object with getEntry
    Laundry refObj = new Laundry(111, "towels", "this is the first gift", true); // **
    Laundry tbObj = (Laundry) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupLaundryTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Laundry> tbObjList = new ArrayList<Laundry>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/LaundryForTesting.csv"); // **

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
    tbControl = setupLaundryTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"laundryID", "laundryType", "description", "inStock"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupLaundryTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Laundry> refList = getRefList(); // **
    System.out.println(refList);
    ArrayList<Laundry> tbList = tbControl.readTable(); // **
    System.out.println(tbList);

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
    LaundryTbl L1 = LaundryTbl.getInstance(); // **
    LaundryTbl L2 = LaundryTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
