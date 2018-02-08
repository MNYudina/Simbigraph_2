package simbigraph.graphs.neighborhood;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashSet;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.graphs.prefAttachment.GenClassicalBA;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.prefAttachment.SpeedBAGenerator;
import simbigraph.graphs.views.NetGrhFilter;

import edu.uci.ics.jung.algorithms.generators.random.BarabasiAlbertGenerator;
import edu.uci.ics.jung.algorithms.generators.random.EppsteinPowerLawGenerator;
import edu.uci.ics.jung.algorithms.generators.random.ErdosRenyiGenerator;
import edu.uci.ics.jung.algorithms.generators.random.KleinbergSmallWorldGenerator;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;

public class PanelGraphView extends JPanel {
	private static final long serialVersionUID = 1L;
	public Graph graph=MainNeiGraph.graph;
	public VisualizationViewer<Object,Integer> vv;
	private AbstractLayout<Object,Integer> layout;
	private Factory<Graph<Object, Integer>> graphFactory = new Factory<Graph<Object, Integer>>() {
		private int n = 0;
		public Graph<Object, Integer> create() {
			return new UndirectedSparseGraph<Object, Integer>();
		}
	};
	public static Factory<Object> vertexFactory; //= sim.agentFactory;
	private Factory<Integer> edgeFactory = new Factory<Integer>() {
		int n = 0;
		public Integer create() {
			return n++;
		}
	};
	/**
	 * This is the default constructor
	 */
	Simulation sim;
	public PanelGraphView() {
		super();
		setVv();
	}
	public VisualizationViewer getVisViewer() {
		return vv;
	}
		public void setNet(String fileName) {
			graph = new SparseGraph<SimgraphNode, Integer>();
	        PajekNetReader<Graph<Object, Integer>, Object, Integer> pnr;
	        try {
	            pnr = new PajekNetReader<Graph<Object, Integer>, Object, Integer>(vertexFactory, edgeFactory);
	            File file = new File(fileName);
	            pnr.load(fileName, graph);
	            
	        } catch (IOException e5) {
	        	System.out.println("cc");
	        }
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
	}

	public VisualizationViewer getVv() {
		return vv;
	}

	
	Class[] layoutClasses = new Class[]{CircleLayout.class,SpringLayout.class,FRLayout.class,KKLayout.class};
    Class subLayoutType = CircleLayout.class;
    AggregateLayout<SimgraphNode,Integer> clusteringLayout;



