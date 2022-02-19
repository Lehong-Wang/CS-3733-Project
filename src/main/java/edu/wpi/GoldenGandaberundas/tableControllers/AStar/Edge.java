package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

public class Edge {
  public double weight;
  public Point point;

  public Edge(double weight, Point point) {
    this.weight = weight;
    this.point = point;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(weight);
    return stringBuilder.toString();
  }
}
