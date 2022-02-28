package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Employee;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeCloud;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationCloud;
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

    LocationTbl.getInstance();
    PathTbl.getInstance();

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
    LocationCloud locationCloud = new LocationCloud(LocationTbl.getInstance().readTable());
    // locationCloud.writeTable();
    // System.out.println("LOCATION: " + locationCloud.getLocation("FDEPT00101").toString());
    // System.out.println(locationCloud.editEntry("FDEPT00101", "xCoord", 200));

    EmployeeCloud employeeCloud = new EmployeeCloud(EmployeeTbl.getInstance().readTable());
    // employeeCloud.writeTable();
    employeeCloud.editEntry("123", "LName", "E10");
    employeeCloud.readTable();
    Employee steve =
        new Employee(
            4455, "Steve", "Hang", "Doctor", "shang@bwh.com", "74123695478", "100 Institute Road");
    employeeCloud.addEntry(steve);

    //    AudioVisualTbl.getInstance(); // .loadBackup("BackupsCSVs/BackupAudioVisualTbl.csv");
    //    PathTbl.getInstance(); // .loadBackup("BackupsCSVs/pathTbl.csv");
    //
    //    MedEquipmentTbl.getInstance().loadBackup("TestCSVs/medEquipSimulation.csv");
    //    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    //    points = PathTbl.getInstance().createBranchedLocations(points);
    //
    //    CredentialsTbl.getInstance().addEntry(new Credential(0, "admin"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(123, "password"));
    //    System.out.println(CredentialsTbl.getInstance().getEntry(0).checkPassword("admin"));
    //    CredentialsTbl.getInstance().addEntry(new Credential(1, "staff"));

    //    Simulation.update();
    PathTbl.createStatsMap();

    //    floorMaps.load();
    //    App.launch(App.class, args);

    //    SimulationController sim = new SimulationController();
    //    sim.update();
  }
}
