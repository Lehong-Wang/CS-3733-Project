package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

public class Path {
  private String edgeID;
  private String startNode;
  private String endNode;

  public Path(String edgeID, String startNode, String endNode) {
    this.edgeID = edgeID;
    this.startNode = startNode;
    this.endNode = endNode;
  }

  public Path() {}

  public String getEdgeID() {
    return edgeID;
  }

  public void setEdgeID(String edgeID) {
    this.edgeID = edgeID;
  }

  public String getStartNode() {
    return startNode;
  }

  public void setStartNode(String startNode) {
    this.startNode = startNode;
  }

  public String getEndNode() {
    return endNode;
  }

  public void setEndNode(String endNode) {
    this.endNode = endNode;
  }

  public boolean equals(Object o) {
    Path obj = (Path) o;
    return edgeID.equals(obj.edgeID)
        && startNode.equals(obj.startNode)
        && endNode.equals(obj.endNode);
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(edgeID);
    stringBuilder.append(",");
    stringBuilder.append(startNode);
    stringBuilder.append(",");
    stringBuilder.append(endNode);
    return stringBuilder.toString();
  }
}
