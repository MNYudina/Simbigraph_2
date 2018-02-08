package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simbigraph.core.Context;
import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.graphs.views.GraphModelingPanel;
import texts.GetText;


public class KronnekerGraphProjection extends Projection
{
	private GraphModelingPanel panel;
	public KronnekerGraphProjection()
	{
		super();
		setType("KronnekerGraph");

		String[] Size = {"Dimension", "Size", "200", "Size", "spinner:2,5000"};
		getProperties().add(Size);
		/*
		String[] VerTable = {"String", "VerTable", "1.0/n", "Stoh. attachment", "table"};
 		getProperties().add(VerTable); 		
*/	 		 		
 		String str =GetText.getText("GraphSim");
 		String[] Import = {"Simulation", str};
 		getEvents().clear();
 		getEvents().add(Import);
	
 		panel= new GraphModelingPanel();
 		panel.setVisible(true);
 		panel.validate();
	}
	public GraphModelingPanel getPanel() {
		return panel;
	}
	@Override
	public void update() {
		panel.vv.repaint();
	}
	@Override
	public void stop() {
		if(panel.simTimer!=null)panel.simTimer.stop();
		if(panel.updateTimer!=null)panel.updateTimer.stop();
		Context.setGraph(null);
		Context.setGrid(null);
	}

}
