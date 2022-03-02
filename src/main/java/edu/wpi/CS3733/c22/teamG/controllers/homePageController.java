package edu.wpi.CS3733.c22.teamG.controllers;

import edu.wpi.CS3733.c22.teamG.componentObjects.floorMaps;
import java.util.ArrayList;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class homePageController {

  int count = 0;
  @FXML ImageView mainImage;

  public void initialize() {
    //    mainImage.setImage(floorMaps.hospital);
    slideShow();
  }

  public void slideShow() {

    ArrayList<Image> images = new ArrayList<>();
    images.add(floorMaps.hospital);
    images.add(floorMaps.peopleHospital);
    images.add(floorMaps.sideBuildingHospital);
    images.add(floorMaps.PeopleWalkingBWH);
    images.add(floorMaps.BWH_BridgeInside);
    images.add(floorMaps.lookingAtXrays);

    Timeline timeline = new Timeline();
    KeyValue transparent = new KeyValue(mainImage.opacityProperty(), 0.4);
    KeyValue opaque = new KeyValue(mainImage.opacityProperty(), 1.0);

    KeyFrame startFadeIn = new KeyFrame(Duration.millis(500), transparent);
    KeyFrame endFadeIn = new KeyFrame(Duration.seconds(1), opaque);
    KeyFrame startFadeOut = new KeyFrame(Duration.seconds(25), opaque);
    KeyFrame endFadeOut =
        new KeyFrame(
            Duration.millis(500),
            e -> {
              if (count < images.size()) {
                mainImage.setImage(images.get(count));
                if (count == images.size() - 1) {
                  count = 0;
                } else {
                  count++;
                }
              }
            },
            transparent);

    timeline.getKeyFrames().addAll(startFadeIn, endFadeIn, startFadeOut, endFadeOut);

    timeline.setCycleCount(Animation.INDEFINITE);
    timeline.play();
  }
}
