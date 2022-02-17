package edu.wpi.GoldenGandaberundas.componentObjects;

public class religiousRequest {

  private String religion;
  private String serviceType;
  private String serviceDescription;

  public religiousRequest(String religion, String serviceType, String serviceDescription) {
    this.religion = religion;
    this.serviceType = serviceType;
    this.serviceDescription = serviceDescription;
  }

  public String getReligion() {
    return religion;
  }

  public void setReligion(String religion) {
    this.religion = religion;
  }

  public String getServiceType() {
    return serviceType;
  }

  public void setServiceType(String serviceType) {
    this.serviceType = serviceType;
  }

  public String getServiceDescription() {
    return serviceDescription;
  }

  public void setServiceDescription(String serviceDescription) {
    this.serviceDescription = serviceDescription;
  }
}
