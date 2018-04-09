/*

    Trabalho 1: Compilador Little
    
    Evelyn  RA:
    Renato  RA:
    William Adriano Alves   RA: 620203
    
*/

import Lexer.*;
import Error.*;

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
        program();
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
            //salvar o valor do IDENT?
            lexer.nextToken();
            
            if(lexer.token != Symbol.BEGIN)
                error.signal("faltou Begin");
            lexer.nextToken();
            
            Decl();
            FuncDeclarations();

    		if (lexer.token != Symbol.END)
    			error.signal("faltou end");
    		lexer.nextToken();
    }
    
    //Decl ::= StringDeclList | StringDeclList Decl |  VarDeclList | VarDeclList Decl | empty
    public void Decl(){
        if (lexer.token == Symbol.STRING){
            StringDecList();
            if(lexer.token == Symbol.STRING || lexer.token == Symbol.INT
            || lexer.token == Symbol.FLOAT)
                Decl();
        }
        else if (lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
            VarDecList();
            if(lexer.token == Symbol.STRING || lexer.token == Symbol.INT
            || lexer.token == Symbol.FLOAT)
                Decl();
        }
        
    }
    
    //StringDeclList ::= StringDecl | StringDecl StringDeclList
    public void StringDecList(){
        StringDecl();
        if(lexer.token == Symbol.STRING)
            StringDecList();
    }
    
    //StringDecl ::= STRING IDENT := STRINGLITERAL ‘;’ | empty
    public void StringDecl(){
        if(lexer.token == Symbol.STRING){
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.signal("Identificador nao encontrado");
            lexer.nextToken();
            //salvar IDENT?
            if(lexer.token != Symbol.ASSIGN)
                error.signal(" := nao encontrado");
            lexer.nextToken();
            if(lexer.token != Symbol.STRINGLITERAL)
                error.signal("String nao encontrada");
            //salvar o valor da string?
            lexer.nextToken();
            if(lexer.token != Symbol.SEMICOLON)
                error.signal("Ponto-e-virgula nao encontrado");
            lexer.nextToken();
        }
    }
    
    //VarDecList ::= VarDecl | VarDecl VarDecList
	public void VarDecList(){
 		VarDecl();
 		if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT)
 		     VarDecList();
	}
	
	//VarDecl ::= VarType IdList ‘;’ | empty
	public void VarDecl(){
	    if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
	        lexer.nextToken();
	        IdList();
	        if(lexer.token != Symbol.SEMICOLON)
	           error.signal("ponto-e-virgula nao encontrado");
	        lexer.nextToken();
	    }
	}
	
	//IdList ::= IDENT | IDENT ‘,’ IdList
	public void IdList(){
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador nao encontrado");
        //salvar ID
        lexer.nextToken();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            IdList();
        }
    }
    
    //ParamDeclList ::= ParamDecl | ParamDecl ‘,’ ParamDeclList
    public void ParamDecList(){
        ParamDecl();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            ParamDecList();            
        }
    }
    
    //ParamDecl ::= VarType IDENT
    public void ParamDecl(){
        if(lexer.token != Symbol.FLOAT && lexer.token != Symbol.INT)
            error.signal("tipo invalido de variavel");
        lexer.nextToken();
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador invalido");
        lexer.nextToken();
    }
    
    
    public void FuncDeclarations(){
        FuncDecl();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            FuncDeclarations();
        }
    }
    
    public void FuncDecl(){
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
                ParamDecList();
            if(lexer.token != Symbol.RPAR)
                error.signal("e necessario fechar o parenteses");
            lexer.nextToken();
            if(lexer.token != Symbol.BEGIN)
                error.signal("BEGIN nao encontrado");
            lexer.nextToken();
            FuncBody();
            if(lexer.token != Symbol.END)
                error.signal("END nao encontrado");
            lexer.nextToken();
        }
    }
    
    public void FuncBody(){
        Decl();
        StmtList();
    }
    
    public void StmtList(){
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE ||
           lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR)
            Stmt();
        if(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE ||
           lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR)
           StmtList();
    }
    
    public void Stmt(){
        if(lexer.token == Symbol.IDENT)
            AssignStmt();
        else if(lexer.token == Symbol.READ)
            ReadStmt();
        else if(lexer.token == Symbol.WRITE)
            WriteStmt();
        else if(lexer.token == Symbol.RETURN)
            ReturnStmt();
        else if(lexer.token == Symbol.IF)
            IfStmt();
        else if(lexer.token == Symbol.FOR)
            ForStmt();
        else error.signal("STMT invalido");
    }
    
    //AssignStmt ::= AssignExpr ';'
    public void AssignStmt(){
        AssignExpr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("faltou ponto-e-virgula");
        lexer.nextToken();
    }
    
    //AssignExpr ::= IDENT := Expr
    public void AssignExpr(){
        if(lexer.token != Symbol.IDENT)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.ASSIGN)
            error.signal(":= necessario");
        lexer.nextToken();
        Expr();
    }
    
    //ReadStmt ::= READ ‘(‘ IdList ‘);’
    public void ReadStmt(){
        if(lexer.token != Symbol.READ)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        IdList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
    }
    
    //WriteStmt ::= WRITE ‘(‘ IdList ‘);’
    public void WriteStmt(){
        if(lexer.token != Symbol.WRITE)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        IdList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
    }
    
    //ReturnStmt ::= RETURN expr ‘;’
    public void ReturnStmt(){
        if(lexer.token != Symbol.RETURN)
            error.signal("RETURN necessario");
        lexer.nextToken();
        Expr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
    }
    
    //IfStmt ::= IF ( Cond ) THEN StmtList ElsePart ENDIF
    public void IfStmt(){
        if(lexer.token != Symbol.IF)
            error.signal("IF errado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessaria");
        lexer.nextToken();
        Cond();
        if(lexer.token != Symbol.RPAR)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        if(lexer.token != Symbol.THEN)
            error.signal("THEN necessario");
        lexer.nextToken();
        StmtList();
        ElsePart();
        if(lexer.token != Symbol.ENDIF)
            error.signal("ENDIF necessario");
        lexer.nextToken();
    }
    
    //ForStmt ::= FOR ({AssignExpr} ’;’ {Cond} ‘;’ {AssignExpr}) StmtList ENDFOR
    public void ForStmt(){
        if(lexer.token != Symbol.FOR)
            error.signal("FOR errado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessaria");
        lexer.nextToken();
        if(lexer.token == Symbol.IDENT)
            AssignExpr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("Ponto e virgula necessaria");
        lexer.nextToken();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            Cond();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        if(lexer.token == Symbol.IDENT)
            AssignExpr();
        if(lexer.token != Symbol.RPAR)
            error.signal("fechamento de parenteses necessario");
        lexer.nextToken();
        StmtList();
        if(lexer.token != Symbol.ENDFOR)
            error.signal("ENDFOR necessario");
        lexer.nextToken();
    }
    
    //ElsePart ::= ELSE StmtList | empty
    public void ElsePart(){
        if(lexer.token == Symbol.ELSE){
            lexer.nextToken();
            StmtList();
        }
    }
    
    //Cond ::= Expr Compop Expr
    public void Cond(){
        Expr();
        Compop();
        Expr();
    }
    
    //Compop ::= < | > | =
    public void Compop(){
        if(lexer.token != Symbol.EQUAL && lexer.token != Symbol.LT && lexer.token != Symbol.GT)
            error.signal("erro no sinal");
            //adicionar ifs pra saber qual eh
        lexer.nextToken();
    }
    
    public void Expr(){
        Factor();
        ExprTail();
    }
    
    public void ExprTail(){
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
            Addop();
            Factor();
            ExprTail();
        }
    }
    
    public void Factor(){
        PostfixExpr();
        FactorTail();
    }
    
    public void FactorTail(){
        if(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
            Mulop();
            PostfixExpr();
            FactorTail();
        }
    }
    
    public void PostfixExpr(){
        if(lexer.token == Symbol.IDENT){
            if(lexer.checkNextToken() == Symbol.LPAR)
                CallExpr();
            else
                Primary();
        }
        else
            Primary();
            
    }
    
    public void CallExpr(){
        if(lexer.token != Symbol.IDENT)
            error.signal("identificador não encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessária");
        lexer.nextToken();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            ExprList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
    }
    
    public void ExprList(){
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            Expr();
        if(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            ExprList();
        }
    }
    
    
    public void Primary(){
        if(lexer.token == Symbol.LPAR){
            lexer.nextToken();
            Expr();
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
    
    public void Addop(){
        if(lexer.token != Symbol.PLUS && lexer.token != Symbol.MINUS)
            error.signal("+ ou - não encontrado");
        lexer.nextToken();
    }
    
    public void Mulop(){
        if(lexer.token != Symbol.MULT && lexer.token != Symbol.DIV)
            error.signal("* ou / não encontrado");
        lexer.nextToken();
    }
    
}