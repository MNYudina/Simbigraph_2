package temp;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.Timer;

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

import edu.uci.ics.jung.graph.util.Pair;

public class SugarSpaceGridExample extends Simulation {
	
	public abstract class ProtoAgent {
		int size = 255;

		public ProtoAgent() {

		}

		abstract void step();

		public abstract Color getColor();
	}

	public class Sugar extends ProtoAgent {
		@Override
		void step() {

		}

		@Override
		public Color getColor() {
			if (size > 0)
				return new Color(0, size, 0);
			else
				return new Color(0, 0, 0);
		}
	}

	public class Eater extends ProtoAgent {

		@Override
		void step() {
			GridPoint point = grid.getLocation(this);
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
			grid.moveTo(this, x, y);

			Iterable<ProtoAgent> it = grid.getObjectsAt(x, y);
			for (ProtoAgent protoAgent : it) {
				if (protoAgent instanceof Sugar) {
					protoAgent.size = protoAgent.size - 25;
					this.size = this.size + 25;
				}
			}
			}

		@Override
		public Color getColor() {
			return Color.RED;
		}

	}
	Grid<ProtoAgent> grid;

	
	private void doAgentStructure() {

		GridDimensions dim = grid.getDimensions();
		for (int i = 0; i < dim.getWidth(); i++) {
			for (int j = 0; j < dim.getHeight(); j++) {
				grid.moveTo(new Sugar(), i, j);
			}
		}
		for (int i = 0; i < 10; i++) {
			// устанавливаю агента
			grid.moveTo(new Eater(), 5 + i, 15);
		}
		
		return;
	}

//Моделирование активности агентов	
	public void step(SimState state) {
		Iterable<ProtoAgent> li = grid.getObjects();
		for (ProtoAgent agent : li) {agent.step();
		}
	}
//Установка задержки для моделирования агентов
	public void start() {
		super.start();
		schedule.scheduleRepeating(this);
	}

	@Override
	public Class[] getVisClass() {
		return new Class[]{Sugar.class,Eater.class};
	}
	@Override
	public Color getAgentColor(Object obj) {
		Color col =Color.RED;
		if (obj instanceof ProtoAgent)return ((ProtoAgent)obj).getColor();
		return col;
	}

	@Override
	public void init(Object env) {
		grid =(Grid)env;
		doAgentStructure();
	}
	public Class getAgentClass() {
		return ProtoAgent.class;
	}

	@Override
	public Factory getAgentFactory() {
		return new Factory<Eater>(){
			@Override
			public Eater create() {
				return new Eater();
			}
		};
		}
}