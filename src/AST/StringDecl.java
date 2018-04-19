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
public class StringDecl {
    Ident ident;
    String str;
    
    public StringDecl(Ident ident, String str){
        this.ident = ident;
        this.str = str;
    }
    
    public Ident getIdent(){
        return this.ident;
    }
    
    public String getStrLiteral(){
        return this.str;
    }
    
    public void genC(){
        System.out.print("String ");
        ident.genC();
        System.out.print("=" + this.str + ";");
    }
}
