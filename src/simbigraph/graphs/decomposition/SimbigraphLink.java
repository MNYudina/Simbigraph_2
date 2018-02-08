package simbigraph.graphs.decomposition;
/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * Graph decomposition Edge.
 * 
 */
///	Ещё один странный класс. 
//  TODO он также не используется в графах декомпозиции. 

public class SimbigraphLink {
static int ids;
int id;
double p=0.5;
public double getP() {
		return p;
	}

	public void setP(double p) {
		this.p = p;
	}


	@Override
		public String toString() {
			return "p"+id;
		//return "";
		}
////
	
public SimbigraphLink() {
	id=ids++;
}
}
