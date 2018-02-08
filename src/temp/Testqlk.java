package temp;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import org.apache.commons.collections15.Factory;

import simbigraph.core.PrefAttechRule;
import simbigraph.graphs.prefAttachment.ConfGeneral;
import simbigraph.graphs.prefAttachment.GenClassicalBA;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.visualization.subLayout.GraphCollapser;

public class Testqlk {
	Factory<Integer> vertexFactory=new Factory<Integer>() { // Фабрика вершин
		public Integer create() {
			return new Integer(nodeCount++);
		}
	};
	Factory<Integer> edgeFactory=new Factory<Integer>() { // Фабрика ребер
		public Integer create() {
			return new Integer(edgeCount++);
		}
	};
	Factory<Graph<Integer, Integer>> graphFactory = new Factory<Graph<Integer, Integer>>() 
	{

		public Graph<Integer, Integer> create() {
				return new UndirectedSparseGraph<Integer, Integer>();
			}
		};
	Graph<Integer, Integer> g;
	int nodeCount = 0, edgeCount = 0;

	public Testqlk() {
	// выбираем граф и генерируем
		//g = doBA();
	//g= doNotBA();
		//double[][] mass = getTettaMatrix(g, 8);
		//g = doOrtBA();
		//g=getNet("Alpa07.net");
		double[][] mass = getQMatrix(g, 10);
		
	
		// выводим на экран фундаментальную матрицу
		
	
		System.out.println("узлов:"+g.getVertexCount());
		System.out.println("ребер:"+g.getEdgeCount());


		for (int i = 0; i < mass[0].length; i++) {
			for (int j = 0; j < mass.length; j++) {
				System.out.print(mass[i][j]/((double) g.getEdgeCount()) + " ");
			}
			System.out.println();
		}
	}

	 private Graph<Integer, Integer> getNet(String fileName) {
		System.out.println(fileName);
		Graph graph = new SparseGraph();
		PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
		try {
			pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
					vertexFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);

		} catch (IOException e5) {
			System.out.println("IOException!!!!!!!!!!!!!!!!!!");
		}
		System.out.println("Nodes num=" + graph.getVertexCount());
		System.out.println("Edges num=" + graph.getEdgeCount());
		return graph;
	}


	private double[][] getQMatrix(Graph graph, int size) {
		double[][] ret = new double[size][size];
		Collection list = graph.getEdges();
		for (Object edge : list) {
			Pair p = graph.getEndpoints(edge);
			Object n1 = p.getFirst();
			Object n2 = p.getSecond();
		/*	if (graph.degree(n1) > graph.degree(n2)) {
				Object n3 = n1;
				n1 = n2;
				n2 = n3;
			}*/
			int degree_n1 = graph.degree(n1);
			int degree_n2 = graph.degree(n2);
			if (degree_n1 < size && degree_n2 < size) {
				ret[degree_n1][degree_n2] = ret[degree_n1][degree_n2] + 1.;
				//ret[degree_n2][degree_n1] = ret[degree_n2][degree_n1] + 1.;

			}
		}
		return ret;
	}
	

	private Graph<Integer, Integer> doOrtBA() {
		Graph seed_graph = createOrtSeed();
		GenClassicalBA gen = new GenClassicalBA(vertexFactory, edgeFactory, 2,
				new prefRule());

		return gen.evolve(10000-5, seed_graph);
	}

	private Graph createOrtSeed() {
		Graph gr = new DirectedSparseGraph<Integer, Integer>();
		for (int i = -1; i > -6; i--) {
			Integer n = new Integer(i);
			gr.addVertex(n);
		}
		int l = -1;
		Object[] mass = gr.getVertices().toArray();
		for (int i = 0; i < mass.length - 1; i++)
			for (int j = i + 1; j < mass.length; j++)
				if (i != j)
					gr.addEdge(new Integer(l--), (Integer) mass[i],
							(Integer) mass[j], EdgeType.DIRECTED);

		return gr;
	}

	private Graph<Integer, Integer> doNotBA() {

		int initVertices= 10000;
		int edgesToAttach = 2;

		ConfGeneral gen = new ConfGeneral(graphFactory,vertexFactory,edgeFactory,edgesToAttach);
		Graph graph =gen.getBA(initVertices,edgesToAttach);
		return graph;
	}

	private Graph<Integer, Integer> doBA() {
		Graph seed_graph = createSeed();
		
		GenClassicalBA gen = new GenClassicalBA(vertexFactory, edgeFactory, 2,
				new prefRule());

		return gen.evolve(10000-5, seed_graph);
	}

	private Graph createSeed() {
		Graph gr = new UndirectedSparseGraph<Integer, Integer>();
		for (int i = -1; i > -6; i--) {
			Integer n = new Integer(i);
			gr.addVertex(n);
		}
		int l = -1;
		Object[] mass = gr.getVertices().toArray();
		for (int i = 0; i < mass.length - 1; i++)
			for (int j = i + 1; j < mass.length; j++)
				if (i != j)
					gr.addEdge(new Integer(l--), (Integer) mass[i],
							(Integer) mass[j], EdgeType.UNDIRECTED);

		return gr;
	}

	public static void main(String[] args) {
		new Testqlk();

	}

	public double[][] getTettaMatrix(Graph graph, int size) {
		double[][] ret = new double[size][size];
		Collection list = graph.getEdges();
		for (Object edge : list) {
			Pair p = graph.getEndpoints(edge);
			Object n1 = p.getFirst();
			Object n2 = p.getSecond();
			if (graph.degree(n1) > graph.degree(n2)) {
				Object n3 = n1;
				n1 = n2;
				n2 = n3;
			}
			int degree_n1 = graph.degree(n1);
			int degree_n2 = graph.degree(n2);
			if (degree_n1 < size && degree_n2 < size) {
				ret[degree_n1][degree_n2] = ret[degree_n1][degree_n2] + 1.;
				ret[degree_n2][degree_n1] = ret[degree_n2][degree_n1] + 1.;

			}
		}
		return ret;
	}
}

class prefRule extends PrefAttechRule {
	public double f(int k) {
		return k;
	}

	public int getM() {
		return Integer.MAX_VALUE;
	}
};
