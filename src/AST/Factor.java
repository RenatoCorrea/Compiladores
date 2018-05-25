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

    

    public Factor(PostFixExpr postfixexpr, FactorTail factortail) {
        this.postfixexpr = postfixexpr;
        this.factortail = factortail;
    }
    
    public PostFixExpr getPostfixexpr() {
        return postfixexpr;
    }

    public FactorTail getFactortail() {
        return factortail;
    }
    
    public void genC( PW pw ){
        postfixexpr.genC(pw);
        factortail.genC(pw);
    }
}
