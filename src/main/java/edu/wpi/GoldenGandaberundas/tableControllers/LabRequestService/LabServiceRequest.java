package edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService;

import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class LabServiceRequest extends Request {
  private Integer labServiceID;
  private String priority;

  public LabServiceRequest(
      Integer reqID,
      String nodeID,
      Integer requesterID,
      Integer completerID,
      long submittedTime,
      long completedTime,
      Integer patientID,
      String status,
      String notes,
      Integer labServiceID,
      String priority) {
    super(
        reqID,
        nodeID,
        requesterID,
        completerID,
        submittedTime,
        completedTime,
        patientID,
        "LaboratoryService",
        status,
        notes);
    this.labServiceID = labServiceID;
    this.priority = priority;
  }

  public LabServiceRequest(Request request, int labServiceID, String priority) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "LaboratoryService",
        request.getRequestStatus(),
        request.getNotes());
    this.labServiceID = labServiceID;
    this.priority = priority;
  }

  public LabServiceRequest() {}

  public int getLabServiceID() {
    return labServiceID;
  }

  public void setLabServiceID(Integer labServiceID) {
    this.labServiceID = labServiceID;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return super.toString() + "," + labServiceID + "," + priority;
  }

  public boolean equals(Object obj) {
    LabServiceRequest o = (LabServiceRequest) obj;
    return this.labServiceID.equals(o.labServiceID)
        && this.requestID.equals(o.requestID)
        && this.priority.equals(o.priority);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, labServiceID));
  }
}
