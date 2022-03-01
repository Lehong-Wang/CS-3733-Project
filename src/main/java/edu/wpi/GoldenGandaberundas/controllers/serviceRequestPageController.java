package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;

public class serviceRequestPageController {
  @FXML JFXButton ComputerServiceButton;
  @FXML JFXButton AudioVisualButton;
  @FXML JFXButton FoodButton;
  @FXML JFXButton GiftFloralButton;
  @FXML JFXButton LanguageButton;
  @FXML JFXButton LaundryButton;
  @FXML JFXButton MedicineDeliveryButton;
  @FXML JFXButton MedicalEquipmentButton;
  @FXML JFXButton PatientTransportButton;
  @FXML JFXButton ReligiousButton;
  @FXML JFXButton allRequestsButton;
  @FXML JFXButton EmployeeDBButton;

  mainController main = null;

  /**
   * The "constructor" for the serviceRequestPageController page, required to be run upon
   * initialization so that the page can switch to other pages
   *
   * @param realMain - The main controller for the main page
   */
  @FXML
  public void setMainControler(mainController realMain) {
    this.main = realMain;
    int currID = CurrentUser.getUser().getEmpID();
    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
    boolean hideShit = true;
    for (int i = 0; i < perms.size(); i++) {
      if (perms.get(i) == 111) {
        hideShit = false;
        break;
      }
    }
    if (hideShit == true) {
      EmployeeDBButton.setManaged(false);
      EmployeeDBButton.setVisible(false);
    }
  }

  @FXML
  public void switchAllRequests() {
    main.switchAllRequests();
  }

  @FXML
  public void switchMedicalEquipment() throws IOException {
    main.switchMedicalEquipment(null);
  }

  @FXML
  public void switchMedicineDelivery() throws IOException {
    main.switchMedicineDelivery(null);
  }

  @FXML
  public void switchGiftFloral() throws IOException {
    main.switchGiftFloral(null);
  }

  @FXML
  public void switchLanguage() throws IOException {
    main.switchLanguage(null);
  }

  @FXML
  public void switchLaundry() throws IOException {
    main.switchLaundry(null);
  }

  @FXML
  public void switchCompService() throws IOException {
    main.switchCompService(null);
  }

  @FXML
  public void switchFood() throws IOException {
    main.switchFood(null);
  }

  @FXML
  public void switchPatientTransport() throws IOException {
    main.switchPatientTransport(null);
  }

  @FXML
  public void switchReligious() throws IOException {
    main.switchReligious(null);
  }

  @FXML
  public void switchAudioVisual() throws IOException {
    main.switchAudioVisual(null);
  }

  @FXML
  public void switchEmployeeDB() throws IOException {
    main.switchEmployeeDB(null);
  }
}
