package simbigraph.stat.algorithms.subgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.stat.exception.GraphStatsException;
import simbigraph.stat.exception.UnsupportedEdgeTypeException;

/**
 * This is parallel version of 4-size directed subgraphs counter which uses
 * random carcasses sampling algorithm.
 * 
 * @author Yudina Maria, Yudin Evgeniy
 */
public class ParallelDirFourSizeSubgraphsCounterSampling<V, E>  {

	
	int[] massKoef={0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 1, 1, 2, 2, 4, 6, 6, 0, 0, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 0, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 2, 4, 6, 4, 6, 2, 2, 6, 6, 6, 6, 1, 2, 2, 2, 4, 6, 4, 6, 6, 6, 6, 4, 6, 6, 1, 2, 6, 2, 6, 6, 6, 12, 12, 6, 6, 12, 12, 12, 12, 12, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 2, 0, 1, 2, 2, 2, 1, 2, 2, 4, 6, 4, 6, 6, 6, 6, 4, 6, 6, 2, 2, 2, 2, 2, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 12, 6, 12, 12, 12, 12, 12, 6, 6, 6, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 12, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 6, 6, 6, 6, 4, 6, 6, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};
	private Graph<V, E> graph;

    private int numberOfRuns;
    private int numberOfRunsLapka; 
    private int numberOfRunsScoba; 

    private int numberOfThreads;
    
    private long exploredNumberOfSubgraphs4_1, exploredNumberOfSubgraphs4_2, exploredNumberOfSubgraphs4_3, exploredNumberOfSubgraphs4_4, exploredNumberOfSubgraphs4_5, exploredNumberOfSubgraphs4_6;
	final long motifs[]= new long[100];

    private int approximateNumberOfSubgraphs4_1, approximateNumberOfSubgraphs4_2, approximateNumberOfSubgraphs4_3, approximateNumberOfSubgraphs4_4, approximateNumberOfSubgraphs4_5, approximateNumberOfSubgraphs4_6;
    
    List<Integer> resultsList;
    List<Integer> resultsList2;
    
	double exactNumberOfSubgraphs4_1 = 0;
	long exactNumberOfPathsOfLengthThree = 0;

	/**
	 * This nested static class is used to store parameters of each layer of the
	 * vertices such as probability of selection and list of vertices.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 */
    protected static class VertexLayerParameters<V> {
		
		private double probability;
		
		private List<V> vertices = new ArrayList<>();
		
		public VertexLayerParameters() {
			vertices = new ArrayList<>();
		}
		
		public double getProbability() {
			return probability;
		}

		public List<V> getVerticies() {
			return vertices;
		}
		
	}
    
	/**
	 * This nested static class is used to store parameters of each layer of the
	 * edges such as probability of selection and list of edges.
	 * 
	 * @author Yudina Maria, Yudin Evgeniy
	 */
    protected static class EdgeLayerParameters<E> {
		
		private double probability;
		
		private List<E> edges = new ArrayList<>();
		
		public EdgeLayerParameters() {
			edges = new ArrayList<>();
		}
		
		public double getProbability() {
			return probability;
		}

		public List<E> getEdges() {
			return edges;
		}
		
	}
	
