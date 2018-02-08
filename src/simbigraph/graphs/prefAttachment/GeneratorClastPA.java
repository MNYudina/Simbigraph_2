package simbigraph.graphs.prefAttachment;
/**
 * @category √енератор графа с подстройкой коэффициента кластеризации
 * использует ускорение за счЄт хранени€ "матрицы" рЄбер
 * 
 * @author Yudin
 */

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
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


public class GeneratorClastPA<V,E> {
	
	Para temp = new Para(0, 0);
	Map<Para, List<E>> pair_matrix = new HashMap();
	Map<Integer, List<V>> map = new HashMap();
//	int initN = 4, addEd = 3;
	int countN = 0, avEd = 0, avCount = 0;
	Graph<V, E> graph;
	int[] massMaxK = new int[100];;
	int[] massVilki = new int[100];;
	protected PrefferentialAttachment attachRule;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;
	protected double numEdgesToAttach[];
	Random mRand = new Random();
	

	public GeneratorClastPA(Factory<V> vertexFactory,
			Factory<E> edgeFactory, double[] probEdgesToAttach , PrefferentialAttachment attachRule)
	{	
		double s=0.;
		for (double d : probEdgesToAttach) {
			s=s+d;
		}
		assert Math.abs(s-1.)<0.000000001 : "—умма веро€тностей по различным значени€м "
			+ "числа добавл€емых на шаге ребер должна равн€тьс€ 1";

		this.vertexFactory = vertexFactory;
		this.edgeFactory = edgeFactory;
		this.numEdgesToAttach= probEdgesToAttach;
		this.attachRule =attachRule;
		mRand = new Random();
	}
	
	
	// ----------------------------------------------------------------------
	public Graph<V, E> evolve(int step,  double lyamda, Graph<V, E> graph)  {
	
		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator.hasNext();) {
			V v =  iterator.next();
			addToLayer(v, graph.degree(v));
		}
		//int l = 0;
		for (E edg : graph.getEdges()) {
			
			Pair<V> pa = graph.getEndpoints(edg);
			int min = graph.degree(pa.getFirst());
			int max = graph.degree(pa.getSecond());

			if (min > max)
				add2pair(max, min, edg);
			else
				add2pair(min, max, edg);
		}
	
		mRand = new Random();

		// -------------------------------------------------------------------------
		// обращаюсь к матрице и нахожу одно из рЄбер, тем самым и требуемых два
		// и требуемых два узла остальные узлы беру случайно.
		//

