package edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService;

import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class GiftRequest extends Request {
  private Integer giftID;
  private Integer quantity;

  public GiftRequest(
      Integer reqID,
      String nodeID,
      Integer requesterID,
      Integer completerID,
      long submittedTime,
      long completedTime,
      Integer patientID,
      String status,
      String notes,
      int giftID,
      int quantity) {
    super(
        reqID,
        nodeID,
        requesterID,
        completerID,
        submittedTime,
        completedTime,
        patientID,
        "GiftFloral",
        status,
        notes);
    this.giftID = giftID;
    this.quantity = quantity;
  }

  public GiftRequest(Request request, int giftID, int quantity) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "GiftFloral",
        request.getRequestStatus(),
        request.getNotes());
    this.giftID = giftID;
    this.quantity = quantity;
  }

  public GiftRequest() {}

  public int getGiftID() {
    return giftID;
  }

  public void setGiftID(int giftID) {
    this.giftID = giftID;
  }

  public void setGiftID(Integer giftID) {
    this.giftID = giftID;
  }

  public Integer getQuantity() {
    return quantity;
  }

  public void setQuantity(Integer quantity) {
    this.quantity = quantity;
  }

  @Override
  public String toString() {
    return super.toString() + "," + giftID + "," + quantity;
  }

  public boolean equals(Object obj) {
    GiftRequest o = (GiftRequest) obj;
    return this.giftID.equals(o.giftID)
        && this.quantity == o.quantity
        && this.requestID.equals(o.requestID);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, giftID));
  }
}
