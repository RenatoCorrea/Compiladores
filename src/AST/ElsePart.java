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
public class ElsePart {
    StmtList stmtlist;

    public StmtList getStmtlist() {
        return stmtlist;
    }

    public ElsePart(StmtList stmtlist) {
        this.stmtlist = stmtlist;
    }
    
    public void genC( PW pw ){
        if(stmtlist == null) return;
        pw.println("else{");
        pw.add();
        stmtlist.genC(pw);
        pw.sub();
        pw.println("}");
    }
}
