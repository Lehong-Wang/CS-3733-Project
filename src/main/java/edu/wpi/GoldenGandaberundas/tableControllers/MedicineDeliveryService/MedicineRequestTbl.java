package edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class MedicineRequestTbl extends TableController<MedicineRequest, ArrayList<Integer>> {

  private static MedicineRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;

  private MedicineRequestTbl() throws SQLException {
    super(
        "MedicineRequests",
        Arrays.asList("reqID", "medicineID", "dosage", "quantity"),
        "reqID, medicineID");
    String[] cols = {"reqID", "medicineID", "dosage", "quantity"};
    masterTable = RequestTable.getInstance();
    createTable();

    objList = new ArrayList<MedicineRequest>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static MedicineRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedicineRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<MedicineRequest> readTable() {
    ArrayList tableInfo = new ArrayList<MedicineRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        System.out.println(masterTable.getEntry(r.getInt(1)));
        tableInfo.add(
            new MedicineRequest(
                masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getInt(3), r.getInt(4)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    System.out.println(tableInfo);
    return tableInfo;
  }

  @Override
  public boolean addEntry(MedicineRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }

    if (!this.getEmbedded()) {
      return addEntryOnline(obj);
    }

    MedicineRequest medicineRequest = (MedicineRequest) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?);");
      s.setInt(1, medicineRequest.getRequestID());
      s.setInt(2, medicineRequest.getMedicineID());
      s.setInt(3, medicineRequest.getDosage());
      s.setInt(4, medicineRequest.getQuantity());
      s.executeUpdate();

      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(MedicineRequest medicineRequest) {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              " IF NOT EXISTS (SELECT 1 FROM "
                  + tbName
                  + " WHERE "
                  + colNames.get(0)
                  + " = ?"
                  + " AND "
                  + colNames.get(1)
                  + " = ?"
                  + ")"
                  + "BEGIN"
                  + "    INSERT INTO "
                  + tbName
                  + " VALUES (?, ?, ?, ?)"
                  + "end");

      s.setInt(1, medicineRequest.getRequestID());
      s.setInt(2, medicineRequest.getMedicineID());
      s.setInt(3, medicineRequest.getRequestID());
      s.setInt(4, medicineRequest.getMedicineID());
      s.setInt(5, medicineRequest.getDosage());
      s.setInt(6, medicineRequest.getQuantity());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<MedicineRequest> readBackup(String fileName) {

    ArrayList<MedicineRequest> medReqList = new ArrayList<>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("reqID,medicineID,dosage,quantity"))) { // **
        System.err.println("medicine request backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        MedicineRequest req = // **
            new MedicineRequest(
                Integer.parseInt(element[0]),
                element[1],
                Integer.parseInt(element[2]),
                Integer.parseInt(element[3]),
                Long.parseLong(element[4]),
                Long.parseLong(element[5]),
                Integer.parseInt(element[6]),
                element[8],
                element[9],
                Integer.parseInt(element[10]),
                Integer.parseInt(element[11]),
                Integer.parseInt(element[12])); // **
        medReqList.add(req);
        currentLine = buffer.readLine();
      }

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return medReqList;
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
      s.execute("PRAGMA foreign_keys = ON"); // **
      s.execute(
          "CREATE TABLE IF NOT EXISTS  MedicineRequests("
              + "reqID INTEGER NOT NULL, "
              + "medicineID INTEGER NOT NULL,"
              + "dosage INTEGER,"
              + "quantity INTEGER,"
              + "CONSTRAINT MedicineRequestPK PRIMARY KEY (reqID, medicineID), "
              + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT MedIDFK FOREIGN KEY (medicineID) REFERENCES Medicine (medicineID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + " );");

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
          "CREATE TABLE MedicineRequests("
              + "reqID INTEGER NOT NULL, "
              + "medicineID INTEGER NOT NULL,"
              + "dosage INTEGER,"
              + "quantity INTEGER,"
              + "CONSTRAINT MedicineRequestPK PRIMARY KEY (reqID, medicineID), "
              + "CONSTRAINT MedReqFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT MedIdFK FOREIGN KEY (medicineID) REFERENCES Medicine (medicineID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + " );");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public MedicineRequest getEntry(ArrayList<Integer> pkID) {
    MedicineRequest medReq = new MedicineRequest(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE (" + pkCols + ")" + " =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next(); // **

        if (entryExists(pkID)) {
          medReq.setRequestID(r.getInt(1));
          medReq.setMedicineID(r.getInt(2));
          medReq.setDosage(r.getInt(3));
          medReq.setQuantity(r.getInt(4));
        }

        return medReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return medReq; // **
  }
}
