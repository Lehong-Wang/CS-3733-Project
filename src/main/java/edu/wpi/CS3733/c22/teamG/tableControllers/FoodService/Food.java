package edu.wpi.CS3733.c22.teamG.tableControllers.FoodService;

import java.util.Objects;

public class Food {

  private int foodID;
  private String foodName;
  private String ingredients;
  private Integer calories;
  private String allergens;
  private double price;
  private boolean inStock;
  private String foodType;

  public Food(
      int foodID,
      String foodName,
      String ingredients,
      Integer calories,
      String allergens,
      double price,
      boolean inStock,
      String foodType) {
    this.foodID = foodID;
    this.foodName = foodName;
    this.ingredients = ingredients;
    this.allergens = allergens;
    this.calories = calories;
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
        && food.price == price
        && inStock == food.inStock
        && Objects.equals(ingredients, food.ingredients)
        && Objects.equals(foodType, food.foodType)
        && Objects.equals(foodName, food.foodName)
        && calories.equals(food.calories)
        && Objects.equals(allergens, food.allergens);
  }

  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append(foodID);
    sb.append(",");
    sb.append(foodName);
    sb.append(",");
    sb.append(ingredients);
    sb.append(",");
    sb.append(calories);
    sb.append(",");
    sb.append(allergens);
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

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public String getIngredients() {
    return ingredients;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }

  public Integer getCalories() {
    return calories;
  }

  public void setCalories(Integer calories) {
    this.calories = calories;
  }

  public String getAllergens() {
    return allergens;
  }

  public void setAllergens(String allergens) {
    this.allergens = allergens;
  }

  public boolean isInStock() {
    return inStock;
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
