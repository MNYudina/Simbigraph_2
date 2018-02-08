package simbigraph.graphs.decomposition;
/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * This class used to iterate over the two-digit numbers using the monotonicity property.
 * For the calculation the reliability of decomposition graph .
 */

public class bits{
    private long Bits;
    private int col;
    bits(int _i){
        col=_i;
        Bits=0L;
    }
    bits(long _Bits,int _i){
        col=_i;
        Bits=_Bits;
    }
    boolean shr(){
        Bits=Bits<<1L;
        long tmp=1L<<col;
        return (Bits&tmp)==tmp;
    }
    boolean inc(long _sum){
        long tmp=1L<<col;
        Bits=Bits+_sum;
        return (Bits&tmp)==tmp;
    }
    boolean dec(long _sum){
        long tmp=1L<<col;
        Bits=Bits-_sum;
        return (Bits&tmp)==tmp;
    }
    public boolean get(int _i){
        long tmp=1L<<(_i);
        return (Bits&tmp)==tmp;
    }
    boolean set(int _i,boolean _x){
        if((_i>=0)&&(_i<col)){
            if(_x){
                Bits=(Bits|(1L<<_i));
            }
            else{
                Bits=(Bits&(~(1L<<_i)));
            }
            return true;
        }
        return false;
    }
    boolean hasbits(bits _toDel){
        return (_toDel.size()==col)&&((Bits&(_toDel.Bits))==_toDel.Bits);
    }
    public bits copy(){
        bits tmp=new bits(col);
        tmp.Bits=Bits;
        return tmp;
    }
    void clear(){
        Bits=0L;
    }
    public int size(){
        return col;
    }
    @Override
    public String toString() {
    	// TODO Auto-generated method stub
    /*	return super.toString();
    }
    void print(){*/
    	String str=" ";
        for(int j=0;j<col;j++){
            if((Bits&(1<<j))==(1<<j))
            	str=str+"1";
            else
            	str=str+"0";
        }
		return str+" ";
    }
    long getLong(){
        return Bits;
    }
}
