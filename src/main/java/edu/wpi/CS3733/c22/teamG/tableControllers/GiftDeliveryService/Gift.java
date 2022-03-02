package edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService;

public class Gift {

  private int giftID;
  private String giftType;
  private String description;
  private double price;
  private boolean inStock;

  public Gift(int giftID, String giftType, String description, double price, boolean inStock) {
    this.giftID = giftID;
    this.giftType = giftType;
    this.description = description;
    this.price = price;
    this.inStock = inStock;
  }

  public Gift() {}

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(giftID)
        .append(",")
        .append(giftType)
        .append(",")
        .append(description)
        .append(",")
        .append(price)
        .append(",")
        .append(inStock);
    return sb.toString();
  }

  public boolean equals(Object obj) {
    Gift o = (Gift) obj;
    return (this.giftID == o.giftID)
        && this.giftType.equals(o.giftType)
        && this.description.equals(o.description)
        && (this.price == o.price)
        && (this.inStock == o.inStock);
  }

  public int getGiftID() {
    return giftID;
  }

  public void setGiftID(int giftID) {
    this.giftID = giftID;
  }

  public String getGiftType() {
    return giftType;
  }

  public void setGiftType(String giftType) {
    this.giftType = giftType;
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
}
