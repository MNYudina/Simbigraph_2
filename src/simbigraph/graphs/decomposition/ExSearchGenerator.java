package simbigraph.graphs.decomposition;

import java.util.ArrayList;

/**
 * 
 * @author Eugene Eudene
 * 
 * @version $Revision$ $Date$
 * 
 * Class for calculation iterate over the two-digit numbers using the monotonicity property.
 */
public class ExSearchGenerator {
    private bits next;
    private ArrayList close,pred;
    int size;
    private void fillpred(int _i){
        long tmp=((bits)pred.get(_i)).getLong();
        for(int i=_i-1;i>=0;i--){
            tmp=tmp*2L+1L;
            pred.set(i,new bits(tmp,size-i));
        }
    }
    public ExSearchGenerator(int _i){
        size=_i;
        next=new bits(_i);
        pred=new ArrayList();
        for(int i=2;i<size;i++){
            pred.add(new bits(1,size+2-i));
        }
        next=new bits(_i);
        close=new ArrayList();
    }
    private boolean isONE=true;
    public boolean generateNext(){
        if(next.getLong()==0){
            next.inc(1);
            return true;
        }
        if(next.shr()){
            if(isONE){
                next=new bits(3L,size);
                pred.set(0,next.copy());
                isONE=false;
                return true;
            }
            for(int i=0;i<size-2;i++){
                bits tmp=(bits)pred.get(i);
                if(tmp.getLong()==1){
                    tmp=new bits(2,size-i);
                    pred.set(i,tmp);
                }
                if((!tmp.shr())&&(!tmp.dec(1))){
                    fillpred(i);
                    next=((bits)pred.get(0)).copy();
                    return true;
                }
            }
        }
        else
            return true;
        return false;
    }
    public boolean NextInClose(){
        for(int i=0;i<close.size();i++){
            if(next.hasbits((bits)close.get(i))){
                return true;
            }
        }
        return false;
    }
    public bits getNext(){
        return next;
    }
    public void addClose(bits _close){
        close.add(_close);
    }

}