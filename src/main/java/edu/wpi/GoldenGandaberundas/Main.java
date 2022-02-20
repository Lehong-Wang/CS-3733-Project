package edu.wpi.GoldenGandaberundas;

import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import java.sql.SQLException;

public class Main {

  public static void main(String[] args) throws SQLException {

    //    System.out.println(PathTbl.getInstance().objList);
    //    // Initializes the database tables in memory
    //
    //    EmployeeTbl.getInstance();
    LocationTbl.getInstance();
    TableController.setConnection(ConnectionType.clientServer);

    //    PatientTbl.getInstance();
    //    LaundryTbl.getInstance();
    //    MedEquipmentTbl.getInstance();
    //    MedicineTbl.getInstance();
    //    GiftTbl.getInstance();
    //    CredentialsTbl.getInstance();
    //    AudioVisualTbl.getInstance();
    //    ComputerTbl.getInstance();
    //    FoodTbl.getInstance();
    //    PathTbl.getInstance();
    //
    //    GiftRequestTbl.getInstance();
    //    MedicineRequestTbl.getInstance();
    //    MedEquipRequestTbl.getInstance();
    //    LaundryRequestTbl.getInstance();
    //    FoodTbl.getInstance();
    //
    //
    //    LocationTbl.getInstance();
    //    PathTbl.getInstance();
    //
    //    GiftRequestTbl.getInstance();
    //    MedicineRequestTbl.getInstance();
    //    MedEquipRequestTbl.getInstance();
    //    LaundryRequestTbl.getInstance();
    //    FoodRequestTbl.getInstance();
    //    ComputerRequestTbl.getInstance();
    //    AudioVisualRequestTbl.getInstance();
    //
    //    PathTbl.getInstance().createAStarPath("FDEPT00101", "WHALL00702");
    //    MedEquipmentTbl.getInstance();
    //
    //    floorMaps.load();
    //    App.launch(App.class, args);
  }
}
