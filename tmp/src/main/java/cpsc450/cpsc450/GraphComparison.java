/**
 * CPSC 450, HW-4
 * 
 * NAME: S. Bowers
 * DATE: Fall 2024
 */ 

package cpsc450;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import java.io.File;
import java.awt.Font;
import java.awt.Color;
import java.awt.BasicStroke;
import static java.lang.System.out;
import java.util.Map;
import java.util.*;
import java.io.*;

import org.jfree.data.xy.XYSeries;


public class GraphComparison {

    /**
   * Create a sparse adjacency matrix consisting of vertices of
   * connected three cycles.
   * @param n The size of the graph in terms of the number of vertices.
   * @returns The adjacency matrix.
   */
  static Graph createSparseAdjMatrix(int n) throws Exception {
    Graph graph = new AdjMatrix(n);
    // create a "forward" line graph
    for (int x = 0; x < n - 1; ++x)
      graph.addEdge(x, x + 1);
    // create a "backward" line graph for every other vertex
    for (int x = n - 1; x > 2; --x)
      graph.addEdge(x, x - 2);
    return graph;
  }

  
  /**
   * Create a sparse adjacency list consisting of vertices of
   * connected three cycles.
   * @param n The size of the graph in terms of the number of vertices.
   * @returns The adjacency list.
   */
  static Graph createSparseAdjList(int n) {
    Graph graph = new AdjList(n);
    // create a "forward" line graph
    for (int x = 0; x < n - 1; ++x) 
      graph.addEdge(x, x + 1);
    // create a "backward" line graph for every other vertex
    for (int x = n - 1; x > 2; --x)
      graph.addEdge(x, x - 2);
    return graph;
  }

  
  /**
   * Create a dense adjacency matrix with all vertices connected to
   * each without self edges. 
   * @param n The size of the graph in terms of the number of vertices.
   * @returns The adjacency matrix.
   */
  static Graph createDenseAdjMatrix(int n) {
    Graph graph = new AdjMatrix(n);
    for (int x = 0; x < n; ++x) 
      for (int y = 0; y < n; ++y)
        if (x != y)
          graph.addEdge(x, y);
    return graph;
  }

  
  /**
   * Create a dense adjacency list with all vertices connected to
   * each without self edges. 
   * @param n The size of the graph in terms of the number of vertices.
   * @returns The adjacency list.
   */
  static Graph createDenseAdjList(int n) {
    Graph graph = new AdjList(n);
    for (int x = 0; x < n; ++x) 
      for (int y = 0; y < n; ++y)
        if (x != y)
          graph.addEdge(x, y);
    return graph;
  }

  

  /**
   * Measure the execution time for Floyd-Warshall algorithm.
   * @param g The graph.
   * @param l The edge labeling.
   * @return The time taken in milliseconds.
   */
  static long timeFloydWarshall(Graph g, EdgeLabeling<Integer> l) {
    long start = System.currentTimeMillis();
    GraphAlgorithms.floydWarshall(g, l);
    long end = System.currentTimeMillis();
    return end - start;
  }

  /**
   * Measure the execution time for Johnson's algorithm.
   * @param g The graph.
   * @param l The edge labeling.
   * @return The time taken in milliseconds.
   */
  static long timeJohnsons(Graph g, EdgeLabeling<Integer> l) {
    long start = System.currentTimeMillis();
    GraphAlgorithms.johnsonsAlgorithm(g, l);
    long end = System.currentTimeMillis();
    return end - start;
  }

  /**
   * Create a chart
   */  
  static void chart(XYSeries[] series, String title, String file) throws Exception {
    XYSeriesCollection ds = new XYSeriesCollection();
    for (XYSeries s : series)
      ds.addSeries(s);
    // build the chart
    JFreeChart chart = ChartFactory.createXYLineChart(title, "vertices", "time (ms)", ds);
    XYPlot plot = (XYPlot) chart.getPlot();
    plot.setBackgroundPaint(new Color(220, 220, 220));
    // configure the chart
    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
    for (int i = 0; i < series.length; ++i) {
      renderer.setSeriesShapesVisible(i, true);
      renderer.setSeriesShapesFilled(i, true);
      renderer.setSeriesStroke(i, new BasicStroke(2.5f));
    }
    // save the result
    int width = 640; // 1024;
    int height = 480; // 768;
    File lineChart = new File(file);
    ChartUtils.saveChartAsPNG(lineChart, chart, width, height);
  }

  /**
   * Run comparative benchmarks for sparse and dense graphs.
   */
  static void compareAlgorithms() throws Exception {
    System.out.println("----------------------------------------");
    System.out.println("COMPARING JOHNSON'S AND FLOYD-WARSHALL");
    System.out.println("----------------------------------------");
    int STEP = 50;
    int END = 500;

    XYSeries johnsonSparse = new XYSeries("Johnson Sparse");
    XYSeries floydSparse = new XYSeries("Floyd-Warshall Sparse");
    XYSeries johnsonDense = new XYSeries("Johnson Dense");
    XYSeries floydDense = new XYSeries("Floyd-Warshall Dense");

    // Sparse graph comparison
    for(int n = STEP; n <= END; n += STEP){
      Graph sparseGraph = createSparseAdjList(n);
      EdgeLabeling<Integer> sparseLabels = generateRandomEdgeWeights(sparseGraph, 1, 100);

      long johnsonTime = timeJohnsons(sparseGraph, sparseLabels);
      long floydTime = timeFloydWarshall(sparseGraph, sparseLabels);

      johnsonSparse.add(n, johnsonTime);
      floydSparse.add(n, floydTime);

      System.out.println("Sparse Graph (" + n + " vertices): Johnson = " + johnsonTime + "ms, Floyd-Warshall = " + floydTime + "ms");
    }

    // Dense graph comparison
    for(int n = STEP; n <= END; n += STEP){
      Graph denseGraph = createDenseAdjList(n);
      EdgeLabeling<Integer> denseLabels = generateRandomEdgeWeights(denseGraph, 1, 100);

      long johnsonTime = timeJohnsons(denseGraph, denseLabels);
      long floydTime = timeFloydWarshall(denseGraph, denseLabels);

      johnsonDense.add(n, johnsonTime);
      floydDense.add(n, floydTime);

      System.out.println("Dense Graph (" + n + " vertices): Johnson = " + johnsonTime + "ms, Floyd-Warshall = " + floydTime + "ms");
    }

    XYSeries[] series = { johnsonSparse, floydSparse, johnsonDense, floydDense };
    String title = "Johnson's vs Floyd-Warshall Algorithm Comparison";
    chart(series, title, "algorithm_comparison.png");
  }

  /**
   * Generate random edge weights for a graph.
   * @param g The graph.
   * @param min Minimum weight.
   * @param max Maximum weight.
   * @return The edge labeling with random weights.
   */
  static EdgeLabeling<Integer> generateRandomEdgeWeights(Graph g, int min, int max) {
    EdgeLabeling<Integer> labels = new EdgeLabeling<>(g);
    Random random = new Random();
    for(int u = 0; u < g.vertices(); u++){
      for(int v : g.out(u)){
        labels.addLabel(u, v, random.nextInt(max - min + 1) + min);
      }
    }

    return labels;
  }

  public static void main(String[] args) throws Exception {
    compareAlgorithms();
  }
}
