package edu.wpi.GoldenGandaberundas.componentObjects;

public class giftFloralRequest {

  private String gift;
  private String location;

  public giftFloralRequest(String gift, String location) {
    this.gift = gift;
    this.location = location;
  }

  public String getGift() {
    return gift;
  }

  public void setGift(String gift) {
    this.gift = gift;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
