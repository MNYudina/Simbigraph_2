package temp;

import java.awt.Color;

import org.apache.commons.collections15.Factory;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.engine.Steppable;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MooreQuery;

public class SimulationZver extends Simulation {
	//int iterable;
	//public Grid<Trava> grid;
	public abstract class Trava implements Steppable{
		int ienergy = 60;

		public Trava() {
		}

	}

	public class RealTrava extends Trava {
		int ienergy = 50;

		public RealTrava(int energy) {
			ienergy = energy;
		}
		@Override
		public void step(SimState state) {
			ienergy+=3;
		}
	}

	public class Agent extends Trava {
		int ienergy = 250;

		public Agent(int energy) {
			ienergy = energy;
		}

		@Override
		public void  step(SimState state) {
			Grid grid = Context.getGrid();
			GridPoint point = grid.getLocation(this);
			if(point==null)return;

			int x = point.getX();
			int y = point.getY();
			this.ienergy -= 5;
			if (this.ienergy <= 0) {
				grid.remove(this);return;}
				
			MooreQuery<Trava> query = new MooreQuery<Trava>(grid,this);
			boolean neig =false;
			if(!neig){
			if (Math.random() > 0.5)
				x = x + 1;
			else
				x = x - 1;
			if (Math.random() > 0.5)
				y = y + 1;
			else
				y = y - 1;
			}
			
			grid.moveTo(this, x, y);
			Iterable<Trava> it = grid.getObjectsAt(x, y);
			ex:for (Trava i : it) {
				if (i instanceof RealTrava) {
					this.ienergy = this.ienergy + i.ienergy;
					i.ienergy=0;
					//grid.remove(i);
					//System.out.println("סמזנאכ");
					break ex;
				}
			}

			if (this.ienergy <= 0) {
				grid.remove(this);
				
			}
			if (this.ienergy > 260) {
				grid.moveTo(new Agent(Math.round(this.ienergy / 2)), x + 1,
						y + 1);
				this.ienergy = Math.round(this.ienergy / 2);
			}
		}

	}

	public class Zver extends Trava {
		int ienergy = 250;

		public Zver(int energy) {
			ienergy = energy;
		}

		@Override
		public void  step(SimState state) {
			Grid grid = Context.getGrid();
			GridPoint point = grid.getLocation(this);
			if(point==null)return;
			int x = point.getX();
			int y = point.getY();
			if (Math.random() > 0.5)
				x = x + 1;
			else
				x = x - 1;
			if (Math.random() > 0.5)
				y = y + 1;
			else
				y = y - 1;
			

			Iterable<Trava> it = grid.getObjectsAt(x, y);
			ex:for (Trava i : it) {
				if (i instanceof Agent) {
					this.ienergy = this.ienergy + i.ienergy;
					grid.remove(i);break ex;
					
				}
			}
			grid.moveTo(this, x, y);
			this.ienergy -= 5;

			if (this.ienergy <= 0) {
				grid.remove(this);
			}else if (this.ienergy > 260) {
				Zver obj =new Zver(Math.round(this.ienergy / 2));
				grid.moveTo(obj, x + 1,	y + 1);
				schedule.scheduleRepeating(obj);

				this.ienergy = Math.round(this.ienergy / 2);
				if (this.ienergy <= 0) {
					grid.remove(this);
				}
			}
			
		}
	}

	@Override
	public void step(SimState state) {
		Grid grid = Context.getGrid();
		Iterable<Trava> it = grid.getObjects();
		for (Trava obj : it) {
			schedule.scheduleOnce(obj);
		}
		System.out.println("number of agent "+grid.size());
		if(grid.size()==0)schedule.reset();
	}

	@Override
	public void start() {
		super.start();
		schedule.scheduleRepeating(this);	

	}

	@Override
	public void init(Object env) {
		//grid = (Grid) env;
		doAgentStructure();

	}

	private void doAgentStructure() {
		Grid grid = Context.getGrid();

		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getWidth(); i++) {
			for (int j = 0; j < dim.getHeight(); j++) {		
				if(Math.random()<0.6)grid.moveTo(new RealTrava(60),  i, j);
				if(Math.random()<0.05)grid.moveTo(new Agent(255), i, j);
				if(Math.random()<0.05)grid.moveTo(new Zver(255), i, j);
		}}
		
	}
	
	@Override
	public Class[] getVisClass() {
		return new Class[]{RealTrava.class,Agent.class,Zver.class};
	}
	@Override
	public Color getAgentColor(Object obj) {
		Color col =Color.GREEN;
		if (obj instanceof Agent)return Color.BLUE;
		if (obj instanceof Zver)return Color.BLACK;

		return col;
	}

	@Override
	public Factory getAgentFactory() {
		return null;
	}
}
