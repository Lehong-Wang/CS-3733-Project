package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
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
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    LocationTbl.getInstance(); // .loadBackup("BackupsCSVs/locationTbl.csv");
    System.out.println(LocationTbl.getInstance().getObjList());

    EmployeeTbl.getInstance(); // .loadBackup("BackupsCSVs/employeeTbl.csv");
    PatientTbl.getInstance(); // .loadBackup("BackupsCSVs/patientTbl.csv");
    LaundryTbl.getInstance(); // .loadBackup("BackupsCSVs/laundryTbl.csv");
    MedEquipmentTbl.getInstance(); // .loadBackup("BackupsCSVs/medEquipmentTbl.csv");
    MedicineTbl.getInstance(); // .loadBackup("BackupsCSVs/medicineTbl.csv");
    GiftTbl.getInstance(); // .loadBackup("BackupsCSVs/giftTbl.csv");
    CredentialsTbl.getInstance(); // .loadBackup("BackupsCSVs/credentialsTbl.csv");
    AudioVisualTbl.getInstance(); // .loadBackup("BackupsCSVs/audioVisualTbl.csv");
    ComputerTbl.getInstance(); // .loadBackup("BackupsCSVs/computerTbl.csv");
    FoodTbl.getInstance(); // .loadBackup("BackupsCSVs/foodTbl.csv");
    PathTbl.getInstance(); // .loadBackup("BackupsCSVs/pathTbl.csv");
    RequestTable.getInstance();

    PermissionTbl.getInstance(); // .loadBackup("BackupsCSVs/PermissionsTbl.csv");
    EmployeePermissionTbl.getInstance(); // .loadBackup("BackupsCSVs/EmployeePermissions.csv");

    MedEquipRequestTbl.getInstance(); // .loadBackup("backups/MedEquipRequests.csv");
    LaundryRequestTbl.getInstance(); // .loadBackup("backups/LaundryRequests.csv");
    GiftRequestTbl.getInstance(); // .loadBackup("backups/GiftRequests.csv");
    MedicineRequestTbl.getInstance(); // .loadBackup("backups/MedicineRequests.csv");
    FoodRequestTbl.getInstance(); // .loadBackup("TestCSVs/FoodRequestForTesting.csv");
    ComputerRequestTbl.getInstance(); // .loadBackup("TestCSVs/ComputerRequestForTesting.csv");
    AudioVisualRequestTbl
        .getInstance(); // .loadBackup("TestCSVs/AudioVisualRequestForTesting.csv");

    PathTbl.createStatsMap();

    floorMaps.load();
    App.launch(App.class, args);
  }
}