	public void setVv() {
        //clusteringLayout = new AggregateLayout<SimgraphNode,Integer>(new FRLayout<SimgraphNode,Integer>(graph));
        
		Layout clusteringLayout = new XYLayout(graph);
        final VisualizationModel<Object,Integer> visualizationModel = 
            new DefaultVisualizationModel<Object,Integer>(clusteringLayout);
        vv =  new VisualizationViewer<Object,Integer>(visualizationModel);
        vv.setBackground(Color.white);
        vv.setVertexToolTipTransformer(new ToStringLabeller());
		graphMouse = new DefaultModalGraphMouse();
		vv.setGraphMouse(graphMouse);
		final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction
				.getInstance();
		final Set exclusions = new HashSet();
		Transformer pTrans = vv.getRenderContext()
					.getVertexFillPaintTransformer();
			vv.getRenderContext().setVertexFillPaintTransformer(
					new Transformer<Object, Paint>() {
						public Color transform(Object arg0) {
							return Color.RED;
						}
				});
	        GraphZoomScrollPane gzsp = new GraphZoomScrollPane(vv);
	        JComboBox modeBox = graphMouse.getModeComboBox();
	        modeBox.addItemListener(graphMouse.getModeListener());
	        graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
	        final ScalingControl scaler = new CrossoverScalingControl();
	        JButton plus = new JButton("+");
	        plus.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                scaler.scale(vv, 1.1f, vv.getCenter());
	            }
	        });
	        JButton minus = new JButton("-");
	        minus.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                scaler.scale(vv, 1/1.1f, vv.getCenter());
	            }
	        });
	        JPanel save = new JPanel();
	        JButton buttonSave= new JButton("Save graph");
	        buttonSave.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	       		 PajekNetWriter<SimgraphNode, Integer> gm = new  PajekNetWriter<SimgraphNode, Integer>();
	 	        
	       		JFileChooser chooser = new JFileChooser();
	       		chooser.setFileFilter(new NetGrhFilter());
	       		chooser.setAcceptAllFileFilterUsed(false);
	       		chooser.setSelectedFile(new File("graph.net"));

		    	
	       		try {
					chooser.setCurrentDirectory(new File(new File("/graphs").getCanonicalPath()));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

	       	    int returnVal = chooser.showSaveDialog(null);	
	       	    if(returnVal==JFileChooser.APPROVE_OPTION)
	       		 try {
	 	        	File selectedFile = chooser.getSelectedFile();
	 	            gm.save(graph, new FileWriter(selectedFile.getAbsolutePath()));
	 	        } catch (IOException e1) {
	 	            e1.printStackTrace();
	 	        }
	 		}
	        });
	        save.add(buttonSave);;

	        
	        Dimension space = new Dimension(20,20);
	        Box controls = Box.createVerticalBox();
	        //controls.add(Box.createRigidArea(space));
	        JPanel zoomControls = new JPanel(new GridLayout(1,2));
	        zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
	        zoomControls.add(plus);
	        zoomControls.add(minus);
	        heightConstrain(zoomControls);
	        controls.add(zoomControls);
	        controls.add(Box.createRigidArea(space));
	        JPanel modePanel = new JPanel(new GridLayout(1,1));
	        modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
	        modePanel.add(modeBox);
	        heightConstrain(modePanel);
	        controls.add(modePanel);
	        //controls.add(Box.createRigidArea(space));
	        controls.add(save);


	        JPanel pan=new JPanel(new BorderLayout());
	        pan.add(controls,BorderLayout.EAST);
	        pan.add(gzsp, BorderLayout.CENTER);
	       // JScrollPane pane = new JScrollPane(pan);
	       // pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
	        add(pan);
			validate();
	}
	  private void heightConstrain(Component component) {
	    	Dimension d = new Dimension(component.getMaximumSize().width,
	    			component.getMinimumSize().height);
	    	component.setMaximumSize(d);
	    }
	  private Layout getLayoutFor(Class layoutClass, Graph graph) throws Exception {
	    	Object[] args = new Object[]{graph};
	    	Constructor constructor = layoutClass.getConstructor(new Class[] {Graph.class});
	    	return  (Layout)constructor.newInstance(args);
	    }
	    

	    
	  DefaultModalGraphMouse graphMouse;
	  JMenu modeMenu;

	
	public void setErdosReniyGen(int vertices, double prob) {
		Factory<UndirectedGraph<Object, Integer>> graphFactory2 = new Factory<UndirectedGraph<Object, Integer>>() {
			private int n = 0;

			public UndirectedGraph<Object, Integer> create() {
				return new UndirectedSparseGraph<Object, Integer>();
			}
		};

		ErdosRenyiGenerator<Object, Integer> gen = new ErdosRenyiGenerator<Object, Integer>(
				graphFactory2, vertexFactory, edgeFactory, vertices, prob);
		//gen.evolveGraph(evolvingSteps);
		graph = gen.create();
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
	
		
	}

	public void setEppstGen(int nodes, int beta, int degree) {
		EppsteinPowerLawGenerator<Object, Integer> gen = new EppsteinPowerLawGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, nodes, degree,
				beta);
		//gen.evolveGraph(evolvingSteps);
		graph = gen.create();
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
	}

	public void setBAGen(int initVertices, int edgesToAttach, int evolvingSteps) {
		BarabasiAlbertGenerator<Object, Integer> gen = new BarabasiAlbertGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, initVertices, edgesToAttach, 1,
				new HashSet());
		gen.evolveGraph(evolvingSteps);
		graph = gen.create();
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
	}

	public void KleinbergGenerator(int latticeSize, double clustExp) {
		KleinbergSmallWorldGenerator<Object, Integer> gen = new KleinbergSmallWorldGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, latticeSize, clustExp);
		//gen.evolveGraph(evolvingSteps);
		graph = gen.create();
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
		
	}

	public void setMyBAGen(int vertices, int evolvingSteps) {

		SpeedBAGenerator<Object, Integer> gen = new SpeedBAGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, vertices, evolvingSteps);
		graph = gen.create();
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
	}

	public void setSim(Simulation sim2) {
		sim=sim2;
		//vertexFactory = sim.agentFactory;

	}

	
	public void EvolveGraph(int parameter1, int steps,
			PrefferentialAttachment prefRule, boolean isDirected) {
		GenClassicalBA<Object, Integer> gen =
			new GenClassicalBA<Object, Integer>(vertexFactory, edgeFactory, parameter1,  prefRule);
		graph = gen.evolve(steps, graph);
		System.out.println("Узлов=" + graph.getVertexCount());
		System.out.println("Рёбер=" + graph.getEdgeCount());
	}
	
}
