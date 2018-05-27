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
public class Factor {
    PostFixExpr postfixexpr;
    FactorTail factortail;
    VarType tipo;

    

    public Factor(PostFixExpr postfixexpr, FactorTail factortail, VarType tipo) {
        this.postfixexpr = postfixexpr;
        this.factortail = factortail;
        this.tipo = tipo;
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
        postfixexpr.genC(pw);
        factortail.genC(pw);
    }
}
