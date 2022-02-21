package edu.wpi.GoldenGandaberundas.tableControllers.LaundryService;

import edu.wpi.GoldenGandaberundas.ConnectionType;
import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class LaundryTbl extends TableController<Laundry, Integer> {

  private static LaundryTbl instance = null;

  private LaundryTbl() throws SQLException {
    super(
        "Laundry",
        Arrays.asList(new String[] {"laundryID", "laundryType", "description", "inStock"}));
    String[] cols = {"laundryID", "laundryType", "description", "inStock"};

    createTable();

    objList = new ArrayList<Laundry>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static LaundryTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new LaundryTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Laundry> readTable() {
    ArrayList tableInfo = new ArrayList<Laundry>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Laundry( // **
                r.getInt(1), r.getString(2), r.getString(3), r.getBoolean(4)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(Laundry obj) {
    if (TableController.getConnectionType() == ConnectionType.clientServer) {
      return addEntryOnline(obj);
    }
    Laundry laundry = (Laundry) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?,?,?,?);");

      // **
      s.setInt(1, laundry.getLaundryID());
      s.setString(2, laundry.getLaundryType());
      s.setString(3, laundry.getDescription());
      s.setBoolean(4, laundry.getInStock());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(Laundry laundry) {
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
                  + " VALUES (?, ?, ?, ?)"
                  + "end");

      s.setInt(1, laundry.getLaundryID());
      s.setInt(2, laundry.getLaundryID());
      s.setString(3, laundry.getLaundryType());
      s.setString(4, laundry.getDescription());
      s.setBoolean(5, laundry.getInStock());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public ArrayList<Laundry> readBackup(String fileName) {
    ArrayList<Laundry> laundryList = new ArrayList<Laundry>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("laundryID,laundryType,description,inStock"))) { // **
        System.err.println("laundry format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Laundry laun = // **
            new Laundry(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                Boolean.parseBoolean(element[3])); // **
        laundryList.add(laun); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return laundryList; // **
  }

  @Override
  public boolean createTable() {
    if (TableController.getConnectionType() == ConnectionType.clientServer) {
      return createTableOnline();
    }
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
          "CREATE TABLE IF NOT EXISTS  Laundry(laundryID INTEGER NOT NULL ,laundryType TEXT, description TEXT, inStock BOOLEAN, PRIMARY KEY ('laundryID'));");
      this.writeTable();
      return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean createTableOnline() {
    try {

      PreparedStatement s1 =
          connection.prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return false;
      }
      Statement s = connection.createStatement();
      s.execute(
          "CREATE TABLE  Laundry(laundryID INTEGER NOT NULL ,laundryType TEXT, description TEXT, inStock BIT, PRIMARY KEY (laundryID));");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Laundry getEntry(Integer pkID) {
    Laundry laundry = new Laundry(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next(); // **
        laundry.setLaundryID(r.getInt(1));
        laundry.setLaundryType(r.getString(2));
        laundry.setDescription(r.getString(3));
        laundry.setInStock(r.getBoolean(4));
        return laundry; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return laundry; // **
  }
}
