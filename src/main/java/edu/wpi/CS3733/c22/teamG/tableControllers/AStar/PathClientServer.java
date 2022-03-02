package edu.wpi.CS3733.c22.teamG.tableControllers.AStar;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class PathClientServer implements TableController<Path, String> {
  /** name of table */
  private String tbName;
  /** name of columns in database table the first entry is the primary key */
  private List<String> colNames;
  /** list of keys that make a composite primary key */
  private String pkCols = null;
  /** list that contains the objects stored in the database */
  private ArrayList<Path> objList;
  /** relative path to the database file */
  ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

  public PathClientServer(String tbName, String[] cols, String pkCols, ArrayList<Path> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  // reads the DB table and returns the information as a ArrayList<Path>
  public ArrayList<Path> readTable() {
    ArrayList tableInfo = new ArrayList<Path>();
    try {
      PreparedStatement s =
          connectionHandler.getConnection().prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new Path(r.getString(1), r.getString(2), r.getString(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  public void writeTable() {
    for (Path obj : objList) {
      this.addEntry(obj);
    }
  }

  public boolean addEntry(Path path) {
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
                      + " VALUES (?, ?, ?)"
                      + "end");

      s.setString(1, path.getEdgeID());
      s.setString(2, path.getEdgeID());
      s.setString(3, path.getStartNode());
      s.setString(4, path.getEndNode());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  public ArrayList<Path> readBackup(String fileName) {
    ArrayList<Path> pathList = new ArrayList<Path>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("pathID,pathType,description,inStock"))) { // **
        System.err.println("path format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Path path = // **
            new Path(element[0], element[1], element[2]); // **
        pathList.add(path); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return pathList;
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
          "CREATE TABLE  Paths("
              + "edgeID varchar(101) NOT NULL ,"
              + "startNode varchar(50) NOT NULL, "
              + "endNode varchar(50) NOT NULL, "
              + "CONSTRAINT PathPk PRIMARY KEY (edgeID), "
              + "CONSTRAINT PathNodeID FOREIGN KEY (startNode) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT PathNodeID2 FOREIGN KEY (endNode) REFERENCES Locations (nodeID));");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Path getEntry(String pkID) {
    Path path = new Path(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connectionHandler
                .getConnection()
                .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setString(1, pkID);
        ResultSet r = s.executeQuery();
        r.next(); // **
        path.setEdgeID(r.getString(1));
        path.setStartNode(r.getString(2));
        path.setEndNode(r.getString(3));
        return path; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return path; // **
  }

  public boolean loadFromArrayList(ArrayList<Path> objList) {
    this.createTable();
    deleteTableData();
    for (Path comp : objList) {
      if (!this.addEntry(comp)) {
        return false;
      }
    }
    this.objList = this.readTable();

    return true;
  }

  /**
   * searched the path table and returns and creates a list of path points
   *
   * @param startNode finds the starting node
   * @return update list of the string nodes
   */
  public ArrayList<String> getConnectedPoints(String startNode) {
    ArrayList<String> connect = new ArrayList<>();
    try {
      PreparedStatement s =
          connectionHandler
              .getConnection()
              .prepareStatement("SELECT * FROM " + tbName + " WHERE " + colNames.get(1) + " =?;");
      s.setString(1, startNode);
      ResultSet r = s.executeQuery();
      while (r.next()) {
        connect.add(r.getString(3));
      }

      return connect;
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connect;
  }

  /**
   * takes in the path points and creates the proper branches for the points
   *
   * @param points the lsit of every point in the path list
   * @return updates points list
   */
  public ArrayList<Point> createBranchedLocations(ArrayList<Point> points) {

    for (Point p : points) {
      ArrayList<String> branched = getConnectedPoints(p.loc);
      for (String b : branched) {
        for (Point o : points) {
          if (b.equals(o.loc)) {
            p.addBranch(o);
            break;
          }
        }
      }
    }
    return points;
  }

  /**
   * Takes in the name for the start and ending nodes and creates the appropriate a star path
   *
   * @param start string of the starting node
   * @param end string of the ending node
   * @return list of Strings created from the a star path
   */
  public List<String> createAStarPath(String start, String end) {
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    if (points.size() != 0) {

      points = PathTbl.getInstance().createBranchedLocations(points);

      int startID = 0;
      int endID = 0;
      for (Point o : points) {
        if (o.loc.equals(start)) {
          startID = points.indexOf(o);
        }
        if (o.loc.equals(end)) {
          endID = points.indexOf(o);
        }
      }
      points.get(startID).g = 0;
      Point test = points.get(startID).aStar(points.get(endID));
      return points.get(startID).locationsPath(test);
    }
    return null;
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
  public boolean editEntry(String pkid, String colName, Object value) {
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
  public ArrayList<Path> loadBackup(String fileName) {
    createTable();
    ArrayList<Path> listObjs = readBackup(fileName);

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
  public boolean entryExists(String pkID) {
    //    if (pkID instanceof ArrayList) {
    //      return entryExistsComposite((ArrayList<String>) pkID);
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

  public ArrayList<Path> getObjList() {
    return objList;
  }
}
