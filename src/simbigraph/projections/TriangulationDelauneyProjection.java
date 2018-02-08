package simbigraph.projections;

import java.awt.Component;

import simbigraph.delaune.DelaunaeGraphPanel;
import simbigraph.delaune.DelauneyPanel;
import simbigraph.grid.views.Grid2DPanel;
import texts.GetText;

public class TriangulationDelauneyProjection extends Projection {
	private DelauneyPanel panel; 
 public TriangulationDelauneyProjection() {
	 super();
		setType("Delauney Triangulation");
		
		String[] Size = {"Dimension", "Size", "200", "Size", "spinner:2,5000"};
		getProperties().add(Size);
		panel = new DelauneyPanel();
		panel.setBounds(0, 0, 400, 500);
		panel.setVisible(true);


		//String[] NanoParticleType = {"String", "NanoParticleType", "Cubic", "NanoParticleType", "combobox:Hex,Cubic"};
		//getProperties().add(NanoParticleType);
		// String str ="в данной проекции моделирование не поддерживается, используйте проекцию Networks";
		// String[] Import = {"Simulation", str};
		// getEvents().add(Import);
		}
	@Override
	public DelauneyPanel getPanel() {

		return panel;
	}

	@Override
	public void update() {
		panel.repaint();		
	}
	public Component getPanelGraph() {
		DelaunaeGraphPanel p=new DelaunaeGraphPanel();
		panel.setGraphView(p);
		return p;
	}
	@Override
	public void stop() {
		// TODO Auto-generated method stub
		
	}

}
