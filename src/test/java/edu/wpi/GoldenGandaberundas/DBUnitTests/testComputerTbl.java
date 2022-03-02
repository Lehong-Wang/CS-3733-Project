package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.Computer;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.ComputerTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testComputerTbl {

  String tbName = "Computer"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a ComputerTblController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupComputerTbl() {
    TableController tbControl = null;
    tbControl = ComputerTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/ComputerForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Computer> getRefList() { // **
    ArrayList<Computer> refObjList = new ArrayList<Computer>();
    // NEED ALL ENTRIES IN CSV
    Computer L1 =
        new Computer(111, "type1", "os1", "processor1", "host1", "model1", "manufacturer1", "1");
    Computer L2 =
        new Computer(222, "type2", "os2", "processor2", "host2", "model2", "manufacturer2", "2");
    Computer L3 =
        new Computer(333, "type3", "os3", "processor3", "host3", "model3", "manufacturer3", "3");
    Computer L4 =
        new Computer(444, "type4", "os4", "processor4", "host4", "model4", "manufacturer4", "4");
    Computer L5 =
        new Computer(555, "type5", "os5", "processor5", "host5", "model5", "manufacturer5", "5");
    Computer L6 =
        new Computer(666, "type6", "os6", "processor6", "host6", "model6", "manufacturer6", "6");
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
    tbControl = setupComputerTbl(); // **

    // create test Object
    Computer testObj =
        new Computer(
            777, "type7", "os7", "processor7", "host7", "model7", "manufacturer7", "7"); // **
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
    tbControl = setupComputerTbl(); // **
    // create object from first object in csv and get that object with getEntry
    Computer refObj =
        new Computer(
            111, "type1", "os1", "processor1", "host1", "model1", "manufacturer1", "1"); // **
    Computer tbObj = (Computer) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupComputerTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Computer> tbObjList = new ArrayList<Computer>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/ComputerForTesting.csv"); // **

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
    tbControl = setupComputerTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {
      "computerID",
      "computerType",
      "os",
      "processor",
      "hostName",
      "model",
      "manufacturer",
      "serialNumber"
    };
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupComputerTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Computer> refList = getRefList(); // **
    System.out.println(refList);
    ArrayList<Computer> tbList = tbControl.readTable(); // **
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
    ComputerTbl L1 = ComputerTbl.getInstance(); // **
    ComputerTbl L2 = ComputerTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
