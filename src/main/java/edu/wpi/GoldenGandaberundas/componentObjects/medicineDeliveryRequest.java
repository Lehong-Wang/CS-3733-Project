package edu.wpi.GoldenGandaberundas.componentObjects;

public class medicineDeliveryRequest {

  String patientLocation;
  String time;
  int deliveryNumber;

  public medicineDeliveryRequest(String patientLocation, String time, int deliveryNumber) {
    this.patientLocation = patientLocation;
    this.time = time;
    this.deliveryNumber = deliveryNumber;
  }

  public String getPatientLocation() {
    return patientLocation;
  }

  public void setPatientLocation(String patientLocation) {
    this.patientLocation = patientLocation;
  }

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public int getDeliveryNumber() {
    return deliveryNumber;
  }

  public void setDeliveryNumber(int deliveryNumber) {
    this.deliveryNumber = deliveryNumber;
  }
}
