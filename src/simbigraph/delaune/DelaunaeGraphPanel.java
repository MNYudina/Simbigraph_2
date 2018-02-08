package simbigraph.delaune;

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
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;

import simbigraph.graphs.views.NetGrhFilter;


import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.Hypergraph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphMLWriter;
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

public class DelaunaeGraphPanel extends JPanel {

	private static final long serialVersionUID = 1L;
	public Graph<DelanuneyAgent, Integer> graph = new SparseGraph();

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
	public DelaunaeGraphPanel() {
		super();
		setVv();
	}
	public VisualizationViewer getVisViewer() {
		return vv;
	}

	public VisualizationViewer getVv() {
		return vv;
	}

	
	Class[] layoutClasses = new Class[]{CircleLayout.class,SpringLayout.class,FRLayout.class,KKLayout.class};
    Class subLayoutType = CircleLayout.class;
    AggregateLayout<DelanuneyAgent,Integer> clusteringLayout;

    VisualizationModel<DelanuneyAgent,Integer> visualizationModel;
	public void setVv() {
        //clusteringLayout = new AggregateLayout<SimgraphNode,Integer>(new FRLayout<SimgraphNode,Integer>(graph));
        
		
		graph.addVertex(new DelanuneyAgent(10,60));
		graph.addVertex(new DelanuneyAgent(70,80));


		Layout clusteringLayout = new XYDelauneLayout(graph);
		visualizationModel = new DefaultVisualizationModel<DelanuneyAgent,Integer>(clusteringLayout);
        vv =  new VisualizationViewer<DelanuneyAgent,Integer>(visualizationModel);
        vv.setBackground(Color.white);
        vv.setVertexToolTipTransformer(new ToStringLabeller());
		graphMouse = new DefaultModalGraphMouse();
		vv.setGraphMouse(graphMouse);
		
		
		Transformer pTrans = vv.getRenderContext()
					.getVertexFillPaintTransformer();
			vv.getRenderContext().setVertexFillPaintTransformer(
					new Transformer<DelanuneyAgent, Paint>() {
						public Color transform(DelanuneyAgent arg0) {
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
	 	        
	       		JFileChooser chooser = new JFileChooser();
	       		chooser.setFileFilter(new NetGrhFilter());
	       		chooser.setAcceptAllFileFilterUsed(false);
	       		chooser.setSelectedFile(new File("graph.net"));

	       		 PajekNetWriter<DelanuneyAgent, Integer> gm = new  PajekNetWriter<DelanuneyAgent, Integer>();

	       		try {
					chooser.setCurrentDirectory(new File(new File("/graphs").getCanonicalPath()));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

	       	    int returnVal = chooser.showSaveDialog(null);	
	       	    if(returnVal==JFileChooser.APPROVE_OPTION){
	 	        	File selectedFile = chooser.getSelectedFile();
	 	            //надо бы сохранить координаты
	       
				
	 	        	Transformer<DelanuneyAgent, String> vertex_transformer2 = new Transformer<DelanuneyAgent, String>() {
			 		 public String transform(DelanuneyAgent a) {
				            return new Double(a.getX()).toString();
				        }
	 	        	};
	 	        	Transformer<DelanuneyAgent, String> vertex_transformer3 = new Transformer<DelanuneyAgent, String>() {
			 		 public String transform(DelanuneyAgent a) {
				            return new Double(a.getY()).toString();
				        }
	 	        	};
				
		 	
	 	        	GraphMLWriter<DelanuneyAgent,String> wr=new GraphMLWriter();
	 	        	try {
	 	        			wr.addVertexData("x", null, null, vertex_transformer2);
	 	        			wr.addVertexData("y", null, null, vertex_transformer3);
	 	        			wr.save((Graph)graph, new FileWriter(selectedFile));
	 	        	} catch (IOException e1) {
					e1.printStackTrace();
	 	        	}
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
		public void updateGraph(Graph gr) {
			graph=gr;
			Layout clusteringLayout = new XYDelauneLayout(graph);
			visualizationModel.setGraphLayout(clusteringLayout);
			
		}

	
	


	
}
