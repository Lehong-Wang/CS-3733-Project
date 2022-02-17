package edu.wpi.GoldenGandaberundas.controllers.validators;

import java.util.Locale;

public class AddEmployeeValidator {
  private String empID;
  private String FName;
  private String LName;
  private String permission;
  private String role;
  private String email;
  private String phoneNum;
  private String address;

  public AddEmployeeValidator(
      String empID,
      String FName,
      String LName,
      String permission,
      String role,
      String email,
      String phoneNum,
      String address) {
    this.empID = empID;
    this.FName = FName;
    this.LName = LName;
    this.permission = permission;
    this.role = role;
    this.email = email;
    this.phoneNum = phoneNum;
    this.address = address;
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
   * Perform regex validation on FName
   *
   * @return bool validation status
   */
  private boolean validateFName() {
    return this.FName.trim().toLowerCase(Locale.ROOT).matches("[a-zA-Z]+");
  }

  /**
   * Perform regex validation on LName
   *
   * @return bool validation status
   */
  private boolean validateLName() {
    return this.LName.trim().toLowerCase(Locale.ROOT).matches("[a-zA-Z]+");
  }

  /**
   * Perform regex validation on permission
   *
   * @return bool validation status
   */
  private boolean validatePermission() {
    String permStr = this.permission.trim().toLowerCase(Locale.ROOT);
    boolean valid = false;
    return (permStr.equals("it") || permStr.equals("admin") || permStr.equals("healthcare"));
  }

  /**
   * Perform regex validation on role
   *
   * @return bool validation status
   */
  private boolean validateRole() {
    return this.role.trim().toLowerCase(Locale.ROOT).matches("[a-zA-Z]+");
  }

  /**
   * Perform regex validation on email
   *
   * @return bool validation status
   */
  private boolean validateEmail() {
    return this.email.trim().toLowerCase(Locale.ROOT).matches("\\w*[@]{1}\\w*[.]{1}[a-zA-Z]{3}");
  }

  /**
   * Perform regex validation on phoneNum
   *
   * @return bool validation status
   */
  private boolean validatePhoneNum() {
    return this.phoneNum.toLowerCase(Locale.ROOT).trim().matches("[0-9]{10}");
  }

  /**
   * Perform regex validation on address
   *
   * @return bool validation status
   */
  private boolean validateAddress() {
    return this.address.trim().toLowerCase(Locale.ROOT).matches("[0-9]+\\s+[a-zA-Z]+\\s+[a-zA-Z]+");
  }

  /**
   * Run all of the individual validation functions
   *
   * @return
   */
  public boolean validateTextFields() {
    return validateEmpID()
        && validateFName()
        && validateLName()
        && validatePermission()
        && validateRole()
        && validateEmail()
        && validatePhoneNum()
        && validateAddress();
  }

  public String traceValidationError() {
    String errorMsg;
    if (!validateFName()) {
      errorMsg = "Field 'First Name' is Invalid";
    } else if (!validateLName()) {
      errorMsg = "Field 'Last Name' is Invalid";
    } else if (!validateEmpID()) {
      errorMsg = "Field 'Employee ID' is Invalid";
    } else if (!validatePermission()) {
      errorMsg = "Field 'Permission' is Invalid";
    } else if (!validateRole()) {
      errorMsg = "Field 'Role' is Invalid";
    } else if (!validateEmail()) {
      errorMsg = "Field 'Email' is Invalid";
    } else if (!validatePhoneNum()) {
      errorMsg = "Field 'Phone Number' is Invalid";
    } else if (!validateAddress()) {
      errorMsg = "Field 'Address' is Invalid";
    } else {
      errorMsg = "You real life broke this shit";
    }
    return errorMsg;
  }
}

//
