/*************************************************
*   Trabalho 3 - FINAL: Analisador Sintatico     *
*                                                *
*   Evelin Soares                   RA:          *   
*   Renato Vinícius Melaré Corrêa   RA: 620211   *
*   William Adriano Alves           RA: 620203   * 
**************************************************/

import Lexer.*;
import Error.*;
import AST.*;
import AuxComp.*;
import java.io.PrintWriter;
import java.util.ArrayList;

public class Compiler {

    // para geracao de codigo
    public static final boolean GC = true; 
    
    private SymbolTable symbolTable;
    private Lexer lexer;
    private CompilerError error;
    private VarType tipoReturn;

    public Program compile( char []input, PrintWriter outError ) {
        symbolTable = new SymbolTable();
        error = new CompilerError( lexer, new PrintWriter(outError) );
        lexer = new Lexer(input, error);
        error.setLexer(lexer);
                
        lexer.nextToken();
        Program p = null;
        try {
          p = program();
        } catch ( Exception e ) {
              // the below statement prints the stack of called methods.
              // of course, it should be removed if the compiler were 
              // a production compiler.
            e.printStackTrace();
        }
        if ( error.wasAnErrorSignalled() )
          return null;
        else
          return p;                       
    }

    //Program ::= PROGRAM IDENT BEGIN PgmBody END
    //PgmBody ::= Decl FuncDeclarations
    public Program program(){
        if (lexer.token != Symbol.PROGRAM)
                error.signal("faltou Program");
        lexer.nextToken();

        if(lexer.token != Symbol.IDENT)
            error.signal("faltou Identificador");
        Ident ident = new Ident(lexer.getStringValue());
        //Analise Semantica
        //verificar se o identificador ja consta na SymbolTable
        if ( symbolTable.getInGlobal(ident.getIdent()) != null ) 
             error.signal(ident.getIdent() + " ja foi declarado");
        //se nao existe, adiciona
        symbolTable.putInGlobal(ident.getIdent(), ident);    
        lexer.nextToken();

        if(lexer.token != Symbol.BEGIN)
            error.signal("faltou Begin");
        lexer.nextToken();

        ArrayList<Decl> decl = decl();
        FuncDeclarations fd = funcDeclarations();

        if (lexer.token != Symbol.END)
            error.signal("faltou end");
        lexer.nextToken();
        
        //temporario
        if ( lexer.token != Symbol.EOF ) 
            error.signal("EOF expected");
        
        // Analise Semantica
        // Deve haver uma funcao Main
        if (symbolTable.getInGlobal("main") == null )
          error.signal("O codigo fonte deve ter uma funcao chamada main");
        
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
        StringDecl tmp;
        do{
            tmp = stringdecl();
            if(tmp != null)
                sd.add(tmp);
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
            //Analise Semantica
            //verificar se o identificador ja consta na SymbolTable
            if ( symbolTable.getInGlobal(ident.getIdent()) != null ) 
                 error.signal(ident.getIdent() + " ja foi declarado");
            //se nao existe, adiciona
            symbolTable.putInGlobal(ident.getIdent(), new Variable(new VarType(Symbol.STRING.toString()), ident.getIdent()));  
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
        VarDecl tmp;
        do{
            tmp = vardecl();
            if(tmp != null)
                vd.add(tmp);
        }while(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT);
        
        return new VarDecList(vd);
    }
	
    //VarDecl ::= VarType IdList ‘;’ | empty
    public VarDecl vardecl(){
        if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
            VarType tipo = new VarType(lexer.token.toString());
            lexer.nextToken();
            ArrayList<Ident> idList = idList(tipo);
            if(lexer.token != Symbol.SEMICOLON)
               error.signal("ponto-e-virgula nao encontrado");
            lexer.nextToken();
            return new VarDecl(tipo, idList);
        }
        return null;
    }
	
