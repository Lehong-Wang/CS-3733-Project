package edu.wpi.GoldenGandaberundas;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;

public interface TableController<T, T1> {
  //  protected TableController(String tableName, List<String> colNames) throws SQLException {
  //  }
  //
  //  protected TableController(String tableName, List<String> colNames, String pkCols){
  //  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  public abstract ArrayList<T> readTable();

  // method to write objList to table
  public abstract void writeTable();

  public ArrayList<T> getObjList();
  /*
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

  */

  public boolean deleteEntry(T1 pkid);

  /**
   * Removes the entry from the Object Request table
   *
   * @param pkid The Array List of the interger pkid
   * @return true if completed
   */
  /*
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

  */

  /**
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  public boolean addEntry(T obj);

  public void createBackup(File f);

  /**
   * Loads a CSV file in to memory, parses to find the attributes of the objects stored in the table
   *
   * @param fileName location of the CSV file
   * @return arraylist containing n number of T objects, null if error
   */
  public ArrayList<T> readBackup(String fileName);

  public ArrayList<T> loadBackup(String fileName);

  public boolean editEntry(T1 pkid, String colName, Object value);

  /** runs SQL commands to create the table in the hospitalData.db file */
  public void createTable();

  public boolean entryExists(T1 pkID);

  /*
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
  */

  /**
   * Retrieves a specific object stored in the database
   *
   * @param pkID primary key value that identifies the desired object
   * @return object stored using PKID or null on error
   */
  public T getEntry(T1 pkID);

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

  //  public void backUpAllTables() {
  //    File backupDir = new File("Backups");
  //    if (!backupDir.exists()) {
  //      backupDir.mkdir();
  //    }
  //    for (TableController table : allActiveTables) {
  //      table.createBackup(
  //          new File(backupDir.getAbsoluteFile().toString() + "/" + table.tbName + ".csv"));
  //    }
  //  }

  //  public void loadAllTables() {
  //    File backupDir = new File("Backups");
  //    if (!backupDir.exists()) {
  //      System.err.println("load dir fails");
  //      return;
  //    }
  //    for (TableController table : allActiveTables) {
  //      table.loadBackup(backupDir.getAbsoluteFile().toString() + "/" + table.tbName + ".csv");
  //    }
  //  }

  public boolean loadFromArrayList(ArrayList<T> objList);
}
