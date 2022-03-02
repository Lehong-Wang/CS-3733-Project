package edu.wpi.CS3733.c22.teamG;

import edu.wpi.CS3733.c22.teamG.componentObjects.floorMaps;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class App extends Application {

  @Override
  public void init() {
    log.info("Starting Up");
  }

  @Override
  public void start(Stage primaryStage) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("views/loginScreen.fxml"));
    // Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));

    // Parent root = FXMLLoader.load(getClass().getResource("views/main.fxml"));
    Scene scene = new Scene(root, 1280, 800);
    // Below line is to set styleSheet, does not maintain styleSheet when switching scenes unless
    // stylesheet is added in fxml file
    // scene.getStylesheets().add(getClass().getResource("styleSheets/cssTest.css").toExternalForm());

    //
    // scene.getStylesheets().add(getClass().getResource("styleSheets/DarkMode.css").toExternalForm());
    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();
    primaryStage.getIcons().add(floorMaps.hospitalLogo);
    primaryStage.setTitle("Brigham & Woman's Hospital");
    // primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("views/main.fxml"))));
    // primaryStage.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
