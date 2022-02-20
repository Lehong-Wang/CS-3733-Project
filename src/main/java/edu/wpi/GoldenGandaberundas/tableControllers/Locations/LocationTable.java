package edu.wpi.GoldenGandaberundas.tableControllers.Locations;


import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;

import java.io.*;

import java.util.ArrayList;


public interface LocationTable{


    /**
     * Modifies the attribute so that it is equal to value MAKE SURE YOU KNOW WHAT DATA TYPE YOU ARE
     * MODIFYING
     *
     * @param pkid the primary key that represents the row you are modifying
     * @param colName column to be modified
     * @param value new value for column
     * @return true if successful, false otherwise
     */
    public boolean editEntry(String pkid, String colName, Object value);


    /**
     * removes a row from the database
     *
     * @param pkid primary key of row to be removed
     * @return true if successful, false otherwise
     */
    public boolean deleteEntry(String pkid);


    /**
     * Add object to the table
     *
     * @param obj
     * @return true if successful, false otherwise
     */
    public boolean addEntry(Location obj);

    /**
     * creates CSV file representing the objects stored in the table
     *
     * @param f filename of the to be created CSV
     */
    public void createBackup(File f);

    /**
     * Loads a CSV file in to memory, parses to find the attributes of the objects stored in the table
     *
     * @param fileName location of the CSV file
     * @return arraylist containing n number of T objects, null if error
     */
    public abstract ArrayList<Location> readBackup(String fileName);

    // drop current table and enter data from CSV
    public ArrayList<Location> loadBackup(String fileName);

    /** runs SQL commands to create the table in the hospitalData.db file */
    public abstract void createTable();

    // checks if an entry exists
    public boolean entryExists(String pkID);


    /**
     * Retrieves a specific object stored in the database
     *
     * @param pkID primary key value that identifies the desired object
     * @return object stored using PKID or null on error
     */
    public Location getEntry(String pkID);


    public void backUpAllTables();

    public void loadAllTables();


    /**
     * Creates a list of points with for each location with their x and y coordinates
     *
     * @return the list of points
     */
    public ArrayList<Point> getNodes();

}
