package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

public class Permission {
  private int permID;
  private String type;
  private String permDescription;

  public Permission(int permID, String type, String permDescription) {
    this.permID = permID;
    this.type = type;
    this.permDescription = permDescription;
  }

  public Permission() {}

  public int getPermID() {
    return permID;
  }

  public void setPermID(int permID) {
    this.permID = permID;
  }

  public String getPermDescription() {
    return permDescription;
  }

  public void setPermDescription(String permDescription) {
    this.permDescription = permDescription;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String toString() {
    return permID + "," + permDescription;
  }

  public boolean equals(Object o) {
    Permission perm = (Permission) o;
    return this.permID == perm.getPermID()
        && this.type.equals(perm.getType())
        && this.permDescription.equals(perm.getPermDescription());
  }
}
