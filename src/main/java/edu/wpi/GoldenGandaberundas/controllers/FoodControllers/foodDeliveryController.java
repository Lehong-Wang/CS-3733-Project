package edu.wpi.GoldenGandaberundas.controllers.FoodControllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.App;
import edu.wpi.GoldenGandaberundas.CurrentUser;
import edu.wpi.GoldenGandaberundas.tableControllers.EmployeePermissionTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.Food;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequest;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodRequestTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.FoodService.FoodTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.LocationTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.Patient;
import edu.wpi.GoldenGandaberundas.tableControllers.Patients.PatientTbl;
import edu.wpi.GoldenGandaberundas.tableControllers.Requests.RequestTable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.controlsfx.control.SearchableComboBox;

public class foodDeliveryController {

  @FXML TextField quantityField;

  @FXML TableView foodMenuTable;
  @FXML TableColumn<Food, Integer> foodID;
  @FXML TableColumn<Food, String> foodNameMenu;
  @FXML TableColumn<Food, Double> price;
  @FXML TableColumn<Food, Boolean> inStock;
  @FXML TableColumn<Food, String> ingredients;
  @FXML TableColumn<Food, Integer> calories;
  @FXML TableColumn<Food, String> allergens;
  @FXML TableColumn<Food, String> foodType;

  @FXML TableView menuTable;
  @FXML TableColumn<FoodMenuItem, String> foodNameCart;
  @FXML TableColumn<FoodMenuItem, Integer> quantityCart;
  ArrayList<FoodMenuItem> currentMenu = new ArrayList<>();

  @FXML TableView foodRequestsTable;
  @FXML TableColumn<FoodRequest, Integer> requestID;
  @FXML TableColumn<FoodRequest, String> locationID;
  @FXML TableColumn<FoodRequest, Integer> timeStart;
  @FXML TableColumn<FoodRequest, Integer> timeEnd;
  @FXML TableColumn<FoodRequest, Integer> patientID;
  @FXML TableColumn<FoodRequest, Integer> empInitiated;
  @FXML TableColumn<FoodRequest, Integer> empCompleter;
  @FXML TableColumn<FoodRequest, String> requestStatus;
  @FXML TableColumn<FoodRequest, String> notes;
  @FXML TableColumn<FoodRequest, Integer> foodTblID;
  @FXML TableColumn<FoodRequest, Integer> quantity;

  @FXML SearchableComboBox<Integer> patientComboBox;
  @FXML SearchableComboBox<String> locationComboBox;
  @FXML TextArea noteField;

  // Admin Buttons
  @FXML private JFXButton backupMenuButton;
  @FXML private JFXButton backupRequestsButton;
  @FXML private JFXButton loadMenuButton;
  @FXML private JFXButton loadRequestButton;
  @FXML private JFXButton refreshButton;

