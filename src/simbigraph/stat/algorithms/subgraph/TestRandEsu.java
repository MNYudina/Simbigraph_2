package simbigraph.stat.algorithms.subgraph;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;

public class TestRandEsu {
	public static void main(String[] args) {
		Graph<Long, Long> graph;
		// graph = Test.initGraphLong();

		/*	graph = new SparseGraph<Long, Long>();
		graph.addEdge(1l, 1l, 2l);
		graph.addEdge(2l, 2l, 3l);
		graph.addEdge(3l, 3l, 1l);
		graph.addEdge(4l, 1l, 4l);
		graph.addEdge(5l, 3l, 8l);
		graph.addEdge(6l, 3l, 9l);
		graph.addEdge(7l, 2l, 7l);
		graph.addEdge(8l, 2l, 6l);
		graph.addEdge(9l, 1l, 5l);
		
		graph.addEdge(1l, 1l, 2l);
		graph.addEdge(2l, 2l, 3l);
		graph.addEdge(3l, 3l, 1l);
		graph.addEdge(7l, 3l, 7l);
		graph.addEdge(4l, 3l, 4l);
		graph.addEdge(5l, 3l, 5l);
		graph.addEdge(6l, 3l, 6l);
*/		
		
		
		graph = Test.initGraphLong("mygraphs//twitter//Twitter_obr_new.net");
		//graph = Test.initGraphLong("mygraphs//my_polBlog1.net");
		//graph = Test.initGraphLong("RealGraphs//PGPgiantcompo.net");
		System.out.println("E=" + graph.getEdgeCount() + " V=" + graph.getVertexCount());
		long t1 = System.currentTimeMillis();
		enumirateSubgraphs(graph, 3);
		System.out.println("t=" + (t1 - System.currentTimeMillis()));
		System.out.println(num);
	}

	private static void enumirateSubgraphs(Graph<Long, Long> graph, int i) {
		// 1
		for (Long v : graph.getVertices()) {
			// 2
			Set<Long> Vext = new HashSet();
			for (Long n : graph.getNeighbors(v)) {
				if (n > v)
					Vext.add(n);
			}
			// 3
			Set<Long> V = new HashSet();
			V.add(v);
			//if(Math.random()<0.5)
				extendSubgraph(V, Vext, v, graph);
		}
	}

	static int num = 0;

	private static void extendSubgraph(Set<Long> Vsub, Set<Long> Vext, Long v, Graph<Long, Long> graph) {
		// 1
		if (Vsub.size() == 3) {// num++;
			// for (Long long1 : Vext) {
			num++;//System.out.println(num++ + ":" + Vsub + " ");

			// }
			return;
		}
		// 2
		while (Vext.size() > 0) {
			// Long w = Vext.stream().findAny().get();

			for (Iterator<Long> it = Vext.iterator(); it.hasNext();) {
				Long w = (Long) it.next();
				// 3
				it.remove();
				// 4
				Set<Long> _Vext = new HashSet(Vext);
				
				
				Set<Long> tempSet = new HashSet(graph.getNeighbors(w));
				Set<Long> tempSet2 = new HashSet();
				for (Long v_ : Vsub)
					for (Long v2_ : graph.getNeighbors(v_))
						if(tempSet.contains(v2_))tempSet2.add(v2_);
					
				
				tempSet.addAll(new HashSet(Vsub));
				tempSet.removeAll(tempSet2);
				
				for (Iterator iterator = tempSet.iterator(); iterator.hasNext();) {
					Long u = (Long) iterator.next();
					if (u <= v || Vsub.contains(u)||u==w){
						iterator.remove();
						//System.out.println(u);						
					}

				}
				_Vext.addAll(tempSet);
				_Vext.remove(w);
				
				
				// 5

				Set<Long> newVsub = new HashSet(Vsub);
				newVsub.add(w);
				//if(Math.random()<0.5)
					extendSubgraph(newVsub, _Vext, v, graph);
			}
		}

	}
}