package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Employee;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.EmployeeTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testEmployeeTbl {

  String tbName = "Employees"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a EmployeesTblController call this function before each test
   * //** setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupEmployeesTbl() {
    TableController tbControl = null;
    tbControl = EmployeeTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/EmployeeForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Employee> getRefList() { // **
    ArrayList<Employee> refObjList = new ArrayList<Employee>();
    // NEED ALL ENTRIES IN CSV
    Employee L1 =
        new Employee(
            123,
            "Paul",
            "Godinez",
            "network specialist",
            "pgodinez@wpi.edu",
            "456852456",
            "123 main street");
    Employee L2 =
        new Employee(456, "Will", "BC", "ENT", "wbc@wpi.edu", "852369741", "456 worcester street");
    Employee L3 =
        new Employee(
            666, "W", "Wong", "professor", "wwong2@wpi.edu", "5088315000", "100 institute Road");
    Employee L4 =
        new Employee(
            777, "Lehong", "Wang", "database", "lwang11@wpi.edu", "1234567890", "1 main street");
    Employee L5 =
        new Employee(888, "Ben", "Bob", "sergain", "123@wpi.edu", "987654321", "10 main street");
    Employee L6 =
        new Employee(999, "Alice", "Mary", "CEO", "000@wpi.edu", "9999999999", "999 main street");
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
    tbControl = setupEmployeesTbl(); // **

    // create test Object
    Employee testObj =
        new Employee(
            777,
            "Lehong",
            "Wang",
            "database",
            "lwang11@wpi.edu",
            "1234567890",
            "1 main street"); // **
    // add Obj to DB
    tbControl.addEntry(testObj);

    boolean objExist = false;
    objExist = tbControl.entryExists(testObj.getEmpID()); // **
    Assertions.assertTrue(objExist);
  }

  /** test for getEntry() */
  @Test
  public void testGetEntry() {
    TableController tbControl;
    tbControl = setupEmployeesTbl(); // **
    // create object from first object in csv and get that object with getEntry
    Employee refObj =
        new Employee(
            123,
            "Paul",
            "Godinez",
            "network specialist",
            "pgodinez@wpi.edu",
            "456852456",
            "123 main street"); // **
    Employee tbObj = (Employee) tbControl.getEntry(123); // **
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupEmployeesTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Employee> tbObjList = new ArrayList<Employee>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/EmployeeForTesting.csv"); // **

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
    tbControl = setupEmployeesTbl(); // **

    PreparedStatement s = testConnection.prepareStatement("PRAGMA table_info(" + tbName + ");");
    ResultSet r2 = s.executeQuery();
    List<String> testColNameList = new ArrayList<String>();
    // the column name is output in the second column of each row.
    while (r2.next()) {
      testColNameList.add(r2.getString(2));
      System.out.println(r2.getString(2));
    }

    // check the columns    //**
    String[] cols = {"empID", "fName", "lName", "role", "email", "phoneNumber", "address"};
    ArrayList<String> refColNames = new ArrayList(Arrays.asList(cols));

    boolean isSame = refColNames.equals(testColNameList);
    testConnection.close();
    Assertions.assertTrue(isSame);
  }

  /** test for readTable() */
  @Test
  public void testReadTable() {
    TableController tbControl;
    tbControl = setupEmployeesTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Employee> refList = getRefList(); // **
    ArrayList<Employee> tbList = tbControl.readTable(); // **

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
    EmployeeTbl L1 = EmployeeTbl.getInstance(); // **
    EmployeeTbl L2 = EmployeeTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
