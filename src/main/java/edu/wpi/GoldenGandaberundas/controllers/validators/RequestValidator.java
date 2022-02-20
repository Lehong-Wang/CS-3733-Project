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

    public boolean validateTextFields() {
        return validateNotes();
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
