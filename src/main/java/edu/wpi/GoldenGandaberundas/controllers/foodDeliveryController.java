package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.Food;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.ListSelectionView;

public class foodDeliveryController {

  @FXML ListSelectionView<Food> bruh;
  @FXML JFXButton homeBtn;
  @FXML ChoiceBox restaurantCB;
  @FXML Label menuLabel;



  @FXML JFXButton currentOrdersBtn; // Cool Sliding Button
  @FXML JFXDrawer drawer; // sliding side menu
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML TableView ordersTbl; // Service Table


  @FXML
  public void initialize() {

  }


}
