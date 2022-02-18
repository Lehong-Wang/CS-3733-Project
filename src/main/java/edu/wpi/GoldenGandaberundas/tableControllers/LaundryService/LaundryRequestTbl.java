package edu.wpi.GoldenGandaberundas.tableControllers.LaundryService;

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

public class LaundryRequestTbl extends TableController<LaundryRequest, ArrayList<Integer>> {

  private static LaundryRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;

  private LaundryRequestTbl() throws SQLException {
    super("LaundryRequests", Arrays.asList("reqID", "laundryID"), "reqID, laundryID");
    String[] cols = {"reqID", "laundryID"};
    masterTable = RequestTable.getInstance();
    createTable();
    objList = new ArrayList<LaundryRequest>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static LaundryRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new LaundryRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<LaundryRequest> readTable() {
    ArrayList tableInfo = new ArrayList<LaundryRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        System.out.println(masterTable.getEntry(r.getInt(1)));
        tableInfo.add(
            new LaundryRequest( // **
                masterTable.getEntry(r.getInt(1)), r.getInt(2)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(LaundryRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }
    if (this.getEmbedded()) {
      return addEntryOnline(obj);
    }
    LaundryRequest launReq = (LaundryRequest) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?);");

      // **
      s.setInt(1, launReq.getRequestID());
      s.setInt(2, launReq.getLaundryID());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean addEntryOnline(LaundryRequest laundryReq) {
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
                  + " VALUES (?, ?)"
                  + "end");

      s.setInt(1, laundryReq.getRequestID());
      s.setInt(2, laundryReq.getLaundryID());
      s.setInt(3, laundryReq.getRequestID());
      s.setInt(4, laundryReq.getLaundryID());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<LaundryRequest> readBackup(String fileName) {
    ArrayList<LaundryRequest> laundryList = new ArrayList<LaundryRequest>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);

      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        LaundryRequest req = // **
            new LaundryRequest(
                Integer.parseInt(element[0]),
                element[1],
                Integer.parseInt(element[2]),
                Integer.parseInt(element[3]),
                Long.parseLong(element[4]),
                Long.parseLong(element[5]),
                element[8],
                element[9],
                Integer.parseInt(element[10])); // **
        laundryList.add(req);
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
          "CREATE TABLE IF NOT EXISTS  LaundryRequests("
              + "reqID INTEGER NOT NULL, "
              + "laundryID INTEGER NOT NULL, "
              + "CONSTRAINT LaundryRequestPK PRIMARY KEY (reqID, laundryID), "
              + "CONSTRAINT LaundryRequestFK1 FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE , "
              + "CONSTRAINT LaundryRequestFK2 FOREIGN KEY (laundryID) REFERENCES Laundry (laundryID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE"
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
          "CREATE TABLE  LaundryRequests("
              + "reqID INTEGER NOT NULL, "
              + "laundryID INTEGER NOT NULL, "
              + "CONSTRAINT LaundryRequestPK PRIMARY KEY (reqID, laundryID), "
              + "CONSTRAINT LaundryRequestFK1 FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE , "
              + "CONSTRAINT LaundryRequestFK2 FOREIGN KEY (laundryID) REFERENCES Laundry (laundryID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE"
              + ");");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public LaundryRequest getEntry(ArrayList<Integer> pkID) {
    LaundryRequest launReq = new LaundryRequest();

    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE (" + pkCols + ") =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next(); // **

        if (entryExists(pkID)) {
          launReq.setRequestID(r.getInt(1));
          launReq.setLaundryID(r.getInt(2));
        }

        return launReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return launReq; // **
  }
}
