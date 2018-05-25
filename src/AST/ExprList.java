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
public class ExprList {
    ArrayList<Expr> exprlist;

    public ExprList(ArrayList<Expr> exprlist) {
        this.exprlist = exprlist;
    }
    
    public void genC( PW pw ){
        for(int i = 0; i<(exprlist.size())-1; i++){
            exprlist.get(i).genC(pw);
            pw.out.print(", ");
        }
        exprlist.get(exprlist.size()-1).genC(pw);
    }
    
}
