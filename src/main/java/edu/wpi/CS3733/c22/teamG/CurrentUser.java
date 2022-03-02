package edu.wpi.CS3733.c22.teamG;

import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Employee;

public class CurrentUser {
  private static Employee user = null;

  public static void setUser(Employee newUser) {
    System.out.println("User set to: " + newUser.getFName() + " " + newUser.getLName());
    user = newUser;
  }

  public static void clearUser() {
    user = null;
  }

  public static Employee getUser() {
    return user;
  }
}
