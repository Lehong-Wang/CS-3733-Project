package edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService;

import edu.wpi.GoldenGandaberundas.ConnectionType;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class GiftRequestTbl extends TableController<GiftRequest, ArrayList<Integer>> {

    // **
    // created instance for singleton
    private static GiftRequestTbl instance = null;
    private static TableController<Request, Integer> masterTable = null;

    // **
    // created constructor fo the table
    private GiftRequestTbl() throws SQLException {
        super("GiftRequests", Arrays.asList("reqID", "giftID", "quantity"), "reqID, giftID");
        String[] cols = {"reqID", "giftID", "quantity"};
        masterTable = RequestTable.getInstance();
        createTable();

        objList = new ArrayList<GiftRequest>();
        objList = readTable();
    }

    // **
    // created getInstance method for singleton implementation
    public static GiftRequestTbl getInstance() {
        if (instance == null) {
            synchronized (TableController.class) {
                if (instance == null) {
                    try {
                        instance = new GiftRequestTbl();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return instance;
    }

    // Creates readTable method and returns an array list of Gift Requests
    @Override
    public ArrayList<GiftRequest> readTable() {
        ArrayList tableInfo = new ArrayList<GiftRequest>(); // **
        try {
            PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
            ResultSet r = s.executeQuery();
            while (r.next()) {
                System.out.println(masterTable.getEntry(r.getInt(1)));
                tableInfo.add(
                        new GiftRequest( // **
                                masterTable.getEntry(r.getInt(1)), r.getInt(2), r.getInt(3)));
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
        return tableInfo;
    }

    @Override
    public boolean addEntry(GiftRequest obj) {
        if (!RequestTable.getInstance().entryExists(obj.getRequestID())) {
            RequestTable.getInstance().addEntry(obj);
        }
        if (TableController.getConnectionType() == ConnectionType.clientServer) {
            return addEntryOnline(obj);
        }

        GiftRequest giftReq = (GiftRequest) obj; // **
        PreparedStatement s = null;
        try {
            s =
                    connection.prepareStatement( // **
                            "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?);");

            s.setInt(1, obj.getRequestID());
            s.setInt(2, obj.getGiftID());
            s.setInt(3, obj.getQuantity());
            s.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("LINE NUM: " + e.getStackTrace()[0].getLineNumber());
            e.printStackTrace();
            return false;
        }
    }

    private boolean addEntryOnline(GiftRequest gift) {
        try {
            PreparedStatement s =
                    connection.prepareStatement(
                            " IF NOT EXISTS (SELECT 1 FROM "
                                    + tbName
                                    + " WHERE "
                                    + colNames.get(0)
                                    + " = ?"
                                    + " AND "
                                    + colNames.get(1)
                                    + " = ?"
                                    + ")"
                                    + "BEGIN"
                                    + "    INSERT INTO "
                                    + tbName
                                    + " VALUES (?, ?, ?)"
                                    + "end");

            s.setInt(1, gift.getRequestID());
            s.setInt(2, gift.getGiftID());
            s.setInt(3, gift.getRequestID());
            s.setInt(4, gift.getGiftID());
            s.setInt(5, gift.getQuantity());
            s.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<GiftRequest> readBackup(String fileName) {
        ArrayList<GiftRequest> gReqList = new ArrayList<GiftRequest>(); // **

        try {
            File csvFile = new File(fileName);
            BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
            String currentLine = buffer.readLine(); // reads a line from the csv file
            System.out.println(currentLine);

            currentLine = buffer.readLine();

            while (currentLine != null) { // cycles in the while loop until it reaches the end
                String[] element = currentLine.split(","); // separates each element based on a comma
                GiftRequest req = // **
                        new GiftRequest(
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
                gReqList.add(req); // adds the location to the list
                currentLine = buffer.readLine();
            }
            ; // creates a Location

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return gReqList; // **
    }

    @Override
    public boolean createTable() {
        if (TableController.getConnectionType() == ConnectionType.clientServer) {
            return createTableOnline();

        }
        try {
            PreparedStatement s =
                    connection.prepareStatement(
                            "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
            s.setString(1, tbName);
            ResultSet r = s.executeQuery();
            r.next();
            if (r.getInt(1) != 0) {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
            e.printStackTrace();
            return false;
        }

        System.out.println("SQLite driver registered!");

        Statement s = null;
        try {
            s = connection.createStatement();
            s.execute("PRAGMA foreign_keys = ON"); // **
            s.execute(
                    "CREATE TABLE IF NOT EXISTS  GiftRequests("
                            + "reqID INTEGER NOT NULL, "
                            + "giftID integer, "
                            + "quantity integer, "
                            + "CONSTRAINT GiftRequestPK PRIMARY KEY (reqID, giftID), "
                            + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
                            + "ON UPDATE CASCADE "
                            + "ON DELETE CASCADE, "
                            + "CONSTRAINT GiftFK FOREIGN KEY (giftID) REFERENCES Gifts (giftID) "
                            + "ON UPDATE CASCADE "
                            + "ON DELETE SET NULL);");
            this.writeTable();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean createTableOnline() {
        try {

            PreparedStatement s1 =
                    connection.prepareStatement("SELECT COUNT(*) FROM sys.tables WHERE name = ?;");
            s1.setString(1, tbName);
            ResultSet r = s1.executeQuery();
            r.next();
            if (r.getInt(1) != 0) {
                return false;
            }
            Statement s = connection.createStatement();
            s.execute(
                    "CREATE TABLE  GiftRequests("
                            + "reqID INTEGER NOT NULL, "
                            + "giftID INTEGER NOT NULL, "
                            + "quantity INTEGER, "
                            + "CONSTRAINT GiftRequestPK PRIMARY KEY (reqID, giftID), "
                            + "CONSTRAINT RequestFK FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
                            + "ON UPDATE CASCADE "
                            + "ON DELETE CASCADE, "
                            + "CONSTRAINT GiftFK FOREIGN KEY (giftID) REFERENCES Gifts (giftID) "
                            + "ON UPDATE CASCADE "
                            + "ON DELETE CASCADE);");
            this.writeTable();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public GiftRequest getEntry(ArrayList<Integer> pkID) {
        GiftRequest gift = new GiftRequest();
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
                    gift.setRequestID(r.getInt(1));
                    gift.setGiftID(r.getInt(2));
                    gift.setQuantity(r.getInt(3));
                }
                return gift; // **
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return gift; // **
    }
}
