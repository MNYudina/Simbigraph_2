/*package simbigraph.graphs.prefAttachment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;

*//**
 * Поддерживает генерацию графа Павлова
 * 
 * @author Yudin
 * 
 *//*
public class GenConfModelBA <V, E> {

	protected Factory<Graph<V, E>> graphFactory;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;

	private Graph<V, E> graph;
	private Random mRand;
	private int m;

	*//**
	 * 
	 * @param m
	 *            Параметр, определяющий распредления связности вершин
	 *//*
	public GenConfModelBA(Factory<Graph<V, E>> graphFactory, Factory<V> vertexFactory,
			Factory<E> edgeFactory, int numEdgesParameter) {

		assert numEdgesParameter > 0 : "Number of edges to attach "
				+ "at each time step must be positive";

		this.graphFactory = graphFactory;
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
		m = numEdgesParameter;
		mRand = new Random();
	}
//Конфигурационная модель со случайным связыванием
	public Graph<V,E> getBara2(int step) {
		graph = graphFactory.create();
		//Map for matching Vertex's degree (number of incident edges) 
		Map<V,Integer> mapNodeDegree = new HashMap<V,Integer> ();
		//list use for fast getting vertexes by its index, not use mapNodeDegree for this
		List<V> list = new ArrayList<V>();
		int c = 1;

		int tr = 0;
		// генерирую список означенных степенями вершин
		for (int i = 0; i < step; i++) {
			int degree = getK(m);
			V node = vertexFactory.create();
			mapNodeDegree.put(node, degree);
			list.add(node);
			graph.addVertex(node);
		}

		//соединяю вершины случайно выбирая две из них
		int edgecount = 0;
		out: while (true) {
			if (mapNodeDegree.size() <= 1)
				break;
			V first, second;
			first = list.get(mRand.nextInt(list.size()));
			int c4 = 0;
			do {
				second = list.get(mRand.nextInt(list.size()));
				c4++;
				if (c4 > list.size() * 2)
					break out;
			} while (first == second || (graph.isNeighbor(first, second)));

			int k1 = mapNodeDegree.get(first)-1;
			int k2 = mapNodeDegree.get(second) - 1;
			
			mapNodeDegree.put(first, k1);
			mapNodeDegree.put(second, k2);
			
			graph.addEdge(edgeFactory.create(), first, second);
			if (k1 == 0)
				list.remove(first);
			if (k2 == 0)
				list.remove(second);
		}
		return graph;
	}

	private int getK(int m) {
		int k = m;
		double rand = mRand.nextDouble();
		double tr = 0;
		while (true) {
			tr = tr + m * (2 * (m + 1)) / (1.0 * k * (k + 1) * (k + 2));
			if (rand < tr)
				break;
			k++;
		}
		return k;
	}
}
*/