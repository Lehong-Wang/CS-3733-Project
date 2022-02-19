package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.CredentialsTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
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
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Main {

  public static void main(String[] args) throws SQLException {

    // Initializes the database tables in memory
    EmployeeTbl.getInstance();
    LocationTbl.getInstance();
    PatientTbl.getInstance();
    LaundryTbl.getInstance();
    MedEquipmentTbl.getInstance();
    MedicineTbl.getInstance();
    GiftTbl.getInstance();
    CredentialsTbl.getInstance();

    RequestTable.getInstance();

    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();
    FoodTbl.getInstance();

    Point head = new Point("start", 0, 0);
    head.g = 0;

    Point n1 = new Point("node1", 6, 4);
    Point n2 = new Point("node2", 2, 10);
    Point n3 = new Point("node3", 10, 15);

    head.addBranch(n1);
    head.addBranch(n2);
    head.addBranch(n3);
    n3.addBranch(n2);

    Point n4 = new Point("node4", -1, 20);
    Point n5 = new Point("node5", 23, 24);
    Point target = new Point("target", 17, 20);

    n1.addBranch(n4);
    n2.addBranch(n5);
    n3.addBranch(n4);

    n4.addBranch(target);
    n5.addBranch(n4);
    n5.addBranch(target);

    Point res = head.aStar(target);
    List<String> loc = n1.locationsPath(res);

    System.out.println(LocationTbl.getInstance().getNodes());

    PathTbl.getInstance().loadBackup("backups/AllLocationEdges.csv");
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    points = PathTbl.getInstance().createBranchedLocations(points);
    System.out.println(points);

    //    floorMaps.load();
    //    App.launch(App.class, args);
  }
}
