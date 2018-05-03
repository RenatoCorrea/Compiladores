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
public class IfStmt extends Stmt{
    Cond cond;
    StmtList stmtList;
    ElsePart elsepart;

    

    public IfStmt(Cond cond, StmtList stmtList, ElsePart elsepart) {
        this.cond = cond;
        this.stmtList = stmtList;
        this.elsepart = elsepart;
    }
    
    public Cond getCond() {
        return cond;
    }

    public StmtList getStmtList() {
        return stmtList;
    }

    public ElsePart getElsepart() {
        return elsepart;
    }
    
    public void genC(){
    
    }

}
