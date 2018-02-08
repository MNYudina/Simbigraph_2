package simbigraph.stat.algorithms.subgraph;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.stat.algorithms.subgraph.ParallelDirFourSizeSubgraphsCounterSampling.EdgeLayerParameters;
import simbigraph.stat.algorithms.subgraph.ParallelDirThreeSizeSubgraphsCounterSampling.VertexLayerParameters;

/**
   * @author Yudin Evgeniy

 */
public class DirThreeSizeSubgraphsCounterSampling<V, E> {

	private Graph<V, E> graph;

	private Map<Integer, VertexLayerParameters<V>> vertexLayers;

	/**
	 * Constructs and initializes the class.
	 *
     * @author Yudina Maria, Yudin Evgeniy
	 * @param graph
	 *            the graph
	 * @param vertexLayers
	 *            layers of the vertices
	 */
	public DirThreeSizeSubgraphsCounterSampling(Graph<V, E> graph, Map<Integer, VertexLayerParameters<V>> vertexLayers) {
		this.graph = graph;
		this.vertexLayers = vertexLayers;
	}

	/**
	 * Chooses one of the <code>graph</code>'s "forks" randomly and tests if it
	 * is a <code>graph</code>'s "triangle".
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @return 1 if "triangle" was explored or 0 if "fork" was explored
	 */
	public int doRun() {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = null;

		// Choose a layer of vertices taking into account the probabilities of
		// layers selection
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}

