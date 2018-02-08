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
import simbigraph.graphs.decomposition.GraphDecompositionPanel;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.views.GraphModelingPanel;


public class DecompositionGraphProjection extends Projection
{
	private GraphDecompositionPanel mn;
	public DecompositionGraphProjection()
	{
		super();
		setType("ProjGraphDecomposition");

 		String[] EvolvSteps1 = {"int", "EvolvSteps", "50", "Evolv. Steps", "spinner:0,10000000"};
 		getProperties().add(EvolvSteps1);

 		String[] Method = {"String", "Source", "Alone Vertex", "Seed Graph", "combobox:Alone Vertex,File"};
 		getProperties().add(Method);
 		
 		String[] FileName = {"String", "FileName", "", "Seed Graph", "filechooser","Source=File"};
 		getProperties().add(FileName);
		
 		String[] panelDecompItems = {"dec","DecompItems", "", "Item", "itemlist"};
 		getProperties().add(panelDecompItems);

 		
 		String[] but = {"String","but", "", "", "button"};
 		getProperties().add(but);

 		mn= new GraphDecompositionPanel();
 		mn.setVisible(true);
 		mn.validate();
	}
	public GraphDecompositionPanel getPanel() {
		return mn;
	}
	@Override
	public void update() {
		mn.vv.repaint();
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
