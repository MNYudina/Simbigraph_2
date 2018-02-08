package simbigraph.graphs.neighborhood;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections15.Factory;

import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.gui.MainFrame;
import simbigraph.math.RandomHelper;

import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.graph.util.Pair;

public class SimulationNei extends Simulation {

	public static List<LifeModelAgent> currentActiveAgents;
	public static List<DumpAgent> currentDumpsAgents;
	public static List<DumpAgent> forDumpsRemove;
	
	private void createSeedConfiguration() {
		currentActiveAgents = new ArrayList();
		currentDumpsAgents = new ArrayList();
		forDumpsRemove = new ArrayList();
		MainNeiGraph.graph = new UndirectedSparseGraph<Agent, Integer>();
		
		GridDimensions dim = MainNeiGraph.grid.getDimensions();
		int ch = 0;
		for (int i = 0; i < dim.getWidth(); i++) {
			for (int j = 0; j < dim.getHeight(); j++) {
				if ((RandomHelper.getUniform().nextDoubleFromTo(0.0d, 1.0d)) < MainNeiGraph.density) {
					double power =0., sum=0.;
					double d= RandomHelper.getUniform().nextDoubleFromTo(0.0d, 1.0d);
					for(Pair<Double> p:MainNeiGraph.listPair){
						sum= sum+p.getFirst();
						if(d<sum){power=p.getSecond();break;}
						
					}
					LifeModelAgent agent = new LifeModelAgent(i, j, power);
					MainNeiGraph.grid.moveTo(agent, agent.getX(), agent.getY());
					currentActiveAgents.add(agent);
					MainNeiGraph.graph.addVertex(agent);
				}
			}
		}
		return;
	}

	{
		createSeedConfiguration();
	}


	public synchronized void step(SimState state) {
	
		
		if((currentDumpsAgents.size()==0)&&(currentActiveAgents.size()==0)&&(forDumpsRemove.size()==0)){
			state.finish();
			return;
		}
		//пытаюсь из пустышек сделать агентов
		for(DumpAgent dump:currentDumpsAgents)
		{
			dump.step();;
		}
		for(DumpAgent dump:forDumpsRemove)
			currentDumpsAgents.remove(dump);
		forDumpsRemove.clear();
		
		//расставляю вокруг агентов из списка пустуышки и очищаю список
		for(LifeModelAgent agent:currentActiveAgents)
		{
			agent.step();
		}
		currentActiveAgents.clear();
	
	}
	public void start() {
		super.start();
		schedule.scheduleRepeating(this);
		final Simulation sim = this;
		new Thread(new Runnable() {
			public void run() {
				long steps = 0;
				while (steps < Integer.MAX_VALUE) {
					synchronized (sim) {
						if (!schedule.step(sim))
							break;
					}
					MainFrame.selectedSBGNode.update();

					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				finish();
			}
		}).start();
	}
	@Override
	public Class[] getVisClass() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void init(Object env) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Factory getAgentFactory() {
		return new Factory<LifeModelAgent>(){
			@Override
			public LifeModelAgent create() {
				return new LifeModelAgent(0,0,0);
			}
		};
		}


}