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

public class Simulation25 extends Simulation {
	public abstract class ProtoAgent implements Steppable{
		int ienergy = 40;
		int demand_energy = 20;

		public ProtoAgent() {
		}

	}

	public class Sugar extends ProtoAgent {
		public Sugar(int energy) {
			ienergy = energy;
		}
		
		@Override
		public void step(SimState state) {
			ienergy+=2;
		}
	}

	public class SugarAgent extends ProtoAgent {
		
		public SugarAgent(int energy) {
			ienergy = energy;
			 ienergy = 150;
			 demand_energy = 80;
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
				
			MooreQuery<ProtoAgent> query = new MooreQuery<ProtoAgent>(grid,this);
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
			Iterable<ProtoAgent> it = grid.getObjectsAt(x, y);
			ex:for (ProtoAgent i : it) {
				if (i instanceof Sugar) {
					this.ienergy = this.ienergy + 20;
					i.ienergy-=20;
					if(i.ienergy<=0)grid.remove(i);
						break ex;
				}
			}

			if (this.ienergy <= 0) {
				grid.remove(this);
				
			}
/*			if (this.ienergy > 260) {
				grid.moveTo(new SugarAgent(Math.round(this.ienergy / 2)), x + 1,
						y + 1);
				this.ienergy = Math.round(this.ienergy / 2);
			}
*/		}

	}
	@Override
	public void step(SimState state) {
		Grid grid = Context.getGrid();
		Iterable<ProtoAgent> it = grid.getObjects();
		for (ProtoAgent obj : it) {
			schedule.scheduleOnce(obj);
		}
		if(grid.size()==0)schedule.reset();
	}

	@Override
	public void start() {
		super.start();
		schedule.scheduleRepeating(this);	

	}

	@Override
	public void init(Object env) {
		doAgentStructure();

	}

	private void doAgentStructure() {
		Grid grid = Context.getGrid();

		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getWidth(); i++) {
			for (int j = 0; j < dim.getHeight(); j++) {			// устанавливаю агента
				if(Math.random()<0.4)grid.moveTo(new Sugar(60),  i, j);
				if(Math.random()<0.2)grid.moveTo(new SugarAgent(255), i, j);

		}}
		
	}
	
	@Override
	public Class[] getVisClass() {
		return new Class[]{Sugar.class,SugarAgent.class};
	}
	@Override
	public Color getAgentColor(Object obj) {
		Color col =Color.YELLOW;
		if (obj instanceof SugarAgent){
			if(((SugarAgent)obj).demand_energy<((SugarAgent)obj).ienergy)	return Color.BLUE; else return Color.GREEN;
		}
		if (obj instanceof Sugar){
			if(((Sugar)obj).demand_energy<((Sugar)obj).ienergy)	return Color.YELLOW; else return Color.RED;
		}
		
		return col;
	}

	@Override
	public Factory getAgentFactory() {
		return null;
	}
}
