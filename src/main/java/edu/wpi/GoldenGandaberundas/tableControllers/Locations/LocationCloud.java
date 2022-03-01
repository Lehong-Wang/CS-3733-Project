package edu.wpi.GoldenGandaberundas.tableControllers.Locations;

import edu.wpi.GoldenGandaberundas.CloudController;
import java.util.ArrayList;

public class LocationCloud {
  private ArrayList<Location> objList = null;
  private String tbName = "Locations";

  public LocationCloud(ArrayList<Location> objList) {
    this.objList = objList;
  }

  public void writeTable() {
    CloudController.getInstance().uploadAllDocuments("Locations", objList);
  }

  public Location getLocation(String pkid) {
    return (Location) CloudController.getInstance().getEntry("Locations", pkid, Location.class);
  }

  public boolean editEntry(String pkid, String colname, Object value) {
    return CloudController.getInstance().editEntry(tbName, pkid, colname, value);
  }

  public boolean deleteEntry(String pkid){
    return CloudController.getInstance().deleteEntry(tbName, pkid);
  }
}
