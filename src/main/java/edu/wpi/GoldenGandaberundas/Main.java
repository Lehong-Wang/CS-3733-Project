package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionHandler;
import edu.wpi.GoldenGandaberundas.tableControllers.DBConnection.ConnectionType;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.*;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipmentTbl;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {
    LocationTbl.getInstance(); // .loadBackup("BackupsCSVs/locationTbl.csv");
    //    System.out.println(LocationTbl.getInstance().getObjList());
    //    LocationTbl.getInstance().loadBackup("BackupsCSVs/locationTbl.csv");

    EmployeeTbl.getInstance(); // .loadBackup("BackupsCSVs/employeeTbl.csv");
    PatientTbl.getInstance(); // .loadBackup("BackupsCSVs/patientTbl.csv");
    LaundryTbl.getInstance(); // .loadBackup("BackupsCSVs/laundryTbl.csv");
    MedEquipmentTbl.getInstance(); // .loadBackup("TestCSVs/medEquipSimulation.csv");
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
    //    ConnectionHandler.getInstance().setConnection(ConnectionType.clientServer);
    //    System.out.println();
    //    System.out.println();
    //    System.out.println(LocationTbl.getInstance().editEntry("FDEPT00101", "xcoord", 100));
    //    System.out.println();
    //    ConnectionHandler.getInstance().setConnection(ConnectionType.embedded);

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
    //    PathTbl.getInstance().loadBackup("BackupsCSVs/pathTbl.csv");
    //
    //    MedEquipmentTbl.getInstance().loadBackup("TestCSVs/medEquipSimulation.csv");

    //    EmployeeTbl.getInstance()
    //        .addEntry(
    //            new Employee(0, "admin", "admin", "admin", "admin@bhm.org", "1111111111", "I
    // live"));
    //    EmployeeTbl.getInstance()
    //        .addEntry(
    //            new Employee(1, "staff", "staff", "staff", "staff@bhm.org", "2222222222", "I
    // die"));
    CredentialsTbl.getInstance().addEntry(new Credential(0, "admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(123, "123"));
    CredentialsTbl.getInstance().addEntry(new Credential(69, "69"));
    CredentialsTbl.getInstance().addEntry(new Credential(70, "70"));
    CredentialsTbl.getInstance().addEntry(new Credential(96, "96"));
    CredentialsTbl.getInstance().addEntry(new Credential(420, "420"));
    CredentialsTbl.getInstance().addEntry(new Credential(456, "456"));
    CredentialsTbl.getInstance().addEntry(new Credential(777, "777"));
    CredentialsTbl.getInstance().addEntry(new Credential(888, "888"));
    //    System.out.println(CredentialsTbl.getInstance().getEntry(0).checkPassword("admin"));
    CredentialsTbl.getInstance().addEntry(new Credential(1, "staff"));
    CredentialsTbl.getInstance().addEntry(new Credential(999, "999"));
    CredentialsTbl.getInstance().addEntry(new Credential(666, "666"));
    EmployeePermissionTbl.getInstance().addEntry(new EmployeePermission(0, 111));

    //    Simulation.update();
    PathTbl.createStatsMap();

    //    List<String> points1 = PathTbl.getPathPoints(1, 2);
    //    List<String> points2 = PathTbl.getPathPoints(15, 37);
    //    List<String> points3 = PathTbl.getPathPoints(37, 25);
    //    List<String> points4 = PathTbl.getPathPoints(25, 15);
    //    List<String> points5 = PathTbl.getPathPoints(62, 7);
    //    List<String> points6 = PathTbl.getPathPoints(78, 18);
    //    PathTbl.getInstance().createAStarPathwStats(points1.get(0), points1.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points2.get(0), points2.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points3.get(0), points3.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points4.get(0), points4.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points5.get(0), points5.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points6.get(0), points6.get(1));
    //    System.out.println(PathTbl.getInstance().createAStarPathwStats("GSTOR00103",
    // "GSTOR00103"));

    //    Simulation.update();
    //    PathTbl.createStatsMap();
    //    List<String> points1 = PathTbl.getPathPoints(1, 2);
    //    List<String> points2 = PathTbl.getPathPoints(15, 37);
    //    List<String> points3 = PathTbl.getPathPoints(37, 25);
    //    List<String> points4 = PathTbl.getPathPoints(25, 15);
    //    List<String> points5 = PathTbl.getPathPoints(62, 7);
    //    List<String> points6 = PathTbl.getPathPoints(78, 18);
    //    PathTbl.getInstance().createAStarPathwStats(points1.get(0), points1.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points2.get(0), points2.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points3.get(0), points3.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points4.get(0), points4.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points5.get(0), points5.get(1));
    //    PathTbl.getInstance().createAStarPathwStats(points6.get(0), points6.get(1));
    //    System.out.println(PathTbl.getInstance().createAStarPathwStats("GSTOR00103",
    // "GSTOR00103"));

    //    PathTbl.printStatsMap();

    //    Simulation.update();
    //    System.out.println(PathTbl.getPathPoints(3, 9));

    //    EmployeePermissionTbl.getInstance();

    //    EmployeePermission adminPerm = new EmployeePermission(123, 111);
    //    EmployeePermission staffPerm = new EmployeePermission(456, 222);
    //    EmployeePermissionTbl.getInstance().addEntry(adminPerm);
    //    EmployeePermissionTbl.getInstance().addEntry(staffPerm);
    floorMaps.load();
    App.launch(App.class, args);

    //    SimulationController sim = new SimulationController();
    //    sim.update();
  }
}
