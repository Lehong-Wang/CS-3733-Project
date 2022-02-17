package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.componentObjects.floorMaps;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class homePageController {

  @FXML ImageView mainImage;

  public void initialize() {
    mainImage.setImage(floorMaps.hospital);
  }
}
