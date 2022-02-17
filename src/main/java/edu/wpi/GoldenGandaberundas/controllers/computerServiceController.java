package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import org.controlsfx.control.SearchableComboBox;

public class computerServiceController implements Initializable {

  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML TableView problemsTbl; // Service Table
  @FXML JFXButton currProblemsBtn; // Cool Sliding Button

  @FXML private SearchableComboBox locationSearchBox; // Location drop box

  TableController locationTableController = LocationTbl.getInstance();

  String locations = ""; // String for location of request, value is gotten from location drop down

  @Override
  public void initialize(URL location, ResourceBundle resources) {

    locationSearchBox.setOnAction(
        (event) -> {
          String selectedItem = (String) locationSearchBox.getSelectionModel().getSelectedItem();
          locations = selectedItem;
        });

    ArrayList<String> searchList = locList();
    ObservableList<String> oList = FXCollections.observableArrayList(searchList);
    locationSearchBox.setItems(oList);
  }

  public void backupDB() {}

  public void load() {}

  public void submit() {}

  public void clear() {}

  public ArrayList<String> locList() {
    ArrayList<Location> locArray = new ArrayList<Location>();
    locArray = locationTableController.readTable();
    ArrayList<String> locNodeAr = new ArrayList<String>();

    for (int i = 0; i < locArray.size(); i++) {
      locNodeAr.add(i, locArray.get(i).getNodeID());
      // locationSearchBox.getItems().add(locArray.get(i).getNodeID());
      System.out.println(locNodeAr.get(i));
    }
    return locNodeAr;
  }
}
