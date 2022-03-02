package edu.wpi.CS3733.c22.teamG.componentObjects;

public class foodDeliveryRequest {

  private String time;
  private String order;
  private String restaurant;
  private double cost;

  // Test constructor for object creation testing TODO Delete this
  public foodDeliveryRequest(String time, String order) {
    this.time = time;
    this.order = order;
  }

  public foodDeliveryRequest(String time, String order, String restaurant, double cost) {
    this.time = time;
    this.order = order;
    this.restaurant = restaurant;
    this.cost = cost;
  }

  /*
  May Cause issues so commented out, being used somewhere?
  public String getTime() {
      return time;
  }

  public void setTime(String time) {
      this.time = time;
  } */

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getRestaurant() {
    return restaurant;
  }

  public void setRestaurant(String restaurant) {
    this.restaurant = restaurant;
  }

  public double getCost() {
    return cost;
  }

  public void setCost(double cost) {
    this.cost = cost;
  }
}
