package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

public class Location {
  private String nodeID;
  private int xcoord;
  private int ycoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;

  public Location(
      String nodeID,
      int xcoord,
      int ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  public String getNodeID() {
    return nodeID;
  }

  public void setNodeID(String nodeID) {
    this.nodeID = nodeID;
  }

  public int getXcoord() {
    return xcoord;
  }

  public void setXcoord(int xcoord) {
    this.xcoord = xcoord;
  }

  public int getYcoord() {
    return ycoord;
  }

  public void setYcoord(int ycoord) {
    this.ycoord = ycoord;
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
    sb.append(xcoord);
    sb.append(",");
    sb.append(ycoord);
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
        && this.xcoord == anotherLoc.xcoord
        && this.ycoord == anotherLoc.ycoord
        && this.floor.equals(anotherLoc.floor)
        && this.building.equals(anotherLoc.building)
        && this.nodeType.equals(anotherLoc.nodeType)
        && this.longName.equals(anotherLoc.longName)
        && this.shortName.equals(anotherLoc.shortName);
  }
}
