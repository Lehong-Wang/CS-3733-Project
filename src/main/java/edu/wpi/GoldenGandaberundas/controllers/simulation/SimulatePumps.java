package edu.wpi.GoldenGandaberundas.controllers.simulation;

import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ThreadLocalRandom;

public class SimulatePumps {
  private static double frequency = 1;

  private static ArrayList<MedEquipmentSimulation> newPumpList = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_List = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_Stored = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_Dirty = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_InRoom = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_Cleaning = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Pumps_StillActive = new ArrayList<>();

  private static ArrayList<String> currentValidPumpLocations = new ArrayList<>();

  private static int numPumpsToSend;
  private static final int PUMP_CLEANING_TIME = 1;
  private static int hours = 0;

  private static final boolean DEUBUG_PUMP_SIM = false;
  private static final boolean PRINT_PUMP_SIM = false;

  public static ArrayList<MedEquipmentSimulation> updatePumps(
      ArrayList<MedEquipmentSimulation> currentPumpList, int time) {
    hours = time;
    if (DEUBUG_PUMP_SIM) {
      System.out.println("TIME: " + hours);
    }
    clearPumpLists();
    sortPumpLists(currentPumpList);
    if (PRINT_PUMP_SIM) {
      printPumpLists();
    }
    updateDirtyPumps();
    updateCleaningPumps();
    updateInUsePumps();
    updateStoredPumps();
    if (DEUBUG_PUMP_SIM) {
      System.out.println("New Pump List" + "(" + newPumpList.size() + "): " + newPumpList);
    }
    return newPumpList;
  }

  /** Clears static pump lists */
  private static void clearPumpLists() {
    newPumpList.clear();
    Pumps_StillActive.clear();
    Pumps_Cleaning.clear();
    Pumps_Dirty.clear();
    Pumps_Stored.clear();
    Pumps_InRoom.clear();
  }

  /**
   * Sort into static lists based on status
   *
   * @param fullPumpList
   */
  private static void sortPumpLists(ArrayList<MedEquipmentSimulation> fullPumpList) {
    for (MedEquipmentSimulation eqp : fullPumpList) {
      Pumps_List = (ArrayList<MedEquipmentSimulation>) fullPumpList.clone();
      switch (eqp.getStatus().trim().toUpperCase(Locale.ROOT)) {
        case "IN-USE":
          Pumps_InRoom.add(eqp);
          break;

        case "DIRTY":
          Pumps_Dirty.add(eqp);
          break;

        case "CLEANING":
          Pumps_Cleaning.add(eqp);
          break;

        case "STORED":
          Pumps_Stored.add(eqp);
          break;
      }
    }
  }

  /** prints static lists to terminal */
  private static void printPumpLists() {
    //    System.out.println('\n' + "HOUR: " + hours);
    System.out.println('\n' + "----------PUMPS---------");
    System.out.println("In Use Pumps" + "(" + Pumps_InRoom.size() + "): " + Pumps_InRoom);
    System.out.println("Dirty Pumps" + "(" + Pumps_Dirty.size() + "): " + Pumps_Dirty);
    System.out.println("Cleaning Pumps" + "(" + Pumps_Cleaning.size() + "): " + Pumps_Cleaning);
    System.out.println("Stored Pumps" + "(" + Pumps_Stored.size() + "): " + Pumps_Stored);
  }

  /**
   * Based on conditions decide how many recliners to send to rooms this iteration
   *
   * @return numRecliners to send out of storage this round
   */
  public static void setNumPumpsToSend() {
    // Base Number of pumps which is equal to the number that will be leaving the floor
    double basis = Pumps_Dirty.size() + 1;
    double totalNumPumps = Pumps_List.size();
    double peakTimeMod = 0, inStorageMod = 0;

    // If the time is between 10am and 3pm
    if ((hours % 24) > 10 && (hours & 24) < 15) {
      peakTimeMod = basis * 0.35;
    } else {
      peakTimeMod = basis * -0.2;
    }

    // If there are less than 30% of beds in storage, slow release rate
    if ((Pumps_Stored.size() / totalNumPumps) < 0.3) {
      inStorageMod = basis * -0.25;
    }

    int retVal = (int) Math.round(basis + peakTimeMod + inStorageMod);
    // Safeguard for ensuring we don't decide to send more than we have
    if (retVal >= Pumps_Stored.size()) {
      retVal = Pumps_Stored.size() - 1;
    }
    if (retVal < 0) {
      retVal = 0;
    }

    if (DEUBUG_PUMP_SIM) {
      System.out.println("Pump Basis " + basis);
      System.out.println("Pump Time Mod " + peakTimeMod);
      System.out.println("Pump Storage Mod " + inStorageMod);
      System.out.println("Pumps to Send " + retVal);
    }
    numPumpsToSend = retVal;
  }

  /** Update Dirty Beds */
  private static void updateDirtyPumps() {
    for (MedEquipmentSimulation pump : Pumps_Dirty) {
      pump.setCleaningEndTime(PUMP_CLEANING_TIME);
      pump.setStatus("Cleaning");
      pump.setCurrLoc("GSTOR00201");
      if (DEUBUG_PUMP_SIM) {
        System.out.println("Pump # " + pump.getMedID() + " is now in cleaning");
      }
      newPumpList.add(pump);
    }
  }

