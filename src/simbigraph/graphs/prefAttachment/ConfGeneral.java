/**
 * 
 * @author Yudinev
 * Генератор графа-творога с распределением вершин как в графе Барабаши-Альберт
 * 
 * 
 */

package simbigraph.graphs.prefAttachment;

import edu.uci.ics.jung.graph.Graph;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


import org.apache.commons.collections15.Factory;
public class ConfGeneral<V,E> {
	
	protected Factory<Graph<V, E>> graphFactory;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;
	private Graph<V, E> graph;
	Random mRand = new Random();
	int m;
	public ConfGeneral(Factory<Graph<V, E>> graphFactory, Factory<V> vertexFactory,
			Factory<E> edgeFactory, int mass_addEdd) {
		this.graphFactory = graphFactory;
		this.vertexFactory = vertexFactory;
		this.edgeFactory = edgeFactory;
		this.m=mass_addEdd;
		mRand = new Random();
		
	}
	class myComp implements Comparator<Object> {
		Graph graph;

		public myComp(Graph graph) {
			this.graph = graph;
		}

		public int compare(Object o1, Object o2) {
			Object n1 =  o1;
			Object n2 = o2;

			int j1 = mapVertexDegree.get(n1) - graph.inDegree(n1);
			int j2 = mapVertexDegree.get(n2) - graph.inDegree(n2);
			return (j1 < j2 ? -1 : (j1 == j2 ? 0 : 1));
		}
	}
	int tt = 0;
	Map<V,Integer> mapVertexDegree = new HashMap<V,Integer>();

	public Graph<V, E>  getBA(int num, int m) {
		List<V> curVertexes = new ArrayList();
		graph = graphFactory.create();
		for (int i = 0; i < num; i++) {
			V n = vertexFactory.create();
			int a=getVirtexK(m);
			graph.addVertex(n);
			for (int j = 0; j < a; j++) {
				curVertexes.add(n);
			}

		}
		V[] mass=(V[]) graph.getVertices().toArray();
		Random rand = new Random();
		do{
			V a =curVertexes.get(rand.nextInt(curVertexes.size()));
			V b =curVertexes.get(rand.nextInt(curVertexes.size()));
			if(a!=b&&!graph.isNeighbor(a, b)){
				graph.addEdge(edgeFactory.create(),a, b);
				curVertexes.remove(a);
				curVertexes.remove(b);
			}
		}while(curVertexes.size()>1);
		
	
		return graph;
	}

	// -------------------------------------------------------------------------------------------------
	private static int getVirtexK(int m) {
		int l = m;
		int k = m;
		double rand = Math.random();
		double tr = 0;
		while (true) {
			tr = tr + (2.0 * l * (l + 1)) / (1.0 * k * (k + 1) * (k + 2));
			if (rand < tr) {
				break;
			}
			k++;
		}
		return k;
	}
}
