package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery;

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

public class MedEquipmentClientServer implements TableController<MedEquipment, Integer> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<MedEquipment> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  Connection connection = connectionHandler.getConnection();

  public MedEquipmentClientServer(
      String tbName, String[] cols, String pkCols, ArrayList<MedEquipment> objList)
      throws SQLException {
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(new String[] {"medID", "medEquipmentType", "status", "currLoc"});
    // createTable();

    this.objList = new ArrayList<MedEquipment>();
    this.objList = readTable();
  }

  @Override
  public ArrayList readTable() {
    ArrayList medEquipments = new ArrayList<MedEquipment>();
    try { // if code works, do this:
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      // s.setString(1, tbName);
      ResultSet r = s.executeQuery();
      while (r.next()) {
        medEquipments.add(
            new MedEquipment(r.getInt(1), r.getString(2), r.getString(3), r.getString(4)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    return medEquipments;
  }

  public boolean deleteEntry(Integer pkid) {
    try {
      PreparedStatement s =
          connection.prepareStatement(
              "DELETE FROM " + tbName + " WHERE " + colNames.get(0) + " = " + (int) pkid + ";");
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public boolean addEntry(MedEquipment med) {
    try {
      PreparedStatement s =
          connection.prepareStatement(
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
      s.setInt(1, med.getMedID());
      s.setInt(2, med.getMedID());
      s.setString(3, med.getMedEquipmentType());
      s.setString(4, med.getStatus());
      s.setString(5, med.getCurrLoc().trim().toUpperCase(Locale.ROOT));
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      System.out.println(med.getCurrLoc());
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<MedEquipment> readBackup(String fileName) {
    ArrayList<MedEquipment> med = new ArrayList<>(); // creates a list for the location objects
    File csvFile = new File(fileName);
    try {
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        MedEquipment currentMed = this.createMedEquipment(element); // creates a Location
        med.add(currentMed); // adds the location to the list
        currentLine = buffer.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return med;
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
          "CREATE TABLE  MedEquipment("
              + "medID INTEGER NOT NULL, "
              + "medEquipmentType TEXT NOT NULL, "
              + "status TEXT NOT NULL, "
              + "currLoc varchar(50) NOT NULL, "
              + "PRIMARY KEY (medID),"
              + "CONSTRAINT nodeID FOREIGN KEY (currLoc) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE"
              + ");");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public MedEquipment getEntry(Integer pkid) {
    MedEquipment med = new MedEquipment();
    try {
      if (this.entryExists(pkid)) {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " = " + pkid + ";");
        ResultSet r = s.executeQuery();
        r.next();
        med.setMedID(pkid);
        med.setMedEquipmentType(r.getString(2));
        med.setStatus(r.getString(3));
        med.setCurrLoc(r.getString(4));
        return med;
      } else {
        return null;
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedEquipment> objList) {
    return false;
  }

  public MedEquipment createMedEquipment(String[] ele) {
    MedEquipment med = new MedEquipment(Integer.parseInt(ele[0]), ele[1], ele[2], ele[3]);
    return med;
  }

  public void writeTable() {

    for (MedEquipment obj : objList) {

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
  public ArrayList<MedEquipment> loadBackup(String fileName) {
    createTable();
    ArrayList<MedEquipment> listObjs = readBackup(fileName);

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

  public ArrayList<MedEquipment> getObjList() {
    return objList;
  }
}
