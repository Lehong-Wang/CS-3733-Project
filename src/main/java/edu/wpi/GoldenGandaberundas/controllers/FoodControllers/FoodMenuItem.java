package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.Food;

public class FoodMenuItem {

  Food foodItem;
  String foodName;
  int quantity;

  public FoodMenuItem(Food foodItem, int quantity) {
    this.foodItem = foodItem;
    this.foodName = foodItem.getFoodName();
    this.quantity = quantity;
  }

  public void increaseQuantity() {
    quantity++;
  }

  public void decreaseQuantity() {
    quantity--;
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public Food getFoodItem() {
    return foodItem;
  }

  public void setFoodItem(Food foodItem) {
    this.foodItem = foodItem;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }
}
