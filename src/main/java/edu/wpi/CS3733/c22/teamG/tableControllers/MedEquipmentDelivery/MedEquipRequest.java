package edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery;

import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class MedEquipRequest extends Request {
  private Integer medID;

  public MedEquipRequest(
      Integer requestID,
      String locationID,
      Integer empInitiated,
      Integer empCompleter,
      long timeStart,
      long timeEnd,
      Integer patientID,
      String requestStatus,
      String notes,
      int medID) {
    super(
        requestID,
        locationID,
        empInitiated,
        empCompleter,
        timeStart,
        timeEnd,
        patientID,
        "MedEquipDelivery",
        requestStatus,
        notes);
    this.medID = medID;
  }

  public MedEquipRequest(Request request, int medID) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "MedEquipDelivery",
        request.getRequestStatus(),
        request.getNotes());
    this.medID = medID;
  }

  public MedEquipRequest() {
    super();
  }

  //  public ArrayList<Integer> getPK() {
  //    return new ArrayList<Integer>(Arrays.asList(requestID, medID));
  //  }

  @Override
  public String toString() {
    return super.toString() + "," + requestID + ", " + medID;
  }

  public int getReqID() {
    return requestID;
  }

  public void setReqID(int reqID) {
    this.requestID = reqID;
  }

  public int getMedID() {
    return medID;
  }

  public void setMedID(int medID) {
    this.medID = medID;
  }

  public boolean equals(Object obj) {
    MedEquipRequest o = (MedEquipRequest) obj;
    return this.medID.equals(o.medID) && this.requestID.equals(o.requestID);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(this.requestID, this.medID));
  }
}
