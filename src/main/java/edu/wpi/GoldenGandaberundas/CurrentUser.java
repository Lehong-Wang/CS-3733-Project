package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;

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
