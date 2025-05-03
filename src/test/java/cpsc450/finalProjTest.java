/**
 * CPSC 450, HW-4
 * 
 * NAME: S. Bowers, Henry Stone
 * DATE: Fall 2024
 *
 * Basic unit tests breadth-first search and associated algorithms.
 */

package cpsc450;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.Map;


class finalProjTest {

  //======================================================================
  // Adjacency List Tests
  //======================================================================
  
  //----------------------------------------------------------------------
  // Basic Johnsons Algorithm Tests
  // I repurposed the tests given for Floyd-Warshall in HW8
  
  @Test
  void johnsonsOneVertex() {
    Graph g = new AdjList(1);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    assertEquals(1, pathCosts.size());
    assertEquals(1, pathCosts.get(0).size());
    assertEquals(0, pathCosts.get(0).get(0));
  }

  @Test
  void johnsonsTwoVertexNoPath() {
    Graph g = new AdjList(2);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    assertEquals(2, pathCosts.size());
    assertEquals(2, pathCosts.get(0).size());
    assertEquals(2, pathCosts.get(1).size());
    assertEquals(0, pathCosts.get(0).get(0));
    assertEquals(Integer.MAX_VALUE, pathCosts.get(0).get(1));
    assertEquals(Integer.MAX_VALUE, pathCosts.get(1).get(0));
    assertEquals(0, pathCosts.get(1).get(1));
  }

