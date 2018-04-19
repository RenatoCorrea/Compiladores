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
public class Program {
    Ident ident;
    ArrayList<Decl> decl;
    FuncDeclarations fd;

    public Program(Ident ident, ArrayList<Decl> decl, FuncDeclarations fd) {
        this.ident = ident;
        this.decl = decl;
        this.fd = fd;
    }

    public Ident getIdent() {
        return ident;
    }

    public ArrayList<Decl> getDecl() {
        return decl;
    }

    public FuncDeclarations getFuncDeclaration() {
        return fd;
    }
    
    public void genC(){
        System.out.print("#include<stdio.h>\n\n");
        for(Decl d: decl)
            d.genC();
        fd.genC();
        
    }
}
