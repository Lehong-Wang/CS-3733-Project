package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import java.util.HashMap;

public class Location {
  private String nodeID;
  private long xCoord;
  private long yCoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;

  public Location(
      String nodeID,
      long xCoord,
      long yCoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xCoord = xCoord;
    this.yCoord = yCoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public Location() {}

  public Location(HashMap hashMap, String pkID) {
    System.out.println(hashMap);
    this.nodeID = pkID;
    this.xCoord = (long) hashMap.get("xCoord");
    this.yCoord = (long) hashMap.get("yCoord");
    this.floor = (String) hashMap.get("floor");
    this.building = (String) hashMap.get("floor");
    this.nodeType = (String) hashMap.get("nodeType");
    this.longName = (String) hashMap.get("longName");
    this.shortName = (String) hashMap.get("shortName");
  }

  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public long getXcoord() {
    return xCoord;
  }

  public void setXcoord(long xCoord) {
    this.xCoord = xCoord;
  }

  public long getYcoord() {
    return yCoord;
  }

  public void setYcoord(long ycoord) {
    this.yCoord = ycoord;
  }

  public String getFloor() {
    return floor;
  }

  public void setFloor(String floor) {
    this.floor = floor;
  }

  public String getBuilding() {
    return building;
  }

  public void setBuilding(String building) {
    this.building = building;
  }

  public String getNodeType() {
    return nodeType;
  }

  public void setNodeType(String nodeType) {
    this.nodeType = nodeType;
  }

  public String getLongName() {
    return longName;
  }

  public void setLongName(String longName) {
    this.longName = longName;
  }

  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(nodeID);
    sb.append(",");
    sb.append(xCoord);
    sb.append(",");
    sb.append(yCoord);
    sb.append(",");
    sb.append(floor);
    sb.append(",");
    sb.append(building);
    sb.append(",");
    sb.append(nodeType);
    sb.append(",");
    sb.append(longName);
    sb.append(",");
    sb.append(shortName);

    return sb.toString();
  }

  public boolean equals(Object o) {
    Location anotherLoc = (Location) o;
    return this.nodeID.equals(anotherLoc.nodeID)
        && this.xCoord == anotherLoc.xCoord
        && this.yCoord == anotherLoc.yCoord
        && this.floor.equals(anotherLoc.floor)
        && this.building.equals(anotherLoc.building)
        && this.nodeType.equals(anotherLoc.nodeType)
        && this.longName.equals(anotherLoc.longName)
        && this.shortName.equals(anotherLoc.shortName);
  }
}
