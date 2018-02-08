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
import org.jfree.data.statistics.Statistics;

import simbigraph.util.Statistic;

public class GeneratorClastPA2<V, E> {

	Para temp = new Para(0, 0);
	Map<Para, List<E>> pair_matrix = new HashMap();
	Map<Integer, List<V>> map = new HashMap();
	// int initN = 4, addEd = 3;
	int countN = 0, avEd = 0, avCount = 0;
	// Graph<V, E> graph;
	int[] massMaxK = new int[100];;
	int[] massVilki = new int[100];;
	protected PrefferentialAttachment attachRule;
	protected Factory<V> vertexFactory;
	protected Factory<E> edgeFactory;
	protected double numEdgesToAttach[];
	Random mRand = new Random();

	public GeneratorClastPA2(Factory<V> vertexFactory, Factory<E> edgeFactory,
			double[] probEdgesToAttach, PrefferentialAttachment attachRule) {
		double s = 0.;
		for (double d : probEdgesToAttach) {
			s = s + d;
		}
		assert Math.abs(s - 1.) < 0.000000001 : "—умма веро€тностей по различным значени€м "
				+ "числа добавл€емых на шаге ребер должна равн€тьс€ 1";

		this.vertexFactory = vertexFactory;
		this.edgeFactory = edgeFactory;
		this.numEdgesToAttach = probEdgesToAttach;
		this.attachRule = attachRule;
		mRand = new Random();
	}

	// ----------------------------------------------------------------------
	public Graph<V, E> evolve(int step, double lyamda, Graph<V, E> graph) {

		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			V v = iterator.next();
			addToLayer(v, graph.degree(v));
		}
		// int l = 0;
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

