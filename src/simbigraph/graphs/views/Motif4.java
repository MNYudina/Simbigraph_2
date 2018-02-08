package simbigraph.graphs.views;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetReader;

class Para {
	int _n1, _n2;

	Para(int n1, int n2) {
		_n1 = n1;
		_n2 = n2;
	}

	public int hashCode() {
		return _n2 + _n1;
	}

	public boolean equals(Object o) {
		return (o instanceof Para) && (_n1 == ((Para) o)._n1)
				&& (_n2 == ((Para) o)._n2);
	}

	public Para setKK(int k1, int k2) {
		_n1 = k1;
		_n2 = k2;
		return this;
	}
}

public class Motif4 {
	public double CoefCl=0;
	public long n_kvadrat=0;
	public long n_semi_klika=0;
	public long n_roga=0;
	public long n_klika=0;
	public long iterations =0;

	public Motif4(int count, Graph graph2) {
		 this.iterations =count;
		S=0;
		Graph graph = graph2;
		// поместить в матрицу ребер
		for (Object l0 : graph.getEdges()) {
			Pair<Object> pa = graph.getEndpoints(l0);
			int min = graph.degree(pa.getFirst());
			int max = graph.degree(pa.getSecond());
			Para temp = null;
			if (min > max)
				add2pair(max, min, l0);
			else
				add2pair(min, max, l0);
			S=S+(max-1)*(min-1);
		}
		//System.out.println("S"+S);
		
		for(int i=0; i<count; i++){
		//формируем вероятности выбора ребер
		for (Para p : mapOfPairList.keySet()) {
			double pr=mapOfPairList.get(p).size()*(p._n1-1)*(p._n2-1)/(double)S;
			pair_prob.put(p, pr);
		}
		
		Para myPair=getPair();
		// разыгрываю случайную ребро в списке ребер, с заданными степенями вершин
		Object e1 = mapOfPairList.get(myPair).get(
				mRand.nextInt(mapOfPairList.get(myPair).size())); 
		//вершины ребра
		Pair pV= graph.getEndpoints(e1);
		Object v1 = pV.getFirst();
		Object v2 = pV.getSecond();
		//ребра для двух вершин выбранного ребра
		List list1 = new ArrayList(graph.getIncidentEdges(v1));
		List list2 = new ArrayList( graph.getIncidentEdges(v2));
		
		list1.remove(e1);
		list2.remove(e1);
		//случайные вершины
		Object v3 = graph.getOpposite(v1, list1.get(mRand.nextInt(list1.size())));
		Object v4 = graph.getOpposite(v2, list2.get(mRand.nextInt(list2.size())));
		if ((graph.isNeighbor(v3, v4))&&(graph.isNeighbor(v3, v2))&&(graph.isNeighbor(v1, v4))){
			n_klika++;
		}else if ((graph.isNeighbor(v3, v4))&&((graph.isNeighbor(v3, v2))||(graph.isNeighbor(v1, v4)))){
			n_semi_klika++;
		}else if (graph.isNeighbor(v3, v4)){
			n_kvadrat++;
		}
		else{
			n_roga++;
		}
		}
		//CoefCl=n_kvadrat/1000.;
		}
	Para temp = new Para(0, 0);
	Map<Para, List<Object>> mapOfPairList = new HashMap();
	Map<Integer, List<Object>> mapVertecies = new HashMap();
	Map<Para, Double> pair_prob = new HashMap();
	Factory<Object> vertexFactory = new Factory<Object>() {
		public Object create() {
			return new Object();
		}
	};
	Factory<Object> edgeFactory = new Factory<Object>() {
		public Object create() {
			return new Object();
		}
	};
	Random mRand = new Random();

	private Graph getNet(String fileName) {
		//System.out.println(fileName);
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
		//System.out.println("Nodes num=" + graph.getVertexCount());
		//System.out.println("Edges num=" + graph.getEdgeCount());
		return graph;
	}
	private long S;

	//
	private Para getPair() {
		double s = 0.;
		Para k = null;
		double rand = mRand.nextDouble();
		for (Para l : pair_prob.keySet()) {
			s = s + pair_prob.get(l);
			if (rand < s) {
				k = l;
				break;
			}
		}
		return k;
	}

	private void add2pair(int min, int max, Object link) {
		Para para = new Para(min, max);
		List<Object> p = mapOfPairList.get(para);
		if (p == null) {
			p = new ArrayList();
			mapOfPairList.put(para, p);
		}
		p.add(link);
	}


}
