package edu.wpi.GoldenGandaberundas.controllers.validators;

import java.util.Locale;

public class AddLocationValidator {
  private String nodeID;
  private String xcoord;
  private String ycoord;
  private String floor;
  private String building;
  private String nodeType;
  private String longName;
  private String shortName;

  public AddLocationValidator(
      String nodeID,
      String xcoord,
      String ycoord,
      String floor,
      String building,
      String nodeType,
      String longName,
      String shortName) {
    this.nodeID = nodeID;
    this.xcoord = xcoord;
    this.ycoord = ycoord;
    this.floor = floor;
    this.building = building;
    this.nodeType = nodeType;
    this.longName = longName;
    this.shortName = shortName;
  }

  /**
   * Perform regex validation on NodeID
   *
   * @return bool validation status
   */
  public boolean validateNodeID() {
    return this.nodeID.trim().toUpperCase().matches("[A-Z]{5}[0-9]{3}[L0123]{2}");
  }

  /**
   * Perform regex validation on Xcoord
   *
   * @return bool validation status
   */
  public boolean validateXcoord() {
    return this.xcoord.trim().matches("[0-9]+");
  }

  /**
   * Perform regex validation on Ycoord
   *
   * @return bool validation status
   */
  public boolean validateYcoord() {
    return this.ycoord.trim().matches("[0-9]+");
  }

  /**
   * Perform regex validation on Floor
   *
   * @return bool validation status
   */
  public boolean validateFloor() {
    return this.floor.trim().toUpperCase(Locale.ROOT).matches("[L,0-4]+");
  }

  /**
   * Perform regex validation on Building
   *
   * @return bool validation status
   */
  public boolean validateBuilding() {
    return this.building.trim().toUpperCase(Locale.ROOT).matches("[A-Z[\\s]]*");
  }

  /**
   * Perform regex validation on NodeType Node Type ex is EXIT
   *
   * @return bool validation status
   */
  public boolean validateNodeType() {
    return this.nodeType.trim().toUpperCase().matches("[A-Z]{4}");
  }

  /**
   * Perform regex validation on LongName
   *
   * @return bool validation status
   */
  public boolean validateLongName() {
    return this.longName.trim().toUpperCase(Locale.ROOT).matches("[\\w\\s]*");
  }

  /**
   * Perform regex validation on ShortName
   *
   * @return bool validation status
   */
  public boolean validateShortName() {
    return this.shortName.trim().toUpperCase(Locale.ROOT).matches("[\\w\\s]*");
  }

  /**
   * Run all the individual validation functions
   *
   * @return bool validation status
   */
  public boolean validateTextFields() {
    return validateNodeID()
        && validateXcoord()
        && validateYcoord()
        && validateFloor()
        && validateBuilding()
        && validateNodeType()
        && validateLongName()
        && validateShortName();
  }

  /**
   * Trace Validation Error
   *
   * @return String Error Message to be Posted to Error Label
   */
  public String traceValidationError() {
    String errorMsg;
    if (!validateNodeID()) {
      errorMsg = "Field 'Node ID' is Invalid";
    } else if (!validateXcoord()) {
      errorMsg = "Field 'XCoord' is Invalid";
    } else if (!validateYcoord()) {
      errorMsg = "Field 'YCoord' is Invalid";
    } else if (!validateFloor()) {
      errorMsg = "Field 'Floor' is Invalid";
    } else if (!validateBuilding()) {
      errorMsg = "Field 'Building' is Invalid";
    } else if (!validateNodeType()) {
      errorMsg = "Field 'NodeType' is Invalid";
    } else if (!validateLongName()) {
      errorMsg = "Field 'LongName' is Invalid";
    } else if (!validateShortName()) {
      errorMsg = "Field 'ShortName' is Invalid";
    } else {
      errorMsg = "You real life broke this";
    }
    return errorMsg;
  }
}
