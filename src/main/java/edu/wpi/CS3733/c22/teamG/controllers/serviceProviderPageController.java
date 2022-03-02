package edu.wpi.CS3733.c22.teamG.controllers;

import com.jfoenix.controls.JFXButton;
import java.io.IOException;
import javafx.fxml.FXML;

public class serviceProviderPageController {
  @FXML JFXButton ComputerServiceButton;
  @FXML JFXButton AudioVisualButton;
  @FXML JFXButton FoodButton;
  @FXML JFXButton GiftFloralButton;
  @FXML JFXButton LaundryButton;
  @FXML JFXButton MedicineDeliveryButton;
  @FXML JFXButton MedicalEquipmentButton;

  mainController main = null;

  /**
   * The "constructor" for the serviceRequestPageController page, required to be run upon
   * initialization so that the page can switch to other pages
   *
   * @param realMain - The main controller for the main page
   */
  @FXML
  public void setMainController(mainController realMain) {
    this.main = realMain;
    //    int currID = CurrentUser.getUser().getEmpID();
    //    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
  }

  @FXML
  public void switchProviderEquipment() throws IOException {
    main.switchProviderEquipment();
  }

  @FXML
  public void switchProviderMedicine() throws IOException {
    main.switchProviderMedicine();
  }

  @FXML
  public void switchProviderGift() throws IOException {
    main.switchProviderGift();
  }

  @FXML
  public void switchProviderLaundry() throws IOException {
    main.switchProviderLaundry();
  }

  @FXML
  public void switchProviderComputer() throws IOException {
    main.switchProviderComputer();
  }

  @FXML
  public void switchProviderFood() throws IOException {
    main.switchProviderFood();
  }

  @FXML
  public void switchProviderAV() throws IOException {
    main.switchProviderAudioVisual();
  }
}
