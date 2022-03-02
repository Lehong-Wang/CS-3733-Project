package edu.wpi.CS3733.c22.teamG.tableControllers.FoodService;

import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class FoodRequest extends Request {
  private Integer foodID;
  private Integer quantity;

  public FoodRequest(
      Integer reqID,
      String nodeID,
      Integer requesterID,
      Integer completerID,
      long submittedTime,
      long completedTime,
      Integer patientID,
      String status,
      String notes,
      int foodID,
      int quantity) {
    super(
        reqID,
        nodeID,
        requesterID,
        completerID,
        submittedTime,
        completedTime,
        patientID,
        "Food",
        status,
        notes);
    this.foodID = foodID;
    this.quantity = quantity;
  }

  public FoodRequest(Request request, int foodID, int quantity) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "Food",
        request.getRequestStatus(),
        request.getNotes());
    this.foodID = foodID;
    this.quantity = quantity;
  }

  public FoodRequest() {}

  public int getFoodID() {
    return foodID;
  }

  public void setFoodID(int foodID) {
    this.foodID = foodID;
  }

  public void setFoodID(Integer foodID) {
    this.foodID = foodID;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return super.toString() + "," + foodID + "," + quantity;
  }

  public boolean equals(Object obj) {
    FoodRequest o = (FoodRequest) obj;
    return this.foodID.equals(o.foodID)
        && this.quantity == o.quantity
        && this.requestID.equals(o.requestID);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, foodID));
  }
}
