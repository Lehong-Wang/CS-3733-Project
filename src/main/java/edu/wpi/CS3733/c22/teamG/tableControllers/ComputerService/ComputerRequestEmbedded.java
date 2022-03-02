package edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService;

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
import java.util.Locale;
import java.util.stream.Collectors;

public class ComputerRequestEmbedded
    implements TableController<ComputerRequest, ArrayList<Integer>> {
  private static TableController<Request, Integer> masterTable = RequestTable.getInstance();
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<ComputerRequest> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public ComputerRequestEmbedded(
      String tbName, String[] cols, String pkCols, ArrayList<ComputerRequest> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

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
  public void createTable() {
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

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public ComputerRequest getEntry(ArrayList<Integer> pkID) {
    ComputerRequest comReq = new ComputerRequest();
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

  @Override
  public boolean loadFromArrayList(ArrayList<ComputerRequest> objList) {

    this.createTable();
    deleteTableData();
    for (ComputerRequest computerRequest : objList) {
      if (!this.addEntry(computerRequest)) {
        return false;
      }
    }
    this.objList = this.readTable();
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

    for (ComputerRequest obj : objList) {

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
  public ArrayList<ComputerRequest> loadBackup(String fileName) {
    createTable();
    ArrayList<ComputerRequest> listObjs = readBackup(fileName);

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

  public ArrayList<ComputerRequest> getObjList() {
    return objList;
  }
}
