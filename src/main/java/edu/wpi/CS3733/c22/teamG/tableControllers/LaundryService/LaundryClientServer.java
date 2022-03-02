package edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LaundryClientServer implements TableController<Laundry, Integer> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<Laundry> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public LaundryClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<Laundry> objList) throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  // reads the DB table and returns the information as a ArrayList<Laundry>
  public ArrayList<Laundry> readTable() {
    ArrayList tableInfo = new ArrayList<Laundry>();
    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new Laundry(r.getInt(1), r.getString(2), r.getString(3), r.getBoolean(4)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public void writeTable() {
    for (Laundry obj : objList) {
      this.addEntry(obj);
    }
  }

  public boolean deleteEntry(String pkid) {
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement("DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = ?;");
      s.setObject(1, pkid);
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return false;
  }

  public boolean addEntry(Laundry laundry) {
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement(
                  " IF NOT EXISTS (SELECT 1 FROM "
                      + tbName
                      + " WHERE "
                      + colNames.get(0)
                      + " = ?)"
                      + "BEGIN"
                      + "    INSERT INTO "
                      + tbName
                      + " VALUES (?, ?, ?, ?)"
                      + "end");

      s.setInt(1, laundry.getLaundryID());
      s.setInt(2, laundry.getLaundryID());
      s.setString(3, laundry.getLaundryType());
      s.setString(4, laundry.getDescription());
      s.setBoolean(5, laundry.getInStock());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public ArrayList<Laundry> readBackup(String fileName) {
    ArrayList<Laundry> laundryList = new ArrayList<Laundry>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("laundryID,laundryType,description,inStock"))) { // **
        System.err.println("laundry format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Laundry laun = // **
            new Laundry(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                Boolean.parseBoolean(element[3])); // **
        laundryList.add(laun); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return laundryList;
  }

  public void createTable() {
    try {
      Connection temp = ConnectionHandler.getInstance().getConnection();
      System.out.println(temp);
      PreparedStatement s1 =
          connectionHandler
              .getConnection()
              .prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }
      Statement s = connectionHandler.getConnection().createStatement();
      s.execute(
          "CREATE TABLE  Laundry(laundryID INTEGER NOT NULL ,laundryType TEXT, description TEXT, inStock BIT, PRIMARY KEY (laundryID));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Laundry getEntry(Integer pkID) {
    Laundry laundry = new Laundry(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connectionHandler
                .getConnection()
                .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next(); // **
        laundry.setLaundryID(r.getInt(1));
        laundry.setLaundryType(r.getString(2));
        laundry.setDescription(r.getString(3));
        laundry.setInStock(r.getBoolean(4));
        return laundry; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return laundry; // **
  }

  public boolean loadFromArrayList(ArrayList<Laundry> objList) {
    this.createTable();
    deleteTableData();
    for (Laundry lan : objList) {
      if (!this.addEntry(lan)) {
        return false;
      }
    }
    this.objList = readTable();
    return true;
  }

  private void deleteTableData() {
    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
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
  public boolean editEntry(Integer pkid, String colName, Object value) {
    try {

      PreparedStatement s =
          connectionHandler
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
    try {
      PreparedStatement s =
          connectionHandler
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
  public ArrayList<Laundry> loadBackup(String fileName) {
    createTable();
    ArrayList<Laundry> listObjs = readBackup(fileName);

    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("DELETE FROM " + tbName + ";");
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
    //    if (pkID instanceof ArrayList) {
    //      return entryExistsComposite((ArrayList<Integer>) pkID);
    //    }
    boolean exists = false;
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement(
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

  public ArrayList<Laundry> getObjList() {
    return objList;
  }
}
