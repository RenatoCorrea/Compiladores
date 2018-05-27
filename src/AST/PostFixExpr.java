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
public class PostFixExpr extends PFExpr{
    PFExpr pfexpr;
    VarType tipo;

    public PostFixExpr(PFExpr pfexpr, VarType tipo) {
        this.pfexpr = pfexpr;
        this.tipo =  tipo;
    }
    
    public VarType getType(){
        return tipo;
    }
    
    public void genC( PW pw ){
        pfexpr.genC(pw);
    }
    
}
