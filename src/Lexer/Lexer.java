/*
 Program ::= 'begin' VarDecList ';' AssignStatment 'end'
 VarDecList ::= Variable | Variable ',' VarDecList
 Variable ::= Letter {Letter}
 Letter ::= 'A' | 'B' | ... | 'Z' | 'a' | ... |'z'
 AssignStatment ::= Variable '=' Expr ';'
 Expr ::= Oper  Expr Expr  | Number
 Oper ::= '+' | '-' | '*' | '/'
 Number ::= Digit {Digit}
 Digit ::= '0'| '1' | ... | '9'
 */

package Lexer;

import java.util.*;
import Error.*;

public class Lexer {

	// apenas para verificacao lexica
	public static final boolean DEBUGLEXER = false;
        private boolean checkTokenPrint = true;
        private boolean STRFLAG = false;
    
    public Lexer( char[] input, CompilerError error ) {
        this.input = input;
        // add an end-of-file label to make it easy to do the lexer
        input[input.length - 1] = '\0';
        // number of the current line
        lineNumber = 1; 
        tokenPos = 0;
        this.error = error;
    }
    
    // contains the keywords - palavras reservadas da linguagem
    static private Hashtable<String, Symbol> keywordsTable;
    
    // this code will be executed only once for each program execution
    static {
        keywordsTable = new Hashtable<String, Symbol>();
        keywordsTable.put("program", Symbol.PROGRAM);
        keywordsTable.put( "begin", Symbol.BEGIN );
        keywordsTable.put( "end", Symbol.END );
        keywordsTable.put( "function", Symbol.FUNCTION );
        keywordsTable.put( "read", Symbol.READ );
        keywordsTable.put( "write", Symbol.WRITE );        
        keywordsTable.put( "if", Symbol.IF );
        keywordsTable.put( "then", Symbol.THEN );
        keywordsTable.put( "else", Symbol.ELSE );
        keywordsTable.put( "endif", Symbol.ENDIF );
        keywordsTable.put( "return", Symbol.RETURN );
        keywordsTable.put( "for", Symbol.FOR );
        keywordsTable.put( "endfor", Symbol.ENDFOR );
        keywordsTable.put( "float", Symbol.FLOAT );
        keywordsTable.put( "int", Symbol.INT );        
        keywordsTable.put( "void", Symbol.VOID );
        keywordsTable.put( "string", Symbol.STRING );        
        keywordsTable.put( "intnumber", Symbol.INTLITERAL );        
        keywordsTable.put( "floatnumber", Symbol.FLOATLITERAL );
        keywordsTable.put( "stringliteral", Symbol.STRINGLITERAL );
    }
    
    
    public void nextToken() {
        
        //tokens a serem ignorados
        while(input[tokenPos] == ' ' ||
              input[tokenPos] == '\n' || input[tokenPos] == '\t' || input[tokenPos] == '\r'){
            if (input[tokenPos] == '\n'){
                lineNumber++;
            }
            tokenPos++;
        }
        
        //indica o final do arquivo
        if (input[tokenPos] == '\0'){
            token = Symbol.EOF;
            return;
        }
        
        //verificar se eh um comentario
        if (input[tokenPos] == '-' && input[tokenPos+1] == '-'){
            //enquanto nao houver quebra de linha ou fim do arquivo
            //ainda eh um comentario
            while(input[tokenPos] != '\n' && input[tokenPos] != '\0'){
                tokenPos++;
            }
            //importante!!!
            nextToken();
            //(os comentarios serao ignorados, esse nextToken retornara ao compilador
            //um tolken 'util')
            return;
        }

        //-------  Tokens 'uteis' (funcoes, variaveis, parametros...) -------
        
        //Quero reconhecer o token (que serah um dos Symbols)
        StringBuffer aux = new StringBuffer();
        //enquanto houver numeros na entrada
        while (Character.isDigit(input[tokenPos])){
            //os numeros serao concatenados em uma string
            aux = aux.append(input[tokenPos]);
            //move o indicador de onde esta o inicio do proximo token 
            tokenPos++;
        }

        //se a string nao for nula (tem numeros nela)
        if (aux.length() > 0){
            //converte a string para inteiro
            numberValue = Integer.parseInt(aux.toString());
            //caso o inteiro seja maior que o permitido, erro!
            if (numberValue > MaxValueInteger){
                error.signal("Numero inteiro maior que o permitido!");
            }
            //deu tudo certo: aviso que o token se trata de um numero
            token = Symbol.NUMBER;
        
        // se a string estava vazia, era pq nao havia numeros, entao:
        } else {
            //pode se tratar de letras
            //(se for) enquanto houverem letras
            STRFLAG = false;
            if(input[tokenPos] == '\"'){
                STRFLAG = true;
                tokenPos++;
            }
            while (Character.isLetter(input[tokenPos]) && !STRFLAG){
                //concatena as letras em uma string
                aux = aux.append(input[tokenPos]);
                tokenPos++;
            }
            
            while(input[tokenPos] != '\"' && input[tokenPos] != ';' && input[tokenPos] != '\0' && STRFLAG){
                aux = aux.append(input[tokenPos]);
                tokenPos++;
            }
            if(input[tokenPos] != '\"' && STRFLAG)
                error.signal("Faltou fechar aspas");
            if(STRFLAG)
                tokenPos++;
            
            //se realmente haviam letras
            if (aux.length() > 0){
                Symbol temp;
                //verifica se a palavra formada pelas letras eh uma palavra reservada
                if(STRFLAG){
                    temp = keywordsTable.get(Symbol.STRINGLITERAL.toString());
                    stringValue = aux.toString();
                }
                else
                    temp = keywordsTable.get(aux.toString().toLowerCase());
                //se nao for
                if (temp == null){ 
                    //entao se trata do nome de alguma variavel/funcao
                    //avisa que se trata de um identificador
                    token = Symbol.IDENT;
                    //salvo o nome do identificador
                    stringValue = aux.toString();
                }
                else {
                    //se for uma palavra reservada, aviso de qual comando se trata
                    token = temp;
                }
            //se a entrada nao eh nem numero, nem palavra
            //entao trata-se de um simbolo/operador
            } else {
                switch (input[tokenPos]){
                    case '+':
                        token = Symbol.PLUS;
                        break;
                    case '-':
                        token = Symbol.MINUS;
                        break;
                    case '*':
                        token = Symbol.MULT;
                        break;
                    case '/':
                        token = Symbol.DIV;
                        break;
                    case '=':
                        token = Symbol.EQUAL;
                        break;
                    case '<':
                        token = Symbol.LT;
                        break;
                    case '>':
                        token = Symbol.GT;
                        break;
                    case '(':
                        token = Symbol.LPAR;
                        break;
                    case ')':
                        token = Symbol.RPAR;
                        break;
                    case ':':
                        if(input[tokenPos+1] == '='){
                            token = Symbol.ASSIGN;
                            tokenPos++;
                        }    
                        break;
                    case ',':
                        token = Symbol.COMMA;
                        break;
                    case ';':
                        token = Symbol.SEMICOLON;
                        break;
                    default:
                        error.signal("erro lexico");
                }
                tokenPos++;
            }
        }
        if (DEBUGLEXER && checkTokenPrint)
            System.out.println(token.toString());
        
        //indica onde eh o fim do token
        lastTokenPos = tokenPos - 1;
    }
    
