package simbigraph.graphs.prefAttachment;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.algorithms.generators.EvolvingGraphGenerator;
import edu.uci.ics.jung.algorithms.generators.GraphGenerator;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.MultiGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class SpeedBAGenerator<V,E> implements EvolvingGraphGenerator<V,E> {
    private Graph<V, E> mGraph = null;
    private int mNumEdgesToAttachPerStep;
    private int mElapsedTimeSteps;
    private Random mRandom;
    protected Factory<Graph<V,E>> graphFactory;
    protected Factory<V> vertexFactory;
    protected Factory<E> edgeFactory;
    
    Map<Integer, List<V>> map = new HashMap();
	int initN = 4, addEd = 3, init_vertices=2;
	int countN = 0, avEd = 0, avCount = 0;
	Graph<V, E> graph;
	int[] massMaxK = new int[100];;
	int[] massVilki = new int[100];;
	int tr = 0;
    
    public SpeedBAGenerator(Factory<Graph<V,E>> graphFactory,
    		Factory<V> vertexFactory, Factory<E> edgeFactory, 
    		int init_vertices, int numEdgesToAttach)
    {
        assert numEdgesToAttach > 0 : "Number of edges to attach " +
                    "at each time step must be positive";
        
        mNumEdgesToAttachPerStep = numEdgesToAttach;
        mRandom = new Random(2);
        this.graphFactory = graphFactory;
        this.vertexFactory = vertexFactory;
        this.edgeFactory = edgeFactory;
        this.init_vertices=init_vertices;
    }
    private void initialize() {
    	mGraph = graphFactory.create();
       // mElapsedTimeSteps = 0;
    }

  

    public int numIterations() {
        return mElapsedTimeSteps;
    }

    public Graph<V, E> create() {
       
    	initN = 3;
		addEd = mNumEdgesToAttachPerStep;// количество начальных вершин и ср кол рёбер
		// int countN=0;
		// int k_max = 500;
		// инициализация полносвязный граф
		map.put(addEd, new LinkedList());
		graph = new DirectedSparseMultigraph();
		for (int i = 0; i < initN; i++) {
			V n = vertexFactory.create();
			graph.addVertex(n);
			List<V> list = map.get(addEd);
			list.add(n);
			++countN;
			// map.put(initN, list);
		}
		int l = 0;
		Object[] mass = graph.getVertices().toArray();
		for (int i = 0; i < mass.length - 1; i++)
			for (int j = i + 1; j < mass.length; j++)
				graph.addEdge(edgeFactory.create(), (V) mass[i],
						(V) mass[j], EdgeType.DIRECTED);
		// ---------------
		// for (int k = 0; k < k_max+1; k++)
		// map.put(k, new LinkedList());

		// avEd=graph.getEdgeCount();
		// avCount = graph.getVertexCount();
		// выращивание
		// Random r = mRand.nextDouble();;
		
		for (int i = 0; i < init_vertices-3; i++) {
			avEd = graph.getEdgeCount();
			avCount = graph.getVertexCount();
			V new_n = vertexFactory.create();
			Map<V, Integer> set = new HashMap();
			List<V> list = new LinkedList();
			// разыгрываю случайную величину к
			// for (int j = 0; j < addEd; j++) {
			// разыгрываю случайную величину к
			do {
				int k;
				boolean flag = true;
				V n = null;
				k = getKLogT();
				// if(maxK<k)maxK=k;
				n = map.get(k).get(mRand.nextInt(map.get(k).size()));
				set.put(n, new Integer(k));
				list.add(n);
			} while (list.size() != addEd);

			for (V n : list) {
				graph.addEdge(edgeFactory.create(), new_n, n);
				// переношу в соответв слой новую вершину
				int tec = set.get(n);
				//map.get(tec).remove(n);
				
				removeFromLayer(tec,n);
				addToLayer(n, tec + 1);
				set.put(n, tec + 1);
				addToLayer(new_n, addEd);

			}
			
		}
		return graph;
    }
    
    
    private void removeFromLayer(int tec, V n) {
		// TODO Auto-generated method stub
		map.get(tec).remove(n);
		if(map.get(tec).size()==0)map.remove(tec);
	}

	
	// -----------------
	private int getK(int k_max, int l) {

		// разыгрываем
		int k = l;
		do {
			double rand = Math.random();
			double tr = 0;
			k = l;
			while (true) {
				tr = tr + ((l + 1)) / (1.0 * (k + 1) * (k + 2));
				if (rand < tr)
					break;
				k++;
			}

		} while (k >= k_max);

		return k;
		// return 0;
	}

	Random mRand = new Random();

	// --------------------------------------
	double f(int k) {
		double d = (double) k;
		return d;
	}

	private int getKLogT() {

		// разыгрываем
		int k = 0;
		double rand = mRand.nextDouble();
		double tr = 0;
		// считаем знаменатель
		double sum = 0.0;
		for (int op : map.keySet())
			sum = sum + f(op) * map.get(op).size();

		double sum2 = 0.0;
		// находим k
		for (int l : map.keySet()) {
			int A = map.get(l).size();

			tr = tr + ((double) A * f(l)) / sum;
			if (rand < tr) {
				k = l;
				break;
			}
		}
		return k;
	}

	
	// --------------------------------------
	private void addToLayer(V n, int i) {
		List<V> list = map.get(i);
		if (list == null) {
			list = new LinkedList();
			map.put(i, list);
		}
		if (!list.contains(n))
			list.add(n);

	}
	public void evolveGraph(int arg0) {
		// TODO Auto-generated method stub
		
	}

	
	

	
}