  @FXML
  public void initialize() {

    /**
     * Sets the cell value factories for the food menu Each of them uses the values from the Food
     * Object
     */
    foodID.setCellValueFactory(new PropertyValueFactory<Food, Integer>("foodID"));
    foodType.setCellValueFactory(new PropertyValueFactory<Food, String>("foodType"));
    // price.setCellValueFactory(new PropertyValueFactory<Food, Double>("price"));
    inStock.setCellValueFactory(new PropertyValueFactory<Food, Boolean>("inStock"));
    foodNameMenu.setCellValueFactory(new PropertyValueFactory<Food, String>("foodName"));
    ingredients.setCellValueFactory(new PropertyValueFactory<Food, String>("ingredients"));
    calories.setCellValueFactory(new PropertyValueFactory<Food, Integer>("calories"));
    allergens.setCellValueFactory(new PropertyValueFactory<Food, String>("allergens"));

    /**
     * Sets the cell value factories for the requests table each of the cell value factories make a
     * new property value factory which takes in the exact name of the value in the class of
     * foodrequest
     */
    requestID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("requestID"));
    locationID.setCellValueFactory(new PropertyValueFactory<FoodRequest, String>("locationID"));
    timeStart.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("timeStart"));
    timeEnd.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("timeEnd"));
    patientID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("patientID"));
    empInitiated.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, Integer>("empInitiated"));
    empCompleter.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, Integer>("empCompleter"));
    requestStatus.setCellValueFactory(
        new PropertyValueFactory<FoodRequest, String>("requestStatus"));
    notes.setCellValueFactory(new PropertyValueFactory<FoodRequest, String>("notes"));
    foodTblID.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("foodID"));
    quantity.setCellValueFactory(new PropertyValueFactory<FoodRequest, Integer>("quantity"));

    /**
     * Sets the cell value factories for the "cart" table Each makes one out of the FoodMenuItem
     * included in the FoodController package
     */
    foodNameCart.setCellValueFactory(new PropertyValueFactory<FoodMenuItem, String>("foodName"));
    quantityCart.setCellValueFactory(new PropertyValueFactory<FoodMenuItem, Integer>("quantity"));

    /** Gets the patients from the patient table and sets them to the patient combobox */
    ArrayList<Integer> patientIDs = new ArrayList<Integer>();
    for (Patient p : PatientTbl.getInstance().readTable()) {
      patientIDs.add(p.getPatientID());
    }
    patientComboBox.setItems(FXCollections.observableArrayList(patientIDs));

    /** Gets the locations from the locations table and sets them to the location combobox */
    ArrayList<String> locations = new ArrayList<String>();
    for (Location l : (ArrayList<Location>) LocationTbl.getInstance().readTable()) {
      locations.add(l.getNodeID());
    }
    locationComboBox.setItems(FXCollections.observableArrayList(locations));

    /**
     * This function is what allows the current "cart" menu to be double clicked and remove one
     * quantity of an item from a cart.
     */
    menuTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            removeOneFromCart();
          }
        });

    /**
     * This function is what allows the foodRequeststable to be double clicked and open the edit
     * form
     */
    foodRequestsTable.setOnMouseClicked(
        e -> {
          if (e.getClickCount() > 1) {
            onEdit();
          }
        });

    checkPerms();
    refresh();
  }

  /**
   * First, gets the selected item from the foodrequests table, then loads the edit form for food
   * and uses its editForm "constructor" to pass in the selectd item. Then opens
   */
  public void onEdit() {
    if (foodRequestsTable.getSelectionModel().getSelectedItem() != null) {
      FoodRequest selectedItem =
          (FoodRequest) foodRequestsTable.getSelectionModel().getSelectedItem();
      try {
        FXMLLoader load = new FXMLLoader(App.class.getResource("views/editFoodReqForm.fxml"));
        AnchorPane editForm = load.load();
        editFoodReqFormController edit = load.getController();
        edit.editForm(RequestTable.getInstance().getEntry(selectedItem.getPK().get(0)));
        Stage stage = new Stage();
        stage.setScene(new Scene(editForm));
        stage.show();

      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  /** Refreshes the tables */
  public void refresh() {
    if (CurrentUser.getUser().getEmpID() != 0) {
      ArrayList<FoodRequest> frs = new ArrayList<>();
      for (FoodRequest fr : FoodRequestTbl.getInstance().readTable()) {
        if (fr.getEmpInitiated() == CurrentUser.getUser().getEmpID()) {
          frs.add(fr);
        }
      }
      foodRequestsTable.getItems().setAll(frs);
    } else {
      foodRequestsTable.getItems().setAll(FoodRequestTbl.getInstance().readTable());
    }

    foodMenuTable.getItems().setAll(FoodTbl.getInstance().readTable());
    menuTable.getItems().setAll(currentMenu);
  }

  /**
   * When button is pressed, checks to see if there is a selected item on the foodMenuTable, if
   * there is it will add an instance of it to the "cart" if it doesnt exist. If it does exist it
   * will increase its quantity and leave the function
   */
  public void addToCart() {
    if (foodMenuTable.getSelectionModel().getSelectedItem() != null) {
      Food addFood = (Food) foodMenuTable.getSelectionModel().getSelectedItem();
      for (FoodMenuItem fm : currentMenu) {
        if (fm.getFoodItem().equals(addFood)) {
          fm.increaseQuantity();
          refresh();
          return;
        }
      }
      FoodMenuItem menuItem = new FoodMenuItem(addFood, 1);
      currentMenu.add(menuItem);
      refresh();
    }
  }

  /**
   * When remove from cart button is pressed, checks to see if there is a selected item on the
   * "menu", no matter the quantity it will remove if if it exists
   */
  public void removeFromCart() {
    if (menuTable.getSelectionModel().getSelectedItem() != null) {
      currentMenu.remove(menuTable.getSelectionModel().getSelectedItem());
    }
    refresh();
  }

  /**
   * Is the method that is used when the "cart" table is double clicked Decreases the quantity of
   * the menu item by one unless it decreases it to 0 where it will delete it instead.
   */
  public void removeOneFromCart() {
    if (menuTable.getSelectionModel().getSelectedItem() != null) {
      ((FoodMenuItem) menuTable.getSelectionModel().getSelectedItem()).decreaseQuantity();
      if (((FoodMenuItem) menuTable.getSelectionModel().getSelectedItem()).getQuantity() == 0) {
        removeFromCart();
      }
    }

    refresh();
  }

  /**
   * Submits a request in the requests table when there is stuff in the "cart" Only things not
   * auto-generated are the patientID, the locationID and the notes
   */
  public void submit() {
    int requestNum =
        RequestTable.getInstance().readTable().size() - 1 < 0
            ? 0
            : RequestTable.getInstance()
                    .readTable()
                    .get(RequestTable.getInstance().readTable().size() - 1)
                    .getRequestID()
                + 1;
    int requesterID = CurrentUser.getUser().getEmpID();
    int patientID = patientComboBox.getValue();
    String location = locationComboBox.getValue();
    String notes = noteField.getText();
    for (FoodMenuItem fm : currentMenu) {
      FoodRequest foodRequest =
          new FoodRequest(
              requestNum,
              location,
              requesterID,
              null,
              0,
              0,
              patientID,
              "Submitted",
              notes,
              fm.getFoodItem().getFoodID(),
              fm.getQuantity());
      RequestTable.getInstance().addEntry(foodRequest);
    }
    currentMenu.clear();
    refresh();
  }

  /** Opens a file directory and prompts the user to create a backup for the food menu database */
  @FXML
  public void backupFood() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Food File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      FoodTbl.getInstance().createBackup(new File(selectedFile.toString() + "\\foodTblBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  /**
   * Opens a file directory and prompts the user to create a backup for the food requests database
   */
  @FXML
  public void backupRequests() {
    DirectoryChooser directoryChooser = new DirectoryChooser();
    directoryChooser.setTitle("Select Back Up Requests File");
    Stage popUpDialog = new Stage();
    File selectedFile = directoryChooser.showDialog(popUpDialog);
    popUpDialog.show();

    if (selectedFile != null) {
      FoodRequestTbl.getInstance()
          .createBackup(new File(selectedFile.toString() + "\\medLaundryRequestsBackUp.csv"));
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
  }

  /** Opens a file directory and prompts the user to load a DB File Backup for the food menu */
  @FXML
  public void loadDBFood() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Food File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      FoodTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  /**
   * Opens a file directory and prompts the user to load a DB File Backup for the food requests
   * table
   */
  @FXML
  public void loadDBRequests() {
    FileChooser fileChooser = new FileChooser();
    fileChooser.setTitle("Select Back Up Food Requests File To Load");
    fileChooser
        .getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("Comma Seperated Values", "*.csv", "*.CSV"));
    Stage popUpDialog = new Stage();
    File selectedFile = fileChooser.showOpenDialog(popUpDialog);
    popUpDialog.show();
    if (selectedFile != null) {
      System.out.println(selectedFile.toString());
      FoodRequestTbl.getInstance().loadBackup(selectedFile.toString());
    } else {
      System.err.println("BACK UP FILE SELECTED DOES NOT EXIST");
    }
    popUpDialog.close();
    refresh();
  }

  /**
   * Method that iterates through a users permissions and hides elements they dont have access too
   */
  public void checkPerms() {
    int currID = CurrentUser.getUser().getEmpID();
    //
    ArrayList<Integer> perms = EmployeePermissionTbl.getInstance().getPermID(currID);
    System.out.println(perms);
    boolean hideShit = true;
    for (int i = 0; i < perms.size(); i++) {
      if (perms.get(i) == 111) {
        hideShit = false;
        break;
      }
    }
    if (hideShit == true) {
      hideAdmin();
    }
  }

  /** Helper method for checking perms which uses a switch case to hide elements */
  public void hideAdmin() {
    backupMenuButton.setVisible(false);
    backupMenuButton.setManaged(false);
    backupRequestsButton.setVisible(false);
    backupRequestsButton.setManaged(false);
    loadMenuButton.setVisible(false);
    loadMenuButton.setManaged(false);
    loadRequestButton.setVisible(false);
    loadRequestButton.setManaged(false);
    refreshButton.setVisible(false);
    refreshButton.setManaged(false);
  }
}
