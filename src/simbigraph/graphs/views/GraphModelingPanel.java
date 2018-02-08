package simbigraph.graphs.views;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TimerTask;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.table.DefaultTableModel;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections15.Predicate;
import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.Factory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.xml.sax.SAXException;

import simbigraph.core.Context;
import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.graphs.prefAttachment.BarabasiAlbertGenerator2;
import simbigraph.graphs.prefAttachment.GenClassicalBA;
import simbigraph.graphs.prefAttachment.GenConfCurdsBA;
import simbigraph.graphs.prefAttachment.GenNonIntBA;
import simbigraph.graphs.prefAttachment.ConfGeneral;
import simbigraph.graphs.prefAttachment.GenTvorogBA;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.prefAttachment.SpeedBAGenerator;
import simbigraph.grid.pseudogrid.SimControlPanel;
import simbigraph.grid.views.panels2d.SquarePanel;
import simbigraph.projections.ProjGraphPA;
import simbigraph.util.Statistic;
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
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.SparseMultigraph;
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

public class GraphModelingPanel extends SimControlPanel {
	private static final long serialVersionUID = 1L;
	public VisualizationViewer<Object, Integer> vv;
	private AbstractLayout<Object, Integer> layout;
	private Factory<Graph<Object, Integer>> graphFactory = new Factory<Graph<Object, Integer>>() {
	private int n = 0;

	public Graph<Object, Integer> create() {
			return new SparseMultigraph<Object, Integer>();
		}
	};
	public Factory<Object> vertexFactory; // = sim.agentFactory;
	private Factory<Integer> edgeFactory = new Factory<Integer>() {
		int n = 0;

		public Integer create() {
			return n++;
		}
	};
	public GraphModelingPanel() {
		super();
	}

