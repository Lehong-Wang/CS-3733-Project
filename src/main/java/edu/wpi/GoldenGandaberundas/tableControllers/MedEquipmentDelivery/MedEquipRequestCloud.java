package edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.CloudController;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.io.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MedEquipRequestCloud implements TableController<MedEquipRequest, ArrayList<Integer>> {
  private ArrayList<MedEquipRequest> objList = null;
  private String tbName = "MedEquipRequests";

  public MedEquipRequestCloud(ArrayList<MedEquipRequest> objList) {
    this.objList = objList;
  }

  /**
   * Generates ArrayList of objects that are stored in the table tbName
   *
   * @return objects stored in tbName
   */
  @Override
  public ArrayList<MedEquipRequest> readTable() {
    return CloudController.getInstance().readTable(tbName, MedEquipRequest.class);
  }

  public void writeTable() {
    CloudController.getInstance().uploadAllDocuments("MedEquipRequests", objList);
  }

  @Override
  public ArrayList<MedEquipRequest> getObjList() {
    return this.objList;
  }

  @Override
  public boolean deleteEntry(ArrayList<Integer> pkid) {
    return CloudController.getInstance()
        .deleteEntry(tbName, (pkid.get(0).toString() + pkid.get(1).toString()));
  }

  /**
   * Add object to the table
   *
   * @param obj
   * @return true if successful, false otherwise
   */
  @Override
  public boolean addEntry(MedEquipRequest obj) {
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
    } else {
      System.err.println("MED EQUIP REQ NOT");
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
  public ArrayList<MedEquipRequest> readBackup(String fileName) {
    ArrayList<MedEquipRequest> med = new ArrayList<>(); // creates a list for the location objects
    File csvFile = new File(fileName);
    try {
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
        med.add(req); // adds the location to the list
        currentLine = buffer.readLine();
      }
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return med;
  }

  @Override
  public ArrayList<MedEquipRequest> loadBackup(String fileName) {
    ArrayList<MedEquipRequest> listObjs = readBackup(fileName);
    this.objList = listObjs;
    this.writeTable();
    return listObjs;
  }

  @Override
  public boolean editEntry(ArrayList<Integer> pkid, String colName, Object value) {
    return CloudController.getInstance()
        .editEntry(tbName, (pkid.get(0).toString() + pkid.get(1).toString()), colName, value);
  }

  /** runs SQL commands to create the table in the hospitalData.db file */
  @Override
  public void createTable() {}

  @Override
  public boolean entryExists(ArrayList<Integer> pkID) {
    return false;
  }

  /**
   * Retrieves a specific object stored in the database
   *
   * @param pkID primary key value that identifies the desired object
   * @return object stored using PKID or null on error
   */
  @Override
  public MedEquipRequest getEntry(ArrayList<Integer> pkid) {
    return (MedEquipRequest)
        CloudController.getInstance()
            .getEntry(
                tbName, (pkid.get(0).toString() + pkid.get(1).toString()), MedEquipRequest.class);
  }

  @Override
  public boolean loadFromArrayList(ArrayList<MedEquipRequest> objList) {
    System.out.println(objList);
    this.createTable();
    for (MedEquipRequest req : objList) {
      if (!this.addEntry(req)) {
        return false;
      }
    }
    this.objList = objList;
    return true;
  }

  public MedEquipRequest getMedEquipRequest(ArrayList<Integer> pkid) {
    return (MedEquipRequest)
        CloudController.getInstance()
            .getEntry(
                "MedEquipRequests",
                (pkid.get(0).toString() + pkid.get(1).toString()),
                MedEquipRequest.class);
  }
}
