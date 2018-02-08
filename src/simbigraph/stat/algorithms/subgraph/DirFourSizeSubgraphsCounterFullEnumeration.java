package simbigraph.stat.algorithms.subgraph;

import java.util.LinkedList;
import java.util.List;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;

/**
 * @author Yudina Maria, Yudin Evgeniy
 */
public class DirFourSizeSubgraphsCounterFullEnumeration<V, E> {

	private Graph<V, E> graph;

	int massMotif[] = new int[220];

	/**
	 * Constructs and initializes the class.
	 *
     * @author Yudina Maria, Yudin Evgeniy
	 * @param graph
	 *            the graph
	 */
	public DirFourSizeSubgraphsCounterFullEnumeration(Hypergraph<V, E> graph) {
		this.graph = (Graph<V, E>) graph;
	}

	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_2 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @param edge
	 *            of the graph
	 * @return number of subgraphs4_2, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_2(E edge) {
		int numberOfSubgraphs4_2 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if (!graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4)) {
						numberOfSubgraphs4_2++;
					}
				}
			}
		}
		return numberOfSubgraphs4_2;
	}

	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_3 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @param edge
	 *            of the graph
	 * @return number of subgraphs4_3, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_3(E edge) {
		int numberOfSubgraphs4_3 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if ((graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4))
							|| (!graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4))) {
						numberOfSubgraphs4_3++;
					}
				}
			}
		}
		return numberOfSubgraphs4_3;
	}

	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_4 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @param edge
	 *            of the graph
	 * @return number of subgraphs4_4, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_4(E edge) {
		int numberOfSubgraphs4_4 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if (!graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) {
						numberOfSubgraphs4_4++;
					}
				}
			}
		}
		return numberOfSubgraphs4_4;
	}

	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_5 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @param edge
	 *            of the graph
	 * @return number of subgraphs4_5, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_5(E edge) {
		int numberOfSubgraphs4_5 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if ((graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && !graph.isNeighbor(v3, v4))
							|| (graph.isNeighbor(v1, v4) && !graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4))
							|| (!graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4))) {
						numberOfSubgraphs4_5++;
					}
				}
			}
		}
		return numberOfSubgraphs4_5;
	}

	/**
	 * Calculates number of <code>graph</code>'s subgraphs4_6 by testing
	 * connectivity of <code>edge</code>'s endpoints with all pair of vertices
	 * from the list of vertices, which incident to the <code>edge</code>.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @param edge
	 *            of the graph
	 * @return number of subgraphs4_6, which included the <code>edge</code>
	 */
	public int getNumberOfSubgraphs4_6(E edge) {
		int numberOfSubgraphs4_6 = 0;
		V v1 = graph.getEndpoints(edge).getFirst();
		V v2 = graph.getEndpoints(edge).getSecond();
		List<V> successors1 = new LinkedList<V>(graph.getSuccessors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getSuccessors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		for (V v3 : successors1) {
			for (V v4 : successors2) {
				if (v3 != v4) {
					if (graph.isNeighbor(v1, v4) && graph.isNeighbor(v2, v3) && graph.isNeighbor(v3, v4)) {
						numberOfSubgraphs4_6++;
					}
				}
			}
		}
		return numberOfSubgraphs4_6;
	}

	public int searchOtherTypesOf4SubgraphsLapka(V v1) {

		// Choose 3 successors of the vertex randomly
		List<V> v1List = new LinkedList<>(graph.getNeighbors(v1));
		/*
		 * int randomIntValue = randomGenerator.nextInt(v1List.size()); V v2 =
		 * v1List.remove(randomIntValue); randomIntValue =
		 * randomGenerator.nextInt(v1List.size()); V v3 =
		 * v1List.remove(randomIntValue); randomIntValue =
		 * randomGenerator.nextInt(v1List.size()); V v4 =
		 * v1List.remove(randomIntValue);
		 */

		for (V v2 : v1List) {
			for (V v3 : v1List) {
				for (V v4 : v1List) {
					if (!graph.getNeighbors(v2).contains(v3) && !graph.getNeighbors(v2).contains(v4)
							&& !graph.getNeighbors(v3).contains(v4)) {

						V[] vert = (V[]) new Object[4];
						vert[0] = v1;
						vert[1] = v2;
						vert[2] = v3;
						vert[3] = v4;
						int code = 0;
						for (int i = 0; i < vert.length - 1; i++) {
							for (int j = i + 1; j < vert.length; j++) {
								E o1 = graph.findEdge(vert[i], vert[j]);
								if (o1 != null)
									code |= DirFourSizeSubgraphsCounterSampling.arr_idx[4 * i + j];
								E o2 = graph.findEdge(vert[j], vert[i]);
								if (o2 != null)
									code |= DirFourSizeSubgraphsCounterSampling.arr_idx[4 * j + i];
							}
						}
						int ee = DirFourSizeSubgraphsCounterSampling.arrcode[code];
						massMotif[ee] = massMotif[ee] + 1;

					}
				}
			}
		}

		return 0;
	}

	public int searchOtherTypesOf4SubgraphsScobka(E selectedEdge) {

		// Get endpoints of the edge
		V v1 = graph.getEndpoints(selectedEdge).getFirst();
		V v2 = graph.getEndpoints(selectedEdge).getSecond();

		// Generate a list of successors of the endpoints
		List<V> successors1 = new LinkedList<V>(graph.getNeighbors(v1));
		List<V> successors2 = new LinkedList<V>(graph.getNeighbors(v2));
		successors1.remove(v2);
		successors2.remove(v1);

		// Choose 2 successors of the endpoints randomly
		// V v3 = successors1.get(randomGenerator.nextInt(successors1.size()));
		// V v4 = successors2.get(randomGenerator.nextInt(successors2.size()));

		for (V v3 : successors1) {
			for (V v4 : successors2) {
				V[] vert = (V[]) new Object[4];
				vert[0] = v1;
				vert[1] = v2;
				vert[2] = v3;
				vert[3] = v4;
				int code = 0;
				for (int i = 0; i < vert.length - 1; i++) {
					for (int j = i + 1; j < vert.length; j++) {
						E o1 = graph.findEdge(vert[i], vert[j]);
						if (o1 != null)
							code |= DirFourSizeSubgraphsCounterSampling.arr_idx[4 * i + j];
						E o2 = graph.findEdge(vert[j], vert[i]);
						if (o2 != null)
							code |= DirFourSizeSubgraphsCounterSampling.arr_idx[4 * j + i];
						int ee = DirFourSizeSubgraphsCounterSampling.arrcode[code];
						massMotif[ee] = massMotif[ee] + 1;
					}
				}
			}
		}

		return 5;// arrcode[code];
		// return 3;
	}
}