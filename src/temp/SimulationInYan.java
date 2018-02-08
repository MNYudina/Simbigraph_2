package temp;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;

public class SimulationInYan extends Simulation {
	class In  {
		public Color color = Color.BLUE;

		public Color getColor() {
			return color;
		}
	}

	class Yan {
		public Color color = Color.GREEN;

		public Color getColor() {
			return color;
		}
	}

	List<In> inSet = new ArrayList();
	List<Yan> yanSet = new ArrayList();
	// Grid<SimgraphNode> grid;
	CellAccessor<Object, Map<GridPoint, Object>> accessor = new MultiOccupancyCellAccessor<Object>();

	private void doStructure() {
		Grid grid = Context.getGrid();
		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getWidth(); i++)
			for (int j = 0; j < dim.getHeight(); j++) {
				if ((simbigraph.math.RandomHelper.getUniform()
						.nextDoubleFromTo(0.0d, 1.0d) > 0.6)) {
					Object a = null;
					if (simbigraph.math.RandomHelper.getUniform()
							.nextDoubleFromTo(0.0d, 1.0d) > 0.5) {
						a = new In();
						inSet.add((In) a);
					} else {
						a = new Yan();
						yanSet.add((Yan) a);
					}
					grid.moveTo(a, i, j);
				}
			}
		return;
	}

	// -----------------------------------------------------------------------
	private int NumYanNeighbors;
	private int NumInNeighbors;
	private ArrayList deathList = new ArrayList();
	private Map<Object, GridPoint> birthList = new HashMap<Object, GridPoint>();

	public void birthAgent(int i, int j, Object newAgent) {
		birthList.put(newAgent, new GridPoint(i, j));
	}

	public void dieAgent(Object protoAgent) {
		deathList.add(protoAgent);
	}

	public int getNumInYanNeighbors(int x, int y) {
		Grid grid = Context.getGrid();
		GridDimensions dims = grid.getDimensions();
		NumYanNeighbors = 0;
		NumInNeighbors = 0;
		int ncount = 0;
		try {
			for (int j = y - 1; j <= y + 1; j++) {
				for (int i = x - 1; i <= x + 1; i++) {
					if (!(j == y && i == x)) {
						for (Object agent : grid.getObjectsAt(i, j)) {
							if (agent instanceof Yan) {
								NumYanNeighbors++;
							}
							if (agent instanceof In) {
								NumInNeighbors++;
							}
							ncount++;
						}
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error!!!");
		}
		return ncount;
	}

	public void step(SimState state) {
		Grid grid = Context.getGrid();
		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getDimension(0); i++) {
			for (int j = 0; j < dim.getDimension(1); j++) {
				Object agent = null;
				for (Object agentt : grid.getObjectsAt(i, j))
					agent = agentt;
				int numNeighbor = getNumInYanNeighbors(i, j);
				if (agent == null) {
					if (numNeighbor != 0) {
						if (NumYanNeighbors == 2 && NumInNeighbors == 1) {
							In newAgent = new In();
							birthAgent(i, j, newAgent);
						}
						if (NumYanNeighbors == 1 && NumInNeighbors == 2) {
							Yan newAgent = new Yan();
							birthAgent(i, j, newAgent);
						}
					}
				} else {
					if (numNeighbor >= 5 || numNeighbor < 2) {
						dieAgent(agent);
					}
					if (numNeighbor == 4) {
						// if {
						if ((agent instanceof Yan) && (NumInNeighbors != 2))
							dieAgent(agent);
						if ((agent instanceof In) && (NumYanNeighbors != 2))
							dieAgent(agent);
					}
				}
			}
		}
		for (Object a : deathList) {
			grid.remove(a);
		}

		Set set = birthList.entrySet();
		Iterator i = set.iterator();
		while (i.hasNext()) {
			Map.Entry me = (Map.Entry) i.next();
			Object a =  me.getKey();
			GridPoint p = (GridPoint) me.getValue();
			grid.moveTo(a, p.getX(), p.getY());
		}
		birthList = new HashMap();
		deathList = new ArrayList();
	}

	public void start() {
		super.start();
		schedule.scheduleRepeating(this);	
	}
	@Override
	public void init(Object env) {
		doStructure();
	}
	@Override
	public Color getAgentColor(Object obj) {
		Color col = Color.GREEN;
		if (obj instanceof Yan)
			return Color.YELLOW;
		if (obj instanceof In)
			return Color.BLACK;

		return col;
	}
	@Override
	public Factory getAgentFactory() {
		return null;
	}

}