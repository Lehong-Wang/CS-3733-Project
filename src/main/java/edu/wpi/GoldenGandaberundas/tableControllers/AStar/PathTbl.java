package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.controllers.simulation.Simulation;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.*;

public class PathTbl extends TableController<Path, String> {

  private static PathTbl instance = null;
  /** name of table */
  protected String tbName;
  /** name of columns in database table the first entry is the primary key */
  protected List<String> colNames;
  /** list of keys that make a composite primary key */
  protected String pkCols = null;
  /** list that contains the objects stored in the database */
  protected ArrayList<Path> objList;
  /** relative path to the database file */
  private static HashMap<String, Integer> statsMap = new HashMap<>();


    ConnectionHandler connection = ConnectionHandler.getInstance();

    private PathTbl() throws SQLException {
    super("Paths", Arrays.asList(new String[] {"edgeID", "startNode", "endNode"}));
    String[] cols = {"edgeID", "startNode", "endNode"};
    createTable();

    objList = new ArrayList<Path>();
    objList = readTable();
  }

  // created getInstance method for singleton implementation
  public static PathTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new PathTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  @Override
  public ArrayList<Path> readTable() {

    ArrayList<Path> tableInfo = new ArrayList<>();
    try {
      PreparedStatement s = connection.prepareStatement("SELECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();

      while (r.next()) {
        tableInfo.add(new Path(r.getString(1), r.getString(2), r.getString(3)));
      }
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(Path obj) {
    if (!this.getEmbedded()) {
      return addEntryOnline(obj);
    }
    Path path = (Path) obj;
    PreparedStatement s = null;
    try {
      s = connection.prepareStatement("INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

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

  private boolean addEntryOnline(Path path) {
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
                  + " VALUES (?, ?, ?)"
                  + "end");

      s.setString(1, path.getEdgeID());
      s.setString(2, path.getStartNode());
      s.setString(3, path.getEndNode());
      s.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
    return true;
  }

  @Override
  public ArrayList<Path> readBackup(String fileName) {
    ArrayList<Path> paths = new ArrayList<>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file

      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("edgeID,startNode,endNode"))) {
        System.err.println("path backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        Path path = new Path(element[0], element[1], element[2]);
        paths.add(path); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return paths;
  }

  @Override
  public void createTable() {
    if (!this.getEmbedded()) {
      createTableOnline();
      return;
    }
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

  private void createTableOnline() {
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
          "CREATE TABLE  Paths("
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

  @Override
  public Path getEntry(String pkID) {
    Path path = new Path();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE " + colNames.get(0) + " =?;");
        s.setString(1, pkID);
        ResultSet r = s.executeQuery();
        r.next();
        path.setEdgeID(r.getString(1));
        path.setStartNode(r.getString(2));
        path.setEndNode(r.getString(3));
        return path;
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return path;
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

  /**
   * Takes in the name for the start and ending nodes and creates the appropriate a star path Keeps
   * Statistics using HashMap statsMap on Nodes visited
   *
   * @param start string of the starting node
   * @param end string of the ending node
   * @return list of Strings created from the a star path
   */
  public List<String> createAStarPathwStats(String start, String end) {
    List<String> path = createAStarPath(start, end);
    if (path != null) {
      for (String node : path) {
        statsMap.put(node, statsMap.get(node) + 1);
      }
      return path;
    } else {
      return null;
    }
  }

  /** Creates a HashMap to use for keeping node traversal statistics */
  public static void createStatsMap() {
    ArrayList<Location> allLocs = LocationTbl.getInstance().readTable();
    for (int i = 0; i < allLocs.size(); i++) {
      String loc = allLocs.get(i).getNodeID();
      statsMap.put(loc, 0);
    }
  }

  public static void printStatsMap() {
    for (String key : statsMap.keySet()) {
      System.out.println(key + " = " + statsMap.get(key));
    }
  }

  @Override
  public void writeTable() {

    for (Path obj : objList) {

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
  public boolean editEntry(String pkid, String colName, Object value) {
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

  /**
  * removes a row from the database
  *
  * @param pkid primary key of row to be removed
  * @return true if successful, false otherwise
  */
 public boolean deleteEntry(String pkid) {
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
   /*
   * Returns the starting and ending points of a MedEquip in the SimulationList
   *
   * @param medID - Piece of Equipment
   * @param hour - Beginning hour of path
   * @return List<String> where index 0 = starting point, index 1 = ending point
   */
  public static List<String> getPathPoints(int medID, int hour) {
    List<String> retVal = new ArrayList<>();
    retVal.add(0, Simulation.pathList[medID][hour]);
    retVal.add(1, Simulation.pathList[medID][hour + 1]);
    //    System.out.println("Eqp #" + medID + ": Starts: " + retVal.get(0) + " Ends: " +
    // retVal.get(1));
    MedEquipmentTbl.getInstance().editEntry(medID, "currLoc", retVal.get(1));
    return retVal;
  }

  /**
   * Returns the starting and ending points of a MedEquip in the SimulationList for a longer
   * duration
   *
   * @param medID - Piece of Equipment
   * @param hour - Beginning time of path
   * @param fasterHour - End of lonnher duration time
   * @return List<String> where index 0 = starting point, index 1 = ending point
   */
  public static List<String> getPathPointsFaster(int medID, int hour, int fasterHour) {
    List<String> retVal = new ArrayList<>();
    retVal.add(0, Simulation.pathList[medID][hour]);
    retVal.add(1, Simulation.pathList[medID][fasterHour]);
    MedEquipmentTbl.getInstance().editEntry(medID, "currLoc", retVal.get(1));
    return retVal;
  }

  public String getTableName() {
    return tbName;
  }
}
