package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class EmployeeDBController implements Initializable {
  @FXML Button homeBtn; // home btn with icon and text
  @FXML Button homeIconBtn; // home btn with just an icon
  @FXML Button formButton;
  @FXML Button formIconButton;
  @FXML JFXHamburger hamburger; // hamburger menu btn
  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox sideMenuMax; // container for the menu with icon a nd text
  @FXML VBox sideMenuMin; // container for the menu with just icons
  @FXML VBox sideMenuCont; // container for the entire set of side menus
  @FXML TableView empTable; // Table object
  @FXML TableColumn<Employee, Integer> empID;
  @FXML TableColumn<Employee, String> firstName;
  @FXML TableColumn<Employee, String> lastName;
  @FXML TableColumn<Employee, String> role;
  @FXML TableColumn<Employee, String> email;
  @FXML TableColumn<Employee, String> phoneNumber;
  @FXML TableColumn<Employee, String> address;

  TableController tbCont;

  @FXML
  public void backup() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up File");

    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {

      System.out.println(selectedFile.toString());
      tbCont.createBackup(new File(selectedFile.toString() + "\\employeeBackup.csv"));

    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDB() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Employee  Back Up File");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));

    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      tbCont.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refreshTable();
  }

  public void populateTable(List<Employee> empList) {
    empTable.getItems().setAll();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    empID.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("empID"));
    firstName.setCellValueFactory(new PropertyValueFactory<Employee, String>("FName"));
    lastName.setCellValueFactory(new PropertyValueFactory<Employee, String>("LName"));
    role.setCellValueFactory(new PropertyValueFactory<Employee, String>("role"));
    email.setCellValueFactory(new PropertyValueFactory<Employee, String>("email"));
    phoneNumber.setCellValueFactory(new PropertyValueFactory<Employee, String>("phoneNum"));
    address.setCellValueFactory(new PropertyValueFactory<Employee, String>("address"));

    empTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            int currID = CurrentUser.getUser().getEmpID();
            ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
            for (int i = 0; i < perms.size(); i++) {
              if (perms.get(i) == 111) {
                editEmployeePerm();
              }
            }
          }
        });

    refreshTable();
  }

  private void editEmployeePerm() {
    if (empTable.getSelectionModel().getSelectedItem() != null) {
      try {
        Employee emp = (Employee) empTable.getSelectionModel().getSelectedItem();
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editEmployeePerms.fxml"));
        AnchorPane editForm = load.load();
        editEmployeePermsController edit = load.getController();
        edit.editEmployee(emp);
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private void refreshTable() {
    tbCont = null;
    tbCont = EmployeeTbl.getInstance();
    empTable.getItems().setAll(tbCont.readTable());
  }

  /**
   * adds a new employee
   * @throws IOException oops
   */
  public void addEmployee() throws IOException {
    try {
      FXMLLoader load = new FXMLLoader(App.class.getResource("views/signUpScreen.fxml"));
      Stage stage = new Stage();
      Scene scene = new Scene(load.load());
      stage.setScene(scene);
      stage.show();
      stage.setOnCloseRequest(e -> refreshTable());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
