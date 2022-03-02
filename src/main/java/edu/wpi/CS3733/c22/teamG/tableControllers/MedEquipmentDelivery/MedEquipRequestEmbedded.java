package edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class MedEquipRequestEmbedded
    implements TableController<MedEquipRequest, ArrayList<Integer>> {
  private static TableController<Request, Integer> masterTable = RequestTable.getInstance();
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<MedEquipRequest> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public MedEquipRequestEmbedded(
      String tbName, String[] cols, String pkCols, ArrayList<MedEquipRequest> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<MedEquipRequest> readTable() {
    ArrayList tableInfo = new ArrayList<MedEquipRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new MedEquipRequest(masterTable.getEntry(r.getInt(1)), r.getInt(2))); // **
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(MedEquipRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }

    MedEquipRequest medEquipRequest = (MedEquipRequest) obj; // **
    PreparedStatement s = null;
    try {
      s = connection.prepareStatement("INSERT OR IGNORE INTO " + tbName + " VALUES (?,?);");
      s.setInt(1, obj.getReqID());
      s.setInt(2, obj.getMedID());
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
  public void createTable() {
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
        return;
      } else System.out.println("COUNT: " + count);
    } catch (SQLException e) {
      System.out.println("MASSIVE ERROR");
      e.printStackTrace();
      return;
    }
    try {
      Class.forName("org.sqlite.JDBC"); // connects to db
    } catch (ClassNotFoundException e) {
      System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
      e.printStackTrace();
      return;
    }

    System.out.println("SQLite driver registered!");
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

    } catch (SQLException e) {
      e.printStackTrace();
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

  @Override
  public boolean loadFromArrayList(ArrayList<MedEquipRequest> objList) {

    this.createTable();
    deleteTableData();
    for (MedEquipRequest medEquipRequest : objList) {
      if (!this.addEntry(medEquipRequest)) {
        return false;
      }
    }
    this.objList = readTable();
    return true;
  }

  private void deleteTableData() {
    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeTable() {

    for (MedEquipRequest obj : objList) {

      this.addEntry(obj);
    }
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
  public boolean editEntry(ArrayList<Integer> pkid, String colName, Object value) {
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkid.size() - 1; i++) {
      pkString.append(pkid.get(i)).append(",");
    }
    pkString.append(pkid.get(pkid.size() - 1));
    try {

      PreparedStatement s =
          connection.prepareStatement(
              "UPDATE "
                  + tbName
                  + " SET "
                  + colName
                  + " = ? WHERE ("
                  + pkCols
                  + ") = ("
                  + pkString
                  + ");");
      s.setObject(1, value);
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
  public boolean deleteEntry(ArrayList<Integer> pkid) {
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkid.size() - 1; i++) {
      pkString.append(pkid.get(i)).append(",");
    }
    pkString.append(pkid.get(pkid.size() - 1));
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "DELETE FROM " + tbName + " WHERE (" + pkCols + ") = (" + pkString + ");");
      s.executeUpdate();
      return true;
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
  public ArrayList<MedEquipRequest> loadBackup(String fileName) {
    createTable();
    ArrayList<MedEquipRequest> listObjs = readBackup(fileName);

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
  public boolean entryExists(ArrayList<Integer> pkID) {
    boolean exists = false;
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkID.size() - 1; i++) {
      pkString.append(pkID.get(i)).append(",");
    }
    pkString.append(pkID.get(pkID.size() - 1));

    try {
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT count(*) FROM "
                  + tbName
                  + " WHERE ("
                  + pkCols
                  + ") = ("
                  + pkString.toString()
                  + ");");
      ResultSet r = s.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        exists = true;
      }
      return exists;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<MedEquipRequest> getObjList() {
    return objList;
  }
}
