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
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {

    System.out.println(PathTbl.getInstance().objList);
    // Initializes the database tables in memory

    EmployeeTbl.getInstance();
    LocationTbl.getInstance();
    PatientTbl.getInstance();
    LaundryTbl.getInstance();
    MedEquipmentTbl.getInstance();
    MedicineTbl.getInstance();
    GiftTbl.getInstance();
    CredentialsTbl.getInstance();
    AudioVisualTbl.getInstance();
    ComputerTbl.getInstance();
    FoodTbl.getInstance();
    PathTbl.getInstance();

    LocationTbl.getInstance();
    PathTbl.getInstance();

    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();
    FoodRequestTbl.getInstance();
    ComputerRequestTbl.getInstance();
    AudioVisualRequestTbl.getInstance();

    PathTbl.getInstance().createAStarPath("FDEPT00101", "WHALL00702");
    MedEquipmentTbl.getInstance();

    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    EmployeeTbl.getInstance().loadBackup("BackupsCSVs/employeeTbl.csv");
    ComputerTbl.getInstance().loadBackup("BackupsCSVs/computerTbl.csv");
    FoodTbl.getInstance().loadBackup("BackupsCSVs/foodTbl.csv");
    GiftTbl.getInstance().loadBackup("BackupsCSVs/giftTbl.csv");
    LaundryTbl.getInstance().loadBackup("BackupsCSVs/laundryTbl.csv");
    MedicineTbl.getInstance().loadBackup("BackupsCSVs/medicineTbl.csv");
    PatientTbl.getInstance().loadBackup("BackupsCSVs/patientTbl.csv");
    CredentialsTbl.getInstance().addEntry(new Credential(123, "password"));
    PathTbl.getInstance().loadBackup("BackupsCSVs/pathTbl.csv");
    MedEquipmentTbl.getInstance().loadBackup("BackupsCSVs/medEquipmentTbl.csv");

    floorMaps.load();
    App.launch(App.class, args);
  }
}
