package simbigraph.gui;

import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.Document;

import org.apache.commons.collections15.Factory;

import simbigraph.core.CompilingClassLoader;
import simbigraph.core.Context;
import simbigraph.core.JavaFileMaker;
import simbigraph.core.Simulation;
import simbigraph.delaune.DelanuneyAgent;
import simbigraph.delaune.DelauneyPanel;
import simbigraph.graphs.decomposition.GraphDecompositionPanel;
import simbigraph.graphs.neighborhood.SimulationNei;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.views.GraphModelingPanel;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.SingleOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.grid.pseudogrid.PseudoLatticePanel;
import simbigraph.grid.views.Grid2DPanel;
import simbigraph.grid.views.MainGrid3D;
import simbigraph.projections.DecompositionGraphProjection;
import simbigraph.projections.KronnekerGraphProjection;
import simbigraph.projections.Lattice2DProjection;
import simbigraph.projections.Lattice3DProjection;
import simbigraph.projections.NeighborhoodGraphProjection;
import simbigraph.projections.NetworksProjection;
import simbigraph.projections.PAttachDetermGraphProjection;
import simbigraph.projections.PAttachStohasticGraphProjection;
import simbigraph.projections.Projection;
import simbigraph.projections.PseudoLatticeProjection;
import simbigraph.projections.TriangulationDelauneyProjection;
import simbigraph.util.Statistic;
import temp.Simulation0;
import temp.Simulation25;
import temp.SimulationGraphSIR;
import temp.SimulationGraphSIRS;
import temp.SimulationPseudo2;
import temp.SimulationPseudoSIR;
import edu.uci.ics.jung.graph.util.Pair;

/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 *          Class of the main application window. Defines the Toolbar, splin
 *          panes, which contain information about network structures and its
 *          properties.
 */
public class MainFrame extends JFrame {
	/**
	 * Since it is possible work with only one projection, make it static
	 */
	// in the future to change, either in singleton, or add the ability to work
	// with multiple projections
	public static Projection selectedSBGNode;

	private HashMap<String, AbstractButton> toolbarButtons = new HashMap<String, AbstractButton>();
	private JTabbedPane jTabbedPaneWorkspace;
	private JTree jTreeProjects;
	private JSplitPane jSplitPane1, jSplitPane2;
	private JTextPane jTextPaneConsole;
	private PropertiesPanel propertiesPanel;
	private File openedFile = null;
	private String lastDir = "";
	private Simulation sim;

