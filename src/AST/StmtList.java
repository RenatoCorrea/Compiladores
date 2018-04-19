package AST;

import java.util.ArrayList;

public class StmtList{
    ArrayList<Stmt> lista;

    public StmtList(ArrayList<Stmt> lista) {
        this.lista = lista;
    }

    public ArrayList<Stmt> getStmtList() {
        return lista;
    }
    
    public void genC(){
    }
    
}
