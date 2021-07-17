package student;
import rs.etf.sab.operations.PackageOperations.Pair;

/**
 *
 * @author Jovana
 */
public class jj170586_Pair<A,B> implements Pair<A,B> {
    private A pA;
    private B pB;
    
    public jj170586_Pair(A pA, B pB){
        this.pA=pA;
        this.pB=pB;
    }
    
    public A getFirstParam() {
        return pA;
    }

    public B getSecondParam() {
        return pB;
    }
}
