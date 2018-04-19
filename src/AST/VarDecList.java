package AST;

import java.util.ArrayList;

public class VarDecList extends Decl{
    ArrayList<VarDecl> vd;
    
    public VarDecList(ArrayList<VarDecl> vd){
        this.vd = vd;
    }

    public ArrayList<VarDecl> getVarDecl() {
        return vd;
    }
      
    
    public void genC(){
        for(VarDecl declaracao:vd){
            declaracao.genC();
            System.out.print("\n");
        }
    }
}
