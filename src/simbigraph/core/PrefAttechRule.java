package simbigraph.core;
import simbigraph.graphs.prefAttachment.PrefferentialAttachment;

public class PrefAttechRule implements PrefferentialAttachment
{
		public double f(int k) {return 1.0;	}
		public int getM() {return Integer.MAX_VALUE;}
}
