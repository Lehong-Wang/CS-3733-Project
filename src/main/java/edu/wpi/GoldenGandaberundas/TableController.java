package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.sqlite.SQLiteConfig;

public abstract class TableController<T, T1> {

  /** name of table */
  protected String tbName;

  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<T> objList;
  /** relative path to the database file */
  private static String dbPath = new String("jdbc:sqlite:hospitalData.db");

  private static String clientServerPath =
      new String("jdbc:sqlserver://130.215.250.187:58910;databaseName=gandaberunda");
  /** connection object used to access the database */
  protected static Connection connection;
  /** single object allowed to be instantiated in subclasses */
  private static boolean embedded = true;

  private static ArrayList<TableController> allActiveTables = new ArrayList<TableController>();

  /**
   * procedure to connect to the database APP NEEDS TO FAIL IF THIS DOES NOT CONNECT IT IS ESSENTIAL
   * THAT THIS EXECUTES CORRECTLY
   */
  static {
    try {
      SQLiteConfig config = new SQLiteConfig();
      config.enforceForeignKeys(true);
      Class.forName("org.sqlite.JDBC"); // check if we have the drive
      connection =
          DriverManager.getConnection(dbPath, config.toProperties()); // create a connection object
      PreparedStatement s = connection.prepareStatement("PRAGMA foreign_keys = ON;");
      s.execute();
    } catch (SQLException | ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  protected TableController(String tableName, List<String> colNames) throws SQLException {
    this.tbName = tableName;
    this.colNames = colNames;
    this.pkCols = colNames.get(0);
    allActiveTables.add(this);
  }

  protected TableController(String tableName, List<String> colNames, String pkCols)
      throws SQLException {
    this.tbName = tableName;
    this.colNames = colNames;
    this.pkCols = pkCols;
    allActiveTables.add(this);
  }

  public String getTableName() {
    return tbName;
  }

  public static void setConnection(boolean embed) {
    embedded = embed;

    try {
      if (embedded) {
        connection.close();
        SQLiteConfig config = new SQLiteConfig();
        config.enforceForeignKeys(true);
        connection = DriverManager.getConnection(dbPath, config.toProperties());
        TransferAllData();
      } else {
        connection.close();
        connection = DriverManager.getConnection(clientServerPath, "admin", "admin");
        TransferAllData();
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public boolean getEmbedded() {
    return embedded;
  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  public abstract ArrayList<T> readTable();

  // method to write objList to table
  public void writeTable() {

    for (T obj : objList) {

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
  public boolean editEntry(T1 pkid, String colName, Object value) {
    if (pkid instanceof ArrayList) {
      return editEntryComposite((ArrayList<Integer>) pkid, colName, value);
    }
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

  private boolean editEntryComposite(ArrayList<Integer> pkid, String colName, Object value) {
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
  public boolean deleteEntry(T1 pkid) {
    if (pkid instanceof ArrayList) {
      return deleteEntryComposite((ArrayList<Integer>) pkid);
    }
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
   * Removes the entry from the Object Request table
   *
   * @param pkid The Array List of the interger pkid
   * @return true if completed
   */
  private boolean deleteEntryComposite(ArrayList<Integer> pkid) {
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
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  public abstract boolean addEntry(T obj);

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
    //    objList = readTable();
    //
    //    try {
    //      PrintWriter print = new PrintWriter(f);
    //
    //      for (int i = 0; i < colNames.size() - 1; i++) {
    //        String s = colNames.get(i);
    //        print.write(s + ",");
    //      }
    //      print.write(colNames.get(colNames.size() - 1));
    //      print.write("\n");
    //
    //      for (Object c : objList) {
    //        print.println(c.toString());
    //        print.flush();
    //      }
    //      print.close();
    //
    //    } catch (Exception e) {
    //      e.printStackTrace(); // prints error messages in reverse time order
    //    }
  }

  /**
   * Loads a CSV file in to memory, parses to find the attributes of the objects stored in the table
   *
   * @param fileName location of the CSV file
   * @return arraylist containing n number of T objects, null if error
   */
  public abstract ArrayList<T> readBackup(String fileName);

  // drop current table and enter data from CSV
  public ArrayList<T> loadBackup(String fileName) {
    createTable();
    ArrayList<T> listObjs = readBackup(fileName);

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

  /** runs SQL commands to create the table in the hospitalData.db file */
  public abstract void createTable();

  // checks if an entry exists
  public boolean entryExists(T1 pkID) {
    if (pkID instanceof ArrayList) {
      return entryExistsComposite((ArrayList<Integer>) pkID);
    }
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
  };

  private boolean entryExistsComposite(ArrayList<Integer> pkid) {
    boolean exists = false;
    StringBuilder pkString = new StringBuilder();
    for (int i = 0; i < pkid.size() - 1; i++) {
      pkString.append(pkid.get(i)).append(",");
    }
    pkString.append(pkid.get(pkid.size() - 1));

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

  /**
   * Retrieves a specific object stored in the database
   *
   * @param pkID primary key value that identifies the desired object
   * @return object stored using PKID or null on error
   */
  public abstract T getEntry(T1 pkID);

  //  /**
  //   * set the objList to the list passed in only used for testing
  //   *
  //   * @param list
  //   * @return the new objList
  //   */
  //  public ArrayList<T> setObjList(ArrayList<T> list) {
  //    objList = list;
  //    return objList;
  //  }

  public void backUpAllTables() {
    File backupDir = new File("Backups");
    if (!backupDir.exists()) {
      backupDir.mkdir();
    }
    for (TableController table : allActiveTables) {
      table.createBackup(
          new File(backupDir.getAbsoluteFile().toString() + "/" + table.tbName + ".csv"));
    }
  }

  public void loadAllTables() {
    File backupDir = new File("Backups");
    if (!backupDir.exists()) {
      System.err.println("load dir fails");
      return;
    }
    for (TableController table : allActiveTables) {
      table.loadBackup(backupDir.getAbsoluteFile().toString() + "/" + table.tbName + ".csv");
    }
  }

  public static void TransferAllData() {
    for (TableController table : allActiveTables) {
      table.createTable();
      table.deleteTableData();
    }
    for (TableController table : allActiveTables) {
      TableController temp = table;
      if (temp.tbName.equals("Requests")) {
        continue;
      }
      for (Object entry : table.objList) {
        table.addEntry(entry);
      }
      table.readTable();
    }
  }

  private void deleteTableData() {
    PreparedStatement s = null;
    try {
      s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }
}
