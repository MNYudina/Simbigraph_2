package simbigraph.projections;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.*;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.apache.commons.collections15.Factory;

import simbigraph.core.SimgraphNode;
import simbigraph.core.Simulation;
import simbigraph.engine.SimState;
import simbigraph.engine.Steppable;
import texts.GetText;

import edu.uci.ics.jung.graph.Graph;
/**
 * 
 * @author Eugene Eudene 
 *
 */

 abstract public class Projection   
{
    private String type = "";
    public void setType(String value)
    {
    	type = value;
    }
    public String getType()
    {
    	return type;
    }
	
    private boolean isSelected = false;
    public void setSelected(boolean value)
    {
    	isSelected = value;
    }
    public boolean getSelected()
    {
    	return isSelected;
    }

    private List<String[]> properties = new LinkedList<String[]>();
	public List<String[]> getProperties()
    {
		return properties;
	}

	private List<String[]> events = new ArrayList<String[]>();
	public List<String[]> getEvents()
    {
		return events;
	}
	
	public void setEventOrProperty(String name, String value)
    {
		for (int i = 0; i < properties.size(); i++)
		{
			if (properties.get(i)[1].equalsIgnoreCase(name))
			{
				properties.get(i)[2] = value;
				break;
			}
			
		}
		for (int i = 0; i < events.size(); i++)
		{
			 if (events.get(i)[0].equalsIgnoreCase(name))
				{events.get(i)[1] = value;
				break;}
		}
    }
    public String getProperty(String name)
    {
    	for (int i = 0; i < properties.size(); i++)
		{
			if (properties.get(i)[1].equalsIgnoreCase(name))
			{
				return properties.get(i)[2];
			}
		}
    	return "";
    }
    
    public List<String> getItemsProperties (String str)
    {
    	List<String> values = new ArrayList();
    	for (int i = 0; i < properties.size(); i++)
		{
			if (properties.get(i)[0].equals(str.intern()))
			{
				if(properties.get(i)[2]=="")return null;
				values.add(properties.get(i)[2]);
			}
		}
    	return values;
    }
    
    public String getEvent(String name)
    {
    	for (int i = 0; i < events.size(); i++)
		{
			if (events.get(i)[0].equalsIgnoreCase(name))
			{
				return events.get(i)[1];
			}
		}
    	return "";
    }
    /**
     * Initializes a projection, setting the parameters of simulation by default,
     * factory agents by default, no upcoming events and so on.
     */
    public Projection()
    {
    	super();
    	/*String str =GetText.getText("SugarSpaceSim");
		
 		String[] Import = {"Simulation", str};
 		getEvents().add(Import);
 		 str =""
 		+"{agentFactory = new Factory<SimgraphNode>(){"+ "\r\n"
 		+"public SimgraphNode create() {"+ "\r\n"
 		+"return new SimgraphNode();"+ "\r\n"
 		+"}};}";
 		String[] Agent = {"AgentFactory", str};
 		getEvents().add(Agent);
 		
 		str =""
 	    	+"public void step(SimState state) {"+ "\r\n"
 	    	+"}"+ "\r\n"
 	     	    	
 	    	+"public void start(){"+ "\r\n"
 	    	+"super.start();"+ "\r\n"
 	    	+"schedule.scheduleRepeating(this);"+ "\r\n"
 	    	+"final Simulation sim= this;"+ "\r\n"

 	    	+"new Thread(new Runnable()"+ "\r\n"
 	    	+"{ public void run() {"+ "\r\n"
 	    	+"long steps=0;"+ "\r\n"
 	    	+"while(steps < Integer.MAX_VALUE)"+ "\r\n"
 	    	+"{synchronized(sim)            		{"+ "\r\n"
 	    	+"if (!schedule.step(sim)) break;"+ "\r\n"
 	    	+"steps = schedule.getSteps();}"+ "\r\n"
 	    	+"MainFrame.selectedSBGNode.update(); }"+ "\r\n"
 	    	+"finish();}"+ "\r\n"
 	    	+"}).start();}";
 		String[] atOnce = {"Scheduling", str};
 		getEvents().add(atOnce);*/
    }
    
	abstract public Component getPanel();
	public abstract void update() ;
	public void setProperty(String type, String nodeName, String nodeValue, String nodeValue2, String nodeValue3  ) {
		properties.add(properties.size()-1, new String[]{type, nodeName, nodeValue, nodeValue2, nodeValue3});		
	}
	public abstract void stop() ;

  
 
}