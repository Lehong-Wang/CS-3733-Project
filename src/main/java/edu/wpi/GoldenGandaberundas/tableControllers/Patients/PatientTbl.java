package edu.wpi.GoldenGandaberundas.tableControllers.Patients;

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

public class PatientTbl extends TableController<Patient, Integer> {

  private static PatientTbl instance = null;

  private PatientTbl() throws SQLException {
    super("Patients", Arrays.asList(new String[] {"patientID", "fName", "lName"}));
    String[] cols = {"patientID", "location", "fName", "lName"};
    createTable();
    objList = new ArrayList<Patient>();
    objList = readTable();
  }

  public static PatientTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new PatientTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Patient> readTable() {
    ArrayList patients = new ArrayList<Patient>();
    try { // if code works, do this:
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        patients.add(new Patient(r.getInt(1), r.getString(2), r.getString(3), r.getString(4)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    return patients;
  }

  @Override
  public boolean addEntry(Patient obj) {
    if (TableController.getConnectionType() == ConnectionType.clientServer) {
      return addEntryOnline(obj);
    }
    Patient pat = obj;
    PreparedStatement s = null;
    try {
      s = connection.prepareStatement("INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?);");
      s.setInt(1, pat.getPatientID());
      s.setString(2, pat.getLocation());
      s.setString(3, pat.getfName());
      s.setString(4, pat.getlName());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(Patient obj) {
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
      s.setInt(1, obj.getPatientID());
      s.setInt(2, obj.getPatientID());
      s.setString(3, obj.getLocation());
      s.setString(4, obj.getfName());
      s.setString(5, obj.getlName());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Patient> readBackup(String fileName) {
    ArrayList<Patient> patients = new ArrayList<>(); // creates a list for the location objects
    File csvFile = new File(fileName);
    try {
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);

      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("patientID,fName,lName"))) {
        System.err.println("patient backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Patient currentPat =
            new Patient(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                element[3]); // creates a Location
        patients.add(currentPat); // adds the location to the list
        currentLine = buffer.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return patients;
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
          "CREATE TABLE IF NOT EXISTS  Patients("
              + "patientID TEXT NOT NULL ,"
              + "location TEXT, "
              + "fName TEXT NOT NULL, "
              + "lName TEXT NOT NULL, "
              + "PRIMARY KEY ('patientID')"
              + "CONSTRAINT nodeID FOREIGN KEY (location) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE SET NULL"
              + ");");
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
          "CREATE TABLE Patients("
              + "patientID INTEGER NOT NULL ,"
              + "location VARCHAR(50) NOT NULL DEFAULT 'FDEPT00101', "
              + "fName TEXT NOT NULL, "
              + "lName TEXT NOT NULL, "
              + "PRIMARY KEY (patientID),"
              + "CONSTRAINT nodeIDFK FOREIGN KEY (location) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE "
              + ");");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public Patient getEntry(Integer pkID) {
    Patient pat = new Patient();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        pat.setPatientID(r.getInt(1));
        pat.setLocation(r.getString(2));
        pat.setfName(r.getString(3));
        pat.setlName(r.getString(4));
        return pat;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return pat;
  }
}
