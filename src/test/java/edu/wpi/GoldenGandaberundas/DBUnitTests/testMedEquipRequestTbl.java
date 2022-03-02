package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipRequest;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testMedEquipRequestTbl {

  String tbName = "MedEquipRequests";
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a MedEquipTblRequestTblController call this function before
   * each test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupMedEquipRequestTbl() {
    TableController tbControl = null;
    tbControl = MedEquipRequestTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/MedEquipRequestsForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<MedEquipRequest> getRefList() {
    ArrayList<MedEquipRequest> refObjList = new ArrayList<>();
    MedEquipRequest R1 =
        new MedEquipRequest(
            111, "FDEPT00101", 123, 123, 604800, 864000, 123, "complete", "note", 607);
    MedEquipRequest R2 =
        new MedEquipRequest(
            222, "FDEPT00101", 456, 436, 604800, 864000, 123, "incomplete", "notes", 436);
    MedEquipRequest R3 =
        new MedEquipRequest(
            333, "FDEPT00201", 456, 123, 604800, 864000, 123, "in progress", "this is a note", 789);
    MedEquipRequest R4 =
        new MedEquipRequest(
            444, "FDEPT00201", 123, 123, 604800, 864000, 123, "complete", "deliver", 34);
    MedEquipRequest R5 =
        new MedEquipRequest(
            555, "FDEPT00301", 123, 456, 604800, 864000, 123, "incomplete", "please note", 123);
    MedEquipRequest R6 =
        new MedEquipRequest(
            666, "FDEPT00301", 456, 456, 604800, 864000, 123, "in progress", "please", 402);
    /*    refObjList.add(R1);
    refObjList.add(R2);
    refObjList.add(R3);
    refObjList.add(R4);
    refObjList.add(R5);
    refObjList.add(R6);*/

    MedEquipRequest m1 =
        new MedEquipRequest(120, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111);
    MedEquipRequest m2 =
        new MedEquipRequest(121, "FDEPT00101", 123, 456, 0, 100, 222, "In_Progress", "", 222);
    refObjList.add(m1);
    refObjList.add(m2);
    //    RequestTable.getInstance().addEntry(R1);
    //    RequestTable.getInstance().addEntry(R2);
    //    RequestTable.getInstance().addEntry(R3);
    //    RequestTable.getInstance().addEntry(R4);
    //    RequestTable.getInstance().addEntry(R5);
    //    RequestTable.getInstance().addEntry(R6);
    RequestTable.getInstance().addEntry(m1);
    RequestTable.getInstance().addEntry(m2);

    return refObjList;
  }
  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupMedEquipRequestTbl(); // **

    // create test Object //**
    MedEquipRequest testObj =
        new MedEquipRequest(122, "FDEPT00101", 123, 456, 0, 100, 333, "In_Progress", "", 333); // **
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
    tbControl = setupMedEquipRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    MedEquipRequest refObj =
        new MedEquipRequest(120, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(120); // **
    test.add(111); // **
    MedEquipRequest tbObj = (MedEquipRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupMedicineRequestsTbl(); // **
    tbControl = MedEquipRequestTbl.getInstance(); // **
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<MedEquipRequest> tbObjList = new ArrayList<>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/MedEquipRequestsForTesting.csv"); // **

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
    tbControl = setupMedEquipRequestTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "medEquipID"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupMedEquipRequestTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<MedEquipRequest> refList = getRefList(); // **
    ArrayList<MedEquipRequest> tbList = tbControl.readTable(); // **

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
    MedEquipRequestTbl L1 = MedEquipRequestTbl.getInstance(); // **
    MedEquipRequestTbl L2 = MedEquipRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
