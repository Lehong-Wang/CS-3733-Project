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

public class PathEmbedded implements TableController<Path, String> {
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

  Connection connection = connectionHandler.getConnection();

  public PathEmbedded(String tbName, String[] cols, String pkCols, ArrayList<Path> objList)
      throws SQLException {
    // create a new table with column names if none table of same name exist
    // if there is one, do nothing
    this.tbName = tbName;
    this.pkCols = pkCols;
    colNames = Arrays.asList(cols);
    this.objList = objList;
  }

  public ArrayList<Path> readTable() {
    ArrayList tableInfo = new ArrayList<Path>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(
            new Path( // **
                r.getString(1), r.getString(2), r.getString(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  public boolean addEntry(Path obj) {
    Path path = (Path) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

      // **
      s.setString(1, path.getEdgeID());
      s.setString(2, path.getStartNode());
      s.setString(3, path.getEndNode());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
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
          "CREATE TABLE IF NOT EXISTS  Paths("
              + "edgeID TEXT NOT NULL ,"
              + "startNode TEXT NOT NULL, "
              + "endNode TEXT NOT NULL, "
              + "CONSTRAINT PathPk PRIMARY KEY ('edgeID'), "
              + "CONSTRAINT PathFk1 FOREIGN KEY (startNode) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE, "
              + "CONSTRAINT PathFk2 FOREIGN KEY (endNode) REFERENCES Locations (nodeID) "
              + " ON UPDATE CASCADE "
              + " ON DELETE CASCADE );");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public Path getEntry(String pkID) {
    Path path = new Path(); // **
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
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

  private void deleteTableData() {
    try {
      PreparedStatement s = connection.prepareStatement("DELETE FROM " + tbName + ";");
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
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
          connection.prepareStatement(
              "SELECT * FROM " + tbName + " WHERE " + colNames.get(1) + " =?;");
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

  public void writeTable() {

    for (Path obj : objList) {

      this.addEntry(obj);
    }
  }

  public boolean editEntry(String pkid, String colName, Object value) {
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
  public boolean deleteEntry(String pkid) {
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

  /**
   * Loads a CSV file in to memory, parses to find the attributes of the objects stored in the table
   *
   * @param fileName location of the CSV file
   * @return arraylist containing n number of T objects, null if error
   */
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
        System.err.println("Path format not recognized"); // **
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Path comp = // **
            new Path(element[0], element[1], element[2]); // **
        pathList.add(comp); // adds the location to the list
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

  // drop current table and enter data from CSV
  public ArrayList<Path> loadBackup(String fileName) {
    createTable();
    ArrayList<Path> listObjs = readBackup(fileName);

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
  public boolean entryExists(String pkID) {
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

  public ArrayList<Path> getObjList() {
    return objList;
  }
}
