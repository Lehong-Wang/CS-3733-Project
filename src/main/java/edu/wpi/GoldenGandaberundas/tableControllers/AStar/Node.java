package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Node {

  public int x;
  public int y;
  public double g = 0;
  public double f = 0;
  public Node parent = null;

  public List<Edge> neighbors;
  private int id;

  public Node(int x, int y) {
    this.x = x;
    this.y = y;
    this.neighbors = new ArrayList<Edge>();
  }

  public void addBranch(Node node) {
    double weight = Math.sqrt(Math.pow((this.x - node.x), 2) + Math.pow((this.y - node.y), 2));
    Edge newEdge = new Edge(weight, node);
    neighbors.add(newEdge);
  }

  public double calculateHeuristic(Node target) {
    return Math.sqrt((this.x - target.x) ^ 2 + (this.y - target.y) ^ 2);
  }

  public static ArrayList<Node> aStar(Node start, Node target) {
    PriorityQueue<Node> closedList = new PriorityQueue<>();
    PriorityQueue<Node> openList = new PriorityQueue<>();
    ArrayList<Node> path = new ArrayList<>();

    start.f = start.g + start.calculateHeuristic(target);
    openList.add(start);

    while (!openList.isEmpty()) {
      Node n = openList.peek();
      if (n == target) {
        return path;
      }

      for (Edge edge : n.neighbors) {
        Node m = edge.node;
        double totalWeight = n.g + edge.weight;
        if (!openList.contains(m) && !closedList.contains(m)) {
          m.parent = n;
          m.g = totalWeight;
          m.f = m.g + m.calculateHeuristic(target);
          openList.add(m);
        } else {
          if (totalWeight < m.g) {
            m.parent = n;
            m.g = totalWeight;
            m.f = m.g + m.calculateHeuristic(target);

            if (closedList.contains(m)) {
              closedList.remove(m);
              openList.add(m);
            }
          }
        }
      }
      openList.remove(n);
      closedList.add(n);
    }
    return null;
  }

  public static void printPath(Node target) {
    Node n = target;

    if (n == null) return;

    List<Integer> ids = new ArrayList<>();

    while (n.parent != null) {
      ids.add(n.id);
      n = n.parent;
    }
    ids.add(n.id);
    Collections.reverse(ids);

    for (int id : ids) {
      System.out.print(id + " ");
    }
    System.out.println("");
  }
}