    // return the line number of the last token got with getToken()
    public int getLineNumber() {
        return lineNumber;
    }
    
    public String getCurrentLine() {
        int i = lastTokenPos;
        if ( i == 0 )
            i = 1;
        else
            if ( i >= input.length )
                i = input.length;
        
        StringBuffer line = new StringBuffer();
        // go to the beginning of the line
        while ( i >= 1 && input[i] != '\n' )
            i--;
        if ( input[i] == '\n' )
            i++;
        // go to the end of the line putting it in variable line
        while ( input[i] != '\0' && input[i] != '\n' && input[i] != '\r' ) {
            line.append( input[i] );
            i++;
        }
        return line.toString();
    }
    
    public String getStringValue() {
        return stringValue;
    }
    
    public int getNumberValue() {
        return numberValue;
    }
    
    public char getCharValue() {
        return charValue;
    }
    
    public Symbol checkNextToken(){
        Symbol bkptoken = token;
        String bkpstringValue = this.stringValue;
        int bkpnumberValue = this.numberValue;
        char bkpcharValue = this.charValue;
        int bkptokenPos = this.tokenPos;
        int bkplastTokenPos = this.lastTokenPos;
        int bkplineNumber = this.lineNumber;
        
        Symbol returnToken;
        
        
        this.checkTokenPrint = false;
        this.nextToken();
        this.checkTokenPrint = true;
        returnToken = this.token;
        
        
        
        this.stringValue = bkpstringValue;
        this.numberValue = bkpnumberValue;
        this.charValue = bkpcharValue;
        this.tokenPos = bkptokenPos;
        this.lastTokenPos = bkplastTokenPos;
        this.lineNumber = bkplineNumber;
        this.token = bkptoken;
        
        return returnToken;
                
    }
    // current token
    public Symbol token;
    private String stringValue;
    private int numberValue;
    private char charValue;
    
    private int  tokenPos;
    //  input[lastTokenPos] is the last character of the last token
    private int lastTokenPos;
    // program given as input - source code
    private char[] input;
    
    // number of current line. Starts with 1
    private int lineNumber;
    
    private CompilerError error;
    private static final int MaxValueInteger = 32768;
}