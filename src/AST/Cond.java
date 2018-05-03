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
public class Cond {
    Expr expr1, expr2;
    ComPop compop;

    public Cond(Expr expr1, Expr expr2, ComPop compop) {
        this.expr1 = expr1;
        this.expr2 = expr2;
        this.compop = compop;
    }
    
    
}
