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



public class GenTvorogBA<V,E> {
	protected Factory<Graph<V, E>> graphFactory;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;
	private Graph<V, E> graph;
	Random mRand = new Random();
	int m;
	public GenTvorogBA(Factory<Graph<V, E>> graphFactory, Factory<V> vertexFactory,
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

	public Graph<V, E>  getGraphTvorog(int num, int m) {
		List<V> curVertexes = new ArrayList();
		// UndirectedGraph<MyNode, MyLink> g=new UndirectedSparseGraph();
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
		ex: do {
			
			//сортирую список всех вершин
			Collections.sort(curVertexes, new myComp(graph));
			V n1 = curVertexes.get(0);// null;//
			//System.out.println( mapVertexDegree.get(n1) - graph.inDegree(n1));
		
			
			
			// разыгрываем с кем можно соединить n1
			V n2 = null;
			for (int i = curVertexes.size() - 1; i >= 0; i--) {
				V n = curVertexes.get(i);
				System.out.println( mapVertexDegree.get(n) - graph.inDegree(n));

				if ((n != n1) && (!graph.isNeighbor(n, n1))) {
					n2 = n;
					graph.addEdge(edgeFactory.create(), n2, n1);
					break;
				}
				// если вакансий больше нет то
				// удаляем вершины из списка
			}
			if(n2!=null){
				if (graph.inDegree(n1) == mapVertexDegree.get(n1))
					curVertexes.remove(n1);
				if (graph.inDegree(n2) == mapVertexDegree.get(n2))
					curVertexes.remove(n2);
				if (curVertexes.size() < 1)
					break ex;
			}
			else{
				List curVert = new ArrayList();
				for (V f : curVertexes) {
					int j = graph.inDegree(f);
					for (int i = 0; i < (mapVertexDegree.get(f) - j); i++) {
						V newNode =vertexFactory.create();
						
						mapVertexDegree.put(newNode, getVirtexK(m));
						curVert.add(newNode);
						graph.addVertex(newNode);
						graph.addEdge(edgeFactory.create(), f, newNode);
						
					}
				}
				//System.out.println("оп");
				curVertexes.clear();
				curVertexes = curVert;
				continue ex;
				
				
			}
			// if(curVertexes.size()>100) break ex;

		} while (true);

		// }
     // System.out.println("ssssssssss");
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
