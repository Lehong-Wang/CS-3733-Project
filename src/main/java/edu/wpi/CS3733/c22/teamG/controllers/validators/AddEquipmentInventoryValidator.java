package edu.wpi.CS3733.c22.teamG.controllers.validators;

import java.util.Locale;

public class AddEquipmentInventoryValidator {
  private String medID;
  private String type;
  private String status;
  private String currLoc;

  public AddEquipmentInventoryValidator(String medID, String type, String status, String currLoc) {
    this.medID = medID;
    this.type = type;
    this.status = status;
    this.currLoc = currLoc;
  }

  /**
   * Perform regex validation on medID
   *
   * @return bool validation status
   */
  private boolean validateMedID() {
    return this.medID.trim().toUpperCase(Locale.ROOT).matches("[0-9]+");
  }

  /**
   * Perform regex validation on type
   *
   * @return bool validation status
   */
  private boolean validateType() {
    String str = this.type.trim().toUpperCase(Locale.ROOT);
    boolean valid = false;
    if (str.equals("BED")
        || str.equals("X-RAY")
        || str.equals("INFUSION PUMP")
        || str.equals("RECLINER")) {
      valid = true;
    } else {
      valid = false;
    }
    return valid;
  }

  /**
   * Perform regex validation on status
   *
   * @return bool validation status
   */
  private boolean validateStatus() {
    String str = this.status.trim().toUpperCase(Locale.ROOT);
    boolean valid = false;
    if (str.equals("READY") || str.equals("DIRTY") || str.equals("IN USE")) {
      valid = true;
    } else {
      valid = false;
    }
    return valid;
  }

  /**
   * Perform regex validation on currLoc
   *
   * @return bool validation status
   */
  private boolean validateCurrLoc() {
    return this.currLoc.trim().toUpperCase(Locale.ROOT).matches("[A-Z]{5}[0-9]{3}[L0123]{2}");
  }

  /**
   * Run all the individual validation functions
   *
   * @return bool validation status
   */
  public boolean validateTextFields() {
    return validateMedID() && validateType() && validateStatus() && validateCurrLoc();
  }

  /**
   * Trace Validation Error
   *
   * @return String Error Message to be Posted to Error Label
   */
  public String traceValidationError() {
    String errorMsg;
    if (!validateMedID()) {
      errorMsg = "Field 'Equipment ID' is Invalid";
    } else if (!validateType()) {
      errorMsg = "Field 'Type of Equipment' is Invalid";
    } else if (!validateCurrLoc()) {
      errorMsg = "Field 'Location ID' is Invalid";
    } else if (!validateStatus()) {
      errorMsg = "Field 'Equipment Status' is Invalid";
    } else {
      errorMsg = "You real life broke this";
    }
    return errorMsg;
  }
}
