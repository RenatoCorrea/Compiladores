/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class AddOp {
    String addop;

    public AddOp(String addop) {
        this.addop = addop;
    }

    public String getAddop() {
        return addop;
    }
    
    public void genC( PW pw ){
        pw.out.print(" " + addop + " ");
    }
    
    
}
