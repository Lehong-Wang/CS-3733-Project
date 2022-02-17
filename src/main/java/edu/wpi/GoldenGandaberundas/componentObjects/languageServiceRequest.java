package edu.wpi.GoldenGandaberundas.componentObjects;

public class languageServiceRequest {

  private String language;
  private String location;

  public languageServiceRequest(String language, String location) {
    this.language = language;
    this.location = location;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public String getLocation() {
    return location;
  }

  public void setLocation(String location) {
    this.location = location;
  }
}
