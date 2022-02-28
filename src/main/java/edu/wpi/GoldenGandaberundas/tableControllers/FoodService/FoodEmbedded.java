package edu.wpi.GoldenGandaberundas.tableControllers.FoodService;

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

public class FoodEmbedded implements TableController<Food, Integer> {
  private static edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl instance =
      null; // DAO
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Food> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public FoodEmbedded(String tbName, String[] cols, String pkCols, ArrayList<Food> objList) {
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  @Override
  public ArrayList<Food> readTable() { // **
    ArrayList tableInfo = new ArrayList<Food>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Food( // **
                r.getInt(1),
                r.getString(2),
                r.getString(3),
                r.getInt(4),
                r.getString(5),
                r.getDouble(6),
                r.getBoolean(7),
                r.getString(8)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(Food obj) {
    Food med = (Food) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

      // **
      s.setInt(1, med.getFoodID());
      s.setString(2, med.getFoodName());
      s.setString(3, med.getIngredients());
      s.setInt(4, med.getCalories());
      s.setString(5, med.getAllergens());
      s.setDouble(6, med.getPrice());
      s.setBoolean(7, med.getInStock());
      s.setString(8, med.getFoodType());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<Food> readBackup(String fileName) {
    ArrayList<Food> medList = new ArrayList<Food>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("foodID,description,price,inStock,foodType"))) { // **
        System.err.println("Food backup format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Food med = // **
            new Food(
                Integer.parseInt(element[0]),
                element[1],
                element[2],
                Integer.parseInt(element[3]),
                element[4],
                Double.parseDouble(element[5]),
                Boolean.parseBoolean(element[6]),
                element[7]); // **
        medList.add(med); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return medList; // **
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
      s.execute("PRAGMA foreign_keys = ON");
      s.execute(
          "CREATE TABLE IF NOT EXISTS  Food("
              + "foodID INTEGER NOT NULL ,"
              + "foodName TEXT NOT NULL, "
              + "ingredients TEXT, "
              + "calories INTEGER, "
              + "allergens TEXT, "
              + "price DOUBLE NOT NULL, "
              + "inStock BOOLEAN NOT NULL, "
              + "foodType TEXT NOT NULL, "
              + "PRIMARY KEY ('foodID'), "
              + "CONSTRAINT foodTypeEnum CHECK(foodType in('Entree','Side','Drink','Dessert')));");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public Food getEntry(Integer pkID) { // **
    Food med = new Food(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setInt(1, pkID); // **
        ResultSet r = s.executeQuery();
        r.next();
        med.setFoodID(r.getInt(1));
        med.setFoodName(r.getString(2));
        med.setIngredients(r.getString(3));
        med.setCalories(r.getInt(4));
        med.setAllergens(r.getString(5));
        med.setPrice(r.getDouble(6));
        med.setInStock(r.getBoolean(7));
        med.setFoodType(r.getString(8));
        System.out.println(med);
        return med;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return med; // **
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Food> objList) {
    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
      this.objList = objList;
      this.writeTable();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  public void writeTable() {

    for (Food obj : objList) {

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
  public ArrayList<Food> loadBackup(String fileName) {
    createTable();
    ArrayList<Food> listObjs = readBackup(fileName);

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

  public String getTableName() {
    return tbName;
  }

  public ArrayList<Food> getObjList() {
    return objList;
  }
}
