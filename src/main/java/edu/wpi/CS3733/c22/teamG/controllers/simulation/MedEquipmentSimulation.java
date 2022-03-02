package edu.wpi.CS3733.c22.teamG.controllers.simulation;

import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipment;

public class MedEquipmentSimulation extends MedEquipment {
  private int inRoomEndTime, cleaningEndTime, outsideEndTime;

  public MedEquipmentSimulation(int medID, String type, String status, String currLoc) {
    super(medID, type, status, currLoc);
    this.inRoomEndTime = 0;
    this.cleaningEndTime = 0;
    this.outsideEndTime = 0;
  }

  public int getInRoomEndTime() {
    return inRoomEndTime;
  }

  public void setInRoomEndTime(int TTD) {
    this.inRoomEndTime = TTD;
  }

  public int getCleaningEndTime() {
    return cleaningEndTime;
  }

  public void setCleaningEndTime(int cleaningEndTime) {
    this.cleaningEndTime = cleaningEndTime;
  }

  public int getOutsideEndTime() {
    return outsideEndTime;
  }

  public void setOutsideEndTime(int outsideEndTime) {
    this.outsideEndTime = outsideEndTime;
  }
}
