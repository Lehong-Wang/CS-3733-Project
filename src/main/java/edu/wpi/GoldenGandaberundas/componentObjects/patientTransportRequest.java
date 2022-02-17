package edu.wpi.GoldenGandaberundas.componentObjects;

public class patientTransportRequest {

  private String patientName;
  private String locTo;
  private String locFrom;

  public patientTransportRequest(String patientName, String locTo, String locFrom) {
    this.patientName = patientName;
    this.locTo = locTo;
    this.locFrom = locFrom;
  }

  public String getPatientName() {
    return patientName;
  }

  public void setPatientName(String patientName) {
    this.patientName = patientName;
  }

  public String getLocTo() {
    return locTo;
  }

  public void setLocTo(String locTo) {
    this.locTo = locTo;
  }

  public String getLocFrom() {
    return locFrom;
  }

  public void setLocFrom(String locFrom) {
    this.locFrom = locFrom;
  }
}
