package simbigraph.graphs.prefAttachment;
/*
 * This software is open-source under the BSD license
 */
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Factory;
import edu.uci.ics.jung.graph.Graph;

/**
 * @version 0.1, 01/12/10
 * 
 * <p>The accelerated "preferential attachment" random graph generator. At each time
 * step, a new vertex is created and is connected to existing vertices
 * according to the principle of "preferential attachment", whereby 
 * vertices with higher degree have a higher probability of being 
 * selected for attachment.</p>
 * 
 * <p>At a given timestep, the probability <code>p</code> of creating an edge
 * between an existing vertex <code>v</code> and the newly added vertex is
 * <pre>
 * p = f(degree(v)) / Summ_j f(degree(v_j));
 * </pre>
 * </p>
 * <p>where <code>Summ_j f(degree(v_j))</code> is sum of given "preferential attachment" functions 
 * of the degree of connectivity of vertices </p>

 * <p> All nodes with the specified connection are stored in one  map's slot(layers),
 *  thereby accelerating the generation algorithm. The layer is specified node's degree. 
 *  The probability Qk of choosing such a layer k  is 
 * 	Qk=|A|/ Summ_j f(degree(v_j));
 * 	where |A| is the number of vertexes in layers of k  
 *  After the layer is selected nodes are getting randomly from it</p>
 *      
 * <p>The graph created may be either directed or undirected (controlled by type the given Graph  
 * If the graph is directed, then the edges added will be directed
 * from the newly added vertex u to the existing vertex v</p> 
 * <p>The <code>parallel</code> constructor parameter specifies whether parallel edges
 * may be created.</p>
 * 
 * @see "V.Zadorozhniy and E.Yudin, Definition, generating and application of statistical homogeneity random graphs
 * Herald of Omsk Science №3(83), 2009.(in Russian)"
 * @author  Vladimir Zadorozhniy
 * @author  Eugene Yudin

 * 
 */

public class GenClassicalBA<V,E> {
	protected PrefferentialAttachment attachRule;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;
	Map<Integer, List<V>> map = new HashMap<Integer, List<V>>();
	Random mRand = new Random();
	int addEd;
	boolean parallel = false;
	

	/**
	 * 
	 * Create an instance of the generator, with which you can
	 * on the basis of the graph-seed to grow by "preferential attachment" the graph    
     * @param vertexFactory defines a way to create nodes
     * @param edgeFactory defines a way to create edges
	 * @param numEdgesToAttach the number of edges that should be attached
     * @param attachRule is interface specifying method which use in rule of "preferential attachment" 
	 */
	public GenClassicalBA(Factory<V> vertexFactory,
			Factory<E> edgeFactory, int numEdgesToAttach, PrefferentialAttachment attachRule)
	{
		assert numEdgesToAttach > 0 : "Number of edges to attach "
			+ "at each time step must be positive";

		this.vertexFactory = vertexFactory;
		this.edgeFactory = edgeFactory;
		this.attachRule =attachRule;
		addEd= numEdgesToAttach;
		mRand = new Random();
	}
	
	/**
	 * Create an instance of the generator, with which you can
	 * on the basis of the graph-seed to grow by "preferential attachment" the graph 	
	 * and which will use the current time as a seed for the random number generation.
	 * @param vertexFactory defines a way to create nodes
	 * @param edgeFactory defines a way to create edges
	 * @param numEdgesToAttach the number of edges that should be attached
	 * @param parallel specifies whether the algorithm permits parallel edges
	 * @param seed random number seed
	 * @param attachRule  is interface specifying method which use in rule of "preferential attachment"
	 */
	public GenClassicalBA(Factory<V> vertexFactory,
			Factory<E> edgeFactory, int numEdgesToAttach, boolean parallel, int seed, PrefferentialAttachment attachRule)
	{
		this(vertexFactory,
				edgeFactory, numEdgesToAttach, attachRule);
		this.parallel=parallel;
		mRand = new Random(seed);
	}
	public Graph<V, E> evolve(int step, Graph<V, E> graph) {
	try{
		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator.hasNext();) {
			V v = iterator.next();
			addToLayer(v, graph.degree(v));
		}
		// ---------------
		for (int i = 0; i < step; i++) {
			//if(i%1000==0)System.out.println("steps="+i);
			V new_n = vertexFactory.create();
			Map<V, Integer> set = new HashMap<V, Integer> ();
			//Set<V> list = new HashSet<V>();
			do {
				int k = getLayer();
				V n = map.get(k).get(mRand.nextInt(map.get(k).size()));
				set.put(n, new Integer(k));
				//list.add(n);
			} while (set.size() != addEd);
			for (V n : set.keySet()) {
				graph.addEdge(edgeFactory.create(), new_n, n);
				// переношу в соответв слой новую вершину
				int tec = set.get(n);
				map.get(tec).remove(n);
				addToLayer(n, tec + 1);
				set.put(n, tec + 1);
			}
			addToLayer(new_n, addEd);
		}
	}catch(Exception t){
		System.out.println("ошибка"+t.getMessage());
	}
		return graph;
	}
		
	//------------------
	private void addToLayer(V n, int i) {
		List<V> list = map.get(i);
		if (list == null) {
			list = new LinkedList<V>();
			map.put(i, list);
		}
		if (!list.contains(n))
			list.add(n);
	}
	
	//------------------
	private int getLayer() {
		// разыгрываем
		int k = 0;
		double rand = mRand.nextDouble();
		double tr = 0;
		// считаем знаменатель
		double sum = 0.0;
		for (int op : map.keySet())
			sum = sum +  attachRule.f(op) * map.get(op).size();
		if(sum==0){
			System.out.println("00");
			for (int op : map.keySet())
				sum = sum +  attachRule.f(op) * map.get(op).size();
		}
		for (int l : map.keySet()) {
			int A = map.get(l).size();
			tr = tr + ((double) A *  attachRule.f(l)) / sum;
			if (rand < tr) {
				k = l;
				break;
			}
		}
		
		return k;
	}
}
