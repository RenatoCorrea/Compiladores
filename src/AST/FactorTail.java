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

    public FactorTail(MulOp mulop, PostFixExpr postfixexpr, FactorTail factortail) {
        this.mulop = mulop;
        this.postfixexpr = postfixexpr;
        this.factortail = factortail;
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
    
}
