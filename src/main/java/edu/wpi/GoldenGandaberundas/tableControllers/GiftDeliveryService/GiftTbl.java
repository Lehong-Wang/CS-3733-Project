package edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

// first parameter is object giftType, second is pkid giftType
// change the giftTypes accordingly in the methods
public class GiftTbl extends TableController<Gift, String> {

  private static GiftTbl instance = null;

  private GiftTbl() throws SQLException {
    super(
        "Gifts",
        Arrays.asList(new String[] {"giftID", "giftType", "description", "price", "inStock"}));
    String[] cols = {"giftID", "giftType", "description", "price", "inStock"};
    createTable();

    objList = new ArrayList<Gift>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static GiftTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new GiftTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Gift> readTable() {

    ArrayList<Gift> tableInfo = new ArrayList<>();
    try {
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();

      while (r.next()) {
        tableInfo.add(
            new Gift(r.getInt(1), r.getString(2), r.getString(3), r.getDouble(4), r.getBoolean(5)));
      }
      return tableInfo;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean addEntry(Gift obj) {
    if (!this.getEmbedded()) {
      return addEntryOnline(obj);
    }
    Gift gift = (Gift) obj;
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement(
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?);");

      s.setInt(1, gift.getGiftID());
      s.setString(2, gift.getGiftType());
      s.setString(3, gift.getDescription());
      s.setDouble(4, gift.getPrice());
      s.setBoolean(5, gift.getInStock());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(Gift gift) {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              " IF NOT EXISTS (SELECT 1 FROM "
                  + tbName
                  + " WHERE "
                  + colNames.get(0)
                  + " = ?)"
                  + "BEGIN"
                  + "    INSERT INTO "
                  + tbName
                  + " VALUES (?, ?, ?, ?, ?)"
                  + "end");

      s.setInt(1, gift.getGiftID());
      s.setInt(2, gift.getGiftID());
      s.setString(3, gift.getGiftType());
      s.setString(4, gift.getDescription());
      s.setDouble(5, gift.getPrice());
      s.setBoolean(6, gift.getInStock());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public ArrayList<Gift> readBackup(String fileName) {
    ArrayList<Gift> gifts = new ArrayList<>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file

      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("giftID,description,price,inStock"))) {
        System.err.println("gift backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Gift gift =
            new Gift(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                Double.parseDouble(element[3]),
                Boolean.parseBoolean(element[4]));
        gifts.add(gift); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return gifts;
  }

  @Override
  public void createTable() {
    if (!this.getEmbedded()) {
      createTableOnline();
      return;
    }
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return;
    }

    try {
      Class.forName("org.sqlite.JDBC");
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return;
    }

    System.out.println("SQLite driver registered!");

    Statement s = null;
    try {
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Gifts("
              + "giftID INTEGER NOT NULL ,"
              + "giftType TEXT NOT NULL, "
              + "description TEXT, "
              + "price DOUBLE NOT NULL, "
              + "inStock BOOLEAN NOT NULL, "
              + "PRIMARY KEY ('giftID'));");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  private void createTableOnline() {
    try {

      PreparedStatement s1 =
          connection.prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
      Statement s = connection.createStatement();
      s.execute(
          "CREATE TABLE  Gifts("
              + "giftID INTEGER NOT NULL ,"
              + "giftType TEXT NOT NULL, "
              + "description TEXT, "
              + "price FLOAT NOT NULL, "
              + "inStock BIT NOT NULL, "
              + "PRIMARY KEY (giftID));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Gift getEntry(String pkID) {
    Gift gift = new Gift();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setString(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        gift.setGiftID(r.getInt(1));
        gift.setGiftType(r.getString(2));
        gift.setDescription(r.getString(3));
        gift.setPrice(r.getDouble(4));
        gift.setInStock(r.getBoolean(5));
        return gift;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return gift;
  }
}
