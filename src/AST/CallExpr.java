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
public class CallExpr extends PFExpr{
    Ident id;
    ExprList exprlist;

    public CallExpr(Ident id, ExprList exprlist) {
        this.id = id;
        this.exprlist = exprlist;
    }

    public Ident getId() {
        return id;
    }

    public ExprList getExprlist() {
        return exprlist;
    }
    
    public void genC( PW pw) {
        id.genC(pw);
        pw.out.print("(");
        exprlist.genC(pw);
        pw.out.print(")");
    }

}
