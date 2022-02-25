package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeePermission;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Permission;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.PermissionTbl;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class editEmployeePermsController {

  @FXML Label title;
  @FXML SearchableComboBox<Integer> employeeComboBox;
  @FXML SearchableComboBox<Integer> permissionComboBox;

  @FXML
  public void editEmployee(Employee e) {
    ArrayList<Integer> empIDs = new ArrayList<>();
    for (Employee emps : EmployeeTbl.getInstance().readTable()) {
      empIDs.add(emps.getEmpID());
    }
    employeeComboBox.setItems(FXCollections.observableArrayList(empIDs));

    ArrayList<Integer> perms = new ArrayList<>();
    for (Permission p : PermissionTbl.getInstance().readTable()) {
      perms.add(p.getPermID());
    }
    permissionComboBox.setItems(FXCollections.observableArrayList(perms));

    employeeComboBox.setValue(e.getEmpID());
  }

  public boolean permExists() {
    if (permissionComboBox.getValue() != null && employeeComboBox.getValue() != null) {
      ArrayList<Integer> pkIDs = new ArrayList<>();
      pkIDs.add(employeeComboBox.getValue());
      pkIDs.add(permissionComboBox.getValue());
      return EmployeePermissionTbl.getInstance().entryExists(pkIDs);
    } else {
      return false;
    }
  }

  public void deletePermission() {
    if (permExists()) {
      ArrayList<Integer> pkIDs = new ArrayList<Integer>();
      pkIDs.add(employeeComboBox.getValue());
      pkIDs.add(permissionComboBox.getValue());
      EmployeePermissionTbl.getInstance().deleteEntry(pkIDs);
      Stage stage = (Stage) title.getScene().getWindow();
      stage.close();
    } else {
      title.setText("User Doesn't Have That Permission!");
    }
  }

  public void addPermision() {
    if (permExists()) {
      title.setText("Permission Already Exists!");
    } else {
      EmployeePermission bruh =
          new EmployeePermission(employeeComboBox.getValue(), permissionComboBox.getValue());
      EmployeePermissionTbl.getInstance().addEntry(bruh);
      Stage stage = (Stage) title.getScene().getWindow();
      stage.close();
    }
  }
}
