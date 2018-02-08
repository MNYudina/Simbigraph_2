package simbigraph.grid.views.panels2d;

import java.awt.Dimension;

import javax.swing.JPanel;

import simbigraph.core.Simulation;
import simbigraph.grid.model.Grid;

public class Abstract2DPanel extends JPanel{
	public int sizeCell = 15;
	public Grid grid;
	protected Simulation sim;


	public void updatePrefferedSize() {
		int x = grid.getDimensions().getWidth() * sizeCell;
		int y = grid.getDimensions().getHeight() * sizeCell;
		setPreferredSize(new Dimension(x, y));
		setSize(new Dimension(x, y));
	}
}
