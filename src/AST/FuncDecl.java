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
        
    public void genC(){
    //VarType Ident '(' ParamDecList ')' {
    //FuncBody
    //}
    tipo.genC();
    System.out.print(" ");
    ident.genC();
    System.out.print(" (");
    if(pdl != null) pdl.genC();
    System.out.print(") {\n");
    fb.genC();
    System.out.print("}");
    }
}
