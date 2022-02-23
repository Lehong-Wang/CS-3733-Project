package edu.wpi.GoldenGandaberundas.tableControllers.AStar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Point implements Comparable<Point> {

  public int x;
  public int y;
  public double g = 0;
  public double f = 0;
  public Point parent = null;
  public String loc;

  public List<Edge> neighbors;
  public int id;
  private static int idCounter = 0;

  public Point(String loc, int x, int y) {
    this.x = x;
    this.y = y;
    this.id = idCounter++;
    this.loc = loc;
    this.neighbors = new ArrayList<Edge>();
  }

  /**
   * Takes in a point and adds its branches with the right weight
   *
   * @param point the point to add branches for
   */
  public void addBranch(Point point) {
    double weight = Math.sqrt(Math.pow((this.x - point.x), 2) + Math.pow((this.y - point.y), 2));
    Edge newEdge = new Edge(weight, point);
    neighbors.add(newEdge);
  }

  /**
   * finds the heuristic for the path planning
   *
   * @param target
   * @return
   */
  public double calculateHeuristic(Point target) {
    return Math.sqrt(Math.pow((this.x - target.x), 2) + Math.pow((this.y - target.y), 2));
  }

  /**
   * Calculates astart from one point to another
   *
   * @param target the end point
   * @return the start point
   */
  public Point aStar(Point target) {
    PriorityQueue<Point> closedList = new PriorityQueue<>();
    PriorityQueue<Point> openList = new PriorityQueue<>();

    this.f = this.g + this.calculateHeuristic(target);
    openList.add(this);

    while (!openList.isEmpty()) {
      Point n = openList.peek();
      if (n == target) {
        return n;
      }

      for (Edge edge : n.neighbors) {
        Point m = edge.point;
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

  /**
   * find the path bases on the parents
   *
   * @param target the end point
   * @return the start point
   */
  public List<String> locationsPath(Point target) {
    Point n = target;
    List<String> locs = new ArrayList<>();

    if (n == null) return locs;

    while (n.parent != null) {
      locs.add(n.loc);
      n = n.parent;
    }
    locs.add(n.loc);
    Collections.reverse(locs);

    //    for (String loc : locs) {
    //      System.out.print(loc + " ");
    //    }
    //    System.out.println("");
    return locs;
  }

  public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(loc);
    stringBuilder.append(",");
    stringBuilder.append(x);
    stringBuilder.append(",");
    stringBuilder.append(y);
    stringBuilder.append(",");
    stringBuilder.append(neighbors);
    return stringBuilder.toString();
  }

  /**
   * used for the priority queue
   *
   * @param o
   * @return
   */
  @Override
  public int compareTo(Point o) {
    return Double.compare(this.f, o.f);
  }
}
