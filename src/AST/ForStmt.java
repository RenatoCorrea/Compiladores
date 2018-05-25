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
public class ForStmt extends Stmt{
    AssignExpr assign1, assign2;
    Cond cond;
    StmtList stmtlist;

    public ForStmt(AssignExpr assign1, Cond cond, AssignExpr assign2, StmtList stmtlist) {
        this.assign1 = assign1;
        this.assign2 = assign2;
        this.cond = cond;
        this.stmtlist = stmtlist;
    }

    public AssignExpr getAssign1() {
        return assign1;
    }

    public AssignExpr getAssign2() {
        return assign2;
    }

    public Cond getCond() {
        return cond;
    }

    public StmtList getStmtlist() {
        return stmtlist;
    }
    
    
    public void genC( PW pw ){
        pw.print("for (");
        assign1.genC(pw);
        pw.out.print(", ");
        cond.genC(pw);
        pw.out.print(", ");
        assign2.genC(pw);
        pw.out.println("){");
        pw.add();
        stmtlist.genC(pw);
        pw.sub();
        pw.println("}");
    }

}
