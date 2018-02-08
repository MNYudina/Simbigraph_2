package simbigraph.projections;
import java.awt.*;
import java.awt.geom.*;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.swing.JLabel;
import javax.swing.JPanel;

import simbigraph.core.Context;
import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.graphs.views.GraphModelingPanel;
import texts.GetText;


public class NetworksProjection extends Projection
{
	private GraphModelingPanel panel;
	public NetworksProjection()
	{
		super();
		setType("Networks");
		
 		String[] Source = {"String", "Source", "Generator", "Source", "combobox:File,Generator"};
 		getProperties().add(Source);
 		String st = "";
		try {
			st = new File(new File("./graphs").getCanonicalPath())+ "/SeedsForPreeAttachments/graph.net";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
 		String[] FileName = {"String", "FileName", st, "File Name", "filechooser", "Source=File"};
 		getProperties().add(FileName);
 		
 		String[] Generator = {"String", "Generator", "Barabasi-Albert", "Generator", "combobox:Barabasi-Albert,CurdsBA,ConfGeneralBA,ConfBA,Eppstein,Erdos-Renyi,Kleinberg", "Source=Generator"};
 		getProperties().add(Generator);
 		
 		String[] NodesAmount = {"int", "NodesAmount", "100", "Nodes Amount", "spinner:0,100000", "Source@Generator,Generator=Eppstein,Generator=Erdos-Renyi,Generator=ConfBA,Generator=ConfGeneralBA,Generator=CurdsBA"};
 		getProperties().add(NodesAmount);
 		
 		String[] LinksAmount = {"int", "LinksAmount", "3", "Links Amount", "spinner:0,100", "Source@Generator,Generator=Simple,Generator=Barabasi-Albert,Generator=Eppstein,Generator=ConfBA,Generator=ConfGeneralBA"};
 		getProperties().add(LinksAmount);

 		String[] EvolvSteps = {"int", "EvolvSteps", "50", "Evolv. Steps", "spinner:0,10000000", "Source@Generator,Generator=Simple,Generator=Barabasi-Albert"};
 		getProperties().add(EvolvSteps);
 		
 		String[] InitNodes = {"int", "InitNodes", "3", "InitNodes", "spinner:0,100000", "Source@Generator,Generator=Simple,Generator=Barabasi-Albert"};
 		getProperties().add(InitNodes);

 		String[] RParameter = {"int", "RParameter", "25", "R Parameter", "spinner:0,100", "Source@Generator,Generator=Eppstein"};
 		getProperties().add(RParameter);
 		
 		String[] LinkProbability = {"double", "LinkProbability", "0.1", "Link Probability", "textfield", "Source@Generator,Generator=Erdos-Renyi"};
 		getProperties().add(LinkProbability);
 		
 		String[] GridSize = {"int", "GridSize", "5", "Grid Size", "spinner:0,100", "Source@Generator,Generator=Kleinberg"};
 		getProperties().add(GridSize);
 		String[] ClusteringCoefficient = {"int", "ClusteringCoefficient", "2", "Clustering Coefficient", "spinner:0,100", "Source@Generator,Generator=Kleinberg"};
 		getProperties().add(ClusteringCoefficient);
 		
 		
 		String str =GetText.getText("GraphSim");
 		String[] Import = {"Simulation", str};
 		getEvents().clear();
 		getEvents().add(Import);
		
 		panel= new GraphModelingPanel();
 		panel.setVisible(true);
 		panel.validate();
	}
	public GraphModelingPanel getPanel() {
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
