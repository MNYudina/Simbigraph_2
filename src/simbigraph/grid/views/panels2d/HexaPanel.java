package simbigraph.grid.views.panels2d;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import simbigraph.core.Simulation;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;

public class HexaPanel extends Abstract2DPanel {
	public Class[] classAgent;
	protected Simulation sim;
	private GridDimensions dim;
	int ShiftX;
	double ShiftY, ShiftX_new, ShiftY_new;
	double MyCos = Math.cos(Math.PI/6);
	double MySin = Math.sin(Math.PI/6);

	

	public HexaPanel(Grid grid, Simulation sim, Integer s, Class[] classAgent) {
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
		int ShiftX;
		double ShiftY, ShiftX_new, ShiftY_new;
		double MyCos = Math.cos(Math.PI/6);
		double MySin = Math.sin(Math.PI/6);
		int vertnum = 0, horiznum = 0;
		if (dim.getWidth() % 2 == 0)
			vertnum = horiznum = dim.getWidth() / 2;
		else
			vertnum = (dim.getWidth() + 1) / 2;
		for (int j = 0; j < vertnum; j++)
		{
			for (int i = 0; i < dim.getHeight(); i++)
			{
				ShiftX = 3 * j * sizeCell;
				ShiftY = 2 * i *sizeCell* MyCos;
				g.drawLine(0 + ShiftX, (int)(sizeCell * MyCos + ShiftY), (int)(sizeCell * MySin + ShiftX), 0 + (int)(ShiftY)); // пр€ма€ AB
				g.drawLine((int)(sizeCell * MySin + ShiftX), 0 + (int)(ShiftY), (int)(sizeCell * (1 + MySin) + ShiftX), 0 + (int)(ShiftY)); // пр€ма€ BC				
				g.drawLine((int)(sizeCell * (1 + MySin) + ShiftX), 0 + (int)(ShiftY), (int)(sizeCell * (1 + 2 * MySin) + ShiftX), (int)(sizeCell * MyCos + ShiftY)); //пр€ма€ CD
				g.drawLine((int)(sizeCell * (1 + 2 * MySin) + ShiftX), (int)(sizeCell * MyCos + ShiftY), (int)(sizeCell * (1 + MySin) + ShiftX), (int)(2 *sizeCell* MyCos + ShiftY)); //пр€ма€ DE
				g.drawLine((int)(sizeCell * (1 + MySin) + ShiftX), (int)(2 *sizeCell* MyCos + ShiftY), (int)(sizeCell * MySin + ShiftX), (int)(2 *sizeCell* MyCos + ShiftY)); // пр€ма€ EF
				g.drawLine((int)(sizeCell * MySin + ShiftX), (int)(2 *sizeCell* MyCos + ShiftY), 0 + ShiftX, (int)(sizeCell * MyCos + ShiftY)); // пр€ма€ FA
			}
		}
		if (dim.getWidth() % 2 == 0)
		{
			g.drawLine((int)(3 *sizeCell* dim.getWidth() / 2), (int)(sizeCell * MyCos), (int)(3 *sizeCell* dim.getWidth() / 2 +sizeCell* MySin), 0);
			g.drawLine((int)(3 *sizeCell* dim.getWidth() / 2), (int)((2 * dim.getHeight() - 1) *sizeCell* MyCos), (int)(3 *sizeCell* dim.getWidth() / 2 +sizeCell* MySin), (int)(dim.getHeight() * 2 *sizeCell* MyCos));
		}
		if (dim.getWidth() % 2 != 0)
			horiznum = (dim.getWidth() - 1) / 2;
		for (int j = 0; j < horiznum; j++)
		{
			for (int i = 0; i < dim.getHeight()-1; i++)
			{
				ShiftX = 3 * j * sizeCell;
				ShiftY = 2 * i *sizeCell* MyCos;
				ShiftX_new =sizeCell* (1 + MySin);
				ShiftY_new =sizeCell* MyCos;
				g.drawLine((int)(ShiftX_new + ShiftX), (int)(ShiftY_new +sizeCell* MyCos + ShiftY), (int)(ShiftX_new +sizeCell* MySin + ShiftX), (int)(ShiftY_new + ShiftY)); 
				g.drawLine((int)(ShiftX_new +sizeCell* MySin + ShiftX), (int)(ShiftY_new + ShiftY), (int)(ShiftX_new +sizeCell* (1 + MySin) + ShiftX),(int)(ShiftY_new + ShiftY)); 
				g.drawLine((int)(ShiftX_new +sizeCell* (1 + MySin) + ShiftX), (int)(ShiftY_new + ShiftY), (int)(ShiftX_new +sizeCell* (1 + 2 * MySin) + ShiftX), (int)(ShiftY_new +sizeCell* MyCos + ShiftY)); 
				g.drawLine((int)(ShiftX_new +sizeCell* (1 + 2 * MySin) + ShiftX), (int)(ShiftY_new +sizeCell* MyCos + ShiftY), (int)(ShiftX_new +sizeCell* (1 + MySin) + ShiftX), (int)(ShiftY_new + 2 *sizeCell* MyCos + ShiftY));
				g.drawLine((int)(ShiftX_new +sizeCell* (1 + MySin) + ShiftX), (int)(ShiftY_new + 2 *sizeCell* MyCos + ShiftY), (int)(ShiftX_new +sizeCell* MySin + ShiftX), (int)(ShiftY_new + 2 *sizeCell* MyCos + ShiftY)); 
				g.drawLine((int)(ShiftX_new +sizeCell* MySin + ShiftX), (int)(ShiftY_new + 2 *sizeCell* MyCos + ShiftY), (int)(ShiftX_new + ShiftX), (int)(ShiftY_new +sizeCell* MyCos + ShiftY));				
			}
		}
		// ќтрисовываю агентов
		for (int t = 0; t < classAgent.length; t++) {
			for (int i = 0; i < dim.getWidth(); i++) {
				for (int j = 0; j < grid.getDimensions().getHeight(); j++) {
					Iterable<Object> it = grid.getObjectsAt(i, j);
					for (Object agent : it) {
						if (classAgent[t].isInstance(agent)) {
							g.setColor(sim.getAgentColor(agent));
							if (i % 2 == 0)
								g.fillOval((int) (sizeCell / 2 + 1.5 * i
										* sizeCell),
										(int) (sizeCell * MyCos + 2 * j
												* sizeCell * MyCos)
												- sizeCell / 2, sim.getAgentSize(agent),
												sim.getAgentSize(agent));
							else
								g.fillOval((int) (sizeCell / 2 + 1.5 * i
										* sizeCell),
										(int) (2 * j * sizeCell * MyCos)
												- sizeCell / 2, sim.getAgentSize(agent),
												sim.getAgentSize(agent));
								}
					}
				}
			}
		}
	}

	
}
