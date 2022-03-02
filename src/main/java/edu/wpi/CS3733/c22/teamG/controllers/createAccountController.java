package edu.wpi.CS3733.c22.teamG.controllers;

import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Credential;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.CredentialsTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Employee;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.EmployeeTbl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class createAccountController {
  @FXML private TextField fNameField;
  @FXML private TextField lNameField;
  @FXML private TextField addressField;
  @FXML private TextField emailField;
  @FXML private TextField phoneNumField;
  @FXML private TextField roleField;
  @FXML private TextField employeeIDField;
  @FXML private TextField permissionField;
  @FXML private TextField passwordField;
  @FXML private TextField password2Field;

  private TableController empTable = EmployeeTbl.getInstance();
  private TableController credTable = CredentialsTbl.getInstance();

  /** submits the entered employee information */
  public void submit() {
    boolean valid = true;
    String firstName = fNameField.getText();
    String lastName = lNameField.getText();
    String address = addressField.getText();
    String email = emailField.getText();
    String phoneNum = phoneNumField.getText();
    String role = roleField.getText();
    int employeeID = 0;
    try {
      employeeID = Integer.parseInt(employeeIDField.getText());
    } catch (NumberFormatException e) {
      employeeIDField.setText("Invalid employee ID");
      valid = false;
    }
    int permission = 0;
    try {
      permission = Integer.parseInt(permissionField.getText());
    } catch (NumberFormatException e) {
      permissionField.setText("Invalid permission");
      valid = false;
    }

    String password = passwordField.getText();
    String password2 = password2Field.getText();

    if (password.equals(password2) && valid) {
      if (validateDataSafe()) {
        Employee emp =
            new Employee(employeeID, firstName, lastName, role, email, phoneNum, address);
        empTable.addEntry(emp);
        Credential cred = new Credential(employeeID, password);
        credTable.addEntry(cred);
        clearText();
        fNameField.setText("Submitted!");
      } else {
        clearText();
        fNameField.setText("Invalid Input");
      }
    }
  }

  /** clears all the text boxes */
  public void clearText() {
    fNameField.setText("");
    lNameField.setText("");
    addressField.setText("");
    emailField.setText("");
    phoneNumField.setText("");
    roleField.setText("");
    employeeIDField.setText("");
    permissionField.setText("");
    passwordField.setText("");
    password2Field.setText("");
  }

  /**
   * Checks if the data all has values and makes sure there are no semicolons or the such that check
   * code
   *
   * @return Boolean (If Data is Safe Returns True, Else false)
   */
  public boolean validateDataSafe() {
    String[] sqlComs = {
      "ALTER",
      "CREATE",
      "DELETE",
      "DROP",
      "DROP TABLE",
      "EXEC",
      "EXECUTE",
      "INSERT",
      "INSERT INTO",
      "INTO",
      "MERGE",
      "SELECT",
      "UPDATE",
      "UNION",
      "UNION ALL",
      "ALL"
    };
    for (String s : sqlComs) {
      if (fNameField.getText().toUpperCase().contains(s)
          || lNameField.getText().toUpperCase().contains(s)
          || addressField.getText().toUpperCase().contains(s)
          || emailField.getText().toUpperCase().contains(s)
          || phoneNumField.getText().toUpperCase().contains(s)
          || roleField.getText().toUpperCase().contains(s)
          || employeeIDField.getText().toUpperCase().contains(s)
          || permissionField.getText().toUpperCase().contains(s)
          || passwordField.getText().toUpperCase().contains(s)
          || password2Field.getText().toUpperCase().contains(s)) {
        return false;
      }
    }

    if (fNameField.getText().isBlank()
        || lNameField.getText().isBlank()
        || addressField.getText().isBlank()
        || emailField.getText().isBlank()
        || phoneNumField.getText().isBlank()
        || roleField.getText().isBlank()
        || employeeIDField.getText().isBlank()
        || permissionField.getText().isBlank()
        || passwordField.getText().isBlank()
        || password2Field.getText().isBlank()) {
      return false;
    }
    boolean same = false;
    try {
      for (int i = 0; i < empTable.readTable().size(); i++) {
        Employee emp = (Employee) empTable.readTable().get(i);
        if (emp.getEmpID() == Integer.parseInt(employeeIDField.getText())) same = true;
      }
    } catch (NumberFormatException e) {
      return false;
    }

    try {
      if (Integer.parseInt(permissionField.getText()) == 111
          || Integer.parseInt(permissionField.getText()) == 222
          || Integer.parseInt(permissionField.getText()) == 333
          || Integer.parseInt(permissionField.getText()) == 444
          || Integer.parseInt(permissionField.getText()) == 555
          || Integer.parseInt(permissionField.getText()) == 666) {
      } else {
        same = true;
      }
    } catch (NumberFormatException e) {
      return false;
    }
    if (same) {
      return false;
    } else {
      return true;
    }
  }
}
