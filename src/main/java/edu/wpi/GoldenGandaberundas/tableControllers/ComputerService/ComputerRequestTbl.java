package edu.wpi.GoldenGandaberundas.tableControllers.ComputerService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class ComputerRequestTbl extends TableController<ComputerRequest, ArrayList<Integer>> {

  // **
  // created instance for singleton
  private static ComputerRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;

  // **
  // created constructor fo the table
  private ComputerRequestTbl() throws SQLException {
    super(
        "ComputerRequests",
        Arrays.asList(new String[] {"reqID", "computerID", "problemType", "priority"}),
        "reqID, computerID");
    String[] cols = {"reqID", "computerID", "problemType", "priority"};
    masterTable = RequestTable.getInstance();
    createTable();

    objList = new ArrayList<ComputerRequest>();
    objList = readTable();
  }

  // **
  // created getInstance method for singleton implementation
  public static ComputerRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new ComputerRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  // Creates readTable method and returns an array list of Computer Requests
  @Override
  public ArrayList<ComputerRequest> readTable() {
    ArrayList tableInfo = new ArrayList<ComputerRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new ComputerRequest(
                masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getString(3), r.getString(4)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(ComputerRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }
    ComputerRequest computerReq = (ComputerRequest) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?);");

      s.setInt(1, obj.getRequestID());
      s.setInt(2, obj.getComputerID());
      s.setString(3, obj.getProblemType());
      s.setString(4, obj.getPriority());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<ComputerRequest> readBackup(String fileName) {
    ArrayList<ComputerRequest> cReqList = new ArrayList<>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("reqID,computerID,problemType,priority"))) { // **
        System.err.println("computer request backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        ComputerRequest req = // **
            new ComputerRequest(
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
                element[11],
                element[12]);
        cReqList.add(req); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return cReqList; // **
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
      s.execute("PRAGMA foreign_keys = ON"); // **
      s.execute(
          "CREATE TABLE IF NOT EXISTS  ComputerRequests("
              + "reqID INTEGER NOT NULL, "
              + "computerID INTEGER NOT NULL, "
              + "problemType TEXT NOT NULL,"
              + "priority TEXT NOT NULL, "
              + "CONSTRAINT ComputerRequestPK PRIMARY KEY (reqID, computerID), "
              + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE, "
              + "CONSTRAINT ComputerFK FOREIGN KEY (computerID) REFERENCES Computer (computerID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE);");
      this.writeTable();
      return true;

    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ComputerRequest getEntry(ArrayList<Integer> pkID) {
    ComputerRequest comReq = new ComputerRequest();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE (" + pkCols + ") = (?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next();

        if (entryExists(pkID)) {
          comReq.setRequestID(r.getInt(1));
          comReq.setComputerID(r.getInt(2));
          comReq.setProblemType(r.getString(3));
          comReq.setPriority(r.getString(4));
        }
        return comReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return comReq; // **
  }
}
