package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testAudioVisualRequestTbl {

  String tbName = "AudioVisualRequests"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a AudioVisualRequestTblController call this function before
   * each test //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupAudioVisualRequestTbl() {
    TableController tbControl = null;
    tbControl = AudioVisualRequestTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/AudioVisualRequestForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<AudioVisualRequest> getRefList() { // **
    ArrayList<AudioVisualRequest> refObjList = new ArrayList<AudioVisualRequest>();
    // NEED ALL ENTRIES IN CSV
    AudioVisualRequest m1 =
        new AudioVisualRequest(
            50, "FDEPT00101", 123, 999, 0, 864000, 111, "Submitted", "note1", 111, "high");
    AudioVisualRequest m2 =
        new AudioVisualRequest(
            51, "FDEPT00201", 456, 888, 0, 864000, 222, "Completed", "note2", 222, "low");
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
    tbControl = setupAudioVisualRequestTbl(); // **

    // create test Object
    AudioVisualRequest testObj =
        new AudioVisualRequest(
            52, "FDEPT00201", 456, 888, 0, 864000, 222, "Completed", "note2", 222, "low"); // **
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
    tbControl = setupAudioVisualRequestTbl(); // **
    // create object from first object in csv and get that object with getEntry
    AudioVisualRequest refObj =
        new AudioVisualRequest(
            50, "FDEPT00101", 123, 999, 0, 864000, 111, "Submitted", "note1", 111, "high"); // **
    ArrayList<Integer> test = new ArrayList<>();
    test.add(50);
    test.add(111);
    AudioVisualRequest tbObj = (AudioVisualRequest) tbControl.getEntry(test); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    // tbControl = setupMedicineRequestsTbl(); // **
    tbControl = AudioVisualRequestTbl.getInstance();
    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<AudioVisualRequest> tbObjList = new ArrayList<AudioVisualRequest>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/AudioVisualRequestForTesting.csv"); // **

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
    tbControl = setupAudioVisualRequestTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"reqID", "audioVisualID", "priority"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupAudioVisualRequestTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<AudioVisualRequest> refList = getRefList(); // **
    ArrayList<AudioVisualRequest> tbList = tbControl.readTable(); // **

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
    AudioVisualRequestTbl L1 = AudioVisualRequestTbl.getInstance(); // **
    AudioVisualRequestTbl L2 = AudioVisualRequestTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
