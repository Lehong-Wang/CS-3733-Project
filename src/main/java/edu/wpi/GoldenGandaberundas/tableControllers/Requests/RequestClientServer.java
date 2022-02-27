package edu.wpi.GoldenGandaberundas.tableControllers.Requests;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class RequestClientServer implements TableController<Request, Integer> {

  private static edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Request> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  /**
   * creates an instance of the master request table this table holds common and meta data across
   * all data objects
   *
   * @throws SQLException
   */
  public RequestClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<Request> objList) throws SQLException {
    this.tbName = tbName;
    colNames = Arrays.asList(cols);
    this.pkCols = pkCols;
    this.objList = objList;
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

  @Override
  public boolean loadFromArrayList(ArrayList<Request> objList) {
    return false;
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
      return this.editEntry(req.getRequestID(), colName, value);
    } else {
      return this.getSpecificServiceTable(req).editEntry(req.getPK(), colName, value);
    }
  }

  private void deleteEntries() {
    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName);
      s.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

  public void writeTable() {

    for (Request obj : objList) {

      this.addEntry(obj);
    }
  }

  public ArrayList<Request> getObjList() {
    return objList;
  }

  /**
   * Modifies the attribute so that it is equal to value MAKE SURE YOU KNOW WHAT DATA TYPE YOU ARE
   * MODIFYING
   *
   * @param pkid the primary key that represents the row you are modifying
   * @param colName column to be modified
   * @param value new value for column
   * @return true if successful, false otherwise
   */
  // public boolean editEntry(T1 pkid, String colName, Object value)
  public boolean editEntry(Integer pkid, String colName, Object value) {
    //    if (pkid instanceof ArrayList) {
    //      return editEntryComposite((ArrayList<Integer>) pkid, colName, value);
    //    }
    try {

      PreparedStatement s =
          connection.prepareStatement(
              "UPDATE "
                  + tbName
                  + " SET "
                  + colName
                  + " = ? WHERE ("
                  + colNames.get(0)
                  + ") =(?);");
      s.setObject(1, value);
      s.setObject(2, pkid);
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * removes a row from the database
   *
   * @param pkid primary key of row to be removed
   * @return true if successful, false otherwise
   */
  public boolean deleteEntry(Integer pkid) {
    //    if (pkid instanceof ArrayList) {
    //      return deleteEntryComposite((ArrayList<Integer>) pkid);
    //    }
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
      s.setObject(1, pkid);
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * creates CSV file representing the objects stored in the table
   *
   * @param f filename of the to be created CSV
   */
  public void createBackup(File f) {
    if (objList.isEmpty()) {
      return;
    }
    /* Instantiate the writer */
    PrintWriter writer = null;
    try {
      writer = new PrintWriter(f);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    /* Get the class type of the objects in the array */
    final Class<?> type = objList.get(0).getClass();

    /* Get the name of all the attributes */
    final ArrayList<Field> classAttributes = new ArrayList<>(List.of(type.getDeclaredFields()));

    boolean doesExtend = Request.class.isAssignableFrom(type);
    if (doesExtend) {
      final Class<?> superType = objList.get(0).getClass().getSuperclass();
      classAttributes.addAll(0, (List.of(superType.getDeclaredFields())));
    }

    /* Write the parsed attributes to the file */
    writer.println(classAttributes.stream().map(Field::getName).collect(Collectors.joining(",")));

    /* For each object, read each attribute and append it to the file with a comma separating */
    PrintWriter finalWriter = writer;
    objList.forEach(
        obj -> {
          finalWriter.println(
              classAttributes.stream()
                  .map(
                      attribute -> {
                        attribute.setAccessible(true);
                        String output = "";
                        try {
                          output = attribute.get(obj).toString();
                        } catch (IllegalAccessException | ClassCastException e) {
                          System.err.println("[CSVUtil] Object attribute access error.");
                        }
                        return output;
                      })
                  .collect(Collectors.joining(",")));
          finalWriter.flush();
        });
    writer.close();
  }

  // drop current table and enter data from CSV
  public ArrayList<Request> loadBackup(String fileName) {
    createTable();
    ArrayList<Request> listObjs = readBackup(fileName);

    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
      this.objList = listObjs;
      this.writeTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return listObjs;
  }

  // checks if an entry exists
  public boolean entryExists(Integer pkID) {
    boolean exists = false;
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");

      s.setObject(1, pkID);

      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        exists = true;
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return exists;
  }

  public String getTableName() {
    return tbName;
  }
}
