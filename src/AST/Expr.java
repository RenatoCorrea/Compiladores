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
    VarType tipo;

    public Expr(Factor factor, ExprTail exprtail, VarType tipo) {
        this.factor = factor;
        this.exprtail = exprtail;
        this.tipo = tipo;
    }

    public Factor getFactor() {
        return factor;
    }

    public ExprTail getExprtail() {
        return exprtail;
    }

    
    public void setTipo(VarType tipo) {
        this.tipo = tipo;
    }
    
    public String getTipo(){
        return tipo.getType();
    }
    
    @Override
    public void genC( PW pw ) {
        factor.genC(pw);
        exprtail.genC(pw);
    }
    
}
