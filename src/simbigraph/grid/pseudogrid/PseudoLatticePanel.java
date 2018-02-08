package simbigraph.grid.pseudogrid;

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
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.xml.sax.SAXException;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.delaune.DelanuneyAgent;
import simbigraph.delaune.XYDelauneLayout;
import simbigraph.graphs.views.NetGrhFilter;
import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.io.GraphMLReader;
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

public class PseudoLatticePanel extends SimControlPanel {
	private static final long serialVersionUID = 1L;
	//public Graph<DelanuneyAgent,Integer> graph = new SparseGraph();
	Class[] layoutClasses = new Class[]{CircleLayout.class,SpringLayout.class,FRLayout.class,KKLayout.class};
    Class subLayoutType = CircleLayout.class;
    AggregateLayout<DelanuneyAgent,Integer> clusteringLayout;
    VisualizationModel<DelanuneyAgent,Integer> visualizationModel;

	public VisualizationViewer<DelanuneyAgent,Integer> vv;
	private AbstractLayout<DelanuneyAgent,Integer> layout;
	private Factory<Graph<DelanuneyAgent, Integer>> graphFactory = new Factory<Graph<DelanuneyAgent, Integer>>() {
		private int n = 0;
		public Graph<DelanuneyAgent, Integer> create() {
			return new UndirectedSparseGraph<DelanuneyAgent, Integer>();
		}
	};
	public static Factory<DelanuneyAgent> vertexFactory; //= sim.agentFactory;
	private Factory<Integer> edgeFactory = new Factory<Integer>() {
		int n = 0;
		public Integer create() {
			return n++;
		}
	};
	/**
	 * This is the default constructor
	 */
	public PseudoLatticePanel() {
		super();
	}
	public void init(String nameFile, Factory<DelanuneyAgent> agFactory) {
		setVv(nameFile,agFactory);
		super.init(vv);

	}
	public VisualizationViewer getVisViewer() {
		return vv;
	}

	public VisualizationViewer getVv() {
		return vv;
	}

	
	public void setVv(String nameFile, Factory<DelanuneyAgent> agFactory) {
		Graph graph = new SparseGraph<DelanuneyAgent, Integer>();
		Context.setGraph(graph);
		Layout clusteringLayout = new XYDelauneLayout(graph);
		visualizationModel = new DefaultVisualizationModel<DelanuneyAgent,Integer>(clusteringLayout);
        vv =  new VisualizationViewer<DelanuneyAgent,Integer>(visualizationModel);
        vv.setBackground(Color.white);
        vv.setVertexToolTipTransformer(new ToStringLabeller());
		graphMouse = new DefaultModalGraphMouse();
		vv.setGraphMouse(graphMouse);
		final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction
				.getInstance();
		final Set exclusions = new HashSet();
		
		
		 
		 vertexFactory =agFactory;
		
		GraphMLReader<SparseGraph<DelanuneyAgent, Integer>, DelanuneyAgent, Integer> gmlr = null;
		try {
				gmlr = new GraphMLReader<SparseGraph<DelanuneyAgent, Integer>, DelanuneyAgent, Integer>(
						vertexFactory, edgeFactory);
				gmlr.load(nameFile, (SparseGraph<DelanuneyAgent, Integer>) graph);
		}catch (SAXException e2) {}
		catch (ParserConfigurationException e6) {} 
		catch (IOException e5) {	}
		System.out.println("Узлов="+graph.getVertexCount());
		System.out.println("ребер="+graph.getEdgeCount());
		Transformer<DelanuneyAgent, String> t_x = gmlr.getVertexMetadata().get("y").transformer;
		Transformer<DelanuneyAgent, String> t_y = gmlr.getVertexMetadata().get("x").transformer;

		for(DelanuneyAgent me:((Graph<DelanuneyAgent,Integer>)Context.getGraph()).getVertices())
		{
			me.setX(Double.valueOf(t_x.transform(me)));
			//me.setY(t_y.transform(me));
			me.setY(Double.valueOf(t_y.transform(me)));

		}
		vv.getRenderContext().setVertexFillPaintTransformer(
				new Transformer<DelanuneyAgent, Paint>() {
					public Color transform(DelanuneyAgent arg0) {
						return sim.getAgentColor(arg0);
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


	        JPanel pan=new JPanel(new BorderLayout());
	        pan.add(controls,BorderLayout.EAST);
	        pan.add(gzsp, BorderLayout.CENTER);
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
		public void updateGraph(Graph gr) {
			Context.setGraph(gr);
			Layout clusteringLayout = new XYDelauneLayout(gr);
			
			
			visualizationModel.setGraphLayout(clusteringLayout);
			
		}
		public void setNet() {
			Graph graph = Context.getGraph();
			Layout clusteringLayout = new XYDelauneLayout(graph);
			visualizationModel.setGraphLayout(clusteringLayout);
		}
		public void setSim(Simulation sim2) {
			// TODO Auto-generated method stub
			sim =sim2;
		}
}
