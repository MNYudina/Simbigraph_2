package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simbigraph.core.Context;
import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.views.GraphModelingPanel;
import texts.GetText;


public class PAttachStohasticGraphProjection extends ProjGraphPA
{
	public GraphModelingPanel panel;

	public PAttachStohasticGraphProjection()
	{
		super();
		setType("ProjGraphPAttachStohastic");
 		String[] Method = {"String", "MethodGeneration", "ByRule", "MethodGeneration", "combobox:ByRule,byCallibration"};
 		getProperties().add(Method);
 		File f=new File("graphs/SeedsForPreeAttachments/graph.net");
 		String[] FileName = {"String", "FileName", f.getAbsolutePath(), "Seed Graph", "filechooser"};
 		getProperties().add(FileName);
 		
 		String[] EvolvSteps1 = {"int", "EvolvSteps", "50", "Evolv. Steps", "spinner:0,10000000"};
 		getProperties().add(EvolvSteps1);
 		
 		f=new File("graphs/RealGraphs/myAS.net");
 		String[] calib = {"String", "Calibrate", f.getAbsolutePath(), "Calibrate Graph", "calibrate", "MethodGeneration=byCallibration"};
 		getProperties().add(calib);

 		
 		String[] VerTable = {"String", "VerTable", "1.0/n", "Stoh. attachment", "table"};
 		getProperties().add(VerTable);
 		String str =""
    		+"import simbigraph.graphs.prefAttachment.*;"+ "\r\n"
    		+"import simbigraph.gui.*;"+ "\r\n"
    		+"import simbigraph.core.*;"+ "\r\n"
    		+"public class prefRule extends PrefAttechRule{"+ "\r\n"
    		+"public double f(int k) {return 1.0;}"+ "\r\n"
    		+"public int getM() {return Integer.MAX_VALUE;}	};"+ "\r\n";
		
 		String[] FileNamed = {"String", "Code", str, "RuleClass", "textpane"};
		getProperties().add(FileNamed);
 		

 		//repaint();
 		panel= new GraphModelingPanel();
 		//mn.setBackground(Color.WHITE);
 		//mn.setBounds(0, 0, 300, 300);
 		///mn.setPreferredSize(new Dimension(100, 300));
 		
 		String st =GetText.getText("GraphSim");
 		String[] Import = {"Simulation", st};
 		getEvents().clear();
 		getEvents().add(Import);
 		
 		
 		panel.setVisible(true);
 		panel.validate();
 		///add(mn);
	}
	public JPanel getPanel() {
		return panel;
	}
	@Override
	public void update() {
		panel.vv.repaint();
	}
	@Override
	public void stop() {
		if(panel.simTimer!=null)panel.simTimer.stop();
		if(panel.updateTimer!=null)panel.updateTimer.stop();
		Context.setGraph(null);
		Context.setGrid(null);
		
	}

}
