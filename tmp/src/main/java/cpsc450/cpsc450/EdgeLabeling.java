/**
 * CPSC 450, HW-3
 *
 * NAME: Henry Stone
 * DATE: Fall 2024
 */

package cpsc450;

import java.util.HashMap;
import java.util.Optional;


/**
 * An edge labeling for a given graph, where labels can be of any
 * type and a graph can have many edge labelings.
 */
public class EdgeLabeling<T> {

  private Graph graph;                                  // the graph to be labeled
  private HashMap<Integer,HashMap<Integer,T>> labels;   // the graph edge labels

  /**
   * Create an edge labeling for the given graph.
   * @param graph The graph to label.
   */ 
  public EdgeLabeling(Graph graph) {
    this.graph = graph;
    this.labels = new HashMap<>();
  }

  /**
   * Check to see if a label exists on an edge. Only returns true if
   * underlying graph still has the given edge. 
   * @param x The start (from) vertex of the edge.
   * @param y The end (to) vertex of the edge.
   * @returns True if the edge has a label, false otherwise.
   */
  public boolean hasLabel(int x, int y) {
    if (!this.graph.hasEdge(x, y)){
      this.removeLabel(x, y);
      return false;
    }
    return labels.containsKey(x) && labels.get(x).containsKey(y);
  }
  
  /**
   * Add or overwrite an edge label. The edge must exist in the
   * underlying graph.
   * @param x The start (from) vertex of the edge.
   * @param y The end (to) vertex of the edge.
   */ 
  public void addLabel(int x, int y, T label) {
    if (!this.graph.hasEdge(x, y)) return;

    if(!labels.containsKey(x)){
      labels.put(x, new HashMap<>());
    }

    labels.get(x).put(y, label);
  }

  /**
   * Remove an edge label if it exists. 
   * @param x The start (from) vertex of the edge.
   * @param y The end (to) vertex of the edge.
   */ 
  public void removeLabel(int x, int y) {
    if (labels.containsKey(x)) {
      labels.get(x).remove(y);
      if (labels.get(x).isEmpty()) {
          labels.remove(x);
      }
  }
  }

  /**
   * Returns the label for the given edge, if it exists. 
   * @param x The start (from) vertex of the edge.
   * @param y The end (to) vertex of the edge.
   * @returns The label or empty as an optional value.
   */ 
  public Optional<T> getLabel(int x, int y){
    if (!graph.hasEdge(x, y)) return Optional.empty();
    
    if (labels.containsKey(x) && labels.get(x).containsKey(y)) {
        return Optional.of(labels.get(x).get(y));
    }
    
    return Optional.empty();
  }

}
