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
public class Primary extends PrimaryAbstract{
    PrimaryAbstract primary;
    VarType tipo;

    public Primary(PrimaryAbstract primary, VarType tipo) {
        this.primary = primary;
        this.tipo = tipo;
    }
    
    public VarType getType(){
        return tipo;
    }
    
    @Override
    public void genC( PW pw ) {
        primary.genC(pw);
    }

}
