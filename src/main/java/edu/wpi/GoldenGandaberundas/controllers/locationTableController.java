package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerSlideCloseTransition;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

public class locationTableController implements Initializable {
  @FXML Button homeBtn; // home btn with icon and text
  @FXML Button homeIconBtn; // home btn with just an icon
  @FXML JFXHamburger hamburger; // hamburger menu btn
  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox sideMenuMax; // container for the menu with icon a nd text
  @FXML VBox sideMenuMin; // container for the menu with just icons
  @FXML VBox sideMenuCont; // container for the entire set of side menus
  @FXML TableView locTable; // Table object
  @FXML private JFXButton addLocBtn;
  @FXML TableColumn<Location, String> nodeID;
  @FXML TableColumn<Location, Integer> xcord;
  @FXML TableColumn<Location, Integer> ycord;
  @FXML TableColumn<Location, String> floor;
  @FXML TableColumn<Location, String> building;
  @FXML TableColumn<Location, String> nodeType;
  @FXML TableColumn<Location, String> longName;
  @FXML TableColumn<Location, String> shortName;

  TableController tc = null;

  public void goHome(ActionEvent actionEvent) throws IOException {
    // gets the current stage that the button exists on
    Stage stage = (Stage) homeBtn.getScene().getWindow();

    // sets the current scene back to the home screen
    // !!!!!!!!!! NOTE: you can only access FXML files stored in a directory with the same name as
    // the package!!!!!!!!!
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/main.fxml"))));
  }

  /** clicking on the backup btn opens a file selection dialog to choose the file name used to * */
  @FXML
  public void backup() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up File");

    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {

      System.out.println(selectedFile.toString());
      tc.createBackup(new File(selectedFile.toString() + "\\locationBackup.csv"));

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
      tc.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refreshTable();
  }

  @FXML
  public void toForm(ActionEvent actionEvent) throws IOException {
    Stage stage = (Stage) addLocBtn.getScene().getWindow();
    stage.setScene(new Scene(FXMLLoader.load(App.class.getResource("views/addLocationForm.fxml"))));
  }

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

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    nodeID.setCellValueFactory(new PropertyValueFactory<Location, String>("nodeID"));
    xcord.setCellValueFactory(new PropertyValueFactory<Location, Integer>("xcoord"));
    ycord.setCellValueFactory(new PropertyValueFactory<Location, Integer>("ycoord"));
    floor.setCellValueFactory(new PropertyValueFactory<Location, String>("floor"));
    building.setCellValueFactory(new PropertyValueFactory<Location, String>("building"));
    nodeType.setCellValueFactory(new PropertyValueFactory<Location, String>("nodeType"));
    longName.setCellValueFactory(new PropertyValueFactory<Location, String>("longName"));
    shortName.setCellValueFactory(new PropertyValueFactory<Location, String>("shortName"));
    // Uncomment when DB works
    //    populateTable(empDB.readTable());

    refreshTable();
  }

  private void refreshTable() {
    tc = LocationTbl.getInstance();
    locTable.getItems().setAll(tc.readTable());
  }
}
