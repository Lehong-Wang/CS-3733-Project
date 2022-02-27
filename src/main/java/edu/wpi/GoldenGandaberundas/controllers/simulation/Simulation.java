package edu.wpi.GoldenGandaberundas.controllers.simulation;

import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipment.MedEquipment;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentSimulation.SimMedEquipmentTbl;
import java.util.*;

public class Simulation {
  static ArrayList<String> validLocations = new ArrayList<>();
  static Map<String, String> corrLoc;
  static String teamLetter = "G";

  static {
    validLocations.add(teamLetter + "PATI00103");
    validLocations.add(teamLetter + "PATI00203");
    validLocations.add(teamLetter + "PATI00303");
    validLocations.add(teamLetter + "PATI00403");
    validLocations.add(teamLetter + "PATI00503");
    validLocations.add(teamLetter + "PATI00603");
    validLocations.add(teamLetter + "PATI00703");
    validLocations.add(teamLetter + "PATI00803");
    validLocations.add(teamLetter + "PATI00903");
    validLocations.add(teamLetter + "PATI01003");
    validLocations.add(teamLetter + "PATI01103");
    validLocations.add(teamLetter + "PATI01203");
    validLocations.add(teamLetter + "PATI01303");
    validLocations.add(teamLetter + "PATI01403");
    validLocations.add(teamLetter + "PATI01503");
    validLocations.add(teamLetter + "PATI01603");
    validLocations.add(teamLetter + "PATI01703");
    validLocations.add(teamLetter + "PATI01803");
    validLocations.add(teamLetter + "PATI01903");
    validLocations.add(teamLetter + "PATI02003");
    validLocations.add(teamLetter + "STOR001L1"); // TODO add new location

    corrLoc = new HashMap<>();
    // TODO change the corresponding locations so they are not all the same
    corrLoc.put("GPATI00103", "HHALL00103");
    corrLoc.put("GPATI00203", "HHALL00103");
    corrLoc.put("GPATI00303", "HHALL00203");
    corrLoc.put("GPATI00403", "HHALL00203");
    corrLoc.put("GPATI00503", "HHALL00303");
    corrLoc.put("GPATI00603", "HHALL00403");
    corrLoc.put("GPATI00703", "HHALL00503");
    corrLoc.put("GPATI00803", "HHALL00603");
    corrLoc.put("GPATI00903", "HHALL00703");
    corrLoc.put("GPATI01003", "HHALL00803");
    corrLoc.put("GPATI01103", "HHALL00903");
    corrLoc.put("GPATI01203", "HHALL01003");
    corrLoc.put("GPATI01303", "HHALL01103");
    corrLoc.put("GPATI01403", "HHALL01103");
    corrLoc.put("GPATI01503", "HHALL01203");
    corrLoc.put("GPATI01603", "HHALL01303");
    corrLoc.put("GPATI01703", "HHALL01303");
    corrLoc.put("GPATI01803", "HHALL01403");
    corrLoc.put("GPATI01903", "GSTOR001L1"); // TODO UPDATE THIS LOCATION TO NEW INPUT
    corrLoc.put("GPATI02003", "HHALL01503");
  }

  private static ArrayList<MedEquipmentSimulation> Beds_List = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Recliners_List = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_List = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> fullMedList = new ArrayList<>();
  public static String[][] pathList;
  private static MedEquipmentSimulation XRay;
  static TableController<MedEquipment, Integer> medTable;

  private final String smallEqpCSV = "TestCSVs/medEquipSimulationSubset.csv";
  private static final String eqpCSV = "TestCSVs/medEquipSimulation.csv";

  private int numPumpsToSend, numReclinersToSend;
  private int endTime = 160;
  private static int hours = 0;

  public void update(int end) {
    endTime = end;
    System.out.println(endTime);
    takeSnapshot();
    sortLists(fullMedList);
    makeSimulationLists(fullMedList);

    for (hours = 1; hours <= endTime; hours++) {
      ArrayList<MedEquipmentSimulation> tempBedsRecs =
          SimulateBedsRecs.updateBedsRecliners(Beds_List, Recliners_List, hours);

      ArrayList<MedEquipmentSimulation> tempPumps = SimulatePumps.updatePumps(Pumps_List, hours);

      MedEquipmentSimulation tempXRay = SimulateXRay.updateXRay(XRay, hours);

      ArrayList<MedEquipmentSimulation> tempFullList = new ArrayList<>();
      tempFullList.addAll(tempPumps);
      tempFullList.addAll(tempBedsRecs);
      tempFullList.add(tempXRay);
      addIterationToSimLists(tempFullList);
      sortLists(tempFullList);
    }
    //    printSimList();
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
  private static void addIterationToSimLists(ArrayList<MedEquipmentSimulation> eqpList) {
    for (MedEquipmentSimulation eqp : eqpList) {
      pathList[eqp.getMedID()][hours - 1] = eqp.getCurrLoc();
    }
  }

  private static void printSimList() {
    System.out.println('\n');
    for (int i = 1; i < pathList.length; i++) {
      System.out.println("EQP # " + i + ": " + Arrays.deepToString(pathList[i]));
    }
  }

  /** Intended to take Snapshot from DB Currently loading CSV */
  public static void takeSnapshot() {
    medTable = SimMedEquipmentTbl.getInstance();
    //    ArrayList<MedEquipment> MedDBList = medTable.readTable();
    //    ArrayList<MedEquipment> MedDBList = medTable.readBackup(eqpCSV);
    ArrayList<MedEquipment> MedDBList = medTable.readTable();
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
  public static void sortLists(ArrayList<MedEquipmentSimulation> list) {
    Beds_List.clear();
    Recliners_List.clear();
    Pumps_List.clear();
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
      return "GSTOR001L1";
    }
  }

  public static void setTeamLetter(Character letter) {
    teamLetter = letter.toString().toUpperCase(Locale.ROOT);
  }
}
