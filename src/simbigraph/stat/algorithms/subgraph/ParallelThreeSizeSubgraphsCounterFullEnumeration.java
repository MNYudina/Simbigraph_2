package simbigraph.stat.algorithms.subgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.stat.algorithms.subgraph.ParallelDirThreeSizeSubgraphsCounterSampling.VertexLayerParameters;
import simbigraph.stat.exception.UnsupportedEdgeTypeException;

/**
 * This is parallel version of 3-size undirected subgraphs counter which uses
 * full enumeration algorithm.
 * 
  * @author Yudin Evgeniy

 */
public class ParallelThreeSizeSubgraphsCounterFullEnumeration<V, E>  {

	private Graph<V, E> graph;

	private int numberOfThreads;

	private int numberOfForks, numberOfTriangles;

	/**
	 * Constructs and initializes the class.
	 *
	 * @author Yudin Evgeniy

	 * @param graph
	 *            the graph
	 * @param numberOfThreads
	 *            number of parallel threads
	 */
	public ParallelThreeSizeSubgraphsCounterFullEnumeration(Graph<V, E> graph, int numberOfThreads) {
		this.graph = graph;
		this.numberOfThreads = numberOfThreads;
	}

	/**
	 * Saves exact number of the <code>graph</code>'s "triangles" into
	 * <code>numberOfTriangles</code> variable.<br>
	 * Saves exact number of the <code>graph</code>'s "forks" into
	 * <code>numberOfForks</code> variable.<br>
	 * If the <code>graph</code> includes directed edges then
	 * <code>simbigraph.stat.exception.UnsupportedEdgeTypeException</code>
	 * is thrown.
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
	 * @author Yudin Evgeniy

	 * @throws UnsupportedEdgeTypeException
	 */
	public void execute()  {
		DirThreeSizeSubgraphsCounterFullEnumeration<V, E> counter = new DirThreeSizeSubgraphsCounterFullEnumeration<>(graph);
		Collection<V> vertices = graph.getVertices();

		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		
		if (false) {
			doOriented();
			return;
			// throw new UnsupportedEdgeTypeException("The parallel version of
			// 3-size subgraphs counter which uses full enumeration algorithm
			// does not work with " + graph.getDefaultEdgeType() + " graph.");
		}
		
		try {
			forkJoinPool.submit(() -> numberOfTriangles = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getNumberOfTriangles(vertex)).sum() / 3).get();
			forkJoinPool.submit(() -> numberOfForks = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getNumberOfForks(vertex)).sum() - numberOfTriangles * 3).get();
		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		

	}

	/**
	 * @author Yudin Evgeniy

	 */
	@Override
	public String toString() {
		int numberOfSubgraphs = numberOfForks + numberOfTriangles;
		return String.format("Number of forks = %d(%.3f%%). Number of triangles = %d(%.3f%%).", numberOfForks,
				(double) numberOfForks / numberOfSubgraphs * 100.0, numberOfTriangles,
				(double) numberOfTriangles / numberOfSubgraphs * 100.0);
	}

	/**
	 * Find oriented 3-subgraphs author Yudin Evgeniy
	 */
	private void doOriented() {

		DirThreeSizeSubgraphsCounterFullEnumeration<V, E> counter = new DirThreeSizeSubgraphsCounterFullEnumeration<>(graph);
		Collection<V> vertices = graph.getVertices();

		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_1 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_1(vertex)).sum()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_2 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_2(vertex)).sum()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_3 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_3(vertex)).sum()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_4 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_4(vertex)).sum()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_5 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_5(vertex)).sum()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_6 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_6(vertex)).sum()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_7 = vertices.stream().parallel()
					.mapToInt(vertex -> counter.getSubgraphs3_7(vertex)).sum()).get();

			

		} catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}

		System.out.println("" + exploredNumberOfSubgraphs3_1);
		System.out.println("" + exploredNumberOfSubgraphs3_2);
		System.out.println("" + exploredNumberOfSubgraphs3_3);
		System.out.println("" + exploredNumberOfSubgraphs3_4);
		System.out.println("" + exploredNumberOfSubgraphs3_5);
		System.out.println("" + exploredNumberOfSubgraphs3_6);
		System.out.println("" + exploredNumberOfSubgraphs3_7);
		System.out.println("" + exploredNumberOfSubgraphs3_8);
		System.out.println("" + exploredNumberOfSubgraphs3_9);
		System.out.println("" + exploredNumberOfSubgraphs3_10);
		System.out.println("" + exploredNumberOfSubgraphs3_11);
		System.out.println("" + exploredNumberOfSubgraphs3_12);
		System.out.println("" + exploredNumberOfSubgraphs3_13);

		System.out.println("----------------------------------------");
		System.out.println("C="+counter.getNumberOfTriangles2());
		counter.searchOrientedTypesOfSubgraphs();

	

	}

	List<Integer> resultsList;

	private long exploredNumberOfSubgraphs3_1, exploredNumberOfSubgraphs3_2, exploredNumberOfSubgraphs3_3,
			exploredNumberOfSubgraphs3_4, exploredNumberOfSubgraphs3_5, exploredNumberOfSubgraphs3_6,
			exploredNumberOfSubgraphs3_7, exploredNumberOfSubgraphs3_8, exploredNumberOfSubgraphs3_9,
			exploredNumberOfSubgraphs3_10, exploredNumberOfSubgraphs3_11, exploredNumberOfSubgraphs3_12,
			exploredNumberOfSubgraphs3_13;

}