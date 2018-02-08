package simbigraph.core;

import java.awt.Color;

import javax.swing.Timer;

import org.apache.commons.collections15.Factory;

import simbigraph.engine.SimState;
import simbigraph.engine.Steppable;
import simbigraph.graphs.decomposition.SimbigraphNode;
import simbigraph.gui.MainFrame;
/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * A Simulation represents the simulation proper witch inhereted from Ascape's SimState. 
    <p>A SimState contains the random number generator and the simulator's schedule.
    <p>When a simulation is begun, SimState's start() method is called.  Then the schedule is stepped some N times.  Last, the SimState's finish() method is called, and the simulation is over.
	<p>The Simbigraph garanted call start() of Simulation  before running of visualization
    <p>SimStates are serializable; if you wish to be able to checkpoint your simulation and read from checkpoints, you should endeavor to make all objects in the simulation serializable as well.  Prior to serializing to a checkpoint, preCheckpoint() is called.  Then after serialization, postCheckpoint() is called.  When a SimState is loaded from a checkpoint, awakeFromCheckpoint() is called to give you a chance to make any adjustments.  SimState also implements several methods which call these methods and then serialize the SimState to files and to streams.
    <p>SimState also maintains a private registry of AsynchronousSteppable objects (such as YoYoYo), and handles pausing and resuming
    them during the checkpointing process, and killing them during finish() in case they had not completed yet.
    <p>If you override any of the methods foo() in SimState, should remember to <b>always</b> call super.foo() for any such method foo().
 */

public abstract class Simulation extends SimState implements Steppable{
	private static final long serialVersionUID = 8203670482790414418L;
	private int sizeCell=15;
	public int getSizeCell() {
		return sizeCell;
	}

	public void setSizeCell(int sizeCell) {
		this.sizeCell = sizeCell;
	}

	public Simulation() {
		super(System.currentTimeMillis());
	}
	
	/**
	 * —пециальный метод позвол€ющий задать пор€док отрисовки агентов (используетс€ при работе с Grid)
	 * @return
	 */
	public Class[] getVisClass(){return new Class[]{Object.class};};
	
	/**
	 * @param Agent
	 * @return Color
	 */
	public  Color getAgentColor(Object obj)
	{
		return Color.RED;
	}
	
	public  boolean isVisible(Object obj)
	{
		return true;
	}

	public  int getAgentSize(Object obj)
	{
		return sizeCell;
	}
	
	/**
	 * »нициализирует основную структуру моделировани струкуру(это может быть Grid Graph) 
	 * ¬ в Simdigraph инициализаци€ обеспечиваетс€ до начала моделировани€(вызова метода Simulation.run())
	 * @param env 
	 */
	abstract public void init(Object env);

	/**
	 * ћетод позвол€ет задать способ создани€ агентов(наоример это может быть необходимо, при добавлении агентов в решЄтку из панели рисовани€ или при создании графа)
	 * @return Factory - фабрика агентов
	 */
	public abstract Factory getAgentFactory();

		
	
}
