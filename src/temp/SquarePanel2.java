package temp;

import simbigraph.core.*;
import simbigraph.grid.model.*;
import java.awt.*;
import simbigraph.grid.views.panels2d.SquarePanel;
import temp.SugarSpaceGridExample.ProtoAgent;
public class SquarePanel2 extends SquarePanel{
public SquarePanel2(Grid grid, Simulation sim, Integer s, Class[] classAgent) {
super(grid, sim, s, classAgent);}
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	
		for (int i = 0; i < grid.getDimensions().getHeight() + 1; i++) {
			g.drawLine(0, i * sizeCell, sizeCell
					* grid.getDimensions().getWidth(), i * sizeCell);
		}
		// vertical
		for (int i = 0; i < grid.getDimensions().getWidth() + 1; i++) {
			g.drawLine(i * sizeCell, 0, i * sizeCell, sizeCell
					* grid.getDimensions().getHeight());
		}
		// Отрисовываю агентов по массиву переданных цветов
		for(int t=0;t<classAgent.length;t++){
			for (int i = 0; i < grid.getDimensions().getWidth(); i++){
				for (int j = 0; j < grid.getDimensions().getHeight(); j++) {

	Iterable<Object> it = null;
	it = grid.getObjectsAt(i, j);
	for (Object protoAgent : it) {	
if (classAgent[t].isInstance(protoAgent)) {
 g.setColor(sim.getAgentColor(protoAgent));
 g.fillOval(i * sizeCell, j * sizeCell,
			sizeCell,sizeCell);
}
}
	it = grid.getObjectsAt(i, j);
	for (Object protoAgent : it) {	
if (classAgent[t].isInstance(protoAgent)) {
 g.setColor(sim.getAgentColor(protoAgent));
 g.fillOval(i * sizeCell, j * sizeCell,
			sizeCell,sizeCell);
}
}
		  }
	    }
	  }
	}

}
