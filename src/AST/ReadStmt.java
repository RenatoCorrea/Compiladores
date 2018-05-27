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
public class ReadStmt extends Stmt{
    ArrayList<Ident> idlist;
    ArrayList<VarType> typeList;
    
    public ReadStmt(ArrayList<Ident> idlist, ArrayList<VarType> typeList) {
        this.idlist = idlist;
        this.typeList = typeList;
    }

    public ArrayList<Ident> getIdlist() {
        return idlist;
    }
    
    public ArrayList<VarType> getTypeList(){
        return typeList;
    }
    
    public void genC( PW pw ){
        for(int i = 0; i<(idlist.size()); i++){
            if ( typeList.get(i).getType().compareTo(Symbol.FLOAT.toString()) == 0 )
              pw.print("{ char s[256]; gets(s); sscanf(s, \"%f\", &"  );
            else // should only be an integer
              pw.print("{ char s[256]; gets(s); sscanf(s, \"%d\", &"  );
            idlist.get(i).genC(pw);
            pw.out.println("); }" );
        }
    }
}
