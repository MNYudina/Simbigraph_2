package simbigraph.core;
import java.io.File;
import java.util.*;

import simbigraph.engine.SimState;
import simbigraph.engine.Steppable;
import simbigraph.projections.Projection;



public class JavaFileMaker
{
	String code;
	public JavaFileMaker(Projection pr)
	{
		code=""
		+pr.getEvent("Simulation") + "\r\n";
			
		//+"import simbigraph.compiling.*;"+ "\r\n"
		//+"public class Simulation0 extends Simulation" + "\r\n"
		//		+"{"
		//+"public Class[] getVisClass(){ return null;}" + "\r\n"
		//+pr.getEvent("AgentFactory") + "\r\n"
		//+pr.getEvent("Scheduling") + "\r\n"
		//+"}";
	}

	public void setRule(Projection pr)
	{
		code=""
		+pr.getProperty("code");
	}

	public String getCode() {
		return code;
	}


}
