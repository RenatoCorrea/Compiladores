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
public class FactorTail {
    MulOp mulop;
    PostFixExpr postfixexpr;
    FactorTail factortail;
    VarType tipo;

    public FactorTail(MulOp mulop, PostFixExpr postfixexpr, FactorTail factortail, VarType tipo) {
        this.mulop = mulop;
        this.postfixexpr = postfixexpr;
        this.factortail = factortail;
        this.tipo = tipo;
    }

    public MulOp getMulop() {
        return mulop;
    }

    public PostFixExpr getPostfixexpr() {
        return postfixexpr;
    }

    public FactorTail getFactortail() {
        return factortail;
    }
    
    public VarType getType(){
        return tipo;
    }
    
    public void genC( PW pw ){
        if(mulop == null) return;
        pw.out.print(" ");
        mulop.genC(pw);
        pw.out.print(" ");
        postfixexpr.genC(pw);
        factortail.genC(pw);
    }
    
}
