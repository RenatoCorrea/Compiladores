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
public class AssignExpr {
    
    Ident id;
    Expr expr;

    public AssignExpr(Ident id, Expr expr) {
        this.id = id;
        this.expr = expr;
    }

    public Ident getId() {
        return id;
    }

    public Expr getExpr() {
        return expr;
    }
    
    public void genC(PW pw){
        id.genC(pw);
        pw.out.print(" = ");
        expr.genC(pw);
    }
}
