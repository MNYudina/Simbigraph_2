package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simbigraph.core.Context;
import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;
import simbigraph.graphs.views.GraphModelingPanel;
import texts.GetText;

public class PAttachDetermGraphProjection extends ProjGraphPA
{
	private GraphModelingPanel panel;

	public PAttachDetermGraphProjection()
	{
		super();
		setType("ProjGraphPAttachDeterm");
 		String[] Method = {"String", "MethodGeneration", "ByRule", "Method Generation", "combobox:ByRule,byCallibration"};
 		getProperties().add(Method);
 		File f=new File("graphs/SeedsForPreeAttachments/graph.net");
 		String[] FileName = {"String", "FileName", f.getAbsolutePath(), "Seed Graph", "filechooser"};
 		getProperties().add(FileName);

 		String[] EvolvSteps1 = {"int", "EvolvSteps", "50", "Evolv. Steps", "spinner:0,10000000"};
 		getProperties().add(EvolvSteps1);
 		
 		String[] parameter1 = {"int", "ParameterM", "2", "Parameter m", "spinner:2,100"};//, "MethodGeneration=ByRule"
 		getProperties().add(parameter1);


 		String str =""
    		+"import simbigraph.graphs.prefAttachment.*;"+ "\r\n"
    		+"import simbigraph.gui.*;"+ "\r\n"
    		+"import simbigraph.core.*;"+ "\r\n"
    		+"public class prefRule extends PrefAttechRule{"+ "\r\n"
    		+"public double f(int k) {return 1.0;}"+ "\r\n"
    		+"public int getM() {return Integer.MAX_VALUE;}	};"+ "\r\n";
 		
 		String[] FileNamed = {"String", "Code", str, "RuleClass", "textpane"};
		getProperties().add(FileNamed);

 		//String[] VerTable = {"String", "VerTable", "0.3/n", "Parameters", "table", "MethodGeneration=byCallibration"};
 		//getProperties().add(VerTable);

 		f=new File("./graphs/RealGraphs/myAS.net");
 		String[] calib = {"String", "Calibrate", f.getAbsolutePath(), "Calibrate Graph", "calibrate", "MethodGeneration=byCallibration"};
 		getProperties().add(calib);


 		
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
