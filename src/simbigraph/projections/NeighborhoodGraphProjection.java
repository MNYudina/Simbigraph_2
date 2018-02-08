package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.graphs.neighborhood.MainNeiGraph;
import simbigraph.graphs.neighborhood.PanelGraphView;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.views.GraphModelingPanel;
import simbigraph.grid.views.Grid2DPanel;


public class NeighborhoodGraphProjection extends Projection
{
	private MainNeiGraph panel; 

	public NeighborhoodGraphProjection()
	{
		super();
		
		setType("ProjGraphNeighborhood");
 		
		String[] Size = {"Dimension", "Size", "200,200", "Size", "size"};
 		getProperties().add(Size);
 		
 		String[] prob = {"double", "Density", "0.001", "Density", "spinnerProb:0.0001,0.9999"};
 		getProperties().add(prob);
 		
 		String[] panelDecompItems = {"seed","SeedItem", "1.,0.2", "Item", "seedlist:0.1,1.0"};
 		getProperties().add(panelDecompItems);
 		
 		String[] but = {"String","but2", "", "", "button2"};
 		getProperties().add(but);

 		MainNeiGraph ngn = new MainNeiGraph();

		setPanel(ngn);
		ngn.setBounds(0, 0, 400, 500);
		ngn.setVisible(true);
	}

	@Override
	public MainNeiGraph getPanel() {
		
		return panel;
	}

	@Override
	public void update() {
		panel.repaint();
	}

	public void setPanel(MainNeiGraph ngn) {
		panel = ngn;
	}

	public Component getPanelGraph() {
		return new PanelGraphView();
	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
