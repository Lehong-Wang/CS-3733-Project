package edu.wpi.GoldenGandaberundas.tableControllers;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeePermission;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class EmployeePermissionTbl extends TableController<EmployeePermission, ArrayList<Integer>> {
  // creates the instance for the table
  private static EmployeePermissionTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<EmployeePermission> objList;
  /** relative path to the database file */


  ConnectionHandler connection = ConnectionHandler.getInstance();

  private EmployeePermissionTbl() throws SQLException {
    super("EmployeePermissions", Arrays.asList(new String[] {"empID", "permID"}), "empID,permID");
    String[] cols = {"empID", "permID"};
    createTable();

    objList = new ArrayList<EmployeePermission>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static EmployeePermissionTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new EmployeePermissionTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance; // returns instance
  }

  /**
   * Reads the current table for the object
   *
   * @return the Array List of the objects in the table
   */
  @Override
  public ArrayList<EmployeePermission> readTable() {
    // creates Array List to return
    ArrayList tableInfo = new ArrayList<EmployeePermission>(); // **
    try {
      // selects all from the table
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new EmployeePermission(r.getInt(1), r.getInt(2)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    return tableInfo; // returns the Array List of objects
  }

  /**
   * Adds the object to the table
   *
   * @param obj the incoming object to add to the table
   * @return true if the object is added
   */
  @Override
  public boolean addEntry(EmployeePermission obj) {
    // creates permission object to return
    EmployeePermission empPerm = (EmployeePermission) obj; // **
    PreparedStatement s = null;
    try {
      // inserts the object in the table
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?,?);");

      // **
      s.setInt(1, empPerm.getEmpID());
      s.setInt(2, empPerm.getPermID());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * reads a backup CSV file to create a list of the object
   *
   * @param fileName location of the CSV file
   * @return Array List of the of object
   */
  @Override
  public ArrayList<EmployeePermission> readBackup(String fileName) {
    // creates the array list for the object
    ArrayList<EmployeePermission> empPermList = new ArrayList<EmployeePermission>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      // checks of the attributes are the same with the col
      if (!currentLine.toLowerCase(Locale.ROOT).trim().equals(new String("empID,permID"))) { // **
        System.err.println("employee permission backup format not recognized"); // **
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

  /** Method to create the Table for with the proper attributes */
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
      // creates the table
      s.execute(
          "CREATE TABLE IF NOT EXISTS  EmployeePermissions("
              + "empID INTEGER NOT NULL, "
              + "permID INTEGER NOT NULL, "
              + "CONSTRAINT EmployeePermissionsPK PRIMARY KEY (empID,permID)"
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

  /**
   * Returns the objects the matches the primary key
   *
   * @param pkID primary key value that identifies the desired object
   * @return the object with the pkid
   */
  @Override
  public EmployeePermission getEntry(ArrayList<Integer> pkID) {
    // creates the object to return
    EmployeePermission empPerm = new EmployeePermission(); // **
    if (this.entryExists(pkID)) {
      try {
        // returns the selected object that matches the pkid
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + pkCols + " =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next(); // **

        if (entryExists(pkID)) {
          empPerm.setEmpID(r.getInt(1));
          empPerm.setPermID(r.getInt(2));
        }
        return empPerm; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return empPerm; // **
  }

  public void writeTable() {

    for (EmployeePermission obj : objList) {

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
  public boolean deleteEntry(ArrayList<Integer> pkid) {
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
  public ArrayList<EmployeePermission> loadBackup(String fileName) {
    createTable();
    ArrayList<EmployeePermission> listObjs = readBackup(fileName);

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
    //    if (pkID instanceof ArrayList) {
    //      return entryExistsComposite((ArrayList<Integer>) pkID);
    //    }
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
  /**
   * Method that when given an employee ID returns a list of permissions associated with that
   * employee
   *
   * @param empID
   * @return
   */
  public ArrayList<Integer> getPermID(int empID) {
    ArrayList<Integer> permAr = new ArrayList<Integer>();
    try {
      // returns the selected object that matches the pkid
      PreparedStatement s =
          connection.prepareStatement(
              "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =(?);");
      s.setInt(1, empID);
      ResultSet r = s.executeQuery();
      while (r.next()) {
        permAr.add(r.getInt(2));
      }
      return permAr; // **
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return permAr; // **
  }
  }

  public String getTableName() {
    return tbName;
  }
}
