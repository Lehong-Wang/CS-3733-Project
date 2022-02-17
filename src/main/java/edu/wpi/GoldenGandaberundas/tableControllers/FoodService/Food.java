package edu.wpi.GoldenGandaberundas.tableControllers.FoodService;

import java.util.Objects;

public class Food {

  private int foodID;
  private String description;
  private double price;
  private boolean inStock;
  private String foodType;

  public Food(int foodID, String description, double price, boolean inStock, String foodType) {
    this.foodID = foodID;
    this.description = description;
    this.price = price;
    this.inStock = inStock;
    this.foodType = foodType;
  }

  public Food() {}

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Food food = (Food) o;
    return foodID == food.foodID
        && Double.compare(food.price, price) == 0
        && inStock == food.inStock
        && Objects.equals(description, food.description)
        && Objects.equals(foodType, food.foodType);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(foodID);
    sb.append(",");
    sb.append(description);
    sb.append(",");
    sb.append(price);
    sb.append(",");
    sb.append(inStock);
    sb.append(",");
    sb.append(foodType);
    return sb.toString();
  }

  public int getFoodID() {
    return foodID;
  }

  public void setFoodID(int foodID) {
    this.foodID = foodID;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    this.price = price;
  }

  public boolean getInStock() {
    return inStock;
  }

  public void setInStock(boolean inStock) {
    this.inStock = inStock;
  }

  public String getFoodType() {
    return foodType;
  }

  public void setFoodType(String foodType) {
    this.foodType = foodType;
  }
}
