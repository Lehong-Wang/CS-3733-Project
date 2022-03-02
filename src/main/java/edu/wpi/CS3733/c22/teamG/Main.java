package edu.wpi.CS3733.c22.teamG;

import edu.wpi.CS3733.c22.teamG.componentObjects.floorMaps;
import edu.wpi.CS3733.c22.teamG.tableControllers.AStar.PathTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeeObjects.*;
import edu.wpi.CS3733.c22.teamG.tableControllers.EmployeePermissionTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.FoodService.FoodTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.LaundryService.LaundryTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Locations.LocationTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.MedicineDeliveryService.MedicineTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Patients.PatientTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.PermissionTbl;
import edu.wpi.CS3733.c22.teamG.tableControllers.Requests.RequestTable;
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
