package AST;

import java.util.ArrayList;

public class FuncDeclarations {
    ArrayList<FuncDecl> fd;

    public FuncDeclarations(ArrayList<FuncDecl> fd) {
        this.fd = fd;
    }

    public ArrayList<FuncDecl> getFd() {
        return fd;
    }
    
    public void genC(){
        if(fd.get(0) == null) return;
        for(FuncDecl declaracao:fd){
            declaracao.genC();
            System.out.print("\n");
        }
    }
    
}
