package simbigraph.stat.algorithms.subgraph;

import java.io.IOException;

import org.apache.commons.collections15.Factory;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.PajekNetReader;
import simbigraph.stat.exception.GraphStatsException;
import simbigraph.stat.exception.UnsupportedEdgeTypeException;
import simbigraph.stat.graph.AdjacencyListGraph;

public class Test3 {
	  private static Graph<Integer, Integer> initGraph(String filename) {
	        long startTime;
	        Graph<Integer, Integer> graph = null;

	        //LOG.info("Loading graph from {} file.", parameters.getGraphFile());
	        startTime = System.nanoTime();
	        try {
	        	
	            graph = loadGraph(filename);
	         //   LOG.info("Graph successfully loaded in {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
	        } catch (IOException e) {
	        //	LOG.error("Failed to load graph from {} file.", parameters.getGraphFile());
	         //   LOG.debug("Failed to load graph from {} file.", parameters.getGraphFile(), e);
	        	System.out.println("ddd"+e);
	            System.exit(1);
	        }
	        return graph;
	    }

	    private static Graph<Integer, Integer> loadGraph(String path) throws IOException {
	        return new PajekNetReader<>(createIntegerFactory(), createIntegerFactory()).load(path, new SparseGraph<>());
	    }

	   
	    private static Factory<Integer> createIntegerFactory() {
	        return new Factory<Integer>() {
	            private int n = 0;

	            @Override
	            public Integer create() {
	                return n++;
	            }
	        };
	    }
public static void main(String[] args) throws GraphStatsException {

 
  // Graph graph = initGraph("Twitter_obr.net");
   //Graph<Long, Long>  graph = Test.initGraphLong("F:\\Диссертация МАША\\данные\\!!мотивы\\email\\Email_obr_new.net");
	//Graph<Long, Long>  graph = Test.initGraphLong("\\RealGraphs\\Email_obr_new.net");
	//Graph<Long, Long>  graph = Test.initGraphLong("\\RealGraphs\\Email_obr_new.net");
	//Graph<Long, Long>  graph = Test.initGraphLong("my_polBlog1.net");

	//Graph graph = initGraph("one.net");
	//Graph graph = initGraph("tree_rebro.net");
	//Graph graph = initGraph("dwa.net");
	//Graph graph = initGraph("chetyre.net");
   /* graph = new SparseGraph<Integer, Integer>();
	graph.addEdge(4, 2, 1, EdgeType.DIRECTED);
	graph.addEdge(5, 1,3, EdgeType.DIRECTED);
	graph.addEdge(6, 3,2,EdgeType.DIRECTED);*/

	Graph graph = initGraph("MyAs.net");
    
    System.out.println("E="+graph.getEdgeCount()+" V="+graph.getVertexCount());
    
    //ParallelThreeSizeSubgraphsCounterSampling<Long, Long> p3= new ParallelThreeSizeSubgraphsCounterSampling<Long, Long>(graph,100000,8);
    //ParallelDirFourSizeSubgraphsCounterSampling<Long, Long> p4= new ParallelDirFourSizeSubgraphsCounterSampling<Long, Long>(graph,10000,8);

    long t1=System.currentTimeMillis();
   ParallelThreeSizeSubgraphsCounterFullEnumeration <Integer, Integer> p4= new ParallelThreeSizeSubgraphsCounterFullEnumeration(graph,8);
   // ParallelFourSizeSubgraphsCounterFullEnumeration <Integer, Integer> p= new ParallelFourSizeSubgraphsCounterFullEnumeration(graph,8);
   // p3.execute();
   // System.out.println(p3);
System.out.println("----------------------------------------------------------------------");
    p4.execute();
    System.out.println("t="+(System.currentTimeMillis()-t1));

    System.out.println(p4);

}

}