	/**
     * Constructs and initializes the class.
     *
     * @author Yudina Maria, Yudin Evgeniy
     * @param graph the graph
     * @param numberOfRuns number of runs of sampling algorithm 
     * @param numberOfThreads number of parallel threads
     */
	public ParallelDirFourSizeSubgraphsCounterSampling(Hypergraph<V, E> graph, int numberOfRuns, int numberOfThreads) {
        this.graph = (Graph<V, E>) graph;
        this.numberOfRuns = numberOfRuns;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * Saves number of explored <code>graph</code>'s subgraphs4_1 into
	 * <code>exploredNumberOfSubgraphs4_1</code> variable.<br>
	 * Saves results of runs of the algorithm into <code>resultsList</code>.<br>
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @throws UnsupportedEdgeTypeException
	 */
	public void execute() throws GraphStatsException {
		if (true) {
			oriented4motif();
			return;
		}
	}

	private void oriented4motif() {
		Collection<V> vertices = graph.getVertices();
		Map<Integer, VertexLayerParameters<V>> vertexLayers = new HashMap<>();
		int numberOfVertexSuccessors;
		
		/* 
		 * Bind each vertex of the graph to one layer of the vertices
		 * defined by number of successors of the vertex.
		 */
		for (V vertex : vertices) {
    		numberOfVertexSuccessors = graph.getNeighborCount(vertex);
    		if (vertexLayers.get(numberOfVertexSuccessors) == null) {
    			vertexLayers.put(numberOfVertexSuccessors, new VertexLayerParameters<>());
    		}
    		vertexLayers.get(numberOfVertexSuccessors).vertices.add(vertex);
		}
		
		
		// Calculate exact number of the graph's subgraphs4_1
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			exactNumberOfSubgraphs4_1 += vertexLayer.getValue().vertices.size() * vertexLayer.getKey()/ 6. * (vertexLayer.getKey() - 1) * (vertexLayer.getKey() - 2) ;
		}
		
		// Calculate probability of selection for each layer of the vertices
    	for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
    		vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size()/ (6.0 * exactNumberOfSubgraphs4_1) * vertexLayer.getKey() * (vertexLayer.getKey() - 1) * (vertexLayer.getKey() - 2)) ;
    		//if(vertexLayer.getValue().probability<=0) System.out.println("FF:"+vertexLayer.getValue().probability);
    	}

    	Collection<E> edges = graph.getEdges();
		Map<Integer, EdgeLayerParameters<E>> edgeLayers = new HashMap<>();
		int numberOfPathsOfLengthThree;
		
		/* 
		 * Bind each edge of the graph to one layer of the edges
		 * defined by number of path of length 3 including the edge.
		 */
		for (E edge : edges) {
			V v1 = graph.getEndpoints(edge).getFirst();
			V v2 = graph.getEndpoints(edge).getSecond();
			numberOfPathsOfLengthThree = (graph.getNeighborCount(v1) - 1) * (graph.getNeighborCount(v2) - 1);
			if (edgeLayers.get(numberOfPathsOfLengthThree) == null) {
				edgeLayers.put(numberOfPathsOfLengthThree, new EdgeLayerParameters<>());
    		}
			edgeLayers.get(numberOfPathsOfLengthThree).edges.add(edge);
		}
		
		// Calculate exact number of the graph's path of length three
		for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
			exactNumberOfPathsOfLengthThree += edgeLayer.getValue().edges.size() * edgeLayer.getKey();
		}
		
		// Calculate probability of selection for each layer of the edges
    	for (Entry<Integer, EdgeLayerParameters<E>> edgeLayer : edgeLayers.entrySet()) {
    		edgeLayer.getValue().probability = (edgeLayer.getValue().edges.size() / (double)exactNumberOfPathsOfLengthThree* edgeLayer.getKey()) ;
    	}
    	
    	DirFourSizeSubgraphsCounterSampling<V, E> counter = new DirFourSizeSubgraphsCounterSampling<>(graph, vertexLayers, edgeLayers);

    	
    	 numberOfRunsLapka = (int)(numberOfRuns*(exactNumberOfPathsOfLengthThree)/((double)(exactNumberOfSubgraphs4_1+exactNumberOfPathsOfLengthThree)));
    	 numberOfRunsScoba =numberOfRuns-numberOfRunsLapka;
    	
    	List<Integer> resultsOfRunsScobka = new ArrayList<>(numberOfRunsScoba);
    	List<Integer> resultsOfRunsLapka = new ArrayList<>(numberOfRunsLapka);

    	for (int i = 0; i < numberOfRunsScoba; i++) {
    		resultsOfRunsScobka.add(0);
		}

    	for (int i = 0; i < numberOfRunsLapka; i++) {
    		resultsOfRunsLapka.add(0);
		}

    	ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
    	try {
			forkJoinPool.submit(() -> resultsList2 = resultsOfRunsLapka.stream().parallel().map(resultOfRun2 -> counter.searchOtherTypesOf4SubgraphsLapka()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)).get();
			forkJoinPool.submit(() -> resultsList = resultsOfRunsScobka.stream().parallel().map(resultOfRun3-> counter.searchOtherTypesOf4SubgraphsScobka()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)).get();
			resultsList.addAll(resultsList2);
			for (int i = 0; i < motifs.length; i++) {
				final int num=i;
				forkJoinPool.submit(() -> motifs[num] = resultsList.stream().parallel().filter(result -> result == num).count()).get();
			}
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
    	    	   	
	}

	/**
     * @author Yudina Maria, Yudin Evgeniy
	 */
	@Override
    public String toString() {
		int arr_lapkis[] ={3, 7, 24, 8, 25, 26, 76, 92, 93, 94};

		HashSet<Integer> lapkaSet = new HashSet();
		for (int i = 0; i < arr_lapkis.length; i++) {
			lapkaSet.add(arr_lapkis[i]);
		}
		
		String str="";
		for (int i = 0; i < motifs.length; i++) {
			
			str=str+(i)+": " + (!lapkaSet.contains(i)?(double)motifs[i]/numberOfRunsScoba*exactNumberOfPathsOfLengthThree/massKoef[i]:(double)motifs[i]/numberOfRunsLapka*exactNumberOfSubgraphs4_1)+"\n";
		}		

		return str;
    }

}