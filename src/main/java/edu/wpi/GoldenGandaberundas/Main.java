package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
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
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {

    System.out.println(PathTbl.getInstance().objList);

    floorMaps.load();
    EmployeeTbl.getInstance();
    LocationTbl.getInstance();
    EmployeeTbl.getInstance();
    LocationTbl.getInstance(); // .loadBackup("BackupsCSVs/locationTbl.csv");
    PatientTbl.getInstance();
    LaundryTbl.getInstance();
    MedEquipmentTbl.getInstance(); // .loadBackup("BackupsCSVs/medEquipmentTbl.csv");
    MedicineTbl.getInstance();
    GiftTbl.getInstance();
    PermissionTbl.getInstance(); // .loadBackup("BackupsCSVs/PermissionsForTesting.csv");
    CredentialsTbl.getInstance(); // .loadBackup("BackupsCSVs/CredentialsTbl.csv");
    AudioVisualTbl.getInstance(); // .loadBackup("BackupsCSVs/audioVisualTbl.csv");
    ComputerTbl.getInstance();
    FoodTbl.getInstance(); // .loadBackup("BackupsCSVs/foodTbl.csv");

    PathTbl.getInstance();
    FoodTbl.getInstance();
    LocationTbl.getInstance();
    PathTbl.getInstance();

    //    EmployeeTbl.getInstance().loadBackup("BackupsCSVs/employeeTbl.csv");
    //    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    //    PatientTbl.getInstance().loadBackup("BackupsCSVs/patientTbl.csv");
    //    LaundryTbl.getInstance().loadBackup("BackupsCSVs/laundryTbl.csv");
    //    MedEquipmentTbl.getInstance().loadBackup("BackupsCSVs/medEquipmentTbl.csv");
    //    MedicineTbl.getInstance().loadBackup("BackupsCSVs/medicineTbl.csv");
    //    GiftTbl.getInstance().loadBackup("BackupsCSVs/giftTbl.csv");
    //    PermissionTbl.getInstance().loadBackup("BackupsCSVs/PermissionsForTesting.csv");
    //    CredentialsTbl.getInstance().loadBackup("BackupsCSVs/CredentialsTbl.csv");
    //    AudioVisualTbl.getInstance().loadBackup("BackupsCSVs/audioVisualTbl.csv");
    //    ComputerTbl.getInstance().loadBackup("BackupsCSVs/computerTbl.csv");
    //    PathTbl.getInstance().loadBackup("BackupsCSVs/pathTbl.csv");
    //    FoodTbl.getInstance().loadBackup("BackupsCSVs/foodTbl.csv");
    //    //    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");
    //    //    PathTbl.getInstance().loadBackup("BackupsCSVs/")

    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();
    FoodRequestTbl.getInstance();
    ComputerRequestTbl.getInstance();
    AudioVisualRequestTbl.getInstance();

    //    Food yummyFood = new Food(1, "Chicken", "Chicken and Rice", 300, "Meat", 10.99, true,
    // "Dinner");
    //    FoodTbl.getInstance().addEntry(yummyFood);

    PathTbl.getInstance(); // .loadBackup("backups/AllLocationEdges.csv");
    EmployeeTbl.getInstance(); // .loadBackup("BackupsCSVs/employeeTbl.csv");
    EmployeePermissionTbl.getInstance(); // .loadBackup("BackupsCSVs/employeePermissionsTbl.csv");

    //        CredentialsTbl.getadminInstance().addEntry(new Credential(456, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(666, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(123, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(777, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(888, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(999, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(70, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(420, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(69, "p"));
    //        CredentialsTbl.getInstance().addEntry(new Credential(96, "p"));

    AudioVisualTbl.getInstance(); // .loadBackup("BackupsCSVs/BackupAudioVisualTbl.csv");
    PathTbl.getInstance(); // .loadBackup("BackupsCSVs/pathTbl.csv");

    MedEquipmentTbl.getInstance(); // .loadBackup("TestCSVs/medEquipSimulation.csv");
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    points = PathTbl.getInstance().createBranchedLocations(points);

    EmployeeTbl.getInstance()
        .addEntry(
            new Employee(0, "admin", "admin", "admin", "admin@bhm.org", "1111111111", "I live"));
    EmployeeTbl.getInstance()
        .addEntry(
            new Employee(1, "staff", "staff", "staff", "staff@bhm.org", "2222222222", "I die"));
    CredentialsTbl.getInstance().addEntry(new Credential(0, "admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(123, "password"));
    //    System.out.println(CredentialsTbl.getInstance().getEntry(0).checkPassword("admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(1, "staff"));
    CredentialsTbl.getInstance().addEntry(new Credential(999, "999"));
    CredentialsTbl.getInstance().addEntry(new Credential(666, "666"));
    EmployeePermissionTbl.getInstance().addEntry(new EmployeePermission(0, 111));

    //    Simulation.update();
    PathTbl.createStatsMap();


    //    floorMaps.load();
    //    App.launch(App.class, args);

    //    SimulationController sim = new SimulationController();
    //    sim.update();
  }
}
