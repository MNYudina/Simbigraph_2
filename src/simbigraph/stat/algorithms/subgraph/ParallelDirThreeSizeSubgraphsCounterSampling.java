package simbigraph.stat.algorithms.subgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.stat.exception.UnsupportedEdgeTypeException;

/**
 * This is parallel version of 3-size undirected subgraphs counter which uses
 * random carcasses sampling algorithm.
 * 
 * @author Yudin Evgeniy
 */
public class ParallelDirThreeSizeSubgraphsCounterSampling<V, E>  {

	private Graph<V, E> graph;

    private long numberOfRuns;

    private int numberOfThreads;
    
    private long exploredNumberOfForks, exploredNumberOfTriangles;
    
    private int approximateNumberOfForks, approximateNumberOfTriangles;
    
	/**
	 * This nested static class is used to store parameters of each layer of the
	 * vertices such as probability of selection and list of vertices.
	 * 
	 * @author Yudin Evgeniy

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
     * Constructs and initializes the class.
     *
     * @author Yudin Evgeniy
     * @param graph the graph
     * @param numberOfRuns number of runs of sampling algorithm 
     * @param numberOfThreads number of parallel threads
     */
	public ParallelDirThreeSizeSubgraphsCounterSampling(Graph<V, E> graph, int numberOfRuns, int numberOfThreads) {
        this.graph = graph;
        this.numberOfRuns = numberOfRuns;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * Saves number of explored <code>graph</code>'s "forks" into
	 * <code>exploredNumberOfForks</code> variable.<br>
	 * Saves number of explored <code>graph</code>'s "triangles" into
	 * <code>exploredNumberOfTriangles</code> variable.<br>
	 * Approximate number of the <code>graph</code>'s "forks" is calculated as
	 * number of explored <code>graph</code>'s "forks" divided by number of runs
	 * of sampling algorithm and multiplied by exact number of the
	 * <code>graph</code>'s "forks".<br>
	 * Approximate number of the <code>graph</code>'s "triangles" is calculated
	 * as number of explored <code>graph</code>'s "triangles" divided by three
	 * times number of runs of sampling algorithm and multiplied by exact number
	 * of the <code>graph</code>'s "forks".<br>
	 * If the <code>graph</code> includes directed edges then
	 * <code>simbigraph.stat.exception.UnsupportedEdgeTypeException</code>
	 * is thrown.
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
	 * @author Yudin Evgeniy
	 * 
	 */
	public void execute(){
		if (true) {
			doOriented();
			return;
			//throw new UnsupportedEdgeTypeException("The parallel version of 3-size subgraphs counter which uses random carcasses sampling algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
		}
		
			}
	/**
	 * Find oriented 3-subgraphs
	 * author Yudin Evgeniy
	 */
	private void doOriented() {
		Collection<V> vertices = graph.getVertices();
		Map<Integer, VertexLayerParameters<V>> vertexLayers = new HashMap<>();
		int numberOfVertexNeubor;
	    long t1=System.currentTimeMillis();

		/* 
		 * Bind each vertex of the graph to one layer of the vertices
		 * defined by number of successors of the vertex.
		 */
		for (V vertex : vertices) {
    		numberOfVertexNeubor = graph.getNeighbors(vertex).size();
    		if ( vertexLayers.get(numberOfVertexNeubor) == null) {
    			vertexLayers.put(numberOfVertexNeubor, new VertexLayerParameters<>());
    		}
    		vertexLayers.get(numberOfVertexNeubor).vertices.add(vertex);
		}
		
		long exactNumberOfForks = 0l;
		// Calculate exact number of the graph's "forks"
		for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
			exactNumberOfForks += vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1l) / 2l;
			if(exactNumberOfForks<0)
				System.out.println(exactNumberOfForks);
		}
		System.out.println("forks = "+exactNumberOfForks);
		// Calculate probability of selection for each layer of the vertices
    	for (Entry<Integer, VertexLayerParameters<V>> vertexLayer : vertexLayers.entrySet()) {
    		double d=(vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1)) / (2.0)/ exactNumberOfForks;
    		if(d<0)
    			System.out.println(d);
    		vertexLayer.getValue().probability = (vertexLayer.getValue().vertices.size() * vertexLayer.getKey() * (vertexLayer.getKey() - 1.)) / 2.0 / exactNumberOfForks;
    	}
    	
    	DirThreeSizeSubgraphsCounterSampling<V, E> counter = new DirThreeSizeSubgraphsCounterSampling<>(graph, vertexLayers);
    	List<Integer> resultsOfRuns = new ArrayList<>((int)numberOfRuns);
    	for (int i = 0; i < numberOfRuns; i++) {
    		resultsOfRuns.add(0);
		}
    	System.out.println("заполнение списка"+	    (System.currentTimeMillis()-t1));
    	
    	/*ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> exploredNumberOfTriangles = resultsOfRuns.stream().parallel().mapToInt(resultOfRun -> counter.doRun()).sum()).get();
			exploredNumberOfForks = numberOfRuns - exploredNumberOfTriangles;
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		approximateNumberOfForks = (int) ((double)exploredNumberOfForks / numberOfRuns * exactNumberOfForks);
		approximateNumberOfTriangles = (int) ((double)exploredNumberOfTriangles / numberOfRuns * exactNumberOfForks / 3)*/;		
	
		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		try {
			forkJoinPool.submit(() -> resultsList = resultsOfRuns.stream().parallel().map(resultOfRun -> counter.searchOrientedTypesOfSubgraphs()).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)).get();

			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_1 = resultsList.stream().parallel().filter(result -> result == 1).count()).get();

			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_2 = resultsList.stream().parallel().filter(result -> result == 2).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_3 = resultsList.stream().parallel().filter(result -> result == 3).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_4 = resultsList.stream().parallel().filter(result -> result == 4).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_5 = resultsList.stream().parallel().filter(result -> result == 5).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_6 = resultsList.stream().parallel().filter(result -> result == 6).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_7 = resultsList.stream().parallel().filter(result -> result == 7).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_8 = resultsList.stream().parallel().filter(result -> result == 8).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_9 = resultsList.stream().parallel().filter(result -> result == 9).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_10 = resultsList.stream().parallel().filter(result -> result == 10).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_11 = resultsList.stream().parallel().filter(result -> result == 11).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_12 = resultsList.stream().parallel().filter(result -> result == 12).count()).get();
			forkJoinPool.submit(() -> exploredNumberOfSubgraphs3_13 = resultsList.stream().parallel().filter(result -> result == 13).count()).get();

        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}
		approximateNumberOfSubgraphs4_1 = (exploredNumberOfSubgraphs3_1 / (double)numberOfRuns);
		approximateNumberOfSubgraphs4_2 = exploredNumberOfSubgraphs3_2 / (double)numberOfRuns ;
		approximateNumberOfSubgraphs4_3 = exploredNumberOfSubgraphs3_3 / (double)numberOfRuns ;//!!! нужно проверить какие числа ставить
		approximateNumberOfSubgraphs4_4 = exploredNumberOfSubgraphs3_4 / (double)numberOfRuns  ;
		approximateNumberOfSubgraphs4_5 = exploredNumberOfSubgraphs3_5 / (double)numberOfRuns ;
		approximateNumberOfSubgraphs4_6 = exploredNumberOfSubgraphs3_6 / (double)numberOfRuns ;	
		approximateNumberOfSubgraphs4_7 = exploredNumberOfSubgraphs3_7 / (double)numberOfRuns  / 3.;	
		approximateNumberOfSubgraphs4_8 = exploredNumberOfSubgraphs3_8 / (double)numberOfRuns  / 3.;	
		approximateNumberOfSubgraphs4_9 = exploredNumberOfSubgraphs3_9 / (double)numberOfRuns  / 3.;	
		approximateNumberOfSubgraphs4_10 = exploredNumberOfSubgraphs3_10 / (double)numberOfRuns  / 3.;	
		approximateNumberOfSubgraphs4_11 =exploredNumberOfSubgraphs3_11 / (double)numberOfRuns  / 3.;	
		approximateNumberOfSubgraphs4_12 = exploredNumberOfSubgraphs3_12 / (double)numberOfRuns / 3.;	
		approximateNumberOfSubgraphs4_13 = exploredNumberOfSubgraphs3_13 / (double)numberOfRuns / 3.;	

	
	
	
	
	
	}
    List<Integer> resultsList;

    private long exploredNumberOfSubgraphs3_1, exploredNumberOfSubgraphs3_2, exploredNumberOfSubgraphs3_3, exploredNumberOfSubgraphs3_4, exploredNumberOfSubgraphs3_5, exploredNumberOfSubgraphs3_6, exploredNumberOfSubgraphs3_7,
    exploredNumberOfSubgraphs3_8,exploredNumberOfSubgraphs3_9,exploredNumberOfSubgraphs3_10,exploredNumberOfSubgraphs3_11,exploredNumberOfSubgraphs3_12, exploredNumberOfSubgraphs3_13;
    
    private double approximateNumberOfSubgraphs4_1, approximateNumberOfSubgraphs4_2, approximateNumberOfSubgraphs4_3, approximateNumberOfSubgraphs4_4, approximateNumberOfSubgraphs4_5, approximateNumberOfSubgraphs4_6,
    approximateNumberOfSubgraphs4_7, approximateNumberOfSubgraphs4_8, approximateNumberOfSubgraphs4_9, approximateNumberOfSubgraphs4_10, approximateNumberOfSubgraphs4_11, approximateNumberOfSubgraphs4_12, approximateNumberOfSubgraphs4_13;

	

	/**
	 * @author Andrey Kurchanov
	 */
	@Override
    public String toString() {
		int approximateNumberOfSubgraphs = approximateNumberOfForks + approximateNumberOfTriangles;
		//System.out.println("exploredNumberOfSubgraphs3_2="+exploredNumberOfSubgraphs3_13);
		//System.out.println("exploredNumberOfSubgraphs3_2/1.="+exploredNumberOfSubgraphs3_2/1.);

    	return String.format(""+
				
				approximateNumberOfSubgraphs4_1+
				"\n"+approximateNumberOfSubgraphs4_2+
				"\n"+approximateNumberOfSubgraphs4_3+
				"\n"+approximateNumberOfSubgraphs4_4+
				"\n"+approximateNumberOfSubgraphs4_5+
				"\n"+approximateNumberOfSubgraphs4_6+
				"\n"+approximateNumberOfSubgraphs4_7+
				"\n"+approximateNumberOfSubgraphs4_8+
				"\n"+ approximateNumberOfSubgraphs4_9+
				"\n"+approximateNumberOfSubgraphs4_10+
				"\n"+approximateNumberOfSubgraphs4_11+
				"\n"+approximateNumberOfSubgraphs4_12+
				"\n"+approximateNumberOfSubgraphs4_13);
		/*return String.format("approximateNumberOfForks:"+approximateNumberOfForks+
				"\napproximateNumberOfTriangles:"+approximateNumberOfTriangles+
				"approximateNumberOfForks:"+3.*approximateNumberOfTriangles/(approximateNumberOfForks+approximateNumberOfTriangles));*/
    }

}
