package AST;

/**
 *
 * @author WilliamAlves
 */
public class Variable {
    VarType tipo;
    String Nome;

    public Variable(VarType tipo, String Nome) {
        this.tipo = tipo;
        this.Nome = Nome;
    }

    public VarType getTipo() {
        return tipo;
    }

    public void setTipo(VarType tipo) {
        this.tipo = tipo;
    }

    public String getNome() {
        return Nome;
    }

    public void setNome(String Nome) {
        this.Nome = Nome;
    }
           
}