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
public class FloatLiteral extends PrimaryAbstract{
    String floatnumber;

    public FloatLiteral(String floatnumber) {
        this.floatnumber = floatnumber;
    }
    
    @Override
    public void genC( PW pw ) {
        pw.out.print(floatnumber);
    }

}
