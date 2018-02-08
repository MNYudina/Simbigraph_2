package temp;

import java.awt.Color;
import java.util.Iterator;

import javax.swing.JFrame;

import org.apache.commons.collections15.Factory;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import simbigraph.core.Context;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import edu.uci.ics.jung.graph.Graph;

public class SimulationGraph extends Simulation {

	public class InfAgent {

		boolean infected = false;
		boolean n_infected = false;

		public Color getColor() {
			Color color = Color.GRAY;
			if (isInfected())
				color = Color.RED;

			return color;
		}

		public boolean isInfected() {
			return infected;
		}

		public InfAgent() {
			if (Math.random() < 0.5) {
				infected = true;
			}
		}

		public void setInfected(boolean infected) {
			this.infected = infected;
		}

		public void step() {
			Graph graph = Context.getGraph();
			Iterator<Object> it = graph.getNeighbors(this).iterator();
			int k = 0, k_inf = 0;
			if (!infected) {
				while (it.hasNext()) {
					k++;
					InfAgent n = (InfAgent) it.next();
					if (n.isInfected()) {
						k_inf++;
					}
				}
				if (Math.random()/(1.0*k_inf) <  0.05)
					n_infected = true;
			} else {
				if (Math.random() < 0.2)
					n_infected = false;
			}
		}

		public void post_step() {
			infected=n_infected;
		}
	}

	// private Graph graph;
	public void step(SimState state) {
		Graph graph = Context.getGraph();
		int sumInfected = 0;
		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			InfAgent agent = ((InfAgent) iterator.next());
			agent.step();
		}
		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			InfAgent agent = ((InfAgent) iterator.next());
			agent.post_step();
			if (agent.infected == true)
				sumInfected++;

		}
		series1.add(schedule.getTime(), (double) sumInfected);

	}

	XYSeries series1;
	int rr = 0;

	public void start() {
		super.start();
		schedule.scheduleRepeating(this);

		final JFrame frame = new JFrame("");

		series1 = new XYSeries("time");
		// for (int i = 1; i < 10; i++)
		// series1.add((double) i, (double) i);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Sum of infected nodes", "time", "Sum", dataset,
				PlotOrientation.VERTICAL, false, true, false);
		final XYPlot plot = chart.getXYPlot();
		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesLinesVisible(0, true);
		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		final ChartPanel chartPanel = new ChartPanel(chart);

		frame.add(chartPanel);
		frame.setVisible(true);
		frame.pack();

	}

	@Override
	public Class[] getVisClass() {
		return new Class[] { InfAgent.class };
	}

	@Override
	public Color getAgentColor(Object obj) {
		Color col = ((InfAgent) obj).getColor();
		return col;
	}

	@Override
	public void init(Object env) {
		// Graph graph = Context.getGraph();
		// graph = (Graph)env;
	}

	@Override
	public Factory getAgentFactory() {
		return new Factory<InfAgent>() {
			@Override
			public InfAgent create() {
				return new InfAgent();
			}
		};
	}

}