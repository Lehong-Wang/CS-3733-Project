package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

public class Edge {
  public double weight;
  public Node node;

  public Edge(double weight, Node node) {
    this.weight = weight;
    this.node = node;
  }
}
