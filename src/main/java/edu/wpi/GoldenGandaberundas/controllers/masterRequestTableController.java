package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.Request;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.IOException;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class masterRequestTableController {
  @FXML TableView masterRequestsTable;
  @FXML TableColumn<Request, Integer> requestID;
  @FXML TableColumn<Request, String> locationID;
  @FXML TableColumn<Request, Integer> timeStart;
  @FXML TableColumn<Request, Integer> timeEnd;
  @FXML TableColumn<Request, Integer> patientID;
  @FXML TableColumn<Request, Integer> empInitiated;
  @FXML TableColumn<Request, Integer> empCompleter;
  @FXML TableColumn<Request, String> requestStatus;
  @FXML TableColumn<Request, String> notes;
  @FXML TableColumn<Request, String> requestType;

  mainController main = null;

  @FXML
  public void setMainControler(mainController realMain) {
    this.main = realMain;
  }

  @FXML
  public void initialize() {
    requestID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("requestID"));
    locationID.setCellValueFactory(new PropertyValueFactory<Request, String>("locationID"));
    timeStart.setCellValueFactory(new PropertyValueFactory<Request, Integer>("timeStart"));
    timeEnd.setCellValueFactory(new PropertyValueFactory<Request, Integer>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<Request, Integer>("patientID"));
    empInitiated.setCellValueFactory(new PropertyValueFactory<Request, Integer>("empInitiated"));
    empCompleter.setCellValueFactory(new PropertyValueFactory<Request, Integer>("empCompleter"));
    requestStatus.setCellValueFactory(new PropertyValueFactory<Request, String>("requestStatus"));
    notes.setCellValueFactory(new PropertyValueFactory<Request, String>("notes"));
    requestType.setCellValueFactory(new PropertyValueFactory<Request, String>("requestType"));

    masterRequestsTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            System.out.println("Here!");
            goToOtherPage();
          }
        });

    refresh();
  }

  @FXML
  public void refresh() {
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<Request> filteredRequests = new ArrayList<>();
      for (Request r : RequestTable.getInstance().readTable()) {
        if (r.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          filteredRequests.add(r);
        }
      }
      masterRequestsTable.getItems().setAll(filteredRequests);
    } else {
      masterRequestsTable.getItems().setAll(RequestTable.getInstance().readTable());
    }
  }

  private void goToOtherPage() {
    if (masterRequestsTable.getSelectionModel().getSelectedItem() != null) {
      Request currRequest = (Request) masterRequestsTable.getSelectionModel().getSelectedItem();
      String service = currRequest.getRequestType();
      System.out.println(service);
      try {
        switch (service) {
          case "MedEquipDelivery":
            main.switchMedicalEquipment(null);
            break;
          case "MedicineDelivery":
            main.switchMedicineDelivery(null);
            break;
          case "GiftFloral":
            main.switchGiftFloral(null);
            break;
          case "LaundryService":
            main.switchLaundry(null);
            break;
          case "Food":
            main.switchFood(null);
            break;
          case "Computer":
            main.switchCompService(null);
            break;
          case "AudioVisual":
            main.switchAudioVisual(null);
            break;
          default:
            main.goHome(null);
            break;
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
