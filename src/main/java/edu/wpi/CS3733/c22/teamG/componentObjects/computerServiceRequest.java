package edu.wpi.CS3733.c22.teamG.componentObjects;

public class computerServiceRequest {

  private String location;
  private String issueType;
  private String issueDescription;

  public computerServiceRequest(String location, String issueType, String issueDescription) {
    this.location = location;
    this.issueType = issueType;
    this.issueDescription = issueDescription;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getIssueType() {
    return issueType;
  }

  public void setIssueType(String issueType) {
    this.issueType = issueType;
  }

  public String getIssueDescription() {
    return issueDescription;
  }

  public void setIssueDescription(String issueDescription) {
    this.issueDescription = issueDescription;
  }
}
