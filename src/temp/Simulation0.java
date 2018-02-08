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
import temp.SimulationGridInf.InfAgent;
public class Simulation0 extends Simulation {
	class CA {
		public int state, oldState;
		public void switchStates(){
			oldState = state;
		}
		public void step(){
			Grid grid = Context.getGrid();
			MooreQuery<CA> query = new MooreQuery(grid,this);
			Iterator<CA> iter = query.query().iterator();
			int a =	iter.next().oldState; 
			state=a;
			while (iter.hasNext()) {
				a=state; state=(a^iter.next().oldState);
			}
		}
	}
	private void doStructure() {
		Grid grid = Context.getGrid();GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getWidth(); i++)
			for (int j = 0; j < dim.getHeight(); j++) grid.moveTo(new CA(), i, j);
		int seedWidth=dim.getHeight()/5;
		for (int j=dim.getHeight()/2-seedWidth/2; j<dim.getHeight()/2+seedWidth/2; j++){
			for (int i=dim.getWidth()/2-seedWidth/2; i<dim.getWidth()/2+seedWidth/2; i++){
				CA ca = (CA) grid.getObjectAt(i,j);	ca.state=1;	}}	return;
	}
	public void step(SimState state) {
		Grid grid = Context.getGrid();
		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getDimension(0); i++) 
			for (int j = 0; j < dim.getDimension(1); j++) ((CA) grid.getObjectAt(i,j)).switchStates();
		for (int i = 0; i < dim.getDimension(0); i++) 
			for (int j = 0; j < dim.getDimension(1); j++) ((CA) grid.getObjectAt(i,j)).step();}
	public void start() {super.start();	schedule.scheduleRepeating(this);}
	@Override	public void init(Object env) {doStructure();}
	@Override
	public Color getAgentColor(Object obj) {
		Color col = Color.GREEN;
		if (obj instanceof CA)
			if (((CA)obj).state==1)return Color.BLACK;
			else return Color.WHITE;
		return col;
	}
	@Override	public Factory getAgentFactory() {
		return null;
	}
}