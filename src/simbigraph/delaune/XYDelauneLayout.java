package simbigraph.delaune;

import java.awt.geom.Point2D;
import java.util.HashMap;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.map.LazyMap;


import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;

public class XYDelauneLayout extends StaticLayout<DelanuneyAgent, Integer> {
    public XYDelauneLayout(Graph<DelanuneyAgent, Integer> graph) {
		super(graph);
		locations =   	LazyMap.decorate(new HashMap<DelanuneyAgent, Point2D>(),
	    			new Transformer<DelanuneyAgent,Point2D>() {
						public Point2D transform(DelanuneyAgent arg0) {
							return new Point2D.Double(arg0.getX(),arg0.getY());
						}});
	}

	
}
