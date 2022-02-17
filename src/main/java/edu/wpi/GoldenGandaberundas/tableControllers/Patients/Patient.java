package edu.wpi.GoldenGandaberundas.tableControllers.Patients;

public class Patient {
  private int patientID;
  private String location;
  private String fName;
  private String lName;

  public Patient(int patientID, String location, String fName, String lName) {
    this.patientID = patientID;
    this.location = location;
    this.fName = fName;
    this.lName = lName;
  }

  public Patient() {}

  public int getPatientID() {
    return patientID;
  }

  public void setPatientID(int patientID) {
    this.patientID = patientID;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getfName() {
    return fName;
  }

  public void setfName(String fName) {
    this.fName = fName;
  }

  public String getlName() {
    return lName;
  }

  public void setlName(String lName) {
    this.lName = lName;
  }

  @Override
  public String toString() {
    return patientID + "," + location + ',' + fName + ',' + lName;
  }

  public boolean equals(Object obj) {
    Patient o = (Patient) obj;
    return this.patientID == o.patientID
        && this.location.equals(o.location)
        && this.fName.equals(o.fName)
        && this.lName.equals(o.lName);
  }
}
