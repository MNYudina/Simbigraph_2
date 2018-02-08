package simbigraph.graphs.neighborhood;

import java.awt.geom.Point2D;
import java.util.HashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;


import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;

public class XYLayout extends StaticLayout<Agent, Integer> {

    
    public XYLayout(Graph<Agent, Integer> graph) {
		super(graph);
		locations =   	LazyMap.decorate(new HashMap<Agent, Point2D>(),
	    			new Transformer<Agent,Point2D>() {
						public Point2D transform(Agent arg0) {
							return new Point2D.Double(arg0.getX()*MainNeiGraph.sellSize,arg0.getY()*MainNeiGraph.sellSize);
						}});
	}

	
}
