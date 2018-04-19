/*

    Trabalho 1: Compilador Little
    
    Evelyn  RA:
    Renato  RA:
    William Adriano Alves   RA: 620203
    
*/

import Lexer.*;
import Error.*;
import AST.*;
import java.util.ArrayList;

public class Compiler {

	// para geracao de codigo
	public static final boolean GC = true; 
    
	private Lexer lexer;
    private CompilerError error;

    public void compile( char []p_input ) {
        error = new CompilerError(null);
        lexer = new Lexer(p_input, error);
        error.setLexer(lexer);
                
        lexer.nextToken();
        Program p = program();
        if (lexer.token != Symbol.EOF) 
            error.signal("nao chegou no fim do arq");
        
        //while(lexer.token != Symbol.EOF){
        //    lexer.nextToken();
        //}
    }

    //Program ::= PROGRAM IDENT BEGIN PgmBody END
    //PgmBody ::= Decl FuncDeclarations
    public void program(){
        if (lexer.token != Symbol.PROGRAM)
                error.signal("faltou Program");
        lexer.nextToken();

        if(lexer.token != Symbol.IDENT)
            error.signal("faltou Identificador");
        Ident ident = new Ident(lexer.getStringValue());
        lexer.nextToken();

        if(lexer.token != Symbol.BEGIN)
            error.signal("faltou Begin");
        lexer.nextToken();

        ArrayList<Decl> decl = decl();
        FuncDeclaration fd = funcDeclarations();

        if (lexer.token != Symbol.END)
            error.signal("faltou end");
        lexer.nextToken();
        
        return new Program(ident, decl, fd);
    }
    
    //Decl ::= StringDeclList | StringDeclList Decl |  VarDeclList | VarDeclList Decl | empty
    public ArrayList<Decl> decl(){
        ArrayList<Decl> decl = new ArrayList();
        do{
            if (lexer.token == Symbol.STRING){
                decl.add(stringDecList());
            }
            else if (lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
                decl.add(varDecList());
            }
        }while(lexer.token == Symbol.STRING || lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT);
        return decl;
    }
    
    //StringDeclList ::= StringDecl | StringDecl StringDeclList
    public StringDecList stringDecList(){
        ArrayList<StringDecl> sd = new ArrayList();
        do{
            sd.add(stringdecl());
        }while(lexer.token == Symbol.STRING);
        
        return new StringDecList(sd);
    }
    
    //StringDecl ::= STRING IDENT := STRINGLITERAL ‘;’ | empty
    public StringDecl stringdecl(){
        if(lexer.token == Symbol.STRING){
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.signal("Identificador nao encontrado");
            Ident ident = new Ident(lexer.getStringValue());
            lexer.nextToken();
            if(lexer.token != Symbol.ASSIGN)
                error.signal(" := nao encontrado");
            lexer.nextToken();
            if(lexer.token != Symbol.STRINGLITERAL)
                error.signal("String nao encontrada");
            //Pode ser que tenha que copiar por metodo
            String strLiteral = lexer.getStringValue();
            lexer.nextToken();
            if(lexer.token != Symbol.SEMICOLON)
                error.signal("Ponto-e-virgula nao encontrado");
            lexer.nextToken();
            
            return new StringDecl(ident, strLiteral);
        }
        return null;
    }
    
    //VarDecList ::= VarDecl | VarDecl VarDecList
    public VarDecList varDecList(){
        ArrayList<VarDecl> vd = new ArrayList();
        do{
            vd.add(vardecl());
        }while(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT);
        
        return new VarDecList(vd);
    }
	
