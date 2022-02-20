package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class CredentialsTbl extends TableController<Credential, Integer> {
  /** single instance of class allowed */
  private static CredentialsTbl instance = null;

  private CredentialsTbl() throws SQLException {
    super("Credentials", Arrays.asList(new String[] {"empID", "password", "salt"}));
    tbName = "Credentials";
    colNames = Arrays.asList(new String[] {"empID", "password", "salt"});
    createTable();

    objList = readTable();
  }

  /**
   * double lock lazy initialization of singleton design should be thread safe
   *
   * @return
   */
  public static CredentialsTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new CredentialsTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Credential> readTable() {
    ArrayList tableInfo = new ArrayList<Credential>();
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new Credential(r.getInt(1), r.getString(2), r.getString(3)));
      }
      return tableInfo;
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean addEntry(Credential obj) {
    if (!entryExists(obj.getEmpID())) {
      try {
        PreparedStatement statement =
            connection.prepareStatement("INSERT INTO " + tbName + " VALUES (?, ?, ?);");
        statement.setInt(1, obj.getEmpID());
        statement.setString(2, obj.getHashedPass());
        statement.setString(3, obj.getSalt());
        statement.executeUpdate();
        return true;
      } catch (SQLException throwables) {
        throwables.printStackTrace();
      }
    }
    return false;
  }

  @Override
  public ArrayList<Credential> readBackup(String fileName) {
    ArrayList<Credential> credList = new ArrayList<>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine.trim().equals(new String("empID,password,salt"))) {
        System.err.println("credential backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Credential cred = new Credential(Integer.parseInt(element[0]), element[1], element[2]);
        credList.add(cred); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return credList;
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

    try {
      PreparedStatement FKStatement = connection.prepareStatement("PRAGMA foreign_keys = TRUE");
      FKStatement.execute();
      PreparedStatement statement =
          connection.prepareStatement(
              "CREATE TABLE IF NOT EXISTS "
                  + tbName
                  + "(empID INTEGER NOT NULL, "
                  + "password TEXT NOT NULL, "
                  + "salt TEXT NOT NULL, "
                  + "PRIMARY KEY('empID'), "
                  + "CONSTRAINT CredFK FOREIGN KEY ('empID') REFERENCES Employees('empID')"
                  + "ON UPDATE CASCADE "
                  + "ON DELETE CASCADE"
                  + "); ");
      statement.executeUpdate();
      this.writeTable();
      return true;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      return false;
    }
  }

  /**
   * returns specific entry from the table to be used by UI when loading the whole table into memory
   * is unnecessary
   *
   * @param pkID
   * @return credential from matching email or null if it does not exist
   */
  @Override
  public Credential getEntry(Integer pkID) {
    Integer empID;
    String hashedPass;
    String salt;
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        empID = (r.getInt(1));
        hashedPass = (r.getString(2));
        salt = (r.getString(3));

        return new Credential(empID, hashedPass, salt);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}
