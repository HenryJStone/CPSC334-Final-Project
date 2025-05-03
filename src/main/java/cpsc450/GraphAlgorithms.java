/**
 * CPSC 450, Final Project
 *
 * NAME: Henry Stone
 * DATE: Fall 2024
 *
 */ 

package cpsc450;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;


/** 
 * Suite of graph-based algorithms. 
 */
public class GraphAlgorithms {

  /**
   * Returns the single-source shortest (integer) weighted paths from the given
   * source vertex using Dijkstra's algorithm. Assumes weights are
   * given as (positive) integers such that the maximum weight is
   * Integer.MAX_VALUE. 
   * @param g The given directed graph.
   * @param l The given edge (weighted) labeling
   * @param s The source vertex
   * @returns The minimum path cost from s to each vertex v (reachable
   * from s) given as a map from vertices to their corresponding
   * shortest path weights. 
   */
  public static Map<Integer,Integer> Dijkstras(Graph g, EdgeLabeling<Integer> l, int s)
  {
    int vertices = g.vertices();
    Map<Integer, Integer> distances = new HashMap<>(); // Distance from source to each vertex
    PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1])); // Min-heap [vertex, distance]
    Set<Integer> visited = new HashSet<>(); // Track visited nodes

    // Initialize distances to all vertices as infinity, except the source
    for(int i = 0; i < vertices; i++){
      distances.put(i, Integer.MAX_VALUE);
    }
    distances.put(s, 0); // Distance to source is 0
    pq.add(new int[]{s, 0}); // Start with the source vertex

    while (!pq.isEmpty()) {
      // Extract the vertex with the minimum distance
      int[] current = pq.poll();
      int currentVertex = current[0];
      int currentDistance = current[1];

      if(visited.contains(currentVertex)){
       continue;
      }
      visited.add(currentVertex);

      // Process all outgoing edges from the current vertex
      for(int neighbor : g.out(currentVertex)){
        if(visited.contains(neighbor)){
          continue;
        }

        // Get the weight of the edge currentVertex -> neighbor
        int edgeWeight = l.getLabel(currentVertex, neighbor).orElse(Integer.MAX_VALUE);

        // Relaxation step: Update distance if a shorter path is found
        int newDistance = currentDistance + edgeWeight;
        if(newDistance < distances.get(neighbor)){
          distances.put(neighbor, newDistance);
          pq.add(new int[]{neighbor, newDistance});
        }
      }
    }

    return distances; // Return the map of shortest distances from source to all reachable vertices
  }

  /**
   * Computes the single-source shortest (integer) weighted paths from
   * the given source vertex using the Bellman-Ford algorithm. Assumes
   * weights are given as (possibly negative) integers where the
   * maximum weight is Integer.MAX_VALUE.
   * @param g The given directed graph.
   * @param l The given edge (integer-weighted) labeling
   * @param s The source vertex
   * @returns The minimum path cost from s to each vertex v (reachable
   * from s) given as a list with indexes as vertices and values as
   * path costs from s. If the graph has a negative cycle, an empty
   * list is returned. 
   */
  public static List<Integer> bellmanFord(Graph g, EdgeLabeling<Integer> l, int s) {
    // TODO: Implement the Bellman-Ford algorithm
    List<Integer> dist = new ArrayList<>(Collections.nCopies(g.vertices(), Integer.MAX_VALUE));
    dist.set(s, 0); // Distance to the source is 0

    for(int i = 1; i < g.vertices(); i++){
      for(int u = 0; u < g.vertices(); u++){
        for(int v : g.out(u)){
          int weight = l.getLabel(u, v).orElse(Integer.MAX_VALUE);
          if(dist.get(u) != Integer.MAX_VALUE && dist.get(v) > dist.get(u) + weight){
            dist.set(v, dist.get(u) + weight);
          }
        }
      }
    }

    // Check for negative-weight cycles
    for(int u = 0; u < g.vertices(); u++){
      for(int v : g.out(u)){
        int weight = l.getLabel(u, v).orElse(Integer.MAX_VALUE);
        if(dist.get(u) != Integer.MAX_VALUE && dist.get(v) > dist.get(u) + weight){
          // Found a negative cycle
          return new ArrayList<>();
        }
      }
    }

    // No negative cycles found, return the distances
    return dist;
  }

  /**
   * Computes all pairs short (integer) weighted paths using the
   * Floyd-Warshall algorithm. Assumes (possibly negative) weights are
   * given as integers where the maximum weight is Integer.MAX_VALUE.
   * @param g The given directed graph.
   * @param l The given edge (integer-weighted) labeling
   * @returns A list of shortest-path-weight lists for each vertex in
   * the graph such that the shortest path weight from a vertex x to a
   * vertex y is given by weights.get(x).get(y). Returns an empty list
   * if the graph has a negative cycle.
   */
  public static List<List<Integer>> floydWarshall(Graph g, EdgeLabeling<Integer> l) {
    // TODO: Implement the Floyd-Warshall algorithm
    int n = g.vertices();
    // Initialize distances with the adjacency matrix representation
    List<List<Integer>> dist = new ArrayList<>();
    for(int u = 0; u < n; u++){
      List<Integer> row = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));
      dist.add(row);
      dist.get(u).set(u, 0); // Distance to self is 0
      for(int v : g.out(u)){
        int weight = l.getLabel(u, v).orElse(Integer.MAX_VALUE);
        row.set(v, weight);
      }
    }

    for(int k = 0; k < n; k++){
      for(int u = 0; u < n; u++){
        for(int v = 0; v < n; v++){
          if(dist.get(u).get(k) != Integer.MAX_VALUE && dist.get(k).get(v) != Integer.MAX_VALUE){
            dist.get(u).set(v, 
            Math.min(dist.get(u).get(v), dist.get(u).get(k) + dist.get(k).get(v)));
          }
        }
      }
    }

    // Check for negative-weight cycles
    for(int u = 0; u < n; u++){
      if(dist.get(u).get(u) < 0){
        return new ArrayList<>(); // Negative cycle detected
      }
    }

    return dist;
  }

  public static List<List<Integer>> johnsonsAlgorithm(Graph g, EdgeLabeling<Integer> l) {
    int n = g.vertices();
    Graph extendedGraph = new AdjList(n + 1);
    EdgeLabeling<Integer> extendedLabels = new EdgeLabeling<>(extendedGraph);

    for(int u = 0; u < n; u++){
      for(int v : g.out(u)){
        extendedGraph.addEdge(u, v);
        extendedLabels.addLabel(u, v, l.getLabel(u, v).orElse(Integer.MAX_VALUE));
      }
      // Add edges from the new vertex 's' to all other vertices
      extendedGraph.addEdge(n, u);
      extendedLabels.addLabel(n, u, 0);
    }

    // Run Bellman-Ford algorithm from the new vertex 's'
    List<Integer> distance = bellmanFord(extendedGraph, extendedLabels, n);
    if(distance.isEmpty()){
      // if empty, return an empty list for negative cycle
      return new ArrayList<>();
    }

    // Re-weight the edges of the original graph
    for(int u = 0; u < n; u++){
      for(int v : g.out(u)){
        int newWeight = l.getLabel(u, v).orElse(Integer.MAX_VALUE) + distance.get(u) - distance.get(v);
        l.addLabel(u, v, newWeight);
      }
    }

    // Use Dijkstra's for each vertex to find distances
    List<List<Integer>> result = new ArrayList<>();
    for(int u = 0; u < n; u++){
      Map<Integer, Integer> shortestPaths = Dijkstras(g, l, u);
      List<Integer> row = new ArrayList<>(Collections.nCopies(n, Integer.MAX_VALUE));
      for(int v : shortestPaths.keySet()){
        row.set(v, shortestPaths.get(v));
      }
      // Adjust distances back to original weights
      for(int v = 0; v < n; v++){
        if(row.get(v) != Integer.MAX_VALUE){
          row.set(v, row.get(v) - distance.get(u) + distance.get(v));
        }
      }
      result.add(row);
    }

    return result;
  }
}

