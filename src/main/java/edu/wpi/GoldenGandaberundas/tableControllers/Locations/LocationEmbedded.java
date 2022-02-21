package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class LocationEmbedded implements TableController<Location, String> {
    /** name of table */
    private String tbName;
    /** name of columns in database table the first entry is the primary key */
    private List<String> colNames;
    /** list of keys that make a composite primary key */
    private String pkCols = null;
    /** list that contains the objects stored in the database */
    private ArrayList<Location> objList;
    /** relative path to the database file */


    ConnectionHandler connectionHandler = ConnectionHandler.getInstance();
    Connection connection = connectionHandler.getConnection();


    private LocationEmbedded(String tbName, String[] cols, String pkCols, ArrayList<Location> objList) throws SQLException {
        // create a new table with column names if none table of same name exist
        // if there is one, do nothing
        createTable();
        this.tbName = tbName;
        this.pkCols = pkCols;
        colNames = Arrays.asList(cols);
        objList = readTable();
    }

    @Override
    public ArrayList<Location> readTable() {
        ArrayList tableInfo = new ArrayList<Location>();
        try {
            PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
            ResultSet r = s.executeQuery();
            while (r.next()) {
                tableInfo.add(
                        new Location(
                                r.getString(1),
                                r.getInt(2),
                                r.getInt(3),
                                r.getString(4),
                                r.getString(5),
                                r.getString(6),
                                r.getString(7),
                                r.getString(8)));
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
        return tableInfo;
    }

    @Override
    public void writeTable() {

    }

    @Override
    public boolean deleteEntry(String pkid) {
        return false;
    }

    @Override
    public boolean addEntry(Location loc) {
        if (entryExists(loc.getNodeID())) {
            System.out.println("loc ID exists: " + loc.getNodeID());

        }
        else {
            PreparedStatement s = null;
            try {
                // check if exists and add to DB
                s =
                        connection.prepareStatement(
                                "INSERT OR IGNORE INTO " + tbName + " VALUES (?, ?, ?, ?, ?, ?, ?, ?);");

                s.setString(1, loc.getNodeID());
                s.setInt(2, loc.getXcoord());
                s.setInt(3, loc.getYcoord());
                s.setString(4, loc.getFloor());
                s.setString(5, loc.getBuilding());
                s.setString(6, loc.getNodeType());
                s.setString(7, loc.getLongName());
                s.setString(8, loc.getShortName());
                s.executeUpdate();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    @Override
    public void createBackup(File f) {

    }

    @Override
    public ArrayList<Location> readBackup(String fileName) {
        ArrayList<Location> locList = new ArrayList<>();

        try {
            File csvFile = new File(fileName);
            BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
            String currentLine = buffer.readLine(); // reads a line from the csv file
            //      System.out.println(currentLine);
            if (!currentLine
                    .toLowerCase(Locale.ROOT)
                    .trim()
                    .equals(new String("nodeID,xcoord,ycoord,floor,building,nodeType,longName,shortName"))) {
                System.err.println("location backup format not recognized");
            }
            currentLine = buffer.readLine();

            while (currentLine != null) { // cycles in the while loop until it reaches the end
                if (!currentLine.isEmpty()) { // check if a line is blank
                    String[] element = currentLine.split(","); // separates each element based on a comma
                    Location loc =
                            new Location(
                                    element[0],
                                    Integer.parseInt(element[1]),
                                    Integer.parseInt(element[2]),
                                    element[3],
                                    element[4],
                                    element[5],
                                    element[6],
                                    element[7]);
                    locList.add(loc); // adds the location to the list
                    //          System.out.println(loc.toString());
                }
                currentLine = buffer.readLine();
            }
            // creates a Location

        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return locList;
    }

    @Override
    public ArrayList<Location> loadBackup(String fileName) {
        createTable();
        ArrayList<Location> listObjs = readBackup(fileName);

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

    @Override
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

    @Override
    public void createTable() {
        try {
            // check if there is table with same name
            PreparedStatement s =
                    connection.prepareStatement(
                            "SELECT count(*) FROM sqlite_master WHERE tbl_name = ? LIMIT 1;");
            s.setString(1, tbName);
            ResultSet r = s.executeQuery();
            r.next();
            // if there is already table, exit
            if (r.getInt(1) != 0) {
                System.out.println("Locations Table Already Exists");
                return;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return;
        }

        try {
            // check for SQL driver
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite driver not found on classpath, check your gradle configuration.");
            e.printStackTrace();
            return;
        }

        System.out.println("SQLite driver registered!");

        // create new SQL table
        Statement s = null;
        try {
            s = connection.createStatement();
            s.execute("PRAGMA foreign_keys = ON");
            s.execute(
                    "CREATE TABLE IF NOT EXISTS  Locations("
                            + "nodeID TEXT NOT NULL ,"
                            + "xcoord INTEGER NOT NULL, "
                            + "ycoord INTEGER NOT NULL, "
                            + "floor TEXT NOT NULL, "
                            + "building TEXT NOT NULL,"
                            + "nodeType TEXT NOT NULL,"
                            + "longName TEXT NOT NULL,"
                            + "shortName TEXT NOT NULL, "
                            + "PRIMARY KEY ('nodeID'));");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean entryExists(String pkID) {
        return false;
    }

    @Override
    public Location getEntry(String pkID) {
        return null;
    }

    @Override
    public boolean loadFromArrayList(ArrayList<Location> objList) {
        return false;
    }
}
