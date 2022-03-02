package edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService;

import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class LaundryRequest extends Request {
  private Integer laundryID;

  public LaundryRequest(
      Integer reqID,
      String nodeID,
      int requesterID,
      int completerID,
      long submittedTime,
      long completedTime,
      String status,
      String notes,
      int laundryID) {
    super(
        reqID,
        nodeID,
        requesterID,
        completerID,
        submittedTime,
        completedTime,
        null,
        "LaundryService",
        status,
        notes);
    this.laundryID = laundryID;
  }

  public LaundryRequest(Request request, int laundryID) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "LaundryService",
        request.getRequestStatus(),
        request.getNotes());
    this.laundryID = laundryID;
  }

  public LaundryRequest() {}

  @Override
  public String toString() {
    return super.toString() + "," + laundryID;
  }

  public Integer getLaundryID() {
    return laundryID;
  }

  public void setLaundryID(Integer laundryID) {
    this.laundryID = laundryID;
  }

  public boolean equals(Object obj) {
    LaundryRequest o = (LaundryRequest) obj;
    return this.laundryID.equals(o.laundryID) && this.requestID.equals(o.requestID);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, laundryID));
  }
}
