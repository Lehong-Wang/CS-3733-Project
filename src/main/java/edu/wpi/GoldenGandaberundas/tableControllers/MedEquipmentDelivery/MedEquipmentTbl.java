package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MedEquipmentTbl extends TableController<MedEquipment, Integer> {

  private static MedEquipmentTbl instance = null;

  private MedEquipmentTbl() throws SQLException {
    super(
        "MedEquipment",
        Arrays.asList(new String[] {"medID", "medEquipmentType", "status", "currLoc"}),
        "reqID,medID");
    createTable();

    objList = new ArrayList<MedEquipment>();
    objList = readTable();
  }

  public static MedEquipmentTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedEquipmentTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return (MedEquipmentTbl) instance;
  }

  @Override
  public ArrayList readTable() {
    ArrayList medEquipments = new ArrayList<MedEquipment>();
    try { // if code works, do this:
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      // s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      while (r.next()) {
        medEquipments.add(
            new MedEquipment(r.getInt(1), r.getString(2), r.getString(3), r.getString(4)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    return medEquipments;
  }

  @Override
  public boolean deleteEntry(Integer pkid) {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = " + (int) pkid + ";");
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean addEntry(MedEquipment obj) {
    if (!this.getEmbedded()) {
      return addEntryOnline(obj);
    }
    MedEquipment med = (MedEquipment) obj;
    PreparedStatement s = null;

    try {
      s = connection.prepareStatement("INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ? ,?);");

      s.setInt(1, med.getMedID());
      s.setString(2, med.getMedEquipmentType());
      s.setString(3, med.getStatus());
      s.setString(4, med.getCurrLoc());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(MedEquipment med) {
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
      s.setInt(1, med.getMedID());
      s.setInt(2, med.getMedID());
      s.setString(3, med.getMedEquipmentType());
      s.setString(4, med.getStatus());
      s.setString(5, med.getCurrLoc().trim().toUpperCase(Locale.ROOT));
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(med.getCurrLoc());
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<MedEquipment> readBackup(String fileName) {
    ArrayList<MedEquipment> med = new ArrayList<>(); // creates a list for the location objects
    File csvFile = new File(fileName);
    try {
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        MedEquipment currentMed = this.createMedEquipment(element); // creates a Location
        med.add(currentMed); // adds the location to the list
        currentLine = buffer.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return med;
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
          "CREATE TABLE IF NOT EXISTS  MedEquipment("
              + "medID TEXT NOT NULL, "
              + "medEquipmentType TEXT NOT NULL, "
              + "status TEXT NOT NULL, "
              + "currLoc TEXT NOT NULL, "
              + "PRIMARY KEY ('medID'),"
              + "CONSTRAINT nodeID FOREIGN KEY (currLoc) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + ");");

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
          "CREATE TABLE  MedEquipment("
              + "medID INTEGER NOT NULL, "
              + "medEquipmentType TEXT NOT NULL, "
              + "status TEXT NOT NULL, "
              + "currLoc varchar(50) NOT NULL, "
              + "PRIMARY KEY (medID),"
              + "CONSTRAINT nodeID FOREIGN KEY (currLoc) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + ");");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public MedEquipment getEntry(Integer pkid) {
    MedEquipment med = new MedEquipment();
    try {
      if (this.entryExists(pkid)) {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " = " + pkid + ";");
        ResultSet r = s.executeQuery();
        r.next();
        med.setMedID(pkid);
        med.setMedEquipmentType(r.getString(2));
        med.setStatus(r.getString(3));
        med.setCurrLoc(r.getString(4));
        return med;
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  public MedEquipment createMedEquipment(String[] ele) {
    MedEquipment med = new MedEquipment(Integer.parseInt(ele[0]), ele[1], ele[2], ele[3]);
    return med;
  }
}
