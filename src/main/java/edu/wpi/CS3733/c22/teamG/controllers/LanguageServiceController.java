package edu.wpi.CS3733.c22.teamG.controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;

public class LanguageServiceController implements Initializable {

  @FXML JFXDrawer drawer; // sliding side menu
  @FXML JFXButton currRequestsBtn; // Cool Sliding Button
  @FXML VBox drawerBox; // Cool Sliding Table
  @FXML TableView servicesTbl; // Service Table

  @Override
  public void initialize(URL location, ResourceBundle resources) {}

  @FXML
  public void backup() {}

  @FXML
  public void load() {}

  @FXML
  public void submit() {}

  @FXML
  public void clearFields() {}
}
