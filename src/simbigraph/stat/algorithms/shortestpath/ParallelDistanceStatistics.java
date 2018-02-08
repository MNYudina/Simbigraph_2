package simbigraph.stat.algorithms.shortestpath;

import java.util.Collection;
import java.util.function.Function;
import edu.uci.ics.jung.graph.Hypergraph;

/**
 * This is parallel version of
 * edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics class.
 * 
 * @see edu.uci.ics.jung.algorithms.shortestpath.DistanceStatistics
 * @author Yudin Evgeniy
 */
public class ParallelDistanceStatistics<V, E> {
	Collection<V> vertices;
	Object[] eccs;

	/**
	 * Saves eccentricities of the <code>g</code> vertices into
	 * <code>eccs</code> array. The eccentricity is defined to be the maximum,
	 * over all pairs of vertices <code>u,v</code>, of the length of the
	 * shortest path from <code>u</code> to <code>v</code>. If the graph is
	 * disconnected (that is, some vertex is not reachable from another
	 * vertices), the value associated with the vertex into <code>eccs</code>
	 * array will be <code>Double.POSITIVE_INFINITY</code>.
	 * 
	 * This method uses Function and Parallel Stream features of JDK 1.8 for
	 * parallel execution.
	 */
	public void getEccentricities(Hypergraph<V, E> g,
			ParallelUnweightedShortestPath<V, E> d) {
		vertices = g.getVertices();
		Function<V, Double> f_inner = (v) -> {
			int di = d.getEccentricity(v);
			if (di == Integer.MAX_VALUE)
				return Double.POSITIVE_INFINITY;
			else
				return (double) di;
		};
		eccs = vertices.parallelStream().map(f_inner).sorted().toArray();
	}

	/**
	 * Returns the diameter of <code>g</code>, ignoring edge weights. The
	 * diameter is defined to be the maximum, over all pairs of vertices
	 * <code>u,v</code>, of the length of the shortest path from <code>u</code>
	 * to <code>v</code>, or <code>Double.POSITIVE_INFINITY</code> if any of
	 * these distances do not exist.
	 * 
	 * @see #getEccentricities(Hypergraph, ParallelUnweightedShortestPath)
	 */
	public double diameter(Hypergraph<V, E> g) {
		if (eccs == null)
			getEccentricities(g, new ParallelUnweightedShortestPath<V, E>(g));
		return (Double) eccs[eccs.length - 1];
	}

	/**
	 * Returns the radius of <code>g</code>, ignoring edge weights. The radius
	 * is defined to be the minimum, over all pairs of vertices <code>u,v</code>
	 * , of the length of the shortest path from <code>u</code> to
	 * <code>v</code>, or <code>Double.POSITIVE_INFINITY</code> if any of these
	 * distances do not exist.
	 * 
	 * @see #getEccentricities(Hypergraph, ParallelUnweightedShortestPath)
	 */
	public double radius(Hypergraph<V, E> g) {
		if (eccs == null)
			getEccentricities(g, new ParallelUnweightedShortestPath<V, E>(g));
		return (Double) eccs[0];
	}
}