package edu.wpi.GoldenGandaberundas.tableControllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeePermission;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EmployeePermissionClientServer
    implements TableController<EmployeePermission, ArrayList<Integer>> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<EmployeePermission> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public EmployeePermissionClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<EmployeePermission> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<EmployeePermission> readTable() { // **
    ArrayList tableInfo = new ArrayList<EmployeePermission>(); // **
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new EmployeePermission(r.getInt(1), r.getInt(2)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo;
  }

  @Override
  public boolean addEntry(EmployeePermission empPerm) {
    PreparedStatement s = null;
    try {
      s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement(
                  " IF NOT EXISTS (SELECT 1 FROM "
                      + tbName
                      + " WHERE "
                      + colNames.get(0)
                      + " = ? AND "
                      + colNames.get(1)
                      + " = ?)"
                      + "BEGIN"
                      + "    INSERT INTO "
                      + tbName
                      + " VALUES (?, ?)"
                      + "end");
      // **
      s.setInt(1, empPerm.getEmpID());
      s.setInt(2, empPerm.getPermID());
      s.setInt(3, empPerm.getEmpID());
      s.setInt(4, empPerm.getPermID());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<EmployeePermission> readBackup(String fileName) {
    ArrayList<EmployeePermission> empPermList = new ArrayList<EmployeePermission>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("empPermID,deviceType,locID,description"))) { // **
        System.err.println("EmployeePermission backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        EmployeePermission empPerm = // **
            new EmployeePermission(
                Integer.parseInt(element[0]), Integer.parseInt(element[1])); // **
        empPermList.add(empPerm); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return empPermList; // **
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
          "CREATE TABLE  EmployeePermissions("
              + "empID INTEGER NOT NULL, "
              + "permID INTEGER NOT NULL,"
              + " CONSTRAINT EmployeePermissionsPK PRIMARY KEY (empID,permID), "
              + "CONSTRAINT EmplyoeePermissionsFK1 FOREIGN KEY (empID) REFERENCES Employees (empID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT EmplyoeePermissionsFK2 FOREIGN KEY (permID) REFERENCES Permissions (permID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE "
              + ");");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public EmployeePermission getEntry(ArrayList<Integer> pkID) { // **
    EmployeePermission empPerm = new EmployeePermission(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            ConnectionHandler.getInstance()
                .getConnection()
                .prepareStatement(
                    "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next();
        if (entryExists(pkID)) {
          empPerm.setEmpID(r.getInt(1));
          empPerm.setPermID(r.getInt(2));
        }
        return empPerm;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return empPerm; // **
  }

  @Override
  public boolean loadFromArrayList(ArrayList<EmployeePermission> objList) {
    this.createTable();
    deleteTableData();
    for (EmployeePermission empPerm : objList) {
      if (!this.addEntry(empPerm)) {
        return false;
      }
    }
    this.objList = readTable();
    System.err.println("PERMS: " + this.objList);
    return true;
  }

  private void deleteTableData() {
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
      objList = this.readTable();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeTable() {

    for (EmployeePermission obj : objList) {
      this.addEntry(obj);
    }
    this.objList = readTable();
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
    //    if (pkid instanceof ArrayList) {
    //      return editEntryComposite((ArrayList<Integer>) pkid, colName, value);
    //    }
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
                      + colNames.get(0)
                      + ") =(?);");
      s.setObject(1, value);
      s.setObject(2, pkid);
      s.executeUpdate();
      this.objList = this.readTable();
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
    //    if (pkid instanceof ArrayList) {
    //      return deleteEntryComposite((ArrayList<Integer>) pkid);
    //    }
    try {
      PreparedStatement s =
          ConnectionHandler.getInstance()
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
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
  public ArrayList<EmployeePermission> loadBackup(String fileName) {
    createTable();
    ArrayList<EmployeePermission> listObjs = readBackup(fileName);

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
    //    if (pkID instanceof ArrayList) {
    //      return entryExistsComposite((ArrayList<Integer>) pkID);
    //    }
    boolean exists = false;
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

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return exists;
  }

  public String getTableName() {
    return tbName;
  }

  public ArrayList<EmployeePermission> getObjList() {
    return objList;
  }
}
