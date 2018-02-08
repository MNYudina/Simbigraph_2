package simbigraph.stat.algorithms.shortestpath;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.PriorityBlockingQueue;
import javafx.util.Pair;
import edu.uci.ics.jung.graph.Hypergraph;

/**
 * This is parallel version of
 * edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath class.
 * 
 * @see edu.uci.ics.jung.algorithms.shortestpath.UnweightedShortestPath
 * @author  * @author Yudin Evgeniy
 */
public class ParallelUnweightedShortestPath<V, E> {
	private Hypergraph<V, E> mGraph;

	/**
	 * Constructs and initializes algorithm
	 * 
	 * @param g
	 *            the graph
	 */
	public ParallelUnweightedShortestPath(Hypergraph<V, E> g) {
		mGraph = g;
	}

	/**
	 * Computes the shortest path distances from a given node to all other
	 * nodes.
	 * 
	 * @param source
	 *            the source node
	 */
	public int getEccentricity(V source) {
		int ecc = 0;
		Map<V, Integer> distances = new HashMap<V, Integer>();
		Collection<V> vertices = mGraph.getVertices();
		for (V v : vertices) {
			distances.put(v, Integer.MAX_VALUE);
		}
		distances.replace(source, 0);
		PriorityBlockingQueue<Pair<Integer, V>> q = new PriorityBlockingQueue<Pair<Integer, V>>(
				100, new Comparator<Pair<Integer, V>>() {
					@Override
					public int compare(Pair<Integer, V> o1, Pair<Integer, V> o2) {
						return (o1.getKey() > o2.getKey() ? -1 : 1);
					}
				});
		q.add(new Pair<Integer, V>(0, source));
		while (!q.isEmpty()) {
			V v = q.peek().getValue();
			int cur_d = -q.peek().getKey();
			q.poll();
			if (cur_d > distances.get(v).intValue())
				continue;
			for (V neighbor : mGraph.getNeighbors(v)) {
				if (distances.get(v).intValue() + 1 < distances.get(neighbor)
						.intValue()) {
					distances
							.replace(neighbor, distances.get(v).intValue() + 1);
					q.add(new Pair<Integer, V>(
							-(distances.get(v).intValue() + 1), neighbor));
				}
			}
			if (distances.get(v).intValue() > ecc)
				ecc = distances.get(v).intValue();
		}
		return ecc;
	}
}