	private void doToolBar() {
		JToolBar toolBar = new JToolBar();
		toolBar.setRollover(true);
		toolBar.setFloatable(false);
		toolBar.setVisible(true);
		toolBar.setPreferredSize(new java.awt.Dimension(0, 30));
		add("North", toolBar);
		JButton jButtonView = new JButton();
		jButtonView.setFocusable(false);
		// jButtonSave.setText("New");
		jButtonView.setIcon(new ImageIcon(getClass().getResource(
				"/images/IconNewProject.png")));
		jButtonView.setToolTipText("Build Projection...");
		jButtonView.setMargin(new Insets(3, 5, 3, 5));
		toolBar.add(jButtonView);
		toolbarButtons.put("jButtonNew", jButtonView);
		jButtonView.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {

				CreateModel op = new CreateModel();
				op.execute();
				// doModel();
			}
		});
		JButton jButtonOpen = new JButton();
		jButtonOpen.setFocusable(false);
		jButtonOpen.setIcon(new ImageIcon(getClass().getResource(
				"/images/IconOpen.png")));
		jButtonOpen.setToolTipText("Open...");
		jButtonOpen.setMargin(new Insets(3, 5, 3, 5));
		jButtonOpen.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonOpenActionPerformed(evt);
				for (String nAme : toolbarButtons.keySet())
					toolbarButtons.get(nAme).setEnabled(true);
			}
		});
		toolBar.add(jButtonOpen);
		toolbarButtons.put("jButtonOpen", jButtonOpen);
		toolBar.addSeparator();
		JButton jButtonSave = new JButton();
		jButtonSave.setFocusable(false);
		// jButtonSave.setText("Save");
		jButtonSave.setIcon(new ImageIcon(getClass().getResource(
				"/images/IconSave.png")));
		jButtonSave.setToolTipText("Save");
		jButtonSave.setMargin(new Insets(3, 5, 3, 5));
		jButtonSave.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSaveActionPerformed(evt);
			}
		});
		toolBar.add(jButtonSave);
		toolbarButtons.put("jButtonSave", jButtonSave);
		JButton jButtonSaveAs = new JButton();
		jButtonSaveAs.setFocusable(false);
		jButtonSaveAs.setIcon(new ImageIcon(getClass().getResource(
				"/images/IconSaveAs.png")));
		// jButtonSaveAs.setText("Save as...");
		jButtonSaveAs.setToolTipText("Save as...");
		jButtonSaveAs.setMargin(new Insets(3, 5, 3, 5));
		jButtonSaveAs.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				jButtonSaveAsActionPerformed(evt);
			}
		});
		toolBar.add(jButtonSaveAs);
		toolbarButtons.put("jButtonSaveAs", jButtonSaveAs);
		toolBar.addSeparator();

		for (String nAme : toolbarButtons.keySet()) {
			toolbarButtons.get(nAme).setEnabled(false);
		}
		jButtonOpen.setEnabled(true);
	}

	protected void jButtonStartActionPerformed(ActionEvent evt) {
		sim.start();
	}

	/**
	 * Creates Main Frame of the Project Set images for the elements and
	 * dimensions of the main window
	 */
	public MainFrame(String name) {
		super(name);

		// Назначаю UI представителя
		
		 for (UIManager.LookAndFeelInfo laf : UIManager
		 .getInstalledLookAndFeels()) { if ("Melal".equals(laf.getName())) {
		 try { UIManager.setLookAndFeel(laf.getClassName()); } catch
		 (Exception e) { e.printStackTrace(); } break; } }
		
		// Итак
		setLayout(new java.awt.BorderLayout());
		// Добавляю тулбар на север
		doToolBar();
		// разбиваю центральную часть на две
		int div1loc = 440;
		jSplitPane1 = new JSplitPane();
		jSplitPane1.setDividerLocation(div1loc);
		jSplitPane1.setOneTouchExpandable(true);
		jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
		add("Center", jSplitPane1);
		//
		int div2loc = 790;
		jSplitPane2 = new JSplitPane();
		jSplitPane2.setDividerLocation(div2loc);
		jSplitPane2.setOneTouchExpandable(true);
		jSplitPane2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		JScrollPane jScrollPane1 = new JScrollPane();
		jTabbedPaneWorkspace = new JTabbedPane();
		jTabbedPaneWorkspace
				.addChangeListener(new javax.swing.event.ChangeListener() {
					public void stateChanged(javax.swing.event.ChangeEvent evt) {
						// jTabbedPaneWorkspaceStateChanged(evt);
					}
				});
		jSplitPane2.setLeftComponent(jTabbedPaneWorkspace);

		CustomTreeNode rootProjects = new CustomTreeNode("Projections");
		CustomTreeNode project = new CustomTreeNode("Network-based games");
		rootProjects.add(project);
		project.add(new CustomTreeNode("2D Lattices"));
		project.add(new CustomTreeNode("3D Lattices"));
		project.add(new CustomTreeNode("Pseudo Lattices"));
		project.add(new CustomTreeNode("Deterministic PA-graph"));
		project.add(new CustomTreeNode("Stohastic PA-graph"));
		project.add(new CustomTreeNode("Networks"));
		project.add(new CustomTreeNode("Kronneker graph"));

		CustomTreeNode category = new CustomTreeNode("Network formation games");
		rootProjects.add(category);

		category.add(new CustomTreeNode("Decomposition graph"));
		category.add(new CustomTreeNode("Neigborhood graph"));
		category.add(new CustomTreeNode("Delauney Triangulation"));

		jTreeProjects = new JTree(rootProjects);
		jTreeProjects.setRootVisible(false);
		jTreeProjects.expandRow(2);
		jTreeProjects.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				jTreeProjectsMouseClicked(evt);
			}
		});
		jTreeProjects.setCellRenderer(new TreeIconRenderer());

		JScrollPane jScrollPane2 = new JScrollPane();
		jScrollPane2.setViewportView(jTreeProjects);
		JTabbedPane jTabbedPane2 = new JTabbedPane();
		jTabbedPane2.addTab("Projections", new ImageIcon(getClass()
				.getResource("/images/IconBrowseProject.png")), jScrollPane2);
		jTabbedPane2.setTabComponentAt(0,
				new ButtonTabComponent(jTabbedPane2, new ImageIcon(getClass()
						.getResource("/images/IconBrowseProject.png")), false));
		jSplitPane2.setRightComponent(jTabbedPane2);
		jSplitPane2.setResizeWeight(1);
		jSplitPane1.setTopComponent(jSplitPane2);
		jSplitPane1.setResizeWeight(1);
		propertiesPanel = new PropertiesPanel();
		jTextPaneConsole = new JTextPane();
		jTextPaneConsole.setFont(new java.awt.Font(java.awt.Font.MONOSPACED,
				java.awt.Font.PLAIN, 14));

		JScrollPane jScrollPane6 = new JScrollPane();
		jScrollPane6.setViewportView(jTextPaneConsole);
		JTabbedPane jTabbedPane3 = new JTabbedPane();
		jTabbedPane3.addTab(
				"Properties",
				new ImageIcon(getClass().getResource(
						"/images/IconProperties.png")), propertiesPanel);
		jTabbedPane3.setTabComponentAt(0,
				new ButtonTabComponent(jTabbedPane3, new ImageIcon(getClass()
						.getResource("/images/IconProperties.png")), false));
		jTabbedPane3
				.addTab("Console",
						new ImageIcon(getClass().getResource(
								"/images/IconOutput.png")), jScrollPane6);
		jTabbedPane3.setTabComponentAt(1,
				new ButtonTabComponent(jTabbedPane3, new ImageIcon(getClass()
						.getResource("/images/IconOutput.png")), false));
		jTabbedPane3.setTabPlacement(JTabbedPane.BOTTOM);

		jSplitPane1.setBottomComponent(jTabbedPane3);
		setSize(new Dimension(1000, 700));
		setPreferredSize(new Dimension(1000, 700));
		setLocationRelativeTo(null);
		setVisible(true);
		// redirectSystemStreams();
	}

	/**
	 * set selectedSBGNode(current Projection) depending on the selected
	 * structure in the structure tree\ in MainFrame
	 * 
	 * @param evt
	 *            mouse clip event
	 */
	private void jTreeProjectsMouseClicked(java.awt.event.MouseEvent evt) {
		if (evt.getButton() == MouseEvent.BUTTON1 && evt.getClickCount() == 2
				&& jTreeProjects.getSelectionPath().getPathCount() > 2) {
			System.out.println(jTreeProjects.getSelectionModel()
					.getSelectionPath().getLastPathComponent());
			if (selectedSBGNode != null)
				selectedSBGNode.stop();
			if (new String("Networks").equals(jTreeProjects.getSelectionModel()
					.getSelectionPath().getLastPathComponent().toString())) {

				selectedSBGNode = new NetworksProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString().equals("3D Lattices")) {
				selectedSBGNode = new Lattice3DProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Deterministic PA-graph")) {
				selectedSBGNode = new PAttachDetermGraphProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Stohastic PA-graph")) {
				selectedSBGNode = new PAttachStohasticGraphProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Decomposition graph")) {
				selectedSBGNode = new DecompositionGraphProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString().equals("2D Lattices")) {

				selectedSBGNode = new Lattice2DProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Neigborhood graph")) {
				selectedSBGNode = new NeighborhoodGraphProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Delauney Triangulation")) {
				selectedSBGNode = new TriangulationDelauneyProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Pseudo Lattices")) {
				selectedSBGNode = new PseudoLatticeProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			} else if (jTreeProjects.getSelectionModel().getSelectionPath()
					.getLastPathComponent().toString()
					.equals("Kronneker graph")) {
				selectedSBGNode = new KronnekerGraphProjection();
				jTabbedPaneWorkspace.removeAll();
				jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
						.getLastPathComponent().toString(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.removeAll();
				propertiesPanel.addRows(selectedSBGNode);
			}

			for (String nAme : toolbarButtons.keySet()) {
				toolbarButtons.get(nAme).setEnabled(true);
			}

		}
	}

	private void jButtonSaveActionPerformed(java.awt.event.ActionEvent evt) {
		if (openedFile != null) {
			String projectName = ((CustomTreeNode) jTreeProjects.getModel()
					.getRoot()).getFirstChild().toString();
			SaveProcess sp = new SaveProcess(projectName, openedFile);
			sp.execute();
		} else {
			jButtonSaveAsActionPerformed(evt);
		}
	}

	// сохранить файл проекта
	private void jButtonSaveAsActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		// fc.setSelectedFile(new java.io.File(aO1.getClass().getName()));
		String projectName = ((CustomTreeNode) jTreeProjects.getModel()
				.getRoot()).getFirstChild().toString();
		fc.setSelectedFile(new java.io.File(projectName + ".sbg"));
		fc.setFileFilter(new SbdFilter());
		fc.setAcceptAllFileFilterUsed(false);
		if (lastDir != null)
			fc.setCurrentDirectory(new java.io.File("./"));
		if (fc.showSaveDialog(null) == 0) {
			File selectedFile = fc.getSelectedFile();
			if (selectedFile != null) {
				SaveProcess sp = new SaveProcess(projectName, selectedFile);
				sp.execute();
			}
		}
	}

	// открыть файл проекта
	private void jButtonOpenActionPerformed(java.awt.event.ActionEvent evt) {
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new SbdFilter());
		fc.setAcceptAllFileFilterUsed(false);
		if (lastDir != null)
			fc.setCurrentDirectory(new File("./"));
		if (fc.showOpenDialog(null) == 0) {
			File selectedFile = fc.getSelectedFile();
			if (selectedFile != null) {
				OpenProcess op = new OpenProcess(selectedFile);
				op.execute();
			}
		}
	}

	// /================================================================================
	public static void main(String[] args) {
		MainFrame mf = new MainFrame("SimBiGraph");
		mf.pack();
		mf.setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// =================================================
	class CreateModel extends javax.swing.SwingWorker<Void, Void> {
		ProgressDialog pd = null;

		public CreateModel() {
			// TODO Auto-generated constructor stub
			pd = new ProgressDialog("creating network...");
			// doModel();
		}

		@Override
		protected Void doInBackground() throws Exception {

			doModel();
			return null;
		}

		@Override
		public void done() {
			pd.dispose();
		}
	}

	// /================================================================================
	class SaveProcess extends javax.swing.SwingWorker<Void, Void> {
		String projectName = "";
		File selectedFile = null;
		ProgressDialog pd = null;

		public SaveProcess(String projectName, File selectedFile) {
			this.projectName = projectName;
			this.selectedFile = selectedFile;
			pd = new ProgressDialog("Saving project...");
		}

		@Override
		public Void doInBackground() {
			try {
				javax.xml.stream.XMLOutputFactory factory = javax.xml.stream.XMLOutputFactory
						.newInstance();
				javax.xml.stream.XMLStreamWriter writer = factory
						.createXMLStreamWriter(new FileOutputStream(
								selectedFile));

				writer.writeStartDocument("1.0");
				writer.writeStartElement("Project");
				writer.writeAttribute("TYPE", selectedSBGNode.getType());

				for (int j = 0; j < selectedSBGNode.getProperties().size(); j++) {
					writer.writeStartElement(selectedSBGNode.getProperties()
							.get(j)[1]);
					writer.writeCData(selectedSBGNode.getProperties().get(j)[2]);
					writer.writeEndElement();
				}

				for (int j = 0; j < selectedSBGNode.getEvents().size(); j++) {
					writer.writeStartElement(selectedSBGNode.getEvents().get(j)[0]);
					writer.writeCData(selectedSBGNode.getEvents().get(j)[1]);
					writer.writeEndElement();
				}

				writer.writeEndDocument();

				writer.flush();
				writer.close();

				try {
					BufferedWriter out = new BufferedWriter(
							new java.io.OutputStreamWriter(
									new FileOutputStream("settings.txt")));
					out.write(selectedFile.getPath());
					out.close();

					lastDir = selectedFile.getPath();
				} catch (java.io.IOException e) {
				}

			} catch (Exception e) {
			}

			return null;
		}

		@Override
		public void done() {
			pd.dispose();
		}
	}

	class OpenProcess extends javax.swing.SwingWorker<Void, Void> {
		File selectedFile = null;
		ProgressDialog pd = null;

		public OpenProcess(File selectedFile) {
			this.selectedFile = selectedFile;
			pd = new ProgressDialog("Loading project...");
		}

		@Override
		public Void doInBackground() {
			try {
				jTabbedPaneWorkspace.removeAll();
				propertiesPanel.removeAll();

				javax.xml.parsers.DocumentBuilderFactory dbFactory = javax.xml.parsers.DocumentBuilderFactory
						.newInstance();
				javax.xml.parsers.DocumentBuilder builder = dbFactory
						.newDocumentBuilder();
				org.w3c.dom.Document doc = builder.parse(selectedFile);

				org.w3c.dom.Node projectNode = doc.getFirstChild();
				String type = projectNode.getAttributes().item(0)
						.getNodeValue();

				if (type.equals("Networks")) {
					selectedSBGNode = new NetworksProjection();
				} else if (type.equals("3D Lattices")) {
					selectedSBGNode = new Lattice3DProjection();
				} else if (type.equals("2D Lattices")) {
					selectedSBGNode = new Lattice2DProjection();
				} else if (type.equals("ProjGraphPAttachDeterm")) {
					selectedSBGNode = new PAttachDetermGraphProjection();
				} else if (type.equals("Pseudo Lattices")) {
					selectedSBGNode = new PseudoLatticeProjection();
				}

				else if (type.equals("ProjGraphPAttachStohastic")) {
					selectedSBGNode = new PAttachStohasticGraphProjection();
				} else if (type.equals("ProjGraphNeighborhood")) {
					selectedSBGNode = new NeighborhoodGraphProjection();
					List<String[]> list = selectedSBGNode.getProperties();
					Iterator<String[]> it = list.iterator();
					System.out.println(list.size());
					while (it.hasNext()) {
						String[] a = it.next();
						if (a[1].substring(0, 4).equals("Seed"))
							it.remove();
					}
					System.out.println(list.size());
					for (int ii = 0; ii < projectNode.getChildNodes()
							.getLength(); ii++) {
						org.w3c.dom.Node childNode = projectNode
								.getChildNodes().item(ii);
						if (childNode.getNodeName().substring(0, 4)
								.equals("Seed"))
							selectedSBGNode.setProperty("seed",
									childNode.getNodeName(), "", "Item",
									"seedlist:0.1,0.9");
					}
				} else if (type.equals("ProjGraphDecomposition")) {
					selectedSBGNode = new DecompositionGraphProjection();
					List<String[]> list = selectedSBGNode.getProperties();
					Iterator<String[]> it = list.iterator();
					System.out.println(list.size());
					while (it.hasNext()) {
						String[] a = it.next();
						if (a[1].substring(0, 3).equals("Dec"))
							it.remove();
					}
					System.out.println(list.size());
					for (int ii = 0; ii < projectNode.getChildNodes()
							.getLength(); ii++) {
						org.w3c.dom.Node childNode = projectNode
								.getChildNodes().item(ii);
						if (childNode.getNodeName().substring(0, 3)
								.equals("Dec"))
							selectedSBGNode.setProperty("dec",
									childNode.getNodeName(), "", "Item",
									"itemlist");
					}

				} else
					throw new Exception("Unsupported Projection");

				for (int ii = 0; ii < projectNode.getChildNodes().getLength(); ii++) {
					org.w3c.dom.Node childNode = projectNode.getChildNodes()
							.item(ii);

					selectedSBGNode.setEventOrProperty(childNode.getNodeName(),
							childNode.getTextContent());
				}
				jTabbedPaneWorkspace.addTab(selectedSBGNode.getType(), null,
						selectedSBGNode.getPanel());
				propertiesPanel.addRows(selectedSBGNode);
				try {
					BufferedWriter out = new BufferedWriter(
							new java.io.OutputStreamWriter(
									new FileOutputStream("settings.txt")));
					out.write(selectedFile.getPath());
					out.close();
					lastDir = selectedFile.getPath();
				} catch (java.io.IOException e) {
					System.out.println("my exception");
				}
			} catch (Exception e) {
				System.out.println("!!!!" + e.toString());
			}
			// doModel();
			return null;
		}

		@Override
		public void done() {
			openedFile = selectedFile;
			pd.dispose();
		}
	}

	/**
	 * Create graph using given property of the projection also creates a class
	 * Simulation that is responsible for modeling, Simulation is based on the
	 * code entered in the projection on the basis filling tab "Code" panel
	 * properties
	 */
	void doModel() {
		if (selectedSBGNode instanceof NetworksProjection) {
			sim = createSimulation(selectedSBGNode);
			// sim=new SimulationGraphSIR();
			// sim = new SimulationGraph();
			sim.start();

			NetworksProjection env = (NetworksProjection) selectedSBGNode;
			GraphModelingPanel mn = env.getPanel();

			mn.vertexFactory = sim.getAgentFactory();

			if (env.getProperty("source").equalsIgnoreCase("file")) {

				// грузи из файла
				mn.setNet(env.getProperty("FileName"));
			}
			// генерируй граф
			else {
				if (env.getProperty("Generator").equalsIgnoreCase(
						"Barabasi-Albert"))
					BarabasiAlbertGenerator(mn, env.getProperty("InitNodes"),
							env.getProperty("LinksAmount"),
							env.getProperty("EvolvSteps"));
				else if (env.getProperty("Generator").equalsIgnoreCase(
						"Eppstein"))
					EppsteinGenerator(mn, env.getProperty("NodesAmount"),
							env.getProperty("LinksAmount"),
							env.getProperty("RParameter"));
				else if (env.getProperty("Generator").equalsIgnoreCase(
						"Erdos-Renyi"))
					ErdosRenyiGenerator(mn, env.getProperty("NodesAmount"),
							env.getProperty("LinkProbability"));
				else if (env.getProperty("Generator").equalsIgnoreCase(
						"Kleinberg"))
					KleinbergGenerator(mn, env.getProperty("GridSize"),
							env.getProperty("ClusteringCoefficient"));
				else if (env.getProperty("Generator").equalsIgnoreCase(
						"CurdsBA"))
					TvorogGenerator(mn, env.getProperty("InitNodes"),
							env.getProperty("NodesAmount"));
				else if (env.getProperty("Generator")
						.equalsIgnoreCase("ConfBA"))
					ConfBAGenerator(mn, env.getProperty("NodesAmount"),
							env.getProperty("LinksAmount"));
				else if (env.getProperty("Generator").equalsIgnoreCase(
						"ConfGeneralBA"))
					ConfGeneralBAGenerator(mn, env.getProperty("NodesAmount"),
							env.getProperty("LinksAmount"));
				else
					SimpleGenerator(mn, env.getProperty("LinksAmount"),
							env.getProperty("EvolvSteps"));
			}
			mn.setSim(sim);
			sim.init(null);

			if (mn.vv == null)
				mn.init();

			mn.setVisible(true);
			mn.validate();
		} else if (selectedSBGNode instanceof Lattice3DProjection) {
			Lattice3DProjection env2 = (Lattice3DProjection) selectedSBGNode;
			MainGrid3D mainGrid3DPanel = env2.getPanel();
			// sim=new images.CubeInteractionSimulation();
			// sim=new images.SimulationR();
			sim = createSimulation(selectedSBGNode);

			int d = Integer.valueOf(env2.getProperty("Size"));

			Context.setGrid(new DefaultGrid(new WrapAroundBorders(),
					new MultiOccupancyCellAccessor(), d, d, d));

			boolean isHex = env2.getProperty("GridType")
					.equalsIgnoreCase("Hex");
			mainGrid3DPanel.setSim(sim);

			mainGrid3DPanel.init(isHex, d);
			sim.init(null);
			mainGrid3DPanel.upd();

			validate();
			sim.start();

		} else if (selectedSBGNode instanceof Lattice2DProjection) {
			Lattice2DProjection env2 = (Lattice2DProjection) selectedSBGNode;
			Grid2DPanel grid2DPanel = env2.getPanel();
			sim = createSimulation(selectedSBGNode);
			// sim=new SimulationInYan();
			// sim=new SimulationAuto();
			// sim= new SimulationGridInf();
			// sim=new Simulation0();
			int d = Integer.valueOf(env2.getProperty("Size"));
			Context.setGrid(new DefaultGrid(new WrapAroundBorders(),
					new SingleOccupancyCellAccessor(), d, d));
			sim.init(null);
			grid2DPanel.setSim(sim);
			if (env2.getProperty("GridType").equalsIgnoreCase("HEX"))
				grid2DPanel.init(Grid2DPanel.TYPEGRID.HEXA);
			else if (env2.getProperty("GridType").equalsIgnoreCase("Square"))
				grid2DPanel.init(Grid2DPanel.TYPEGRID.SQUARE);
			else
				grid2DPanel.init(Grid2DPanel.TYPEGRID.TRIANGLE);
			grid2DPanel.repaint();
			// System.out.println("число агентов "+grid2DPanel.grid.size());
			validate();
			sim.start();
		} else if (selectedSBGNode instanceof TriangulationDelauneyProjection) {
			TriangulationDelauneyProjection env2 = (TriangulationDelauneyProjection) selectedSBGNode;
			DelauneyPanel delPan = env2.getPanel();

			jTabbedPaneWorkspace.addTab("Graph", null,
					((TriangulationDelauneyProjection) selectedSBGNode)
							.getPanelGraph());
			delPan.init();
			delPan.repaint();
			validate();
		} else if (selectedSBGNode instanceof PseudoLatticeProjection) {
			PseudoLatticeProjection env = (PseudoLatticeProjection) selectedSBGNode;
			PseudoLatticePanel pslPanel = env.getPanel();
			sim = createSimulation(selectedSBGNode);
			// sim=new SimulationPseudoSIR();
			pslPanel.setSim(sim);
			sim.start();
			Factory<DelanuneyAgent> agFactory = (Factory<DelanuneyAgent>) sim
					.getAgentFactory();
			// sim.init(null);
			pslPanel.init(env.getProperty("FileName"), agFactory);
			sim.init(null);
			pslPanel.repaint();
			validate();
		} else if (selectedSBGNode instanceof NeighborhoodGraphProjection) {
			NeighborhoodGraphProjection env2 = (NeighborhoodGraphProjection) selectedSBGNode;
			List<String> graphsCode = env2.getItemsProperties("seed");
			if (graphsCode == null || graphsCode.size() == 0) {
				JOptionPane.showMessageDialog(null,
						"Please, add all seed properly", "Error!",
						JOptionPane.ERROR_MESSAGE);
				return;
			}
			List<Double> listProp = new ArrayList();
			List<Pair<Double>> list = new ArrayList();
			for (String string : graphsCode) {
				list.add(new Pair(Double.valueOf(string.split(",")[0]), Double
						.valueOf(string.split(",")[1])));
				listProp.add(Double.valueOf(string.split(",")[0]));
			}
			double summD = 0.0;
			for (Double d : listProp) {
				summD = summD + d;
			}
			if (Math.abs(summD - 1.0) > 0.0001) {
				JOptionPane
						.showMessageDialog(
								null,
								"Sum of probabilities getting the graphs decomposition must be 1",
								"Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// sim = createSimulation(selectedSBGNode);
			int dx = Integer.valueOf(env2.getProperty("Size").split(",")[0]);
			int dy = Integer.valueOf(env2.getProperty("Size").split(",")[1]);
			double p = Double.valueOf(env2.getProperty("Density"));
			env2.getPanel().setParameters(dx, dy, p, list);
			sim = new SimulationNei();
			env2.getPanel().setSim(sim);
			env2.getPanel().repaint();
			sim.start();
			jTabbedPaneWorkspace.addTab(jTreeProjects.getSelectionPath()
					.getLastPathComponent().toString()
					+ "Graph", null,
					((NeighborhoodGraphProjection) selectedSBGNode)
							.getPanelGraph());
			validate();

		} else if (selectedSBGNode instanceof PAttachDetermGraphProjection) {
			sim = createSimulation(selectedSBGNode);
			// sim=new SimulationGraphSIR();
			// sim = new Simulation22();
			// selectedSBGNode.setSim(sim);
			PAttachDetermGraphProjection env = (PAttachDetermGraphProjection) selectedSBGNode;
			int dx = 0, dy = 0;
			GraphModelingPanel mn = (GraphModelingPanel) env.getPanel();

			Factory<Object> agFactory = sim.getAgentFactory();
			mn.vertexFactory = agFactory;
			if (env.getProperty("source").equalsIgnoreCase("Real Data")) {
				// грузи из файла
				mn.setNet(env.getProperty("FileName"));
			}
			// генерируй граф
			else {
				int parameter1 = Integer.valueOf(env.getProperty("ParameterM"));
				int steps = Integer.valueOf(env.getProperty("EvolvSteps"));
				boolean isDirected = false;
				Boolean.valueOf(env.getProperty("Directed"));
				mn.setNet(env.getProperty("FileName"));
				PrefferentialAttachment prefRule = createPrefAtachRule(selectedSBGNode);
				mn.EvolveGraph(parameter1, steps, prefRule, isDirected);
			}
			// System.out.println("Загрузил, начинаю отображение");
			mn.setSim(sim);

			if (mn.vv == null)
				mn.init();
			else {
				mn.vv.removeAll();
				mn.init();
			}
			sim.init(null);
			sim.start();

			mn.setVisible(true);
			mn.vv.repaint();
			mn.validate();
		} else if (selectedSBGNode instanceof PAttachStohasticGraphProjection) {
			sim = createSimulation(selectedSBGNode);
			// selectedSBGNode.setSim(sim);
			// sim = new SimulationGraphSIR();

			// selectedSBGNode.setSim(sim);

			PAttachStohasticGraphProjection env = (PAttachStohasticGraphProjection) selectedSBGNode;
			int dx = 0, dy = 0;
			GraphModelingPanel grid2DPanel = (GraphModelingPanel) env
					.getPanel();
			grid2DPanel.vertexFactory = sim.getAgentFactory();
			if (env.getProperty("source").equalsIgnoreCase("Real Data")) {
				// грузи из файла
				grid2DPanel.setNet(env.getProperty("FileName"));
			}
			// генерируй граф
			else {
				String str = env.getProperty("VerTable");
				int steps = Integer.valueOf(env.getProperty("EvolvSteps"));
				String[] doub = str.split("/n");
				double[] d = new double[doub.length];
				double sum = 0.;
				for (int i = 0; i < doub.length; i++) {
					d[i] = Double.valueOf(doub[i]);
					sum = sum + d[i];
				}
				if (Math.abs(sum - 1.0) > 0.0001) {
					JOptionPane
							.showMessageDialog(
									null,
									"Sum of probabilities of the attachments must be 1",
									"Error!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				boolean isDirected = false;// Boolean.valueOf(env.getProperty("Directed"));

				grid2DPanel.setNet(env.getProperty("FileName"));

				PrefferentialAttachment prefRule = createPrefAtachRule(selectedSBGNode);
				grid2DPanel.EvolveGraph(d, steps, prefRule, isDirected);
			}
			grid2DPanel.setSim(sim);
			if (grid2DPanel.vv == null)
				grid2DPanel.init();
			else {
				grid2DPanel.vv.removeAll();
				grid2DPanel.init();
			}
			grid2DPanel.setVisible(true);
			if (grid2DPanel.vv != null)
				grid2DPanel.vv.repaint();
			grid2DPanel.validate();
			sim.init(null);
			sim.start();

		} else if (selectedSBGNode instanceof DecompositionGraphProjection) {
			// sim = createSimulation(selectedSBGNode);

			DecompositionGraphProjection env = (DecompositionGraphProjection) selectedSBGNode;
			GraphDecompositionPanel mn = (GraphDecompositionPanel) env
					.getPanel();
			int steps = Integer.valueOf(env.getProperty("EvolvSteps"));

			List<String> graphsCode = env.getItemsProperties("dec");
			if (graphsCode == null || graphsCode.size() == 0) {
				JOptionPane.showMessageDialog(null,
						"Please, add all decomposition graphs properly",
						"Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			List<Double> listProp = new ArrayList();

			for (String string : graphsCode) {
				BufferedReader br = new BufferedReader(new StringReader(string));
				try {
					listProp.add(Double.valueOf(br.readLine()));
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			double summD = 0.0;
			for (Double d : listProp) {
				summD = summD + d;
			}
			if (Math.abs(summD - 1.0) > 0.000001) {
				JOptionPane
						.showMessageDialog(
								null,
								"Sum of probabilities getting the graphs decomposition must be 1",
								"Error!", JOptionPane.ERROR_MESSAGE);
				return;
			}

			mn.createEdgeDecomposition(graphsCode, listProp, steps);

			if (mn.vv == null)
				mn.setVv();
			else {
				mn.vv.removeAll();
				mn.setVv();
			}
			mn.setVisible(true);
			mn.vv.repaint();

			mn.validate();

			for (String s : graphsCode)
				System.out.println(s);
		}
		for (String nAme : toolbarButtons.keySet()) {
			toolbarButtons.get(nAme).setEnabled(true);
		}
		toolbarButtons.get("jButtonNew").setEnabled(false);
	}

	// создаёт класс, ответственный за моделирование процесса
	// динамически на основе кода занесённой в проекцию
	// на основе заполения вкладки "Code" панели свойств

	private Simulation createSimulation(Projection pr) {
		Class<?> compiledClass = null;
		Simulation s = null;
		try {
			CompilingClassLoader ccl = new CompilingClassLoader();
			// делает текстовый файл
			JavaFileMaker jfm = new JavaFileMaker(pr);
			System.out.println(jfm.getCode());
			// Класс
			compiledClass = ccl.compileClass(jfm.getCode(), "Simulation0");// in
																			// memory
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (compiledClass != null) {
			System.out.println("compiled");
			// compileFailed = true;
		} else {
			System.out.println("not compiled");
			// compileFailed = false;
		}
		try {
			s = (Simulation) compiledClass.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	// /
	private PrefferentialAttachment createPrefAtachRule(Projection pr) {
		Class<?> compiledClass = null;
		PrefferentialAttachment s = null;
		try {
			CompilingClassLoader ccl = new CompilingClassLoader();

			// делает текстовый файл
			JavaFileMaker jfm = new JavaFileMaker(pr);
			jfm.setRule(pr);

			System.out.println(jfm.getCode());
			// Класс
			compiledClass = ccl.compileClass(jfm.getCode(), "prefRule");// in
																		// memory
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		if (compiledClass != null) {
			System.out.println("compiled");
			// compileFailed = true;
		} else {
			System.out.println("not compiled");
			// compileFailed = false;
		}
		try {
			s = (PrefferentialAttachment) compiledClass.newInstance();
			// s = ((PrefferentialAttachment)new PrefAttechRule());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return s;
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	// Реализация генераторов стандартных графов, чего она тут-то???
	// TODO: куда-нить перенести (в соответстующую проекцию, видимо)
	// ///////////////////////////////////////////////////////////////////////////////////
	private void SimpleGenerator(GraphModelingPanel owner, String vert,
			String evs) {
		int vertices;
		vertices = Integer.valueOf(vert);
		int evolvingSteps = Integer.valueOf(evs);
		owner.setMyBAGen(vertices, evolvingSteps);
	}

	private void TvorogGenerator(GraphModelingPanel owner,
			String tfInitVertices, String tfEdgesToAttach) {
		int initVertices, edgesToAttach, evolvingSteps;
		boolean parallel;

		initVertices = Integer.valueOf(tfInitVertices);
		edgesToAttach = Integer.valueOf(tfEdgesToAttach);
		// evolvingSteps = Integer.valueOf(tfEvolvingSteps);

		parallel = false;// chbParallel.isSelected();

		Random rand = new Random();
		owner.setTvorogGen(initVertices, edgesToAttach);
	}

	private void ConfGeneralBAGenerator(GraphModelingPanel owner,
			String tfInitVertices, String tfEdgesToAttach) {
		int initVertices, edgesToAttach, evolvingSteps;
		boolean parallel;

		initVertices = Integer.valueOf(tfInitVertices);
		edgesToAttach = Integer.valueOf(tfEdgesToAttach);

		parallel = false;// chbParallel.isSelected();

		Random rand = new Random();
		owner.setConfGeneralBAGen(initVertices, edgesToAttach);
	}

	private void ConfBAGenerator(GraphModelingPanel owner,
			String tfInitVertices, String tfEdgesToAttach) {
		int initVertices, edgesToAttach, evolvingSteps;
		boolean parallel;

		initVertices = Integer.valueOf(tfInitVertices);
		edgesToAttach = Integer.valueOf(tfEdgesToAttach);

		parallel = true;// chbParallel.isSelected();

		Random rand = new Random();
		owner.setConfBAGen(initVertices, edgesToAttach);
	}

	private void BarabasiAlbertGenerator(GraphModelingPanel owner,
			String tfInitVertices, String tfEdgesToAttach,
			String tfEvolvingSteps) {
		int initVertices, edgesToAttach, evolvingSteps;
		boolean parallel;

		initVertices = Integer.valueOf(tfInitVertices);
		edgesToAttach = Integer.valueOf(tfEdgesToAttach);
		evolvingSteps = Integer.valueOf(tfEvolvingSteps);

		parallel = false;// chbParallel.isSelected();

		Random rand = new Random();
		owner.setBAGen(initVertices, edgesToAttach, evolvingSteps);
	}

	private void EppsteinGenerator(GraphModelingPanel owner,
			String tfEppsteinNumVertices, String tfEppsteinNumEdges, String tfR) {
		int vertices, edges, r;

		vertices = Integer.valueOf(tfEppsteinNumVertices);
		edges = Integer.valueOf(tfEppsteinNumEdges);
		r = Integer.valueOf(tfR);
		owner.setEppstGen(vertices, edges, r);

	}

	private void ErdosRenyiGenerator(GraphModelingPanel owner,
			String tfErdosNumVertices, String tfErdosConnProb) {
		int vertices;
		double prob;

		vertices = Integer.valueOf(tfErdosNumVertices);
		prob = Double.valueOf(tfErdosConnProb);
		owner.setErdosReniyGen(vertices, prob);
	}

	private void KleinbergGenerator(GraphModelingPanel owner,
			String tfKleinbergLatticeSize, String tfKleinbergClustExp) {
		int latticeSize;
		double clustExp;

		latticeSize = Integer.valueOf(tfKleinbergLatticeSize);
		clustExp = Double.valueOf(tfKleinbergClustExp);
		owner.KleinbergGenerator(latticeSize, clustExp);
	}

	// ////////////////////////////////////////////////////////////////////////////////////
	private void redirectSystemStreams() {
		OutputStream out = new OutputStream() {
			@Override
			public void write(int b) throws IOException {
				updateTextPane(String.valueOf((char) b));
			}

			@Override
			public void write(byte[] b, int off, int len) throws IOException {
				updateTextPane(new String(b, off, len));
			}

			@Override
			public void write(byte[] b) throws IOException {
				write(b, 0, b.length);
			}
		};

		System.setOut(new PrintStream(out, true));
		System.setErr(new PrintStream(out, true));
	}

	/*
	 * private void updateTextArea(final String text) {
	 * SwingUtilities.invokeLater(new Runnable() { public void run() {
	 * jTextPaneConsole.append(text); } }); }
	 */
	private void updateTextPane(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Document doc = jTextPaneConsole.getDocument();
				try {
					doc.insertString(doc.getLength(), text, null);
					// jTextPaneConsole.se
				} catch (Exception e) {
				}
				jTextPaneConsole.setCaretPosition(doc.getLength() - 1);
			}
		});
	}

}