
package AST;

import java.util.ArrayList;

public class VarDecl {
   ArrayList<Ident> idList;
   VarType tipo;
    
    public VarDecl(VarType tipo, ArrayList<Ident> idList){
        this.tipo = tipo;
        this.idList = idList;
    }

    public ArrayList<Ident> getIdList() {
        return idList;
    }

    public VarType getTipo() {
        return tipo;
    }
    
    
    public void genC( PW pw ){
        
        pw.print("");
        tipo.genC(pw);
        for(int i = 0; i<(idList.size())-1; i++){
            pw.out.print(" ");
            idList.get(i).genC(pw);
            pw.out.print(",");
        }
        pw.out.print(" ");
        idList.get((idList.size())-1).genC(pw);
        pw.out.println(";");
    }
}
