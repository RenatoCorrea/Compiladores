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
public class VarType {
    
    public VarType( String type ){
        this.type = type;
    }
    
    public String getType(){ return this.type; }
    
    public void getC(){
        System.out.println(this.type);
    }
    
    String type;
}
