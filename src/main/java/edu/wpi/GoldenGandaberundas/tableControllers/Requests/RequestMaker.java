package edu.wpi.GoldenGandaberundas.tableControllers.Requests;

import edu.wpi.GoldenGandaberundas.tableControllers.AudioVisualService.AudioVisualRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.ComputerService.ComputerRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.GiftDeliveryService.GiftRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedEquipmentDelivery.MedEquipRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.MedicineDeliveryService.MedicineRequest;

public class RequestMaker {
  private Request audioVisual;
  private Request computer;
  private Request food;
  private Request gift;
  private Request laundry;
  private Request medEquipment;
  private Request medicine;

  public RequestMaker() {
    audioVisual = new AudioVisualRequest();
    computer = new ComputerRequest();
    food = new FoodRequest();
    gift = new GiftRequest();
    laundry = new LaundryRequest();
    medEquipment = new MedEquipRequest();
    medicine = new MedicineRequest();
  }

  public void getAudioVisualRequestPK() {
    audioVisual.getPK();
  }

  public void getComputerRequestPK() {
    computer.getPK();
  }

  public void getFoodRequestPK() {
    food.getPK();
  }

  public void getGiftRequestPK() {
    gift.getPK();
  }

  public void getLaundryRequestPK() {
    laundry.getPK();
  }

  public void getMedEquipmentRequestPK() {
    medEquipment.getPK();
  }

  public void getMedicineRequestPK() {
    medicine.getPK();
  }

  public void audioVisualRequestToString() {
    audioVisual.toString();
  }

  public void computerRequestToString() {
    computer.toString();
  }

  public void foodRequestToString() {
    food.toString();
  }

  public void giftRequestToString() {
    gift.toString();
  }

  public void laundryRequestToString() {
    laundry.toString();
  }

  public void medEquipmentRequestToString() {
    medEquipment.toString();
  }

  public void medicineRequestToString() {
    medicine.toString();
  }

  public boolean audioVisualRequestEquals(AudioVisualRequest req) {
    return audioVisual.equals(req);
  }

  public boolean computerRequestEquals(ComputerRequest req) {
    return computer.equals(req);
  }

  public boolean foodRequestEquals(FoodRequest req) {
    return food.equals(req);
  }

  public boolean giftRequestEquals(GiftRequest req) {
    return gift.equals(req);
  }

  public boolean laundryRequestEquals(LaundryRequest req) {
    return laundry.equals(req);
  }

  public boolean medEquipmentRequestEquals(MedEquipRequest req) {
    return medEquipment.equals(req);
  }

  public boolean medicineRequestEquals(MedicineRequest req) {
    return medicine.equals(req);
  }
}
