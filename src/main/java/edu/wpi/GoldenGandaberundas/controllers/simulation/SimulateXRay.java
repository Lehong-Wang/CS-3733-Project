package edu.wpi.GoldenGandaberundas.controllers.simulation;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SimulateXRay {

  private static int hours = 0;
  private static MedEquipmentSimulation XRay;
  private static final boolean DEBUG_XRAY_SIM = false;
  private static final boolean PRINT_XRAY_SIM = false;

  public static MedEquipmentSimulation updateXRay(MedEquipmentSimulation currentXRay, int time) {
    hours = time;
    XRay = currentXRay;
    // If XRay is in a room, check TTL
    if (XRay.getStatus().trim().toUpperCase(Locale.ROOT).equals("IN-USE")) {
      // If time is expired, sent to storage
      if (XRay.getInRoomEndTime() <= hours) {
        XRay.setStatus("STORED");
        XRay.setCurrLoc("GSTOR00103");
        if (DEBUG_XRAY_SIM) {
          System.out.println("X-Ray is now in storage");
        }
      } else {
        if (DEBUG_XRAY_SIM) {
          System.out.println("X-Ray has " + (XRay.getInRoomEndTime() - hours) + " hours remaining");
        }
      }
    } else if (XRay.getStatus().trim().toUpperCase(Locale.ROOT).equals("STORED")) {
      XRay.setStatus("In-Use");
      int rnd = new Random().nextInt(Simulation.validLocations.size());
      String rndLoc = Simulation.validLocations.get(rnd);
      int rndTTD = ThreadLocalRandom.current().nextInt(1, 5);
      XRay.setInRoomEndTime(hours + rndTTD);
      XRay.setCurrLoc(rndLoc);

      if (DEBUG_XRAY_SIM) {
        System.out.println("X-Ray will be sent from storage to room " + XRay.getCurrLoc());
      }
    }
    if (PRINT_XRAY_SIM) {
      printXRay();
    }
    return XRay;
  }

  private static void printXRay() {
    System.out.println('\n' + "----------XRAY---------");
    System.out.println("X-Ray: [" + XRay + "]");
  }
}