	public VisualizationViewer getVisViewer() {
		return vv;
	}
	private static Graph<Integer, Integer> getNetEdgelist(String fileName, Factory<Object> vertexFactory2, Factory<Integer> edgeFactory2) {
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
			gr.addEdge(edgeFactory2.create(), Integer.valueOf(mass[0]), Integer.valueOf(mass[1]));
		}
					

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("Nodes num=" + gr.getVertexCount());
		System.out.println("Edges num=" + gr.getEdgeCount());
		return gr;
		
	}
	public void setNet(String fileName) {
		Graph graph = new SparseGraph<Object, Integer>();
		if(fileName.endsWith(".shpx"))
			graph = getNetEdgelist(fileName,vertexFactory,edgeFactory);//g = createCube();
		else	
		{
			
		PajekNetReader<Graph<Object, Integer>, Object, Integer> pnr;
		try {
			pnr = new PajekNetReader<Graph<Object, Integer>, Object, Integer>(
					vertexFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);
			//loadVladGraph(fileName, graph);

		} catch (IOException e5) {
			System.out.println("IOException!!!!!!!!!!!!!!!!!!");
		}
		}
		System.out.println("Nodes num=" + graph.getVertexCount());
		System.out.println("Edges num=" + graph.getEdgeCount());
		Context.setGraph(graph);
	}
	

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
	        //A=(SimgraphNode[]) graph.getVertices().toArray();
	       
	        System.out.println("Graph size"+graph.getVertexCount());
	        
	        String curLine2 = skip(br, e_pred);
	        String str="";
	        do{
	        //for (int i = 0; i < 10; i++) {
	        	str =br.readLine();
	        	if(str==null)break;
	        	String[] mass=str.split(" ");
	        	int index1=Integer.valueOf(mass[0]);
	        	int index2=Integer.valueOf(mass[1]);
	        	///System.out.println("1:"+index1+";2:"+index2);
	        	graph.addEdge(edgeFactory.create(), vert[index1-1],vert[index2-1]);
			//}
	        }while(true);
	        System.out.println("Graph edges"+graph.getEdgeCount());
	        
	       /* // read vertices until we see any Pajek format tag ('*...')
	        curLine = null;
	        while (br.ready())
	        {
	            curLine = br.readLine();
	            if (curLine == null || t_pred.evaluate(curLine))
	                break;
	            if (curLine == "") // skip blank lines
	                continue;
	            
	            try
	            {
	                readVertex(curLine, id, num_vertices);
	            }
	            catch (IllegalArgumentException iae)
	            {
	                br.close();
	                reader.close();
	                throw iae;
	            }
	        }   

	        // skip over the intermediate stuff (if any) 
	        // and read the next arcs/edges section that we find
	        curLine = readArcsOrEdges(curLine, br, g, id, edge_factory);

	        // ditto
	        readArcsOrEdges(curLine, br, g, id, edge_factory);*/
	        
	        br.close();
	        reader.close();
	        
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

	public VisualizationViewer getVv() {
		return vv;
	}

	Class[] layoutClasses = new Class[] { CircleLayout.class,
			SpringLayout.class, FRLayout.class, KKLayout.class };
	
	
	Class subLayoutType = CircleLayout.class;
	AggregateLayout<Object, Integer> clusteringLayout;

	public void init() {
		final Graph graph =Context.getGraph();
		
		JPanel pan;

		if (graph.getVertexCount() < 5000) {
			clusteringLayout = new AggregateLayout<Object, Integer>(
					new FRLayout<Object, Integer>(graph));
			final VisualizationModel<Object, Integer> visualizationModel = new DefaultVisualizationModel<Object, Integer>(
					clusteringLayout);
			vv = new VisualizationViewer<Object, Integer>(visualizationModel);
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
					scaler.scale(vv, 1 / 1.1f, vv.getCenter());
				}
			});
			JComboBox layoutTypeComboBox = new JComboBox(layoutClasses);
			layoutTypeComboBox.setRenderer(new DefaultListCellRenderer() {
				public Component getListCellRendererComponent(JList list,
						Object value, int index, boolean isSelected,
						boolean cellHasFocus) {
					String valueString = value.toString();
					valueString = valueString.substring(valueString
							.lastIndexOf('.') + 1);
					return super.getListCellRendererComponent(list,
							valueString, index, isSelected, cellHasFocus);
				}
			});
			layoutTypeComboBox.setSelectedItem(SpringLayout.class);
			layoutTypeComboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					if (e.getStateChange() == ItemEvent.SELECTED) {
						Class clazz = (Class) e.getItem();
						try {
							Layout<Object, Integer> layout = getLayoutFor(clazz,
									graph);
							layout.setInitializer(vv.getGraphLayout());
							clusteringLayout.setDelegate(layout);
							vv.setGraphLayout(clusteringLayout);
						} catch (Exception ex) {
							ex.printStackTrace();
						}
					}
				}
			});
			Box save = Box.createVerticalBox();
			JButton buttonSave = new JButton("  Save  graph   ");
			buttonSave.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					PajekNetWriter<Object, Integer> gm = new PajekNetWriter<Object, Integer>();

					JFileChooser chooser = new JFileChooser();
					chooser.setFileFilter(new NetGrhFilter());
					chooser.setAcceptAllFileFilterUsed(false);
					chooser.setSelectedFile(new File("graph.net"));

					try {
						chooser.setCurrentDirectory(new File(new File(
								"./graphs").getCanonicalPath()));
					} catch (IOException e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

					int returnVal = chooser.showSaveDialog(null);
					if (returnVal == JFileChooser.APPROVE_OPTION)
						try {
							File selectedFile = chooser.getSelectedFile();
							gm.save(graph, new FileWriter(selectedFile
									.getAbsolutePath()));
						} catch (IOException e1) {
							e1.printStackTrace();
						}
				}
			});
			save.add(buttonSave);
			JButton btn2 = new JButton("Degree distrib.");
			btn2.addActionListener(new ActionListener() {
				private void heightConstrain2(Component component) {
					Dimension d = new Dimension(
							component.getMaximumSize().width, component
									.getMinimumSize().height);
					component.setMaximumSize(d);
				}

				@Override
				public void actionPerformed(ActionEvent e) {
					final JFrame frame = new JFrame(
							"Generated graph degree distribution");
					JPanel chartPanel = createChartPanel();

					// frame.add(controls, BorderLayout.EAST);

					frame.setResizable(false);
					frame.add(chartPanel);
					frame.setVisible(true);
					frame.pack();
				}
			});
			save.add(Box.createVerticalStrut(5));
			save.add(btn2);
			save.add(Box.createVerticalStrut(5));
			JButton btn3 = new JButton("  Metric prop.   ");
			btn3.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					JDialog mDialog= new MetricsDialog();
					mDialog.setVisible(true);
				}
				
			});
			
			save.add(btn3);
			save.add(Box.createVerticalStrut(5));
			JButton btn4 = new JButton("  Motifs stats   ");
			save.add(btn4);
			btn4.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent arg0) {
					JDialog mDialog= new MotifsDialog();
					mDialog.setVisible(true);
				}
				
			});

			Dimension space = new Dimension(20, 20);
			Box controls = Box.createVerticalBox();
			// controls.add(Box.createRigidArea(space));
			JPanel zoomControls = new JPanel(new GridLayout(1, 2));
			zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
			zoomControls.add(plus);
			zoomControls.add(minus);
			heightConstrain(zoomControls);
			controls.add(zoomControls);
			controls.add(Box.createRigidArea(space));
			JPanel layoutControls = new JPanel(new GridLayout(0, 1));
			layoutControls
					.setBorder(BorderFactory.createTitledBorder("Layout"));
			layoutControls.add(layoutTypeComboBox);
			heightConstrain(layoutControls);
			controls.add(layoutControls);
			JPanel modePanel = new JPanel(new GridLayout(1, 1));
			modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
			modePanel.add(modeBox);
			heightConstrain(modePanel);
			controls.add(modePanel);
			// controls.add(Box.createRigidArea(space));
			controls.add(save);
			pan = new JPanel(new BorderLayout());
			pan.add(controls, BorderLayout.EAST);
			pan.add(gzsp, BorderLayout.CENTER);
			super.init(pan);
		} else {
			pan = createChartPanel();
			super.init(pan);
/*
			sim.start();
			sim.schedule.step(sim);
			sim.schedule.step(sim);
			sim.schedule.step(sim);*/

			
		}
		JScrollPane sp = new JScrollPane(pan);
		add(sp);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		add(sp);

		validate();
	}
	final List<XYSeries> listSeries = new ArrayList<XYSeries>();

	protected JPanel createChartPanel() {
		JPanel pan = new JPanel();
		pan.setLayout(new BorderLayout());
		final Graph<Object, Integer> graph = Context.getGraph();
		Iterator<Object> it = graph.getVertices().iterator();
		int length = 0;
		while (it.hasNext()) {
			int deg = graph.degree(it.next());
			if (deg > length)
				length = deg;
		}
		it = graph.getVertices().iterator();
		double[] Q_ = new double[length + 1];
		while (it.hasNext()) {
			Object node = it.next();
			int m = graph.degree(node);
			Q_[m] = Q_[m] + 1.0 / ((double) graph.getVertexCount());
		}
		
		
		double _rr=0;
		for (int i = 1; i < Q_.length; i++) {
			_rr+=i*Q_[i];
		}

		final XYSeries series1 = new XYSeries("Generated graph!!:"+_rr/2);
		for (int i = 0; i < Q_.length; i++){
			series1.add((double) i, (double) Q_[i]);
		//System.out.println(""+i+" "+(double) Q_[i]);
		}
		
		listSeries.add(series1);
		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		final JFreeChart chart = ChartFactory.createHistogram("", "k", "Q(k)",
				dataset, PlotOrientation.VERTICAL, true, true, false);

		chart.getPlot().setForegroundAlpha(0.85f);
		
		final ChartPanel chartPanel = new ChartPanel(chart);
		Box controls = Box.createVerticalBox();
		
		final JTextPane jTextPanePropertyValue= new JTextPane(); 
		jTextPanePropertyValue.setFont(new Font(Font.MONOSPACED,
				Font.PLAIN, 14));
		jTextPanePropertyValue.setMargin(new Insets(-3, -2, -3, -2));
		jTextPanePropertyValue
				.addKeyListener(new java.awt.event.KeyAdapter() {
					public void keyReleased(java.awt.event.KeyEvent evt) {
						//jTextPanePropertyValueKeyReleased(evt);
					}
				});
		//jTextPanePropertyValue.setText(propertyValue);

		JScrollPane jScrollPane = new JScrollPane();
		jScrollPane.setViewportView(jTextPanePropertyValue);
		jScrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
		jScrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

		JButton jButtonPropertyValue = new JButton();
		jButtonPropertyValue.setText("...");
		jButtonPropertyValue.setVerticalAlignment(SwingConstants.TOP);
		jButtonPropertyValue.setToolTipText("Open file chooser...");
		jButtonPropertyValue.setPreferredSize(new Dimension(25, 25));
		jButtonPropertyValue
				.addActionListener(new java.awt.event.ActionListener() {
					public void actionPerformed(
							java.awt.event.ActionEvent evt) {
						jFileChooserValueActionPerformed(evt);
					}
					private void jFileChooserValueActionPerformed(
							ActionEvent evt) {
						JFileChooser fc = null;
						fc = new JFileChooser();
						fc.setFileFilter(new NetGrhFilter());
						fc.setAcceptAllFileFilterUsed(false);
						try {

							fc.setCurrentDirectory(new File(new File("./graphs")
									.getCanonicalPath()));
						} catch (IOException e) {
							e.printStackTrace();
						}
						fc.setAcceptAllFileFilterUsed(false);
						if (fc.showOpenDialog(null) == 0) {
							File selectedFile = fc.getSelectedFile();
							if (selectedFile != null) {
								jTextPanePropertyValue.setText(selectedFile.getAbsolutePath());
							}
						}			
					}
				});

		JPanel j = new JPanel();
		j.setLayout(new BorderLayout());
		j.add("North", jButtonPropertyValue);

		JPanel jPanel = new JPanel();
		jPanel.setLayout(new BorderLayout());
		jPanel.add("Center", jScrollPane);
		jPanel.add("East", j);
		heightConstrain(jPanel);
		controls.add(jPanel);

		controls.add(Box.createVerticalStrut(5));
		JButton compare = new JButton("Comparing graphs");
		compare.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// вставить предыдущие графики
				PajekNetReader<Graph<Object, Integer>, Object, Integer> pnr;
				Graph<Object, Integer> graph2 =new SparseMultigraph();

				try {
					pnr = new PajekNetReader<Graph<Object, Integer>, Object, Integer>(
							vertexFactory, edgeFactory);
					pnr.load(jTextPanePropertyValue.getText(), graph2);

				} catch (IOException e5) {
					System.out.println(e5);
				}    
				//final XYSeries series1 = new XYSeries("Generated graph");
				
				Iterator<Object> it2 = graph2.getVertices().iterator();
				int length = 0;
				while (it2.hasNext()) {
					int deg = graph2.degree(it2.next());
					if (deg > length)
						length = deg;
				}
				it2 = graph2.getVertices().iterator();
				double[] Q_ = new double[length + 1];
				while (it2.hasNext()) {
					Object node = it2.next();
					int m = graph2.degree(node);
					Q_[m] = Q_[m] + 1.0 / (graph2.getVertexCount());
				}
				
				double _m=0;
				for (int i = 0; i < Q_.length; i++) {
					_m+=i*Q_[i];
				}
				
				final XYSeries series2 = new XYSeries(jTextPanePropertyValue.getText()+"!!:"+_m/2);
				for (int i = 0; i < Q_.length; i++)
					series2.add((double) i, (double) Q_[i]);
				listSeries.add(series2);
				//удалить все, серии, а потом вставить все 
				dataset.removeAllSeries();
				for(XYSeries series:listSeries)
					dataset.addSeries(series);
			}
		});
		Box box =Box.createHorizontalBox();
		
		//box.add(Box.createHorizontalGlue());

		
		
		JButton buttonSave = new JButton("  Saving    graphs   ");
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PajekNetWriter<Object, Integer> gm = new PajekNetWriter<Object, Integer>();

				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new NetGrhFilter());
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setSelectedFile(new File("graph.net"));

				try {
					chooser.setCurrentDirectory(new File(new File(
							"./graphs").getCanonicalPath()));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					try {
						File selectedFile = chooser.getSelectedFile();
						gm.save(graph, new FileWriter(selectedFile
								.getAbsolutePath()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
			}
		});
		
		
		JButton buttonBigComponent = new JButton("   CalcBigComponent  ");
		buttonBigComponent.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/* Set<Set<Object>> set = Statistic.getClusters(Context.getGraph());
				 int i=1; int max = 0;
				 Set ost = new HashSet();
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					Set<Object> set2 = (Set<Object>) iterator.next();
					System.out.println("кластер"+(i++)+" :"+set2.size());
					if(set2.size()>max){max=set2.size(); ost = set2;}
					
				}
				Graph gr=Context.getGraph();
				for (Iterator iterator = set.iterator(); iterator.hasNext();) {
					Set<Object> set2 = (Set<Object>) iterator.next();
					if(set2!=ost)
						for (Object o : set2) {
							gr.removeVertex(o);
						}
				}
				
				///-------------------------------------------------
				
				PajekNetWriter<Object, Integer> gm = new PajekNetWriter<Object, Integer>();

				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new NetGrhFilter());
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setSelectedFile(new File("graph.net"));

				try {
					chooser.setCurrentDirectory(new File(new File(
							"./graphs").getCanonicalPath()));
				} catch (IOException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}

				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					try {
						File selectedFile = chooser.getSelectedFile();
						gm.save(graph, new FileWriter(selectedFile
								.getAbsolutePath()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}	
			
*/				
				//Set<Set<Object>> set = Statistic.getClusters(Context.getGraph());
				int [][] mass = Statistic.getTettaMatrix(Context.getGraph(),10);
				for (int i = 0; i < mass[0].length; i++) {
					System.out.println();
					for (int k = 0; k < mass[i].length; k++) {
						System.out.print(mass[i][k]/((double) 2*Context.getGraph().getEdgeCount())+" ");
					}
				}
			}
		});


		JPanel calcPanel= new JPanel();
		calcPanel.setName("Experemental Panel");

		final JTextArea jTextPaneConsole =  new JTextArea();
		jTextPaneConsole.setRows(6);

		final JSpinner jSpinnerStart = new JSpinner();
		final JSpinner jSpinnerEnd = new JSpinner();
		final JSpinner jSpinnerStep = new JSpinner();
		final JSpinner jSpinnerCount = new JSpinner();

		
		JButton buttonRandRemoveHubs = new JButton("Hubs Removing");
		buttonRandRemoveHubs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextPaneConsole.append("Random hubs removing (from "+ jSpinnerStart.getValue()+" to "+jSpinnerEnd.getValue()+" ("+jSpinnerStep.getValue()+" steps and "+jSpinnerCount.getValue()+" iterations)"+"\n");
				double [] s =Statistic.percolationNode2(Context.getGraph(),(Integer) jSpinnerCount.getValue(), (Double) jSpinnerStart.getValue(), (Double) jSpinnerEnd.getValue(), (Double) jSpinnerStep.getValue());
				for (int i = 0; i < s.length; i++) {
					jTextPaneConsole.append(s[i]+"\n");
				}
			}
		});
		calcPanel.add(buttonRandRemoveHubs);
		
		
		JButton buttonRandRemoveNodes = new JButton("Nodes removing");
		buttonRandRemoveNodes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextPaneConsole.append("Random nodes removing (from "+ jSpinnerStart.getValue()+" to "+jSpinnerEnd.getValue()+" ("+jSpinnerStep.getValue()+" steps and "+jSpinnerCount.getValue()+" iterations)"+"\n");
				double [] s =Statistic.percolationNode(Context.getGraph(),(Integer) jSpinnerCount.getValue(), (Double) jSpinnerStart.getValue(), (Double) jSpinnerEnd.getValue(), (Double) jSpinnerStep.getValue());
				for (int i = 0; i < s.length; i++) {
					jTextPaneConsole.append(s[i]+"\n");
				}
			}
		});
		calcPanel.add(buttonRandRemoveNodes);
		
		
		JButton buttonRandRemoveEdges = new JButton("Edges Removing");
		buttonRandRemoveEdges.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				jTextPaneConsole.append("Random edges removing (from "+ jSpinnerStart.getValue()+" to "+jSpinnerEnd.getValue()+" ("+jSpinnerStep.getValue()+" steps and "+jSpinnerCount.getValue()+" iterations)"+"\n");
				double [] s =Statistic.percolationEdges(Context.getGraph(),(Integer) jSpinnerCount.getValue(), (Double) jSpinnerStart.getValue(), (Double) jSpinnerEnd.getValue(), (Double) jSpinnerStep.getValue());
				for (int i = 0; i < s.length; i++) {
					jTextPaneConsole.append(s[i]+"\n");
				}
			}
		});
		calcPanel.add(buttonRandRemoveEdges);
		
		JButton buttonCoefClast = new JButton(" Motifs & clustering");
		buttonCoefClast.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//int[] m = Statistic.getTriAndVilk(Context.getGraph());
				//for (int i = 0; i < files.length; i++) {
				//	System.out.println(files[i]);
				int count =(Integer)jSpinnerCount.getValue();
				Motif3 m3= new Motif3(count, Context.getGraph());
				jTextPaneConsole.append("¬ершин "+m3.graph.getVertexCount()+" ребер "+m3.graph.getEdgeCount());
				jTextPaneConsole.append("\nMotifs for 3 verticies");
				jTextPaneConsole.append("\nn_treug "+m3.n_treug*3/(double)m3.iterations);
				jTextPaneConsole.append("\nn_vilks "+m3.n_vilks/(double)m3.iterations);
				jTextPaneConsole.append("\nn_vilks_toch "+m3.n_vilks_toch);

					Motif4 m4= new Motif4(count, Context.getGraph());
					jTextPaneConsole.append("\n-----------------------------------------------");
					jTextPaneConsole.append("\nMotifs for 4 verticies");
					jTextPaneConsole.append("\nn_klika "+m4.n_klika*12/(double)m4.iterations);
					jTextPaneConsole.append("\nn_semi_klika "+m4.n_semi_klika*8/(double)m4.iterations);
					jTextPaneConsole.append("\nn_kvadrat "+m4.n_kvadrat*4/(double)m4.iterations);
					jTextPaneConsole.append("\nn_roga "+m4.n_roga/(double)m4.iterations);

				}});
		calcPanel.add(buttonCoefClast);
		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		JPanel params = new JPanel();
		SpinnerNumberModel spinnerNumberModel1 = new SpinnerNumberModel();SpinnerNumberModel spinnerNumberModel2 = new SpinnerNumberModel();SpinnerNumberModel spinnerNumberModel3 = new SpinnerNumberModel();;SpinnerNumberModel spinnerNumberModel4 = new SpinnerNumberModel();
		spinnerNumberModel1.setMinimum(0.0);spinnerNumberModel2.setMinimum(0.0);spinnerNumberModel3.setMinimum(0.1);spinnerNumberModel4.setMinimum(1);
		spinnerNumberModel1.setMaximum(1.0);spinnerNumberModel2.setMaximum(1.0);spinnerNumberModel3.setMaximum(1.0);spinnerNumberModel4.setMaximum(10000);
		spinnerNumberModel1.setStepSize(0.01);spinnerNumberModel2.setStepSize(0.01);spinnerNumberModel3.setStepSize(0.01);spinnerNumberModel4.setStepSize(1);
		spinnerNumberModel1.setValue(0.0);spinnerNumberModel2.setValue(1.0);spinnerNumberModel3.setValue(0.1);spinnerNumberModel4.setValue(1);
		jSpinnerStart.setValue(0.0);jSpinnerEnd.setValue(1.0);jSpinnerStep.setValue(1.0);
		jSpinnerStart.setPreferredSize(new Dimension(60, jSpinnerStart.getPreferredSize().height));
		jSpinnerStep.setPreferredSize(new Dimension(60, jSpinnerStep.getPreferredSize().height));
		jSpinnerEnd.setPreferredSize(new Dimension(60, jSpinnerEnd.getPreferredSize().height));
		jSpinnerCount.setPreferredSize(new Dimension(70, jSpinnerCount.getPreferredSize().height));

		jSpinnerStart.setModel(spinnerNumberModel1);jSpinnerEnd.setModel(spinnerNumberModel2);jSpinnerStep.setModel(spinnerNumberModel3);jSpinnerCount.setModel(spinnerNumberModel4);
		
		params.add(new JLabel("From: "));params.add(jSpinnerStart);
		params.add(new JLabel("      To: "));	params.add(jSpinnerEnd);
		params.add(new JLabel("      Step: "));params.add(jSpinnerStep);
		params.add(new JLabel("      Number of iteration: "));params.add(jSpinnerCount);

		p.add(calcPanel,BorderLayout.NORTH);
		p.add(params,BorderLayout.SOUTH);

		
		box.add(p);
		Box func = Box.createVerticalBox();

		func.add(compare);
		func.add(buttonSave);
		func.add(buttonBigComponent);
		

		box.add(func);
		controls.add(box);

		
        jTextPaneConsole.setFont(new java.awt.Font(java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 14));
        
        JScrollPane jSPane = new JScrollPane();
        jSPane.setViewportView(jTextPaneConsole);
		controls.add(jSPane);

		controls.add(Box.createVerticalGlue());
		JScrollPane jScrollPane2 = new JScrollPane(chartPanel);

		pan.add(jScrollPane2, BorderLayout.NORTH);
		pan.add(controls);

		
		return pan;
	}

	private void heightConstrain(Component component) {
		Dimension d = new Dimension(component.getMaximumSize().width, component
				.getMinimumSize().height);
		component.setMaximumSize(d);
	}

	private Layout getLayoutFor(Class layoutClass, Graph graph)
			throws Exception {
		Object[] args = new Object[] { graph };
		Constructor constructor = layoutClass
				.getConstructor(new Class[] { Graph.class });
		return (Layout) constructor.newInstance(args);
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
		// gen.evolveGraph(evolvingSteps);
		Graph graph = gen.create();
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num=" + graph.getEdgeCount());
		Context.setGraph(graph);
	}

	public void setEppstGen(int nodes, int beta, int degree) {
		EppsteinPowerLawGenerator<Object, Integer> gen = new EppsteinPowerLawGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, nodes, degree, beta);
		// gen.evolveGraph(evolvingSteps);
		Graph graph = gen.create();
		Context.setGraph(graph);
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());
	}

	public void setBAGen(int initVertices, int edgesToAttach, int evolvingSteps) {
		BarabasiAlbertGenerator2<Object, Integer> gen = new BarabasiAlbertGenerator2<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, initVertices,
				edgesToAttach, 1, new HashSet());
		gen.evolveGraph(evolvingSteps);
		Graph graph = gen.create();
		Context.setGraph(graph);
		System.out.println("переделанный");
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());
	}

	public void KleinbergGenerator(int latticeSize, double clustExp) {
		KleinbergSmallWorldGenerator<Object, Integer> gen = new KleinbergSmallWorldGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, latticeSize, clustExp);
		// gen.evolveGraph(evolvingSteps);
		Graph graph = gen.create();
		Context.setGraph(graph);
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());

	}

	public void setMyBAGen(int vertices, int evolvingSteps) {

		SpeedBAGenerator<Object, Integer> gen = new SpeedBAGenerator<Object, Integer>(
				graphFactory, vertexFactory, edgeFactory, vertices,
				evolvingSteps);
		Graph graph = gen.create();
		Context.setGraph(graph);
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());
	}

	public void setSim(Simulation sim2) {
		sim = sim2;
		//sim.init(graph);
		//vertexFactory = sim.agentFactory;

	}

	public void EvolveGraph(int parameter1, int steps,
			PrefferentialAttachment prefRule, boolean isDirected) {
		GenClassicalBA<Object, Integer> gen = new GenClassicalBA<Object, Integer>(
				vertexFactory, edgeFactory, parameter1, prefRule);
		Context.setGraph(gen.evolve(steps, Context.getGraph()));
		System.out.println("Nodes num =" + Context.getGraph().getVertexCount());
		System.out.println("Edges num =" + Context.getGraph().getEdgeCount());
	}

	public void EvolveGraph(double[] parameter1, int steps,
			PrefferentialAttachment prefRule, boolean isDirected) {
		GenNonIntBA<Object, Integer> gen = new GenNonIntBA(vertexFactory,
				edgeFactory, parameter1, prefRule);
		Context.setGraph(gen.evolve(steps, Context.getGraph()));
		//System.out.println("Nodes num =" + graph.getVertexCount());
		//System.out.println("Edges num =" + graph.getEdgeCount());

	}

	public void setTvorogGen(int initVertices, int edgesToAttach) {
		//GenGraphCurdsBA gen = new GenGraphCurdsBA(graphFactory,vertexFactory,edgeFactory,edgesToAttach);
		//Graph graph =gen.getBara2(evolvingSteps);
  
		GenTvorogBA gen = new GenTvorogBA(graphFactory,vertexFactory,edgeFactory,edgesToAttach);
		Graph graph=gen.getGraphTvorog(initVertices, 2);

		Context.setGraph(graph);
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());
	}

	public void setConfGeneralBAGen(int initVertices, int edgesToAttach	) {
		ConfGeneral gen = new ConfGeneral(graphFactory,vertexFactory,edgeFactory,edgesToAttach);

		Graph graph =gen.getBA(initVertices, edgesToAttach);

		Context.setGraph(graph);
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());		
	}

	public void setConfBAGen(int initVertices, int edgesToAttach) {
		//GenConfModelBA gen = new GenConfModelBA(graphFactory,vertexFactory,edgeFactory,edgesToAttach);
		
		GenConfCurdsBA gen = new GenConfCurdsBA(graphFactory,vertexFactory,edgeFactory,edgesToAttach);
		Graph graph =gen.getBara2(initVertices,edgesToAttach);

		Context.setGraph(graph);
		System.out.println("Nodes num =" + graph.getVertexCount());
		System.out.println("Edges num =" + graph.getEdgeCount());
	}

}
