package simbigraph.core;

import edu.uci.ics.jung.graph.Graph;
import simbigraph.grid.model.Grid;

public class Context {
private static Grid grid;
private static Graph graph;

public static Grid getGrid() {
	return grid;
}
public static void setGrid(Grid grid) {
	Context.grid = grid;
}
public static Graph getGraph() {
	return graph;
}
public static void setGraph(Graph graph) {
	Context.graph = graph;
}
}
