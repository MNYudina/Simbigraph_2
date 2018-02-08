package temp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import simbigraph.graphs.prefAttachment.GenClassicalBA;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.util.Statistic;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

public class SepReconfiguration<V,E> {
	//protected PrefferentialAttachment attachRule;
	//protected Factory<V> vertexFactory;
	//protected Factory<E> edgeFactory;
	//protected double numEdgesToAttach[];
	Map<Integer, List<V>> map = new HashMap();
	Random mRand = new Random();
	double triangles=0;

	public SepReconfiguration() {
		
		mRand = new Random();
	}

	private void reconfiguration(Graph<V, E> graph) {
		map = new HashMap();
		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			V v = iterator.next();
			addToLayer(v, graph.degree(v));
		}
		
		Set<V> set1 = new HashSet();
		Set<E> set2 = new HashSet();
///////////////добавл€ю  рЄбра и вершин которые не вход€т в треугольники//////
		for(E e:graph.getEdges())
		{
			Pair<V> p = graph.getEndpoints(e);
			int count =countShareNeub(p.getFirst(), p.getSecond(), graph);
			if(count==0)
			{
				
				set2.add(e) ;
				set1.add(p.getFirst());
				set1.add(p.getSecond());
			}else 
				triangles+=count;

		}
		triangles=triangles/3;
//////////////////////////////////////////////////////////////////////		
	
		
		List<V> vertexes = new ArrayList(set1);
		List<E> edges = new ArrayList(set2);
		
		
		long t1= System.currentTimeMillis();
		for (int jj = 0; jj < 3000; jj++) {
			// случайный выбор вершины
			V vert = getRanVirt(vertexes);
			if(graph.degree(vert)<2){jj--;continue;}
			// определение вилки
			Pair<V> vilk = getVilk(vert, graph, map);
			if (vilk == null) {
				jj--;
				continue;
			}
			
			// определ€ем подход€щее ребро
			E rebro = getPropeEdge(graph, map, vilk, edges);

			// присоедин€ем и делаем треугольник
			Pair<V> p = doTriangle(rebro, vert, vilk, graph, map,edges);
			// компенсаци€ передаЄм номера сло€

			compensation(vilk, p, graph, map,edges);

			if(jj%200==0){
				
				Set<E> set22 = new HashSet();

				for(E e:graph.getEdges())
				{
					Pair<V> p2 = graph.getEndpoints(e);
					
					if(!isShareNeub(p2.getFirst(), p2.getSecond(), graph))
					{
						set22.add(e) ;
					}
				}
				//System.out.println("j:"+jj+" T:"+Statistic.getTriangles(graph)+"("+triangles+")  V:"+Statistic.getVilk(graph)+" t="+(System.currentTimeMillis()-t1)+" :: "+set22.size()+" :: "+edges.size());
				System.out.println("j:"+jj+" T:"+triangles+" t="+(System.currentTimeMillis()-t1)+" :: "+set22.size()+" :: "+edges.size());
			}
			}
	}

	void compensation(Pair<V> vilk, Pair<V> p, Graph<V, E> graph,
			Map<Integer, List<V>> map, List<E> edges) 
	{
		// случайно берЄм ребро в сло€х вилки
		int n_i = graph.degree(vilk.getFirst());
		int n_k = graph.degree(vilk.getSecond());
		E ed1=graph.findEdge(vilk.getFirst(), vilk.getSecond());

		List<V> list_n_i = map.get(n_i);
		List<V> list_n_k = map.get(n_k);
		/*List<Pair<V>> list = new ArrayList();
		ex1:for (V v1 : list_n_i) {
			for (V v2 : list_n_k) {
				E ed2=graph.findEdge(v1, v2);
				if (v1 != v2 && ed2!=null//ребро
						&&(ed2!=ed1)) //ребро не равно тому что мы нашли
				{
					// нужно чтобы не уходили треугольники
					if(!isShareNeub(v1,v2,graph))
					{
						list.add(new Pair<V>(v1, v2));
						break ex1;
					}
				}
			}
		}
		Pair<V> new_vilk;
		if(list.size()!=0)
			new_vilk = list.get(mRand.nextInt(list.size()));
		else
			new_vilk =vilk;
		
		*/
		
		Pair<V> new_vilk;
		do{
		V v1=list_n_i.get(mRand.nextInt(list_n_i.size()));
		V v2=list_n_k.get(mRand.nextInt(list_n_k.size()));
		E ed2=graph.findEdge(v1, v2);
		if(v1 != v2 && ed2!=null//ребро
				&&(ed2!=ed1)){
			if(!isShareNeub(v1,v2,graph)){
				new_vilk=new Pair<V>(v1, v2);
				break;
			}
		}
		}while(true);
		// случайно берЄм вершины в слое p

		int n_l = graph.degree(p.getFirst());
		int n_m = graph.degree(p.getSecond());
		
		List<V> list_n_l = map.get(n_l);
		List<V> list_n_m = map.get(n_m);
		/*
		List<Pair<V>> list2 = new ArrayList();
		ex2:for (V v1 : list_n_l) {
			for (V v2 : list_n_m) {
				if (!graph.isNeighbor(v1, v2)) {
					// нужно чтобы не уходили треугольники
					if(!isShareNeub(v1,v2,graph))
					{
						 list2.add(new Pair<V>(v1, v2));break ex2;
					}
				}
			}
		}
		Pair<V> new_p = list2.get(mRand.nextInt(list2.size()));*/

		
		Pair<V> new_p;
		do{
		V v1=list_n_l.get(mRand.nextInt(list_n_l.size()));
		V v2=list_n_m.get(mRand.nextInt(list_n_m.size()));
		if(v1 != v2 &&!graph.isNeighbor(v1, v2)){
				new_p=new Pair<V>(v1, v2);
			break;
		}
		}while(true);
		
		
		// соедин€ем с двум€ узлами ребрама в слое p

		map.get(graph.degree(new_vilk.getFirst())).remove(new_vilk.getFirst());
		map.get(graph.degree(new_vilk.getSecond())).remove(new_vilk.getSecond());
		map.get(graph.degree(new_p.getFirst())).remove(new_p.getFirst());
		map.get(graph.degree(new_p.getSecond())).remove(new_p.getSecond());

		E edge = graph.findEdge(new_vilk.getFirst(), new_vilk.getSecond());
		graph.removeEdge(edge);
		graph.addEdge(edge, new_p.getFirst(), new_p.getSecond());
	
		
		
		//удалить рЄбра, дл€ них существует треугольник
		//removeIfTriang(new_p.getFirst(),graph,edges);
		//removeIfTriang(new_p.getSecond(),graph,edges);
		
		//removeIfTriang(vilk.getFirst(),graph,edges);
		//removeIfTriang(vilk.getSecond(),graph,edges);

		triangles+=countShareNeub(vilk.getFirst(),vilk.getSecond(),graph);
		triangles+=countShareNeub(new_p.getFirst(),new_p.getSecond(),graph);
		
		// обновл€ем map
		addToLayer(new_vilk.getFirst(), graph.degree(new_vilk.getFirst()));
		addToLayer(new_vilk.getSecond(), graph.degree(new_vilk.getSecond()));
		addToLayer(new_p.getFirst(), graph.degree(new_p.getFirst()));
		addToLayer(new_p.getSecond(), graph.degree(new_p.getSecond()));
	}

	private int removeIfTriang(V v, Graph<V, E> graph, List<E> edges) {
		//посмотри всех инцидентных и удали
		
		int count =0;
		
			 //у каждой верщины считаем соседей, имеющих общее ребро
            Collection<V> neig_s=graph.getNeighbors(v);
            Iterator<V> it1 =neig_s.iterator();
            while(it1.hasNext()){
            	V node1=it1.next();
                Iterator<V> it2 =neig_s.iterator();
                while(it2.hasNext()){
                	V node2=it2.next();
                    if((node1!=node2)&&graph.isNeighbor(node1, node2))
                    count++;
                }
            }
		return count;
	}

	Pair<V> doTriangle(E rebro, V vert, Pair<V> vilk,
			Graph<V, E> graph, Map<Integer, List<V>> map, List<E> edges) {
		V n1 = vilk.getFirst();
		V n2 = vilk.getSecond();
		Pair<V> p = graph.getEndpoints(rebro);
		V n3 = p.getFirst();
		V n4 = p.getSecond();
		map.get(graph.degree(n1)).remove(n1);
		map.get(graph.degree(n2)).remove(n2);
		map.get(graph.degree(n3)).remove(n3);
		map.get(graph.degree(n4)).remove(n4);

		
		Pair<V> pp=graph.getEndpoints(rebro);

		graph.removeEdge(rebro);
		graph.addEdge(rebro, n1, n2);
		
		//удалить рЄбра, дл€ них существует треугольник


		// обновить map
		addToLayer(n1, graph.degree(n1));
		addToLayer(n2, graph.degree(n2));
		addToLayer(n3, graph.degree(n3));
		addToLayer(n4, graph.degree(n4));
		
		return new Pair<V>(n3, n4);

	}

	E getPropeEdge(Graph<V, E> graph, Map<Integer, List<V>> map, Pair<V> vilk, List<E> edges) {
		// ребро случайное но не должно быть частью треугольника
		do {
			E edge = edges.get(mRand.nextInt(edges.size()));

			// провер€ем есть ли общие соседи;
			Pair<V> p = graph.getEndpoints(edge);
			V n1 = p.getFirst();
			V n2 = p.getSecond();
			
			if( isVarNodes(vilk.getFirst(),vilk.getSecond(),n1,n2)) continue;

			int d1=graph.degree(n1);
			int d2=graph.degree(n2);
		
			if ((!isShareNeub(n1,n2,graph))&&map.get(d1 - 1) != null
					&& map.get(d2 - 1) != null
					&& (((map.get(d1 - 1).size() > 0)&&(d1!=d2)&&(map.get(d2 - 1).size() > 0))
					|| ((map.get(d1 - 1).size() > 1)&&(d1==d2)&&(map.get(d2 - 1).size() > 1))))
				return edge;
			} while (true);
	}

	 boolean isVarNodes(V n3, V n4, V n1, V n2) {
		Set set = new HashSet();
		set.add(n1);set.add(n2);set.add(n3);set.add(n4);
		return set.size()!=4;
	}

	 boolean isShareNeub(V n1, V n2, Graph<V, E> graph) {
		for (V v1 : graph.getNeighbors(n1)) {
			for (V v2 : graph.getNeighbors(n2)) {
				if(v1==v2&&(v1!=n2&&v1!=n2))return true;
			}
		}
		return false;
	}
	 
	 int countShareNeub(V n1, V n2, Graph<V, E> graph){
		 int count=0;
		 for (V v1 : graph.getNeighbors(n1)) {
				for (V v2 : graph.getNeighbors(n2)) {
					if(v1==v2&&(v1!=n2&&v1!=n2))
					count++;
					}
			}
		 
		 
		return count;
	 }

	 int countShareNeub(V n1, Graph<V, E> graph){
		 int count=0;
		 for (V v1 : graph.getNeighbors(n1)) {
			}
		 
		 
		return count;
	 }

	 
	 V getRanVirt(List<V> vertexes) {
		return vertexes.get(mRand.nextInt(vertexes.size()));
	 }

	 Pair<V> getVilk(V vert, Graph<V, E> graph, Map<Integer, List<V>> map) {
		E first = null, second = null;
		List<Pair<V>> list = new ArrayList();
		ex4:for (E link1 : graph.getIncidentEdges(vert)) {
			for (E link2 : graph.getIncidentEdges(vert)) {
				if (link1 != link2) {
					V node1 = graph.getOpposite(vert, link1);
					V node2 = graph.getOpposite(vert, link2);
					
					if (node1!=node2&&!graph.isNeighbor(node1, node2)) {
						// проверить есть ли сверху ребро
						List<V> list_n1 = map.get(graph.degree(node1)+1);
						List<V> list_n2 = map.get(graph.degree(node2)+1);
						if(list_n1==null||list_n2==null||
								list_n1.size()==0||list_n2.size()==0)	continue;
						boolean b = false;

						ex3:for (V v1 : list_n1) {
							for (V v2 : list_n2) {
								if (v1 != v2&&(b = graph.isNeighbor(v1, v2))) {
									//есть хоть одно соответствующее ребро
									if(!isShareNeub(v1,v2,graph))
									break ex3;
								}
							}
						}
						if (b)
						{
							list.add(new Pair<V>(node1, node2));
							break ex4;
						}
					}
				}
			}
		}
		if (list.size() == 0)
			return null;
		return list.get(mRand.nextInt(list.size()));
	}


	private void addToLayer(V n, int i) {
		List<V> list = map.get(i);
		if (list == null) {
			list = new LinkedList();
			map.put(i, list);
		}
		if (!list.contains(n))
			list.add(n);

	}
	void step_svyaz(Graph graph, int length) {
		System.out.println("выводим степени св€зности");
		Iterator<V> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		int[] distr1 = new int[length];
		while (it.hasNext()) {
			V node = it.next();
			// int[] mass = getR2R2(node);
			int mass[] = { graph.degree(node), 0 };
			if (mass[0] < length)
				distr0[mass[0]] = distr0[mass[0]] + 1;
			if (mass[1] < length)
				distr1[mass[1]] = distr1[mass[1]] + 1;
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
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
		// создаЄм граф затравки
		Graph graph = new UndirectedSparseGraph();

		// инициализаци€ полносв€зный граф
		for (int i = 0; i < 3; i++) {
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
			nodeFactory, edgeFactory, 2, prefA);
		g.evolve(50000,  graph);
		//String fileName ="C:\\workspace\\simbigraph\\graphs\\New Folder\\PA_STOH_AS.net";
		//String fileName ="C:\\workspace\\simbigraph\\graphs\\22тыс\\tvorog.net";
		String fileName ="C:\\workspace\\simbigraph\\graphs\\New Folder\\BA10000.net";
		/*PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
		try {
			pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
					nodeFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);
			//loadVladGraph(fileName, graph);

		} catch (IOException e5) {
			System.out.println("IOException!!!!!!!!!!!!!!!!!!");
		}*/
		SepReconfiguration gen= new SepReconfiguration();
		//System.out.println("до:"+Statistic.getTriangles(graph)+" V:"+Statistic.getVilk(graph));
		//System.out.println("до:"+Statistic.getTriangles(graph)+" V:"+Statistic.getVilk(graph));
		gen.step_svyaz(graph, 5);


		gen.reconfiguration(graph);
		System.out.println("после:"+Statistic.getTriangles(graph)+" V:"+Statistic.getVilk(graph));

		gen.step_svyaz(graph, 5);

	}
}
