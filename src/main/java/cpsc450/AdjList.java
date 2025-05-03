/**
 * CPSC 450, HW-3
 * 
 * NAME: Henry Stone
 * DATE: Fall, 2024 
 */

package cpsc450;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


/**
 * Basic adjacency List implementation of the Graph interface.
 */
public class AdjList implements Graph {

  private int vertexCount;                     // total number of vertices
  private int edgeCount;                       // running count of number of edges
  private Map<Integer,Set<Integer>> outEdges;  // storage for the out edges
  private Map<Integer,Set<Integer>> inEdges;   // storage for the in edges

  /**
   * Create an adjacency list (graph) given a specific (fixed) number
   * of vertices.
   * @param vertices The number of vertices of the graph.
   */
  public AdjList(int vertices) throws GraphException {
    if (vertices <= 0) {
      throw new GraphException("Number of vertices must be positive.");
    }
    this.vertexCount = vertices;
    this.edgeCount = 0;
    this.outEdges = new HashMap<>();
    this.inEdges = new HashMap<>();
  
  // Initialize maps with empty sets for each vertex
  for (int i = 0; i < vertices; i++) {
      outEdges.put(i, new HashSet<>());
      inEdges.put(i, new HashSet<>());
  }
  }

  @Override
  public void addEdge(int x, int y) {
    if (!this.hasVertex(x) || !this.hasVertex(y)) return;
    if (!this.outEdges.get(x).contains(y)) { // Check if the edge doesn't already exist
        this.outEdges.get(x).add(y);
        this.inEdges.get(y).add(x);
        this.edgeCount++;
    }
  }

  @Override
  public void removeEdge(int x, int y) {
    if (this.hasEdge(x, y)) {
      this.outEdges.get(x).remove(y);
      this.inEdges.get(y).remove(x);
      this.edgeCount--;
    }
  }

  @Override
  public Set<Integer> out(int x) {
    if (!hasVertex(x)) return new HashSet<>(); // empty set if vertex doesn't exist
    return new HashSet<>(outEdges.get(x));
    
  }

  @Override
  public Set<Integer> in(int x) {
    if (!hasVertex(x)) return new HashSet<>(); // empty set
    return new HashSet<>(inEdges.get(x));
  }

  @Override
  public Set<Integer> adj(int x) {
    Set<Integer> adj = new HashSet<>(out(x));
    adj.addAll(in(x)); // Combine out-edges and in-edges
    return adj;
  }

  @Override
  public boolean hasEdge(int x, int y) {
    if (!hasVertex(x) || !hasVertex(y)) return false;
    return outEdges.get(x).contains(y);
  }

  @Override
  public boolean hasVertex(int x) {
    return x >= 0 && x < vertexCount;
  }

  @Override
  public int vertices() {
    return vertexCount;
  }
  
  @Override
  public int edges() {
    return edgeCount;
  }
  
}
