package simbigraph.graphs.decomposition;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.collections15.Factory;
import org.apache.commons.collections15.Transformer;
import org.xml.sax.SAXException;

import simbigraph.graphs.views.NetGrhFilter;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.AggregateLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.shortestpath.BFSDistanceLabeler;
import edu.uci.ics.jung.algorithms.shortestpath.DijkstraShortestPath;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import edu.uci.ics.jung.io.GraphMLReader;
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.io.PajekNetWriter;
import edu.uci.ics.jung.visualization.DefaultVisualizationModel;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.LayeredIcon;
import edu.uci.ics.jung.visualization.VisualizationModel;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.EditingModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.DefaultVertexIconTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.util.PredicatedParallelEdgeIndexFunction;

public class GraphDecompositionPanel extends JPanel {
	public class VertexIconTransformer<Object> extends
			DefaultVertexIconTransformer<Object> implements
			Transformer<Object, Icon> {

		public Icon transform(Object v) {
			ImageIcon a;
			if (((SimbigraphNode) v).state == -5) {
				a = new ImageIcon(getClass().getResource(
						"/images/IconSource.png"));// new
													// ImageIcon("./img/big.gif");
			} else if (((SimbigraphNode) v).state == 5) {
				a = new ImageIcon(getClass()
						.getResource("/images/IconSink.png"));// new
																// ImageIcon("./img/end.gif");
			} else {
				a = new ImageIcon(getClass()
						.getResource("/images/IconStop.png"));// new
																// ImageIcon("./img/red.gif");
			}
			Icon icon = new LayeredIcon(a.getImage());
			return icon;
		}
	}

	private static final long serialVersionUID = 1L;

	public static Graph<SimbigraphNode, SimbigraphLink> graph;
	public VisualizationViewer<SimbigraphNode, SimbigraphLink> vv;
	private AbstractLayout<SimbigraphNode, SimbigraphLink> layout;
	private Factory<Graph<SimbigraphNode, SimbigraphLink>> graphFactory = new Factory<Graph<SimbigraphNode, SimbigraphLink>>() {
		private int n = 0;

		public Graph<SimbigraphNode, SimbigraphLink> create() {
			return new SparseMultigraph<SimbigraphNode, SimbigraphLink>();
		}
	};
	public static Factory<SimbigraphNode> vertexFactory = new Factory<SimbigraphNode>() {
		int n = 0;

		public SimbigraphNode create() {
			return new SimbigraphNode(n++);
		}
	};
	private Factory<SimbigraphLink> edgeFactory = new Factory<SimbigraphLink>() {
		public SimbigraphLink create() {
			return new SimbigraphLink();
		}
	};

	/**
	 * This is the default constructor
	 */
	public GraphDecompositionPanel() {
		super();
	}

	public VisualizationViewer getVisViewer() {
		return vv;
	}

	public void setNet(String fileName) {
		graph = new SparseMultigraph<SimbigraphNode, SimbigraphLink>();
		PajekNetReader<Graph<SimbigraphNode, SimbigraphLink>, SimbigraphNode, SimbigraphLink> pnr;
		try {
			pnr = new PajekNetReader<Graph<SimbigraphNode, SimbigraphLink>, SimbigraphNode, SimbigraphLink>(
					vertexFactory, edgeFactory);
			File file = new File(fileName);
			pnr.load(fileName, graph);

		} catch (IOException e5) {
			System.out.println("cc");
		}
		// System.out.println("”злgов=" + graph.getVertexCount());
		// System.out.println("–Єбер=" + graph.getEdgeCount());
	}

	public VisualizationViewer getVv() {
		return vv;
	}

	Class[] layoutClasses = new Class[] { CircleLayout.class,
			SpringLayout.class, FRLayout.class, KKLayout.class };
	Class subLayoutType = CircleLayout.class;
	AggregateLayout<SimbigraphNode, SimbigraphLink> clusteringLayout;

