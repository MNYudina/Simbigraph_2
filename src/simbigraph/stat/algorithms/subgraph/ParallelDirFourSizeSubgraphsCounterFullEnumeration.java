package simbigraph.stat.algorithms.subgraph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import simbigraph.stat.exception.UnsupportedEdgeTypeException;

/**
 * This is parallel version of 4-size undirected subgraphs counter which uses
 * full enumeration algorithm.
 * 
 * @author Yudina Maria, Yudin Evgeniy
 */
public class ParallelDirFourSizeSubgraphsCounterFullEnumeration<V, E>  {

	private Graph<V, E> graph;

    private int numberOfThreads;
    
    //private int numberOfSubgraphs4_1;, numberOfSubgraphs4_2, numberOfSubgraphs4_3, numberOfSubgraphs4_4, numberOfSubgraphs4_5, numberOfSubgraphs4_6;
	int[] massKoef={0, 0, 0, 1, 0, 0, 0, 1, 1, 0, 1, 0, 1, 1, 2, 1, 1, 2, 2, 4, 6, 6, 0, 0, 1, 1, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 0, 1, 1, 2, 1, 2, 1, 1, 2, 2, 2, 2, 2, 4, 6, 4, 6, 2, 2, 6, 6, 6, 6, 1, 2, 2, 2, 4, 6, 4, 6, 6, 6, 6, 4, 6, 6, 1, 2, 6, 2, 6, 6, 6, 12, 12, 6, 6, 12, 12, 12, 12, 12, 1, 1, 1, 2, 1, 2, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 2, 0, 1, 2, 2, 2, 1, 2, 2, 4, 6, 4, 6, 6, 6, 6, 4, 6, 6, 2, 2, 2, 2, 2, 6, 6, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 6, 6, 4, 6, 4, 6, 6, 6, 6, 6, 6, 6, 6, 12, 6, 12, 12, 12, 12, 12, 6, 6, 6, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 12, 6, 12, 12, 6, 12, 12, 12, 12, 12, 12, 6, 6, 6, 6, 4, 6, 6, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12, 12};
	int motifs[]= new int[100];
    List<Integer> resultsList;
    List<Integer> resultsList2;

    double exactNumberOfSubgraphs4_1 = 0;
	long exactNumberOfPathsOfLengthThree = 0;



    
    /**
     * Constructs and initializes the class.
     *
     * @author Yudina Maria, Yudin Evgeniy
     * @param graph the graph
     * @param numberOfThreads number of parallel threads
     */
	public ParallelDirFourSizeSubgraphsCounterFullEnumeration(Hypergraph<V, E> graph, int numberOfThreads) {
        this.graph = (Graph<V, E>) graph;
        this.numberOfThreads = numberOfThreads;
    }
	
	/**
	 * <p>
	 * The method uses Function and Parallel Stream features of Java 1.8 and
	 * custom ForkJoinPool for parallel execution.
	 * 
     * @author Yudina Maria, Yudin Evgeniy
	 * @throws UnsupportedEdgeTypeException
	 */
	public void execute() throws UnsupportedEdgeTypeException {
		if (graph.getDefaultEdgeType() == EdgeType.DIRECTED) {
			throw new UnsupportedEdgeTypeException("The parallel version of 4-size subgraphs counter which uses full enumeration algorithm does not work with " + graph.getDefaultEdgeType() + " graph.");
		}
		
		DirFourSizeSubgraphsCounterFullEnumeration<V, E> counter = new DirFourSizeSubgraphsCounterFullEnumeration<>(graph);
		Collection<V> vertices = graph.getVertices();
		Collection<E> edges = graph.getEdges();
		
		ForkJoinPool forkJoinPool = new ForkJoinPool(numberOfThreads);
		/*try {
			forkJoinPool.submit(() -> numberOfSubgraphs4_2 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_2(edge)).sum()).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_3 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_3(edge)).sum() / 2).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_4 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_4(edge)).sum() / 4).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_5 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_5(edge)).sum() / 6).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_6 = edges.stream().parallel().mapToInt(edge -> counter.getNumberOfSubgraphs4_6(edge)).sum() / 12).get();
			forkJoinPool.submit(() -> numberOfSubgraphs4_1 = vertices.stream().parallel().mapToInt(vertex -> counter.getNumberOfSubgraphs4_1(vertex)).sum() - numberOfSubgraphs4_3 - 2 * numberOfSubgraphs4_5 - 4 * numberOfSubgraphs4_6).get();
        } catch (InterruptedException | ExecutionException e) {
			e.printStackTrace();
		}*/
		
		
		try {
			forkJoinPool.submit(() -> resultsList2 = vertices.stream().parallel().map(vertex -> counter.searchOtherTypesOf4SubgraphsLapka(vertex)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)).get();
			forkJoinPool.submit(() -> resultsList = edges.stream().parallel().map(edge-> counter.searchOtherTypesOf4SubgraphsScobka(edge)).collect(ArrayList::new, ArrayList::add, ArrayList::addAll)).get();
			
			
			motifs=counter.massMotif;
			
			resultsList.addAll(resultsList2);
			for (int i = 0; i < motifs.length; i++) {
				final int num=i;
				forkJoinPool.submit(() -> motifs[num] =  (int) resultsList.stream().parallel().filter(result -> result == num).count()).get();
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
			
			str=str+(i)+": " + (!lapkaSet.contains(i)?(double)motifs[i]/massKoef[i]:(double)motifs[i])+"\n";
		}		

		return str;
    }

}