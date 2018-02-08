package simbigraph.stat.algorithms.subgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.stat.algorithms.subgraph.ParallelDirThreeSizeSubgraphsCounterSampling.VertexLayerParameters;

/**
   * @author Yudin Evgeniy

 */
public class DirThreeSizeSubgraphsCounterFullEnumeration<V, E> {

	private Graph<V, E> graph;

	/**
	 * Constructs and initializes the class.
	 *
     * @author Yudina Maria, Yudin Evgeniy
	 * @param graph
	 *            the graph
	 */
	public DirThreeSizeSubgraphsCounterFullEnumeration(Graph<V, E> graph) {
		this.graph = graph;
	}

	/**
	 * Calculates number of <code>vertex</code>'s "forks" using the formula for
	 * calculating the number of 2-combinations from number of the successors of
	 * the <code>vertex</code> in the <code>graph</code>.
	 *
     * @author Yudina Maria, Yudin Evgeniy
	 * @param vertex
	 *            of the graph
	 * @return number of "forks", that are rooted by the <code>vertex</code>
	 */
	public int getNumberOfForks(V vertex) {
		int numberOfSuccessors = graph.getSuccessors(vertex).size();
		int numberOfForks = (numberOfSuccessors * (numberOfSuccessors - 1) / 2);
		return numberOfForks;
	}

	/**
	 * Calculates number of <code>vertex</code>'s "triangles" by testing
	 * connectivity of each pair of vertices from <code>vertex</code>'s
	 * successors list.
	 *
     * @author Yudina Maria, Yudin Evgeniy
	 * @param vertex
	 *            of the graph
	 * @return number of "triangles", that are included the <code>vertex</code>
	 */
	public int getNumberOfTriangles(V vertex) {
		List<V> successors = new ArrayList<>(graph.getSuccessors(vertex));
		int numberOfTriangles = 0;
		for (int i = 0; i < successors.size(); i++) {
			for (int j = i + 1; j < successors.size(); j++) {
				if (graph.getSuccessors(successors.get(i)).contains(successors.get(j))) {
					numberOfTriangles++;
				}
			}
		}
		return numberOfTriangles;
	}

