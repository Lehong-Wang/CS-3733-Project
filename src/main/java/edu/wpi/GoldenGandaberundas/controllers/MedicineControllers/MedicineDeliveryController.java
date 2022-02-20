package edu.wpi.GoldenGandaberundas.controllers.MedicineControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class MedicineDeliveryController {

  @FXML Button submitBtn;
  @FXML TextField locInput;
  @FXML TextField patientInput;
  @FXML TextField requestInput;
  @FXML TextField medicineInput;

  @FXML TableView medicineTbl; // Service Table

  @FXML TableColumn<MedicineRequest, String> reqID;
  @FXML TableColumn<MedicineRequest, String> nodeID;
  @FXML TableColumn<MedicineRequest, Integer> submitTime;
  @FXML TableColumn<MedicineRequest, Long> completeTime;
  @FXML TableColumn<MedicineRequest, Integer> patientID;
  @FXML TableColumn<MedicineRequest, Integer> requesterID;
  @FXML TableColumn<MedicineRequest, Integer> completerID;
  @FXML TableColumn<MedicineRequest, String> status;

  @FXML TableColumn<MedicineRequest, Integer> medID;
  @FXML TableColumn<MedicineRequest, Integer> quantity;
  @FXML TableColumn<MedicineRequest, Integer> dosage;
  @FXML TableColumn<MedicineRequest, String> priority;

  @FXML TableView medicineTable;
  @FXML TableColumn<Medicine, String> medName;
  @FXML TableColumn<Medicine, String> medicineID;
  @FXML TableColumn<Medicine, String> description;
  @FXML TableColumn<Medicine, Double> price;
  @FXML TableColumn<Medicine, Boolean> inStock;

  private PatientTbl patients;
  private MedicineTbl medicine;
  private MedicineRequestTbl requestTable;
  private RequestTable reqTable = RequestTable.getInstance();

  // CSS styling strings used to style side panel buttons
  private static final String IDLE_BUTTON_STYLE =
      "-fx-background-color: #002D59; -fx-alignment: center-left";
  private static final String HOVERED_BUTTON_STYLE =
      "-fx-background-color: -fx-shadow-highlight-color, -fx-outer-border, -fx-inner-border, -fx-body-color; -fx-text-fill: #002D59; -fx-alignment: center-left";

  // Runs when scene loads
  public void initialize() {

    reqID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("requestID"));
    nodeID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("locationID"));
    submitTime.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("timeStart"));
    completeTime.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Long>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("patientID"));
    requesterID.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, Integer>("empInitiated"));
    completerID.setCellValueFactory(
        new PropertyValueFactory<MedicineRequest, Integer>("empCompleter"));
    status.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("requestStatus"));
    medID.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("medicineID"));
    quantity.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("quantity"));
    dosage.setCellValueFactory(new PropertyValueFactory<MedicineRequest, Integer>("dosage"));
    // priority.setCellValueFactory(new PropertyValueFactory<MedicineRequest, String>("priority"));

    medName.setCellValueFactory(new PropertyValueFactory<Medicine, String>("medName"));
    medicineID.setCellValueFactory(new PropertyValueFactory<Medicine, String>("medicineID"));
    description.setCellValueFactory(new PropertyValueFactory<Medicine, String>("description"));
    price.setCellValueFactory(new PropertyValueFactory<Medicine, Double>("price"));
    inStock.setCellValueFactory(new PropertyValueFactory<Medicine, Boolean>("inStock"));

    Patient patient = new Patient(123, "FDEPT00201", "bruh", "moment");

    // MedicineRequest testReq = new MedicineRequest("bruh", "FDEPT00101", 0, 0, 123, 123, 123,
    // "ok");

    // patients = PatientTbl.getInstance();
    // patients.addEntry(patient);
    medicine = MedicineTbl.getInstance();

    requestTable = MedicineRequestTbl.getInstance();
    // orderedMedicine = OrderedMedicineTbl.getInstance();
    // requestTable.addEntry(testReq);
    refresh();

    medicineTbl.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });
  }

  void onEdit() {
    if (medicineTbl.getSelectionModel().getSelectedItem() != null) {
      MedicineRequest selectedItem =
          (MedicineRequest) medicineTbl.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editMedReqForm.fxml"));
        AnchorPane editForm = load.load();
        editMedReqFormController edit = load.getController();
        edit.editForm(reqTable.getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
      System.out.println(selectedItem.getPK());
    }
  }

  @FXML
  public void submit() {
    requestTable = MedicineRequestTbl.getInstance();
    int requestID = 0;
    if (reqTable.readTable().size() == 0) {
      requestID = 0;
    } else {
      requestID = reqTable.readTable().get(reqTable.readTable().size() - 1).getRequestID();
    }
    String nodeID = locInput.getText();
    int patientID = Integer.parseInt(patientInput.getText());
    int requesterID = Integer.parseInt(requestInput.getText());
    int medicineID = Integer.parseInt(medicineInput.getText());

    MedicineRequest medicineRequest2 =
        new MedicineRequest(
            requestID,
            nodeID,
            requesterID,
            123,
            000,
            000,
            patientID,
            "Submitted",
            "",
            medicineID,
            000,
            000);
    RequestTable.getInstance().addEntry(medicineRequest2);
    refresh();
  }

  @FXML
  public void loadRequests() {
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
      requestTable.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void backupMedicine() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Medicine File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      medicine.createBackup(new File(selectedFile.toString() + "\\medicineBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void backupRequests() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Requests File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      MedicineRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\medMedicineRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  @FXML
  public void loadDBMedicine() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Medicine File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      medicine.loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Medicine Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      MedicineRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  @FXML
  public void refresh() {
    requestTable = MedicineRequestTbl.getInstance();

    RequestTable test = RequestTable.getInstance();
    medicine = MedicineTbl.getInstance();
    medicineTbl.getItems().setAll(requestTable.readTable());

    medicineTable.getItems().setAll(medicine.readTable());
  }

  // Method to set buttons style, used in initialize method with slide panel buttons as params
  public void buttonStyle(JFXButton buttonO) {
    buttonO.setStyle(IDLE_BUTTON_STYLE);
    buttonO.setOnMouseEntered(
        e -> {
          buttonO.setStyle(HOVERED_BUTTON_STYLE);
        });
    buttonO.setOnMouseExited(
        e -> {
          buttonO.setStyle(IDLE_BUTTON_STYLE);
        });
  }

  public void editEntry() throws IOException {
    //    Stage stage = new Stage();
    //    stage.setScene(new
    // Scene(FXMLLoader.load(App.class.getResource("views/editMedReqForm.fxml"))));
    //    stage.show();

    Stage stage = new Stage();
    Scene scene = new Scene(FXMLLoader.load(App.class.getResource("views/editMedReqForm.fxml")));
    stage.setScene(scene);
    stage.show();
  }
}
