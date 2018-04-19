
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
    
    
    public void genC(){
        System.out.print(this.tipo);
        for(int i = 0; i<(idList.size())-1; i++){
            System.out.print(" "+idList.get(i)+",");
        }
        System.out.println(" "+idList.get((idList.size())-1)+";");
    }
}
