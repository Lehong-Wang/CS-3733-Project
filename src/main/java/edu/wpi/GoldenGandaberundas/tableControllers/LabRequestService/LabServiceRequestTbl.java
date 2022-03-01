package edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequest;
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

public class LabServiceRequestTbl extends TableController<LabServiceRequest, ArrayList<Integer>> {

  // **
  // created instance for singleton
  private static LabServiceRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;

  // **
  // created constructor fo the table
  private LabServiceRequestTbl() throws SQLException {
    super(
            "LabServiceRequests",
            Arrays.asList(new String[]{"reqID", "labServiceID", "priority"}),
            "reqID, labServiceID");
    String[] cols = {"reqID", "labServiceID", "priority"};
    masterTable = RequestTable.getInstance();
    createTable();
    objList = new ArrayList<>();
    objList = readTable();
  }

  // **
  // created getInstance method for singleton implementation
  public static LabServiceRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new LabServiceRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<LabServiceRequest> readTable() {
    ArrayList tableInfo = new ArrayList<LabServiceRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
                new LabServiceRequest(masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getString(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(LabServiceRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }
    LabServiceRequest labServiceReq = (LabServiceRequest) obj; // **
    PreparedStatement s = null;
    try {
      s =
              connection.prepareStatement( // **
                      "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

      s.setInt(1, obj.getRequestID());
      s.setInt(2, obj.getLabServiceID());
      s.setString(3, obj.getPriority());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<LabServiceRequest> readBackup(String fileName) {
    ArrayList<LabServiceRequest> labReqList = new ArrayList<>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
              .toLowerCase(Locale.ROOT)
              .trim()
              .equals(new String("reqID,labServiceID,priority"))) { // **
        System.err.println("lab service request backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        LabServiceRequest req = // **
                new LabServiceRequest(
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
                        element[11]);
        labReqList.add(req); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return labReqList; // **
  }

  @Override
  public void createTable() {
    try {
      PreparedStatement s =
              connection.prepareStatement("SELECT count(*) FROM sqlite_master WHERE tbl_name = ?;");
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
              "CREATE TABLE TABLE IF NOT EXISTS AudioVisualRequests("
                      + "reqID INTEGER NOT NULL, "
                      + "labServiceID INTEGER NOT NULL, "
                      + "priority TEXT NOT NULL, "
                      + "CONSTRAINT AudioVisualRequestPK PRIMARY KEY (reqID, audioVisualID), "
                      + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
                      + "ON UPDATE CASCADE "
                      + "ON DELETE CASCADE, "
                      + "CONSTRAINT AudioVisualFK FOREIGN KEY (audioVisualID) REFERENCES AudioVisual (avID) "
                      + "ON UPDATE CASCADE "
                      + "ON DELETE CASCADE);");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public LabServiceRequest getEntry(ArrayList<Integer> pkID) {
    LabServiceRequest labServiceReq = new LabServiceRequest();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
                connection.prepareStatement(
                        "SELECT * FROM " + tbName + " WHERE (" + pkCols + ") =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next();

        if (entryExists(pkID)) {
          labServiceReq.setRequestID(r.getInt(1));
          labServiceReq.setLabServiceID(r.getInt(2));
          labServiceReq.setPriority(r.getString(3));
        }
        return labServiceReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return labServiceReq; // **
  }
}
