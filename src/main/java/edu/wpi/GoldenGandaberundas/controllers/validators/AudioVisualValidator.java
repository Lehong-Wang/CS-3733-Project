package edu.wpi.GoldenGandaberundas.controllers.validators;
import java.util.regex.Matcher;
import java.util.Locale;

public class AudioVisualValidator {
  private String avID;
  private String deviceType;
  //location foreign key doesnt need validation
  private String description;
  //no priority validator.  make it a dropdown

  public AudioVisualValidator(
      String avID,
      String deviceType,
      String description
      ) {
    this.avID = avID;
    this.deviceType = deviceType;
    this.description = description;
  }

  /**
   * Perform regex validation on avID
   *
   * @return bool validation status
   */
  private boolean validateAVID() {
    return this.avID.trim().matches("[0-9]+");
  }

  /**
   * Perform regex validation on deviceType
   *
   * @return bool validation status
   */
  private boolean validateDeviceType() {//set to contains() until we find something better or figure out how to use lookingAt()
    return this.deviceType.trim().toLowerCase(Locale.ROOT).matches("[a-zA-Z\\w]+") &&
    !this.deviceType.trim().toLowerCase(Locale.ROOT).contains("\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b");
  }

  /**
   * Perform regex validation on description
   *
   * @return bool validation status
   */
  private boolean validateDescription() {//set to contains() until we find something better or figure out how to use lookingAt()
    return !this.description.trim().toLowerCase(Locale.ROOT).contains("\\b(ALTER|CREATE|DELETE|DROP|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b");
  }

  /**
   * Run all of the individual validation functions
   *
   * @return
   */
  public boolean validateTextFields() {
    return validateAVID()
        && validateDeviceType()
        && validateDescription();
  }

  public String traceValidationError() {
    String errorMsg;
    if (!validateDescription()) {
      errorMsg = "Field 'First Name' is Invalid";
    } else if (!validateDeviceType()) {
      errorMsg = "Field 'Last Name' is Invalid";
    } else if (!validateAVID()) {
      errorMsg = "Field 'Employee ID' is Invalid";
    } else {
      errorMsg = "You real life broke this shit";
    }
    return errorMsg;
  }
}

//
