package simbigraph.graphs.neighborhood;


import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.SingleOccupancyCellAccessor;
import simbigraph.grid.views.Grid2DPanel;
import simbigraph.math.RandomHelper;


import javassist.bytecode.Descriptor.Iterator;

//------------------------------------------------------------
public class DumpAgent extends Agent {
	CellAccessor<Agent, Map<GridPoint, Object>> accessor = new SingleOccupancyCellAccessor<Agent>();
	Map<LifeModelAgent, Integer> mapLifeAgent // хранит смежные зёрна
	= new HashMap<LifeModelAgent, Integer>();
	Map<LifeModelAgent, Integer> mapRealAgent
	= new HashMap<LifeModelAgent, Integer>();

	// ------------------------------------------------------------
	public DumpAgent(LifeModelAgent lifeModelAgent, int dx, int dy) {
		x = dx;
		y = dy;
		mapLifeAgent.put(lifeModelAgent, 1);
		// this.lifeModelAgent = lifeModelAgent;
	}

	// ------------------------------------------------------------
	public void step() {  // --- в результате шага либо данная пустышка
							// перейдёт на следующий шаг
								// --- либо из неё получится новый агент
	//	ISchedule schedule = RunEnvironment.getInstance().getCurrentSchedule();
		
	//	ScheduleParameters params_forSelf =
	//		ScheduleParameters.createOneTime(schedule.getTickCount() + 1);

	//	ScheduleParameters params_forNewAgent =
	//		ScheduleParameters.createOneTime(schedule.getTickCount());
		
		Set set= mapLifeAgent.entrySet();
		java.util.Iterator i=set.iterator();

		boolean alone = false;
		//if(set.size()>1) alone=true;

		while(i.hasNext())
		{
			Map.Entry<LifeModelAgent, Integer> me =(Map.Entry<LifeModelAgent, Integer>) i.next();

			// rand = RandomHelper.getUniform().nextDoubleFromTo(0.0d, 1.0d);
			LifeModelAgent agent = me.getKey();
			int num =me.getValue();
			for(int j=0; j<num;j++)
				if(RandomHelper.getUniform().nextDoubleFromTo(0.0d, 1.0d)<=agent.getStrendth())
				{
					
					if (mapRealAgent.containsKey(agent)) {
						int k = mapRealAgent.get(agent);
						mapRealAgent.put(agent, ++k);
					} else {
						mapRealAgent.put(agent, 1);
					}
				}
		}
		
		int maxCount=0;
		Set setCand= mapRealAgent.entrySet();
		java.util.Iterator iter_cand=setCand.iterator();
		// список в кандидаты на родительство после получения вероятностей
		List<LifeModelAgent> candidates = new ArrayList<LifeModelAgent>();
		
		while(iter_cand.hasNext())
		{
			Map.Entry<LifeModelAgent, Integer> cand =(Map.Entry<LifeModelAgent, Integer>) iter_cand.next();
			LifeModelAgent agent = cand.getKey();
			int num =cand.getValue();
			if(num>maxCount)
			{
				candidates.clear();
				maxCount=num;
				assert candidates.isEmpty()==true; 
				candidates.add(agent);
			}
			else if(num==maxCount)
			{
				candidates.add(agent);
			}
							
		}
		
		
		if(candidates.size()>0)
		{
			//выбирим случайного предка
			LifeModelAgent lifeAgent=candidates.get(RandomHelper.nextIntFromTo(0, candidates.size()-1));
			
			//создадим из пустышки агента соответствующего предка
			LifeModelAgent newLifeAgent = new LifeModelAgent(this.getX(), 
					this.getY(), lifeAgent.getPar());
			Grid grid = MainNeiGraph.grid;;
		//	context.remove(this);
		//	context.add(newLifeAgent);
			//*************************************************
			GridPoint d = MainNeiGraph.grid.getLocation(this);
			
			// MainNeiGraph.grid.moveTo(null,d.getX(),d.getY());
			 
			  if (d != null) 
			      accessor.remove(this, MainNeiGraph.grid.getLocationStorage(), d);
				  MainNeiGraph.grid.getLocationMap().remove(this);
				  SimulationNei.currentActiveAgents.add(newLifeAgent);
				  SimulationNei.forDumpsRemove.add(this);

			
			//*************************************************
			grid.moveTo(newLifeAgent, newLifeAgent.getX(), newLifeAgent.getY());
		//	schedule.schedule(params_forNewAgent, newLifeAgent, "step");
		//	((lmContext) context).addAgent();

		}
		else
		;//	 schedule.schedule(params_forSelf, this, "step");
	}

	// ------------------------------------------------------------
	public void addToMap(LifeModelAgent lifeModelAgent) {
		if (mapLifeAgent.containsKey(lifeModelAgent)) {
			int i = mapLifeAgent.get(lifeModelAgent);
			mapLifeAgent.put(lifeModelAgent, ++i);
		} else {
			mapLifeAgent.put(lifeModelAgent, 1);
		}
	}

	// ------------------------------------------------------------
	public Color getColor() {
		return Color.RED;
	}

	// ------------------------------------------------------------
	public double P_zahvata(Map<LifeModelAgent, Double> mapAgentVer,
			Double sumOfVer) {

		double p = 1;
		Set set = mapLifeAgent.entrySet();
		java.util.Iterator it = set.iterator();
		while (it.hasNext()) {
			Map.Entry<LifeModelAgent, Integer> me = (Map.Entry<LifeModelAgent, Integer>) it
					.next();
			double strength = ((LifeModelAgent) me.getKey()).getStrendth();
			int ch = me.getValue();

			// заполняем карту вероятностей
			double p1 = (Math.pow(1 - strength, ch));
			mapAgentVer.put(me.getKey(), 1 - p1);

			// Находим произведение для вычисления незахвата
			p = p * p1;
			sumOfVer = p1 + 1 - sumOfVer;
		}
		return 1.0 - p;
	}

	// ------------------------------------------------------------
	public int getX() {
		return x;
	}

	// ------------------------------------------------------------
	public int getY() {
		return y;
	}

}
