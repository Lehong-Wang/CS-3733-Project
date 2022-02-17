package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeeObjects.EmployeeTbl;
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

public class Main {

  public static void main(String[] args) throws SQLException {

    // TableController.setConnection(true);
    EmployeeTbl.getInstance();
    LocationTbl.getInstance();
    LaundryTbl.getInstance();
    MedEquipmentTbl.getInstance().loadBackup("Backups/MedEquipment.csv");
    MedicineTbl.getInstance();
    PatientTbl.getInstance();
    GiftTbl.getInstance();

    RequestTable.getInstance();

    GiftRequestTbl.getInstance();
    MedicineRequestTbl.getInstance();
    MedEquipRequestTbl.getInstance();
    LaundryRequestTbl.getInstance();

    floorMaps.load();
    App.launch(App.class, args);
  }
}
