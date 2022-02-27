package edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MedicineRequestClientServer
    implements TableController<MedicineRequest, ArrayList<Integer>> {
  private static TableController<Request, Integer> masterTable = null;
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<MedicineRequest> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public MedicineRequestClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<MedicineRequest> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<MedicineRequest> readTable() {
    ArrayList tableInfo = new ArrayList<MedicineRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        System.out.println(masterTable.getEntry(r.getInt(1)));
        tableInfo.add(
            new MedicineRequest(
                masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getInt(3), r.getInt(4)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    System.out.println(tableInfo);
    return tableInfo;
  }

  @Override
  public boolean addEntry(MedicineRequest medicineRequest) {
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
                  + " VALUES (?, ?, ?, ?)"
                  + "end");

      s.setInt(1, medicineRequest.getRequestID());
      s.setInt(2, medicineRequest.getMedicineID());
      s.setInt(3, medicineRequest.getRequestID());
      s.setInt(4, medicineRequest.getMedicineID());
      s.setInt(5, medicineRequest.getDosage());
      s.setInt(6, medicineRequest.getQuantity());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<MedicineRequest> readBackup(String fileName) {

    ArrayList<MedicineRequest> medReqList = new ArrayList<>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("reqID,medicineID,dosage,quantity"))) { // **
        System.err.println("medicine request backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        MedicineRequest req = // **
            new MedicineRequest(
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
                Integer.parseInt(element[11]),
                Integer.parseInt(element[12])); // **
        medReqList.add(req);
        currentLine = buffer.readLine();
      }

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return medReqList;
  }

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
          "CREATE TABLE MedicineRequests("
              + "reqID INTEGER NOT NULL, "
              + "medicineID INTEGER NOT NULL,"
              + "dosage INTEGER,"
              + "quantity INTEGER,"
              + "CONSTRAINT MedicineRequestPK PRIMARY KEY (reqID, medicineID), "
              + "CONSTRAINT MedReqFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT MedIdFK FOREIGN KEY (medicineID) REFERENCES Medicine (medicineID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + " );");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public MedicineRequest getEntry(ArrayList<Integer> pkID) {
    MedicineRequest medReq = new MedicineRequest(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE (" + pkCols + ")" + " =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next(); // **

        if (entryExists(pkID)) {
          medReq.setRequestID(r.getInt(1));
          medReq.setMedicineID(r.getInt(2));
          medReq.setDosage(r.getInt(3));
          medReq.setQuantity(r.getInt(4));
        }

        return medReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return medReq; // **
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedicineRequest> objList) {
    this.createTable();
    deleteTableData();
    for (MedicineRequest medicineRequest : objList) {
      if (!this.addEntry(medicineRequest)) {
        return false;
      }
    }
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

    for (MedicineRequest obj : objList) {

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
  public ArrayList<MedicineRequest> loadBackup(String fileName) {
    createTable();
    ArrayList<MedicineRequest> listObjs = readBackup(fileName);

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

  public ArrayList<MedicineRequest> getObjList() {
    return objList;
  }
}
