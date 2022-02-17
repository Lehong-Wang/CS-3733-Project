package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.Medicine;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testMedicineTbl {

  String tbName = "Medicine"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a MedicineTblController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupMedicineTbl() {
    TableController tbControl = null;
    tbControl = MedicineTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/MedicineForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Medicine> getRefList() { // **
    ArrayList<Medicine> refObjList = new ArrayList<Medicine>();
    // NEED ALL ENTRIES IN CSV
    Medicine L1 = new Medicine(111, "tylenol", "this is the first gift", 1.11, true);
    Medicine L2 = new Medicine(222, "ibuprofen", "this is the second gift", 2.22, false);
    Medicine L3 = new Medicine(333, "nyquil", "this is the third gift", 3.33, true);
    Medicine L4 = new Medicine(444, "aspirin", "this is the fourth gift", 4.44, false);
    Medicine L5 = new Medicine(555, "zyrtec", "this is the fifth gift", 5.55, true);
    Medicine L6 = new Medicine(666, "acetaminophen", "this is the sixth gift", 6.66, false);
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
    tbControl = setupMedicineTbl(); // **

    // create test Object
    Medicine testObj = new Medicine(444, "sudafed", "this is the fourth gift", 4.44, false); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(testObj.getMedicineID()); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupMedicineTbl(); // **
    // create object from first object in csv and get that object with getEntry
    Medicine refObj = new Medicine(111, "tylenol", "this is the first gift", 1.11, true); // **
    Medicine tbObj = (Medicine) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupMedicineTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Medicine> tbObjList = new ArrayList<Medicine>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/MedicineForTesting.csv"); // **

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
    tbControl = setupMedicineTbl(); // **

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
    tbControl = setupMedicineTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Medicine> refList = getRefList(); // **
    ArrayList<Medicine> tbList = tbControl.readTable(); // **

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
    MedicineTbl L1 = MedicineTbl.getInstance(); // **
    MedicineTbl L2 = MedicineTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
