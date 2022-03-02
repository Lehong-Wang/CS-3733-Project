package edu.wpi.CS3733.c22.teamG.tableControllers.AudioVisualService;

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

public class AudioVisualRequestClientServer
    implements TableController<AudioVisualRequest, ArrayList<Integer>> {
  private static TableController<Request, Integer> masterTable = RequestTable.getInstance();
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<AudioVisualRequest> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public AudioVisualRequestClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<AudioVisualRequest> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<AudioVisualRequest> readTable() {
    ArrayList tableInfo = new ArrayList<AudioVisualRequest>(); // **
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new AudioVisualRequest(masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getString(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(AudioVisualRequest audioVisual) {
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
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
                      + " VALUES (?, ?, ?)"
                      + "end");

      s.setInt(1, audioVisual.getRequestID());
      s.setInt(2, audioVisual.getAudioVisualID());
      s.setInt(3, audioVisual.getRequestID());
      s.setInt(4, audioVisual.getAudioVisualID());
      s.setString(5, audioVisual.getPriority());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<AudioVisualRequest> readBackup(String fileName) {
    ArrayList<AudioVisualRequest> avReqList = new ArrayList<>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("reqID,audioVisualID,priority"))) { // **
        System.err.println("audio visual request backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        AudioVisualRequest req = // **
            new AudioVisualRequest(
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
        avReqList.add(req); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return avReqList; // **
  }

  @Override
  public void createTable() {
    try {

      PreparedStatement s1 =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
      Statement s = ConnectionHandler.getInstance().getConnection().createStatement();
      s.execute(
          "CREATE TABLE  AudioVisualRequests("
              + "reqID INTEGER NOT NULL, "
              + "audioVisualID INTEGER NOT NULL, "
              + "priority TEXT NOT NULL, "
              + "CONSTRAINT AudioVisualRequestPK PRIMARY KEY (reqID, audioVisualID), "
              + "CONSTRAINT AVRequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE, "
              + "CONSTRAINT AudioVisualFK FOREIGN KEY (audioVisualID) REFERENCES AudioVisual (avID));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public AudioVisualRequest getEntry(ArrayList<Integer> pkID) {
    AudioVisualRequest audReq = new AudioVisualRequest();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            ConnectionHandler.getInstance()
                .getConnection()
                .prepareStatement("SELECT * FROM " + tbName + " WHERE (" + pkCols + ") =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next();

        if (entryExists(pkID)) {
          audReq.setRequestID(r.getInt(1));
          audReq.setAudioVisualID(r.getInt(2));
          audReq.setPriority(r.getString(3));
        }
        return audReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return audReq; // **
  }

  @Override
  public boolean loadFromArrayList(ArrayList<AudioVisualRequest> objList) {
    this.createTable();
    deleteTableData();
    for (AudioVisualRequest audioVisualRequest : objList) {
      if (!this.addEntry(audioVisualRequest)) {
        return false;
      }
    }
    this.objList = this.readTable();
    return true;
  }

  private void deleteTableData() {
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeTable() {

    for (AudioVisualRequest obj : objList) {

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
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
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
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
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
  public ArrayList<AudioVisualRequest> loadBackup(String fileName) {
    createTable();
    ArrayList<AudioVisualRequest> listObjs = readBackup(fileName);

    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + ";");
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
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
                  "SELECT * FROM "
                      + tbName
                      + " WHERE "
                      + colNames.get(0)
                      + " = ?"
                      + " AND "
                      + colNames.get(1)
                      + " = ?"
                      + ");");
      s.setInt(1, pkID.get(0));
      s.setInt(2, pkID.get(1));
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

  public ArrayList<AudioVisualRequest> getObjList() {
    return objList;
  }
}
