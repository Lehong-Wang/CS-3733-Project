package edu.wpi.GoldenGandaberundas.controllers;

import com.jfoenix.controls.JFXButton;
import edu.wpi.GoldenGandaberundas.TableController;
import edu.wpi.GoldenGandaberundas.controllers.validators.AddLocationValidator;
import edu.wpi.GoldenGandaberundas.tableControllers.Locations.Location;
import java.util.ArrayList;
import java.util.Locale;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

public class MapSubController {
  @FXML private TextField nodeIDText;
  @FXML private TextField nodeTypeText;
  @FXML private TextField sNameText;
  @FXML private TextField lNameText;
  @FXML private TextField xCoordText;
  @FXML private TextField yCoordText;
  @FXML private TextField buildingText;

  @FXML private JFXButton submitBtn;
  @FXML private JFXButton clearBtn;
  @FXML private JFXButton deleteBtn;
  @FXML private JFXButton closeBtn;

  @FXML private MenuButton floor;

  @FXML private Label errorLabel;

  @FXML private AnchorPane anchorPane;

  private ArrayList<TextField> nodeFields = new ArrayList<TextField>();
  private boolean edit = false;

  private TableController locationTbl = null;

  public void initialize() {
    nodeFields.add(nodeIDText);
    nodeFields.add(nodeTypeText);
    nodeFields.add(sNameText);
    nodeFields.add(lNameText);
    nodeFields.add(xCoordText);
    nodeFields.add(yCoordText);
    nodeFields.add(buildingText);

    submitBtn.setDisable(true);
    clearBtn.setDisable(true);
    deleteBtn.setVisible(false);
    createFloorSelector();
    closeBtn.setOnMouseReleased(
        e -> {
          anchorPane.getParent().setManaged(false);
          anchorPane.getParent().setVisible(false);
          System.out.println("alt click");
        });
  }

  public void setText(Location loc) {
    if (loc == null) {

      System.out.println("NULL");
      System.out.println();
    }
    submitBtn.setText("Submit");
    System.out.println(loc.getNodeID());
    nodeIDText.setText(loc.getNodeID());
    nodeIDText.setDisable(true);
    nodeTypeText.setText(loc.getNodeType());
    sNameText.setText(loc.getShortName());
    lNameText.setText(loc.getLongName());
    xCoordText.setText(String.valueOf(loc.getXcoord()));
    yCoordText.setText(String.valueOf(loc.getYcoord()));
    buildingText.setText(loc.getBuilding());
    floor.setText(loc.getFloor());
    edit = true;
    clearBtn.setDisable(false);
    submitBtn.setDisable(false);
    deleteBtn.setVisible(true);
  }

  /**
   * This method modifies or creates new nodes If the node has been clicked on, the node is edited
   * if all fields of the table have been filled, a new
   */
  public void editLocation() {
    AddLocationValidator validator =
        new AddLocationValidator(
            nodeIDText.getText().trim().toUpperCase(),
            xCoordText.getText(),
            yCoordText.getText(),
            "L2",
            buildingText.getText(),
            nodeTypeText.getText(),
            sNameText.getText(),
            lNameText.getText());
    if (validator.validateTextFields()) {
      if (edit) {
        String selectedID = nodeIDText.getText().trim().toUpperCase(Locale.ROOT);
        locationTbl.editEntry(selectedID, "xCoord", Integer.parseInt(xCoordText.getText().trim()));
        locationTbl.editEntry(selectedID, "yCoord", Integer.parseInt(yCoordText.getText().trim()));
        locationTbl.editEntry(selectedID, "floor", "l2");
        locationTbl.editEntry(
            selectedID, "building", buildingText.getText().trim().toLowerCase(Locale.ROOT));
        locationTbl.editEntry(selectedID, "nodeType", nodeTypeText.getText().trim());
        locationTbl.editEntry(
            selectedID, "longName", lNameText.getText().trim().toLowerCase(Locale.ROOT));
        locationTbl.editEntry(
            selectedID, "shortName", sNameText.getText().toUpperCase(Locale.ROOT));
        System.out.println(locationTbl.getEntry(nodeIDText.getText().trim()).toString());

      } else {
        Location loc =
            new Location(
                nodeIDText.getText().trim().toUpperCase(),
                Integer.parseInt(xCoordText.getText()),
                Integer.parseInt(yCoordText.getText()),
                floor.getText(),
                buildingText.getText(),
                nodeTypeText.getText(),
                sNameText.getText(),
                lNameText.getText());
        System.out.println(locationTbl.addEntry(loc));
      }
    } else {
      errorLabel.setVisible(true);
      errorLabel.setText(validator.traceValidationError());
    }
  }

  /**
   * checks the form fields to verify that they have been filled enables the submit only if all
   * fields are filled enables the clear button if any field is filled
   */
  @FXML
  private void submitLock() {
    for (TextField t : nodeFields) {
      if (t.getText().isEmpty()) {
        submitBtn.setDisable(true);
        return;
      }
      clearBtn.setDisable(false);
    }
    submitBtn.setDisable(false);
  }

  /** sets the node data form to blank */
  @FXML
  private void clear() {
    for (TextField t : nodeFields) {
      t.setText("");
    }
    nodeIDText.setDisable(false);
    clearBtn.setDisable(true);
    submitBtn.setDisable(true);
    deleteBtn.setVisible(false);
    edit = false;
  }

  /** deletes selected node */
  @FXML
  private void delete() {
    System.out.println(
        locationTbl.deleteEntry(nodeIDText.getText().trim().toUpperCase(Locale.ROOT)));
    System.out.println(
        locationTbl.entryExists(nodeIDText.getText().trim().toUpperCase(Locale.ROOT)));
    clear();
  }

  public void createFloorSelector() {
    MenuItem floorL2 = new MenuItem("L2");

    MenuItem floorL1 = new MenuItem("L1");

    MenuItem floor01 = new MenuItem("1");
    MenuItem floor02 = new MenuItem("2");
    MenuItem floor03 = new MenuItem("3");

    floor.getItems().addAll(floorL2, floorL1, floor01, floor02, floor03);

    floorL2.setOnAction(
        e -> {
          floor.setText(((MenuItem) e.getSource()).getText());
        });
    floorL1.setOnAction(
        e -> {
          floor.setText(((MenuItem) e.getSource()).getText());
        });
    floor01.setOnAction(
        e -> {
          floor.setText(((MenuItem) e.getSource()).getText());
        });
    floor02.setOnAction(
        e -> {
          floor.setText(((MenuItem) e.getSource()).getText());
        });
    floor03.setOnAction(
        e -> {
          floor.setText(((MenuItem) e.getSource()).getText());
        });
  }
}
