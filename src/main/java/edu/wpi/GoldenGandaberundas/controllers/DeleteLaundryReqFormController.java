package edu.wpi.GoldenGandaberundas.controllers;

import edu.wpi.GoldenGandaberundas.tableControllers.LaundryService.LaundryRequestTbl;
import java.util.ArrayList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DeleteLaundryReqFormController {
  @FXML TextField laundryRequestIDField;
  @FXML TextField laundryItemField;

  LaundryRequestTbl laundryReq = LaundryRequestTbl.getInstance();

  public void deleteOrder() {
    ArrayList<Integer> pkIDs = new ArrayList<Integer>();
    pkIDs.add(Integer.parseInt(laundryRequestIDField.getText()));
    pkIDs.add(Integer.parseInt(laundryItemField.getText()));
    System.out.println(pkIDs);

    System.out.println(laundryReq.readTable().get(1).getPK());
    if (laundryReq.entryExists(pkIDs)) {
      laundryReq.deleteEntry(pkIDs);
      laundryRequestIDField.setText("Request Deleted!");
      laundryItemField.setText("");
    } else {
      laundryRequestIDField.setText("Invalid Request!");
      laundryItemField.setText("");
    }
  }
}
