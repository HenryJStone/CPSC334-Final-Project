/**
 * CPSC 450, Fall 2024
 * 
 * NAME: Henry Stone
 * DATE: Fall 2024
 */

package cpsc450;

import java.util.HashSet;
import java.util.Set;

/**
 * Adjacency Matrix implementation of the Graph interface. 
 */
public class AdjMatrix implements Graph {

  private int vertexCount;      // total number of vertices
  private int edgeCount;        // running count of number of edges
  private boolean matrix[];     // storage for the matrix as "flattened" 2D array

  /**
   * Create an adjacency matrix (graph) given a specific (fixed)
   * number of vertices.
   * @param vertices The number of vertices in the graph. 
   */ 
  public AdjMatrix(int vertices) throws GraphException {
    if (vertices <= 0) {
      throw new GraphException("Number of vertices must be positive.");
    }
    this.vertexCount = vertices;
    this.edgeCount = 0;
    this.matrix = new boolean[vertices * vertices];
  }

  @Override
  public void addEdge(int x, int y) {
    if (!this.hasVertex(x) || !this.hasVertex(y)) return;
    if (!matrix[x * vertexCount + y]) { // Check if edge doesn't already exist
      matrix[x * vertexCount + y] = true;
      edgeCount++;
    }
  }

  @Override
  public void removeEdge(int x, int y) {
    if (this.hasEdge(x, y)) {
      matrix[x * vertexCount + y] = false;
      edgeCount--;
    }
  }

  @Override
  public Set<Integer> out(int x) {
    Set<Integer> outs = new HashSet<>();
    if (!this.hasVertex(x)) return outs;
    for (int i = 0; i < vertexCount; i++) {
        if (matrix[x * vertexCount + i]) {
            outs.add(i);
        }
    }
    return outs;
  }

  @Override
  public Set<Integer> in(int x) {
    Set<Integer> ins = new HashSet<>();
    if (!this.hasVertex(x)) return ins;
    for (int i = 0; i < vertexCount; i++) {
        if (matrix[i * vertexCount + x]) {
            ins.add(i);
        }
    }
    return ins;
  }

  @Override
  public Set<Integer> adj(int x) {
    Set<Integer> adjacentVertices = new HashSet<>(out(x));
    adjacentVertices.addAll(in(x));
    return adjacentVertices;
  }

  @Override
  public boolean hasEdge(int x, int y) {
    if (!this.hasVertex(x) || !this.hasVertex(y)) return false;
    return this.matrix[x * vertexCount + y];
  }

  @Override
  public boolean hasVertex(int x) {
    if (x < 0) return false;
    if (x >= vertexCount) return false;
    return true;
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
