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
    
    public void genC(){
    
    }
}
