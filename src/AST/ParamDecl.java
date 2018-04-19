package AST;

public class ParamDecl {
    VarType tipo;
    Ident ident;
    
    public ParamDecl(VarType tipo, Ident ident){
        this.ident = ident;
        this.tipo = tipo;
    }

    public VarType getTipo() {
        return tipo;
    }

    public Ident getIdent() {
        return ident;
    }
    
    public void genC(){
        tipo.genC();
        System.out.print(" ");
        ident.genC();
    }
    
}
