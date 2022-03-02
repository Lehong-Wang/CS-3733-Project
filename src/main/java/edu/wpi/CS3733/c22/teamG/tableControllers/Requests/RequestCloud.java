package edu.wpi.CS3733.c22.teamG.tableControllers.Requests;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.CloudController;
import java.io.*;
import java.util.ArrayList;
import java.util.Locale;

public class RequestCloud implements TableController<Request, Integer> {

  private ArrayList<Request> objList = null;
  private String tbName = "Requests";

  public RequestCloud(ArrayList<Request> objList) {
    this.objList = objList;
  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  @Override
  public ArrayList<Request> readTable() {
    return CloudController.getInstance().readTable(tbName, Request.class);
  }

  public void writeTable() {
    CloudController.getInstance().uploadAllDocuments("Requests", objList);
  }

  @Override
  public ArrayList<Request> getObjList() {
    return this.objList;
  }

  @Override
  public boolean deleteEntry(Integer pkid) {
    return CloudController.getInstance().deleteEntry(tbName, pkid.toString());
  }

  /**
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  @Override
  public boolean addEntry(Request obj) {
    return CloudController.getInstance().addEntry(tbName, obj);
  }

  @Override
  public void createBackup(File f) {}

  /**
   * Loads a CSV file in to memory, parses to find the attributes of the objects stored in the table
   *
   * @param fileName location of the CSV file
   * @return arraylist containing n number of T objects, null if error
   */
  @Override
  public ArrayList<Request> readBackup(String fileName) {
    ArrayList<Request> reqList = new ArrayList<Request>();

    try {
      File csvFile = new File(fileName);
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file

      if (!currentLine
          .toLowerCase(Locale.ROOT)
          .trim()
          .equals(
              new String(
                  "requestID, locationID, empInitiated, empCompleter,"
                      + "timeStart, timeEnd, patientID, requestType"
                      + "requestStatus"
                      + "notes"))) {
        System.err.println("requests backup format not recognized");
      }
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        // check if a line is blank
        if (!currentLine.isEmpty()) {
          String[] element = currentLine.split(","); // separates each element based on a comma
          Request request =
              new Request(
                  Integer.parseInt(element[0]),
                  element[1],
                  Integer.parseInt(element[2]),
                  Integer.parseInt(element[3]),
                  Integer.parseInt(element[4]),
                  Integer.parseInt(element[5]),
                  Integer.parseInt(element[6]),
                  element[7],
                  element[8],
                  element.length > 9 ? element[9] : null);
          reqList.add(request); // adds the location to the list
          // reqList.add(readSpecificRequest(element, request));
        } else {
          System.out.println("EMPTY");
        }
        currentLine = buffer.readLine();
      }
      ; // creates a Location

    } catch (FileNotFoundException ex) {
      System.err.println("REQUEST readbackup FAILED: no file found");
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return reqList;
  }

  @Override
  public ArrayList<Request> loadBackup(String fileName) {
    ArrayList<Request> listObjs = readBackup(fileName);
    this.objList = listObjs;
    this.writeTable();
    return listObjs;
  }

  @Override
  public boolean editEntry(Integer pkid, String colName, Object value) {
    return CloudController.getInstance().editEntry(tbName, pkid.toString(), colName, value);
  }

  /** runs SQL commands to create the table in the hospitalData.db file */
  @Override
  public void createTable() {}

  @Override
  public boolean entryExists(Integer pkID) {
    return false;
  }

  /**
   * Retrieves a specific object stored in the database
   *
   * @param pkID primary key value that identifies the desired object
   * @return object stored using PKID or null on error
   */
  @Override
  public Request getEntry(Integer pkID) {
    return (Request) CloudController.getInstance().getEntry(tbName, pkID.toString(), Request.class);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Request> objList) {
    System.out.println(objList);
    this.createTable();
    for (Request req : objList) {
      if (!this.addEntry(req)) {
        return false;
      }
    }
    this.objList = objList;
    return true;
  }

  public Request getRequest(String pkid) {
    return (Request) CloudController.getInstance().getEntry("Requests", pkid, Request.class);
  }

  public boolean editEntry(String pkid, String colname, Object value) {
    return CloudController.getInstance().editEntry(tbName, pkid, colname, value);
  }

  public boolean deleteEntry(String pkid) {
    return CloudController.getInstance().deleteEntry(tbName, pkid);
  }
}
