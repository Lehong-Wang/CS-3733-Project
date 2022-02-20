package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.PathTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.AStar.Point;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.Credential;
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
    CredentialsTbl.getInstance(); // .loadBackup("backups/");

    RequestTable.getInstance();

    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();
    FoodTbl.getInstance();

    LocationTbl.getInstance();
    PathTbl.getInstance();
    ArrayList<Point> points = LocationTbl.getInstance().getNodes();
    points = PathTbl.getInstance().createBranchedLocations(points);

    int start = 0;
    int end = 0;
    for (Point o : points) {
      if (o.loc.equals("FDEPT00101")) {
        start = points.indexOf(o);
      }
      if (o.loc.equals("FRETL00201")) {
        end = points.indexOf(o);
      }
    }
    points.get(start).g = 0;
    Point test = points.get(start).aStar(points.get(end));
    points.get(start).locationsPath(test);

    Credential cred = new Credential(123, "p");
    CredentialsTbl.getInstance().addEntry(cred);

    floorMaps.load();
    App.launch(App.class, args);
  }
}
