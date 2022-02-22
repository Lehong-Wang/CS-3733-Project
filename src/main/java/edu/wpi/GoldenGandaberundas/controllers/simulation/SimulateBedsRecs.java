package edu.wpi.GoldenGandaberundas.controllers.simulation;

import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class SimulateBedsRecs {
  // TODO make list of valid locations
  // TOOD make dict of rooms and locations outside rooms
  public static ArrayList<String> currentValidBedLocations = new ArrayList<>();

  private static ArrayList<MedEquipmentSimulation> newBedList = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> newReclinerList = new ArrayList<>();
  private static final int BED_CLEANING_TIME = 2;

  public static ArrayList<MedEquipmentSimulation> Beds_InRoom = new ArrayList<>();
  public static ArrayList<MedEquipmentSimulation> Beds_Dirty = new ArrayList<>();
  public static ArrayList<MedEquipmentSimulation> Beds_Cleaning = new ArrayList<>();
  public static ArrayList<MedEquipmentSimulation> Beds_AwaitingTransport = new ArrayList<>();
  public static ArrayList<MedEquipmentSimulation> Beds_TemporarilyOutside = new ArrayList<>();
  public static ArrayList<MedEquipmentSimulation> Beds_Stored = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Beds_List = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Beds_StillActive = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Recliners_InRoom = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Recliners_Dirty = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Recliners_Cleaning = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Recliners_Stored = new ArrayList<>();
  private static ArrayList<MedEquipmentSimulation> Recliners_List = new ArrayList<>();

  private static LocationTbl locTable;
  private static int hours;

  private static int numBedsToSend, numReclinersToSend;

  private static boolean DEUBUG_BED_SIM = false;
  private static boolean PRINT_BED_SIM = false;

  public static ArrayList<MedEquipmentSimulation> updateBedsRecliners(
      ArrayList<MedEquipmentSimulation> currentBedList,
      ArrayList<MedEquipmentSimulation> currentReclinerList,
      int time) {
    clearBedLists();
    clearReclinerLists();
    sortBedLists(currentBedList);
    sortReclinerLists(currentReclinerList);
    hours = time;
    if (DEUBUG_BED_SIM) {
      //      System.out.println("TIME: " + hours);
    }
    if (PRINT_BED_SIM) {
      printBedReclinerLists();
    }
    setNumBedsToSend();
    setNumReclinersToSend();
    updateDirtyBeds();
    updateCleaningBeds();
    updateAwaitingTransportBeds();
    updateTemporarilyOutsideBeds();
    updateInUseBeds();
    updateStoredBeds();
    updateDirtyRecliners();
    updateCleaningRecliners();
    updateInRoomRecliners();
    updateStoredRecliners();
    ArrayList<MedEquipmentSimulation> fullList = newBedList;
    fullList.addAll(newReclinerList);
    return newBedList;
  }

  /**
   * Sort into static lists based on status
   *
   * @param fullBedList
   */
  private static void sortBedLists(ArrayList<MedEquipmentSimulation> fullBedList) {
    Beds_List = (ArrayList<MedEquipmentSimulation>) fullBedList.clone();
    for (MedEquipmentSimulation eqp : fullBedList) {
      switch (eqp.getStatus().trim().toUpperCase(Locale.ROOT)) {
        case "IN-USE":
          Beds_InRoom.add(eqp);
          break;

        case "DIRTY":
          Beds_Dirty.add(eqp);
          break;

        case "CLEANING":
          Beds_Cleaning.add(eqp);
          break;

        case "STORED":
          Beds_Stored.add(eqp);
          break;

        case "AWAITING TRANSPORT":
          Beds_AwaitingTransport.add(eqp);
          break;

        case "TEMPORARILY OUTSIDE":
          Beds_TemporarilyOutside.add(eqp);
          break;
      }
    }
  }

  /**
   * Sort into static lists based on status
   *
   * @param fullReclinerList
   */
  private static void sortReclinerLists(ArrayList<MedEquipmentSimulation> fullReclinerList) {
    Recliners_List = (ArrayList<MedEquipmentSimulation>) fullReclinerList.clone();
    for (MedEquipmentSimulation eqp : fullReclinerList) {
      switch (eqp.getStatus().trim().toUpperCase(Locale.ROOT)) {
        case "IN-USE":
          Recliners_InRoom.add(eqp);
          break;

        case "DIRTY":
          Recliners_Dirty.add(eqp);
          break;

        case "CLEANING":
          Recliners_Cleaning.add(eqp);
          break;

        case "STORED":
          Recliners_Stored.add(eqp);
          break;
      }
    }
  }

  /** Clears static beds lists */
  private static void clearBedLists() {
    newBedList.clear();
    Beds_InRoom.clear();
    Beds_Dirty.clear();
    Beds_Cleaning.clear();
    Beds_AwaitingTransport.clear();
    Beds_TemporarilyOutside.clear();
    Beds_Stored.clear();
    Beds_List.clear();
    Beds_StillActive.clear();
  }

  /** Clears static recliners lists */
  private static void clearReclinerLists() {
    newReclinerList.clear();
    Recliners_InRoom.clear();
    Recliners_Cleaning.clear();
    Recliners_Dirty.clear();
    Recliners_Stored.clear();
  }

  /** prints static lists to terminal */
  private static void printBedLists() {
    System.out.println('\n' + "HOUR: " + hours);
    System.out.println("In Use Beds" + "(" + Beds_InRoom.size() + "): " + Beds_InRoom);
    System.out.println("Dirty Beds" + "(" + Beds_Dirty.size() + "): " + Beds_Dirty);
    System.out.println("Cleaning Beds" + "(" + Beds_Cleaning.size() + "): " + Beds_Cleaning);
    System.out.println("Stored Beds" + "(" + Beds_Stored.size() + "): " + Beds_Stored);
    System.out.println(
        "Awaiting Transport Beds"
            + "("
            + Beds_AwaitingTransport.size()
            + "): "
            + Beds_AwaitingTransport);
    System.out.println(
        "Temporarily Outside Beds"
            + "("
            + Beds_TemporarilyOutside.size()
            + "): "
            + Beds_TemporarilyOutside);
  }

  /** prints static lists to terminal */
  private static void printReclinerLists() {
    System.out.println('\n' + "HOUR: " + hours);
    System.out.println(
        "In Use Recliners" + "(" + Recliners_InRoom.size() + "): " + Recliners_InRoom);
    System.out.println("Dirty Beds" + "(" + Recliners_Dirty.size() + "): " + Recliners_Dirty);
    System.out.println(
        "Cleaning Beds" + "(" + Recliners_Cleaning.size() + "): " + Recliners_Cleaning);
    System.out.println("Stored Beds" + "(" + Recliners_Stored.size() + "): " + Recliners_Stored);
  }

  /** prints static lists to terminal */
  private static void printBedReclinerLists() {
    //    System.out.println('\n' + "-------------------- HOUR: " + hours + "
    // --------------------");
    //    System.out.println("----------BEDS---------");
    //    System.out.println("In Use Beds" + "(" + Beds_InRoom.size() + "): " + Beds_InRoom);
    //    System.out.println("Dirty Beds" + "(" + Beds_Dirty.size() + "): " + Beds_Dirty);
    //    System.out.println("Cleaning Beds" + "(" + Beds_Cleaning.size() + "): " + Beds_Cleaning);
    //    System.out.println("Stored Beds" + "(" + Beds_Stored.size() + "): " + Beds_Stored);
    //    System.out.println(
    //        "Awaiting Transport Beds"
    //            + "("
    //            + Beds_AwaitingTransport.size()
    //            + "): "
    //            + Beds_AwaitingTransport);
    //    System.out.println(
    //        "Temporarily Outside Beds"
    //            + "("
    //            + Beds_TemporarilyOutside.size()
    //            + "): "
    //            + Beds_TemporarilyOutside);
    //    System.out.println('\n' + "----------RECLINERS---------");
    //    System.out.println(
    //        "In Use Recliners" + "(" + Recliners_InRoom.size() + "): " + Recliners_InRoom);
    //    System.out.println("Dirty Recliners" + "(" + Recliners_Dirty.size() + "): " +
    // Recliners_Dirty);
    //    System.out.println(
    //        "Cleaning Recliners" + "(" + Recliners_Cleaning.size() + "): " + Recliners_Cleaning);
    //    System.out.println(
    //        "Stored Recliners" + "(" + Recliners_Stored.size() + "): " + Recliners_Stored);
  }

  /**
   * Based on conditions decide how many beds to send to rooms this iteration
   *
   * @return numBeds to send out of storage this round
   */
  private static void setNumBedsToSend() {
    // Base Number of beds which is equal to the number that will be leaving the floor
    double basis = Beds_Dirty.size() + Beds_AwaitingTransport.size();
    double totalNumBeds = Beds_List.size();
    double peakTimeMod = 0, inStorageMod = 0;

    // If the time is between 10am and 3pm
    if ((hours % 24) > 9 && (hours & 24) < 15) {
      peakTimeMod = basis * 0.45;
    } else {
      peakTimeMod = basis * -0.25;
    }

    // If there are less than 20% of beds in storage, slow release rate
    if ((Beds_Stored.size() / totalNumBeds) < 0.35) {
      inStorageMod = basis * -0.35;
    }

    int retVal = (int) Math.round(basis + peakTimeMod + inStorageMod);
    // Safeguard for ensuring we don't decide to send more than we have
    if (retVal >= Beds_Stored.size()) {
      retVal = Beds_Stored.size() - 1;
    }

    if (DEUBUG_BED_SIM) {
      //      System.out.println("Bed Basis " + basis);
      //      System.out.println("Bed Time Mod " + peakTimeMod);
      //      System.out.println("Bed Storage Mod " + inStorageMod);
      //      System.out.println("Beds to Send " + retVal);
    }
    numBedsToSend = retVal;
  }

  /**
   * Based on conditions decide how many recliners to send to rooms this iteration
   *
   * @return numRecliners to send out of storage this round
   */
  public static void setNumReclinersToSend() {
    // Base Number of beds which is equal to the number that will be leaving the floor
    double basis = Recliners_Dirty.size();
    double totalNumRecliners = Recliners_List.size();
    double peakTimeMod = 0, inStorageMod = 0;

    // If the time is between 10am and 3pm
    if ((hours % 24) > 10 && (hours & 24) < 15) {
      peakTimeMod = basis * 0.1;
    } else {
      peakTimeMod = basis * -0.1;
    }

    // If there are more than 20% of recliners in storage, increase release rate
    if ((Recliners_Stored.size() / 6) > 0.2) {
      inStorageMod = basis * +0.55;
    }

    int retVal = (int) Math.round(basis + peakTimeMod + inStorageMod);

    // Safeguard for ensuring we don't decide to send more than we have
    if (retVal > Recliners_Stored.size()) {
      retVal = Recliners_Stored.size();
    }
    if (DEUBUG_BED_SIM) {
      //      System.out.println("Recliner Basis " + basis);
      //      System.out.println("Recliner Time Mod " + peakTimeMod);
      //      System.out.println("Recliner Storage Mod " + inStorageMod);
      //      System.out.println("Recliners to Send " + retVal);
    }
    //    System.out.println("Recliners to Send " + retVal);
    numReclinersToSend = retVal;
  }

  /** Update Dirty Beds */
  private static void updateDirtyBeds() {
    for (MedEquipmentSimulation bed : Beds_Dirty) {
      bed.setCleaningEndTime(hours + BED_CLEANING_TIME);
      bed.setStatus("Cleaning");
      bed.setCurrLoc("GSTOR001L1");
      if (DEUBUG_BED_SIM) {
        //        System.out.println("Bed # " + bed.getMedID() + " is now in cleaning");
      }
      newBedList.add(bed);
    }
  }

  /** Update Cleaning Beds */
  private static void updateCleaningBeds() {
    for (MedEquipmentSimulation bed : Beds_Cleaning) {
      if (bed.getCleaningEndTime() <= hours) {
        bed.setStatus("Stored");
        bed.setCurrLoc("GSTOR001L1");
        if (DEUBUG_BED_SIM) {
          //          System.out.println("Bed # " + bed.getMedID() + " is now in storage");
        }
      }
      newBedList.add(bed);
    }
  }

  // TODO use list of valid locations here
  private static void updateAwaitingTransportBeds() {
    for (MedEquipmentSimulation bed : Beds_AwaitingTransport) {
      bed.setStatus("In-Use");
      int timeToDie = ThreadLocalRandom.current().nextInt(1, 4);
      bed.setInRoomEndTime(hours + timeToDie);
      //      int rnd = new Random().nextInt(Beds_List.size());
      bed.setCurrLoc("FDEPT00101");
      if (DEUBUG_BED_SIM) {
        //        System.out.println("Bed # " + bed.getMedID() + " has been transported");
      }
      newBedList.add(bed);
    }
  }

  /** Update In-Room Beds */
  private static void updateInUseBeds() {
    for (MedEquipmentSimulation bed : Beds_InRoom) {
      if (bed.getInRoomEndTime() <= hours) {
        bed.setStatus("Dirty");
        bed.setCurrLoc(Simulation.getCorrespondingLocation(bed.getCurrLoc()));
        if (DEUBUG_BED_SIM) {
          //          System.out.println("Bed # " + bed.getMedID() + " is now dirty");
        }
        newBedList.add(bed);
      } else if (ThreadLocalRandom.current().nextInt(50) == 7) {
        bed.setStatus("Awaiting Transport");
        bed.setCurrLoc(Simulation.getCorrespondingLocation(bed.getCurrLoc()));
        if (DEUBUG_BED_SIM) {
          //          System.out.println("Bed # " + bed.getMedID() + " is now awaiting transport");
        }
        newBedList.add(bed);
      } else {
        if (DEUBUG_BED_SIM) {
          //          System.out.println(
          //              "Bed # "
          //                  + bed.getMedID()
          //                  + " has "
          //                  + (bed.getInRoomEndTime() - hours)
          //                  + " hours remaining");
        }
        Beds_StillActive.add(bed);
      }
    }
  }

  /** Update Temporarily Outside Beds */
  private static void updateTemporarilyOutsideBeds() {
    for (MedEquipmentSimulation bed : Beds_TemporarilyOutside) {
      // If it is time for the bed to go back in the room
      if (bed.getOutsideEndTime() <= hours) {
        // If the bed is outside the room, but it is time for it to be dirty
        if (bed.getInRoomEndTime() <= hours) {
          bed.setStatus("Dirty");
          if (DEUBUG_BED_SIM) {
            //            System.out.println("Bed # " + bed.getMedID() + " is now dirty");
          }
        } else {
          bed.setStatus("In-Use");
          // The location is currently the hallway which is the value of the hashmap, so we need the
          // key
          bed.setCurrLoc(findKey(bed.getCurrLoc()));
          if (DEUBUG_BED_SIM) {
            //            System.out.println(
            //                "Bed # " + bed.getMedID() + " is now back in room " +
            // bed.getCurrLoc());
          }
        }
      }
      newBedList.add(bed);
    }
  }

  /** Update Stored Beds */
  private static void updateStoredBeds() {
    setCurrentValidLocations();
    if (numBedsToSend > currentValidBedLocations.size()) {
      numBedsToSend = currentValidBedLocations.size();
    }
    for (int i = 0; i < numBedsToSend; i++) {
      // generate random int to select from list of
      int rnd = new Random().nextInt(currentValidBedLocations.size());
      String rndLoc = currentValidBedLocations.get(rnd);
      currentValidBedLocations.remove(rnd);
      MedEquipmentSimulation tempBed = Beds_Stored.get(0);
      Beds_Stored.remove(0);
      tempBed.setCurrLoc(rndLoc);
      tempBed.setStatus("In-Use");
      // TODO change hours to be more realistic
      int rndTTD = ThreadLocalRandom.current().nextInt(12, 48);
      tempBed.setInRoomEndTime(hours + rndTTD);
      if (DEUBUG_BED_SIM) {
        //        System.out.println(
        //            "Bed # "
        //                + tempBed.getMedID()
        //                + " will be sent from storage to room "
        //                + tempBed.getCurrLoc());
      }
      newBedList.add(tempBed);
    }

    for (MedEquipmentSimulation bed : Beds_Stored) {
      newBedList.add(bed);
    }
  }

  /** Update Cleaning Recliners */
  private static void updateCleaningRecliners() {
    for (MedEquipmentSimulation recliner : Recliners_Cleaning) {
      if (recliner.getCleaningEndTime() <= hours) {
        recliner.setStatus("Stored");
        recliner.setCurrLoc("GSTOR00201");
        if (DEUBUG_BED_SIM) {
          //          System.out.println("Recliner # " + recliner.getMedID() + " is now in
          // storage");
        }
      }
      newReclinerList.add(recliner);
    }
  }

  /** Update Dirty Recliners */
  private static void updateDirtyRecliners() {
    for (MedEquipmentSimulation recliner : Recliners_Dirty) {
      recliner.setCleaningEndTime(hours + BED_CLEANING_TIME);
      recliner.setStatus("Cleaning");
      recliner.setCurrLoc("GSTOR00201");
      if (DEUBUG_BED_SIM) {
        //        System.out.println("Recliner # " + recliner.getMedID() + " is now in cleaning");
      }
      newReclinerList.add(recliner);
    }
  }

  /** Update In-Use Recliners */
  private static void updateInRoomRecliners() {
    for (MedEquipmentSimulation recliner : Recliners_InRoom) {
      if (recliner.getInRoomEndTime() <= hours) {
        recliner.setStatus("Dirty");
        recliner.setCurrLoc(Simulation.getCorrespondingLocation(recliner.getCurrLoc()));
        if (DEUBUG_BED_SIM) {
          //          System.out.println("Recliner # " + recliner.getMedID() + " is now dirty");
        }
      } else {
        if (DEUBUG_BED_SIM) {
          //          System.out.println(
          //              "Recliner # "
          //                  + recliner.getMedID()
          //                  + " has "
          //                  + (recliner.getInRoomEndTime() - hours)
          //                  + " hours remaining");
        }
      }
      newReclinerList.add(recliner);
    }
  }

  private static void updateStoredRecliners() {
    if (Beds_StillActive.size() < 1) {
      if (DEUBUG_BED_SIM) {
        //        System.out.println("No Active Beds, No Recliners removed from storage");
      }
    } else {
      for (int i = 0; i < numReclinersToSend; i++) {
        int rnd = ThreadLocalRandom.current().nextInt(Beds_StillActive.size());
        int rndTTD = ThreadLocalRandom.current().nextInt(2, 5);
        MedEquipmentSimulation tempRec = Recliners_Stored.get(0);
        Recliners_Stored.remove(0);
        MedEquipmentSimulation tempBed = Beds_StillActive.get(rnd);
        Beds_StillActive.remove(rnd);
        tempRec.setStatus("In-Use");
        tempRec.setInRoomEndTime(rndTTD);
        tempRec.setCurrLoc(tempBed.getCurrLoc());
        tempBed.setStatus("Temporarily Outside");
        tempBed.setOutsideEndTime(rndTTD);
        tempBed.setCurrLoc(Simulation.getCorrespondingLocation(tempBed.getCurrLoc()));
        if (DEUBUG_BED_SIM) {
          //          System.out.println(
          //              "Recliner # "
          //                  + tempRec.getMedID()
          //                  + " will be sent from storage to room "
          //                  + tempRec.getCurrLoc());
          //          System.out.println(
          //              "Bed # " + tempBed.getMedID() + " will be moved outside to " +
          // tempBed.getCurrLoc());
        }
        newReclinerList.add(tempRec);
        newBedList.add(tempBed);
      }
    }
    newBedList.addAll(Beds_StillActive);
    for (MedEquipmentSimulation recliner : Recliners_Stored) {
      newReclinerList.add(recliner);
    }
  }

  /**
   * Update list of currently valid locations(rooms without a bed) by checking each item in the list
   * and seeing if there is a bed that also has that location
   */
  private static void setCurrentValidLocations() {
    currentValidBedLocations.clear();
    currentValidBedLocations = (ArrayList<String>) Simulation.validLocations.clone();

    ArrayList<String> tempCurValidLoc = new ArrayList<>();
    boolean valid = false;
    for (String strLoc : currentValidBedLocations) {
      valid = true;
      for (MedEquipmentSimulation bed : Beds_StillActive) {
        if (bed.getCurrLoc().equals(strLoc)) {
          if (DEUBUG_BED_SIM) {
            //            System.out.println("Location " + strLoc + " has bed # " + bed.getMedID());
          }
          valid = false;
        }
      }

      if (valid) {
        tempCurValidLoc.add(strLoc);
        if (DEUBUG_BED_SIM) {
          //          System.out.println("No beds in room " + strLoc);
        }
      }
    }
    currentValidBedLocations.clear();
    currentValidBedLocations.addAll(tempCurValidLoc);

    if (DEUBUG_BED_SIM) {
      //      System.out.println(
      //          "Current Valid Locations: "
      //              + "("
      //              + currentValidBedLocations.size()
      //              + "): "
      //              + currentValidBedLocations);
    }
  }

  // TODO test/fix this
  private static String findKey(String value) {
    String key = null;
    for (Map.Entry<String, String> entry : Simulation.corrLoc.entrySet()) {
      if (entry.getValue().equals(value)) {
        //        System.out.println("The key for value " + value + " is " + entry.getKey());
        key = entry.getKey();
      }
    }
    if (key == null) {
      System.err.println("HELP: " + value);
      key = "GPATI00503";
    }
    return key;
  }
}
