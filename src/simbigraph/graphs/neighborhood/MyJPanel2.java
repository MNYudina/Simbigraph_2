package simbigraph.graphs.neighborhood;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import simbigraph.grid.model.Grid;
import simbigraph.grid.model.GridDimensions;
import simbigraph.grid.model.GridPoint;

import edu.uci.ics.jung.graph.util.Pair;


public class MyJPanel2 extends JPanel {
@Override
public synchronized void paint(Graphics ge) {
	super.paint(ge);
	Graphics g = ge.create();
	GridDimensions dim = MainNeiGraph.grid.getDimensions();
	int sizeCell = MainNeiGraph.sellSize;
	//horizontal
	for(int i=0;i<dim.getHeight()+1;i++){
		g.drawLine(0, i*sizeCell, sizeCell*dim.getWidth(), i*sizeCell);
	}
	//vertical
	for(int i=0;i<dim.getWidth()+1;i++){
		g.drawLine( i*sizeCell,0, i*sizeCell, sizeCell*dim.getHeight());
	}
	//Отрисовываю агентов
	for(int i=0;i<dim.getWidth();i++)
	for(int j=0;j<dim.getHeight();j++)
	{
		Agent a= (Agent)MainNeiGraph.grid.getObjectAt(i,j);
		if(a!=null){g.setColor(a.getColor());
		g.fillOval( i*sizeCell, j*sizeCell, sizeCell, sizeCell);}
	}
	/// отрисовываю ребра графа
	g.setColor(Color.BLACK);
	for(Integer i:MainNeiGraph.graph.getEdges()){
		Pair<Agent> p = MainNeiGraph.graph.getEndpoints(i);
		int x1=((Agent)p.getFirst()).getX()*sizeCell+sizeCell/2;
		int x2=((Agent)p.getSecond()).getX()*sizeCell+sizeCell/2;
		int y1=((Agent)p.getFirst()).getY()*sizeCell+sizeCell/2;
		int y2=((Agent)p.getSecond()).getY()*sizeCell+sizeCell/2;
		((Graphics2D)g).setStroke(new BasicStroke(6.0f));
		g.drawLine(x1, y1, x2, y2);

	}
	
}
}