  /** Update Cleaning Recliners */
  private static void updateCleaningPumps() {
    for (MedEquipmentSimulation pump : Pumps_Cleaning) {
      if (pump.getCleaningEndTime() <= hours) {
        pump.setStatus("Stored");
        pump.setCurrLoc("GSTOR00103");
        if (DEUBUG_PUMP_SIM) {
          System.out.println("Pump # " + pump.getMedID() + " is now in storage");
        }
      }
      newPumpList.add(pump);
    }
  }

  /** Update In-Room Pumps */
  private static void updateInUsePumps() {
    for (MedEquipmentSimulation pump : Pumps_InRoom) {
      // If In Room Time is Up
      if (pump.getInRoomEndTime() <= hours) {
        pump.setStatus("Dirty");
        pump.setCurrLoc("GDIRT00103");
        if (DEUBUG_PUMP_SIM) {
          System.out.println("Pump # " + pump.getMedID() + " is now dirty");
        }
      } else {
        if (DEUBUG_PUMP_SIM) {
          System.out.println(
              "Pump # "
                  + pump.getMedID()
                  + " has "
                  + (pump.getInRoomEndTime() - hours)
                  + " hours remaining");
        }
        Pumps_StillActive.add(pump);
      }
      newPumpList.add(pump);
    }
  }

  /** Update Stored Pumps */
  private static void updateStoredPumps() {
    //    setCurrentValidLocations();
    //    for (int i = 0; i < numPumpsToSend; i++) {
    //      // generate random int to select from list of
    //      int rnd = new Random().nextInt(currentValidPumpLocations.size());
    //      String rndLoc = currentValidPumpLocations.get(rnd);
    //      currentValidPumpLocations.remove(rnd);
    //      MedEquipmentSimulation tempPump = Pumps_Stored.get(0);
    //      Pumps_Stored.remove(0);
    //      tempPump.setCurrLoc(rndLoc);
    //      tempPump.setStatus("In-Use");
    //      // TODO change hours to be more realistic
    //      int rndTTD = ThreadLocalRandom.current().nextInt(1, 4);
    //      tempPump.setInRoomEndTime(hours + rndTTD);
    //      if (DEUBUG_PUMP_SIM) {
    //        System.out.println(
    //            "Pump # "
    //                + tempPump.getMedID()
    //                + " will be sent from storage to room "
    //                + tempPump.getCurrLoc());
    //      }
    //      newPumpList.add(tempPump);
    //    }
    //
    //    for (MedEquipmentSimulation pump : Pumps_Stored) {
    //      newPumpList.add(pump);
    //    }

    setCurrentValidLocations();
    for (String loc : currentValidPumpLocations) {
      MedEquipmentSimulation tempPump = Pumps_Stored.get(0);
      Pumps_Stored.remove(0);
      tempPump.setCurrLoc(loc);
      tempPump.setStatus("In-Use");
      // TODO change hours to be more realistic
      int rndTTD = ThreadLocalRandom.current().nextInt(1, 8);
      tempPump.setInRoomEndTime(hours + rndTTD);
      if (DEUBUG_PUMP_SIM) {
        System.out.println(
            "Pump # "
                + tempPump.getMedID()
                + " will be sent from storage to room "
                + tempPump.getCurrLoc());
      }
      newPumpList.add(tempPump);
    }

    for (MedEquipmentSimulation pump : Pumps_Stored) {
      newPumpList.add(pump);
    }
  }

  /**
   * Update list of currently valid locations(rooms without a bed) by checking each item in the list
   * and seeing if there is a bed that also has that location
   */
  private static void setCurrentValidLocations() {
    currentValidPumpLocations.clear();
    currentValidPumpLocations = (ArrayList<String>) Simulation.validLocations.clone();

    ArrayList<String> tempCurValidLoc = new ArrayList<>();
    boolean valid = false;

    for (String strLoc : currentValidPumpLocations) {
      valid = true;
      for (MedEquipmentSimulation pump : Pumps_StillActive) {
        if (pump.getCurrLoc().equals(strLoc)) {
          if (DEUBUG_PUMP_SIM) {
            System.out.println("Location " + strLoc + " has pump # " + pump.getMedID());
          }
          valid = false;
        }
      }
      if (valid) {
        tempCurValidLoc.add(strLoc);
        if (DEUBUG_PUMP_SIM) {
          System.out.println("No pumps in room " + strLoc);
        }
      }
    }

    currentValidPumpLocations.clear();
    currentValidPumpLocations.addAll(tempCurValidLoc);

    if (DEUBUG_PUMP_SIM) {
      System.out.println(
          "All Valid Pump Locations "
              + "("
              + currentValidPumpLocations.size()
              + "): "
              + currentValidPumpLocations);
    }

    // Remove all rooms that will not have a bed next round
    currentValidPumpLocations.removeAll(SimulateBedsRecs.currentValidBedLocations);

    if (DEUBUG_PUMP_SIM) {
      System.out.println(
          "Locations With Bed & No Pump Next Round: "
              + "("
              + currentValidPumpLocations.size()
              + "): "
              + currentValidPumpLocations);
    }
  }

  public static void setFrequency(double freq) {
    frequency = freq;
  }
}
