package edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects;

public class EmployeePermission {
  private int empID;
  private int permID;

  public EmployeePermission(int empID, int permID) {
    this.empID = empID;
    this.permID = permID;
  }

  public EmployeePermission() {}

  public int getEmpID() {
    return empID;
  }

  public void setEmpID(int empID) {
    this.empID = empID;
  }

  public int getPermID() {
    return permID;
  }

  public void setPermID(int permID) {
    this.permID = permID;
  }

  public String toString() {
    return empID + "," + permID;
  }

  public boolean equals(Object o) {
    EmployeePermission empPerm = (EmployeePermission) o;
    return this.permID == empPerm.getPermID() && this.empID == empPerm.getEmpID();
  }
}
