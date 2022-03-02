package edu.wpi.CS3733.c22.teamG.controllers;

import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.Location;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipment;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.stage.Popup;
import org.controlsfx.control.SearchableComboBox;

public class EquipLocEditor {
  @FXML private SearchableComboBox locSearch;
  private MedEquipment medEquipment = null;
  private MapController mapController = null;

  public void initialize() {
    ObservableList<String> locList = FXCollections.observableArrayList();
    ArrayList<Location> locs = LocationTbl.getInstance().readTable();
    for (Location l : locs) {
      locList.add(l.getNodeID());
    }
    locSearch.setItems(locList);
  }

  public void setMedEquipment(MedEquipment med, MapController mapController) {
    this.medEquipment = med;
    this.mapController = mapController;
  }

  public void close() {
    if (locSearch.getValue() != null) {
      MedEquipmentTbl.getInstance()
          .editEntry(medEquipment.getMedID(), "currLoc", locSearch.getValue().toString().trim());
      System.out.println(
          "CHANGE LOC: " + MedEquipmentTbl.getInstance().getEntry(medEquipment.getMedID()));
      mapController.refreshMap();
    }
    ((Popup) locSearch.getScene().getWindow()).hide();
  }
}
