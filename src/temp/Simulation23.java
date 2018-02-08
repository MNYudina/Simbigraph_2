package temp;


import java.awt.Color;
import java.util.Iterator;
import org.apache.commons.collections15.Factory;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.delaune.DelanuneyAgent;
import simbigraph.engine.SimState;
import simbigraph.graphs.views.GraphModelingPanel;
import simbigraph.gui.MainFrame;
import simbigraph.projections.NetworksProjection;
import edu.uci.ics.jung.graph.Graph;


public class Simulation23 extends Simulation{
	public class InfAgent extends DelanuneyAgent {	
	
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

		public InfAgent(double x, double y) {
			super(x,y);
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
			Graph net = Context.getGraph();
			if (SIRstate == 0) {
				Iterator<Object> it = net.getNeighbors(this).iterator();
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

	public  Factory<Object> agentFactory = new Factory<Object>(){
		public InfAgent create() {
			return new InfAgent(0,0);
		}
		
	};
		
{
	agentFactory= new Factory<Object>(){
		public Object create() {
			return new Object();
		}};
}
	
	private final long serialVersionUID = 1L;


	public void step(SimState state) {
	/*	Graph<Object,Integer> net = MainNet.graph;
		for (Iterator<Object> iterator = net.getVertices().iterator(); iterator.hasNext();) {
			iterator.next().step();
		}*/
		
	}
	
	public void start(){
		schedule.scheduleRepeating(this);
		final Simulation sim= this;

        new Thread(new Runnable()
        {
        public void run()
            {
        	long steps=0;
        	while(steps < 10)
            {
        		synchronized(sim)
        		{
        		System.out.println("step");
        		if (!schedule.step(sim))
                    break;
    	         steps = schedule.getSteps();
        		}
        		MainFrame.selectedSBGNode.update();
        		try {
					Thread.sleep(1000);
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
		return new Class[] { InfAgent.class };
	}

	@Override
	public Color getAgentColor(Object obj) {
		Color col = ((InfAgent)obj).getColor();
		return col;
	}

	@Override
	public void init(Object env) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Factory getAgentFactory() {
		return new Factory<InfAgent>(){
			@Override
			public InfAgent create() {
				return new InfAgent(0,0);
			}
		};
		}

}
