package temp;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.collections15.Factory;



import simbigraph.core.Context;
import simbigraph.graphs.prefAttachment.ConfGeneral;
import simbigraph.graphs.prefAttachment.GenClassicalBA;
import simbigraph.graphs.prefAttachment.GeneratorClastPA;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.util.Statistic;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetReader;


public class SquareClustering<V, E>{
	static SquareClustering sq;
	private long eCount = 0;
	
	public void logECount()
	{
		System.out.println("edges processed: " + eCount);
	}
	
	private long getSquaresCount(Graph<V, E> graph)
	{
		eCount = 0;
		long squares=0;
		ThreadFactory thfact = new ThreadFactory();
		thfact.setGraph(graph);
		for(E e:graph.getEdges())
		{
			eCount++;
//			Pair<V> p = graph.getEndpoints(e);
//			V n1 = p.getFirst(), n2 = p.getSecond();
//			int count = 0;
//			for (V v1 : graph.getNeighbors(n1)) {
//				for (V v2 : graph.getNeighbors(n2)) {
//					if(graph.isNeighbor(v1, v2) && (v1!=n2 && v2!=n1))
//							count++;
//					}
//			}
////			if(count!=0)			 
//				squares+=count;
			
			thfact.createThread(e).start();
			

		}
		squares = thfact.getSquaresCount();
//		System.out.println("in set: "+squaresSet.size()+"; count: "+squares);
//		System.out.println("count: "+squares);
		return squares/4;
	}
	// генерация графа затравки, создаю неориентированный граф
	static private Graph seed_graph() {
		Graph gr = new UndirectedSparseMultigraph<Integer, Integer>();
		for (int i = -1; i > -5; i--) {
			Integer n = new Integer(i);
			gr.addVertex(n);
		}
		int l = -1;
		Object[] mass = gr.getVertices().toArray();
		for (int i = 0; i < mass.length - 1; i++)
			for (int j = i + 1; j < mass.length; j++)
				//if (i != j)
					gr.addEdge(new Integer(l--), (Integer) mass[i],
							(Integer) mass[j], EdgeType.UNDIRECTED);

		return gr;
	}
	static private Graph seed_graph2() {
		Graph gr = new UndirectedSparseMultigraph<Integer, Integer>();
		for (int i = 1; i <5; i++) {
			Integer n = new Integer(i);
			gr.addVertex(n);
		}
		gr.addEdge(1, 1,2, EdgeType.UNDIRECTED);
		gr.addEdge(2, 3,2, EdgeType.UNDIRECTED);
		gr.addEdge(3, 3,4, EdgeType.UNDIRECTED);
		gr.addEdge(4, 1,4, EdgeType.UNDIRECTED);

		return gr;
	}

	
	public static void main(String[] args) {
		Factory<Integer> edgeFactory = new Factory<Integer>() {
			int i = 0;

			public Integer create() {
				return new Integer(i++);
			}
		};
		Factory<UndirectedSparseMultigraph<Integer, Integer>> gFactory = new Factory<UndirectedSparseMultigraph<Integer, Integer>>() {
			public UndirectedSparseMultigraph<Integer, Integer> create() {
				return new UndirectedSparseMultigraph<Integer, Integer>();
			}
		};
		Factory<Integer> nodeFactory = new Factory<Integer>() {
			int i = 0;

			public Integer create() {
				return new Integer(i++);
			}
		};		
		sq = new SquareClustering();
		Graph graph = seed_graph2();
//		Graph graph = sq.getErdosReniy(10680,0.1,nodeFactory,edgeFactory);
//		Graph graph = sq.getConfGeneralBAGraph(36692,5,gFactory,nodeFactory,edgeFactory);
		String filename = "C:\\Users\\Yudinev\\Desktop\\simgraphJ\\graphs\\RealGraphs\\myAS.net";
		//Graph graph = sq.getGraphFromFile(filename, nodeFactory, edgeFactory);
		System.out.println("vertex count: "+graph.getVertexCount());
		System.out.println("edges count: "+graph.getEdgeCount());
		
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				sq.logECount();
			}
		};
		timer.schedule( task, 60*1000, 120*1000);
		long t1 = System.currentTimeMillis();
		long sqC = sq.getSquaresCount(graph);
		long t2 = System.currentTimeMillis();
		timer.cancel();
		System.out.println("squares counted: "+sqC);
		System.out.println("time (ms): "+(t2-t1));
