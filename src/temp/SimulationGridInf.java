package temp;


import java.awt.Color;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.collections15.Factory;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MooreQuery;
import simbigraph.grid.model.MultiOccupancyCellAccessor;




public class SimulationGridInf extends Simulation {
	class InfAgent {
		//boolean infected = false;
		public int SIRstate = 0;
		int t1 = 4, t2 = 9, t = 0;
		//Color color = Color.RED;
		int n_SIRstate, n_t;

		
	
		public InfAgent() {
			//color=Color.BLACK;
			if (Math.random() < 0.002) {
				SIRstate = 1;
				n_SIRstate=1;
				t = 1;
			}
		}
		 public void step() {
			 	Grid<InfAgent> grid = Context.getGrid();
			 	if (t == 0)
					n_SIRstate = 0;
				else if (t > 0 && t <= t1)
					n_SIRstate = 1;
				else if (t > t1 && t <= t2)
					n_SIRstate = 2;
				else if (t > t2) {
					n_SIRstate = 0;
					t = 0;
				}
				if (n_SIRstate == 0) {
					int k = 4, k_inf = 0;
					MooreQuery<InfAgent> query = new MooreQuery(grid,this);
					Iterator<InfAgent> it= query.query().iterator();
					
					while (it.hasNext()) {
						k++;
						InfAgent n = (InfAgent)it.next();
						if (n.SIRstate == 1) {
							k_inf++;
						}
					}
					//if(k_inf>0)
						if (0.25*Math.random() < k_inf * 1. / (1. * k))
						n_t = 1;
					else
						n_t = 0;
				} else
					n_t=t+1;

		    }
		 public void post_step() {
			 t=n_t;
			 SIRstate=n_SIRstate;
		 }
			public Color getColor() {
				Color color= Color.BLACK;
				//if(isInfected())color=Color.RED;
				if(SIRstate==0) color= Color.BLUE;
				if(SIRstate==1)color= Color.RED;
				if(SIRstate==2)color= Color.GRAY;
				return color;
			}
		
	}	
	CellAccessor<Object, Map<GridPoint, Object>> accessor = new MultiOccupancyCellAccessor<Object>();

	private void doStructure() {
		Grid grid = Context.getGrid();
		GridDimensions dim = grid.getDimensions();
		
		for (int i = 0; i < dim.getWidth(); i++)
			for (int j = 0; j < dim.getHeight(); j++) {
						InfAgent ca = new InfAgent();
						grid.moveTo(ca, i, j);
		}
		
		
		return;
	}

	// -----------------------------------------------------------------------


	public void step(SimState state) {
		//
		Grid grid = Context.getGrid();
		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getDimension(0); i++) {
			for (int j = 0; j < dim.getDimension(1); j++) {
				InfAgent ca = (InfAgent) grid.getObjectAt(i,j);
				ca.step();
			}
		}
		//
		for (int i = 0; i < dim.getDimension(0); i++) {
			for (int j = 0; j < dim.getDimension(1); j++) {
				InfAgent ca = (InfAgent) grid.getObjectAt(i,j);
				ca.post_step();
			}
		}
		
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
		if (obj instanceof InfAgent)
			col=((InfAgent)obj).getColor();
			
	
		return col;
	}

	@Override
	public Factory getAgentFactory() {
		return null;
	}

}