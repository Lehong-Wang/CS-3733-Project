package edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService;

import java.util.Objects;

public class Computer {
  private int computerID;
  private String computerType;
  private String os;
  private String processor;
  private String hostName;
  private String model;
  private String manufacturer;
  private String serialNumber;

  public Computer() {}

  public Computer(
      int computerID,
      String computerType,
      String os,
      String processor,
      String hostName,
      String model,
      String manufacturer,
      String serialNumber) {
    this.computerID = computerID;
    this.computerType = computerType;
    this.os = os;
    this.processor = processor;
    this.hostName = hostName;
    this.model = model;
    this.manufacturer = manufacturer;
    this.serialNumber = serialNumber;
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(computerID);
    sb.append(",");
    sb.append(computerType);
    sb.append(",");
    sb.append(os);
    sb.append(",");
    sb.append(os);
    sb.append(",");
    sb.append(processor);
    sb.append(",");
    sb.append(hostName);
    sb.append(",");
    sb.append(model);
    sb.append(",");
    sb.append(manufacturer);
    sb.append(",");
    sb.append(serialNumber);
    return sb.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Computer computer = (Computer) o;
    return computerID == computer.computerID
        && Objects.equals(computerType, computer.computerType)
        && Objects.equals(os, computer.os)
        && Objects.equals(processor, computer.processor)
        && Objects.equals(hostName, computer.hostName)
        && Objects.equals(model, computer.model)
        && Objects.equals(manufacturer, computer.manufacturer)
        && Objects.equals(serialNumber, computer.serialNumber);
  }

  public int getComputerID() {
    return computerID;
  }

  public void setComputerID(int computerID) {
    this.computerID = computerID;
  }

  public String getComputerType() {
    return computerType;
  }

  public void setComputerType(String computerType) {
    this.computerType = computerType;
  }

  public String getOs() {
    return os;
  }

  public void setOs(String os) {
    this.os = os;
  }

  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    this.processor = processor;
  }

  public String getHostName() {
    return hostName;
  }

  public void setHostName(String hostName) {
    this.hostName = hostName;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getManufacturer() {
    return manufacturer;
  }

  public void setManufacturer(String manufacturer) {
    this.manufacturer = manufacturer;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }
}
