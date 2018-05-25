/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import java.util.ArrayList;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class WriteStmt extends Stmt{
    ArrayList<Ident> idlist;

    public WriteStmt(ArrayList<Ident> idlist) {
        this.idlist = idlist;
    }

    public ArrayList<Ident> getIdlist() {
        return idlist;
    }
    
    public void genC( PW pw ){
        pw.print("printf(\"");
        for(Ident id : idlist){
            /*if ( id.getType() == Type.charType ) 
                pw.print("%c, ");
            else*/
                pw.out.print("%d");
        }
        pw.out.print("\", ");
        for(int i = 0; i<(idlist.size())-1; i++){
            idlist.get(i).genC(pw);
            pw.out.print(", ");
        }
        idlist.get(idlist.size()-1).genC(pw);
        pw.out.println(");");

    }
}
