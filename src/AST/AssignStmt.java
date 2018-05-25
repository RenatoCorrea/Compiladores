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
public class AssignStmt extends Stmt{
    
    AssignExpr assignExpr;

    public AssignStmt(AssignExpr assignExpr) {
        this.assignExpr = assignExpr;
    }
    
    public AssignExpr getAssignExpr() {
        return assignExpr;
    }

    public void setAssignExpr(AssignExpr assignExpr) {
        this.assignExpr = assignExpr;
    }
    
    public void genC( PW pw ){
        pw.print("");
        assignExpr.genC(pw);
        pw.out.println(";");
    }
}
