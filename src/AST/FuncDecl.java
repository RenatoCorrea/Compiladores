package AST;

public class FuncDecl {
    VarType tipo;
    Ident ident;
    ParamDecList pdl;
    FuncBody fb;

    public FuncDecl(VarType tipo, Ident ident, ParamDecList pdl, FuncBody fb) {
        this.tipo = tipo;
        this.ident = ident;
        this.pdl = pdl;
        this.fb = fb;
    }

    public VarType getVarType() {
        return tipo;
    }

    public Ident getIdent() {
        return ident;
    }

    public ParamDecList getParamDecList() {
        return pdl;
    }

    public FuncBody getFuncBody() {
        return fb;
    }
        
    public void genC( PW pw ){
        //VarType Ident '(' ParamDecList ')' {
        //FuncBody
        //}
        pw.print("");
        tipo.genC(pw);
        pw.out.print(" ");
        ident.genC(pw);
        pw.out.print(" (");
        if(pdl != null) pdl.genC(pw);
        pw.out.println(") {");
        pw.add();
        fb.genC(pw);
        pw.sub();
        pw.out.println("");
        pw.println("}");
    }
}
