package temp;

import java.awt.Color;
import java.util.Iterator;

import org.apache.commons.collections15.Factory;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import edu.uci.ics.jung.graph.Graph;

public class Simulation22 extends Simulation {

	public class InfAgent {	
		
		boolean infected = false;
		int SIRstate = 0;
		int t1 = 4, t2 = 9, t = 0;
		Color color = Color.RED;
		
		public Color getColor() {
			color= Color.BLACK;
			if(isInfected())color=Color.RED;
			
			return color;
		}
		public boolean isInfected() {
			return infected;
		}

		public InfAgent() {
			color=Color.BLACK;
			if (Math.random() < 0.5) {
				SIRstate = 1;
				t = 1;
				infected = true;
				//color=Color.RED;;
			}
		}

		public void setInfected(boolean infected) {
			this.infected = infected;
			if(infected)color = Color.BLACK;
			else color= Color.RED;
		}

		
	    public void step() {
	    	Graph graph = Context.getGraph();
			if (t == 0)
				SIRstate = 0;
			else if (t > 0 && t <= t1)
				SIRstate = 1;
			else if (t > t1 && t <= t2)
				SIRstate = 2;
			else if (t > t2) {
				SIRstate = 0;
				t = 0;
			}
			if (SIRstate == 0) {
				Iterator<Object> it = graph.getNeighbors(this).iterator();
				int k = 0, k_inf = 0;
				while (it.hasNext()) {
					k++;
					InfAgent n = (InfAgent)it.next();
					if (n.SIRstate == 1) {
						k_inf++;
					}
				}
				if (Math.random() < k_inf * 1. / (1. * k))
					t = 1;
				else
					t = 0;
			} else
				t++;

			if (Math.random() > 0.5)
				infected = true;
			else
				infected = false;
	    }
	}

	//private Graph graph;
	public void step(SimState state) {
	Graph graph = Context.getGraph();
	for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator.hasNext();) {
		((InfAgent)iterator.next()).step();
	}

}

	// Установка задержки для моделирования агентов
	public void start() {
		super.start();
		schedule.scheduleRepeating(this);
	}

	@Override
	public Class[] getVisClass() {
		return new Class[] { InfAgent.class };
	}

	@Override
	public Color getAgentColor(Object obj) {
		Color col = ((InfAgent)obj).getColor();
		return col;
	}

	@Override
	public void init(Object env) {
		//Graph graph = Context.getGraph();
		//graph = (Graph)env;
	}

	@Override
	public Factory getAgentFactory() {
		return new Factory<InfAgent>(){
			@Override
			public InfAgent create() {
				return new InfAgent();
			}
		};
		}

	
}