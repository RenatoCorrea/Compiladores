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
public class ReturnStmt extends Stmt{
    Expr expr;

    public ReturnStmt(Expr expr) {
        this.expr = expr;
    }

    public Expr getExpr() {
        return expr;
    }
    
    public void genC( PW pw ){
        pw.print("return ");
        expr.genC(pw);
        pw.out.print(";");
    }

}
