package simbigraph.grid.views.panels2d;
/**
 * @author Vitalyi Ustyanzcev
 */
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import simbigraph.core.Simulation;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;

public class TrianglePanel extends Abstract2DPanel {
	public Class[] classAgent;
	protected Simulation sim;
	private GridDimensions dim;
	int ShiftX;
	double ShiftY, ShiftX_new, ShiftY_new;
	double MyCos = Math.cos(Math.PI / 6);
	double MySin = Math.sin(Math.PI / 6);

	public TrianglePanel(Grid grid, Simulation sim, Integer s,
			Class[] classAgent) {
		this.sim = sim;
		this.classAgent = classAgent;
		sizeCell = s;
		this.grid = grid;
		setPreferredSize(new Dimension(grid.getDimensions().getWidth()
				* sizeCell, grid.getDimensions().getHeight() * sizeCell));
		dim = grid.getDimensions();

	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);

		for (int i = 0; i < grid.getDimensions().getHeight() + 1; i++) {
			g.drawLine(0, i * sizeCell, sizeCell
					* grid.getDimensions().getWidth(), i * sizeCell);
		}
		// vertical
		for (int i = 0; i < grid.getDimensions().getWidth() + 1; i++) {
			g.drawLine(i * sizeCell
					- (sizeCell * grid.getDimensions().getWidth() - 1), 0, i
					* sizeCell, sizeCell * grid.getDimensions().getHeight());
		}

		for (int i = 0; i < grid.getDimensions().getWidth() + 1; i++) {
			g.drawLine(i * sizeCell, 0, i * sizeCell, sizeCell
					* grid.getDimensions().getHeight());
		}

		for (int i = 0; i < grid.getDimensions().getHeight() + 1; i++) {
			g.drawLine(2, i * sizeCell
					- (sizeCell * grid.getDimensions().getHeight() - 1),
					sizeCell * grid.getDimensions().getWidth(), i * sizeCell);
		}
		int count = 0;

		for (int t = 0; t < classAgent.length; t++) {
			for (int i = 0; i < 2 * grid.getDimensions().getWidth(); i++) {
				for (int j = 0; j < grid.getDimensions().getHeight(); j++) {

					Iterable<Object> it = grid.getObjectsAt(i, j);
					for (Object agent : it) {
						if (classAgent[t].isInstance(agent)) {
							g.setColor(sim.getAgentColor(agent));
							if (i % 2 == 0) {
								g.fillOval((i * sizeCell) / 2, j * sizeCell
										+ 15, sizeCell / 2, sizeCell / 2);
								// g.fillOval((i * sizeCell)/2, j *
								// sizeCell+((sizeCell*grid.getDimensions().getHeight())/(sizeCell)),
								// sizeCell/2,
								// sizeCell/2);
							} else {
								g.fillOval((i * sizeCell) / 2, j * sizeCell,
										sizeCell / 2, sizeCell / 2);
							}
						}
					}
				}
			}
		}

	}
}