	//VarDecl ::= VarType IdList ‘;’ | empty
	public VarDecl vardecl(){
	    if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
	        VarType tipo = new VarType(lexer.getStringValue());
                lexer.nextToken();
	        ArrayList<Ident> idList = idList();
	        if(lexer.token != Symbol.SEMICOLON)
	           error.signal("ponto-e-virgula nao encontrado");
	        lexer.nextToken();
                return new VarDecl(tipo, idList);
	    }
	}
	
	//IdList ::= IDENT | IDENT ‘,’ IdList
	public ArrayList<Ident> idList(){
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador nao encontrado");
        //salvar ID
        lexer.nextToken();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            idList();
        }
    }
    
    //ParamDeclList ::= ParamDecl | ParamDecl ‘,’ ParamDeclList
    public void paramDecList(){
        paramdecl();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            paramDecList();            
        }
    }
    
    //ParamDecl ::= VarType IDENT
    public void paramdecl(){
        if(lexer.token != Symbol.FLOAT && lexer.token != Symbol.INT)
            error.signal("tipo invalido de variavel");
        lexer.nextToken();
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador invalido");
        lexer.nextToken();
    }
    
    
    public void funcDeclarations(){
        funcdecl();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            funcDeclarations();
        }
    }
    
    public void funcdecl(){
        if(lexer.token == Symbol.FUNCTION){
            lexer.nextToken();
            if(lexer.token != Symbol.VOID && lexer.token != Symbol.INT && lexer.token != Symbol.FLOAT)
                error.signal("tipo invalido");
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.signal("identificador nao encontrado");
            lexer.nextToken();
            if(lexer.token != Symbol.LPAR)
                error.signal("e necessario o uso de parenteses");
            lexer.nextToken();
            if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT)
                paramDecList();
            if(lexer.token != Symbol.RPAR)
                error.signal("e necessario fechar o parenteses");
            lexer.nextToken();
            if(lexer.token != Symbol.BEGIN)
                error.signal("BEGIN nao encontrado");
            lexer.nextToken();
            funcBody();
            if(lexer.token != Symbol.END)
                error.signal("END nao encontrado");
            lexer.nextToken();
        }
    }
    
    public void funcBody(){
        decl();
        stmtList();
    }
    
    public void stmtList(){
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE ||
           lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR)
            stmt();
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE ||
           lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR)
           stmtList();
    }
    
    public void stmt(){
        if(lexer.token == Symbol.IDENT)
            assignstmt();
        else if(lexer.token == Symbol.READ)
            readstmt();
        else if(lexer.token == Symbol.WRITE)
            writestmt();
        else if(lexer.token == Symbol.RETURN)
            returnstmt();
        else if(lexer.token == Symbol.IF)
            ifstmt();
        else if(lexer.token == Symbol.FOR)
            forstmt();
        else error.signal("STMT invalido");
    }
    
    //AssignStmt ::= AssignExpr ';'
    public void assignstmt(){
        assignexpr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("faltou ponto-e-virgula");
        lexer.nextToken();
    }
    
    //AssignExpr ::= IDENT := Expr
    public void assignexpr(){
        if(lexer.token != Symbol.IDENT)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.ASSIGN)
            error.signal(":= necessario");
        lexer.nextToken();
        expr();
    }
    
    //ReadStmt ::= READ ‘(‘ IdList ‘);’
    public void readstmt(){
        if(lexer.token != Symbol.READ)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        idList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
    }
    
    //WriteStmt ::= WRITE ‘(‘ IdList ‘);’
    public void writestmt(){
        if(lexer.token != Symbol.WRITE)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        idList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
    }
    
    //ReturnStmt ::= RETURN expr ‘;’
    public void returnstmt(){
        if(lexer.token != Symbol.RETURN)
            error.signal("RETURN necessario");
        lexer.nextToken();
        expr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
    }
    
    //IfStmt ::= IF ( Cond ) THEN StmtList ElsePart ENDIF
    public void ifstmt(){
        if(lexer.token != Symbol.IF)
            error.signal("IF errado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessaria");
        lexer.nextToken();
        cond();
        if(lexer.token != Symbol.RPAR)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        if(lexer.token != Symbol.THEN)
            error.signal("THEN necessario");
        lexer.nextToken();
        stmtList();
        elsePart();
        if(lexer.token != Symbol.ENDIF)
            error.signal("ENDIF necessario");
        lexer.nextToken();
    }
    
    //ForStmt ::= FOR ({AssignExpr} ’;’ {Cond} ‘;’ {AssignExpr}) StmtList ENDFOR
    public void forstmt(){
        if(lexer.token != Symbol.FOR)
            error.signal("FOR errado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessaria");
        lexer.nextToken();
        if(lexer.token == Symbol.IDENT)
            assignexpr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("Ponto e virgula necessaria");
        lexer.nextToken();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            cond();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        if(lexer.token == Symbol.IDENT)
            assignexpr();
        if(lexer.token != Symbol.RPAR)
            error.signal("fechamento de parenteses necessario");
        lexer.nextToken();
        stmtList();
        if(lexer.token != Symbol.ENDFOR)
            error.signal("ENDFOR necessario");
        lexer.nextToken();
    }
    
    //ElsePart ::= ELSE StmtList | empty
    public void elsePart(){
        if(lexer.token == Symbol.ELSE){
            lexer.nextToken();
            stmtList();
        }
    }
    
    //Cond ::= Expr Compop Expr
    public void cond(){
        expr();
        compop();
        expr();
    }
    
    //Compop ::= < | > | =
    public void compop(){
        if(lexer.token != Symbol.EQUAL && lexer.token != Symbol.LT && lexer.token != Symbol.GT)
            error.signal("erro no sinal");
            //adicionar ifs pra saber qual eh
        lexer.nextToken();
    }
    
    public void expr(){
        factor();
        exprTail();
    }
    
    public void exprTail(){
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
            addop();
            factor();
            exprTail();
        }
    }
    
    public void factor(){
        postfixexpr();
        factorTail();
    }
    
    public void factorTail(){
        if(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
            mulop();
            postfixexpr();
            factorTail();
        }
    }
    
    public void postfixexpr(){
        if(lexer.token == Symbol.IDENT){
            if(lexer.checkNextToken() == Symbol.LPAR)
                callexpr();
            else
                primary();
        }
        else
            primary();
            
    }
    
    public void callexpr(){
        if(lexer.token != Symbol.IDENT)
            error.signal("identificador não encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessária");
        lexer.nextToken();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            exprList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
    }
    
    public void exprList(){
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            expr();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            exprList();
        }
    }
    
    
    public void primary(){
        if(lexer.token == Symbol.LPAR){
            lexer.nextToken();
            expr();
            if(lexer.token != Symbol.RPAR)
                error.signal("parenteses não encontrado");
        }
        else if(lexer.token == Symbol.IDENT)
            lexer.nextToken();
       
        else if(lexer.token == Symbol.NUMBER)
            lexer.nextToken();
        else
            error.signal("Esperado (, identificador ou number");
    }
    
    public void addop(){
        if(lexer.token != Symbol.PLUS && lexer.token != Symbol.MINUS)
            error.signal("+ ou - não encontrado");
        lexer.nextToken();
    }
    
    public void mulop(){
        if(lexer.token != Symbol.MULT && lexer.token != Symbol.DIV)
            error.signal("* ou / não encontrado");
        lexer.nextToken();
    }
    
}