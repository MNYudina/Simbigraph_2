package simbigraph.graphs.decomposition;
/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * Graph decomposition Node.
 * 
 */
public class SimbigraphNode implements Cloneable
{
	///	Ещё один странный класс. 
	//  TODO он также не используется в графах декомпозиции. 
	public SimbigraphNode(int i) {
		count=i;
	}
	int x,y;
	public int getX() {
		return x;
	}
	public void setX(String n_x) {
		x=Integer.valueOf(n_x);
	}
	public void setState(String transform) {
            state=0;
                if(transform.contains("beg"))
            state=-5;//Integer.valueOf(transform);
                else if(transform.contains("end"))
            state=5;//Integer.valueOf(transform);
                else if(transform.contains("nor"))
            state=1;
                //Integer.valueOf(transform);

	}

	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int count;
	public int state=0;

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return ""+count;
	}
           public Object clone() {
                 SimbigraphNode copy = null;
                try {
                 copy = (SimbigraphNode)super.clone();
              } catch(CloneNotSupportedException e) { }
         copy.state=0;//state;//copy.x=x;copy.y=y;
         copy.count=0;
         return copy;
        }

}