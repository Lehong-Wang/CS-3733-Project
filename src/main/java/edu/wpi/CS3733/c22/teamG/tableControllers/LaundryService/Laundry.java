package edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService;

public class Laundry {
  private Integer laundryID;
  private String laundryType;
  private String description;
  private boolean inStock;

  public Laundry(Integer laundryID, String laundryType, String description, boolean inStock) {
    this.laundryID = laundryID;
    this.laundryType = laundryType;
    this.description = description;
    this.inStock = inStock;
  }

  public Laundry() {}

  public int getLaundryID() {
    return laundryID;
  }

  public void setLaundryID(Integer laundryID) {
    this.laundryID = laundryID;
  }

  public String getLaundryType() {
    return laundryType;
  }

  public void setLaundryType(String laundryType) {
    this.laundryType = laundryType;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public boolean getInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(laundryID);
    stringBuilder.append(",");
    stringBuilder.append(laundryType);
    stringBuilder.append(",");
    stringBuilder.append(description);
    stringBuilder.append(",");
    stringBuilder.append(inStock);
    return stringBuilder.toString();
  }

  public boolean equals(Object obj) {
    Laundry o = (Laundry) obj;
    return (this.laundryID.equals(o.laundryID))
        && this.laundryType.equals(o.laundryType)
        && this.description.equals(o.description)
        && (this.inStock == o.inStock);
  }
}
