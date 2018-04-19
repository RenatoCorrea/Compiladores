package AST;

import java.util.ArrayList;

public class ParamDecList {
    ArrayList<ParamDecl> pdl;

    public ParamDecList(ArrayList<ParamDecl> pdl) {
        this.pdl = pdl;
    }

    public ArrayList<ParamDecl> getParamDecList() {
        return pdl;
    }
    
    public void genC(){
        for(int i = 0; i<(pdl.size())-1; i++){
            pdl.get(i).genC();
            System.out.print(", ");
        }
        pdl.get(pdl.size()-1).genC();
    }
    
}
