package AST;

public class ComPop {
    String compop;

    public String getCompop() {
        return compop;
    }

    public ComPop(String compop) {
        this.compop = compop;
    }
    
    public void genC(PW pw){
        if(compop == "="){
            pw.out.print("==");
        }
        else
            pw.out.print(compop);
    }
}
