package edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects;

import edu.wpi.GoldenGandaberundas.CloudController;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import java.util.ArrayList;

public class EmployeeCloud {
  private ArrayList<Employee> objList = null;
  private String tbName = "Employees";

  public EmployeeCloud(ArrayList<Employee> objList) {
    this.objList = objList;
  }

  public void writeTable() {
    CloudController.getInstance().uploadAllDocuments("Employees", objList);
  }

  public Employee getEmployee(String pkid) {
    return (Employee) CloudController.getInstance().getEntry("Employees", pkid, Location.class);
  }

  public ArrayList<Employee> readTable() {
    return CloudController.getInstance().readTable(tbName, Employee.class);
  }

  public boolean editEntry(String pkid, String colname, Object value) {
    return CloudController.getInstance().editEntry(tbName, pkid, colname, value);
  }

  public void addEntry(Employee obj) {
    CloudController.getInstance().addEntry(tbName, obj);
  }
}
