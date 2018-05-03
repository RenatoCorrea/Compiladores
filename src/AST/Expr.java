/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class Expr extends PrimaryAbstract{
    Factor factor;
    ExprTail exprtail;

    public Expr(Factor factor, ExprTail exprtail) {
        this.factor = factor;
        this.exprtail = exprtail;
    }

    public Factor getFactor() {
        return factor;
    }

    public ExprTail getExprtail() {
        return exprtail;
    }

    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
