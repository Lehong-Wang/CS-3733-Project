package edu.wpi.GoldenGandaberundas;

import java.io.IOException;
import java.util.List;
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
  /*
  public void run(int xCoord, int yCoord, int windowWidth, int windowLength, String cssPath,
                         String destLocationID, String originLocationID) throws ServiceException {

    }
   */
  @Override
  public void start(Stage primaryStage) throws IOException {

    Parent root = FXMLLoader.load(getClass().getResource("views/simulationView.fxml"));

    Scene scene = new Scene(root, 1280, 800);
    // Below line is to set styleSheet, does not maintain styleSheet when switching scenes unless
    // stylesheet is added in fxml file
    // scene.getStylesheets().add(getClass().getResource("styleSheets/cssTest.css").toExternalForm());

    primaryStage.setScene(scene);
    primaryStage.setMaximized(true);
    primaryStage.show();

    Parameters params = getParameters();
    List<String> list = params.getRaw();
    System.out.println(list.size());
    for (String each : list) {
      System.out.println(each);
    }
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
