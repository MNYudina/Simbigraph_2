package simbigraph.graphs.prefAttachment;

import static simbigraph.grid.model.Snippet._f2;
import static simbigraph.grid.model.Snippet._f3;
import static simbigraph.grid.model.Snippet.mass1;
import static simbigraph.grid.model.Snippet.mass2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;

import org.apache.commons.collections15.Factory;

import simbigraph.engine.SimState;
import simbigraph.engine.Steppable;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.gui.MainFrame;
import simbigraph.projections.NetworksProjection;

import edu.uci.ics.jung.graph.Graph;

public class Simulation3 {/*extends SimState implements Steppable{
	
	final double K = (1.602176530 / 1.3806505) * 10000.0;// electron-volt divide
	// by Boltzmann's
	// constant
	double T = 800.0;// room temperature

	static List<Element> feSet = new ArrayList();
	static List<Element> ptSet = new ArrayList();
	Grid<SimgraphNode> grid;
	CellAccessor<SimgraphNode, Map<GridPoint, Object>> accessor = new MultiOccupancyCellAccessor<SimgraphNode>();

	public static Factory<SimgraphNode> agentFactory = new Factory<SimgraphNode>(){
		@Override
		public SimgraphNode create() {
			Color col=Color.BLUE;
			if(Math.random()>0.5)col=Color.GREEN;
			return new Element(col);
		}
		
	};
	public void start() {
		super.start();
	};
	
	private static final long serialVersionUID = 1L;
	private static Simulation sim = new Simulation(System.currentTimeMillis());
	public static Simulation getInstance(){
		return sim;
	}
	private Simulation(long seed) {
		super(seed);
	}

	@Override
	public void step(SimState state) {
		Graph<SimgraphNode,Integer> net = ((ProjClassicalGraphs)MainFrame.selectedSBGNode).mn.graph;
		for (Iterator<SimgraphNode> iterator = net.getVertices().iterator(); iterator.hasNext();) {
			iterator.next().step();
		}
		System.out.println("ss");
	}
	
	
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
	public void go(){
		final Simulation sim = Simulation.getInstance();
		
		sim.start();
		sim.schedule.scheduleRepeating(sim);

        new Thread(new Runnable()
        {
        public void run()
            {
        	long steps=0;
        	while(steps < 10)
            {
        		synchronized(sim)
        		{
        		System.out.println("dd");
        		if (!sim.schedule.step(sim))
                    break;
    	         steps = sim.schedule.getSteps();
        		}
        		MainFrame.selectedSBGNode.update();
        		try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            sim.finish();
        	System.out.println("Simulation End");

            }
        }).start();
 	}
	///----------------------------------
	public static Grid<SimgraphNode> doOktadron(int s) {

		Grid<SimgraphNode> grid = new DefaultGrid("ddd", new SimpleGridAdder<SimgraphNode>(),
				new WrapAroundBorders(),
				new MultiOccupancyCellAccessor<SimgraphNode>(), 50, 50, 50);

		for (int u = 0; u <= 2 * (2 * s - 2); u++)
			for (int i = 0 - u; i < s; i++)
				for (int k = 0; k < s + 5 + u; k++)
					for (int j = 0; j < s + u; j++) {
						if ((k + j == s + u))
							if (((i + j) <= 3 * s - 3) && ((i + j) > -s)
									&& ((i + k) > -s + 1)
									&& ((i + k) <= 3 * s - 3 + 1))
								if ((u <= 2 * s - 2)
										|| ((i > -2 * s + 1)
												&& (j <= 3 * s - 3)
												&& (k <= 3 * s - 3 + 1) && (i
												+ j + k < 2 * (2 * s - 2) + 2)))

								{
									Element a = null;
									if (simbigraph.math.RandomHelper.getUniform()
											.nextDoubleFromTo(0.0d, 1.0d) > 0.5) {
										a = new Element(Color.GREEN);
										feSet.add( a);
									} else {
										a = new Element(Color.BLUE);
										ptSet.add(a);

									}
									grid.moveTo(a, i + 2 * (2 * s - 2), j + 2
											* (2 * s - 2), k + 2 * (2 * s - 2));
								}
					}
		return grid;
	}

	
	private double FindEnergy(SimgraphNode feEl, SimgraphNode ptEl) {
		double de = 0.0, e1 = 0.0, e2 = 0.0;

		// подсчёт энергии e1 до перемещения
		e1 = findE(feEl, ptEl);

		// подсчёт энергии e2 если перемещение состоится
		GridPoint fe_loc = grid.getLocation(feEl);
		GridPoint pt_loc = grid.getLocation(ptEl);
		grid.moveTo(feEl, pt_loc.getX(), pt_loc.getY(), pt_loc.getZ());
		grid.moveTo(ptEl, fe_loc.getX(), fe_loc.getY(), fe_loc.getZ());
		e2 = findE(feEl, ptEl);

		// перемещаю обратно
		fe_loc = grid.getLocation(feEl);
		pt_loc = grid.getLocation(ptEl);
		grid.moveTo(feEl, pt_loc.getX(), pt_loc.getY(), pt_loc.getZ());
		grid.moveTo(ptEl, fe_loc.getX(), fe_loc.getY(), fe_loc.getZ());

		de = e2 - e1;
		return de;

	}

	private double findE(SimgraphNode a1, SimgraphNode a2) {
		int[] m_el1 = countElem(_f2, a1);
		int[] m_el2 = countElem(_f3, a1);
		// int[] m_el3 = countElem(_f4, a1);
		// int[] m_el4 = countElem(_f5, a1);
		// int[] m_el5 = countElem(_f6, a1);
		// int[] m_el6 = countElem(_f7, a1);

		int koef = 0, koef2 = 0;// fe=1 pt=0
		if (a1.getColor().equals(Color.GREEN) )
			koef = 1;
		if (a2.getColor().equals(Color.GREEN))
			koef2 = 1;

		double e1 = m_el1[0] * mass1[0][koef] // + mass2[0][koef] * m_el2[0]
				// + mass3[0][koef] * m_el3[0] + mass4[0][koef] * m_el4[0]
				// + mass5[0][koef] * m_el5[0] + mass6[0][koef] * m_el6[0]
				+ m_el1[1] * mass1[1][koef] + mass2[1][koef] * m_el2[1];// +
																		// mass3[1][koef]
																		// *
																		// m_el3[1]
																		// +
																		// mass4[1][koef]
																		// *
																		// m_el4[1]
		// + mass5[1][koef] * m_el5[1] + mass6[1][koef] * m_el6[1]
		// + k_padding[koef] * (m_el1[2]);
		// + k_padding[koef] * (m_el1[2] + m_el2[2] + m_el3[2]);

		int[] _m_el1 = countElem(_f2, a2);
		int[] _m_el2 = countElem(_f3, a2);
		// int[] _m_el3 = countElem(_f4, a2);
		// int[] _m_el4 = countElem(_f5, a2);
		// int[] _m_el5 = countElem(_f6, a2);
		// int[] _m_el6 = countElem(_f7, a2);

		double e2 = _m_el1[0] * mass1[0][koef2] + mass2[0][koef2] * _m_el2[0]
		// + mass3[0][koef2] * _m_el3[0] + mass4[0][koef2] * _m_el4[0]
				// + mass5[0][koef2] * _m_el5[0] + mass6[0][koef2] * _m_el6[0]
				+ _m_el1[1] * mass1[1][koef2] + mass2[1][koef2] * _m_el2[1];// +
																			// mass3[1][koef2]
																			// *
																			// _m_el3[1]
																			// +
																			// mass4[1][koef2]
																			// *
																			// _m_el4[1]
		// + mass5[1][koef2] * _m_el5[1] + mass6[1][koef2] * _m_el6[1]
		// + k_padding[koef2]*(_m_el1[2]+ _m_el2[2]+ _m_el3[2]);
		// + k_padding[koef2] * (_m_el1[2]);
		return (e1 + e2);

	}

	// ---подчёт элементов вокруг
	// agent1-------------------------------------------------------------------------
	int[] countElem(int[][] mass, SimgraphNode agent1) {
		int Pt = 0;
		int Fe = 0;
		int Pad = 0;
		int Dump = 0; // для понимания

		int[] m_el = new int[4];
		GridPoint loc = grid.getLocation(agent1);

		for (int k = 0; k < mass.length; k++) {

			int x1 = loc.getX() + mass[k][0];
			int y1 = loc.getY() + mass[k][1];
			int z1 = loc.getZ() + mass[k][2];

			SimgraphNode agent = grid.getRandomObjectAt(x1, y1, z1);

			if (agent.getColor().equals(Color.BLUE) )
				Pt++;
			if (agent.getColor().equals(Color.GREEN))
				Fe++;

			
			 * if (agent instanceof PaddingAgent) Pad++; if (agent instanceof
			 * Marker) Dump++;
			 

		}
		m_el[0] = Pt;
		m_el[1] = Fe;
		m_el[2] = Pad;
		m_el[3] = Dump;

		return m_el;

	}*/
	
}
