package edu.wpi.GoldenGandaberundas.tableControllers.Requests;

// import org.apache.commons.net.nntp.NNTP;
// import org.apache.commons.net.nntp.NNTPClient;

import java.util.ArrayList;
import java.util.Arrays;

public class Request{
  protected Integer requestID = -1;
  protected String locationID = "";
  protected Integer empInitiated = -1;
  protected Integer empCompleter = -1;
  protected long timeStart = 0;
  protected long timeEnd = 0;
  protected Integer patientID = null;
  protected String requestType = "";
  protected String requestStatus = "";
  protected String notes = "";

  /**
   * Constructor used primarily by DB Table accessor
   *
   * @param requestID the unique ID used to identify the request
   * @param locationID the location the service is requested
   * @param empInitiated the employee who initiated the request
   * @param empCompleter the employee who completed the request
   * @param timeStart the time the request was submitted
   * @param timeEnd the time the service was completed
   * @param patientID the patient the service is for
   * @param requestType the service that was rendered
   * @param requestStatus the current status of the request fulfillment
   * @param notes additional information the service provider needs
   */
  public Request(
      Integer requestID,
      String locationID,
      Integer empInitiated,
      Integer empCompleter,
      long timeStart,
      long timeEnd,
      Integer patientID,
      String requestType,
      String requestStatus,
      String notes) {
    this.requestID = requestID;
    this.locationID = locationID;
    this.empInitiated = empInitiated;
    this.empCompleter = empCompleter;
    this.timeStart = timeStart;
    this.timeEnd = timeEnd;
    this.patientID = patientID;
    this.requestType = requestType;
    this.requestStatus = requestStatus;
    this.notes = notes;
  }

  /**
   * WIP NOT YET DONE to be used by UI to automatically implement the reqID, type and time stamps
   *
   * @param locationID the location that the service is requires
   * @param empInitiated the employee who began the request
   * @param empCompleter the employee who completed the request
   * @param patientID the patient the service is for
   * @param notes additional description the service provider may need
   */
  public Request(
      String locationID,
      Integer empInitiated,
      Integer empCompleter,
      Integer patientID,
      String notes) {
    this.requestID = requestID;
    this.locationID = locationID;
    this.empInitiated = empInitiated;
    this.empCompleter = empCompleter;

    this.patientID = patientID;

    this.notes = notes;
  }

  public Request() {}

  public Integer getRequestID() {
    return requestID;
  }

  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(this.requestID));
  }

  public void setRequestID(Integer requestID) {
    this.requestID = requestID;
  }

  public String getLocationID() {
    return locationID;
  }

  public void setLocationID(String locationID) {
    this.locationID = locationID;
  }

  public Integer getEmpInitiated() {
    return empInitiated;
  }

  public void setEmpInitiated(Integer empInitiated) {
    this.empInitiated = empInitiated;
  }

  public Integer getEmpCompleter() {
    return empCompleter;
  }

  public void setEmpCompleter(Integer empCompleter) {
    this.empCompleter = empCompleter;
  }

  public long getTimeStart() {
    return timeStart;
  }

  public void setTimeStart(long timeStart) {
    this.timeStart = timeStart;
  }

  public Long getTimeEnd() {
    return timeEnd;
  }

  public void setTimeEnd(long timeEnd) {
    this.timeEnd = timeEnd;
  }

  public Integer getPatientID() {
    return patientID;
  }

  public void setPatientID(Integer patientID) {
    this.patientID = patientID;
  }

  public String getRequestType() {
    return requestType;
  }

  public void setRequestType(String requestType) {
    this.requestType = requestType;
  }

  public String getRequestStatus() {
    return requestStatus;
  }

  public void setRequestStatus(String requestStatus) {
    this.requestStatus = requestStatus;
  }

  public String getNotes() {
    return notes;
  }

  public void setNotes(String notes) {
    this.notes = notes;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(requestID);
    stringBuilder.append(",");
    stringBuilder.append(locationID);
    stringBuilder.append(",");

    stringBuilder.append(empInitiated);
    stringBuilder.append(",");

    stringBuilder.append(empCompleter);
    stringBuilder.append(",");
    stringBuilder.append(timeStart);
    stringBuilder.append(",");
    stringBuilder.append(timeEnd);
    stringBuilder.append(",");
    stringBuilder.append(patientID);
    stringBuilder.append(",");
    stringBuilder.append(requestType);
    stringBuilder.append(",");
    stringBuilder.append(requestStatus);
    stringBuilder.append(",");
    stringBuilder.append(notes);
    return stringBuilder.toString();
  }
}