			double s = 0.;
			double r = mRand.nextDouble();
			int addEd = 0;
			for (int j = 0; j < numEdgesToAttach.length; j++) {
				s = s + numEdgesToAttach[j];
				if (s > r) {
					addEd = j;
					break;
				}
			}
			if (addEd == 0)
				continue;

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
				if ((mRand.nextDouble() <= lyamda) && (listLink_.size() > 0)) {
					// if ((listLink_.size() > 0)) {
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

	private int getLayer() {
		// разыгрываем
		int k = 0;
		double rand = mRand.nextDouble();
		double tr = 0;
		// считаем знаменатель
		double sum = 0.0;
		for (int op : map.keySet())
			sum = sum + attachRule.f(op) * map.get(op).size();
		for (int l : map.keySet()) {
			int A = map.get(l).size();
			tr = tr + ((double) A * attachRule.f(l)) / sum;
			if (rand < tr) {
				k = l;
				break;
			}
		}
		if (k == 0) {
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
		// создаЄм граф затравки
		Graph graph = new UndirectedSparseMultigraph();

		// инициализаци€ полносв€зный граф
		// map.put(3, new LinkedList());
		for (int i = 0; i < 3; i++) {
			Integer n = nodeFactory.create();
			graph.addVertex(n);
			// List<V> list = map.get(addEd);
			// list.add(n);
		}
		// int l = 0;
		Object[] mass2 = graph.getVertices().toArray();
		for (int i = 0; i < mass2.length - 1; i++)
			for (int j = i + 1; j < mass2.length; j++) {
				Integer link = edgeFactory.create();
				graph.addEdge(link, (Integer) mass2[i], (Integer) mass2[j],
						EdgeType.UNDIRECTED);
				// add2pair(addEd, addEd, link);
			}
		double[] mass = new double[] { 0, 0, 1.0 };
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
		GeneratorClastPA2 gen = new GeneratorClastPA2<Integer, Integer>(
				nodeFactory, edgeFactory, mass, prefA);
		gen.evolve(1000, 0., graph);
		System.out.println("до:"+Statistic.getTriangles(graph));
		gen.reconfiguration(graph);
		System.out.println("после:"+Statistic.getTriangles(graph));

		gen.step_svyaz(graph, 10);
	}

	private void reconfiguration(Graph<V, E> graph) {
		map = new HashMap();
		for (Iterator<V> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			V v = iterator.next();
			addToLayer(v, graph.degree(v));
		}

		for (int jj = 0; jj < 33; jj++) {
			// случайный выбор вершины
			V vert = getRanVirt(graph);
			// определение вилки
			Pair<V> vilk = getVilk(vert, graph, map);
			if (vilk == null) {
				jj--;
				continue;
			}
			// определ€ем подход€щее ребро
			E rebro = getPropeEdge(graph, map, vert);
			// присоедин€ем и делаем треугольник
			Pair<V> p = doTriangle(rebro, vert, vilk, graph, map);
			// компенсаци€ передаЄм номера сло€
			compensation(vilk, p, graph, map);

		}

	}

	private void compensation(Pair<V> vilk, Pair<V> p, Graph<V, E> graph,
			Map<Integer, List<V>> map) {
		// случайно берЄм ребро в сло€х вилки
		int n_i = graph.degree(vilk.getFirst());
		int n_k = graph.degree(vilk.getSecond());

		List<V> list_n_i = map.get(n_i);
		List<V> list_n_k = map.get(n_k);
		List<Pair<V>> list = new ArrayList();
		for (V v1 : list_n_i) {
			for (V v2 : list_n_k) {
				if (v1 != v2 && graph.isNeighbor(v1, v2)) {
					// нужно чтобы не уходили треугольники
					Set set = new HashSet();
					set.addAll(graph.getNeighbors(v1));
					if(set.removeAll(graph.getNeighbors(v2)))
					list.add(new Pair<V>(v1, v2));
				}
			}
		}
		Pair<V> new_vilk = list.get(mRand.nextInt(list.size()));

		// случайно берЄм вершины в слое p

		int n_l = graph.degree(p.getFirst());
		int n_m = graph.degree(p.getSecond());

		List<V> list_n_l = map.get(n_i);
		List<V> list_n_m = map.get(n_k);
		List<Pair<V>> list2 = new ArrayList();
		for (V v1 : list_n_l) {
			for (V v2 : list_n_m) {
				if (!graph.isNeighbor(v1, v2)) {
					// нужно чтобы не уходили треугольники
					Set set = new HashSet();
					set.addAll(graph.getNeighbors(v1));
					 if(set.removeAll(graph.getNeighbors(v2)))
					list2.add(new Pair<V>(v1, v2));
				}
			}
		}
		Pair<V> new_p = list2.get(mRand.nextInt(list2.size()));

		// соедин€ем с двум€ узлами ребрама в слое p

		map.get(graph.degree(new_vilk.getFirst())).remove(new_vilk.getFirst());
		map.get(graph.degree(new_vilk.getSecond()))
				.remove(new_vilk.getSecond());
		map.get(graph.degree(new_p.getFirst())).remove(new_p.getFirst());
		map.get(graph.degree(new_p.getSecond())).remove(new_p.getSecond());

		E edge = graph.findEdge(new_vilk.getFirst(), new_vilk.getSecond());
		graph.removeEdge(edge);
		graph.addEdge(edge, new_p.getFirst(), new_p.getSecond());

		// обновл€ем map
		addToLayer(new_vilk.getFirst(), graph.degree(new_vilk.getFirst()));
		addToLayer(new_vilk.getSecond(), graph.degree(new_vilk.getSecond()));
		addToLayer(new_p.getFirst(), graph.degree(new_p.getFirst()));
		addToLayer(new_p.getSecond(), graph.degree(new_p.getSecond()));
	}

	private Pair<V> doTriangle(E rebro, V vert, Pair<V> vilk,
			Graph<V, E> graph, Map<Integer, List<V>> map2) {
		V n1 = vilk.getFirst();
		V n2 = vilk.getSecond();
		Pair<V> p = graph.getEndpoints(rebro);
		V n3 = p.getFirst();
		V n4 = p.getSecond();
		map.get(graph.degree(n1)).remove(n1);
		map.get(graph.degree(n2)).remove(n2);
		map.get(graph.degree(n3)).remove(n3);
		map.get(graph.degree(n4)).remove(n4);

		graph.removeEdge(rebro);
		graph.addEdge(rebro, n1, n2);

		// обновить map
		addToLayer(n1, graph.degree(n1));
		addToLayer(n2, graph.degree(n2));
		addToLayer(n3, graph.degree(n3));
		addToLayer(n4, graph.degree(n4));
		return new Pair<V>(n3, n4);

	}

	private E getPropeEdge(Graph<V, E> graph, Map<Integer, List<V>> map, V vert) {
		E edge = null;
		// ребро случайное но не должно быть частью треугольника
		do {
			int max = graph.getEdgeCount();
			Iterator<E> it = graph.getEdges().iterator();
			for (int i = 0; i < mRand.nextInt(max); i++)
				edge = it.next();
			// провер€ем есть ли общие соседи;
			Pair<V> p = graph.getEndpoints(edge);
			V n1 = p.getFirst();
			V n2 = p.getSecond();
			Set set = new HashSet();
			set.addAll(graph.getNeighbors(n1));
			boolean b = set.removeAll(graph.getNeighbors(n2));
			if (!b && map.get(graph.degree(n1) - 1) != null
					&& map.get(graph.degree(n2) - 1) != null
					&& map.get(graph.degree(n1) - 1).size() > 0
					&& map.get(graph.degree(n2) - 1).size() > 0)
				break;
		} while (true);
		return edge;
	}

	private V getRanVirt(Graph<V, E> graph) {
		V vert = null;
		int max = graph.getVertexCount();
		Iterator<V> it = graph.getVertices().iterator();
		for (int i = 0; i < mRand.nextInt(max); i++) {
			vert = it.next();
		}
		return vert;
	}

	private Pair<V> getVilk(V vert, Graph<V, E> graph, Map<Integer, List<V>> map) {

		E first = null, second = null;
		List<Pair<V>> list = new ArrayList();
		for (E link1 : graph.getIncidentEdges(vert)) {
			for (E link2 : graph.getIncidentEdges(vert)) {
				if (link1 != link2) {

					V node1 = graph.getOpposite(vert, link1);
					V node2 = graph.getOpposite(vert, link2);

					// проверить есть ли сверху ребро
					boolean b = false;
					if (node1!=node2&&!graph.isNeighbor(node1, node2)) {
						List<V> list_n1 = map.get(graph.degree(node1)+1);
						List<V> list_n2 = map.get(graph.degree(node2)+1);
						if(list_n1==null||list_n2==null)return null;

						for (V v1 : list_n1) {
							for (V v2 : list_n2) {
								if (v1 != v2) {
									//есть хоть одно ребро
									b = graph.isNeighbor(v1, v2);
									if (b)
										break;
								}
							}
						}
						if (b)
						{
							list.add(new Pair<V>(node1, node2));
						}
					}
				}
			}
		}
		if (list.size() == 0)
			return null;
		return list.get(mRand.nextInt(list.size()));
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
	void getCountTriangle()
	{
		
		
		
	}
}
