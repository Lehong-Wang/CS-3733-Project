package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery;

import edu.wpi.GoldenGandaberundas.ConnectionType;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class MedEquipRequestTbl extends TableController<MedEquipRequest, ArrayList<Integer>> {
  private static MedEquipRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null; // **

  // first entry in column name is primary key
  private MedEquipRequestTbl() throws SQLException { // creates the table
    super(
        "MedEquipRequests",
        Arrays.asList(new String[] {"reqID", "medEquipID"}),
        "reqID, medEquipID"); // **

    createTable();
    objList = new ArrayList<MedEquipRequest>(); // object list
    //    createTable(); // makes a table with the following values
    masterTable = RequestTable.getInstance(); // **
    objList = readTable(); // reads table
  }

  public static MedEquipRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new MedEquipRequestTbl();

          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return (MedEquipRequestTbl) instance;
  }

  public MedEquipRequestTbl getOrdersTable() {
    return this.getInstance();
  }

  /**
   * ...okay so, reading this table will get a MedEquipReq object with ALL fields of the MedEquipReq
   * and the Req obj
   *
   * @return
   */
  @Override
  public ArrayList<MedEquipRequest> readTable() {
    ArrayList tableInfo =
        new ArrayList<MedEquipRequest>(); // initializes a new list of medical equipment requests
    try { // if code works, do this:
      PreparedStatement s =
          connection.prepareStatement(
              "SElECT * FROM " + tbName + ";"); // prepares to take all attributes from the table
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new MedEquipRequest(masterTable.getEntry(r.getInt(1)), r.getInt(2))); // **
      }
    } catch (Exception e) { // if the other stuff doesn't work
      e.printStackTrace();
      return null;
    }
    return tableInfo; // returns the array list of requests
  }

  @Override
  public boolean addEntry(MedEquipRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }

    if (TableController.getConnectionType() == ConnectionType.clientServer) {
      return addEntryOnline(obj);
    }

    if (!entryExists(obj.getPK())) { // does reqID already exist in the table?
      try { // if reqID does not exist, adds new request object to database
        PreparedStatement s =
            connection.prepareStatement("INSERT OR IGNORE INTO " + tbName + " VALUES (?,?);");
        s.setInt(1, obj.getReqID());
        s.setInt(2, obj.getMedID());
        s.executeUpdate();
        return true;
      } catch (Exception e) {
        e.printStackTrace();
      }
    } else { // if reqID does exist, prints the following message
      System.out.println("Entry ID already exists.");
    }
    return false;
  }

  private boolean addEntryOnline(MedEquipRequest medEquipRequest) {
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

      s.setInt(1, medEquipRequest.getReqID());
      s.setInt(2, medEquipRequest.getMedID());
      s.setInt(3, medEquipRequest.getReqID());
      s.setInt(4, medEquipRequest.getMedID());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<MedEquipRequest> readBackup(String fileName) { // reads csv file for backup
    ArrayList<MedEquipRequest> reqList =
        new ArrayList<>(); // creates a list for the request objects
    try {
      File csvFile = new File(fileName); // takes csv file
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        MedEquipRequest req = // creates a request
            new MedEquipRequest( // **
                Integer.parseInt(element[0]),
                element[1],
                Integer.parseInt(element[2]),
                Integer.parseInt(element[3]),
                Integer.parseInt(element[4]),
                Integer.parseInt(element[5]),
                Integer.parseInt(element[6]),
                element[8],
                element[9],
                Integer.parseInt(element[10]));
        reqList.add(req);
        currentLine = buffer.readLine(); // reads the current line
      }
      return reqList;

    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean createTable() {

    if (TableController.getConnectionType() == ConnectionType.clientServer) {
      return createTableOnline();
    }

    try { // if code works, do this:
      System.out.println("SELECT count(*) FROM sqlite_master WHERE tbl_name = " + tbName + ";");
      PreparedStatement s =
          connection.prepareStatement("SELECT count(*) FROM sqlite_master WHERE tbl_name = ?;");
      s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      r.next();
      int count = r.getInt(1);
      if (count != 0) {
        System.out.println("TABLE DETECTED");
        return false;
      } else System.out.println("COUNT: " + count);
    } catch (SQLException e) {
      System.out.println("MASSIVE ERROR");
      e.printStackTrace();
      return false;
    }
    try {
      Class.forName("org.sqlite.JDBC"); // connects to db
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return false;
    }

    System.out.println("SQLite driver registered!");
    System.out.println("MED EQUIP BUILT");
    Statement s = null;
    try { // this part actually makes the table after getting everything else set up
      s = connection.createStatement();
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          " CREATE TABLE IF NOT EXISTS "
              + tbName
              + "("
              + "    reqID     integer NOT NULL, "
              + "    medEquipID     integer, "
              + "    CONSTRAINT MedEquipReqPK PRIMARY KEY (reqID, medEquipID), "
              + "    CONSTRAINT reqIDFK1 FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "        ON UPDATE CASCADE  "
              + "        ON DELETE CASCADE, "
              + "    CONSTRAINT MedEquipReqFK2 FOREIGN KEY (medEquipID) REFERENCES MedEquipment (medID) "
              + "        ON DELETE CASCADE  "
              + "        ON UPDATE CASCADE "
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
          " CREATE TABLE "
              + tbName
              + "("
              + "    reqID     integer NOT NULL, "
              + "    medEquipID     integer, "
              + "    CONSTRAINT MedEquipReqPK PRIMARY KEY (reqID, medEquipID), "
              + "    CONSTRAINT reqIDFK1 FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "        ON UPDATE CASCADE  "
              + "        ON DELETE CASCADE, "
              + "    CONSTRAINT MedEquipReqFK2 FOREIGN KEY (medEquipID) REFERENCES MedEquipment (medID) "
              + ");");
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public MedEquipRequest getEntry(ArrayList<Integer> pkID) { // grabs one db entry
    MedEquipRequest req = new MedEquipRequest(); // makes a new request object
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM medEquipRequests WHERE ("
                    + pkCols
                    + ") = (?, ?);"); // makes a statement
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery(); // finds the row of the ID

        if (entryExists(pkID)) { // if the ID exists
          // the following lines go through the columns in the row of the ID
          req.setReqID(r.getInt(1));
          req.setMedID(r.getInt(2));
        }
      } catch (Exception e) { // database code didn't work
        e.printStackTrace(); // prints errors
        return null; // nothing happens
      }
    }
    return req; // returns object
  }
}
