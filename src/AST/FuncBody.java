package AST;

import java.util.ArrayList;

public class FuncBody {
    ArrayList<Decl> decl;
    StmtList stmt;

    public FuncBody(ArrayList<Decl> decl, StmtList stmt) {
        this.decl = decl;
        this.stmt = stmt;
    }

    public ArrayList<Decl> getDecl() {
        return decl;
    }

    public StmtList getStmtList() {
        return stmt;
    }
    
    public void genC( PW pw ){
        for(Decl declaracao : decl){
            declaracao.genC(pw);
        }
        if(stmt != null) this.stmt.genC(pw);
    }
}