	public int getSubgraphs3_1(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int ubgraphs3_1 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if (graph.getEdgeType(e1) == EdgeType.UNDIRECTED && graph.getEdgeType(e4) == EdgeType.UNDIRECTED
						&& e5 == null && e6 == null)
					ubgraphs3_1++;

			}
		}
		return ubgraphs3_1;
	}

	public int getSubgraphs3_2(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int subgraphs3_2 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if (e1 != null && e2 == null && graph.getEdgeType(e4) == EdgeType.UNDIRECTED && e5 == null && e6 == null
						|| e3 != null && e4 == null && graph.getEdgeType(e1) == EdgeType.UNDIRECTED && e5 == null
								&& e6 == null)
					subgraphs3_2++;

			}
		}
		return subgraphs3_2;
	}

	public int getSubgraphs3_3(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int subgraphs3_3 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if (e1 != null && e2 == null && e3 != null && e4 == null && e6 == null && e5 == null)
					subgraphs3_3++;

			}
		}
		return subgraphs3_3;
	}

	public int getSubgraphs3_4(V v1) {
		Set<V> neibs1 = new HashSet<>(graph.getSuccessors(v1));
		neibs1.addAll(graph.getPredecessors(v1));
		List<V> neibs=new ArrayList(neibs1);
		int subgraphs3_4 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if ((e1 == null && e2 != null && (e3==e4) && e5 == null && e6 == null
						)|| (e3 == null && e4 != null && e1==e2 && e5 == null
								&& e6 == null))
					subgraphs3_4++;

			}
		}
		return subgraphs3_4;
	}

	public int getSubgraphs3_5(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int subgraphs3_5 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if (e1 == null && e2 != null && e3 == null && e4 != null && e6 == null && e5 == null)
					subgraphs3_5++;

			}
		}
		return subgraphs3_5;
	}
	public int getSubgraphs3_6(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int subgraphs3_6 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if (e1 ==null&&e2!=null  && e3 !=null && e4==null && e6 == null&&e5==null)
					subgraphs3_6++;

			}
		}
		return subgraphs3_6;
	}

	public int getSubgraphs3_7(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int subgraphs3_7 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if (e1 ==e2  && e3 == e4 && e6 == e5)
					subgraphs3_7++;

			}
		}
		return subgraphs3_7;
	}
	
	public int getSubgraphs3_8(V v1) {
		List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
		int subgraphs3_8 = 0;
		for (int i = 0; i < neibs.size(); i++) {
			for (int j = i + 1; j < neibs.size(); j++) {
				V v2 = neibs.get(i);
				V v3 = neibs.get(j);
				E e1 = graph.findEdge(v1, v2);
				E e2 = graph.findEdge(v2, v1);
				E e3 = graph.findEdge(v1, v3);
				E e4 = graph.findEdge(v3, v1);
				E e5 = graph.findEdge(v2, v3);
				E e6 = graph.findEdge(v3, v2);
				if ((e1 != null && e2 == null && e3 == e4  && e6 == null && e5 != null)
						||(e1 != null && e2 == null && e3==null&&  e4!=null  && e6 == e5)
						||(e1 == e2 && e3==null&&  e4!=null  && e6==null&& e5!=null))
					subgraphs3_8++;

			}
		}
		return subgraphs3_8;
	}

	public int searchOrientedTypesOfSubgraphs() {
		int c1 = 0;
		int c2 = 0;
		int c3 = 0;
		int c4 = 0;
		int c5 = 0;
		int c6 = 0;
		int c7 = 0;
		int c8 = 0;
		int c9 = 0;
		int c10 = 0;
		int c11 = 0;
		int c12 = 0;
		int c13 = 0;
		for (V v1 : graph.getVertices()) {

			List<V> neibs = new ArrayList<>(graph.getNeighbors(v1));
			for (int ii = 0; ii < neibs.size(); ii++) {
				for (int jj = ii + 1; jj < neibs.size(); jj++) {

					V v2 = neibs.get(ii);
					V v3 = neibs.get(jj);

					E i = graph.findEdge(v1, v2);
					if (i == null)
						i = graph.findEdge(v2, v1);
					E j = graph.findEdge(v1, v3);
					if (j == null)
						j = graph.findEdge(v3, v1);

					// choose pattern
					if ((graph.getEdgeType(j).equals(EdgeType.UNDIRECTED))
							&& (graph.getEdgeType(i).equals(EdgeType.UNDIRECTED))) // это
																					// ребра
					{
						if (!graph.isNeighbor(v2, v3))
							c1++;

						else {
							E o = graph.findEdge(v2, v3);
							if (o == null)
								o = graph.findEdge(v3, v2);
							if (graph.getEdgeType(o).equals(EdgeType.UNDIRECTED))
								c7++;
							else
								c10++;

						}
					} else if ((graph.getEdgeType(j).equals(EdgeType.UNDIRECTED))// одна
																					// из
																					// связей
																					// -
																					// ребро
							|| (graph.getEdgeType(i).equals(EdgeType.UNDIRECTED))) {
						E l = null, e = null;
						// определяю где ребро, а где дуга
						if (graph.getEdgeType(j).equals(EdgeType.UNDIRECTED)) {
							e = j;
							l = i;
						} else {
							e = i;
							l = j;
						}

						V end_l = graph.getOpposite(v1, l);
						V end_e = graph.getOpposite(v1, e);

						Object dest = graph.getDest(l);
						if (dest == end_l) {
							if (!graph.isNeighbor(v2, v3))
								c2++;
							else {
								E o1 = graph.findEdge(end_l, end_e);
								if (o1 == null)
									o1 = graph.findEdge(end_e, end_l);

								if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED)) {
									V dest2 = graph.getDest(o1);
									if (dest2 == end_l)
										c12++;
									else
										c8++;

								} else
									c10++;
							}
						} else {
							if (!graph.isNeighbor(v2, v3))
								c4++;
							else {
								E o1 = graph.findEdge(end_l, end_e);
								if (o1 == null)
									o1 = graph.findEdge(end_e, end_l);

								if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED)) {
									Object dest2 = graph.getDest(o1);
									if (dest2 == end_l)
										c8++;
									else
										c9++;

								} else
									c10++;
							}
							// если основа - дуги
						}
					}
					else if ((graph.getSource(i) == v1) && (graph.getSource(j) == v1)) {// обе
																					// дуги

						if (!graph.isNeighbor(v2, v3))
							c3++;
						else {
							E o1 = graph.findEdge(v2, v3);
							if (o1 == null)
								o1 = graph.findEdge(v3, v2);
							if (graph.getEdgeType(o1).equals(EdgeType.UNDIRECTED))
								c9++;
							else
								c11++;
						}
					}

					else if ((graph.getDest(i) == v1) && (graph.getDest(j) == v1)) {
						if (!graph.isNeighbor(v2, v3))
							c5++;
						else {
							E o1 = graph.findEdge(v2, v3);
							if (o1 == null)
								o1 = graph.findEdge(v3, v2);
							if (graph.getEdgeType(o1).equals(EdgeType.UNDIRECTED))
								c12++;
							else
								c11++;
						}

					} else {
						E in = null, out = null;
						// определяю где входящая, а где исходящая дуга
						if (graph.getSource(i) == v1) {
							out = i;
							in = j;
						} else {
							out = j;
							in = i;
						}

						V end_in = graph.getOpposite(v1, in);
						V end_out = graph.getOpposite(v1, out);
						if (!graph.isNeighbor(v2, v3))
							c6++;
						else {
							E o1 = graph.findEdge(end_in, end_out);
							if (o1 == null)
								o1 = graph.findEdge(end_out, end_in);

							if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED)) {
								Object dest2 = graph.getDest(o1);
								if (dest2 == end_in)
									c13++;
								else
									c11++;

							} else
								c8++;
						}

					}

					// System.out.println("fff");
					// return 0; //Этого произойти не должно
				}
			}
		}
		System.out.println("-------------------");
		System.out.println("" + c1);
		System.out.println("" + c2);
		System.out.println("" + c3);
		System.out.println("" + c4);
		System.out.println("" + c5);
		System.out.println("" + c6);
		System.out.println("" + c7/3.);
		System.out.println("" + c8/3.);
		System.out.println("" + c9/3.);
		System.out.println("" + c10/3.);
		System.out.println("" + c11/3.);
		System.out.println("" + c12/3.);
		System.out.println("" + c13/3.);// return 0;
		return 0;
	}

	public int getNumberOfTriangles2() {
		int numberOfTriangles = 0;

		for (V vertex : graph.getVertices()) {

			List<V> successors = new ArrayList<>(graph.getNeighbors(vertex));
			for (int i = 0; i < successors.size(); i++) {
				for (int j = i + 1; j < successors.size(); j++) {
					if (graph.getNeighbors(successors.get(i)).contains(successors.get(j))) {
						numberOfTriangles++;
					}
				}
			}
		}
		return numberOfTriangles;
	}

}