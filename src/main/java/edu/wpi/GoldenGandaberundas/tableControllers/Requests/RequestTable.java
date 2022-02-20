package edu.wpi.GoldenGandaberundas.tableControllers.Requests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class RequestTable extends TableController<Request, Integer> {
  private static RequestTable instance = null;

  /**
   * creates an instance of the master request table this table holds common and meta data across
   * all data objects
   *
   * @throws SQLException
   */
  private RequestTable() throws SQLException {
    super(
        "Requests",
        Arrays.asList(
            new String[] {
              "requestID", "locationID", "empInitiated", "empCompleter",
              "timeStart", "timeEnd", "patientID", "requestType",
              "requestStatus", "notes"
            }),
        "requestID");
    String[] cols = {
      "requestID", "locationID", "empInitiated", "empCompleter",
      "timeStart", "timeEnd", "patientID", "requestType",
      "requestStatus", "notes"
    };
    createTable();
    objList = new ArrayList<Request>();
    objList = readTable();
  }

  /**
   * gives access to a static instance of the class so that only 1 instance of the class exists at
   * one time Implementation of singleton design pattern
   *
   * @return returns either a new instance if no previous calls, or reference to existing instance
   */
  public static RequestTable getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new RequestTable();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  /**
   * Generates an array list of request objects that represents the set of data stored in the DB
   * table
   *
   * @return arraylist that holds data stored in DB table, null if error
   */
  @Override
  public ArrayList<Request> readTable() {
    ArrayList<Request> requests = new ArrayList<>();
    try {
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      ResultSet resultSet = s.executeQuery();
      while (resultSet.next()) {
        requests.add(
            new Request(
                resultSet.getInt(1),
                resultSet.getString(2),
                resultSet.getInt(3),
                resultSet.getInt(4),
                resultSet.getLong(5),
                resultSet.getLong(6),
                resultSet.getInt(7),
                resultSet.getString(8),
                resultSet.getString(9),
                resultSet.getString(10)));
      }
      return requests;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
      System.out.println("ERROR in reading REQUESTS backup");
      return null;
    }
  }

  /**
   * adds request object into master request table and corresponding specific service request table
   *
   * @param obj a request type object or one of its child classes
   * @return true on success, false otherwise.
   */
  @Override
  public boolean addEntry(Request obj) {
    Boolean addSuccess = false;
    if (entryExists(obj.getRequestID()) || addRequest(obj)) {
      addSuccess = this.getSpecificServiceTable(obj).addEntry(obj);
    } else addSuccess = false;
    return addSuccess;
  }

  /**
   * adds request object to the request table
   *
   * @param obj object of Request type
   * @return true on success, false otherwise
   */
  private boolean addRequest(Request obj) {
    try {
      PreparedStatement s =
          connection.prepareStatement("INSERT INTO " + tbName + " VALUES (?,?,?,?,?,?,?,?,?,?);");
      s.setInt(1, obj.getRequestID());
      s.setString(2, obj.getLocationID());
      s.setInt(3, obj.getEmpInitiated());
      if (obj.getEmpCompleter() != null) {
        s.setInt(4, obj.getEmpCompleter());
      } else s.setNull(4, Types.NULL);
      s.setLong(5, obj.getTimeStart());
      if (obj.getTimeEnd() != null) {
        s.setLong(6, obj.getTimeEnd());
      } else s.setNull(6, Types.NULL);
      if (obj.getPatientID() != null) {
        s.setInt(7, obj.getPatientID());
      } else s.setNull(7, Types.NULL);
      s.setString(8, obj.getRequestType());
      s.setString(9, obj.getRequestStatus());
      s.setString(10, obj.getNotes());
      int r = s.executeUpdate();
      if (r == 0) {
        return false;
      }
    } catch (SQLException throwables) {
      if (throwables.getErrorCode() == 19) {
        System.out.println("Table contraint violated");
      }
      throwables.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public ArrayList<Request> readBackup(String fileName) {
    ArrayList<Request> reqList = new ArrayList<Request>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file

      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(
              new String(
                  "requestID, locationID, empInitiated, empCompleter,"
                      + "timeStart, timeEnd, patientID, requestType"
                      + "requestStatus"
                      + "notes"))) {
        System.err.println("requests backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        // check if a line is blank
        if (!currentLine.isEmpty()) {
          String[] element = currentLine.split(","); // separates each element based on a comma
          Request request =
              new Request(
                  Integer.parseInt(element[0]),
                  element[1],
                  Integer.parseInt(element[2]),
                  Integer.parseInt(element[3]),
                  Integer.parseInt(element[4]),
                  Integer.parseInt(element[5]),
                  Integer.parseInt(element[6]),
                  element[7],
                  element[8],
                  element.length > 9 ? element[9] : null);
          reqList.add(request); // adds the location to the list
          // reqList.add(readSpecificRequest(element, request));
        } else {
          System.out.println("EMPTY");
        }
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      System.err.println("REQUEST readbackup FAILED: no file found");
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return reqList;
  }

  //  private Request readSpecificRequest(String[] elements, Request request) {
  //    switch (request.getRequestType()) {
  //      case "MedEquipDelivery":
  //        return new MedEquipRequest(request, Integer.parseInt(elements[10]));
  //
  //      case "MedicineDelivery":
  //        return new MedicineRequest(
  //            request,
  //            Integer.parseInt(elements[10]),
  //            Integer.parseInt(elements[11]),
  //            Integer.parseInt(elements[12]));
  //      case "LaundryService":
  //        return new LaundryRequest(request, Integer.parseInt(elements[10]));
  //      default:
  //        return null;
  //    }
  //  }

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
    System.out.println("SQLite driver registered");

    PreparedStatement s = null;
    try {
      s = connection.prepareStatement("PRAGMA foreign_keys = ON");
      s.execute();
      s =
          connection.prepareStatement(
              "CREATE TABLE IF NOT EXISTS Requests("
                  + "requestID INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + "locationID TEXT,"
                  + "empInitiated INTEGER,"
                  + "empCompleter INTEGER,"
                  + "timeStart INTEGER,"
                  + "timeEnd INTEGER,"
                  + "patientID INTEGER,"
                  + "requestType TEXT,"
                  + "requestStatus TEXT,"
                  + "notes TEXT,"
                  + "CONSTRAINT locFK FOREIGN KEY (locationID) REFERENCES Locations (nodeID)"
                  + "   ON UPDATE CASCADE "
                  + "   ON DELETE CASCADE, "
                  + "CONSTRAINT emp1FK FOREIGN KEY (empInitiated) REFERENCES Employees (empID)"
                  + "   ON UPDATE CASCADE "
                  + "   ON DELETE CASCADE, "
                  + "CONSTRAINT emp2FK FOREIGN KEY (empCompleter) REFERENCES Employees (empID)"
                  + "   ON UPDATE CASCADE"
                  + "   ON DELETE CASCADE,"
                  + "CONSTRAINT patFK FOREIGN KEY (patientID) REFERENCES Patients (patientID)"
                  + "   ON UPDATE CASCADE"
                  + "   ON DELETE CASCADE,"
                  + "CONSTRAINT reqTypeEnum CHECK(requestType in('MedEquipDelivery','MedicineDelivery', 'GiftFloral','LaundryService','Food', 'Computer','AudioVisual')),"
                  + "CONSTRAINT reqStatusEnum CHECK(requestStatus in('Submitted', 'In_Progress', 'Completed')));");
      s.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getErrorCode());
      e.printStackTrace();
      System.out.println("Exception in request table creation");
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
          "CREATE TABLE Requests("
              + "requestID INTEGER PRIMARY KEY, "
              + "locationID VARCHAR(50),"
              + "empInitiated INTEGER,"
              + "empCompleter INTEGER,"
              + "timeStart INTEGER,"
              + "timeEnd INTEGER,"
              + "patientID INTEGER,"
              + "requestType VARCHAR(50),"
              + "requestStatus VARCHAR(50),"
              + "notes TEXT,"
              + "CONSTRAINT locFK FOREIGN KEY (locationID) REFERENCES Locations (nodeID)"
              + "   ON UPDATE CASCADE "
              + "   ON DELETE CASCADE, "
              + "CONSTRAINT emp1FK FOREIGN KEY (empInitiated) REFERENCES Employees (empID)"
              + "   ON UPDATE CASCADE "
              + "   ON DELETE CASCADE, "
              + "CONSTRAINT emp2FK FOREIGN KEY (empCompleter) REFERENCES Employees (empID),"
              + "CONSTRAINT patFK FOREIGN KEY (patientID) REFERENCES Patients (patientID)"
              + "   ON UPDATE NO ACTION"
              + "   ON DELETE NO ACTION,"
              + "CONSTRAINT reqTypeEnum CHECK(requestType in('MedEquipDelivery','MedicineDelivery', 'GiftFloral','LaundryService','Food', 'Computer','AudioVisual')),"
              + "CONSTRAINT reqStatusEnum CHECK(requestStatus in('Submitted', 'In_Progress', 'Completed')));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Request getEntry(Integer pkID) {
    Request request = new Request();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        request.setRequestID(r.getInt(1));
        request.setLocationID(r.getString(2));
        request.setEmpInitiated(r.getInt(3));
        request.setEmpCompleter(r.getInt(4));
        request.setTimeStart(r.getLong(5));
        request.setTimeEnd(r.getLong(6));
        request.setPatientID(r.getInt(7));
        request.setRequestType(r.getString(8));
        request.setRequestStatus(r.getString(9));
        request.setNotes(r.getString(10));
        return request;
      } catch (SQLException throwables) {
        throwables.printStackTrace();
        System.out.println("failed to retrieve request");
        return null;
      }
    }
    return null;
  }

  /**
   * Internal method to get the specifc service table controller given a request object
   *
   * @param specific request from a specifc service component
   * @return
   */
  private TableController getSpecificServiceTable(Request specific) {
    TableController table = null;
    switch (specific.getRequestType()) {
      case "MedEquipDelivery":
        return MedEquipRequestTbl.getInstance();
      case "MedicineDelivery":
        return MedicineRequestTbl.getInstance();
      case "LaundryService":
        return LaundryRequestTbl.getInstance();
      case "GiftFloral":
        return GiftRequestTbl.getInstance();
      case "Food":
        return FoodRequestTbl.getInstance();
      case "Computer":
        return ComputerRequestTbl.getInstance();
      case "AudioVisual":
        return AudioVisualRequestTbl.getInstance();
      default:
        return null;
    }
  }

  /**
   * to edit a specifc service request
   *
   * @param req an arraylist consisting of the composite key of the specific request
   * @param colName the attribute to be changed
   * @param value the value that the attribute will be set to
   * @return true on success, false otherwise
   */
  public boolean editEntry(Request req, String colName, Object value) {
    if (colNames.contains(colName)) {
      return super.editEntry(req.getRequestID(), colName, value);
    } else {
      return this.getSpecificServiceTable(req).editEntry(req.getPK(), colName, value);
    }
  }

  public boolean backupAllRequests(File directory) {
    if (!directory.exists()) {
      directory.mkdir();
    }
    MedicineRequestTbl.getInstance()
        .createBackup(new File(directory.toString() + "/medicineRequests.csv"));
    MedEquipRequestTbl.getInstance()
        .createBackup(new File(directory.toString() + "/medicalEquipRequests.csv"));
    LaundryRequestTbl.getInstance()
        .createBackup(new File(directory.toString() + "/laundryRequests.csv"));
    GiftRequestTbl.getInstance().createBackup(new File(directory.toString() + "/giftRequest.csv"));
    FoodRequestTbl.getInstance().createBackup(new File(directory.toString() + "/foodRequest.csv"));
    ComputerRequestTbl.getInstance()
        .createBackup(new File(directory.toString() + "/computerRequest.csv"));
    AudioVisualRequestTbl.getInstance()
        .createBackup(new File(directory.toString() + "/audioVisualRequest.csv"));

    this.createBackup(new File(directory.toString() + "/requests.csv"));
    return true;
  }

  public boolean loadAllRequests(File directory) {
    // this.loadBackup(directory.toString() + "/requests.csv");
    MedicineRequestTbl.getInstance().readBackup(directory.toString() + "/medicineRequests.csv");
    MedEquipRequestTbl.getInstance().readBackup(directory.toString() + "/medicalEquipRequests.csv");
    LaundryRequestTbl.getInstance().readBackup(directory.toString() + "/laundryRequests.csv");
    GiftRequestTbl.getInstance().readBackup(directory.toString() + "/giftRequest.csv");
    FoodRequestTbl.getInstance().readBackup(directory.toString() + "/foodRequest.csv");
    ComputerRequestTbl.getInstance().readBackup(directory.toString() + "/computerRequest.csv");
    AudioVisualRequestTbl.getInstance()
        .readBackup(directory.toString() + "/audioVisualRequest.csv");

    return true;
  }

  private void deleteEntries() {
    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName);
      s.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
