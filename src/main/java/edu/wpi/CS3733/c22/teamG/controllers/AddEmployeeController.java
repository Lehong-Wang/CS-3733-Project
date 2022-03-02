package edu.wpi.CS3733.c22.teamG.controllers;

import edu.wpi.CS3733.c22.teamG.App;
import edu.wpi.CS3733.c22.teamG.TableController;
import edu.wpi.CS3733.c22.teamG.controllers.validators.AddEmployeeValidator;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.Employee;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.EmployeeTbl;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddEmployeeController implements Initializable {

  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private TextField idField;
  @FXML private TextField permField;
  @FXML private TextField roleField;
  @FXML private TextField emailField;
  @FXML private TextField phoneField;
  @FXML private TextField adrField;
  @FXML private Label testLabel;
  @FXML private Button submitBtn;
  @FXML private Button clearBtn;
  @FXML private Button backButton;
  ArrayList<TextField> textFields = new ArrayList<TextField>();
  //  BooleanBinding booleanBinding =
  //      new BooleanBinding() {
  //        @Override
  //        protected boolean computeValue() {
  //          boolean valid = true;
  //          for (TextField text : textFields) {
  //            if (text.getText().trim().isEmpty()) {
  //              valid = false;
  //            }
  //          }
  //          return !valid;
  //        }
  //      };

  // Method ran when scene is loaded.

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    textFields.add(firstName);
    textFields.add(lastName);
    textFields.add(idField);
    textFields.add(permField);
    textFields.add(roleField);
    textFields.add(emailField);
    textFields.add(phoneField);
    textFields.add(adrField);
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
  }

  @FXML
  public void goBack(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) backButton.getScene().getWindow();
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/employeeDB.fxml"))));
  }

  // When submit is pressed, strings from textFields are gathered
  @FXML
  public void submit() throws SQLException {
    String id_str = idField.getText();
    String fName = firstName.getText();
    String lName = lastName.getText();
    String perms = permField.getText().toLowerCase(Locale.ROOT);
    String role = roleField.getText();
    String email = emailField.getText();
    String phoneNumber = phoneField.getText();
    String address = adrField.getText();

    // Create a new AddEmployee using Strings from TextFields
    AddEmployeeValidator newEmp =
        new AddEmployeeValidator(id_str, fName, lName, perms, role, email, phoneNumber, address);

    // If each field passes validation, perform submit function
    if (newEmp.validateTextFields()) {
      Integer id = Integer.parseInt(idField.getText());
      testLabel.setText(fName + " " + lName + " " + perms + " " + id);
      TableController tbControl = EmployeeTbl.getInstance();
      Employee emp = new Employee(id, fName, lName, role, email, phoneNumber, address);
      tbControl.addEntry(emp);
    }
    // If a field fails validation, run the traceError method and set the label to the returned
    // error msg
    else {
      testLabel.setText(newEmp.traceValidationError());
    }
  }

  // When clear is pressed all textFields are set to empty
  @FXML
  public void clearAll() {
    firstName.setText("");
    lastName.setText("");
    idField.setText("");
    permField.setText("");
    roleField.setText("");
    emailField.setText("");
    phoneField.setText("");
    adrField.setText("");
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
  }

  // Method for ensuring all fields are filled out before Submit can be pressed
  @FXML
  private void validateButton() {
    boolean anyFieldFilled = false, allFilled = true;
    for (TextField text : textFields) {
      if (!text.getText().trim().isEmpty()) {
        anyFieldFilled = true;
        break;
      }
    }

    for (TextField text : textFields) {
      if (text.getText().trim().isEmpty()) {
        allFilled = false;
      }
    }
    clearBtn.setDisable(!anyFieldFilled);
    submitBtn.setDisable(!allFilled);
  }

  @FXML
  private void toMap() throws IOException {
    // Stage stage = (Stage) toMapBtn.getScene().getWindow();

    //  stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/mapIcon.fxml"))));
  }
}
