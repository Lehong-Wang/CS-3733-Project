package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.io.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class PathTbl extends TableController<Path, String> {

  private static PathTbl instance = null;

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
}