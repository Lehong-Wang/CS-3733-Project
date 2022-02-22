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

    //normal
    public RequestValidator(
            String notes) {
        this.notes = notes;
        this.amount = "0";
        this.quantity = "0";
    }

    //medicine
    public RequestValidator(
            String notes, String amount, String quantity) {
        this.notes = notes;
        this.amount = amount;
        this.quantity = quantity;
    }

    //gift
    public RequestValidator(
            String notes, String amount) {
        this.notes = notes;
        this.amount = amount;
        this.quantity = "0";
    }


    /**
     * Perform regex validation on notes
     *
     * @return bool validation status
     */
    private boolean validateNotes() {

        return !this.notes.trim().toUpperCase(Locale.ROOT).matches("\\b(ALTER|CREATE|DELETE|DROP|DROP TABLE|EXEC(UTE){0,1}|INSERT( +INTO){0,1}|MERGE|SELECT|UPDATE|UNION( +ALL){0,1})\\b");
    }


    /**
     * Perform regex validation on dosage
     *
     * @return bool validation status
     */
    private boolean validateDosage() {
        //return this.dosage.trim().matches("[0-9]+|[0-9]+.[0-9]+");
        try{
             Double.parseDouble(this.dosage.trim());
             return true;
        } catch (NumberFormatException num){
            return false;
        }
    }

    /**
     * Perform regex validation on dosage
     *
     * @return bool validation status
     */
    private boolean validateQuantity() {
        //return this.quantity.trim().matches("[0-9]+");
        try{
            Integer.parseInt(this.quantity.trim());
            return true;
        } catch (NumberFormatException num){
            return false;
        }
    }

    /**
     * Run all the individual validation functions in a normal request
     *
     * @return bool validation status
     */
    public boolean validateNormalRequestTextFields() {
        return validateNotes();
    }

    /**
     * Run all the individual validation functions in a medicine request
     *
     * @return bool validation status
     */
    public boolean validateMedicineRequestTextFields() {
        return validateDosage() && validateQuantity() && validateDosage();
    }

    /**
     * Run all the individual validation functions in a request with a quantity
     *
     * @return bool validation status
     */
    public boolean validateQuantityRequestTextFields() {
        return validateNotes() && validateQuantity();
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
