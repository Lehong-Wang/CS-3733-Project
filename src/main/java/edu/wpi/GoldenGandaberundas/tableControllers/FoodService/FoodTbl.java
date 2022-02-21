package edu.wpi.GoldenGandaberundas.tableControllers.FoodService;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class FoodTbl extends TableController<Food, Integer> {

  private static FoodTbl instance = null; // **

  private FoodTbl() throws SQLException { // **
    super(
        "Food",
        Arrays.asList(new String[] {"foodID", "description", "price", "inStock", "foodType"}));
    String[] cols = {"foodID", "description", "price", "inStock", "foodType"};
    createTable();
    objList = new ArrayList<Food>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static FoodTbl getInstance() { // **
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new FoodTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Food> readTable() { // **
    ArrayList tableInfo = new ArrayList<Food>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Food( // **
                r.getInt(1), r.getString(2), r.getDouble(3), r.getBoolean(4), r.getString(5)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(Food obj) {
    Food med = (Food) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?);");

      // **
      s.setInt(1, med.getFoodID());
      s.setString(2, med.getDescription());
      s.setDouble(3, med.getPrice());
      s.setBoolean(4, med.getInStock());
      s.setString(5, med.getFoodType());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Food> readBackup(String fileName) {
    ArrayList<Food> medList = new ArrayList<Food>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("foodID,description,price,inStock,foodType"))) { // **
        System.err.println("Food backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Food med = // **
            new Food(
                Integer.parseInt(element[0]),
                element[1],
                Double.parseDouble(element[2]),
                Boolean.parseBoolean(element[3]),
                element[4]); // **
        medList.add(med); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return medList; // **
  }

  @Override
  public boolean createTable() {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return false;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return false;
    }

    System.out.println("SQLite driver registered!");

    Statement s = null;
    try {
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Food("
              + "foodID INTEGER NOT NULL ,"
              + "description TEXT, "
              + "price DOUBLE NOT NULL, "
              + "inStock BOOLEAN NOT NULL, "
              + "foodType TEXT NOT NULL, "
              + "PRIMARY KEY ('foodID'));");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Food getEntry(Integer pkID) { // **
    Food med = new Food(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID); // **
        ResultSet r = s.executeQuery();
        r.next();
        med.setFoodID(r.getInt(1));
        med.setDescription(r.getString(2));
        med.setPrice(r.getDouble(3));
        med.setInStock(r.getBoolean(4));
        med.setFoodType(r.getString(5));
        return med;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return med; // **
  }
}
