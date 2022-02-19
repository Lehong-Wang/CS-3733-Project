package edu.wpi.GoldenGandaberundas;

import static edu.wpi.GoldenGandaberundas.tableControllers.AStar.Node.aStar;

import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Node;
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

    Node head = new Node(0, 0);
    head.g = 0;

    Node n1 = new Node(3, 4);
    Node n2 = new Node(6, 9);
    Node n3 = new Node(10, 15);

    head.addBranch(n1);
    head.addBranch(n2);
    head.addBranch(n3);
    n3.addBranch(n2);

    Node n4 = new Node(15, 20);
    Node n5 = new Node(23, 24);
    Node target = new Node(17, 20);

    n1.addBranch(n4);
    n2.addBranch(n5);
    n3.addBranch(n4);

    n4.addBranch(target);
    n5.addBranch(n4);
    n5.addBranch(target);

    ArrayList<Node> res = aStar(head, target);
    System.out.println(res);
    //    floorMaps.load();
    //    App.launch(App.class, args);
  }
}
