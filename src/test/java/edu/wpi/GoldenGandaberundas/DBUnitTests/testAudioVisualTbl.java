package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.AudioVisualService.AudioVisual;
import edu.wpi.CS3733.c22.teamG.tableControllers.AudioVisualService.AudioVisualTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testAudioVisualTbl {

  String tbName = "AudioVisual"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a AudioVisualTblController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupAudioVisualTbl() {
    TableController tbControl = null;
    tbControl = AudioVisualTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/AudioVisualForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<AudioVisual> getRefList() { // **
    ArrayList<AudioVisual> refObjList = new ArrayList<AudioVisual>();
    // NEED ALL ENTRIES IN CSV
    AudioVisual L1 = new AudioVisual(111, "TV", "FDEPT00101", "device 1");
    AudioVisual L2 = new AudioVisual(222, "TV", "FDEPT00201", "device 2");
    AudioVisual L3 = new AudioVisual(333, "Head phone", "FDEPT00301", "device 3");
    AudioVisual L4 = new AudioVisual(444, "Head phone", "FDEPT00101", "device 4");
    AudioVisual L5 = new AudioVisual(555, "Ipad", "FDEPT00201", "device 5");
    AudioVisual L6 = new AudioVisual(666, "Ipad", "FDEPT00301", "device 6");
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
    tbControl = setupAudioVisualTbl(); // **

    // create test Object
    AudioVisual testObj = new AudioVisual(777, "Ipad", "FDEPT00301", "device 7"); // **
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
    tbControl = setupAudioVisualTbl(); // **
    // create object from first object in csv and get that object with getEntry
    AudioVisual refObj = new AudioVisual(111, "TV", "FDEPT00101", "device 1"); // **
    AudioVisual tbObj = (AudioVisual) tbControl.getEntry(111); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupAudioVisualTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<AudioVisual> tbObjList = new ArrayList<AudioVisual>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/AudioVisualForTesting.csv"); // **

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
    tbControl = setupAudioVisualTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"avID", "deviceType", "locID", "description"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupAudioVisualTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<AudioVisual> refList = getRefList(); // **
    System.out.println(refList);
    ArrayList<AudioVisual> tbList = tbControl.readTable(); // **
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
    AudioVisualTbl L1 = AudioVisualTbl.getInstance(); // **
    AudioVisualTbl L2 = AudioVisualTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
