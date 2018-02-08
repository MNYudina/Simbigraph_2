package simbigraph.grid.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import simbigraph.core.CompilingClassLoader;
import simbigraph.core.Context;
import simbigraph.core.JavaFileMaker;
import simbigraph.core.Simulation;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.grid.pseudogrid.SimControlPanel;
import simbigraph.grid.views.panels2d.Abstract2DPanel;
import simbigraph.grid.views.panels2d.HexaPanel;
import simbigraph.grid.views.panels2d.SquarePanel;
import simbigraph.projections.Projection;
import texts.GetText;

public class MainGrid3D extends SimControlPanel {
	Abstract3DPanel pan;

	public static enum TYPEGRID  {HEXA,SQUARE,TRIANGLE};
	private static final long serialVersionUID = 1L;
	public static  int sellSize=15;
//	public Grid grid;

	 
	public MainGrid3D() {
		super();
		//setLayout(new BorderLayout());
		
	}

	
	public void init(boolean isHex, int d) {
		Grid grid = Context.getGrid();

		GridDimensions dim = grid.getDimensions();
		pan = new Abstract3DPanel(d, isHex);

		JScrollPane sp = new JScrollPane(pan);
		add(sp);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//pan.setVisible(true);
		//add(box, BorderLayout.NORTH);
		super.init(pan);
		//pan.sizeCell = sim.getSizeCell();
		pan.init();

	}
	
	
	private void updateTimerActionPerformed(java.awt.event.ActionEvent evt)
	{
		pan.repaint();
	}

	public void setSim(Simulation sim2) {
		// TODO Auto-generated method stub
		sim=sim2;
	}


	public void upd() {
		pan.setSim(sim);
	}	
	

	
}
