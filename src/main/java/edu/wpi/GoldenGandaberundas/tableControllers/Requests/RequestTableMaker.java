package edu.wpi.GoldenGandaberundas.tableControllers.Requests;

import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequestTbl;
import java.util.ArrayList;

public class RequestTableMaker {
  private AudioVisualRequestTbl audioVisual;
  private ComputerRequestTbl computer;
  private FoodRequestTbl food;
  private GiftRequestTbl gift;
  private LaundryRequestTbl laundry;
  private MedEquipRequestTbl medEquipment;
  private MedicineRequestTbl medicine;

  public RequestTableMaker() {
    audioVisual = AudioVisualRequestTbl.getInstance();
    computer = ComputerRequestTbl.getInstance();
    food = FoodRequestTbl.getInstance();
    gift = GiftRequestTbl.getInstance();
    laundry = LaundryRequestTbl.getInstance();
    medEquipment = MedEquipRequestTbl.getInstance();
    medicine = MedicineRequestTbl.getInstance();
  }

  public AudioVisualRequestTbl audioVisualGetInstance() {
    return audioVisual.getInstance();
  }

  public ComputerRequestTbl computerGetInstance() {
    return computer.getInstance();
  }

  public FoodRequestTbl foodGetInstance() {
    return food.getInstance();
  }

  public GiftRequestTbl giftGetInstance() {
    return gift.getInstance();
  }

  public LaundryRequestTbl laundryGetInstance() {
    return laundry.getInstance();
  }

  public MedEquipRequestTbl medEquipmentGetInstance() {
    return medEquipment.getInstance();
  }

  public MedicineRequestTbl medicineGetInstance() {
    return medicine.getInstance();
  }

  public ArrayList<AudioVisualRequest> audioVisualReadTable() {
    return audioVisual.readTable();
  }

  public ArrayList<ComputerRequest> computerReadTable() {
    return computer.readTable();
  }

  public ArrayList<FoodRequest> foodReadTable() {
    return food.readTable();
  }

  public ArrayList<GiftRequest> giftReadTable() {
    return gift.readTable();
  }

  public ArrayList<LaundryRequest> laundryReadTable() {
    return laundry.readTable();
  }

  public ArrayList<MedEquipRequest> medEquipmentReadTable() {
    return medEquipment.readTable();
  }

  public ArrayList<MedicineRequest> medicineReadTable() {
    return medicine.readTable();
  }

  public void audioVisualAddEntry(AudioVisualRequest obj) {
    audioVisual.addEntry(obj);
  }

  public void computerAddEntry(ComputerRequest obj) {
    computer.addEntry(obj);
  }

  public void foodAddEntry(FoodRequest obj) {
    food.addEntry(obj);
  }

  public void giftAddEntry(GiftRequest obj) {
    gift.addEntry(obj);
  }

  public void laundryAddEntry(LaundryRequest obj) {
    laundry.addEntry(obj);
  }

  public void medEquipmentAddEntry(MedEquipRequest obj) {
    medEquipment.addEntry(obj);
  }

  public void medicineReadAddEntry(MedicineRequest obj) {
    medicine.addEntry(obj);
  }

  public ArrayList<AudioVisualRequest> audioVisualReadBackup(String fileName) {
    return audioVisual.readBackup(fileName);
  }

  public ArrayList<ComputerRequest> computerReadBackup(String fileName) {
    return computer.readBackup(fileName);
  }

  public ArrayList<FoodRequest> foodReadBackup(String fileName) {
    return food.readBackup(fileName);
  }

  public ArrayList<GiftRequest> giftReadBackup(String fileName) {
    return gift.readBackup(fileName);
  }

  public ArrayList<LaundryRequest> laundryReadBackup(String fileName) {
    return laundry.readBackup(fileName);
  }

  public ArrayList<MedEquipRequest> medEquipmentReadBackup(String fileName) {
    return medEquipment.readBackup(fileName);
  }

  public ArrayList<MedicineRequest> medicineReadBackup(String fileName) {
    return medicine.readBackup(fileName);
  }

  public void audioVisualCreateTable() {
    audioVisual.createTable();
  }

  public void computerCreateTable() {
    computer.createTable();
  }

  public void foodCreateTable() {
    food.createTable();
  }

  public void giftCreateTable() {
    gift.createTable();
  }

  public void laundryCreateTable() {
    laundry.createTable();
  }

  public void medEquipmentCreateTable() {
    medEquipment.createTable();
  }

  public void medicineCreateTable() {
    medicine.createTable();
  }

  //    public AudioVisualRequest audioVisualGetEntry(String pkID) {
  //        return audioVisual.getEntry(pkID);
  //    }
  //    public ComputerRequest computerGetEntry(String pkID) {
  //        return computer.getEntry(pkID);
  //    }
  //    public FoodRequest foodGetEntry(String pkID) {
  //        return food.getEntry(pkID);
  //    }
  //    public GiftRequest giftGetEntry(String pkID) {
  //        return gift.getEntry(pkID);
  //    }
  //    public LaundryRequest laundryGetEntry(String pkID) {
  //        return laundry.getEntry(pkID);
  //    }
  //    public MedEquipRequest medEquipmentGetEntry(String pkID) {
  //        return medEquipment.getEntry(pkID);
  //    }
  //    public MedicineRequest medicineGetEntry(String pkID) {
  //        return medicine.getEntry(pkID);
  //    }
}
