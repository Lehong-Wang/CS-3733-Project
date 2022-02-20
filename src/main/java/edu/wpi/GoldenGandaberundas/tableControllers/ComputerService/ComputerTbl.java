package edu.wpi.GoldenGandaberundas.tableControllers.ComputerService;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ComputerTbl extends TableController<Computer, Integer> {

  private static ComputerTbl instance = null; // **

  private ComputerTbl() throws SQLException { // **
    super(
        "Computer",
        Arrays.asList(
            new String[] {
              "computerID",
              "computerType",
              "os",
              "processor",
              "hostName",
              "model",
              "manufacturer",
              "serialNumber"
            }));
    String[] cols = {
      "computerID",
      "computerType",
      "os",
      "processor",
      "hostName",
      "model",
      "manufacturer",
      "serialNumber"
    };
    createTable();
    objList = new ArrayList<Computer>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static ComputerTbl getInstance() { // **
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new ComputerTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Computer> readTable() { // **
    ArrayList tableInfo = new ArrayList<Computer>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Computer( // **
                r.getInt(1),
                r.getString(2),
                r.getString(3),
                r.getString(4),
                r.getString(5),
                r.getString(6),
                r.getString(7),
                r.getString(8)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(Computer obj) {
    Computer med = (Computer) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

      // **
      s.setInt(1, med.getComputerID());
      s.setString(2, med.getComputerType());
      s.setString(3, med.getOs());
      s.setString(4, med.getProcessor());
      s.setString(5, med.getHostName());
      s.setString(6, med.getModel());
      s.setString(7, med.getManufacturer());
      s.setString(8, med.getSerialNumber());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Computer> readBackup(String fileName) {
    ArrayList<Computer> medList = new ArrayList<Computer>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(
              new String(
                  "computerID,computerType,os,processor,hostName,model,manufacturer,serialNumber"))) { // **
        System.err.println("Computer backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Computer med = // **
            new Computer(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                element[3],
                element[4],
                element[5],
                element[6],
                element[7]); // **
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
          "CREATE TABLE IF NOT EXISTS  Computer("
              + "computerID INTEGER NOT NULL ,"
              + "computerType TEXT NOT NULL , "
              + "os TEXT NOT NULL , "
              + "processor TEXT NOT NULL , "
              + "hostName TEXT NOT NULL , "
              + "model TEXT NOT NULL , "
              + "manufacturer TEXT NOT NULL , "
              + "serialNumber TEXT NOT NULL , "
              + "PRIMARY KEY ('computerID'));");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Computer getEntry(Integer pkID) { // **
    Computer med = new Computer(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID); // **
        ResultSet r = s.executeQuery();
        r.next();
        med.setComputerID(r.getInt(1));
        med.setComputerType(r.getString(2));
        med.setOs(r.getString(3));
        med.setProcessor(r.getString(4));
        med.setHostName(r.getString(5));
        med.setModel(r.getString(6));
        med.setManufacturer(r.getString(7));
        med.setSerialNumber(r.getString(8));
        return med;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return med; // **
  }
}
