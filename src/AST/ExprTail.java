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
public class ExprTail {
    AddOp addop;
    Factor factor;
    ExprTail exprtail;

    public ExprTail(AddOp addop, Factor factor, ExprTail exprtail) {
        this.addop = addop;
        this.factor = factor;
        this.exprtail = exprtail;
    }

    public AddOp getAddop() {
        return addop;
    }

    public Factor getFactor() {
        return factor;
    }

    public ExprTail getExprtail() {
        return exprtail;
    }
    
    
}
