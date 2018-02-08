package simbigraph.util;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.commons.collections15.Buffer;
import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.buffer.UnboundedFifoBuffer;
import org.xml.sax.SAXException;



import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.io.GraphMLWriter;
import edu.uci.ics.jung.io.PajekNetReader;

public class Statistic {
	
	
	/*
	 * —читает в графе число треугольников и вилок
	 * ”читывает пометки на узлах подграфа метода Mark

	 */


	public static int[] getTriAndVilk(Graph graph) {
		// перебираем все вершины
		int count = 0;
		int count2 = 0;
		final Collection list = graph.getVertices();

		/*Timer tim =new Timer(100,new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("fff");
				System.out.println("%"+kol/(double)list.size());
			}
			
		});
		
		tim.start();*/
		int kol=0;

		for (Object node : list) {
			kol++;
			if(kol%10000==0)System.out.println("%"+kol/(double)list.size());
			int k = graph.degree(node);
			for (Object link : graph.getIncidentEdges(node)) {
					k++;
			}
			count2 = count2 + k * (k - 1) / 2;


			
			Collection<Object> neig_s = graph.getNeighbors(node);
			Iterator<Object> it1 = neig_s.iterator();

		     Collection<Object> neig=graph.getNeighbors(node);
	            Iterator<Object> it11 =neig.iterator();
	            while(it11.hasNext()){
	            	Object node1=it11.next();
	                Iterator<Object> it2 =neig.iterator();
	                while(it2.hasNext()){
	                	Object node2=it2.next();
	                    if((node1!=node2)&&graph.isNeighbor(node1, node2))
	                    count++;
	                }
	            }
	            
		}
		//tim.stop();
		return new int[] { count / 6, count2 };
	}
	public static int[] getTriAndVilk2(Graph<Object, Object> graph) {
		// перебираем все вершины
		int count = 0;
		int count2 = 0;
		Collection<Object> list = graph.getVertices();

		for (Object node : list) {

			int k = 0;
			for (Object link : graph.getIncidentEdges(node)) {
				//if (!graph.getOpposite(node, link).isMark())
					k++;
			}
			count2 = count2 + k * (k - 1) / 2;

			Collection neig_s = graph.getNeighbors(node);
			Iterator it1 = neig_s.iterator();

			//if (!node.isMark())
				while (it1.hasNext()) {
					Object node1 = it1.next();
					Iterator it2 = neig_s.iterator();
					//if (!node1.isMark())
						while (it2.hasNext()) {
							Object node2 = it2.next();
							if (
									//(!node2.isMark()) && 
									(node1 != node2)
									&& graph.isNeighbor(node1, node2))
								count++;
						}
				}
		}
		return new int[] { count / 6, count2 };
	}
	public static int getTriangles(Graph graph) {
        //перебираем все вершины
        int count = 0;
        Collection list =graph.getVertices();
        for (Object Node : list) {
        //у каждой верщины считаем соседей, имеющих общее ребро
            Collection neig_s=graph.getNeighbors(Node);
            Iterator it1 =neig_s.iterator();
            while(it1.hasNext()){
            	Object node1=it1.next();
                Iterator it2 =neig_s.iterator();
                while(it2.hasNext()){
                	Object node2=it2.next();
                    if((node1!=node2)&&graph.isNeighbor(node1, node2))
                    count++;
                }
            }
        }
            //System.out.println("“реугольников="+count/3);
        return count/6;
    }

	
	/**
	 * 
	 * @param old_graph
	 * @param size
	 * @return TettaMatrix is matrix of edges  with difined size 
	 */
	public static int[][] getTettaMatrix(Graph graph, int size) {
        int[][] ret = new int[size][size]; 
        Collection list =graph.getEdges();
        for (Object edge : list) {
        	Pair p= graph.getEndpoints(edge);
        	Object n1= p.getFirst();
        	Object n2= p.getSecond();
            if(graph.degree(n1)>graph.degree(n2))
            		{Object n3=n1;
            			n1=n2;
            			n2=n3;}
            int degree_n1=graph.degree(n1);
            int degree_n2=graph.degree(n2);
            if(degree_n1<size&&degree_n2<size)
            {
            	ret[degree_n1][degree_n2]=ret[degree_n1][degree_n2]+1;
            	ret[degree_n2][degree_n1]=ret[degree_n2][degree_n1]+1;

            }
        }
    return ret;
	}
	
	
	/*public static void markNodesLessThen(int minDegree,
			Graph<Object, Object> graph) {
		Collection<Object> list = graph.getVertices();
		boolean b;
		do {
			b = false;
			for (Object node : list) {
				if (!node.isMark()) {
					int k = 0;
					for (Iterator iterator = graph.getIncidentEdges(node)
							.iterator(); iterator.hasNext();) {
						Object link = iterator.next();
						if (!graph.getOpposite(node, link).isMark())
							k++;
					}
					if (k < minDegree) {
						node.setMark(true);
						b = true;
					} else
						node.setMark(false);
				}
			}
		} while (b);
	}*/

