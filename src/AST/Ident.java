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
public class Ident extends PrimaryAbstract{
    
    public Ident ( String ident ){
        this.ident = ident;
    }
    
    public String getIdent() { return ident; }
    
    public void genC(){
        System.out.print( ident );
    }
    
    private String ident;

}
