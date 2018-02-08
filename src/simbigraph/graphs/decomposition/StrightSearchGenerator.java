package simbigraph.graphs.decomposition;

import java.util.ArrayList;

/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 *          Class for calculation iterate over the two-digit numbers using the
 *          monotonicity property.
 */
public class StrightSearchGenerator {
	private bits next;
	int size;

	public StrightSearchGenerator(int _i) {
		size = (int) Math.pow(2.0,_i);
        next=new bits(_i);

	}

	public boolean generateNext() {
		/*
		 * if(next.getLong()==0){ next.inc(1); return true; }
		 */
		;
		return next.getLong() < size;
	}

	public bits getNext() {
		bits l=next.copy();
		next.inc(1);
		return l;
	}

	public static void main(String[] args) {
		//StrightSearchGenerator EXG = new StrightSearchGenerator(3);
		ExSearchGenerator EXG = new ExSearchGenerator(3);
		System.out.println("kkkkkk");
		while (EXG.generateNext()) {
			bits tmppp = EXG.getNext();
			System.out.println();

			for (int j = 0; j < tmppp.size(); j++) {
				int i = 0;
				if (tmppp.get(j))
					i = 1;
				System.out.print(i);
			}
		}
		
		/*int N=100;
		for (int j = 1; j <= 20; j++) {
		//for (int N = 1; N <= 10; N++) {
		//double p=0.01*j;
		double a=0.1*j;
		double S=0;
		double e_=a*Math.pow(Math.E, -a);

		for (int i = 1; i <= N; i++) {
			
			S=S+(1.0/(a))*(Math.pow(i, i-1)/fact(i))*Math.pow(e_, i);
			
		}
		System.out.println( 1-S );
		}*/
	}

	private static double fact(int x) {
		double fact =1;
		for(int i =2;i<=x;i++)
			fact*=i;
		return fact;
	}
}