package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Region;
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
  public void openForm(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) formButton.getScene().getWindow();

    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/employeeForm.fxml"))));
  }

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
    fileChooser.setTitle("Select Back Up File");
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

  @FXML
  public void hamburgerToggle(MouseEvent actionEvent) throws IOException {
    // attach the menu to the drawer
    drawer.setSidePane(sideMenuMax);
    // set the animation of the button
    HamburgerSlideCloseTransition burgerTask = new HamburgerSlideCloseTransition(hamburger);
    burgerTask.setRate(burgerTask.getRate() * -1);
    burgerTask.play();

    // if the drawer is open, close it
    if (drawer.isOpened()) {
      drawer.close();
      // sets the width of the left margin to the size of the icon
      sideMenuCont.setMaxWidth(homeIconBtn.getWidth());

      // hide the larger menu
      sideMenuMax.setVisible(false);

      // set the icon menu to visible
      sideMenuMin.setVisible(true);
    } else {
      // if the drawer was closed when the button was pressed
      // open the drawer menu
      drawer.open();
      // reset the width of the side menu
      sideMenuCont.setMaxWidth(Region.USE_COMPUTED_SIZE);
      // hide the icon menu from sight
      sideMenuMin.setVisible(false);
      // show the larger menu
      sideMenuMax.setVisible(true);
    }
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

    refreshTable();
  }

  private void refreshTable() {
    tbCont = null;
    tbCont = EmployeeTbl.getInstance();
    empTable.getItems().setAll(tbCont.readTable());
  }
}
