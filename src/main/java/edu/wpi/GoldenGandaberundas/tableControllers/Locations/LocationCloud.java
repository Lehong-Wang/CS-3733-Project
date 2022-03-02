package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.CloudController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class LocationCloud implements TableController<Location, String> {
  private ArrayList<Location> objList = null;
  private String tbName = "Locations";

  public LocationCloud(ArrayList<Location> objList) {
    this.objList = objList;
  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  @Override
  public ArrayList<Location> readTable() {
    return (ArrayList<Location>) CloudController.getInstance().readTable(tbName, Location.class);
  }

  public void writeTable() {
    CloudController.getInstance().uploadAllDocuments(tbName, objList);
  }

  @Override
  public ArrayList<Location> getObjList() {
    return objList;
  }

  /**
   * Retrieves a specific object stored in the database
   *
   * @param pkID primary key value that identifies the desired object
   * @return object stored using PKID or null on error
   */
  public Location getEntry(String pkID) {
    return (Location) CloudController.getInstance().getEntry(tbName, pkID, Location.class);
  }

  public boolean editEntry(String pkid, String colname, Object value) {
    return CloudController.getInstance().editEntry(tbName, pkid, colname, value);
  }

  /** runs SQL commands to create the table in the hospitalData.db file */
  @Override
  public void createTable() {}

  @Override
  public boolean entryExists(String pkID) {
    return false;
  }

  @Override
  public boolean loadFromArrayList(ArrayList<Location> objList) {
    System.out.println(objList);
    this.createTable();
    for (Location loc : objList) {
      if (!this.addEntry(loc)) {
        return false;
      }
    }
    this.objList = objList;
    return true;
  }

  public boolean deleteEntry(String pkid) {
    return CloudController.getInstance().deleteEntry(tbName, pkid);
  }

  /**
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  @Override
  public boolean addEntry(Location obj) {
    return CloudController.getInstance().addEntry(tbName, obj);
  }

  @Override
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
        }
        currentLine = buffer.readLine();
      }
    } catch (FileNotFoundException ex) {
      ex.printStackTrace();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    return locList;
  }

  @Override
  public ArrayList<Location> loadBackup(String fileName) {
    ArrayList<Location> listObjs = readBackup(fileName);
    this.objList = listObjs;
    this.writeTable();
    return listObjs;
  }
}
