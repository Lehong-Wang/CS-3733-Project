package edu.wpi.CS3733.c22.teamG.tableControllers.MedicineDeliveryService;

import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.Request;
import java.util.ArrayList;
import java.util.Arrays;

public class MedicineRequest extends Request {
  private Integer medicineID = -1;
  private int quantity = -1;
  private int dosage = -1;

  public MedicineRequest(
      Integer requestID,
      String nodeID,
      int prescriberID,
      int pharmacistID,
      long timeStart,
      long timeEnd,
      int patientID,
      String requestStatus,
      String notes,
      Integer medicineID,
      Integer dosage,
      Integer quantity) {
    super(
        requestID,
        nodeID,
        prescriberID,
        pharmacistID,
        timeStart,
        timeEnd,
        patientID,
        "MedicineDelivery",
        requestStatus,
        notes);
    this.dosage = dosage;
    this.medicineID = medicineID;
    this.quantity = quantity;
  }

  public MedicineRequest(Request request, int medicineID, int dosage, int quantity) {
    super(
        request.getRequestID(),
        request.getLocationID(),
        request.getEmpInitiated(),
        request.getEmpCompleter(),
        request.getTimeStart(),
        request.getTimeEnd(),
        request.getPatientID(),
        "MedicineDelivery",
        request.getRequestStatus(),
        request.getNotes());
    this.medicineID = medicineID;
    this.dosage = dosage;
    this.quantity = quantity;
  }

  public MedicineRequest() {}

  public int getMedicineID() {
    return medicineID;
  }

  public void setMedicineID(int medicineID) {
    this.medicineID = medicineID;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getDosage() {
    return dosage;
  }

  public void setDosage(int dosage) {
    this.dosage = dosage;
  }

  public String toString() {
    return super.toString() + "," + medicineID + "," + dosage + "," + quantity;
  }

  public boolean equals(Object obj) {
    MedicineRequest o = (MedicineRequest) obj;
    return this.medicineID.equals(o.medicineID)
        && this.dosage == o.dosage
        && this.quantity == o.quantity
        && this.requestID.equals(o.requestID);
  }

  @Override
  public ArrayList<Integer> getPK() {
    return new ArrayList<Integer>(Arrays.asList(requestID, medicineID));
  }
}
