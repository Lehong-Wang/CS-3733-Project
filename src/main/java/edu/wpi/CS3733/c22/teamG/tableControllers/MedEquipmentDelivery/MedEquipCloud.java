package edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.CloudController;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedEquipCloud implements TableController<MedEquipment, Integer> {
  private ArrayList<MedEquipment> objList = null;
  private String tbName = "MedEquipments";

  public MedEquipCloud(ArrayList<MedEquipment> objList) {
    this.objList = objList;
  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  @Override
  public ArrayList<MedEquipment> readTable() {
    return CloudController.getInstance().readTable(tbName, MedEquipment.class);
  }

  public void writeTable() {
    CloudController.getInstance().uploadAllDocuments("MedEquipments", objList);
  }

  @Override
  public ArrayList<MedEquipment> getObjList() {
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
  public boolean addEntry(MedEquipment obj) {
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
  public ArrayList<MedEquipment> readBackup(String fileName) {
    ArrayList<MedEquipment> med = new ArrayList<>(); // creates a list for the location objects
    File csvFile = new File(fileName);
    try {
      BufferedReader buffer = new BufferedReader(new FileReader(csvFile)); // reads the files
      String currentLine = buffer.readLine(); // reads a line from the csv file
      currentLine = buffer.readLine();

      while (currentLine != null) { // cycles in the while loop until it reaches the end
        String[] element = currentLine.split(","); // separates each element based on a comma
        MedEquipment currentMed = this.createMedEquipment(element); // creates a Location
        med.add(currentMed); // adds the location to the list
        currentLine = buffer.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return med;
  }

  public MedEquipment createMedEquipment(String[] ele) {
    MedEquipment med = new MedEquipment(Integer.parseInt(ele[0]), ele[1], ele[2], ele[3]);
    return med;
  }

  @Override
  public ArrayList<MedEquipment> loadBackup(String fileName) {
    ArrayList<MedEquipment> listObjs = readBackup(fileName);
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
  public MedEquipment getEntry(Integer pkID) {
    return (MedEquipment)
        CloudController.getInstance().getEntry(tbName, pkID.toString(), MedEquipment.class);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedEquipment> objList) {
    System.out.println(objList);
    this.createTable();
    for (MedEquipment req : objList) {
      if (!this.addEntry(req)) {
        return false;
      }
    }
    this.objList = objList;
    return true;
  }

  public MedEquipment getMedEquipment(String pkid) {
    return (MedEquipment)
        CloudController.getInstance().getEntry("MedEquipments", pkid, MedEquipment.class);
  }

  public boolean editEntry(String pkid, String colname, Object value) {
    return CloudController.getInstance().editEntry(tbName, pkid, colname, value);
  }

  public boolean deleteEntry(String pkid) {
    return CloudController.getInstance().deleteEntry(tbName, pkid);
  }
}
