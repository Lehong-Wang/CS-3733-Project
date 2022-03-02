package edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService;

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

public class GiftRequestEmbedded implements TableController<GiftRequest, ArrayList<Integer>> {
  private static TableController<Request, Integer> masterTable = RequestTable.getInstance();
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<GiftRequest> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public GiftRequestEmbedded(
      String tbName, String[] cols, String pkCols, ArrayList<GiftRequest> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<GiftRequest> readTable() {
    ArrayList tableInfo = new ArrayList<GiftRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        System.out.println(masterTable.getEntry(r.getInt(1)));
        tableInfo.add(
            new GiftRequest( // **
                masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getInt(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(GiftRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }

    GiftRequest giftReq = (GiftRequest) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

      s.setInt(1, obj.getRequestID());
      s.setInt(2, obj.getGiftID());
      s.setInt(3, obj.getQuantity());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.err.println("LINE NUM: " + e.getStackTrace()[0].getLineNumber());
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<GiftRequest> readBackup(String fileName) {
    ArrayList<GiftRequest> gReqList = new ArrayList<GiftRequest>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);

      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        GiftRequest req = // **
            new GiftRequest(
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
                Integer.parseInt(element[11]));
        gReqList.add(req); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return gReqList; // **
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
          "CREATE TABLE IF NOT EXISTS  GiftRequests("
              + "reqID INTEGER NOT NULL, "
              + "giftID integer, "
              + "quantity integer, "
              + "CONSTRAINT GiftRequestPK PRIMARY KEY (reqID, giftID), "
              + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE, "
              + "CONSTRAINT GiftFK FOREIGN KEY (giftID) REFERENCES Gifts (giftID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE SET NULL);");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public GiftRequest getEntry(ArrayList<Integer> pkID) {
    GiftRequest gift = new GiftRequest();
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
          gift.setRequestID(r.getInt(1));
          gift.setGiftID(r.getInt(2));
          gift.setQuantity(r.getInt(3));
        }
        return gift; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return gift; // **
  }

  @Override
  public boolean loadFromArrayList(ArrayList<GiftRequest> objList) {

    this.createTable();
    deleteTableData();
    for (GiftRequest giftRequest : objList) {
      if (!this.addEntry(giftRequest)) {
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

    for (GiftRequest obj : objList) {

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
  public ArrayList<GiftRequest> loadBackup(String fileName) {
    createTable();
    ArrayList<GiftRequest> listObjs = readBackup(fileName);

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

  public ArrayList<GiftRequest> getObjList() {
    return objList;
  }
}