	// -------------------------------------------
	/*public static void markNodesLessThen2(int minDegree,
			Graph<Object, Object> graph) {
		Collection<Object> list = graph.getVertices();
		List<Object> list2 = new LinkedList();
		for (Object node : list) {
			if (graph.degree(node) < minDegree)
				node.setMark(true);
			else
				node.setMark(false);
		}
	}*/

	public static void removeNodesLessThen(int minDegree,
			Graph<Object, Object> graph) {
		boolean b = true;
		do {
			b = false;
			Collection<Object> list = graph.getVertices();
			List<Object> list2 = new LinkedList();

			for (Object node : list) {
				if (graph.degree(node) < minDegree)
					list2.add(node);
			}
			for (Object node : list2) {
				graph.removeVertex(node);
				b=true;
			}
			if (b==false) break;
		} while (true);

	}

	public static void writeNodesDegrees(Graph<Object, Object> graph, int length) {
		System.out.println("выводим степени св€зности");

		Iterator<Object> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		int[] distr1 = new int[length];
		while (it.hasNext()) {
			Object node = it.next();

		//	if (!node.isMark())
			{
				int mass[] = { graph.degree(node), 0 };
				if (mass[0] < length)
					distr0[mass[0]] = distr0[mass[0]] + 1;
				if (mass[1] < length)
					distr1[mass[1]] = distr1[mass[1]] + 1;
			}
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
	}

	public static Set<Set<Object>> getClusters(Graph<Object,Object> graph) {

        Set<Set<Object>> clusterSet = new HashSet<Set<Object>>();

        HashSet<Object> unvisitedVertices = new HashSet<Object>(graph.getVertices());

        while (!unvisitedVertices.isEmpty()) {
        	Set<Object> cluster = new HashSet<Object>();
        	Object root = unvisitedVertices.iterator().next();
            unvisitedVertices.remove(root);
            cluster.add(root);

            Buffer<Object> queue = new UnboundedFifoBuffer<Object>();
            queue.add(root);

            while (!queue.isEmpty()) {
            	Object currentVertex = queue.remove();
                Collection<Object> neighbors = graph.getNeighbors(currentVertex);

                for(Object neighbor : neighbors) {
                    if (unvisitedVertices.contains(neighbor)) {
                        queue.add(neighbor);
                        unvisitedVertices.remove(neighbor);
                        cluster.add(neighbor);
                    }
                }
            }
            clusterSet.add(cluster);
        }
        return clusterSet;
    }

	public static int getVilk(Graph graph) {
		int count2 = 0;
		Collection list = graph.getVertices();
		for (Object node : list) {
			int k = graph.degree(node);
			//int k = old_graph.getNeighborCount(node);
			count2 = count2 + k * (k - 1) / 2;
			}
		return count2;
	}

	
	
	
	
	
	private static List mEdgesRemoved = new ArrayList();
	private static List mNodesRemoved = new ArrayList();
	static Map<Object, Pair<Object>> removedEdges = new HashMap<Object, Pair<Object>>();
	public static double[] percolationEdges(Graph gr, int iter, double start_prob,
			double end_prob, double step_prob)
	{
		double[] ret = new double[(int) (1+Math.ceil((end_prob-start_prob)/step_prob))];
		int c=0;
		for (double p = start_prob; p <= end_prob; p = p + step_prob) {
			int ch = 0;
			int s_ns = 0;
			for (int i = 0; i < iter; i++) {
				removeEdges(gr, p);
				Set<Set<Object>> clusterSet = Statistic.getClusters(gr);
				// int s_ns1 = clusterSet.size();
				int max = 0;
				for (Set<Object> set : clusterSet) {
					if (max < set.size())
						max = set.size();
				}
				ch = ch + max;
				restoreNodes(gr);
			}
			ret[c]= (1.0 * ch) / (1.0*iter);
			c++;
		}
		return ret;
	}

	private static void removeEdges(Graph graph, double p) {
		Set setLink = new HashSet();
		Iterator it_e = graph.getEdges().iterator();

		while (it_e.hasNext()) {
			Object edge = it_e.next();

			if (Math.random() < p) {
				Pair removedEdgeEndpoints = graph.getEndpoints(edge);
				removedEdges.put(edge, removedEdgeEndpoints);
				mEdgesRemoved.add(edge);
			}
		}
		for (Object edge : mEdgesRemoved)
			graph.removeEdge(edge);
	}
	private static void restoreNodes(Graph graph) {
		for (Object node : mNodesRemoved) {
			graph.addVertex(node);
		}
		for (Object edge : mEdgesRemoved) {
			Pair endpoints = removedEdges.get(edge);
			graph.addEdge(edge, endpoints.getFirst(), endpoints.getSecond());
		}
		mNodesRemoved.clear();
		mEdgesRemoved.clear();
		removedEdges.clear();
	}

	public static double[]  percolationNode(Graph gr, int iter, double start_prob,
			double end_prob, double step_prob) {
		double[] ret = new double[(int) (1+Math.ceil((end_prob-start_prob)/step_prob))];
		int c=0;

		for (double p = start_prob; p <= end_prob; p = p + step_prob) {
			int ch = 0;
			int s_ns = 0;

			for (int i = 0; i < iter; i++) {
				Map<Integer, Integer> map = new HashMap();
				removeNodes(gr, p);
				Set<Set<Object>> clusterSet = Statistic.getClusters(gr);
				int max = 0;
				for (Set<Object> set : clusterSet) {
					if (max < set.size())
						max = set.size();
				}
				ch = ch + max;
				restoreNodes(gr);
			}
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			format.setMaximumFractionDigits(8);
			format.setMinimumFractionDigits(4);
			ret[c]= (1.0 * ch) / (1.0*iter);
			c++;
		}
		return ret;

	}

	private static void removeNodes(Graph graph, double p) {
		Iterator it_e = graph.getVertices().iterator();
		while (it_e.hasNext()) {
			Object node = it_e.next();
			if (Math.random() < p) {
				for (Object removedEdge : graph.getIncidentEdges(node)) {
					Pair removedEdgeEndpoints = graph
							.getEndpoints(removedEdge);
					removedEdges.put(removedEdge, removedEdgeEndpoints);
					mEdgesRemoved.add(removedEdge);
					// удал€ем рЄбра
					// old_graph.removeEdge(removedEdge);

					// edges.setInUse(true);
				}
				mNodesRemoved.add(node);
			}
		}
		// действительно удал€ем
		for (Object edge : mEdgesRemoved)
			graph.removeEdge(edge);

		for (Object nod : mNodesRemoved)
			graph.removeVertex(nod);
	}
	
	
	
	private static void removeBigNodes(final Graph graph, double p) {
		//выдел€ю множество вершин дл€ удалени€
		List list = new ArrayList();
		list.addAll(graph.getVertices());
		Collections.sort(list,new Comparator(){
			@Override
			public int compare(Object o1, Object o2) {
				return graph.degree(o2)-graph.degree(o1);
			}
			
		});
		
		
		///удал€ю
		Iterator it_e = list.iterator();
		int ch=0; int numDelVirt = (int) (graph.getVertexCount()*p);
		while (it_e.hasNext()) {
			Object node = it_e.next();
			if(++ch>numDelVirt)break;
				for (Object removedEdge : graph.getIncidentEdges(node)) {
					Pair removedEdgeEndpoints = graph
							.getEndpoints(removedEdge);
					removedEdges.put(removedEdge, removedEdgeEndpoints);
					mEdgesRemoved.add(removedEdge);
					// удал€ем рЄбра
					// old_graph.removeEdge(removedEdge);

					// edges.setInUse(true);
				}
				mNodesRemoved.add(node);
		}
		// действительно удал€ем
		for (Object edge : mEdgesRemoved)
			graph.removeEdge(edge);

		for (Object nod : mNodesRemoved)
			graph.removeVertex(nod);
	}
	public static double[] percolationNode2(Graph gr, int iter, double start_prob,
			double end_prob, double step_prob) {
		double[] ret = new double[(int) (1+Math.ceil((end_prob-start_prob)/step_prob))];
		int c=0;

		for (double p = start_prob; p <= end_prob; p = p + step_prob) {
			int ch = 0;
			int s_ns = 0;

			for (int i = 0; i < iter; i++) {
				Map<Integer, Integer> map = new HashMap();
				removeBigNodes(gr, p);
				Set<Set<Object>> clusterSet = Statistic.getClusters(gr);
				int max = 0;
				for (Set<Object> set : clusterSet) {
					if (max < set.size())
						max = set.size();
				}
				ch = ch + max;
				restoreNodes(gr);
			}
			NumberFormat format = NumberFormat.getInstance(Locale.getDefault());
			format.setMaximumFractionDigits(8);
			format.setMinimumFractionDigits(4);
			ret[c]= (1.0 * ch) / (1.0*iter);
			c++;
		}
		return ret;
	}
	

}
