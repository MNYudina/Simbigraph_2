package simbigraph.stat.algorithms.subgraph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.io.PajekNetWriter;
import simbigraph.stat.exception.GraphStatsException;
import simbigraph.stat.exception.UnsupportedEdgeTypeException;
import simbigraph.stat.graph.AdjacencyListGraph;

public class Test {
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
	        	System.out.println("ddd");
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
public static void main_old(String[] args) throws UnsupportedEdgeTypeException {
    Graph<Integer, Integer> graph;

 
    graph = initGraph("mygraphs//my_polBlog1.net");
    
   /* graph = new SparseGraph<Integer, Integer>();
	graph.addEdge(4, 2, 1, EdgeType.DIRECTED);
	graph.addEdge(5, 1,3, EdgeType.DIRECTED);
	graph.addEdge(6, 3,2,EdgeType.DIRECTED);*/

    
    
    System.out.println("E="+graph.getEdgeCount()+" V="+graph.getVertexCount());
    
   // ParallelThreeSizeSubgraphsCounterSampling<Integer, Integer> p= new ParallelThreeSizeSubgraphsCounterSampling<Integer, Integer>(graph,100000,2);
    ParallelThreeSizeSubgraphsCounterFullEnumeration <Integer, Integer> p= new ParallelThreeSizeSubgraphsCounterFullEnumeration(graph,2);
    p.execute();
    System.out.println(p);
    //saveGraph(graph, "mygraphs//my_polBlogR.net");

}
public static void saveGraph(Graph<Long, Long> g, String filename) {
	PajekNetWriter<Long, Long> gm = new PajekNetWriter<Long, Long>();
	Transformer<Long, String> vs = new Transformer<Long, String>() {

		@Override
		public String transform(Long arg0) {
			// TODO Auto-generated method stub
			return arg0.toString();
		}

	};
	Transformer<Long, Number> nev = new Transformer<Long, Number>() {

		@Override
		public Number transform(Long arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

	};

	try {
		gm.save(g, new FileWriter(filename), vs, nev);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

public static void main(String[] args) throws GraphStatsException {
	  Graph<Long, Long> graph;

//	 graph = initGraphLong("mygraphs//g_plus_obr_new.net");
//	 graph = initGraphLong("mygraphs//pol_blogs/my_polBlog1.net");
	//	graph = Test.initGraphLong("RealGraphs//PGPgiantcompo.net");
	 // graph = Test.initGraphLong("RealGraphs//p2p-Gnutella31.net");
	//  graph = Test.initGraphLong("graphs//com-dblp.ungraph.net");
		graph = Test.initGraphLong("MyAs.net");
	//  	graph = Test.initGraphLong("mygraphs//Email-Enron.net");

		
		// graph = Test.initGraphLong();

		/*graph = new SparseGraph<Long, Long>();
		graph.addEdge(1l, 1l, 2l);
		graph.addEdge(2l, 2l, 3l); 
		graph.addEdge(3l, 3l, 1l);
		graph.addEdge(4l, 3l, 5l); 
		graph.addEdge(5l, 3l, 4l);
		graph.addEdge(6l, 3l, 6l); 
		graph.addEdge(7l, 3l, 7l);*/
		
		
		
	  // graph = initGraphLong("mygraphs//email//Email_obr_new.net");
	  
	 // graph = initGraphLong("mygraphs//twitter//Twitter_obr_new.net");
	  //graph = initGraphLong("mygraphs//my_polBlog1.net");
	  // System.out.println("сохраняю");
	   //saveGraph(graph, "mygraphs//Twitter_obrR.net");
	 //  graph = initGraphEdgelistLong("mygraphs//twitter_combined.txt");
	    
	   /* graph = new SparseGraph<Integer, Integer>();
		graph.addEdge(4, 2, 1, EdgeType.DIRECTED);
		graph.addEdge(5, 1,3, EdgeType.DIRECTED);
		graph.addEdge(6, 3,2,EdgeType.DIRECTED);*/

	 // graph = initGraphLong("mygraphs//Email-Enron.net");
	    
	    System.out.println("E="+graph.getEdgeCount()+" V="+graph.getVertexCount());
	 //   System.out.println("Запускаю метод Монте-Кало на 100 000");
	    
	    long t1=System.currentTimeMillis();
	    ParallelDirThreeSizeSubgraphsCounterSampling<Long, Long> p= new ParallelDirThreeSizeSubgraphsCounterSampling<Long, Long>(graph,50000,8);
	  //  ParallelThreeSizeSubgraphsCounterFullEnumeration <Long, Long> p= new ParallelThreeSizeSubgraphsCounterFullEnumeration(graph,1);
	   // ParallelFourSizeSubgraphsCounterSampling<Long, Long> p= new ParallelFourSizeSubgraphsCounterSampling<Long, Long>(graph,50000,2);
	    p.execute();
	    System.out.println("t="+(System.currentTimeMillis()-t1));
	    System.out.println(p);
		//saveGraph(graph, "mygraphs//my_Twitter.net");

}
 public static Graph<Long, Long> initGraphLong(String filename) {
	  long startTime;
      Graph<Long, Long> graph = null;

      //LOG.info("Loading graph from {} file.", parameters.getGraphFile());
      try {
      	
          graph = loadGraphLong(filename);
       //   LOG.info("Graph successfully loaded in {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
      } catch (IOException e) {
      //	LOG.error("Failed to load graph from {} file.", parameters.getGraphFile());
       //   LOG.debug("Failed to load graph from {} file.", parameters.getGraphFile(), e);
    	  System.out.println(e);
      	System.out.println("ddd");
          System.exit(1);
      }
      return graph;
}

public static Graph<Long, Long> loadGraphLong(String path)throws IOException {
	// TODO Auto-generated method stub
	return new PajekNetReader<>(createLongFactory(), createLongFactory()).load(path, new SparseGraph<>());}

public static Graph<String, Long> initGraphEdgelistLong(String filename) {
    long startTime;
    Graph<String, Long> graph = null;

    //LOG.info("Loading graph from {} file.", parameters.getGraphFile());
    startTime = System.nanoTime();
    try {
    	
        graph = loadGraphLongEdgeList(filename);
     //   LOG.info("Graph successfully loaded in {}.", FormatUtils.durationToHMS(System.nanoTime() - startTime));
    } catch (IOException e) {
    //	LOG.error("Failed to load graph from {} file.", parameters.getGraphFile());
     //   LOG.debug("Failed to load graph from {} file.", parameters.getGraphFile(), e);
    	System.out.println("ошибка IOException");
        System.exit(1);
    }
    return graph;
}

private static Graph<String, Long> loadGraphLongEdgeList(String path) throws IOException {
	BufferedReader br = null;
	Graph<String, Long> gr = new SparseMultigraph<>();
	try {
		 
		String sCurrentLine;

		
		 br = new BufferedReader(new FileReader(path));

		int i=0;
		while ((sCurrentLine = br.readLine()) != null) {
			String[] strMass=sCurrentLine.split(" ");
			//System.out.println(strMass[1]+"#"+strMass[2]);
			//gr.addEdge(new Integer(i++),strMass[1], strMass[3]);
			if(strMass.length==2){
				gr.addEdge(new Long(i++),(strMass[0]), (strMass[1]),EdgeType.DIRECTED);
				//System.out.println(""+strMass[0]+" " +strMass[1]);
				//if(strMass[0]<0||strMass[0]<0)
			}
			else System.out.println("не должно быть так"
					+ "");//gr.addVertex(new Long(strMass[0]));
		}
	} catch (IOException e) {
		e.printStackTrace();
	} finally {
		try {
			if (br != null)br.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	return gr;
}


private static Factory<Long> createLongFactory() {
    return new Factory<Long>() {
        private long n = 0;

        @Override
        public Long create() {
            return new Long(n=n+1l);
        }
    };
}

}
