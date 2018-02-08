package simbigraph.graphs.views;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.io.PajekNetReader;

public class Motif3 {
	private Map<Integer, List<Object>> mapVertecies = new HashMap<Integer, List<Object>>();
	private Map<Integer, Double> mapVilks = new HashMap<Integer, Double>();
	private Random mRand = new Random();
	public Graph graph;
	private Factory<Object> vertexFactory = new Factory<Object>() {
		public Object create() {
			return new Object();
		}
	};
	private Factory<Object> edgeFactory = new Factory<Object>() {
		public Object create() {
			return new Object();
		}
	};

	// загрузка графа
	private Graph getNet(String fileName) {
		// System.out.println(fileName);
		Graph graph = new SparseGraph();
		PajekNetReader<Graph<Object, Object>, Object, Object> pnr;
		try {
			pnr = new PajekNetReader<Graph<Object, Object>, Object, Object>(
					vertexFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);

		} catch (IOException e5) {
			System.out.println("IOException!!!!!!!!!!!!!!!!!!");
		}
		// System.out.println("Nodes num=" + graph.getVertexCount());
		// System.out.println("Edges num=" + graph.getEdgeCount());
		return graph;
	}

	// добавление узла в соответствующий слой
	private void addToLayer(Object n, int i) {
		List<Object> list = mapVertecies.get(i);
		if (list == null) {
			list = new LinkedList<Object>();
			mapVertecies.put(i, list);
		}
		if (!list.contains(n))
			list.add(n);
	}

	private int getLayer() {
		double tr = 0.;
		int k = 0;
		double rand = mRand.nextDouble();
		for (int l : mapVilks.keySet()) {
			tr = tr + mapVilks.get(l);
			if (rand < tr) {
				k = l;
				break;
			}
		}
		return k;

	}

	public long dt = 0;
	public int n_vilks, n_treug, n_vilks_toch;
	public int iterations;

	public Motif3(int count, Graph graph2) {
		// for (int ss = 1; ss < 10; ss++) {
		mapVertecies = new HashMap<Integer, List<Object>>();
		mapVilks = new HashMap<Integer, Double>();

		graph = graph2;

		// для ускорения выбора вершин при переборе формирую слои вершин
		long t0 = System.currentTimeMillis(); // засекаем время, алгоритм
												// начинает работать
		double sumProb = 0.;
		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			Object v = iterator.next();
			int k = graph.degree(v);
			sumProb = sumProb + (k * (k - 1) / 2);// (k * (k - 1) / 2) -
													// число
													// сочитаний из k по 2
			addToLayer(v, graph.degree(v));
		}
		// вычисляю вероятности попадания в слой
		for (int k : mapVertecies.keySet()) {
			double prob = (k * (k - 1) / 2) / sumProb;
			n_vilks_toch+=mapVertecies.get(k).size();
			mapVilks.put(k, prob * mapVertecies.get(k).size());
		}

		// Метод Монте-Карло
		// long t3 = System.currentTimeMillis();
		iterations = count;// число испытаний (выбор вилки)
		// System.out.println("N=" + N);
		int success = 0;
		for (int n = 0; n < iterations; n++) {
			{ // разыгрываю слой
				int k = 0;
				do {
					k = getLayer();
				} while (k < 2);
				// разыгрываю случайную вершину в слое
				Object v1 = mapVertecies.get(k).get(
						mRand.nextInt(mapVertecies.get(k).size()));
				// разыгрываю 2 случайные вершины
				List<Object> edges = new ArrayList(graph.getIncidentEdges(v1));
				int i = mRand.nextInt(edges.size());
				int j = i;
				do {
					j = mRand.nextInt(edges.size());
				} while (j == i);
				Object v2 = graph.getOpposite(v1, edges.get(i));
				Object v3 = graph.getOpposite(v1, edges.get(j));
				if (graph.isNeighbor(v2, v3))
					n_treug++;
				else
					n_vilks++;
			}
		}
		
		long t5 = (System.currentTimeMillis());

		dt = (t5 - t0);

	}
}