    //IdList ::= IDENT | IDENT ‘,’ IdList
    public ArrayList<Ident> idList(VarType tipo){
        ArrayList<Ident> lista = new ArrayList();
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador nao encontrado");
        //salvar ID
        Ident ident = new Ident(lexer.getStringValue());
        //Analise Semantica
        //verificar se o identificador ja consta na SymbolTable
        if ( symbolTable.getInGlobal(ident.getIdent()) != null ) 
             error.signal(ident.getIdent() + " ja foi declarado");
        //se nao existe, adiciona
        symbolTable.putInGlobal(ident.getIdent(), new Variable(tipo, ident.getIdent()));      
        //adiciona IDENT na lista
        lista.add(ident);
        lexer.nextToken();
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.signal("Identificador nao encontrado");
            //salvar ID
            ident = new Ident(lexer.getStringValue());
            //Analise Semantica
            //verificar se o identificador ja consta na SymbolTable
            if ( symbolTable.getInGlobal(ident.getIdent()) != null ) 
                 error.signal(ident.getIdent() + " ja foi declarado");
            //se nao existe, adiciona
            symbolTable.putInGlobal(ident.getIdent(), new Variable(tipo, ident.getIdent()));      
            //adiciona IDENT na lista
            lista.add(ident);
            lexer.nextToken();
        }
        return lista;
    }
    
    //IdList ::= IDENT | IDENT ‘,’ IdList
    public ArrayList<Ident> idList(){
        ArrayList<Ident> lista = new ArrayList();
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador nao encontrado");
        Ident ident = new Ident(lexer.getStringValue());
        //Analise Semantica
        //verificar se o identificador consta na SymbolTable
	//erro se nao
        if ( symbolTable.getInGlobal(ident.getIdent()) == null ) 
             error.signal(ident.getIdent() + " nao foi declarado");
        lista.add(new Ident(lexer.getStringValue()));
        lexer.nextToken();
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.signal("Identificador nao encontrado");
            ident = new Ident(lexer.getStringValue());
            //Analise Semantica
            //verificar se o identificador consta na SymbolTable
            //erro se nao
            if ( symbolTable.getInGlobal(ident.getIdent()) == null ) 
                 error.signal(ident.getIdent() + " nao foi declarado");
            lista.add(new Ident(lexer.getStringValue()));
            lexer.nextToken();
        }
        return lista;
    }
    
    //ParamDeclList ::= ParamDecl | ParamDecl ‘,’ ParamDeclList
    public ParamDecList paramDecList(){
        ArrayList<ParamDecl> lista = new ArrayList();
        lista.add(paramdecl());
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            lista.add(paramdecl());          
        }
        return new ParamDecList(lista);
    }
    
    //ParamDecl ::= VarType IDENT
    public ParamDecl paramdecl(){
        if(lexer.token != Symbol.FLOAT && lexer.token != Symbol.INT)
            error.signal("tipo invalido de variavel");
        VarType tipo = new VarType(lexer.token.toString());
        lexer.nextToken();
        if(lexer.token != Symbol.IDENT)
            error.signal("Identificador invalido");        
        Ident ident = new Ident(lexer.getStringValue());
        //Analise Semantica
        //verificar se o identificador ja consta na SymbolTable
        if ( symbolTable.getInGlobal(ident.getIdent()) != null ) 
             error.signal(ident.getIdent() + " ja foi declarado");
        //se nao existe, adiciona
        symbolTable.putInGlobal(ident.getIdent(), new Variable(tipo, ident.getIdent()));
        lexer.nextToken();
        
        return new ParamDecl(tipo, ident);
    }
    
    
    public FuncDeclarations funcDeclarations(){
        ArrayList<FuncDecl> fdecl = new ArrayList();
        FuncDecl tmp;
        tmp = funcdecl();
        if(tmp != null)
            fdecl.add(tmp);
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            tmp = funcdecl();
            if(tmp != null)
                fdecl.add(tmp);
        }
        return new FuncDeclarations(fdecl);
    }
    
    public FuncDecl funcdecl(){
        if(lexer.token == Symbol.FUNCTION){
            tipoReturn = null;
            lexer.nextToken();
            if(lexer.token != Symbol.VOID && lexer.token != Symbol.INT && lexer.token != Symbol.FLOAT)
                error.signal("tipo invalido");
            VarType tipo = new VarType(lexer.token.toString());
            lexer.nextToken();
            if(lexer.token != Symbol.IDENT)
                error.signal("identificador nao encontrado");
            Ident ident = new Ident(lexer.getStringValue());
            //Analise Semantica
            //verificar se o identificador ja consta na SymbolTable
            if ( symbolTable.getInGlobal(ident.getIdent()) != null ) 
                 error.signal(ident.getIdent() + " ja foi declarado");
            //se nao existe, adiciona
            symbolTable.putInGlobal(ident.getIdent(), new Variable(tipo, ident.getIdent())); 
            lexer.nextToken();
            if(lexer.token != Symbol.LPAR)
                error.signal("e necessario o uso de parenteses");
            lexer.nextToken();
            ParamDecList pdl = null;
            if(lexer.token == Symbol.INT || lexer.token == Symbol.FLOAT){
                pdl = paramDecList();
            }
            if(lexer.token != Symbol.RPAR)
                error.signal("e necessario fechar o parenteses");
            lexer.nextToken();
            if(lexer.token != Symbol.BEGIN)
                error.signal("BEGIN nao encontrado");
            lexer.nextToken();
            FuncBody fb = funcBody();
            if(lexer.token != Symbol.END)
                error.signal("END nao encontrado");
            lexer.nextToken();
            //analise semantica
            //Verifica se o tipo do retorno e compativel com o tipo da funcao
            if( tipoReturn != null && (tipo.getType()).compareTo(tipoReturn.getType()) != 0)
                error.signal("O tipo do retorno e incompativel com o tipo da funcao");
            tipoReturn = null;
            return new FuncDecl(tipo, ident, pdl, fb);
        }
        
        return null;
    }
    
    public FuncBody funcBody(){
        ArrayList<Decl> decl = decl();
        StmtList stmt = stmtList();
        
        return new FuncBody(decl, stmt);
    }
    
    public StmtList stmtList(){
        ArrayList<Stmt> lista = new ArrayList();
        Stmt tmp;
        do{
            tmp = stmt();
            if(tmp != null)
                lista.add(tmp);
        }while(lexer.token == Symbol.IDENT || lexer.token == Symbol.READ || lexer.token == Symbol.WRITE ||
           lexer.token == Symbol.RETURN || lexer.token == Symbol.IF || lexer.token == Symbol.FOR);
        return new StmtList(lista);
    }
    
    public Stmt stmt(){
        Stmt var = null;
        if(lexer.token == Symbol.IDENT){
            Ident id = new Ident(lexer.getStringValue());
            if ( lexer.checkNextToken() == Symbol.ASSIGN ) 
                var = assignstmt();
            else
                var = callstmt();
        }
        else if(lexer.token == Symbol.READ)
            var = readstmt();
        else if(lexer.token == Symbol.WRITE)
            var = writestmt();
        else if(lexer.token == Symbol.RETURN)
            var = returnstmt();
        else if(lexer.token == Symbol.IF)
            var = ifstmt();
        else if(lexer.token == Symbol.FOR)
            var = forstmt();
        //else error.signal("STMT invalido");
        
        return var;
    }
    
    //AssignStmt ::= AssignExpr ';'
    public AssignStmt assignstmt(){
        //Tokien eh atualmente um IDENT
        //Verifiacr em AssignExpr se ele consta na SymbolTable
        //Erro se nao constar
        AssignExpr assignExpr = assignexpr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("faltou ponto-e-virgula");
        lexer.nextToken();
        return new AssignStmt(assignExpr);
    }
    
    //AssignExpr ::= IDENT := Expr
    public AssignExpr assignexpr(){
        if(lexer.token != Symbol.IDENT)
            error.signal("identificador nao encontrado");        
        Ident id = new Ident(lexer.getStringValue());
        //Analise Semantica
        //verificar se o identificador consta na SymbolTable
	//erro se nao
        if ( symbolTable.getInGlobal(id.getIdent()) == null ) 
             error.signal(id.getIdent() + " nao foi declarado");
        lexer.nextToken();
        
        if(lexer.token != Symbol.ASSIGN)
            error.signal(":= necessario");
        lexer.nextToken();
        Expr expr = expr();
        //Analise Semantica
        //Verificar se o Tipo da variavel e da Expr sao iguais
        //tipo: Int = Int ou String = String
        Variable aux = (Variable) symbolTable.getInGlobal(id.getIdent());
        if( aux.getTipo().getType().compareTo(Symbol.STRING.toString()) == 0 )
            error.signal(id.getIdent() + " nao e compativel com tipo atribuido");
        if( expr.getTipo().compareTo(Symbol.STRING.toString()) == 0 )
            error.signal(id.getIdent() + " nao e compativel com tipo atribuido");
        return new AssignExpr(id, expr);
    }
    
    //ReadStmt ::= READ ‘(‘ IdList ‘);’
    public ReadStmt readstmt(){
        if(lexer.token != Symbol.READ)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        //Analise Semantica
        //Verificar se existem na SymbolTable
        //Erro se nao existem. Verifica dentro de idList
        ArrayList<Ident> idList = idList();
        ArrayList<VarType> typeList = new ArrayList();
        for(Ident id : idList){
            Variable aux = (Variable) symbolTable.getInGlobal(id.getIdent());
            typeList.add(aux.getTipo());
        }
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        return new ReadStmt(idList, typeList);
    }
    
    //WriteStmt ::= WRITE ‘(‘ IdList ‘);’
    public WriteStmt writestmt(){
        if(lexer.token != Symbol.WRITE)
            error.signal("identificador nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        //Analise semantica
        //Verificar se existe na SymbolTable
        //erro se nao
        ArrayList<Ident> idList = idList();
        ArrayList<VarType> typeList = new ArrayList();
        for(Ident id : idList){
            Variable aux = (Variable) symbolTable.getInGlobal(id.getIdent());
            typeList.add(aux.getTipo());
        }
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        return new WriteStmt(idList, typeList);
    }
    
    //ReturnStmt ::= RETURN expr ‘;’
    public ReturnStmt returnstmt(){
        if(lexer.token != Symbol.RETURN)
            error.signal("RETURN necessario");
        lexer.nextToken();
        Expr expr = expr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        //Analise semantica
        //Salva o tipo do retorno para verificar se ele e igual ao tipo da funcao
        tipoReturn = expr.getType();
        return new ReturnStmt(expr);
    }
    
    //IfStmt ::= IF ( Cond ) THEN StmtList ElsePart ENDIF
    public IfStmt ifstmt(){
        if(lexer.token != Symbol.IF)
            error.signal("IF errado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessaria");
        lexer.nextToken();
        Cond cond = cond();
        if(lexer.token != Symbol.RPAR)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        if(lexer.token != Symbol.THEN)
            error.signal("THEN necessario");
        lexer.nextToken();
        StmtList stmtlist = stmtList();
        ElsePart elsepart = elsePart();
        if(lexer.token != Symbol.ENDIF)
            error.signal("ENDIF necessario");
        lexer.nextToken();
        return new IfStmt(cond, stmtlist, elsepart);
    }
    
    //ForStmt ::= FOR ({AssignExpr} ’;’ {Cond} ‘;’ {AssignExpr}) StmtList ENDFOR
    public ForStmt forstmt(){
        AssignExpr assignexpr1 = null;
        AssignExpr assignexpr2 = null;
        Cond cond = null;
        if(lexer.token != Symbol.FOR)
            error.signal("FOR errado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessaria");
        lexer.nextToken();
        if(lexer.token == Symbol.IDENT)
            //Analise semantica feita dentro de AssignExpr()
            assignexpr1 = assignexpr();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("Ponto e virgula necessaria");
        lexer.nextToken();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            //Analise semantica feita dentro de cond()
            cond = cond();
        if(lexer.token != Symbol.SEMICOLON)
            error.signal("ponto e virgula necessario");
        lexer.nextToken();
        if(lexer.token == Symbol.IDENT)
            //Analise Semantica dentro de AssignExpr()
            assignexpr2 = assignexpr();
        if(lexer.token != Symbol.RPAR)
            error.signal("fechamento de parenteses necessario");
        lexer.nextToken();
        StmtList stmtlist = stmtList();
        if(lexer.token != Symbol.ENDFOR)
            error.signal("ENDFOR necessario");
        lexer.nextToken();
        return new ForStmt(assignexpr1, cond, assignexpr2, stmtlist);
    }
    
    //ElsePart ::= ELSE StmtList | empty
    public ElsePart elsePart(){
        StmtList stmtlist = null;
        if(lexer.token == Symbol.ELSE){
            lexer.nextToken();
            stmtlist = stmtList();
        }
        return new ElsePart(stmtlist);
    }
    
    //Cond ::= Expr Compop Expr
    public Cond cond(){
        Expr expr1 = expr();
        ComPop compop = compop();
        Expr expr2 = expr();
        //Analise semantica
        //verificar se tipos sao compativeis
        //tipo: Int < Int, Float = Float 
        if(expr1.getTipo().compareTo(expr2.getTipo()) != 0)
            error.signal("Tipos incompativeis");
        return new Cond(expr1, expr2, compop);
    }
    
    //Compop ::= < | > | =
    public ComPop compop(){
        if(lexer.token != Symbol.EQUAL && lexer.token != Symbol.LT && lexer.token != Symbol.GT)
            error.signal("erro no sinal");
        String compop = lexer.getStringValue();
        lexer.nextToken();
        return new ComPop(compop);
    }
    
    public Expr expr(){
        Factor factor = factor();
        ExprTail exprtail = exprTail();
        //Analise semantica
        //Verifica se os tipos sao compativeis
        if(exprtail != null && exprtail.getType() != null && factor.getType().getType().compareTo(exprtail.getType().getType()) != 0)
            error.signal("Tipos incompativeis");
        return new Expr(factor, exprtail, factor.getType());
    }
    
    public ExprTail exprTail(){
        AddOp addop = null;
        Factor factor = null;
        ExprTail exprtail = null;
        VarType aux = null;
        if(lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
            addop = addop();
            factor = factor();
            exprtail = exprTail();
        }
        //Analise Semantica
        //Verificar se tipos sao compativeis
        if(exprtail != null && exprtail.getType() != null && factor.getType().getType().compareTo(exprtail.getType().getType()) != 0)
            error.signal("Tipos incompativeis");
        if(factor != null)
            aux = factor.getType();
        return new ExprTail(addop, factor, exprtail, aux);
    }
    
    public Factor factor(){
        PostFixExpr postfixexpr = postfixexpr();
        FactorTail factortail = factorTail();
        VarType tipoFactor = null;
        if(factortail != null && factortail.getType() != null && postfixexpr.getType().getType().compareTo(factortail.getType().getType()) != 0)
            tipoFactor = new VarType(Symbol.FLOAT.toString());
        else
            tipoFactor = postfixexpr.getType();
        return new Factor(postfixexpr, factortail, tipoFactor);
    }
    
    public FactorTail factorTail(){
        MulOp mulop = null;
        PostFixExpr postfixexpr = null;
        FactorTail factortail = null;
        VarType tipoFT = null;
        if(lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
            mulop = mulop();
            postfixexpr = postfixexpr();
            factortail = factorTail();
        }
        //Analise Semantica
        //Verificar Tipos
        //Se o tipo de postfixexpr == tipo de factortail entao basta colocar o tipo de um deles como o tipo de factortail
        if( factortail != null && factortail.getType() != null && postfixexpr != null && factortail.getType().getType().compareTo(postfixexpr.getType().getType()) == 0 )
            tipoFT = factortail.getType();
        //Se o tipo de postfixexpr != tipo de factortail então o tipo será FLOAT
        else if( factortail != null && factortail.getType() != null && postfixexpr != null && factortail.getType().getType().compareTo(postfixexpr.getType().getType()) != 0 )
            tipoFT = new VarType(Symbol.FLOAT.toString());
        else
            tipoFT = null;
        return new FactorTail(mulop, postfixexpr, factortail, tipoFT);
    }
    
    public PostFixExpr postfixexpr(){
        PFExpr postfixexpr = null;
        VarType tipoPFExpr = null;
        if(lexer.token == Symbol.IDENT){
            Ident ident = new Ident(lexer.getStringValue());
            //Analise Semantica
            //verificar se o identificador consta na SymbolTable
            //erro se nao
            if ( symbolTable.getInGlobal(ident.getIdent()) == null ) 
                 error.signal(ident.getIdent() + " nao foi declarado");
            if(lexer.checkNextToken() == Symbol.LPAR){
                //Analise Semantica
                //Identifica o tipo da funcao em callexpr para verificacao de tipos
                postfixexpr = callexpr();
                Variable aux = (Variable) symbolTable.getInGlobal(ident.getIdent());
                tipoPFExpr = aux.getTipo();
            }
            else{
                //Analise Semantica
                //Identifica o tipo da funcao em primary para verificacao de tipos
                Primary auxPrimary = primary();
                postfixexpr = auxPrimary;
                tipoPFExpr = auxPrimary.getType();
            }
        }
        else{
            //Analise Semantica
            //Identifica o tipo da funcao em primary para verificacao de tipos
            Primary auxPrimary = primary();
            postfixexpr = auxPrimary;
            tipoPFExpr = auxPrimary.getType();
        }
        
        return new PostFixExpr(postfixexpr, tipoPFExpr);
    }
    
    public CallExpr callexpr(){
        ExprList exprlist = null;
        if(lexer.token != Symbol.IDENT)
            error.signal("identificador não encontrado");
        Ident id = new Ident(lexer.getStringValue());
        //Analise Semantica
        //verificar se o identificador consta na SymbolTable
	//erro se nao
        if ( symbolTable.getInGlobal(id.getIdent()) == null ) 
             error.signal(id.getIdent() + " nao foi declarado");
        lexer.nextToken();
        if(lexer.token != Symbol.LPAR)
            error.signal("abertura de parenteses necessária");
        lexer.nextToken();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT)
            //Analise Semantica para o IDENT
            //dentro do exprList
            exprlist = exprList();
        if(lexer.token != Symbol.RPAR)
            error.signal("parenteses nao encontrado");
        lexer.nextToken();
        return new CallExpr(id, exprlist);
    }
    
    public ExprList exprList(){
        ArrayList<Expr> exprlist = new ArrayList();
        if(lexer.token == Symbol.LPAR || lexer.token == Symbol.IDENT){
            //Analise Semantica para o IDENT 
            if(lexer.token == Symbol.IDENT){
                Ident ident = new Ident(lexer.getStringValue());
                //Analise Semantica
                //verificar se o identificador consta na SymbolTable
                //erro se nao
                if ( symbolTable.getInGlobal(ident.getIdent()) == null ) 
                    error.signal(ident.getIdent() + " nao foi declarado");
            }
            exprlist.add(expr());
        }
        while(lexer.token == Symbol.COMMA){
            lexer.nextToken();
            exprlist.add(expr());
        }
        return new ExprList(exprlist);
    }
    
    
    public Primary primary(){
        PrimaryAbstract primary = null;
        VarType tipoPrimary = null;
        if(lexer.token == Symbol.LPAR){
            lexer.nextToken();
            Expr expr = expr();
            tipoPrimary = expr.getType();
            primary = expr;
            if(lexer.token != Symbol.RPAR)
                error.signal("parenteses não encontrado");
            
        }
        else if(lexer.token == Symbol.IDENT){
            Ident ident = new Ident(lexer.getStringValue());
            //Analise Semantica
            //verificar se o identificador consta na SymbolTable
            //erro se nao
            if ( symbolTable.getInGlobal(ident.getIdent()) == null ) 
                 error.signal(ident.getIdent() + " nao foi declarado");
            Variable aux = (Variable) symbolTable.getInGlobal(ident.getIdent());
            primary = ident;
            tipoPrimary = aux.getTipo();
            lexer.nextToken();
        }
        else if(lexer.token == Symbol.INTLITERAL){
            primary = new IntLiteral("" + lexer.getNumberValue());
            lexer.nextToken();
            tipoPrimary = new VarType(Symbol.INT.toString());
        }
        else if(lexer.token == Symbol.FLOATLITERAL){
            primary = new FloatLiteral("" + lexer.getNumberValue());
            lexer.nextToken();
            tipoPrimary = new VarType(Symbol.FLOAT.toString());
        }
        else
            error.signal("Esperado (, identificador ou number");
        return new Primary(primary, tipoPrimary);
    }
    
    public AddOp addop(){
        if(lexer.token != Symbol.PLUS && lexer.token != Symbol.MINUS)
            error.signal("+ ou - não encontrado");
        AddOp addop = new AddOp(lexer.getStringValue());
        lexer.nextToken();
        return addop;
    }
    
    public MulOp mulop(){
        if(lexer.token != Symbol.MULT && lexer.token != Symbol.DIV)
            error.signal("* ou / não encontrado");
        MulOp mulop = new MulOp(lexer.getStringValue());
        lexer.nextToken();
        return mulop;
    }
    
    public CallStmt callstmt(){
        CallExpr callexpr;
        callexpr = callexpr();
        Variable tipoCallExpr = (Variable) symbolTable.getInGlobal(callexpr.getId().getIdent());
        return new CallStmt(callexpr, tipoCallExpr.getTipo());
    }
    
}