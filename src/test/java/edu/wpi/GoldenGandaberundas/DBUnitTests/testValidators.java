package edu.wpi.GoldenGandaberundas.DBUnitTests;

import edu.wpi.GoldenGandaberundas.controllers.validators.RequestValidator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class testValidators {
  /**
   * initialize 9 RequestValidators //** setup RequestValidators
   *
   * @return 9 RequestValidators
   */
  RequestValidator emptyRVNote = new RequestValidator("");

  RequestValidator emptyRVGift = new RequestValidator("", "");
  RequestValidator emptyRVMedicine = new RequestValidator("", "", "");

  RequestValidator dropTableRVNote = new RequestValidator("DROP TABLE");
  RequestValidator dropTableRVGift = new RequestValidator("DROP TABLE", "DROP TABLE");
  RequestValidator dropTableRVMedicine =
      new RequestValidator("DROP TABLE", "DROP TABLE", "DROP TABLE");

  RequestValidator rvNoteNormal =
      new RequestValidator("this is a request note!?@#$%^&*()_+=-0987654321`~/?.,<>';:|][}{");
  RequestValidator rvGiftNormal =
      new RequestValidator(
          "this is a Gift request note!?@#$%^&*()_+=-0987654321`~/?.,<>';:|][}{", "6.9");
  RequestValidator rvMedicineNormal =
      new RequestValidator(
          "this is a Medicine request note!?@#$%^&*()_+=-0987654321`~/?.,<>';:|][}{", "6.9", "420");

  /** tests for validateNormalRequestTextFields() */
  @Test // should be true (returns true)
  public void testValidateNormalRequestTextFieldsNull() {
    boolean C = emptyRVNote.validateMedicineRequestTextFields();
    Assertions.assertTrue(C);
  }

  @Test // should be false (returns true)
  public void testValidateNormalRequestTextFieldsDropTable() {
    boolean C = dropTableRVNote.validateMedicineRequestTextFields();
    Assertions.assertFalse(C);
  }

  @Test // should be true (returns true)
  public void testValidateNormalRequestTextFieldsNormal() {
    boolean C = rvNoteNormal.validateMedicineRequestTextFields();
    Assertions.assertTrue(C);
  }

  /** tests for validateQuantityRequestTextFields() */
  @Test // should be false (returns true)
  public void testValidateQuantityRequestTextFieldsNull() {
    boolean C = emptyRVGift.validateMedicineRequestTextFields();
    Assertions.assertFalse(C);
  }

  @Test // should be false (returns true)
  public void testValidateQuantityRequestTextFieldsDropTable() {
    boolean C = dropTableRVGift.validateMedicineRequestTextFields();
    Assertions.assertFalse(C);
  }

  @Test // should be true (returns true)
  public void testValidateQuantityRequestTextFieldsNormal() {
    boolean C = rvGiftNormal.validateMedicineRequestTextFields();
    Assertions.assertTrue(C);
  }

  /** tests for validateMedicineRequestTextFields() */
  @Test // should be false (returns true)
  public void testValidateMedicineRequestTextFieldsNull() {
    boolean C = emptyRVMedicine.validateMedicineRequestTextFields();
    Assertions.assertFalse(C);
  }

  @Test // should be false (returns true)
  public void testValidateMedicineRequestTextFieldsDropTable() {
    boolean C = dropTableRVMedicine.validateMedicineRequestTextFields();
    Assertions.assertFalse(C);
  }

  @Test // should be true (returns true)
  public void testValidateMedicineRequestTextFieldsNormal() {
    boolean C = rvMedicineNormal.validateMedicineRequestTextFields();
    Assertions.assertTrue(C);
  }
}