//		System.out.println("vilks: "+ Statistic.getVilk(graph));
	}
	
	public Graph getErdosReniy(int vertices, double prob,  Factory<Integer> nodeFactory, Factory<Integer> edgeFactory) {
		Factory<UndirectedGraph<Integer, Integer>> graphFactory2 = new Factory<UndirectedGraph<Integer, Integer>>() {
			private int n = 0;

			public UndirectedGraph<Integer, Integer> create() {
				return new UndirectedSparseGraph<Integer, Integer>();
			}
		};

		ErdosRenyiGenerator<Integer, Integer> gen = new ErdosRenyiGenerator<Integer, Integer>(
				graphFactory2, nodeFactory, edgeFactory, vertices, prob);
		// gen.evolveGraph(evolvingSteps);
		Graph graph = gen.create();
//		System.out.println("Nodes num =" + graph.getVertexCount());
//		System.out.println("Edges num=" + graph.getEdgeCount());
		return graph;
	}
	
	public Graph getGraphFromFile(String filename, Factory<Integer> nodeFactory, Factory<Integer> edgeFactory)
	{
		Graph graph = new SparseGraph<Integer, Integer>();
		//reading from file
//		String fileName = "D:\\ngh\\java\\eclipse\\main_workspace\\simbigraph\\graphs\\RealGraphs\\my_inet.net";
		PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
		try {
			pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(nodeFactory, edgeFactory);
			//File file = new File(fileName);
			pnr.load(filename, graph);

		} catch (IOException e5) {
			System.out.println("IOException!!!!!!!!!!!!!!!!!!");
		}
		return graph;
	}

	
	public Graph getBAGraph(int vertices, double prob,  Factory<Integer> nodeFactory, Factory<Integer> edgeFactory)
	{
		// создаём граф затравки
		Graph graph = new UndirectedSparseGraph();
		// инициализация полносвязный граф
		for (int i = 0; i < 6; i++) {
			Integer n = nodeFactory.create();
			graph.addVertex(n);
		}
		Object[] mass2 = graph.getVertices().toArray();
		for (int i = 0; i < mass2.length - 1; i++)
			for (int j = i + 1; j < mass2.length; j++) {
				Integer link = edgeFactory.create();
				graph.addEdge(link, (Integer) mass2[i], (Integer) mass2[j],
						EdgeType.UNDIRECTED);
			}
		PrefferentialAttachment prefA = new PrefferentialAttachment() {
			@Override
			public double f(int k) {
				return k;
			}

			@Override
			public int getM() {
				return Integer.MAX_VALUE;
			}
		};
		GenClassicalBA<Integer, Integer> g = new GenClassicalBA<Integer, Integer>(
			nodeFactory, edgeFactory, 5, prefA);
//		GeneratorClastPA<Integer, Integer> g = new GeneratorClastPA<Integer, Integer>(nodeFactory, edgeFactory,);
		System.out.println("evolving graph...");
		g.evolve((int) (vertices*prob), graph);
		return graph;
	}
	
	public Graph getConfGeneralBAGraph(int initVertices, int edgesToAttach, Factory<Integer> graphFactory, Factory<Integer> nodeFactory, Factory<Integer> edgeFactory)
	{
		ConfGeneral gen = new ConfGeneral(graphFactory,nodeFactory,edgeFactory,edgesToAttach);
		Graph graph =gen.getBA(initVertices, edgesToAttach);
		return graph;
	}
	
	public class ThreadFactory
	{
		private Graph<V, E> graph;
		private long squares;
		public ThreadFactory() {
			// TODO Auto-generated constructor stub
			squares = 0;
		}
		
		public void setGraph(Graph<V, E> graph)
		{
			this.graph = graph;
		}
		
		public long getSquaresCount()
		{
			return squares;
		}
		
		public Thread createThread(E edge)
		{
			CountProcess cp = new CountProcess();
			cp.edge = edge;
			return new Thread(cp);
		}
		
		public synchronized void addSquares(int count)
		{
//				synchronized (this) {
					squares+=count;
//				}
		}
		
		public class CountProcess implements Runnable
		{
			private E edge;
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Pair<V> p = graph.getEndpoints(edge);
				V n1 = p.getFirst(), n2 = p.getSecond();
				int count = 0;
				for (V v1 : graph.getNeighbors(n1)) {
					for (V v2 : graph.getNeighbors(n2)) {
						if(graph.isNeighbor(v1, v2) && (v1!=n2 && v2!=n1))
								count++;
						}
				}
				addSquares(count);
			}
		}
		
	}
	

}


