package simbigraph.projections;

import java.awt.Component;


public abstract class ProjGraphPA extends Projection {

	public double f[];
	public void setF(double[] f) {
		this.f=f;
	}
	public double q_[];
	public void setQ_(double[] f) {
		this.q_=f;
	}
	public double r[];
	public boolean calibFlag=false;
	public void setR_(double[] r) {
		this.r=r;
	}
	/**
	 * 
	 * @return integer value of zero degree after non-zero sequence
	 */
	public int getM() {
		double m=0.;
		boolean b1=false; 
		for(int i=0;i<q_.length;i++){
			if(q_[i]>0) b1=true;
			if((b1)&&(q_[i]<0.00000000001)) return i;
		}
		 return q_.length-1;
	}
}
