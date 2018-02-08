package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import simbigraph.core.Context;
import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.grid.views.Grid2DPanel;
import simbigraph.grid.views.MainGrid3D;
import texts.GetText;
//import simbigraph.grid.views.SimulationInYan;


/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * 
 */ 

public class Lattice2DProjection extends Projection
{
	private Grid2DPanel panel; 
	private static final long serialVersionUID = 1L;
	public Lattice2DProjection()
	{
		super();
		setType("2D Lattices");
		
 		String[] Size = {"Dimension", "Size", "50", "Size", "spinner:2,1000"};
 		getProperties().add(Size);
 		
 		String[] NanoParticleType = {"String", "GridType", "Square", "GridType", "combobox:Hex,Square,Triangle"};
 		getProperties().add(NanoParticleType);
		Grid2DPanel ngn = new Grid2DPanel();
		setPanel(ngn);
		String str =GetText.getText("ZverGridSim");
		
 		String[] Import = {"Simulation", str};
 		getEvents().add(Import);
		ngn.setBounds(0, 0, 400, 500);
		ngn.setVisible(true);

		}

	@Override
	public Grid2DPanel getPanel() {
		return panel;
	}

	@Override
	public void update() {
		panel.repaint();
	}

	public void setPanel(Grid2DPanel ngn) {
		panel = ngn;
	}

	@Override
	public void stop() {
		if(panel.simTimer!=null)panel.simTimer.stop();
		if(panel.updateTimer!=null)panel.updateTimer.stop();
		Context.setGraph(null);
		Context.setGrid(null);
	}
}
