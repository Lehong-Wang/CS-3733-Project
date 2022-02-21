package edu.wpi.GoldenGandaberundas.controllers.simulation;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.util.*;

public class SimulationController {
  static ArrayList<String> validLocations = new ArrayList<>();
  static Map<String, String> corrLoc;

  static {
    validLocations.add("gPATI00103");
    validLocations.add("gPATI00203");
    validLocations.add("gPATI00303");
    validLocations.add("gPATI00403");
    validLocations.add("gPATI00503");
    validLocations.add("gPATI00603");
    validLocations.add("gPATI00703");
    validLocations.add("gPATI00803");
    validLocations.add("gPATI00903");
    validLocations.add("gPATI01003");
    validLocations.add("gPATI01103");
    validLocations.add("gPATI01203");
    validLocations.add("gPATI01303");
    validLocations.add("gPATI01403");
    validLocations.add("gPATI01503");
    validLocations.add("gPATI01603");
    validLocations.add("gPATI01703");
    validLocations.add("gPATI01803");
    validLocations.add("gPATI01903");
    validLocations.add("gPATI02003");

    corrLoc = new HashMap<>();
    // TODO change the corresponding locations so they are not all the same
    corrLoc.put("gPATI00103", "HHALL00103");
    corrLoc.put("gPATI00203", "HHALL00103");
    corrLoc.put("gPATI00303", "HHALL00203");
    corrLoc.put("gPATI00403", "HHALL00203");
    corrLoc.put("gPATI00503", "HHALL00303");
    corrLoc.put("gPATI00603", "HHALL00403");
    corrLoc.put("gPATI00703", "HHALL00503");
    corrLoc.put("gPATI00803", "HHALL00603");
    corrLoc.put("gPATI00903", "HHALL00703");
    corrLoc.put("gPATI01003", "HHALL00803");
    corrLoc.put("gPATI01103", "HHALL00903");
    corrLoc.put("gPATI01203", "HHALL01003");
    corrLoc.put("gPATI01303", "HHALL01103");
    corrLoc.put("gPATI01403", "HHALL01103");
    corrLoc.put("gPATI01503", "HHALL01203");
    corrLoc.put("gPATI01603", "HHALL01303");
    corrLoc.put("gPATI01703", "HHALL01303");
    corrLoc.put("gPATI01803", "HHALL01403");
    corrLoc.put("gPATI01903", "HHALL01503");
    corrLoc.put("gPATI02003", "HHALL01503");
  }

  private ArrayList<MedEquipmentSimulation> Beds_List = new ArrayList<>();
  private ArrayList<MedEquipmentSimulation> Recliners_List = new ArrayList<>();
  private ArrayList<MedEquipmentSimulation> Pumps_List = new ArrayList<>();
  private ArrayList<MedEquipmentSimulation> fullMedList = new ArrayList<>();
  private static String[][] pathList;
  private MedEquipmentSimulation XRay;
  TableController<MedEquipment, Integer> medTable;

  private final String smallEqpCSV = "TestCSVs/medEquipSimulationSubset.csv";
  private final String eqpCSV = "TestCSVs/medEquipSimulation.csv";

  private int numPumpsToSend, numReclinersToSend;
  private final int endTime = 96;
  private static int hours = 0;

  public void update() {
    takeSnapshot();
    sortLists(fullMedList);
    makeSimulationLists(fullMedList);

    for (hours = 1; hours <= endTime; hours++) {
      ArrayList<MedEquipmentSimulation> tempBedsRecs =
          SimulateBedsRecs.updateBedsRecliners(this.Beds_List, this.Recliners_List, hours);

      ArrayList<MedEquipmentSimulation> tempPumps =
          SimulatePumps.updatePumps(this.Pumps_List, hours);

      MedEquipmentSimulation tempXRay = SimulateXRay.updateXRay(this.XRay, hours);

      ArrayList<MedEquipmentSimulation> tempFullList = new ArrayList<>();
      tempFullList.addAll(tempPumps);
      tempFullList.addAll(tempBedsRecs);
      tempFullList.add(tempXRay);
      addIterationToSimLists(tempFullList);
      sortLists(tempFullList);
    }
    printSimList();
  }

  /**
   * Create correctly sized 2D Array
   *
   * @param eqpList
   */
  private void makeSimulationLists(ArrayList<MedEquipmentSimulation> eqpList) {
    pathList = new String[eqpList.size() + 1][endTime];
  }

  /**
   * Add Med eqp into 2D array[ID#][Hour]
   *
   * @param eqpList
   */
  private void addIterationToSimLists(ArrayList<MedEquipmentSimulation> eqpList) {
    for (MedEquipmentSimulation eqp : eqpList) {
      pathList[eqp.getMedID()][hours - 1] = eqp.getCurrLoc();
    }
  }

  private void printSimList() {
    System.out.println('\n');
    for (int i = 1; i < pathList.length; i++) {
      System.out.println("EQP # " + i + ": " + Arrays.deepToString(pathList[i]));
    }
  }

  /** Intended to take Snapshot from DB Currently loading CSV */
  public void takeSnapshot() {
    medTable = MedEquipmentTbl.getInstance();
    //    ArrayList<MedEquipment> MedDBList = medTable.readTable();
    ArrayList<MedEquipment> MedDBList = medTable.readBackup(eqpCSV);
    for (MedEquipment eqp : MedDBList) {
      MedEquipmentSimulation newEqp =
          new MedEquipmentSimulation(
              eqp.getMedID(), eqp.getMedEquipmentType(), eqp.getStatus(), eqp.getCurrLoc());
      fullMedList.add(newEqp);
    }
    sortLists(fullMedList);
    //    System.out.println(fullMedList);
  }

  /**
   * Sort full MedList into lists based on type and status
   *
   * @param list full List of Med Equipment in DB
   */
  public void sortLists(ArrayList<MedEquipmentSimulation> list) {
    this.Beds_List.clear();
    this.Recliners_List.clear();
    this.Pumps_List.clear();
    for (MedEquipmentSimulation eqp : list) {
      //      System.out.println(eqp.getType().trim().toUpperCase(Locale.ROOT));
      switch (eqp.getMedEquipmentType().trim().toUpperCase(Locale.ROOT)) {
        case "BED":
          //          System.out.println(eqp.getStatus().trim().toUpperCase(Locale.ROOT));
          Beds_List.add(eqp);
          break;

        case "RECLINER":
          Recliners_List.add(eqp);
          break;

        case "INFUSION PUMP":
          Pumps_List.add(eqp);
          break;

        case "X-RAY":
          XRay = eqp;
      }
    }
  }

  /** Print sorted lists */
  public void printLists() {}

  /**
   * Returns String representing the location outside the patient room which the current bed is in
   *
   * @param loc the current bed locaiton, should be a patient room
   * @return String of corresponding hallway location
   */
  static String getCorrespondingLocation(String loc) {
    if (corrLoc.containsKey(loc)) {
      return corrLoc.get(loc);
    } else {
      // TODO error handle here, maybe send to storage if the bed is in a weird spot?
      return "gSTOR001l1";
    }
  }
}
