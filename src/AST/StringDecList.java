/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package AST;

import java.util.ArrayList;

public class StringDecList extends Decl{
    ArrayList<StringDecl> sd;
    
    public StringDecList(ArrayList<StringDecl> sd){
        this.sd = sd;
    }

    public ArrayList<StringDecl> getStringDecl() {
        return sd;
    }
       
    public void genC(){
        for(StringDecl declaracao:sd){
            declaracao.genC();
        }
    }
}
