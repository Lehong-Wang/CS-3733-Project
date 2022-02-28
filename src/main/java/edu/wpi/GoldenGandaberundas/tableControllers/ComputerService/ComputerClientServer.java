package edu.wpi.GoldenGandaberundas.tableControllers.ComputerService;

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

public class ComputerClientServer implements TableController<Computer, Integer> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<Computer> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public ComputerClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<Computer> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  // reads the DB table and returns the information as a ArrayList<Computer>
  public ArrayList<Computer> readTable() {
    ArrayList tableInfo = new ArrayList<Computer>();
    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Computer(
                r.getInt(1),
                r.getString(2),
                r.getString(3),
                r.getString(4),
                r.getString(5),
                r.getString(6),
                r.getString(7),
                r.getString(8)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  public void writeTable() {
    for (Computer obj : objList) {
      this.addEntry(obj);
    }
  }

  public boolean addEntry(Computer computer) {
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
                      + " VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
                      + "end");

      s.setInt(1, computer.getComputerID());
      s.setInt(2, computer.getComputerID());
      s.setString(3, computer.getComputerType());
      s.setString(4, computer.getOs());
      s.setString(5, computer.getProcessor());
      s.setString(6, computer.getHostName());
      s.setString(7, computer.getModel());
      s.setString(8, computer.getManufacturer());
      s.setString(9, computer.getSerialNumber());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public ArrayList<Computer> readBackup(String fileName) {
    ArrayList<Computer> computerList = new ArrayList<Computer>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("computerID,computerType,description,inStock"))) { // **
        System.err.println("computer format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Computer computer = // **
            new Computer(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                element[3],
                element[4],
                element[5],
                element[6],
                element[7]); // **
        computerList.add(computer); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return computerList;
  }

  public void createTable() {
    try {

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
          "CREATE TABLE  Computer("
              + "computerID INTEGER NOT NULL, "
              + "computerType TEXT NOT NULL , "
              + "os TEXT NOT NULL , "
              + "processor TEXT NOT NULL , "
              + "hostName TEXT NOT NULL , "
              + "model TEXT NOT NULL , "
              + "manufacturer TEXT NOT NULL , "
              + "serialNumber TEXT NOT NULL , "
              + "PRIMARY KEY (computerID));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Computer getEntry(Integer pkID) {
    Computer computer = new Computer(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connectionHandler
                .getConnection()
                .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next(); // **
        computer.setComputerID(r.getInt(1));
        computer.setComputerType(r.getString(2));
        computer.setOs(r.getString(3));
        computer.setProcessor(r.getString(4));
        computer.setHostName(r.getString(5));
        computer.setModel(r.getString(6));
        computer.setManufacturer(r.getString(7));
        computer.setSerialNumber(r.getString(8));
        return computer; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return computer; // **
  }

  public boolean loadFromArrayList(ArrayList<Computer> objList) {
    this.createTable();
    deleteTableData();
    for (Computer comp : objList) {
      if (!this.addEntry(comp)) {
        return false;
      }
    }
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
  public ArrayList<Computer> loadBackup(String fileName) {
    createTable();
    ArrayList<Computer> listObjs = readBackup(fileName);

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

  public ArrayList<Computer> getObjList() {
    return objList;
  }
}
