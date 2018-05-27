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
public class CallStmt extends Stmt{
    CallExpr callexpr;
    VarType tipo;
    
    public CallStmt(CallExpr callexpr, VarType tipo) {
        this.callexpr = callexpr;
        this.tipo = tipo;
    }

    public CallExpr getCallexpr() {
        return callexpr;
    }

    public VarType getTipo() {
        return tipo;
    }

    @Override
    public void genC(PW pw) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
