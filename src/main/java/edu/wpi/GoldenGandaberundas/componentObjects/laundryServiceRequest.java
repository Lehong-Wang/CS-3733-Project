package edu.wpi.GoldenGandaberundas.componentObjects;

public class laundryServiceRequest {

  private boolean towels;
  private boolean bedding;
  private boolean gowns;

  public laundryServiceRequest(boolean towels, boolean bedding, boolean gowns) {
    this.towels = towels;
    this.bedding = bedding;
    this.gowns = gowns;
  }

  public boolean isTowels() {
    return towels;
  }

  public void setTowels(boolean towels) {
    this.towels = towels;
  }

  public boolean isBedding() {
    return bedding;
  }

  public void setBedding(boolean bedding) {
    this.bedding = bedding;
  }

  public boolean isGowns() {
    return gowns;
  }

  public void setGowns(boolean gowns) {
    this.gowns = gowns;
  }
}
