package edu.wpi.GoldenGandaberundas.tableControllers.LabRequestService;

import java.util.Objects;

public class LabService {
  private int labID;
  private String labType;
  private String description;

  public LabService(int labID, String labType, String description) {
    this.labID = labID;
    this.labType = labType;
    this.description = description;
  }

  public LabService() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    LabService that = (LabService) o;
    return labID == that.labID
        && Objects.equals(labType, that.labType)
        && Objects.equals(description, that.description);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(labID).append(",").append(labType).append(",").append(description);
    return sb.toString();
  }

  public int getLabID() {
    return labID;
  }

  public void setLabID(int labID) {
    this.labID = labID;
  }

  public String getLabType() {
    return labType;
  }

  public void setLabType(String labType) {
    this.labType = labType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}
