package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Credential;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.PermissionTbl;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {

    System.out.println(PathTbl.getInstance().objList);

    LocationTbl.getInstance();
    EmployeeTbl.getInstance(); // .loadBackup("BackupsCSVs/employeeTbl.csv");
    //    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    PatientTbl.getInstance();
    LaundryTbl.getInstance();
    //    MedEquipmentTbl.getInstance().loadBackup("BackupsCSVs/medEquipmentTbl.csv");
    MedEquipmentTbl.getInstance();
    MedicineTbl.getInstance();
    GiftTbl.getInstance();
    PermissionTbl.getInstance();
    //    PermissionTbl.getInstance().loadBackup("BackupsCSVs/PermissionsForTesting.csv");
    CredentialsTbl.getInstance(); // .loadBackup("BackupsCSVs/CredentialsTbl.csv");
    //    AudioVisualTbl.getInstance().loadBackup("BackupsCSVs/audioVisualTbl.csv");
    EmployeePermissionTbl.getInstance();
    ComputerTbl.getInstance();
    AudioVisualTbl.getInstance();
    FoodTbl.getInstance(); // .loadBackup("BackupsCSVs/foodTbl.csv");
    PathTbl.getInstance();
    LocationTbl.getInstance();
    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();
    FoodRequestTbl.getInstance();
    ComputerRequestTbl.getInstance();
    AudioVisualRequestTbl.getInstance();
    //    GiftRequestTbl.getInstance().loadBackup("backups/GiftRequests.csv");
    //    MedicineRequestTbl.getInstance().loadBackup("backups/MedicineRequests.csv");
    //    MedEquipRequestTbl.getInstance().loadBackup("backups/MedEquipRequests.csv");
    //    LaundryRequestTbl.getInstance().loadBackup("backups/LaundryRequests.csv");
    //    FoodRequestTbl.getInstance().loadBackup("TestCSVs/FoodRequestForTesting.csv");
    //    ComputerRequestTbl.getInstance().loadBackup("TestCSVs/ComputerRequestForTesting.csv");
    //
    // AudioVisualRequestTbl.getInstance().loadBackup("TestCSVS/AudioVisualRequestForTesting.csv");

    //    EmployeeTbl.getInstance().loadBackup("BackupsCSVs/employeeTbl.csv");
    //    EmployeePermissionTbl.getInstance().loadBackup("BackupsCSVs/employeePermissionsTbl.csv");
    //    AudioVisualTbl.getInstance(); // .loadBackup("BackupsCSVs/BackupAudioVisualTbl.csv");
    //    PathTbl.getInstance().loadBackup("BackupsCSVs/pathTbl.csv");
    //    MedEquipmentTbl.getInstance().loadBackup("TestCSVs/medEquipSimulation.csv");
    //    LaundryTbl.getInstance().loadBackup("BackupsCSVs/laundryTbl.csv");
    //    GiftTbl.getInstance().loadBackup("BackupsCSVs/giftTbl.csv");
    //    MedicineTbl.getInstance().loadBackup("BackupsCSVs/medicineTbl.csv");
    //        PatientTbl.getInstance().loadBackup("BackupsCSVs/patientTbl.csv");
    //    PermissionTbl.getInstance().loadBackup("BackupsCSVs/PermissionsForTesting.csv");
    //    ComputerTbl.getInstance().loadBackup("BackupsCSVs/computerTbl.csv");

    CredentialsTbl.getInstance().addEntry(new Credential(0, "admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(123, "password"));
    CredentialsTbl.getInstance().addEntry(new Credential(1, "staff"));

    floorMaps.load();
    App.launch(App.class, args);
  }
}
