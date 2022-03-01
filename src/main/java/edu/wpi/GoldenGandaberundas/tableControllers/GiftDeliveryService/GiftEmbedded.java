package edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService;

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

public class GiftEmbedded implements TableController<Gift, Integer> {
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Gift> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  GiftEmbedded(String tbName, String[] cols, String pkCols, ArrayList<Gift> objList) {
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  public ArrayList<Gift> readTable() {

    ArrayList<Gift> tableInfo = new ArrayList<>();
    try {
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();

      while (r.next()) {
        tableInfo.add(
            new Gift(r.getInt(1), r.getString(2), r.getString(3), r.getDouble(4), r.getBoolean(5)));
      }
      objList = tableInfo;
      return tableInfo;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean addEntry(Gift obj) {
    Gift gift = (Gift) obj;
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement(
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?);");

      s.setInt(1, gift.getGiftID());
      s.setString(2, gift.getGiftType());
      s.setString(3, gift.getDescription());
      s.setDouble(4, gift.getPrice());
      s.setBoolean(5, gift.getInStock());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Gift> readBackup(String fileName) {
    ArrayList<Gift> gifts = new ArrayList<>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file

      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("giftID,description,price,inStock"))) {
        System.err.println("gift backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Gift gift =
            new Gift(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                Double.parseDouble(element[3]),
                Boolean.parseBoolean(element[4]));
        gifts.add(gift); // adds the location to the list
        currentLine = buffer.readLine();
      }

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return gifts;
  }

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
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Gifts("
              + "giftID INTEGER NOT NULL ,"
              + "giftType TEXT NOT NULL, "
              + "description TEXT, "
              + "price DOUBLE NOT NULL, "
              + "inStock BOOLEAN NOT NULL, "
              + "PRIMARY KEY ('giftID'));");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Gift getEntry(Integer pkID) {
    Gift gift = new Gift();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        gift.setGiftID(r.getInt(1));
        gift.setGiftType(r.getString(2));
        gift.setDescription(r.getString(3));
        gift.setPrice(r.getDouble(4));
        gift.setInStock(r.getBoolean(5));
        return gift;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return gift;
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Gift> objList) {
    this.createTable();
    deleteTableData();
    for (Gift gift : objList) {
      if (!this.addEntry(gift)) {
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

    for (Gift obj : objList) {

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
  public ArrayList<Gift> loadBackup(String fileName) {
    createTable();
    ArrayList<Gift> listObjs = readBackup(fileName);

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

  public String getTableName() {
    return tbName;
  }

  public ArrayList<Gift> getObjList() {
    return objList;
  }
}
