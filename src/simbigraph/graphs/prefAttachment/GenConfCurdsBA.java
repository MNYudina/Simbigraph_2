/**
 * 
 * @author Yudinev
 * Генератор графа-творога с распределением вершин как в графе Барабаши-Альберт
 * 
 * 
 */

package simbigraph.graphs.prefAttachment;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections15.Factory;



public class GenConfCurdsBA<V,E> {
	protected Factory<Graph<V, E>> graphFactory;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;
	private Graph<V, E> graph;
	Random mRand = new Random();
	int m;
	public GenConfCurdsBA(Factory<Graph<V, E>> graphFactory, Factory<V> vertexFactory,
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

	public Graph<V, E>  getBara2(int num, int m) {
		List<V> curVertexes = new ArrayList();
		graph = graphFactory.create();
		for (int i = 0; i < num; i++) {
			V n = vertexFactory.create();

			tt = i;
			graph.addVertex(n);
			// List<MyNode> list = map.get(initN - 1);
			curVertexes.add(n);
			//n.setK(getVirtexK(m));
			mapVertexDegree.put(n, getVirtexK(m));

		}
		//сортирую список всех вершин
		Collections.sort(curVertexes, new myComp(graph));
		int j=0;
		List aliveList = new ArrayList(curVertexes);
		ex: do {
			V n1 = curVertexes.get(j);
			if(!aliveList.contains(n1)){
				j++;
				continue;
			}
			// разыгрываем с кем можно соединить n1
			V n2 = null;
			ex2:for (int i = curVertexes.size() - 1; i >= 0; i--) {
				if (aliveList.size() < 2)
					break ex;
				n2 = curVertexes.get(i);
				//System.out.println( mapVertexDegree.get(n2) - graph.inDegree(n2));
				if(!aliveList.contains(n2)){
					continue;
				}else if(n2==n1){
					i = curVertexes.size();
					continue;
					}
				graph.addEdge(edgeFactory.create(), n2, n1);
				if (graph.degree(n2) == mapVertexDegree.get(n2))
					aliveList.remove(n2);
				if (graph.degree(n1) == mapVertexDegree.get(n1))
					{aliveList.remove(n1); j++; break ex2;}
			}
		} while (true);

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
