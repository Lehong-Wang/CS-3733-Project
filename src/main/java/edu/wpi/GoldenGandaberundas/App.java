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
    Parameters params = getParameters();
    List<String> list = params.getRaw();
    System.out.println(list.size());
    for (String each : list) {
      System.out.println(each);
    }
    /*
    * int xCoord,
      int yCoord,
      int windowWidth,
      int windowLength,
      String cssPath,
      SimMedEquipmentTbl medEquipmentTbl,
      PathTbl pathTbl,
      LocationTbl locationTbl
    *
    *
     */
    int xCoord = 0;
    int yCoord = 0;
    int windowWidth = 0;
    int windowHeight = 0;
    try {
      xCoord = Integer.parseInt(list.get(0));
      yCoord = Integer.parseInt(list.get(1));
      windowWidth = Integer.parseInt(list.get(2));
      windowHeight = Integer.parseInt(list.get(3));
    } catch (NumberFormatException num) {
      num.printStackTrace();
      System.err.println(
          "[APP START] numeric parameters invalid \n check that the parameters are valid integers");
      return;
    } catch (Exception e) {
      e.printStackTrace();
      System.out.println(e.getMessage());
    }
    Parent root = FXMLLoader.load(getClass().getResource("views/simulationView.fxml"));
    Scene scene = new Scene(root, windowWidth, windowHeight);
    // Below line is to set styleSheet, does not maintain styleSheet when switching scenes unless
    // stylesheet is added in fxml file
    Stage stage2 = new Stage();
    scene.getStylesheets().add(getClass().getResource(list.get(4)).toExternalForm());
    stage2.setScene(scene);
    // primaryStage.setMaximized(false);
    stage2.setX(xCoord);
    stage2.setY(yCoord);
    stage2.setWidth(windowWidth);
    stage2.setHeight(windowHeight);
    stage2.show();
  }

  @Override
  public void stop() {
    log.info("Shutting Down");
  }
}
