package edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService;

import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class ComputerRequest extends Request {
  private Integer computerID;
  private String problemType;
  private String priority;

  public ComputerRequest(
      Integer reqID,
      String nodeID,
      Integer requesterID,
      Integer completerID,
      long submittedTime,
      long completedTime,
      Integer patientID,
      String status,
      String notes,
      Integer computerID,
      String problemType,
      String priority) {
    super(
        reqID,
        nodeID,
        requesterID,
        completerID,
        submittedTime,
        completedTime,
        patientID,
        "Computer",
        status,
        notes);
    this.computerID = computerID;
    this.problemType = problemType;
    this.priority = priority;
  }

  public ComputerRequest(Request request, int computerID, String problemType, String priority) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "Computer",
        request.getRequestStatus(),
        request.getNotes());
    this.computerID = computerID;
    this.problemType = problemType;
    this.priority = priority;
  }

  public ComputerRequest() {}

  public int getComputerID() {
    return computerID;
  }

  public void setComputerID(int computerID) {
    this.computerID = computerID;
  }

  public String getProblemType() {
    return problemType;
  }

  public void setProblemType(String problemType) {
    this.problemType = problemType;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return super.toString() + "," + computerID + "," + problemType + "," + priority;
  }

  public boolean equals(Object obj) {
    ComputerRequest o = (ComputerRequest) obj;
    return this.computerID.equals(o.computerID)
        && this.problemType.equals(o.problemType)
        && this.priority.equals(o.priority)
        && this.requestID.equals(o.requestID);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, computerID));
  }
}
