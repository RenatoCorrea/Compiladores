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
public class IntLiteral extends PrimaryAbstract{
    String intnumber;

    public IntLiteral(String intnumber) {
        this.intnumber = intnumber;
    }
    
    @Override
    public void genC() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
