package edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService;

import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class AudioVisualRequest extends Request {
  private Integer audioVisualID;
  private String priority;

  public AudioVisualRequest(
      Integer reqID,
      String nodeID,
      Integer requesterID,
      Integer completerID,
      long submittedTime,
      long completedTime,
      Integer patientID,
      String status,
      String notes,
      Integer audioVisualID,
      String priority) {
    super(
        reqID,
        nodeID,
        requesterID,
        completerID,
        submittedTime,
        completedTime,
        patientID,
        "AudioVisual",
        status,
        notes);
    this.audioVisualID = audioVisualID;
    this.priority = priority;
  }

  public AudioVisualRequest(Request request, int audioVisualID, String priority) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "AudioVisual",
        request.getRequestStatus(),
        request.getNotes());
    this.audioVisualID = audioVisualID;
    this.priority = priority;
  }

  public AudioVisualRequest() {}

  public int getAudioVisualID() {
    return audioVisualID;
  }

  public void setAudioVisualID(Integer audioVisualID) {
    this.audioVisualID = audioVisualID;
  }

  public String getPriority() {
    return priority;
  }

  public void setPriority(String priority) {
    this.priority = priority;
  }

  @Override
  public String toString() {
    return super.toString() + "," + audioVisualID + "," + priority;
  }

  public boolean equals(Object obj) {
    AudioVisualRequest o = (AudioVisualRequest) obj;
    return this.audioVisualID.equals(o.audioVisualID)
        && this.requestID.equals(o.requestID)
        && this.priority.equals(o.priority);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, audioVisualID));
  }
}