		// Choose a vertex from the layer of vertices randomly
		V selectedVertex = selectedVertexLayer.getVerticies()
				.get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));

		// Choose 2 successors of the vertex randomly
		List<V> selectedVertexSuccessorsList = new LinkedList<>(graph.getSuccessors(selectedVertex));
		int randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor1 = selectedVertexSuccessorsList.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V successor2 = selectedVertexSuccessorsList.remove(randomIntValue);

		if (graph.getSuccessors(successor1).contains(successor2)) {
			return 1;
		} else {
			return 0;
		}
	}

	/**
	 * Chooses one of the <code>graph</code>'s path of length three randomly and
	 * determines which type of subgraph it is.
	 * 
	 * @author Yudin Evgeniy 
	 * @return 1,2,3,4,5,7,8,9,10,11,12,13 if corresponding 3 subgraph was explored in oriented graph
	 */
	public int searchOrientedTypesOfSubgraphs() {
		Random randomGenerator = new Random();
		double randomDoubleValue = randomGenerator.nextDouble();
		while (randomDoubleValue == 0.0) {
			randomDoubleValue = randomGenerator.nextDouble();
		}
		double borderOfProbability = 0.0;
		VertexLayerParameters<V> selectedVertexLayer = null;
		
		// Choose a layer of vertices taking into account the probabilities of layers selection
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			borderOfProbability += vertexLayer.getValue().getProbability();
			//if(borderOfProbability<0)
			//	System.out.println(borderOfProbability);
			if (randomDoubleValue < borderOfProbability) {
				selectedVertexLayer = vertexLayer.getValue();
				break;
			}
		}
		
		// Choose a vertex v1 from the layer of vertices randomly
		V v1 = selectedVertexLayer.getVerticies().get(randomGenerator.nextInt(selectedVertexLayer.getVerticies().size()));
		
		// Choose 2 neubor of the vertex randomly  - v2,v3
		/*List<V> selectedVertexSuccessorsList = new LinkedList<>(graph.getSuccessors(selectedVertex));
		int randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V v2 = selectedVertexSuccessorsList.remove(randomIntValue);
		randomIntValue = randomGenerator.nextInt(selectedVertexSuccessorsList.size());
		V v3 = selectedVertexSuccessorsList.remove(randomIntValue);*/
		List<E> edges = new ArrayList(graph.getIncidentEdges(v1));
		List<E> tempL = new LinkedList(edges);
		E i = tempL.remove(randomGenerator.nextInt(tempL.size()));
		E j = tempL.remove(randomGenerator.nextInt(tempL.size()));
		V v2 = graph.getOpposite(v1, i);
		V v3 = graph.getOpposite(v1, j);

		
		//choose pattern
		if ((graph.getEdgeType(j).equals(EdgeType.UNDIRECTED))
				&& (graph.getEdgeType(i).equals(EdgeType.UNDIRECTED))) // это
																		// ребра
		{
			if (!graph.isNeighbor(v2, v3))
				return 1;//c1++;

			else {
				E o = graph.findEdge(v2, v3);
				if (o == null)
					o = graph.findEdge(v3, v2);
				if (graph.getEdgeType(o).equals(EdgeType.UNDIRECTED))
					return 7;//c7++;
				else
					return 10;//c10++;

			}
		} 
		else if ((graph.getEdgeType(j).equals(EdgeType.UNDIRECTED))// одна из связей - ребро
				|| (graph.getEdgeType(i).equals(EdgeType.UNDIRECTED)))
		{
			E l = null, e = null;
			//  определяю где ребро, а где дуга
			if (graph.getEdgeType(j).equals(EdgeType.UNDIRECTED)) {
				e = j;
				l = i;
			} else {
				e = i;
				l = j;
			}

 			V end_l = graph.getOpposite(v1, l);
			V end_e = graph.getOpposite(v1, e);

			Object dest = graph.getDest(l);
			if (dest == end_l) {
				if (!graph.isNeighbor(v2, v3))
					return 2;//c2++;
				else {
					E o1 = graph.findEdge(end_l, end_e);
					if (o1==null)o1 = graph.findEdge( end_e, end_l);
					
					
					if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED))
					{
						V dest2 = graph.getDest(o1);
						if(dest2==end_l) return 12;//c12++;
						else return 8;// c8++;
						
					}
					else return 10;//c10++;
				}
			} else {
				if (!graph.isNeighbor(v2, v3))
					return 4;//c4++;
				else {
					E o1 = graph.findEdge(end_l, end_e);
					if (o1==null)o1 = graph.findEdge( end_e, end_l);
					
					
					if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED))
					{
						Object dest2 = graph.getDest(o1);
						if(dest2==end_l)return 8;//c8++;
						else return 9;// c9++;
						
					}
					else return 10;//c10++;
				}
				// если основа - дуги
							}
		}
		else if((graph.getSource(i)==v1)&&(graph.getSource(j)==v1))
		{//обе дуги - исходящие
			
				if (!graph.isNeighbor(v2, v3))
					return 3;//c3++;
				else{
					E o1 = graph.findEdge(v2, v3);
					if (o1==null)o1 = graph.findEdge( v3, v2);
					if (graph.getEdgeType(o1).equals(EdgeType.UNDIRECTED))return 9; else return 11;//c9++; else c11++;
				}
		}	
			
		else if((graph.getDest(i)==v1)&&(graph.getDest(j)==v1))
			{// обе дуги- входящие
				if (!graph.isNeighbor(v2, v3))
					return 5;//c5++;
				else{
					E o1 = graph.findEdge(v2, v3);
					if (o1==null)o1 = graph.findEdge( v3, v2);
					if (graph.getEdgeType(o1).equals(EdgeType.UNDIRECTED))return 12; else return 11;//c12++; else c4++;
				}
				
			}
			else{
				E in = null, out = null;
				//  определяю где входящая, а где исходящая дуга
				if (graph.getSource(i)==v1) {
					out = i;
					in = j;
				} else {
					out = j;
					in = i;
				}
				
				V end_in = graph.getOpposite(v1, in);
				V end_out = graph.getOpposite(v1,out);
				if (!graph.isNeighbor(v2, v3))
						return 6;//c6++;
					else {
						E o1 = graph.findEdge(end_in, end_out);
						if (o1==null)o1 = graph.findEdge( end_out, end_in);
						
						
						if (graph.getEdgeType(o1).equals(EdgeType.DIRECTED))
						{
							Object dest2 = graph.getDest(o1);
							if(dest2==end_in)return 13;//c13++;
							else return 11;//c4++;
							
						}
						else return 8;//c8++;
					}
			
		
			
				
			}
	
		//System.out.println("fff");
		//return 0; //Этого произойти не должно	
	}
	
}