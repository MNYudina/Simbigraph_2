package simbigraph.core;
import static simbigraph.grid.model.Snippet._f2;
import static simbigraph.grid.model.Snippet._f3;
import static simbigraph.grid.model.Snippet.mass1;
import static simbigraph.grid.model.Snippet.mass2;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Factory;

import simbigraph.engine.SimState;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.views.MainGrid3D;
import simbigraph.gui.MainFrame;
import simbigraph.math.RandomHelper;
import temp.Simulation10.ProtoAgent;
/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * @deprecated
 * 
 * Use as Example of simulation in debug mode
 */
public class CopyOfSimulation0 extends Simulation
{	
	class FeEl extends SimgraphNode {
		public Color color = Color.BLUE;
		public Color getColor() {
			return color;
		}
	}
	class PtEl extends SimgraphNode {
		public Color color = Color.GREEN;
		public Color getColor() {
			return color;
		}
	}
	final double K = (1.602176530 / 1.3806505) * 10000.0;
	double T = 800.0;
	List<FeEl> feSet = new ArrayList();
	List<PtEl> ptSet = new ArrayList();
	Grid<SimgraphNode> grid;
	CellAccessor<SimgraphNode, Map<GridPoint, Object>> accessor = new MultiOccupancyCellAccessor<SimgraphNode>();
	private Grid<SimgraphNode> doOktadron(int s) {
		//grid =MainGrid3D.grid;
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
									SimgraphNode a = null;
									if (simbigraph.math.RandomHelper.getUniform()
											.nextDoubleFromTo(0.0d, 1.0d) > 0.5) {
										a = new FeEl();
										feSet.add((FeEl) a);
									} else {
										a = new PtEl();
										ptSet.add((PtEl) a);
									}
									grid.moveTo(a, i + 2 * (2 * s - 2), j + 2
											* (2 * s - 2), k + 2 * (2 * s - 2));
									//MainGrid3D.addToUpdateList(a);
								}
					}
		return grid;
	}
	
	
	
	public void step(SimState state) {
		SimgraphNode feEl;
		SimgraphNode ptEl;
		double p = 0;
		do {
			feEl = feSet.get(simbigraph.math.RandomHelper.getUniform().nextIntFromTo(0,
					feSet.size()-1));
			ptEl = ptSet.get(simbigraph.math.RandomHelper.getUniform().nextIntFromTo(0,
					ptSet.size()-1));

			double d_e;
			d_e = FindEnergy(feEl, ptEl);
			if ((d_e) > 0) {
				double step = -(d_e) * K / T;
				p = Math.exp(step);
			} else
				p = 1.0;

		} while (RandomHelper.getUniform().nextDoubleFromTo(0.0d, 1.0d) > p);

		GridPoint fe_loc = grid.getLocation(feEl);
		GridPoint pt_loc = grid.getLocation(ptEl);
		
		grid.moveTo(feEl, pt_loc.getX(), pt_loc.getY(), pt_loc.getZ());
		grid.moveTo(ptEl, fe_loc.getX(), fe_loc.getY(), fe_loc.getZ());
		//MainGrid3D.addToUpdateList(feEl);
		//MainGrid3D.addToUpdateList(ptEl);
		System.out.println("iii");
	}
	private double FindEnergy(SimgraphNode feEl, SimgraphNode ptEl) {
		double de = 0.0, e1 = 0.0, e2 = 0.0;
		e1 = findE(feEl, ptEl);
		GridPoint fe_loc = grid.getLocation(feEl);
		GridPoint pt_loc = grid.getLocation(ptEl);
		grid.moveTo(feEl, pt_loc.getX(), pt_loc.getY(), pt_loc.getZ());
		grid.moveTo(ptEl, fe_loc.getX(), fe_loc.getY(), fe_loc.getZ());
		e2 = findE(feEl, ptEl);
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
		if (a1 instanceof FeEl)
			koef = 1;
		if (a2 instanceof FeEl)
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

	int[] countElem(int[][] mass, SimgraphNode agent1) {
		int Pt = 0;
		int Fe = 0;
		int Pad = 0;
		int Dump = 0; 
		int[] m_el = new int[4];
		GridPoint loc = grid.getLocation(agent1);
		for (int k = 0; k < mass.length; k++) {
			int x1 = loc.getX() + mass[k][0];
			int y1 = loc.getY() + mass[k][1];
			int z1 = loc.getZ() + mass[k][2];
			SimgraphNode simgraphNode = grid.getRandomObjectAt(x1, y1, z1);
			if (simgraphNode instanceof PtEl)
				Pt++;
			else if (simgraphNode instanceof FeEl)
				Fe++;
		}
		m_el[0] = Pt;
		m_el[1] = Fe;
		m_el[2] = Pad;
		m_el[3] = Dump;

		return m_el;

	}
	public void start(){
		super.start();
		schedule.scheduleRepeating(this);
		final Simulation sim= this;
		new Thread(new Runnable()
		{ public void run() {
		long steps=0;
		while(steps < Integer.MAX_VALUE)
		{synchronized(sim)            		{
		if (!schedule.step(sim)) break;
		steps = schedule.getSteps();}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		MainFrame.selectedSBGNode.update(); }
		finish();}
		}).start();}



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
		return new Factory<FeEl>(){
			@Override
			public FeEl create() {
				return new FeEl();
			}
		};
		}

}