package edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService;

import edu.wpi.GoldenGandaberundas.TableController;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MedicineTbl extends TableController<Medicine, String> {

  private static MedicineTbl instance = null;

  private MedicineTbl() throws SQLException {
    super(
        "Medicine",
        Arrays.asList(new String[] {"medicineID", "medName", "description", "price", "inStock"}));
    String[] cols = {"medicineID", "medName", "description", "price", "inStock"};
    createTable();
    objList = new ArrayList<Medicine>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static MedicineTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedicineTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Medicine> readTable() {
    ArrayList tableInfo = new ArrayList<Medicine>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Medicine( // **
                r.getInt(1), r.getString(2), r.getString(3), r.getDouble(4), r.getBoolean(5)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(Medicine obj) {
    if (!this.getEmbedded()) {
      return addEntryOnline(obj);
    }
    Medicine med = (Medicine) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?,?, ?, ?);");

      // **
      s.setInt(1, med.getMedicineID());
      s.setString(2, med.getMedName());
      s.setString(3, med.getDescription());
      s.setDouble(4, med.getPrice());
      s.setBoolean(5, med.getInStock());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(Medicine med) {
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
      s.setInt(1, med.getMedicineID());
      s.setInt(2, med.getMedicineID());
      s.setString(3, med.getMedName());
      s.setString(4, med.getDescription());
      s.setDouble(5, med.getPrice());
      s.setBoolean(6, med.getInStock());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Medicine> readBackup(String fileName) {
    ArrayList<Medicine> medList = new ArrayList<Medicine>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("medicineID,medName, description,price,inStock"))) { // **
        System.err.println("medicine backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Medicine med = // **
            new Medicine(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                Double.parseDouble(element[2]),
                Boolean.parseBoolean(element[3])); // **
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
          "CREATE TABLE IF NOT EXISTS  Medicine(medicineID TEXT NOT NULL ,medName TEXT, description TEXT, price DOUBLE, inStock BOOLEAN, PRIMARY KEY ('medicineID'));");

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
          "CREATE TABLE  Medicine(medicineID Integer NOT NULL ,medName TEXT,description TEXT, price float , inStock BIT, PRIMARY KEY (medicineID));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Medicine getEntry(String pkID) {
    Medicine med = new Medicine(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setString(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        med.setMedicineID(r.getInt(1));
        med.setMedName(r.getString(2));
        med.setDescription(r.getString(3));
        med.setPrice(r.getDouble(4));
        med.setInStock(r.getBoolean(5));
        return med;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return med; // **
  }
}
