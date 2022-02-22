package edu.wpi.GoldenGandaberundas.controllers.validators;

import java.util.Locale;

public class RequestValidator {

    //requestID made automatically and unchangable
    //empInitiated is you (taken from account logged in)
    //empCompleter is person who accepted request (taken from account logged in)
    //timeStart auto-generated
    //timeEnd might be auto-generated?
    //patientID might be dropdown
    //requestType is based on what page you're on
    //requestStatus will be specific values (maybe in dropdown, but not user input value)
    //choose objectID from dropdown
    //taken from location dropdown
    private String notes;
    //changed based on other factors
    private String amount;
    private String dosage;
    private String quantity;

    public RequestValidator(
            String notes) {
        this.notes = notes;
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
     * Perform regex validation on dosage
     *
     * @return bool validation status
     */
    private boolean validateDosage() {
        return this.dosage.trim().matches("[0-9]+");
    }

    /**
     * Perform regex validation on dosage
     *
     * @return bool validation status
     */
    private boolean validateQuantity() {
        return this.quantity.trim().matches("[0-9]+");
    }

    /**
     * Run all the individual validation functions
     *
     * @return bool validation status
     */
    public boolean validateTextFields() {
        return validateDosage();
    }

    /**
     * Trace Validation Error
     *
     * @return String Error Message to be Posted to Error Label
     */
    public String traceValidationError() {
        String errorMsg;
        if (!validateNotes()) {
            errorMsg = "Field 'Notes' is Invalid";
        } else {
            errorMsg = "You real life broke this";
        }
        return errorMsg;
    }
}
