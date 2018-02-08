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

public class SimulationGraphSIR extends Simulation {

	public class InfAgent {
		int n_SIRstate, n_t;

		int SIRstate = 0;
		int t1 = 2, t2 = 6, t = 0;

		public Color getColor() {
			Color color = Color.BLACK;
			if (SIRstate == 0)
				color = Color.BLUE;
			else if (SIRstate == 1)
				color = Color.RED;
			else if (SIRstate == 2)
				color = Color.GRAY;
			return color;
		}

		public InfAgent() {
		}

		public void step() {
			Graph graph = Context.getGraph();
			if (t == 0)
				n_SIRstate = 0;
			else if (t > 0 && t <= t1)
				n_SIRstate = 1;
			else if (t > t1 && t <= t2)
				n_SIRstate = 2;
			
			if (n_SIRstate == 0) {
				Iterator<Object> it = graph.getNeighbors(this).iterator();
				int k = 0, k_inf = 0;
				while (it.hasNext()) {
					k++;
					InfAgent n = (InfAgent) it.next();
					if (n.SIRstate == 1) {
						k_inf++;
					}
				}
				if (k_inf*Math.random()>0.5)
					n_t = 1;
			} else
				n_t = t + 1;
		}

		public void post_step() {
			t = n_t;
			SIRstate = n_SIRstate;
		}

		public void infect() {
			SIRstate=1;t=1;
		}
	}

	public void step(SimState state) {
		Graph graph = Context.getGraph();
		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			((InfAgent) iterator.next()).step();
		}
		int sumInfected=0, sumSusept=0, sumRecov=0;
		for (Iterator<Object> iterator = graph.getVertices().iterator(); iterator
				.hasNext();) {
			InfAgent a=((InfAgent) iterator.next());
			a.post_step();
			if (a.SIRstate == 1)
				sumInfected++;
			else if(a.SIRstate == 2)
				sumRecov++;
			else sumSusept++;
				

		}
		series1.add(schedule.getTime(), (double) sumSusept);
		series2.add(schedule.getTime(), (double) sumInfected);
		series3.add(schedule.getTime(), (double) sumRecov);


	}
	XYSeries series1,series2,series3;

	public void start() {
		super.start();
		schedule.scheduleRepeating(this);

	}


	@Override
	public Color getAgentColor(Object obj) {
		Color col = ((InfAgent) obj).getColor();
		return col;
	}

	@Override
	public void init(Object env) {
		Graph graph = Context.getGraph();
		((InfAgent) graph.getVertices().iterator().next()).infect();
		final JFrame frame = new JFrame("Graph of dynamics");

		series1 = new XYSeries("Suseptible");
		series2 = new XYSeries("Infected");
		series3 = new XYSeries("Recovered");
		// for (int i = 1; i < 10; i++)
		// series1.add((double) i, (double) i);

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series1);
		dataset.addSeries(series2);
		dataset.addSeries(series3);

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Sum of infected/suseptible/removed nodes", "time", "Sum", dataset,
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
	public Factory getAgentFactory() {
		return new Factory<InfAgent>() {
			@Override
			public InfAgent create() {
				return new InfAgent();
			}
		};
	}

}