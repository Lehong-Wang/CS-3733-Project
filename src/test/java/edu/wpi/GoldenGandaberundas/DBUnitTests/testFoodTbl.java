package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.Food;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodTbl;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testFoodTbl {

  String tbName = "Food"; // **
  String dbPath = new String("jdbc:sqlite:hospitalData.db");

  /**
   * initialize a tableController as a FoodTblController call this function before each test //**
   * setup TableController
   *
   * @return a single instance of TableController
   */
  public TableController setupFoodTbl() {
    TableController tbControl = null;
    tbControl = FoodTbl.getInstance(); // **
    tbControl.loadBackup("TestCSVs/FoodForTesting.csv"); // **
    return tbControl;
  }

  /**
   * create a reference Object List this List is what you will get after you read the test csv file
   *
   * @return the expected array of objects in objList
   */
  public ArrayList<Food> getRefList() { // **
    ArrayList<Food> refObjList = new ArrayList<Food>();
    // NEED ALL ENTRIES IN CSV
    Food L1 = new Food(111, "Burger", "Bread/Meat/Cheese", 500, "Dairy/Meat", 1.11, true, "Entree");
    Food L2 = new Food(222, "Coke", "Water/Coke", 200, "None", 2.22, false, "Drink");
    Food L3 = new Food(333, "Fries", "Potatoes/Grease", 250, "None", 3.33, true, "Side");
    Food L4 = new Food(444, "Sundae", "Chocolate/Ice cream", 340, "Dairy", 4.44, false, "Dessert");
    Food L5 = new Food(555, "Sandwich", "Bread/Meat/Lettuce", 600, "None", 5.55, true, "Entree");
    Food L6 = new Food(666, "Chips", "Potatoes", 210, "None", 6.66, false, "Side");
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
    tbControl = setupFoodTbl(); // **

    // create test Object
    Food testObj = new Food(777, "Apple", "Apple", 100, "None", 7.77, true, "Side"); // **
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
    tbControl = setupFoodTbl(); // **
    // create object from first object in csv and get that object with getEntry
    Food refObj =
        new Food(111, "Burger", "Bread/Meat/Cheese", 500, "Dairy/Meat", 1.11, true, "Entree"); // **
    Food tbObj = (Food) tbControl.getEntry(111); // **
    System.out.println(tbObj);
    // compare them
    Assertions.assertTrue(refObj.equals(tbObj));
  }

  /** test for readBackup() */
  @Test
  public void testReadBackup() {
    TableController tbControl;
    tbControl = setupFoodTbl(); // **

    // create a ArrayList of Object and compare it to what we get from csv
    ArrayList<Food> tbObjList = new ArrayList<Food>(); // **
    tbObjList = tbControl.readBackup("TestCSVs/FoodForTesting.csv"); // **

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
    tbControl = setupFoodTbl(); // **

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
      "foodID", "foodName", "ingredients", "calories", "allergens", "price", "inStock", "foodType"
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
    tbControl = setupFoodTbl(); // **

    // compare the expected objList with what we got with readTable()
    ArrayList<Food> refList = getRefList(); // **
    System.out.println(refList);
    ArrayList<Food> tbList = tbControl.readTable(); // **
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
    FoodTbl L1 = FoodTbl.getInstance(); // **
    FoodTbl L2 = FoodTbl.getInstance(); // **

    boolean isSame = L1.equals(L2);
    Assertions.assertTrue(isSame);
  }
}
