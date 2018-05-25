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
public class ReadStmt extends Stmt{
    ArrayList<Ident> idlist;

    public ArrayList<Ident> getIdlist() {
        return idlist;
    }

    public ReadStmt(ArrayList<Ident> idlist) {
        this.idlist = idlist;
    }

    
    public void genC( PW pw ){
        for(Ident id : idlist){
            /*if ( id.getType() == Type.charType ) 
              pw.print("{ char s[256]; gets(s); sscanf(s, \"%c\", &"  );
            else // should only be an integer*/
              pw.print("{ char s[256]; gets(s); sscanf(s, \"%d\", &"  );
            pw.out.println(  id.getIdent() + "); }" );
        }
    }
}
