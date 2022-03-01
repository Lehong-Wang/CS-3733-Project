package edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class LabServiceTbl extends TableController<LabService, Integer> {

  private static LabServiceTbl instance = null; // **

  private LabServiceTbl() throws SQLException { // **
    super("LabService", Arrays.asList(new String[] {"labID", "labType", "description"}));
    String[] cols = {"labID", "labType", "description"};
    createTable();
    objList = new ArrayList<LabService>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static LabServiceTbl getInstance() { // **
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new LabServiceTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<LabService> readTable() { // **
    ArrayList tableInfo = new ArrayList<LabService>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new LabService( // **
                r.getInt(1), r.getString(2), r.getString(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(LabService obj) {
    LabService med = (LabService) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

      // **
      s.setInt(1, med.getLabID());
      s.setString(2, med.getLabType());
      s.setString(3, med.getDescription());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<LabService> readBackup(String fileName) {
    ArrayList<LabService> medList = new ArrayList<LabService>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("labID,labType,description"))) { // **
        System.err.println("Lab Service tbl backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        LabService med = // **
            new LabService(Integer.parseInt(element[0]), element[1], element[2]); // **
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
  public void createTable() {
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
          "CREATE TABLE IF NOT EXISTS LabService("
              + "labID INTEGER NOT NULL ,"
              + "labType TEXT NOT NULL, "
              + "description TEXT, "
              + "PRIMARY KEY ('labID')"
              + ");");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public LabService getEntry(Integer pkID) { // **
    LabService med = new LabService(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID); // **
        ResultSet r = s.executeQuery();
        r.next();
        med.setLabID(r.getInt(1));
        med.setLabType(r.getString(2));
        med.setDescription(r.getString(3));
        return med;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return med; // **
  }
}
