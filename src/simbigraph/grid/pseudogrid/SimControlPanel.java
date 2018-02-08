package simbigraph.grid.pseudogrid;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import simbigraph.core.Simulation;
import simbigraph.grid.views.panels2d.SquarePanel;

public class SimControlPanel extends JPanel{
	protected Simulation sim;
	int timeUpdate = 200;
	int timeSim = 200;
	public Timer updateTimer;
	public Timer simTimer;
	protected JLabel currentTimeLabel =new JLabel(" Current modelling time: -1");

public SimControlPanel() {
	super();
	setLayout(new BorderLayout());
}
	public void init(final JPanel pan) {
		updateTimer = new Timer(timeUpdate, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				currentTimeLabel.setText(" Current modelling time:" +sim.schedule.getTime());
				//System.out.println("f");
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
		
		SpinnerModel numbers1 = new SpinnerNumberModel(200,100, 5000, 100);
		final JSpinner timeupdate = new JSpinner(numbers1);
		JPanel pane1 = new JPanel();
		pane1.add(new JLabel("Time of updating"));
		pane1.add(timeupdate);
		SpinnerModel numbers2 = new SpinnerNumberModel(200, 1, 5000, 10);
		final JSpinner timesimulation = new JSpinner(numbers2);
		JPanel pane2 = new JPanel();
		pane2.add(new JLabel("Time of modelling"));
		pane2.add(timesimulation);
		JPanel pane3=new JPanel();
		pane3.add(pane1);
		pane3.add(pane2);
		
		
		JPanel panel1 = new JPanel();
		JButton btnStep = new JButton("Step");
		JButton btnStop = new JButton("Stop");
		JButton btnResume = new JButton("Resume");
		//JButton btnAbort = new JButton("Abort");
		JButton btnStart = new JButton("Start");
		
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
		/*btnAbort.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// simulation.start();
				simTimer.stop();
				sim.finish();
				pan.repaint();
			}
		});*/
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
		});
		panel1.add(btnStart);
		panel1.add(btnStop);
		panel1.add(btnStep);
		panel1.add(btnResume);
		
		//box.add();
		JPanel panr= new JPanel(new GridLayout(1,2));
		panr.add(panel1); panr.add(currentTimeLabel);
		
		Box box = Box.createVerticalBox();
		box.add(panr);
		box.add(pane3);

		box.setAlignmentX(LEFT_ALIGNMENT);
		
		//FlowLayout sl =new FlowLayout();
		JPanel boxHor = new JPanel();
		boxHor.add(box);
		JPanel contents = new JPanel();
		boxHor.add(contents);

		   javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(boxHor);
		   boxHor.setLayout(jPanel2Layout);
	        jPanel2Layout.setHorizontalGroup(
	            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel2Layout.createSequentialGroup()
	                .addComponent(box)
	                .addContainerGap(327, Short.MAX_VALUE))
	        );
	        jPanel2Layout.setVerticalGroup(
	            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
	            .addGroup(jPanel2Layout.createSequentialGroup()
	                .addComponent(box)
	                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
	        );
		
		
		
		
		//sl.putConstraint(SpringLayout.WEST, box, 5, SpringLayout.WEST, contents);
		//sl.putConstraint(SpringLayout.WEST, button2, 5, SpringLayout.EAST, button1);
/*		boxHor.add(Box.createHorizontalGlue());
		boxHor.add(Box.createGlue());

		boxHor.add(Box.createHorizontalBox());
*/		
		add(boxHor,BorderLayout.NORTH);

	}

}
