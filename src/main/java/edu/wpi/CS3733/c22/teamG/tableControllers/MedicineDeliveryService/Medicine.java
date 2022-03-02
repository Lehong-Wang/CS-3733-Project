package edu.wpi.CS3733.c22.teamG.tableControllers.MedicineDeliveryService;

public class Medicine {
  private Integer medicineID;
  private String medName;
  private String description;
  private double price;
  private boolean inStock;

  public Medicine(
      Integer medicineID, String medName, String description, double price, boolean inStock) {
    this.medicineID = medicineID;
    this.medName = medName;
    this.description = description;
    this.price = price;
    this.inStock = inStock;
  }

  public Medicine() {}

  public Integer getMedicineID() {
    return medicineID;
  }

  public void setMedicineID(Integer medicineID) {
    this.medicineID = medicineID;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean getInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }

  public String getMedName() {
    return medName;
  }

  public void setMedName(String medName) {
    this.medName = medName;
  }

  public boolean isInStock() {
    return inStock;
  }

  @Override
  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(medicineID);
    stringBuilder.append(",");
    stringBuilder.append(medName);
    stringBuilder.append(",");
    stringBuilder.append(description);
    stringBuilder.append(",");
    stringBuilder.append(price);
    stringBuilder.append(",");
    stringBuilder.append(inStock);
    return stringBuilder.toString();
  }

  public boolean equals(Object obj) {
    Medicine o = (Medicine) obj;
    return this.medicineID.equals(o.medicineID)
        && this.description.equals(o.description)
        && (this.price == o.price)
        && (this.inStock == o.inStock);
  }
}