	public void setVv() {
		clusteringLayout = new AggregateLayout<SimbigraphNode, SimbigraphLink>(
				new FRLayout<SimbigraphNode, SimbigraphLink>(graph));
		final VisualizationModel<SimbigraphNode, SimbigraphLink> visualizationModel = new DefaultVisualizationModel<SimbigraphNode, SimbigraphLink>(
				clusteringLayout);
		vv = new VisualizationViewer<SimbigraphNode, SimbigraphLink>(
				visualizationModel);
		vv.setBackground(Color.white);
		vv.setVertexToolTipTransformer(new ToStringLabeller());
		graphMouse = new EditingModalGraphMouse(vv.getRenderContext(),
				vertexFactory, edgeFactory);
		vv.setGraphMouse(graphMouse);
		final PredicatedParallelEdgeIndexFunction eif = PredicatedParallelEdgeIndexFunction
				.getInstance();
		final Set exclusions = new HashSet();
		final VertexIconTransformer<SimbigraphNode> vertexIconTransformer = new VertexIconTransformer<SimbigraphNode>();
		vv.getRenderContext().setVertexIconTransformer(vertexIconTransformer);
		vv.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

		Transformer pTrans = vv.getRenderContext()
				.getVertexFillPaintTransformer();
		/*
		 * vv.getRenderContext().setVertexFillPaintTransformer( new
		 * Transformer<SimbigraphNode,String >() { public Color
		 * transform(SimbigraphNode arg0) { return arg0.getColor(); } });
		 */
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
				return super.getListCellRendererComponent(list, valueString,
						index, isSelected, cellHasFocus);
			}
		});
		layoutTypeComboBox.setSelectedItem(FRLayout.class);
		layoutTypeComboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					Class clazz = (Class) e.getItem();
					try {
						Layout<SimbigraphNode, SimbigraphLink> layout = getLayoutFor(
								clazz, graph);
						layout.setInitializer(vv.getGraphLayout());
						clusteringLayout.setDelegate(layout);
						vv.setGraphLayout(clusteringLayout);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		});
		Box save = Box.createVerticalBox(); // = new JPanel();
											// //save.setLayout(new
											// GridLayout(1,2));
		JButton buttonSave = new JButton("   Save  graph   ");
		buttonSave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PajekNetWriter<SimbigraphNode, SimbigraphLink> gm = new PajekNetWriter<SimbigraphNode, SimbigraphLink>();
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new NetGrhFilter());
				chooser.setAcceptAllFileFilterUsed(false);
				chooser.setSelectedFile(new File("graph.net"));
				try {
					chooser.setCurrentDirectory(new File(new File("./graphs")
							.getCanonicalPath()));
				} catch (IOException e2) {
					e2.printStackTrace();
				}
				int returnVal = chooser.showSaveDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION)
					try {
						File selectedFile = chooser.getSelectedFile();
						gm.save(graph,
								new FileWriter(selectedFile.getAbsolutePath()));
					} catch (IOException e1) {
						e1.printStackTrace();
					}
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
		layoutControls.setBorder(BorderFactory.createTitledBorder("Layout"));
		layoutControls.add(layoutTypeComboBox);
		heightConstrain(layoutControls);
		controls.add(layoutControls);
		JPanel modePanel = new JPanel(new GridLayout(1, 1));
		modePanel.setBorder(BorderFactory.createTitledBorder("Mouse Mode"));
		modePanel.add(modeBox);
		heightConstrain(modePanel);
		controls.add(modePanel);

		JButton btnReduction = new JButton("Reduct.iteration");
		btnReduction.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// нужно вз€ть граф
				// и редуцировать
				//
				boolean change = true;
				// while(change)
				{
					int changes = 0;
					// notrelev();
					if (posled()) {
						changes++;
					}
					if (parallel()) {
						changes++;
					}
					if (changes == 0) {
						if (most())
							changes++;
					}
					change = changes > 0;
				}
				vv.revalidate();
				vv.repaint();
			}

			private boolean most() {
				// нахождени€ пар узлов, св€занных ребром
				// и имеющих еще двух ближайших соседей, общих дл€ найденной
				// пары.
				return false;
			}

			private boolean parallel() {
				// пары, в которых ребра соедин€ют одинаковые узлы

				// System.out.println(graph.getEdgeCount());
				boolean _change = false;
				boolean next = true;
				while (next) {
					next = false;
					ex: for (SimbigraphLink edge1 : graph.getEdges()) {
						for (SimbigraphLink edge2 : graph.getEdges()) {

							Pair<SimbigraphNode> p1 = graph.getEndpoints(edge1);
							Pair<SimbigraphNode> p2 = graph.getEndpoints(edge2);
							Pair<SimbigraphNode> p3 = new Pair(p2.getSecond(),
									p2.getFirst());
							if ((p1 != p2) && (p1.equals(p2) || p1.equals(p3))) {
								// удали текущуюю вершин и св€жи еЄ соседей
								double d1 = edge1.getP();
								double d2 = edge2.getP();

								graph.removeEdge(edge1);
								edge2.setP(1. - (1. - d1) * (1. - d2));
								next = true;
								_change = true;
								break ex;
							}
						}
					}
				}
				// System.out.println("paral" + graph.getEdgeCount());
				return _change;
			}

			private boolean posled() {
				// System.out.println(graph.getEdgeCount());
				boolean _change = false;
				boolean next = true;
				while (next) {
					next = false;
					for (SimbigraphNode node : graph.getVertices()) {
						if (graph.getIncidentEdges(node).size() == 2) {
							// удали текущуюю вершин и св€жи еЄ соседей
							Iterator<SimbigraphNode> nodeIt = graph
									.getNeighbors(node).iterator();
							Iterator<SimbigraphLink> linksIt = graph
									.getIncidentEdges(node).iterator();

							if (node.state != 5 && node.state != -5) {
								SimbigraphLink link = edgeFactory.create();
								link.setP(linksIt.next().getP()
										* linksIt.next().getP());
								graph.addEdge(link, nodeIt.next(),
										nodeIt.next());
								graph.removeVertex(node);
								next = true;
								_change = true;
								break;
							}
						}
					}
				}
				// System.out.println("posled" + graph.getEdgeCount());
				return _change;
			}

			// /!!!!!!!!!!!!!!!!!!!!!!!!

			private boolean notrelev() {
				boolean _change = false;
				boolean next = true;
				while (next) {
					next = false;
					for (SimbigraphNode node : graph.getVertices()) {
						if (graph.getIncidentEdges(node).size() == 1) {
							if (node.state != 5 && node.state != -5) {
								graph.removeVertex(node);
								next = true;
								_change = true;
								break;
							}
						}
					}
				}
				return _change;
			}
		});
		// =====================================================
		buttonSave.setAlignmentX(Component.CENTER_ALIGNMENT);
		save.add(buttonSave);


		JButton relBtn = new JButton("Reliability prop.");
		relBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				final JSpinner jSpinnerStart = new JSpinner();
				final JSpinner jSpinnerEnd = new JSpinner();
				final JSpinner jSpinnerStep = new JSpinner();
				// final JSpinner jSpinnerCount = new JSpinner();
				final JTextArea jTextPaneConsole = new JTextArea();

				Box controls = Box.createVerticalBox();

				final JFrame frame = new JFrame(
						"Graph reliability properties");
				
				JPanel chartPanel = new JPanel();
				JButton btnLowEst = new JButton("Set of min.cuts");
				btnLowEst.addActionListener(new ActionListener() {
					SimbigraphNode beg, end;

					@Override
					public void actionPerformed(ActionEvent e) {
						// System.out.println("LowEstimation");
						jTextPaneConsole.append("Set of minimal cuts \n");

						Set<bits> setBits = new HashSet();
						// for (int ii = 0; ii < 10; ii++)
						// {
						double ii = 5.;
						for (SimbigraphLink edg : graph.getEdges()) {
							edg.setP(ii * 0.1);
						}

						Graph<SimbigraphNode, SimbigraphLink> gr = copyGraph(graph);
						List<SimbigraphLink> Links = new ArrayList();
						Links.addAll(gr.getEdges());
						ExSearchGenerator EXG = new ExSearchGenerator(Links
								.size());

						if (percolate(gr)) {
							while (EXG.generateNext()) {

								if (EXG.NextInClose()) {
									continue;
								}
								gr = copyGraph(graph);
								bits tmppp = EXG.getNext();
								for (int j = 0; j < tmppp.size(); j++) {
									int i = 0;
									if (tmppp.get(j))
										i = 1;
									if (tmppp.get(j)) {
										SimbigraphLink edge = Links.get(j);
										gr.removeEdge(edge);
									}
								}
								boolean t = true;

								if (!percolate(gr)) {
									// пытаемс€ добавить новый путь
									for (bits b : setBits) {
										if (((b.getLong()) & (tmppp.getLong())) == (b
												.getLong()))
											t = false;
									}
									if (t)
										setBits.add(tmppp.copy());
								}

							}
						}
						// }
						for (bits b : setBits) {
							for (int i = 0; i < b.size(); i++) {
								if (b.get(i))
									// System.out.print(Links.get(i));
									jTextPaneConsole.append("" + Links.get(i));

							}
							jTextPaneConsole.append("\n");
						}
						jTextPaneConsole.append("\n");

					}

					private boolean percolate(
							Graph<SimbigraphNode, SimbigraphLink> gr) {
						;
						Set<SimbigraphNode> Z = new HashSet();
						Set<SimbigraphNode> U1 = new HashSet();
						Z.add(beg);
						for (Iterator<SimbigraphNode> it = gr.getNeighbors(beg)
								.iterator(); it.hasNext();) {
							SimbigraphNode smgNode = it.next();
							if (smgNode.equals(end))
								return true;
							if (!Z.contains(smgNode))
								U1.add(smgNode);
						}
						do {
							Set<SimbigraphNode> U2 = new HashSet();
							for (Iterator<SimbigraphNode> it = U1.iterator(); it
									.hasNext();) {
								SimbigraphNode u1 = it.next();
								for (Iterator<SimbigraphNode> it2 = gr
										.getNeighbors(u1).iterator(); it2
										.hasNext();) {
									SimbigraphNode smgNode = it2.next();
									if (smgNode.equals(end))
										return true;
									if (!Z.contains(smgNode))
										U2.add(smgNode);
								}
								Z.add(u1);
							}
							U1 = U2;

						} while (U1.size() > 0);

						return false;
					}

					private Graph<SimbigraphNode, SimbigraphLink> copyGraph(
							Graph<SimbigraphNode, SimbigraphLink> graph) {
						Graph<SimbigraphNode, SimbigraphLink> gr = new SparseMultigraph();
						for (SimbigraphNode vert : graph.getVertices()) {
							gr.addVertex(vert);
							if (vert.state == 5)
								beg = vert;
							if (vert.state == -5)
								end = vert;
						}
						for (SimbigraphLink edg : graph.getEdges()) {
							Pair<SimbigraphNode> p = graph.getEndpoints(edg);
							gr.addEdge(edg, p.getFirst(), p.getSecond());
						}
						return gr;
					}

				});
				heightConstrain(btnLowEst);
				controls.add(btnLowEst);

				JButton btnMonotonic = new JButton("monotonic search");
				btnMonotonic.addActionListener(new ActionListener() {
					SimbigraphNode beg, end;

					@Override
					public void actionPerformed(ActionEvent e) {
						double start_prob = (Double) jSpinnerStart.getValue();
						double end_prob = (Double) jSpinnerEnd.getValue();
						double step_prob = (Double) jSpinnerStep.getValue();
						jTextPaneConsole.append("Monotonic Search (from "
								+ start_prob + " to " + end_prob + " ("
								+ jSpinnerStep.getValue() + " steps"
								+ "\n");

						long t1 = System.currentTimeMillis();
						int k = 0;
						int vsego = 0;

						for (double ii = start_prob; ii <= end_prob; ii = ii
								+ step_prob) {
							vsego += (int) Math.pow(2, graph.getEdges().size());
							for (SimbigraphLink edg : graph.getEdges())
								edg.setP(ii);

							Set<SimbigraphNode> set = new HashSet();
							double MainP = 1.;

							Graph<SimbigraphNode, SimbigraphLink> gr = copyGraph(graph);
							List<SimbigraphLink> Links = new ArrayList();
							Links.addAll(gr.getEdges());

							ExSearchGenerator EXG = new ExSearchGenerator(Links
									.size());

							if (percolate(gr)) {
								MainP = 1.;
								// NumOfCon = 1;
								for (SimbigraphLink link : graph.getEdges()) {
									MainP *= link.getP();
								}
								double factor = 1.;
								while (EXG.generateNext()) {

									if (EXG.NextInClose()) {
										k++;
										continue;
									}
									gr = copyGraph(graph);
									double phi = 1.;
									bits tmppp = EXG.getNext();
									for (int j = 0; j < tmppp.size(); j++) {
										if (tmppp.get(j)) {
											SimbigraphLink edge = Links.get(j);
											gr.removeEdge(edge);
											phi = phi * (1. - edge.getP())
													/ edge.getP();
										}
									}
									if (percolate(gr)) {
										// NumOfCon++;
										factor += phi;
									} else
										EXG.addClose(tmppp.copy());

								}
								MainP *= factor;
							} else {
								MainP = 0.;
							}
							jTextPaneConsole.append(MainP + "\n");

						}
						long t2 = System.currentTimeMillis();
						jTextPaneConsole.append("dt=" + (t2 - t1) + "\n");
						jTextPaneConsole.append("absenteeism: " + k + ", from "
								+ vsego + "\n");
					}

					private boolean percolate(
							Graph<SimbigraphNode, SimbigraphLink> gr) {
						Set<SimbigraphNode> Z = new HashSet();
						Set<SimbigraphNode> U1 = new HashSet();
						Z.add(beg);
						for (Iterator<SimbigraphNode> it = gr.getNeighbors(beg)
								.iterator(); it.hasNext();) {
							SimbigraphNode smgNode = it.next();
							if (smgNode.equals(end))
								return true;
							if (!Z.contains(smgNode))
								U1.add(smgNode);
						}
						do {
							Set<SimbigraphNode> U2 = new HashSet();
							for (Iterator<SimbigraphNode> it = U1.iterator(); it
									.hasNext();) {
								SimbigraphNode u1 = it.next();
								for (Iterator<SimbigraphNode> it2 = gr
										.getNeighbors(u1).iterator(); it2
										.hasNext();) {
									SimbigraphNode smgNode = it2.next();
									if (smgNode.equals(end))
										return true;
									if (!Z.contains(smgNode))
										U2.add(smgNode);
								}
								Z.add(u1);
							}
							U1 = U2;

						} while (U1.size() > 0);
						return false;
					}

					/*
					 * private boolean percolate(Graph<SimbigraphNode,
					 * SimbigraphLink> gr) {
					 * DijkstraShortestPath<SimbigraphNode, SimbigraphLink> alg
					 * = new DijkstraShortestPath( gr); List<SimbigraphLink> l =
					 * alg.getPath(beg, end); if (l.size() > 0) return true;
					 * return false; }
					 */

					private Graph<SimbigraphNode, SimbigraphLink> copyGraph(
							Graph<SimbigraphNode, SimbigraphLink> graph) {
						Graph<SimbigraphNode, SimbigraphLink> gr = new SparseMultigraph();
						for (SimbigraphNode vert : graph.getVertices()) {
							gr.addVertex(vert);
							if (vert.state == 5)
								beg = vert;
							if (vert.state == -5)
								end = vert;
						}
						for (SimbigraphLink edg : graph.getEdges()) {
							Pair<SimbigraphNode> p = graph.getEndpoints(edg);
							gr.addEdge(edg, p.getFirst(), p.getSecond());
						}
						return gr;
					}

				});
				heightConstrain(btnMonotonic);
				controls.add(btnMonotonic);

				// ----------------------
				JButton btnFullEnumiration = new JButton("full search");
				btnFullEnumiration.addActionListener(new ActionListener() {
					SimbigraphNode beg, end;

					@Override
					public void actionPerformed(ActionEvent e) {
						// System.out.println("FullClearEnumirate");
						double start_prob = (Double) jSpinnerStart.getValue();
						double end_prob = (Double) jSpinnerEnd.getValue();
						double step_prob = (Double) jSpinnerStep.getValue();
						jTextPaneConsole.append("Full Search (from "
								+ start_prob + " to " + end_prob + " ("
								+ step_prob + " steps" + "\n");

						long t1 = System.currentTimeMillis();
						for (double ii = start_prob; ii <= end_prob; ii = ii
								+ step_prob)

						{
							for (SimbigraphLink edg : graph.getEdges())
								edg.setP(ii);
							double MainP = 1.;

							Graph<SimbigraphNode, SimbigraphLink> gr = copyGraph(graph);
							List<SimbigraphLink> Links = new ArrayList();
							Links.addAll(gr.getEdges());
							StrightSearchGenerator EXG = new StrightSearchGenerator(Links.size());
							double factor = 1.;
							while (EXG.generateNext()) {
								gr = copyGraph(graph);
								double phi = 1.;
								bits tmppp = EXG.getNext();
								for (int j = 0; j < tmppp.size(); j++) {
									//int i = 0;
									//if (tmppp.get(j))
									//	i = 1;
									SimbigraphLink edge = Links.get(j);
									if (tmppp.get(j)) {
										gr.removeEdge(edge);
										phi = phi * (1. - edge.getP());
									} else
										phi = phi * edge.getP();
								}
								if (percolate(gr)) {
									factor += phi;
								}
							}
							MainP *= factor;
							jTextPaneConsole.append((MainP-1) + "\n");

						}
						long t2 = System.currentTimeMillis();
						jTextPaneConsole.append("dt=" + (t2 - t1) + "\n");

					}

					private boolean percolate(
							Graph<SimbigraphNode, SimbigraphLink> gr) {
						DijkstraShortestPath<SimbigraphNode, SimbigraphLink> alg = new DijkstraShortestPath(
								gr);
						List<SimbigraphLink> l = alg.getPath(beg, end);
						if (l.size() > 0)
							return true;
						return false;
					}

					private Graph<SimbigraphNode, SimbigraphLink> copyGraph(
							Graph<SimbigraphNode, SimbigraphLink> graph) {
						Graph<SimbigraphNode, SimbigraphLink> gr = new SparseMultigraph();
						for (SimbigraphNode vert : graph.getVertices()) {
							gr.addVertex(vert);
							if (vert.state == 5)
								beg = vert;
							if (vert.state == -5)
								end = vert;
						}
						for (SimbigraphLink edg : graph.getEdges()) {
							Pair<SimbigraphNode> p = graph.getEndpoints(edg);
							gr.addEdge(edg, p.getFirst(), p.getSecond());
						}
						return gr;
					}

				});
				heightConstrain(btnFullEnumiration);
				controls.add(btnFullEnumiration);
				// -------------------------
				JButton btnPath = new JButton("upper bound");
				btnPath.addActionListener(new ActionListener() {
					SimbigraphNode beg, end;

					@Override
					public void actionPerformed(ActionEvent e) {
						// System.out.println("GetPaths");
						double start_prob = (Double) jSpinnerStart.getValue();
						double end_prob = (Double) jSpinnerEnd.getValue();
						double step_prob = (Double) jSpinnerStep.getValue();
						jTextPaneConsole.append("upper bound (from "
								+ start_prob + " to " + end_prob + " ("
								+ jSpinnerStep.getValue() + " steps and "
								+ "\n");

						for (double ii = start_prob; ii <= end_prob; ii = ii+ step_prob) {
							for (SimbigraphLink edg : graph.getEdges()) {
								edg.setP(ii);
							}

							double MainP = 1.;
							Set<SimbigraphNode> set = new HashSet();
							int NumOfCon = 1;

							Graph<SimbigraphNode, SimbigraphLink> gr = copyGraph(graph);
							List<SimbigraphLink> Links = new ArrayList();
							Links.addAll(gr.getEdges());

							ExSearchGenerator EXG = new ExSearchGenerator(Links
									.size());
							int k = 0;

							// -----------------------------
							List list = percolate(gr);
							// -----------------------------
							Set<List<SimbigraphLink>> setPath = new HashSet();

							if ((list.size() > 0)) {
								setPath.add(list);
								k++;
								MainP = 1.;
								// NumOfCon = 1;
								for (SimbigraphLink link : graph.getEdges()) {
									MainP *= link.getP();
								}
								double factor = 1.;
								while (EXG.generateNext()) {
									k++;
									gr = copyGraph(graph);
									double phi = 1.;
									bits tmppp = EXG.getNext();
									// System.out.println();

									for (int j = 0; j < tmppp.size(); j++) {
										int i = 0;
										if (tmppp.get(j))
											i = 1;
										// System.out.print(i);
										if (!tmppp.get(j)) {
											SimbigraphLink edge = Links.get(j);
											gr.removeEdge(edge);
											phi = phi * (1. - edge.getP())
													/ edge.getP();
										}
									}
									List list2 = percolate(gr);

									if ((list2.size() > 0)) {
										setPath.add(list2);
										NumOfCon++;
										// System.out.print("!!" + list2);
										factor += phi;
									}
								}
								MainP *= factor;
							} else {
								MainP = 0.;
							}
							double MainP2 = 1;
							double P9 = 1;

							// //////////////////////////////////////////////////
							for (List<SimbigraphLink> li : setPath) {
								double _p = 1;
								for (SimbigraphLink edge : li) {
									_p = _p * edge.getP();
								}
								P9 = P9 * (1 - _p);
							}

							// /////////////////////////////////////////////////////

							// все единицы
							//double P = 0;
							//double _p = 1;

							/*
							 * StrightSearchGenerator Ex = new
							 * StrightSearchGenerator(setPath.size());
							 * 
							 * List<List<SimbigraphLink>> mass=new ArrayList();
							 * int h=0; for (List<SimbigraphLink> a : setPath) {
							 * mass.add(h++, a) ; }
							 * 
							 * while (Ex.generateNext()) { bits tmppp =
							 * Ex.getNext(); //System.out.println();
							 * Set<SimbigraphLink> poglSet= new HashSet(); int
							 * count=0; for (int j = 0; j < tmppp.size(); j++) {
							 * int i = 0; if (tmppp.get(j)) {i = 1;count++;}
							 * //System.out.print(i); if (!tmppp.get(j)) {
							 * poglSet.addAll(mass.get(j)); } } _p=1; for
							 * (SimbigraphLink edge : poglSet) { _p=_p*
							 * (edge.getP()); } if(count%2==1)_p=_p*(-1);
							 * P=P+_p; }
							 */

							//jTextPaneConsole.append(MainP + "\n");

							jTextPaneConsole.append((1 - P9) + "\n");
						}
					}

					private List percolate(
							Graph<SimbigraphNode, SimbigraphLink> gr) {
						DijkstraShortestPath<SimbigraphNode, SimbigraphLink> alg = new DijkstraShortestPath(
								gr);
						List<SimbigraphLink> l = alg.getPath(beg, end);
						return l;
					}

					private Graph<SimbigraphNode, SimbigraphLink> copyGraph(
							Graph<SimbigraphNode, SimbigraphLink> graph) {
						Graph<SimbigraphNode, SimbigraphLink> gr = new SparseMultigraph();
						for (SimbigraphNode vert : graph.getVertices()) {
							gr.addVertex(vert);
							if (vert.state == 5)
								beg = vert;
							if (vert.state == -5)
								end = vert;
						}
						for (SimbigraphLink edg : graph.getEdges()) {
							Pair<SimbigraphNode> p = graph.getEndpoints(edg);
							gr.addEdge(edg, p.getFirst(), p.getSecond());
						}
						return gr;
					}

				});
				heightConstrain(btnPath);
				controls.add(btnPath);

				JButton btnMST = new JButton("MST measure");
				btnMST.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						// /System.out.println("MSTPaths");
						double start_prob = (Double) jSpinnerStart.getValue();
						double end_prob = (Double) jSpinnerEnd.getValue();
						double step_prob = (Double) jSpinnerStep.getValue();
						jTextPaneConsole.append("N terminal measure (from "
								+ start_prob + " to " + end_prob + " ("
								+ jSpinnerStep.getValue() + " steps and "
								+ "\n");

						long t1 = System.currentTimeMillis();

						for (double ii = start_prob; ii <= end_prob; ii = ii
								+ step_prob) {

							for (SimbigraphLink edg : graph.getEdges()) {
								edg.setP(ii);
							}

							double MainP = 1.;
							Set<SimbigraphNode> set = new HashSet();
							MainP = 1;
							int NumOfCon = 0;

							Graph<SimbigraphNode, SimbigraphLink> gr = copyGraph(graph);
							List<SimbigraphLink> Links = new ArrayList();
							Links.addAll(gr.getEdges());
							// System.out.println(gr.getEdgeCount());

							ExSearchGenerator EXG = new ExSearchGenerator(Links
									.size());
							int k = 0;

							if (percolate(gr)) {
								k++;
								MainP = 1.;
								NumOfCon = 1;
								for (SimbigraphLink link : graph.getEdges()) {
									MainP *= link.getP();
								}
								double factor = 1.;
								while (EXG.generateNext()) {
									k++;
									gr = copyGraph(graph);
									double phi = 1.;
									bits tmppp = EXG.getNext();
									// System.out.println();
									for (int j = 0; j < tmppp.size(); j++) {
										int i = 0;
										if (tmppp.get(j))
											i = 1;
										// System.out.print(i);
										if (tmppp.get(j)) {
											SimbigraphLink edge = Links.get(j);
											gr.removeEdge(edge);
											phi = phi * (1. - edge.getP())
													/ edge.getP();
										}
									}
									if (percolate(gr)) {
										NumOfCon++;
										factor += phi;
									}
								}
								MainP *= factor;
							} else {
								MainP = 0.;
							}
							// System.out.println(MainP);
							jTextPaneConsole.append(MainP + "\n");

						}
						long t2 = System.currentTimeMillis();
						jTextPaneConsole.append("dt=" + (t2 - t1) + "\n");
					}

					private boolean percolate(
							Graph<SimbigraphNode, SimbigraphLink> gr) {
						BFSDistanceLabeler bfsd = new BFSDistanceLabeler();
						bfsd.labelDistances(gr, gr.getVertices().iterator()
								.next());
						;
						return graph.getVertexCount() == bfsd
								.getVerticesInOrderVisited().size();
					}

					private Graph<SimbigraphNode, SimbigraphLink> copyGraph(
							Graph<SimbigraphNode, SimbigraphLink> graph) {
						Graph<SimbigraphNode, SimbigraphLink> gr = new SparseMultigraph();
						for (SimbigraphNode vert : graph.getVertices()) {
							gr.addVertex(vert);

							/*
							 * if (vert.state == 5) beg = vert; if (vert.state
							 * == -5) end = vert;
							 */

						}
						for (SimbigraphLink edg : graph.getEdges()) {
							Pair<SimbigraphNode> p = graph.getEndpoints(edg);
							gr.addEdge(edg, p.getFirst(), p.getSecond());
						}
						return gr;
					}

				});
				heightConstrain(btnMST);
				controls.add(btnMST);
				frame.add(controls, BorderLayout.EAST);

				jTextPaneConsole.setRows(10);

				JPanel params = new JPanel();
				SpinnerNumberModel spinnerNumberModel1 = new SpinnerNumberModel();
				SpinnerNumberModel spinnerNumberModel2 = new SpinnerNumberModel();
				SpinnerNumberModel spinnerNumberModel3 = new SpinnerNumberModel();
				;
				SpinnerNumberModel spinnerNumberModel4 = new SpinnerNumberModel();
				spinnerNumberModel1.setMinimum(0.0);
				spinnerNumberModel2.setMinimum(0.0);
				spinnerNumberModel3.setMinimum(0.1);
				spinnerNumberModel4.setMinimum(1);
				spinnerNumberModel1.setMaximum(1.0);
				spinnerNumberModel2.setMaximum(1.0);
				spinnerNumberModel3.setMaximum(1.0);
				spinnerNumberModel4.setMaximum(10000);
				spinnerNumberModel1.setStepSize(0.01);
				spinnerNumberModel2.setStepSize(0.01);
				spinnerNumberModel3.setStepSize(0.01);
				spinnerNumberModel4.setStepSize(1);
				spinnerNumberModel1.setValue(0.0);
				spinnerNumberModel2.setValue(1.0);
				spinnerNumberModel3.setValue(0.1);
				spinnerNumberModel4.setValue(1);
				jSpinnerStart.setValue(0.0);
				jSpinnerEnd.setValue(1.0);
				jSpinnerStep.setValue(1.0);
				jSpinnerStart.setPreferredSize(new Dimension(60, jSpinnerStart
						.getPreferredSize().height));
				jSpinnerStep.setPreferredSize(new Dimension(60, jSpinnerStep
						.getPreferredSize().height));
				jSpinnerEnd.setPreferredSize(new Dimension(60, jSpinnerEnd
						.getPreferredSize().height));

				jSpinnerStart.setModel(spinnerNumberModel1);
				jSpinnerEnd.setModel(spinnerNumberModel2);
				jSpinnerStep.setModel(spinnerNumberModel3);

				params.add(new JLabel("     From: "));
				params.add(jSpinnerStart);
				params.add(new JLabel("        To: "));
				params.add(jSpinnerEnd);
				params.add(new JLabel("        Step: "));
				params.add(jSpinnerStep);

				frame.add(params, BorderLayout.SOUTH);
				frame.setResizable(false);

				jTextPaneConsole.setFont(new java.awt.Font(
						java.awt.Font.MONOSPACED, java.awt.Font.PLAIN, 14));

				JScrollPane jSPane = new JScrollPane();
				jSPane.setViewportView(jTextPaneConsole);

				frame.add(jSPane, BorderLayout.CENTER);
				frame.setVisible(true);
				frame.pack();
				Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
				int w = frame.getSize().width;
				int h = frame.getSize().height;
				int x = (dim.width-w)/2;
				int y = (dim.height-h)/2;
				frame.setLocation(x, y);

			}

			private void heightConstrain(Component component) {
				Dimension d = new Dimension(140,
						component.getMinimumSize().height);
				component.setMaximumSize(d);
			}

		});
		relBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
		save.add(relBtn, Component.CENTER_ALIGNMENT);

		btnReduction.setAlignmentX(Component.CENTER_ALIGNMENT);
		save.add(btnReduction);

		controls.add(save);
		buttonSave.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel pan = new JPanel(new BorderLayout());
		pan.add(controls, BorderLayout.EAST);
		pan.add(gzsp, BorderLayout.CENTER);
		add(pan);
		validate();
	}

	private void heightConstrain(Component component) {
		Dimension d = new Dimension(component.getMaximumSize().width,
				component.getMinimumSize().height);
		component.setMaximumSize(d);
	}

	private Layout getLayoutFor(Class layoutClass, Graph graph)
			throws Exception {
		Object[] args = new Object[] { graph };
		Constructor constructor = layoutClass
				.getConstructor(new Class[] { Graph.class });
		return (Layout) constructor.newInstance(args);
	}

	EditingModalGraphMouse graphMouse;
	JMenu modeMenu;

	public void createEdgeDecomposition(List<String> graphsCode,
			List<Double> listProp, int steps) {
		GraphMLReader<Graph<SimbigraphNode, String>, SimbigraphNode, String> gmlr = null;

		graph = new SparseMultigraph<SimbigraphNode, SimbigraphLink>();

		SimbigraphNode n1 = vertexFactory.create();
		n1.setState("beg");
		// SimbigraphNode n2 = vertexFactory.create();
		// n2.setState("nor");
		SimbigraphNode n3 = vertexFactory.create();
		n3.setState("end");

		// graph.addEdge(edgeFactory.create(), n1, n2);
		// graph.addEdge(edgeFactory.create(), n2, n3);
		graph.addEdge(edgeFactory.create(), n1, n3);

		for (int i = 0; i < steps; i++) {
			// выбираю подстановку и генерирую
			double r = Math.random();
			double s = 0.0;
			int dec = 0;
			double sum = 0;
			for (int j = 0; j < listProp.size(); j++) {
				sum = listProp.get(j) + sum;
			}
			for (int j = 0; j < listProp.size(); j++) {
				s = s + listProp.get(j) / sum;
				if (r < s) {
					dec = j;
					break;
				}
			}
			loadFromStr(graphsCode.get(dec));
		}
	}

	private void loadFromStr(String string) {
		Graph<SimbigraphNode, SimbigraphLink> g = null;
		System.out.println("===================================");
		StringReader sr = new StringReader(string);
		char f = 0;
		do {
			try {
				f = (char) sr.read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} while (f != '\n');
		GraphMLReader<Graph<SimbigraphNode, SimbigraphLink>, SimbigraphNode, SimbigraphLink> gmlr = null;
		Graph<SimbigraphNode, SimbigraphLink> graph1 = null;
		try {
			gmlr = new GraphMLReader<Graph<SimbigraphNode, SimbigraphLink>, SimbigraphNode, SimbigraphLink>(
					vertexFactory, edgeFactory);
			graph1 = new SparseMultigraph<SimbigraphNode, SimbigraphLink>();
			gmlr.load(sr, graph1);
			g = graph1;
		} catch (SAXException e2) {
			System.out.println("#");
		} catch (ParserConfigurationException e6) {
			System.out.println("##");
		} catch (IOException e5) {
			System.out.println("###" + e5);
		}
		System.out.println("граф=" + g.toString());
		System.out.println("”злов=" + g.getVertexCount());
		System.out.println("–Єбер=" + g.getEdgeCount());

		Transformer<SimbigraphNode, String> t_n = gmlr.getVertexMetadata().get(
				"state").transformer;

		// добавл€ем вершины
		SimbigraphNode k = null, p = null;
		for (SimbigraphNode me : g.getVertices()) {
			me.setState(t_n.transform(me));
			if (me.state == -5)
				k = me;
			else if (me.state == 5)
				p = me;
			else
				graph.addVertex(me);
		}

		// берЄм случайное ребро
		Iterator<SimbigraphLink> it = graph.getEdges().iterator();
		Random r = new Random();
		SimbigraphLink edge = null;
		int rand = r.nextInt(graph.getEdges().size());
		for (int i = 0; i <= rand; i++)
			edge = it.next();

		// получаем начальную и конечную вершуну дл€ ребра
		SimbigraphNode v1;
		SimbigraphNode v2;
		if (graph.getEdgeType(edge).equals(EdgeType.UNDIRECTED)) {
			Pair<SimbigraphNode> pair = graph.getEndpoints(edge);
			v1 = pair.getFirst();
			v2 = pair.getSecond();
		} else {
			v1 = graph.getSource(edge);
			v2 = graph.getDest(edge);
		}

		// удал€ю ребро, которое уже было
		graph.removeEdge(edge);
		// св€зываю начальные и конечные вершины
		// Set<SimbigraphLink> set = new HashSet<SimbigraphLink>();
		Set<SimbigraphLink> remove_set = new HashSet<SimbigraphLink>();
		for (SimbigraphLink ed : g.getIncidentEdges(k)) {
			EdgeType t = g.getEdgeType(ed);
			SimbigraphNode n1 = g.getOpposite(k, ed);
			if (p.equals(n1)) {
				// set.add(ed);
				continue;
			}

			if (t == EdgeType.UNDIRECTED)
				graph.addEdge(edgeFactory.create(), v1, n1, EdgeType.UNDIRECTED);
			else {
				if (g.getSource(ed).equals(k))
					graph.addEdge(edgeFactory.create(), v1, n1,
							EdgeType.DIRECTED);
				else
					graph.addEdge(edgeFactory.create(), n1, v1,
							EdgeType.DIRECTED);
			}
			remove_set.add(ed);
		}

		for (SimbigraphLink ed : g.getIncidentEdges(p)) {
			EdgeType t = g.getEdgeType(ed);
			SimbigraphNode n1 = g.getOpposite(p, ed);
			if (k.equals(n1)) {
				// set.add(ed);
				continue;
			}
			if (t == EdgeType.UNDIRECTED)
				graph.addEdge(edgeFactory.create(), v2, n1, EdgeType.UNDIRECTED);
			else {
				if (g.getSource(ed).equals(p))
					graph.addEdge(edgeFactory.create(), v2, n1,
							EdgeType.DIRECTED);
				else
					graph.addEdge(edgeFactory.create(), n1, v2,
							EdgeType.DIRECTED);
			}
			remove_set.add(ed);

		}

		// удал€ю все добавленные к началу и концу вершины
		for (SimbigraphLink ed : remove_set)
			g.removeEdge(ed);

		// добавл€ю новые рЄбра
		Set<SimbigraphLink> newEdges = new HashSet();
		for (SimbigraphLink ed : g.getEdges()) {
			newEdges.add(ed);
		}
		for (SimbigraphLink ed : newEdges) {
			EdgeType t = g.getEdgeType(ed);
			g.getIncidentVertices(ed);
			SimbigraphNode vert1 = g.getEndpoints(ed).getFirst();
			SimbigraphNode vert2 = g.getEndpoints(ed).getSecond();

			if (t == EdgeType.UNDIRECTED)
				graph.addEdge(edgeFactory.create(), vert1, vert2,
						EdgeType.UNDIRECTED);
			else {
				if (g.getSource(ed).equals(vert2))
					graph.addEdge(edgeFactory.create(), vert1, vert2,
							EdgeType.DIRECTED);
				else
					graph.addEdge(edgeFactory.create(), vert2, vert1,
							EdgeType.DIRECTED);
			}
			g.removeEdge(ed);
		}

		// добавл€ю все оставшиес€ вершины

		// отдельно обрабатываю св€зь от начала до конца
		/*
		 * for (SimbigraphLink edg : set) { EdgeType t = g.getEdgeType(edg); if
		 * (t == EdgeType.UNDIRECTED) graph.addEdge(edgeFactory.create(), v2,
		 * v1, EdgeType.UNDIRECTED); else { if (g.getSource(edg).equals(k))
		 * graph.addEdge(edgeFactory.create(), v1, v2, EdgeType.DIRECTED); else
		 * graph.addEdge(edgeFactory.create(), v2, v1, EdgeType.DIRECTED); } }
		 */
	}

}
