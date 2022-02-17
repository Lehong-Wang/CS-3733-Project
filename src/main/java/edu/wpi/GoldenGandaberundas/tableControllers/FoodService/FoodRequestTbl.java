package edu.wpi.GoldenGandaberundas.tableControllers.FoodService;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class FoodRequestTbl extends TableController<FoodRequest, ArrayList<Integer>> {

  // **
  // created instance for singleton
  private static FoodRequestTbl instance = null;
  private static TableController<Request, Integer> masterTable = null;

  // **
  // created constructor fo the table
  private FoodRequestTbl() throws SQLException {
    super(
        "FoodRequests",
        Arrays.asList(new String[] {"reqID", "foodID", "quantity"}),
        "reqID,foodID");
    String[] cols = {"reqID", "foodID", "quantity"};
    masterTable = RequestTable.getInstance();
    createTable();

    objList = new ArrayList<FoodRequest>();
    objList = readTable();
  }

  // **
  // created getInstance method for singleton implementation
  public static FoodRequestTbl getInstance() {
    if (instance == null) {
      synchronized (TableController.class) {
        if (instance == null) {
          try {
            instance = new FoodRequestTbl();
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return instance;
  }

  // Creates readTable method and returns an array list of Food Requests
  @Override
  public ArrayList<FoodRequest> readTable() {
    ArrayList tableInfo = new ArrayList<FoodRequest>(); // **
    try {
      PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
      ResultSet r = s.executeQuery();
      while (r.next()) {
        tableInfo.add(new FoodRequest(masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getInt(3)));
      }
    } catch (SQLException se) {
      se.printStackTrace();
      return null;
    }
    objList = tableInfo;
    return tableInfo;
  }

  @Override
  public boolean addEntry(FoodRequest obj) {
    if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
      RequestTable.getInstance().addEntry(obj);
    }
    FoodRequest foodReq = (FoodRequest) obj; // **
    PreparedStatement s = null;
    try {
      s =
          connection.prepareStatement( // **
              "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

      s.setInt(1, obj.getRequestID());
      s.setInt(2, obj.getFoodID());
      s.setInt(3, obj.getQuantity());
      s.executeUpdate();
      return true;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public ArrayList<FoodRequest> readBackup(String fileName) {
    ArrayList<FoodRequest> fReqList = new ArrayList<>(); // **

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      System.out.println(currentLine);
      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(new String("reqID,foodID,quantity"))) { // **
        System.err.println("food request backup format not recognized"); // **
      }

      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        FoodRequest req = // **
            new FoodRequest(
                Integer.parseInt(element[0]),
                element[1],
                Integer.parseInt(element[2]),
                Integer.parseInt(element[3]),
                Long.parseLong(element[4]),
                Long.parseLong(element[5]),
                Integer.parseInt(element[6]),
                element[8],
                element[9],
                Integer.parseInt(element[10]),
                Integer.parseInt(element[11]));
        fReqList.add(req); // adds the location to the list
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return fReqList; // **
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
      s.execute("PRAGMA foreign_keys = ON"); // **
      s.execute(
          "CREATE TABLE IF NOT EXISTS  FoodRequests("
              + "reqID INTEGER NOT NULL, "
              + "foodID INTEGER NOT NULL, "
              + "quantity INTEGER NOT NULL, "
              + "CONSTRAINT FoodRequestPK PRIMARY KEY (reqID, foodID), "
              + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE, "
              + "CONSTRAINT FoodFK FOREIGN KEY (foodID) REFERENCES Food (foodID) "
              + "ON UPDATE CASCADE "
              + "ON DELETE CASCADE);");

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public FoodRequest getEntry(ArrayList<Integer> pkID) {
    FoodRequest foodReq = new FoodRequest();
    if (this.entryExists(pkID)) {
      try {
        PreparedStatement s =
            connection.prepareStatement(
                "SELECT * FROM " + tbName + " WHERE (" + pkCols + ") =(?,?);");
        s.setInt(1, pkID.get(0));
        s.setInt(2, pkID.get(1));
        ResultSet r = s.executeQuery();
        r.next();

        if (entryExists(pkID)) {
          foodReq.setRequestID(r.getInt(1));
          foodReq.setFoodID(r.getInt(2));
          foodReq.setQuantity(r.getInt(3));
        }
        return foodReq; // **
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
    return foodReq; // **
  }
}
