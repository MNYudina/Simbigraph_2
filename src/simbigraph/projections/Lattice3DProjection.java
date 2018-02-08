package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;

import javax.swing.JPanel;

import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.grid.views.MainGrid3D;
import texts.GetText;


public class Lattice3DProjection extends Projection
{
	
	private MainGrid3D panel; 

	private static final long serialVersionUID = 1L;
	public Lattice3DProjection()
	{
		super();
		setType("3D Lattices");
 		
 		String[] Size = {"Dimension", "Size", "50", "Size", "spinner:100,1000"};
 		getProperties().add(Size);
 		
 		String[] NanoParticleType = {"String", "GridType", "Cubic", "GridType", "combobox:Hex,Cubic"};
 		getProperties().add(NanoParticleType);
 		MainGrid3D ngn = new MainGrid3D();
		setPanel(ngn);
		
		String str =GetText.getText("Particle");
 		String[] Import = {"Simulation", str};
 		getEvents().clear();
 		getEvents().add(Import);
		
		//sim= createSimulation(selectedSBGNode);
		//ngn.setBounds(0, 0, 400, 500);
		ngn.setVisible(true);
		}

	@Override
	public MainGrid3D getPanel() {
		return panel;
	}

	@Override
	public void update() {
		panel.repaint();
	}

	public void setPanel(MainGrid3D ngn) {
		panel = ngn;
		
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}



	
}
