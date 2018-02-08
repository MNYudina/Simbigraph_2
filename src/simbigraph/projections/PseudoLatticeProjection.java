package simbigraph.projections;

import java.awt.Component;
import java.io.File;

import simbigraph.core.Context;
import simbigraph.delaune.DelaunaeGraphPanel;
import simbigraph.delaune.DelauneyPanel;
import simbigraph.grid.pseudogrid.PseudoLatticePanel;
import simbigraph.grid.views.Grid2DPanel;
import texts.GetText;

public class PseudoLatticeProjection extends Projection {
	private PseudoLatticePanel panel; 
 public PseudoLatticeProjection() {
	 super();
		setType("Pseudo Lattices");
	
		panel = new PseudoLatticePanel();
		panel.setBounds(0, 0, 400, 500);
		panel.setVisible(true);
		
 		File f=new File("graphs/PseudoLattice/graph.net");
 		String[] FileName = {"String", "FileName", f.getAbsolutePath(), "Graph", "filechooser"};
 		getProperties().add(FileName);


		//String[] NanoParticleType = {"String", "NanoParticleType", "Cubic", "NanoParticleType", "combobox:Hex,Cubic"};
		//getProperties().add(NanoParticleType);
 		String st =  GetText.getText("PseudoGraph");
 		String[] Import = {"Simulation", st};
 		getEvents().clear();
 		getEvents().add(Import);
		}
	@Override
	public PseudoLatticePanel getPanel() {

		return panel;
	}

	@Override
	public void update() {
		panel.repaint();		
	}
	@Override
	public void stop() {
		if(panel.simTimer!=null)panel.simTimer.stop();
		if(panel.updateTimer!=null)panel.updateTimer.stop();
		Context.setGraph(null);
		Context.setGrid(null);		
	}

}