  @Test
  void johnsonsTwoVertexWithPath() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 33);
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    assertEquals(2, pathCosts.size());
    assertEquals(0, pathCosts.get(0).get(0));
    assertEquals(33, pathCosts.get(0).get(1));
    assertEquals(Integer.MAX_VALUE, pathCosts.get(1).get(0));
    assertEquals(0, pathCosts.get(1).get(1));
  }

  @Test
  void johnsonsPositiveThreeVertexShortcut() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(0, 2);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(1, 2, 3);
    l.addLabel(0, 2, 2);
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    int M = Integer.MAX_VALUE;
    assertEquals(3, pathCosts.size());
    assertEquals(List.of(0, 1, 2), pathCosts.get(0));
    assertEquals(List.of(M, 0, 3), pathCosts.get(1));
    assertEquals(List.of(M, M, 0), pathCosts.get(2));
  }
  
  @Test
  void johnsonsPositiveFourVertex() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(2, 0);
    g.addEdge(3, 2);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(0, 3, 4);
    l.addLabel(1, 2, 6);
    l.addLabel(1, 3, 2);
    l.addLabel(2, 0, 1);    
    l.addLabel(3, 2, 3);    
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    assertEquals(4, pathCosts.size());
    assertEquals(List.of(0, 1, 6, 3), pathCosts.get(0));
    assertEquals(List.of(6, 0, 5, 2), pathCosts.get(1));
    assertEquals(List.of(1, 2, 0, 4), pathCosts.get(2));
    assertEquals(List.of(4, 5, 3, 0), pathCosts.get(3));
  }
  
  @Test
  void johnsonsNegativeFourVertex() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(1, 3);
    g.addEdge(2, 3);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(0, 2, 3);
    l.addLabel(1, 3, 2);
    l.addLabel(2, 3, -4);
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    int M = Integer.MAX_VALUE;
    assertEquals(4, pathCosts.size());
    assertEquals(List.of(0, 1, 3, -1), pathCosts.get(0));
    assertEquals(List.of(M, 0, M, 2), pathCosts.get(1));
    assertEquals(List.of(M, M, 0, -4), pathCosts.get(2));
    assertEquals(List.of(M, M, M, 0), pathCosts.get(3));
  }

  @Test
  void johnsonsNegativeFiveVertex() {
    Graph g = new AdjList(5);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 1);
    g.addEdge(3, 2);
    g.addEdge(3, 4);
    g.addEdge(4, 2);    
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 6);
    l.addLabel(0, 3, 7);
    l.addLabel(1, 2, 5);
    l.addLabel(1, 3, 8);
    l.addLabel(1, 4, -4);
    l.addLabel(2, 1, -2);
    l.addLabel(3, 2, -3);
    l.addLabel(3, 4, 9);
    l.addLabel(4, 2, 7);    
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    int M = Integer.MAX_VALUE;
    assertEquals(5, pathCosts.size());
    assertEquals(List.of(0, 2, 4, 7, -2), pathCosts.get(0));
    assertEquals(List.of(M, 0, 3, 8, -4), pathCosts.get(1));
    assertEquals(List.of(M, -2, 0, 6, -6), pathCosts.get(2));
    assertEquals(List.of(M, -5, -3, 0, -9), pathCosts.get(3));
    assertEquals(List.of(M, 5, 7, 13, 0), pathCosts.get(4));    
  }

  @Test
  void johnsonsNegativeCycle() {
    Graph g = new AdjList(5);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(2, 4);
    g.addEdge(3, 1);
    g.addEdge(4, 3);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(0, 3, 1);
    l.addLabel(1, 2, 2);
    l.addLabel(2, 4, 3);
    l.addLabel(3, 1, -10);
    l.addLabel(4, 3, 4);
    List<List<Integer>> pathCosts = GraphAlgorithms.johnsonsAlgorithm(g, l);
    assertEquals(0, pathCosts.size());
  }

  //----------------------------------------------------------------------
  // Floyd-Warshall all-pairs shortest paths

  @Test
  void floydWarshallOneVertex() {
    Graph g = new AdjList(1);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    assertEquals(1, pathCosts.size());
    assertEquals(1, pathCosts.get(0).size());
    assertEquals(0, pathCosts.get(0).get(0));
  }
  
  @Test
  void floydWarshallTwoVertexNoPath() {
    Graph g = new AdjList(2);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    assertEquals(2, pathCosts.size());
    assertEquals(2, pathCosts.get(0).size());
    assertEquals(2, pathCosts.get(1).size());
    assertEquals(0, pathCosts.get(0).get(0));
    assertEquals(Integer.MAX_VALUE, pathCosts.get(0).get(1));
    assertEquals(Integer.MAX_VALUE, pathCosts.get(1).get(0));
    assertEquals(0, pathCosts.get(1).get(1));
  }
  
  @Test
  void floydWarshallTwoVertexWithPath() {
    Graph g = new AdjList(2);
    g.addEdge(0, 1);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 33);
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    assertEquals(2, pathCosts.size());
    assertEquals(0, pathCosts.get(0).get(0));
    assertEquals(33, pathCosts.get(0).get(1));
    assertEquals(Integer.MAX_VALUE, pathCosts.get(1).get(0));
    assertEquals(0, pathCosts.get(1).get(1));
  }

  @Test
  void floydWarshallPositiveThreeVertexShortcut() {
    Graph g = new AdjList(3);
    g.addEdge(0, 1);
    g.addEdge(1, 2);
    g.addEdge(0, 2);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(1, 2, 3);
    l.addLabel(0, 2, 2);
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    int M = Integer.MAX_VALUE;
    assertEquals(3, pathCosts.size());
    assertEquals(List.of(0, 1, 2), pathCosts.get(0));
    assertEquals(List.of(M, 0, 3), pathCosts.get(1));
    assertEquals(List.of(M, M, 0), pathCosts.get(2));
  }
  
  @Test
  void floydWarshallPositiveFourVertex() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(2, 0);
    g.addEdge(3, 2);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(0, 3, 4);
    l.addLabel(1, 2, 6);
    l.addLabel(1, 3, 2);
    l.addLabel(2, 0, 1);    
    l.addLabel(3, 2, 3);    
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    assertEquals(4, pathCosts.size());
    assertEquals(List.of(0, 1, 6, 3), pathCosts.get(0));
    assertEquals(List.of(6, 0, 5, 2), pathCosts.get(1));
    assertEquals(List.of(1, 2, 0, 4), pathCosts.get(2));
    assertEquals(List.of(4, 5, 3, 0), pathCosts.get(3));
  }
  
  @Test
  void floydWarshallNegativeFourVertex() {
    Graph g = new AdjList(4);
    g.addEdge(0, 1);
    g.addEdge(0, 2);
    g.addEdge(1, 3);
    g.addEdge(2, 3);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(0, 2, 3);
    l.addLabel(1, 3, 2);
    l.addLabel(2, 3, -4);
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    int M = Integer.MAX_VALUE;
    assertEquals(4, pathCosts.size());
    assertEquals(List.of(0, 1, 3, -1), pathCosts.get(0));
    assertEquals(List.of(M, 0, M, 2), pathCosts.get(1));
    assertEquals(List.of(M, M, 0, -4), pathCosts.get(2));
    assertEquals(List.of(M, M, M, 0), pathCosts.get(3));
  }

  @Test
  void floydWarshallNegativeFiveVertex() {
    Graph g = new AdjList(5);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(1, 3);
    g.addEdge(1, 4);
    g.addEdge(2, 1);
    g.addEdge(3, 2);
    g.addEdge(3, 4);
    g.addEdge(4, 2);    
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 6);
    l.addLabel(0, 3, 7);
    l.addLabel(1, 2, 5);
    l.addLabel(1, 3, 8);
    l.addLabel(1, 4, -4);
    l.addLabel(2, 1, -2);
    l.addLabel(3, 2, -3);
    l.addLabel(3, 4, 9);
    l.addLabel(4, 2, 7);    
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    int M = Integer.MAX_VALUE;
    assertEquals(5, pathCosts.size());
    assertEquals(List.of(0, 2, 4, 7, -2), pathCosts.get(0));
    assertEquals(List.of(M, 0, 3, 8, -4), pathCosts.get(1));
    assertEquals(List.of(M, -2, 0, 6, -6), pathCosts.get(2));
    assertEquals(List.of(M, -5, -3, 0, -9), pathCosts.get(3));
    assertEquals(List.of(M, 5, 7, 13, 0), pathCosts.get(4));    
  }

  @Test
  void floydWarshallNegativeCycle() {
    Graph g = new AdjList(5);
    g.addEdge(0, 1);
    g.addEdge(0, 3);
    g.addEdge(1, 2);
    g.addEdge(2, 4);
    g.addEdge(3, 1);
    g.addEdge(4, 3);
    EdgeLabeling<Integer> l = new EdgeLabeling<>(g);
    l.addLabel(0, 1, 1);
    l.addLabel(0, 3, 1);
    l.addLabel(1, 2, 2);
    l.addLabel(2, 4, 3);
    l.addLabel(3, 1, -10);
    l.addLabel(4, 3, 4);
    List<List<Integer>> pathCosts = GraphAlgorithms.floydWarshall(g, l);
    assertEquals(0, pathCosts.size());
  }

}
