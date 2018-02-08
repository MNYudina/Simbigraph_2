package simbigraph.graphs.neighborhood;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingWorker;

import org.apache.commons.collections15.Factory;

import simbigraph.core.Simulation;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.SingleOccupancyCellAccessor;
import simbigraph.grid.model.StickyBorders;
import simbigraph.grid.model.WrapAroundBorders;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class MainNeiGraph extends JPanel {

	private static final long serialVersionUID = 1L;
	Simulation sim;		
	public static Grid<Agent> grid;
	public static Graph<Agent, Integer> graph;//=new UndirectedSparseGraph<Agent, Integer>();;
	public static Factory<Integer> edgeFactory = new Factory<Integer>() {
		int n = 0;
		public Integer create() {
			return n++;
		}
	};


	MyJPanel2 drawingPanel;
	public static  int sellSize=30;
	public static  double density=30;
	public static List<Pair<Double>> listPair;

	  private void heightConstrain(Component component) {
	    	Dimension d = new Dimension(component.getMaximumSize().width,
	    			component.getMinimumSize().height);
	     	component.setMaximumSize(d);
	    }
	public MainNeiGraph() {
		super();
	}
	private javax.swing.Timer updateTimer;
	private int FRAMERATE = 100000;

	public void setParameters(int i, int j, double p,  List<Pair<Double>> list) {
		density = p;
		listPair=list;
		MainNeiGraph.grid = new DefaultGrid(new StickyBorders(),
				new SingleOccupancyCellAccessor<Agent>(),i, j);
		GridDimensions dim = grid.getDimensions();
	
		drawingPanel = new MyJPanel2();
		drawingPanel.setLayout(new java.awt.BorderLayout());
		drawingPanel.setPreferredSize(new java.awt.Dimension(600, 600));
        JButton plus = new JButton("+");
        plus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	sellSize=sellSize+1;
            	drawingPanel.repaint();
            }
        });
        JButton minus = new JButton("-");
        minus.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	if(sellSize>2)sellSize=sellSize-1;
            	drawingPanel.repaint();
            }
        });
        Dimension space = new Dimension(20,20);
        Box controls = Box.createVerticalBox();
        JPanel zoomControls = new JPanel(new GridLayout(1,2));
        zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
        zoomControls.add(plus);        zoomControls.add(minus);
        heightConstrain(zoomControls);
        controls.add(zoomControls);
        controls.add(Box.createRigidArea(space));
		
		JPanel pan=new JPanel(new BorderLayout());
        pan.add(controls,BorderLayout.EAST);
        JScrollPane pane = new JScrollPane(drawingPanel);
        drawingPanel.setSize(dim.getWidth()*sellSize, dim.getHeight()*sellSize);
      
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pan.add(pane, BorderLayout.CENTER);
        pane.setSize(300, 300);
        add(pan);
        add( pan, java.awt.BorderLayout.CENTER);
		validate();
		updateTimer = new javax.swing.Timer(FRAMERATE, new java.awt.event.ActionListener()
		{
			public void actionPerformed(java.awt.event.ActionEvent evt)
			{
				updateTimerActionPerformed(evt);
			}
		});
		updateTimer.start();
	}
	private void updateTimerActionPerformed(java.awt.event.ActionEvent evt)
	{
		drawingPanel.repaint();
		System.out.println("step");
	}
	public void setSim(Simulation sim2) {
		sim=sim2;
	}	

	
}
