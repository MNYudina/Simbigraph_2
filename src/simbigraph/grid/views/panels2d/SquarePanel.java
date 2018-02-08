package simbigraph.grid.views.panels2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import simbigraph.core.Simulation;
import simbigraph.grid.model.Grid;

public class SquarePanel extends Abstract2DPanel {
	public Class[] classAgent;

	public SquarePanel(Grid grid, Simulation sim, Integer s, Class[] classAgent) {
		this.sim = sim;
		this.classAgent = classAgent;
		sizeCell = s;
		this.grid = grid;
		setPreferredSize(new Dimension(grid.getDimensions().getWidth()
				* sizeCell, grid.getDimensions().getHeight() * sizeCell));
	}

	@Override
	public void paint(Graphics gr) {
		super.paint(gr);
		Graphics2D g = (Graphics2D)gr;
		g.setColor(Color.BLACK);
		for (int i = 0; i < grid.getDimensions().getHeight() + 1; i++) {
			g.drawLine(0, i * sizeCell, sizeCell
					* grid.getDimensions().getWidth(), i * sizeCell);
		}
		// vertical
		for (int i = 0; i < grid.getDimensions().getWidth() + 1; i++) {
			g.drawLine(i * sizeCell, 0, i * sizeCell, sizeCell
					* grid.getDimensions().getHeight());
		}
		int count = 0;
		// Отрисовываю агентов по массиву переданных цветов
		for (int t = 0; t < classAgent.length; t++) {
			for (int i = 0; i < grid.getDimensions().getWidth(); i++) {
				for (int j = 0; j < grid.getDimensions().getHeight(); j++) {
					Iterable<Object> it = grid.getObjectsAt(i, j);
					for (Object agent : it) {
						if (classAgent[t].isInstance(agent)) {
							g.setColor(sim.getAgentColor(agent));
							// g.setColor(Color.RED);
							/*Shape oval = new Ellipse2D.Float(-sim.getAgentSize(agent)/2, sim.getAgentSize(agent)/2, sim.getAgentSize(agent), sim.getAgentSize(agent));
							g.translate(i * sizeCell+sizeCell/2, j * sizeCell+sizeCell/2);
							g.draw(oval);*/
							//g.translate(0, 0);
							g.fillOval(i * sizeCell, j * sizeCell,
									//sim.getAgentSize(agent) , sim.getAgentSize(agent) );
									sizeCell , sizeCell );
						}
					}
				}
			}
		}
	}
}
