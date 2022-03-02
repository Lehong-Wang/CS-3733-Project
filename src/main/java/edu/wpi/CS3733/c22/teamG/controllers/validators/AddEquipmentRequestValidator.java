package edu.wpi.CS3733.c22.teamG.controllers.validators;

import java.util.Locale;

public class AddEquipmentRequestValidator {

  private String empID;
  private String equipmentID;
  private String nodeID;
  private String notes;
  private String reqStatus;

  public AddEquipmentRequestValidator(
      String empID, String equipmentID, String nodeID, String notes, String reqStatus) {
    this.empID = empID;
    this.equipmentID = equipmentID;
    this.nodeID = nodeID;
    this.notes = notes;
    this.reqStatus = reqStatus;
  }

  /**
   * Perform regex validation on empID
   *
   * @return bool validation status
   */
  private boolean validateEmpID() {
    return this.empID.trim().matches("[0-9]+");
  }

  /**
   * Perform regex validation on equipmentID
   *
   * @return bool validation status
   */
  private boolean validateEquipmentID() {
    return this.equipmentID.trim().matches("[0-9]+");
  }

  /**
   * Perform regex validation on nodeID Node Type ex is FDEPT00101
   *
   * @return bool validation status
   */
  private boolean validateNodeID() {
    return this.nodeID.matches("[A-Z]{5}[0-9]{3}[L0123]{2}");
  }

  /**
   * Perform regex validation on notes
   *
   * @return bool validation status
   */
  private boolean validateNotes() {
    return this.notes.trim().toUpperCase(Locale.ROOT).matches("[\\w\\s]*");
  }

  /**
   * Perform regex validation on reqStatus
   *
   * @return bool validation status
   */
  private boolean validateReqStatus() {
    return this.reqStatus.trim().toUpperCase(Locale.ROOT).matches("[\\w\\s]*");
  }

  /**
   * Run all the individual validation functions
   *
   * @return bool validation status
   */
  public boolean validateTextFields() {
    return validateEmpID()
        && validateEquipmentID()
        && validateNodeID()
        && validateNotes()
        && validateReqStatus();
  }

  /**
   * Trace Validation Error
   *
   * @return String Error Message to be Posted to Error Label
   */
  public String traceValidationError() {
    String errorMsg;
    if (!validateEmpID()) {
      errorMsg = "Field 'Employee ID' is Invalid";
    } else if (!validateEquipmentID()) {
      errorMsg = "Field 'Equipment ID' is Invalid";
    } else if (!validateNodeID()) {
      errorMsg = "Field 'Node ID' is Invalid";
    } else if (!validateNotes()) {
      errorMsg = "Field 'Notes' is Invalid";
    } else if (!validateReqStatus()) {
      errorMsg = "Field 'Request Status' is Invalid";
    } else {
      errorMsg = "You real life broke this";
    }
    return errorMsg;
  }
}
