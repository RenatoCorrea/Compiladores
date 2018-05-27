package AST;

public class ExprTail {
    AddOp addop;
    Factor factor;
    ExprTail exprtail;
    VarType tipo;

    public ExprTail(AddOp addop, Factor factor, ExprTail exprtail, VarType tipo) {
        this.addop = addop;
        this.factor = factor;
        this.exprtail = exprtail;
        this.tipo = tipo;
    }

    public AddOp getAddop() {
        return addop;
    }

    public Factor getFactor() {
        return factor;
    }

    public ExprTail getExprtail() {
        return exprtail;
    }
    
    public VarType getType(){
        return tipo;
    }
    
    public void genC ( PW pw ){
        if(addop == null) return;
        addop.genC(pw);
        factor.genC(pw);
        exprtail.genC(pw);
    }
    
}
