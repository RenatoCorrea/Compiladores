/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import Lexer.Symbol;
import java.util.ArrayList;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class WriteStmt extends Stmt{
    ArrayList<Ident> idlist;
    ArrayList<VarType> typeList;

    public WriteStmt(ArrayList<Ident> idlist, ArrayList<VarType> typeList) {
        this.idlist = idlist;
        this.typeList = typeList;
    }

    
    public ArrayList<Ident> getIdlist() {
        return idlist;
    }
    
    public ArrayList<VarType> getTypeList() {
        return typeList;
    }
    
    public void genC( PW pw ){
        pw.print("printf(\"");
        for(int i = 0; i<(idlist.size()); i++){
            if ( typeList.get(i).getType().compareTo(Symbol.FLOAT.toString()) == 0 ) 
                pw.print("%f");
            else if ( typeList.get(i).getType().compareTo(Symbol.INT.toString()) == 0 ) 
                pw.print("%d");
            else
                pw.out.print("%s");
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