		for (int i = 0; i < step; i++) {
			
			
			double s=0.;
			double r=mRand.nextDouble();
			int addEd =0;
			for (int j = 0; j < numEdgesToAttach.length; j++) {
					s=s+numEdgesToAttach[j];
					if(s>r)
					{
						addEd =	j;
						break;
					}
			 }
			if(addEd==0)continue;
			
			Map<V, Integer> set = new HashMap();
			// разыгрываю две случайные величины, определ€ющие номера слоЄв
			int k1 = 0, k2 = 0;
			 r = mRand.nextDouble();
			E p = null;
			V n1, n2;
			boolean b = true;
			// массив выбранных индексов
			int[] indexes = new int[addEd];
			do {
				for (int j = 0; j < indexes.length; j++) {
					indexes[j] = getLayer();
				}
				Arrays.sort(indexes);
				// на основе индексов обращаюсь к матрице и формирую список
				// ребер дл€ выбора
				List<E> listLink_ = new LinkedList();
				for (int j = 0; j < indexes.length - 1; j++) {
					for (int k = j + 1; k < indexes.length; k++) {
						Para pr = new Para(indexes[j], indexes[k]);
						if (pair_matrix.get(pr) != null
								&& pair_matrix.get(pr).size() > 0)
							listLink_.addAll(pair_matrix.get(pr));
					}

				}

				n1 = null;
				n2 = null;
				if ((mRand.nextDouble()<=lyamda)&&(listLink_.size() > 0)) {
			//	if ((listLink_.size() > 0)) {
					p = listLink_.get(mRand.nextInt(listLink_.size()));
					if (p == null)
						System.out.println("ff");
					Pair<V> par = graph.getEndpoints(p);
					int i1 = graph.degree(par.getFirst());
					int i2 = graph.degree(par.getSecond());
					boolean b1 = true, b2 = true;
					for (int j = 0; j < indexes.length; j++) {
						if (indexes[j] == i1 && b1) {
							k1 = j;
							b1 = false;
							continue;
						}
						if (indexes[j] == i2 && b2) {
							k2 = j;
							b2 = false;
							continue;
						}
					}
				} else {
					int i1 = mRand.nextInt(indexes.length);
					int i2 = i1;
					do
						i2 = mRand.nextInt(indexes.length);
					while (i2 == i1);

					k1 = i1;
					k2 = i2;
					n1 = map.get(indexes[i1]).get(
							mRand.nextInt(map.get(indexes[i1]).size()));
					n2 = map.get(indexes[i2]).get(
							mRand.nextInt(map.get(indexes[i2]).size()));
				}
				listLink_.clear();
			} while ((p == null) && (n1 == n2));

			Pair<V> pair;
			if (p != null) {
				pair = graph.getEndpoints(p);
			} else
				pair = new Pair(n1, n2);
			// --------------------------------------------------------------------------
			Set<E> setLink = new HashSet();
			List<V> listNodes = new LinkedList();

			listNodes.add(pair.getFirst());
			listNodes.add(pair.getSecond());

			// формирую список узлов
			for (int j = 0; j < indexes.length; j++) {
				if (j != k1 && j != k2)
					listNodes.add(map.get(indexes[j]).get(
							mRand.nextInt(map.get(indexes[j]).size())));
			}

			// формирую список рЄбер
			for (Iterator iterator = listNodes.iterator(); iterator.hasNext();) {
				V myNode = (V) iterator.next();
				Collection<E> list_j = graph.getIncidentEdges(myNode);
				for (Iterator<E> it = list_j.iterator(); it.hasNext();) {
					E link = it.next();
					setLink.add(link);
				}
			}

			// удал€ю из списка вершин
			for (Iterator iterator = listNodes.iterator(); iterator.hasNext();) {
				V myNode = (V) iterator.next();
				map.get(graph.degree(myNode)).remove(myNode);
			}
			// удал€ю из списка рЄбер
			for (E l0 : setLink) {
				Pair<V> pa = graph.getEndpoints(l0);
				int min = graph.degree(pa.getFirst());
				int max = graph.degree(pa.getSecond());
				Para temp = null;
				if (min > max)
					temp = new Para(max, min);
				else
					temp = new Para(min, max);
				pair_matrix.get(temp).remove(l0);
			}

			// --------------------------------------------------------------------------
			// добавить цикл по всему списку новых рЄбер

			V new_n = vertexFactory.create();

			// добавл€ю рЄбра
			Map<E, V> newEdges = new HashMap<E, V>();
			for (Iterator iterator = listNodes.iterator(); iterator.hasNext();) {
				V myNode = (V) iterator.next();
				E l1 = edgeFactory.create();
				newEdges.put(l1, myNode);
				graph.addEdge(l1, new_n, myNode);
			}
			// переношу в соответв слой новую вершину

			// addToLayer(, addEd);
			// добавить цикл по всему списку новых вершин

			listNodes.add(new_n);
			for (Iterator iterator = listNodes.iterator(); iterator.hasNext();) {
				V myNode = (V) iterator.next();
				addToLayer(myNode, graph.degree(myNode));
			}

			// добавить цикл по всему списку новых вершин
			for (E li : newEdges.keySet()) {
				add2pair(addEd, graph.degree(newEdges.get(li)), li);
			}

			for (E l0 : setLink) {
				Pair<V> pa = graph.getEndpoints(l0);
				int min = graph.degree(pa.getFirst());
				int max = graph.degree(pa.getSecond());

				if (min > max)
					add2pair(max, min, l0);
				else
					add2pair(min, max, l0);
			}
			// tr = tr + getTriang(list);

		}
		return graph;
	}

	private void add2pair(int min, int max, E link) {
		// TODO Auto-generated method stub
		Para para = new Para(min, max);
		List<E> p = pair_matrix.get(para);
		if (p == null) {
			p = new ArrayList();
			pair_matrix.put(para, p);
		}
		p.add(link);
	}

	// -----------------

	private int getTriang(List<V> list) {
		int r = 0;
		for (V myNode : list) {
			for (V myNode2 : list) {
				if (graph.isNeighbor(myNode, myNode2))
					r++;
			}

		}
		return r / 2;
	}

	private int getLayer() {
		// разыгрываем
		int k = 0;
		double rand = mRand.nextDouble();
		double tr = 0;
		// считаем знаменатель
		double sum = 0.0;
		for (int op : map.keySet())
			sum = sum +  attachRule.f(op) * map.get(op).size();
		for (int l : map.keySet()) {
			int A = map.get(l).size();
			tr = tr + ((double) A *  attachRule.f(l)) / sum;
			if (rand < tr) {
				k = l;
				break;
			}
		}
		if(k==0){
			new Exception("Big numbers");
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
		//создаЄм граф затравки
		Graph graph = new UndirectedSparseMultigraph();
		
		// инициализаци€ полносв€зный граф
		//map.put(3, new LinkedList());
		for (int i = 0; i < 3; i++) {
			Integer n = nodeFactory.create();
			graph.addVertex(n);
			//List<V> list = map.get(addEd);
			//list.add(n);
		}
		//int l = 0;
		Object[] mass2 = graph.getVertices().toArray();
		for (int i = 0; i < mass2.length - 1; i++)
			for (int j = i + 1; j < mass2.length; j++) {
				Integer link = edgeFactory.create();
				graph.addEdge(link, (Integer) mass2[i], (Integer) mass2[j],
						EdgeType.UNDIRECTED);
				//add2pair(addEd, addEd, link);
			}
		double [] mass =new double[]{0,0,1.0};
		PrefferentialAttachment prefA = new PrefferentialAttachment(){
			@Override
			public double f(int k) {
				// TODO Auto-generated method stub
				return k;
			}
			@Override
			public int getM() {
				// TODO Auto-generated method stub
				return 0;
			}
		};
		GeneratorClastPA gen = new GeneratorClastPA<Integer,Integer>(nodeFactory, edgeFactory, mass, prefA);
		gen.evolve(1000, 1.,graph);
		gen.step_svyaz(graph, 5);
	}
	 void step_svyaz(Graph graph, int length){
	       System.out.println("выводим степени св€зности");

	      Iterator<V> it = graph.getVertices().iterator();
	      long c1 = 1;
	      
	      int[] distr0= new int[length];
	      int[] distr1= new int[length];
	      while (it.hasNext()) {
	          V node = it.next();
	          //int[] mass = getR2R2(node);
	          int mass[]={graph.degree(node),0};
	          if(mass[0]<length)distr0[mass[0]]=distr0[mass[0]]+1;
	          if(mass[1]<length)distr1[mass[1]]=distr1[mass[1]]+1;
	      }
	      for(int i=0; i<distr0.length; i++)
	          System.out.println(distr0[i]);
	  }
}
