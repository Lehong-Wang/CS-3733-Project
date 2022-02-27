package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class MedEquipRequestClientServer
        implements TableController<MedEquipRequest, ArrayList<Integer>> {
    private static TableController<Request, Integer> masterTable = null;
    /** name of table */
    private String tbName;
    /** name of columns in database table the first entry is the primary key */
    private List<String> colNames;
    /** list of keys that make a composite primary key */
    private String pkCols = null;
    /** list that contains the objects stored in the database */
    private ArrayList<MedEquipRequest> objList;
    /** relative path to the database file */
    ConnectionHandler connectionHandler = ConnectionHandler.getInstance();

    Connection connection = connectionHandler.getConnection();

    public MedEquipRequestClientServer(
            String tbName, String[] cols, String pkCols, ArrayList<MedEquipRequest> objList)
            throws SQLException {
        // create a new table with column names if none table of same name exist
        // if there is one, do nothing
        this.tbName = tbName;
        this.pkCols = pkCols;
        colNames = Arrays.asList(cols);
        this.objList = objList;
    }

    @Override
    public ArrayList<MedEquipRequest> readTable() {
        ArrayList tableInfo = new ArrayList<MedEquipRequest>(); // **
        try {
            PreparedStatement s = connection.prepareStatement("SElECT * FROM " + tbName + ";");
            ResultSet r = s.executeQuery();
            while (r.next()) {
                System.out.println(masterTable.getEntry(r.getInt(1)));
                tableInfo.add(new MedEquipRequest(masterTable.getEntry(r.getInt(1)), r.getInt(2))); // **
            }
        } catch (SQLException se) {
            se.printStackTrace();
            return null;
        }
        objList = tableInfo;
        System.out.println(tableInfo);
        return tableInfo;
    }

    @Override
    public boolean addEntry(MedEquipRequest medEquipRequest) {
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
                                    + " VALUES (?, ?)"
                                    + "end");

            s.setInt(1, medEquipRequest.getReqID());
            s.setInt(2, medEquipRequest.getMedID());
            s.setInt(3, medEquipRequest.getReqID());
            s.setInt(4, medEquipRequest.getMedID());
            s.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public ArrayList<MedEquipRequest> readBackup(String fileName) { // reads csv file for backup
        ArrayList<MedEquipRequest> reqList =
                new ArrayList<>(); // creates a list for the request objects
        try {
            File csvFile = new File(fileName); // takes csv file
            BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
            String currentLine = buffer.readLine(); // reads a line from the csv file
            currentLine = buffer.readLine();

            while (currentLine != null) { // cycles in the while loop until it reaches the end
                String[] element = currentLine.split(","); // separates each element based on a comma
                MedEquipRequest req = // creates a request
                        new MedEquipRequest( // **
                                Integer.parseInt(element[0]),
                                element[1],
                                Integer.parseInt(element[2]),
                                Integer.parseInt(element[3]),
                                Integer.parseInt(element[4]),
                                Integer.parseInt(element[5]),
                                Integer.parseInt(element[6]),
                                element[8],
                                element[9],
                                Integer.parseInt(element[10]));
                reqList.add(req);
                currentLine = buffer.readLine(); // reads the current line
            }
            return reqList;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                    " CREATE TABLE "
                            + tbName
                            + "("
                            + "    reqID     integer NOT NULL, "
                            + "    medEquipID     integer, "
                            + "    CONSTRAINT MedEquipReqPK PRIMARY KEY (reqID, medEquipID), "
                            + "    CONSTRAINT reqIDFK1 FOREIGN KEY (reqID) REFERENCES Requests (requestID) "
                            + "        ON UPDATE CASCADE  "
                            + "        ON DELETE CASCADE, "
                            + "    CONSTRAINT MedEquipReqFK2 FOREIGN KEY (medEquipID) REFERENCES MedEquipment (medID) "
                            + ");");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public MedEquipRequest getEntry(ArrayList<Integer> pkID) { // grabs one db entry
        MedEquipRequest req = new MedEquipRequest(); // makes a new request object
        if (this.entryExists(pkID)) {
            try {
                PreparedStatement s =
                        connection.prepareStatement(
                                "SELECT * FROM medEquipRequests WHERE ("
                                        + pkCols
                                        + ") = (?, ?);"); // makes a statement
                s.setInt(1, pkID.get(0));
                s.setInt(2, pkID.get(1));
                ResultSet r = s.executeQuery(); // finds the row of the ID

                if (entryExists(pkID)) { // if the ID exists
                    // the following lines go through the columns in the row of the ID
                    req.setReqID(r.getInt(1));
                    req.setMedID(r.getInt(2));
                }
            } catch (Exception e) { // database code didn't work
                e.printStackTrace(); // prints errors
                return null; // nothing happens
            }
        }
        return req; // returns object
    }

    @Override
    public boolean loadFromArrayList(ArrayList<MedEquipRequest> objList) {
        this.createTable();
        deleteTableData();
        for (MedEquipRequest medEquipRequest : objList) {
            if (!this.addEntry(medEquipRequest)) {
                return false;
            }
        }
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

    @Override
    public void writeTable() {

        for (MedEquipRequest obj : objList) {

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
    public boolean editEntry(ArrayList<Integer> pkid, String colName, Object value) {
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

    /**
     * removes a row from the database
     *
     * @param pkid primary key of row to be removed
     * @return true if successful, false otherwise
     */
    public boolean deleteEntry(ArrayList<Integer> pkid) {
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
    public ArrayList<MedEquipRequest> loadBackup(String fileName) {
        createTable();
        ArrayList<MedEquipRequest> listObjs = readBackup(fileName);

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
    public boolean entryExists(ArrayList<Integer> pkID) {
        boolean exists = false;
        StringBuilder pkString = new StringBuilder();
        for (int i = 0; i < pkID.size() - 1; i++) {
            pkString.append(pkID.get(i)).append(",");
        }
        pkString.append(pkID.get(pkID.size() - 1));

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

    public String getTableName() {
        return tbName;
    }

    public ArrayList<MedEquipRequest> getObjList() {
        return objList;
    }
}

