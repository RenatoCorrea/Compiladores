package AST;

public class ExprTail {
    AddOp addop;
    Factor factor;
    ExprTail exprtail;

    public ExprTail(AddOp addop, Factor factor, ExprTail exprtail) {
        this.addop = addop;
        this.factor = factor;
        this.exprtail = exprtail;
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
    
    public void genC ( PW pw ){
        if(addop == null) return;
        addop.genC(pw);
        factor.genC(pw);
        exprtail.genC(pw);
    }
    
}
