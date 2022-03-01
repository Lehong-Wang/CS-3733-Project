package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testMedicineRequestTbl {

  String tbName = "MedicineRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a MedicineRequestTblController call this function before each
   * test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupMedicineRequestsTbl() {
    TableController tbControl = null;
    tbControl = MedicineRequestTbl.getInstance(); // **
    tbControl.loadBackup("backups/MedicineRequests.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<MedicineRequest> getRefList() { // **
    ArrayList<MedicineRequest> refObjList = new ArrayList<MedicineRequest>();
    // NEED ALL ENTRIES IN CSV
    MedicineRequest L1 =
        new MedicineRequest(
            111, "FDEPT00101", 123, 456, 604800, 864000, 123, "Submitted", "needed", 111, 500, 1);
    MedicineRequest L2 =
        new MedicineRequest(
            222, "FDEPT00101", 123, 123, 604800, 864000, 123, "Completed", "please", 222, 10, 2);
    MedicineRequest L3 =
        new MedicineRequest(
            333, "FDEPT00201", 456, 456, 604800, 864000, 123, "In_Progress", "thanks", 333, 50, 3);
    MedicineRequest L4 =
        new MedicineRequest(
            444, "FDEPT00201", 456, 456, 604800, 864000, 123, "Submitted", "quick", 444, 75, 4);
    MedicineRequest L5 =
        new MedicineRequest(
            555, "FDEPT00301", 456, 123, 604800, 864000, 123, "Completed", "urgent", 555, 20, 5);
    MedicineRequest L6 =
        new MedicineRequest(
            666, "FDEPT00301", 456, 123, 604800, 864000, 123, "In_Progress", "fast", 666, 30, 6);
    //    refObjList.add(L1);
    //    refObjList.add(L2);
    //    refObjList.add(L3);
    //    refObjList.add(L4);
    //    refObjList.add(L5);
    //    refObjList.add(L6);
    MedicineRequest m1 =
        new MedicineRequest(146, "FDEPT00101", 123, 456, 0, 123, 111, "Submitted", "", 111, 5, 500);
    MedicineRequest m2 =
        new MedicineRequest(145, "FDEPT00101", 123, 456, 0, 100, 222, "Submitted", "", 222, 5, 500);
    refObjList.add(m1);
    refObjList.add(m2);
    //    RequestTable.getInstance().addEntry(L1);
    //    RequestTable.getInstance().addEntry(L2);
    //    RequestTable.getInstance().addEntry(L3);
    //    RequestTable.getInstance().addEntry(L4);
    //    RequestTable.getInstance().addEntry(L5);
    //    RequestTable.getInstance().addEntry(L6);
    RequestTable.getInstance().addEntry(m1);
    RequestTable.getInstance().addEntry(m2);

    return refObjList;
  }

  /** test for addEntry() */
  @Test
  public void testAddEntry() {
    TableController tbControl;
    tbControl = setupMedicineRequestsTbl(); // **

    // create test Object
    MedicineRequest testObj =
        new MedicineRequest(
            777,
            "FDEPT00301",
            123,
            123,
            604800,
            864000,
            444,
            "In_Progress",
            "please",
            222,
            50,
            7); // **
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
    tbControl = setupMedicineRequestsTbl(); // **
    // create object from first object in csv and get that object with getEntry
    MedicineRequest refObj =
        new MedicineRequest(
            420,
            "GPATI00101",
            123,
            456,
            604800,
            86400,
            123,
            "Submitted",
            "this is a note",
            111,
            111,
            111); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(420);
    test.add(111);

    MedicineRequest tbObj = (MedicineRequest) tbControl.getEntry(test); // **
    System.out.println("tb " + tbObj);
    System.out.println("ref " + refObj);
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupMedicineRequestsTbl(); // **
    tbControl = MedicineRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<MedicineRequest> tbObjList = new ArrayList<MedicineRequest>(); // **
    tbObjList = tbControl.readBackup("backups/medicineRequests.csv"); // **

    System.out.println("tb " + tbObjList);
    System.out.println("refL " + getRefList());
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
    tbControl = setupMedicineRequestsTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "medicineID", "dosage", "quantity"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupMedicineRequestsTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<MedicineRequest> refList = getRefList(); // **
    ArrayList<MedicineRequest> tbList = tbControl.readTable(); // **

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
    MedicineRequestTbl L1 = MedicineRequestTbl.getInstance(); // **
    MedicineRequestTbl L2 = MedicineRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
