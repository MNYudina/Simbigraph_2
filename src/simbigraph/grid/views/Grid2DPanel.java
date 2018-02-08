package simbigraph.grid.views;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Constructor;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingWorker;
import javax.swing.Timer;

import simbigraph.core.CompilingClassLoader;
import simbigraph.core.Context;
import simbigraph.core.JavaFileMaker;
import simbigraph.core.Simulation;
import simbigraph.grid.model.CellAccessor;
import simbigraph.grid.model.DefaultGrid;
import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;
import simbigraph.grid.model.MultiOccupancyCellAccessor;
import simbigraph.grid.model.WrapAroundBorders;
import simbigraph.grid.pseudogrid.SimControlPanel;
import simbigraph.grid.views.panels2d.Abstract2DPanel;
import simbigraph.grid.views.panels2d.HexaPanel;
import simbigraph.grid.views.panels2d.SquarePanel;
import simbigraph.grid.views.panels2d.TrianglePanel;
import simbigraph.projections.Projection;
import texts.GetText;

public class Grid2DPanel extends SimControlPanel {
	Abstract2DPanel pan;

	public static enum TYPEGRID  {HEXA,SQUARE,TRIANGLE};
	private static final long serialVersionUID = 1L;
	public static  int sellSize=15;
//	public Grid grid;

	 
	public Grid2DPanel() {
		super();
		//setLayout(new BorderLayout());
		
	}

	
	public void init(TYPEGRID type) {
		Grid grid = Context.getGrid();

		GridDimensions dim = grid.getDimensions();
		switch(type){
			//case SQUARE :pan = createIntoPanel(grid, sim, Grid2DPanel.sellSize,sim.getVisClass(),"SquarePanel");break;
		case SQUARE :pan = new SquarePanel(grid, sim, Grid2DPanel.sellSize,sim.getVisClass());break;
		case TRIANGLE :pan = new TrianglePanel(grid, sim, Grid2DPanel.sellSize,sim.getVisClass());break;
		case HEXA : pan = new HexaPanel(grid, sim, Grid2DPanel.sellSize,sim.getVisClass());break;
			//case HEXA : pan = createIntoPanel(grid, sim, Grid2DPanel.sellSize,sim.getVisClass(),"SquarePanel");break;
		default:	;//pan = createIntoPanel(grid, sim, Grid2DPanel.sellSize,sim.getVisClass(),"SquarePanel");break;
		} 
		JScrollPane sp = new JScrollPane(pan);
		add(sp);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		currentTimeLabel =new JLabel();
		
		JButton plus = new JButton("+");
		plus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				pan.sizeCell = pan.sizeCell + 1;
				pan.updatePrefferedSize();
				pan.repaint();

			}
		});
		JButton minus = new JButton("-");
		minus.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pan.sizeCell > 2)
					pan.sizeCell = pan.sizeCell - 1;
				pan.updatePrefferedSize();
				pan.repaint();
			}
		});
		Box controls = Box.createVerticalBox();
		JPanel zoomControls = new JPanel(new GridLayout(1, 2));
		zoomControls.setBorder(BorderFactory.createTitledBorder("Zoom"));
		zoomControls.add(plus);
		zoomControls.add(minus);
		controls.add(zoomControls);
		zoomControls.setMaximumSize(new Dimension(100,100));
		controls.add(Box.createVerticalGlue());
		add(controls,BorderLayout.EAST);
		
		
		/*updateTimer = new Timer(timeUpdate, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentTimeLabel.setText(" Current modelling time: "+sim.schedule.getTime());
				pan.repaint();
			}
		});
		updateTimer.start();
		simTimer  = new Timer(timeSim, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				synchronized (sim) 
				{
					sim.schedule.step(sim);
				}
			}
		});
		simTimer.start();
		JPanel panel1 = new JPanel();
		JButton btnStep = new JButton("Step");
		JButton btnStop = new JButton("Stop");
		JButton btnResume = new JButton("Resume");
		//JButton btnAbort = new JButton("Abort");
		JButton btnStart = new JButton("Start");
		SpinnerModel numbers1 = new SpinnerNumberModel(800, 200, 5000, 200);
		final JSpinner timeupdate = new JSpinner(numbers1);
		JPanel pane1 = new JPanel();
		pane1.add(new JLabel("Time of updating"));
		pane1.add(timeupdate);
		SpinnerModel numbers2 = new SpinnerNumberModel(800, 200, 5000, 200);
		final JSpinner timesimulation = new JSpinner(numbers2);
		JPanel pane2 = new JPanel();
		pane2.add(new JLabel("Time of modelling"));
		pane2.add(timesimulation);
		panel1.add(btnStart);
		panel1.add(btnStop);
		panel1.add(btnStep);
		panel1.add(btnResume);
		panel1.add(currentTimeLabel);
		
		
		//panel1.add(btnAbort);
		pan.setPreferredSize(new java.awt.Dimension(600, 600));
		pan.setSize(new java.awt.Dimension(600, 600));

		
		// zoomControls.createHorizontalGlue();
		Box panel3 = Box.createHorizontalBox();
		panel3.add(pane1);
		panel3.createHorizontalStrut(8);
		panel3.add(pane2);
		panel3.setBorder(BorderFactory.createTitledBorder("Delay parameters"));
		Box panel2 = Box.createHorizontalBox();
		panel2.add(panel3);
		panel2.add(zoomControls);
		panel2.createHorizontalGlue();
		Box box = Box.createVerticalBox();
		box.add(panel1);
		box.add(panel2);
		btnStop.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simTimer.stop();
				pan.repaint();
			}
		});
		btnStep.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				simTimer.stop();
				sim.schedule.step(sim);
				pan.repaint();
			}
		});
		btnStart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeUpdate = (Integer) timeupdate.getValue();
				timeSim = (Integer) timesimulation.getValue();
				sim.finish();
				sim.start();
				simTimer  = new Timer(timeSim, new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						synchronized (sim) 
						{
							sim.schedule.step(sim);
						}
					}
				});
				simTimer.start();
				updateTimer.setDelay(timeUpdate);
			}
		});
		btnResume.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				timeUpdate = (Integer) timeupdate.getValue();
				timeSim = (Integer) timesimulation.getValue();

				simTimer.setDelay(timeSim);
				simTimer.start();
				//sim.schedule.step(sim);
				pan.repaint();
				updateTimer.setDelay(timeUpdate);

			}
		});*/
		
		
		//add(box, BorderLayout.NORTH);
		super.init(pan);
		pan.sizeCell = sim.getSizeCell();
	}
	
	private void updateTimerActionPerformed(java.awt.event.ActionEvent evt)
	{
		pan.repaint();
	}

	public void setSim(Simulation sim2) {
		// TODO Auto-generated method stub
		sim=sim2;
	}	
	

	
}
