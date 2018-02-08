package temp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Predicate;

import simbigraph.core.PrefAttechRule;
import simbigraph.graphs.prefAttachment.ConfGeneral;
import simbigraph.graphs.prefAttachment.GenClassicalBA;
import edu.uci.ics.jung.graph.DirectedSparseGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.PajekNetReader;

public class RoadClass {
	Factory<Integer> vertexFactory = new Factory<Integer>() { // Фабрика вершин
		public Integer create() {
			return new Integer(nodeCount++);
		}
	};
	Factory<Integer> edgeFactory = new Factory<Integer>() { // Фабрика ребер
		public Integer create() {
			return new Integer(edgeCount++);
		}
	};
	Factory<Graph<Integer, Integer>> graphFactory = new Factory<Graph<Integer, Integer>>() {

		public Graph<Integer, Integer> create() {
			return new UndirectedSparseGraph<Integer, Integer>();
		}
	};
	Graph<Integer, Integer> g;
	int nodeCount = 0, edgeCount = 0;

	public RoadClass() {
		//g = getNetEdgelist("MOSCOW_railway.txt");
		g = getNetEdgelist("MOSCOW_new.txt");
		//g = getNetEdgelist("SPB.txt");
		//g = getNetEdgelist("OMSK_OBL.txt");
		//g = getNetEdgelist("roadNet-CA.txt");
		writeNodesDegrees(g, 20);
		//writeNodesInDegrees(g, 20);
		//writeNodesOutDegrees(g, 20);
	}

	private Graph<Integer, Integer> getNet(String fileName) {
		System.out.println(fileName);
		Graph graph = new SparseGraph();
		PajekNetReader<Graph<Integer, Integer>, Integer, Integer> pnr;
		try {
			pnr = new PajekNetReader<Graph<Integer, Integer>, Integer, Integer>(
					vertexFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);

		} catch (IOException e5) {
			System.out.println("IOException!!!!!!!!!!!!!!!!!!");
		}
		System.out.println("Nodes num=" + graph.getVertexCount());
		System.out.println("Edges num=" + graph.getEdgeCount());
		return graph;
		
	}
	private Graph<Integer, Integer> getNetEdgelist(String fileName) {
		System.out.println(fileName);
		Graph<Integer,Integer> gr = new UndirectedSparseGraph();
		FileReader reader;
		try {
			reader = new FileReader(fileName);
		
		BufferedReader br = new BufferedReader(reader);
		/*for (int i = 0; i <numV; i++) {
			gr.addVertex(new Integer(i));	
		}
*/		String str =null;int e=0;
		while((str=br.readLine())!=null){
			String[] mass = str.split(" ");
			gr.addEdge(new Integer(e++), Integer.valueOf(mass[0]), Integer.valueOf(mass[1]));
		}
					

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Nodes num=" + gr.getVertexCount());
		System.out.println("Edges num=" + gr.getEdgeCount());
		return gr;
		
	}

	private Graph createSeed() {
		Graph gr = new UndirectedSparseGraph<Integer, Integer>();
		for (int i = -1; i > -6; i--) {
			Integer n = new Integer(i);
			gr.addVertex(n);
		}
		int l = -1;
		Object[] mass = gr.getVertices().toArray();
		for (int i = 0; i < mass.length - 1; i++)
			for (int j = i + 1; j < mass.length; j++)
				if (i != j)
					gr.addEdge(new Integer(l--), (Integer) mass[i],
							(Integer) mass[j], EdgeType.UNDIRECTED);
		return gr;
	}

	public static void main(String[] args) {
		new RoadClass();

	}

	public static void writeNodesDegrees(Graph<Integer, Integer> graph, int length) {
		System.out.println("выводим степени связности");

		Iterator<Integer> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		while (it.hasNext()) {
			Integer node = it.next();
			{
				if (graph.degree(node) < length)
					distr0[graph.degree(node)] = distr0[graph.degree(node)] + 1;
			}
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
	}
	
	public static void writeNodesInDegrees(Graph<Integer, Integer> graph, int length) {
		System.out.println("выводим входящие степени связности");

		Iterator<Integer> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		while (it.hasNext()) {
			Integer node = it.next();
			{
				if (graph.inDegree(node) < length)
					distr0[graph.inDegree(node)] = distr0[graph.inDegree(node)] + 1;
			}
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
	}
	
	public static void writeNodesOutDegrees(Graph<Integer, Integer> graph, int length) {
		System.out.println("выводим исходящие степени связности");

		Iterator<Integer> it = graph.getVertices().iterator();
		long c1 = 1;

		int[] distr0 = new int[length];
		while (it.hasNext()) {
			Integer node = it.next();
			{
				if (graph.outDegree(node) < length)
					distr0[graph.outDegree(node)] = distr0[graph.outDegree(node)] + 1;
			}
		}
		for (int i = 0; i < distr0.length; i++)
			System.out.println(distr0[i]);
	}
	 private static class StartsWithPredicate implements Predicate<String> {
	        private String tag;
	        
	        protected StartsWithPredicate(String s) {
	            this.tag = s;
	        }
	        
	        public boolean evaluate(String str) {
	            return (str != null && str.toLowerCase().startsWith(tag));
	        }
	    }
    private static final Predicate<String> e_pred = new StartsWithPredicate("*edges");

    private static final Predicate<String> v_pred = new StartsWithPredicate("*vertices");

	private void loadVladGraph(String filename, Graph<Object, Integer> graph) throws IOException {
		FileReader reader=new FileReader(filename);
		BufferedReader br = new BufferedReader(reader);
	        // ignore everything until we see '*Vertices'
        String curLine = skip(br, v_pred);
	        if (curLine == null) // no vertices in the graph; return empty graph
	            return;
	        // create appropriate number of vertices
	        StringTokenizer st = new StringTokenizer(curLine);
	        st.nextToken(); // skip past "*vertices";
	        int num_vertices = Integer.parseInt(st.nextToken());
	        int[] id = null;
	        Object[] vert= new Object[num_vertices];int c=0;
	        if (vertexFactory != null)
	        {
	        	for (int i = 1; i <= num_vertices; i++){
	        		Object a=vertexFactory.create();
	                graph.addVertex(a);
	                vert[c++]=a;
				}
	         }
	       
	        System.out.println("Graph size"+graph.getVertexCount());
	        
	        String curLine2 = skip(br, e_pred);
	        String str="";
	        do{
	        	str =br.readLine();
	        	if(str==null)break;
	        	String[] mass=str.split(" ");
	        	int index1=Integer.valueOf(mass[0]);
	        	int index2=Integer.valueOf(mass[1]);
	        	graph.addEdge(edgeFactory.create(), vert[index1-1],vert[index2-1]);
	        }while(true);
	        System.out.println("Graph edges"+graph.getEdgeCount());
	        br.close();
	        reader.close();
	}
	private String skip(BufferedReader br, Predicate<String> p) throws IOException
    {
        while (br.ready())
        {
            String curLine = br.readLine();
            if (curLine == null)
                break;
            curLine = curLine.trim();
            if (p.evaluate(curLine))
                return curLine;
        }
        return null;
    }


}
