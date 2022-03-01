package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

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

public class CredentialsClientServer implements TableController<Credential, Integer> {
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Credential> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public CredentialsClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<Credential> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing

    // createTable();
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<Credential> readTable() {
    ArrayList tableInfo = new ArrayList<Credential>();
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new Credential(r.getInt(1), r.getString(2), r.getString(3)));
      }
      objList = tableInfo;
      return tableInfo;
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean addEntry(Credential obj) {
    if (!entryExists(obj.getEmpID())) {
      try {
        PreparedStatement statement =
            connection.prepareStatement(
                " IF NOT EXISTS (SELECT 1 FROM "
                    + tbName
                    + " WHERE "
                    + colNames.get(0)
                    + " = ?)"
                    + "BEGIN"
                    + "    INSERT INTO "
                    + tbName
                    + " VALUES (?, ?, ?)"
                    + "end");
        statement.setInt(1, obj.getEmpID());
        statement.setInt(2, obj.getEmpID());
        statement.setString(3, obj.getHashedPass());
        statement.setString(4, obj.getSalt());
        statement.executeUpdate();
        return true;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return false;
  }

  @Override
  public ArrayList<Credential> readBackup(String fileName) {
    ArrayList<Credential> credList = new ArrayList<>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine.toLowerCase(Locale.ROOT).trim().equals(new String("empID,password,salt"))) {
        System.err.println("credential backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Credential cred = new Credential(Integer.parseInt(element[0]), element[1], element[2]);
        credList.add(cred); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return credList;
  }

  @Override
  public void createTable() {

    try {
      PreparedStatement s1 =
          connection.prepareStatement("SELECT count(*) FROM sys.tables WHERE name = ?;");
      s1.setString(1, tbName);
      ResultSet r = s1.executeQuery();
      r.next();
      if (r.getInt(1) != 0) {
        return;
      }

      Statement s = connection.createStatement();
      s.execute(
          "CREATE TABLE Credentials("
              + "empID INTEGER NOT NULL, "
              + "password TEXT NOT NULL, "
              + "salt TEXT NOT NULL, "
              + "PRIMARY KEY(empID), "
              + "CONSTRAINT CredFK FOREIGN KEY (empID) REFERENCES Employees(empID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE "
              + ");");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * returns specific entry from the table to be used by UI when loading the whole table into memory
   * is unnecessary
   *
   * @param pkID
   * @return credential from matching email or null if it does not exist
   */
  @Override
  public Credential getEntry(Integer pkID) {
    Integer empID;
    String hashedPass;
    String salt;
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        empID = (r.getInt(1));
        hashedPass = (r.getString(2));
        salt = (r.getString(3));

        return new Credential(empID, hashedPass, salt);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return null;
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Credential> objList) {
    connection = ConnectionHandler.getInstance().getConnection();
    this.createTable();
    deleteTableData();
    for (Credential cre : objList) {
      if (!this.addEntry(cre)) {
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

  public void writeTable() {
    for (Credential obj : objList) {
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
  public boolean editEntry(Integer pkid, String colName, Object value) {
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
  public boolean deleteEntry(Integer pkid) {
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
  public ArrayList<Credential> loadBackup(String fileName) {
    createTable();
    ArrayList<Credential> listObjs = readBackup(fileName);

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
  public boolean entryExists(Integer pkID) {
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
  }

  public ArrayList<Credential> getObjList() {
    return objList;
  }
}
