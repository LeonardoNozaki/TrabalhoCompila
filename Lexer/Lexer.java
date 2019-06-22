/* ==========================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Disciplina: Compiladores
 * Prof. Leticia Berto
 *
 * Trabalho - Análise Léxica e Sintática (Fase 1)
 *
 * Aluno: Bruno Rizzi       RA: 743515
 * Aluna: Giulia Fazzi      RA: 743542
 * Aluno: Leonardo Nozaki   RA: 743561
 * Aluna: Michele Carvalho  RA: 726573
 * ========================================================================== */

package Lexer;
import java.util.*;
import Error.*;

public class Lexer {

  public Lexer( char []input, CompilerError error ) {
    this.input = input;

    //Adiciona o '\0' como ultimo caracter do array
    //O '\0' serve para indicar que nao tem mais nada para ler
    //Ou seja, significa o EOF da entrada
    input[input.length - 1] = '\0';

    //Numero da linha atual
    lineNumber = 1;

    //Posicao do token atual
    tokenPos = 0;

    //Utiliza o CompilerError passado como parametro
    //para enviar os erros
    this.error = error;
  }

  public boolean getKeyWords(Object key){
    Object obj = keywordsTable.get(key);
    if(obj == null){
      return false;
    }
    return true;
  }
  //Hash para colocar as palavras reservadas
  static private Hashtable<String, Symbol> keywordsTable;

  //Esse codigo vai ser executado uma unica vez em toda execucao do programa
  static {
    //Atribui as palavras reservadas na hash
    keywordsTable = new Hashtable<String, Symbol>();

    keywordsTable.put( "var", Symbol.VAR );
    keywordsTable.put( "Int", Symbol.INT );
    keywordsTable.put( "Boolean", Symbol.BOOLEAN );
    keywordsTable.put( "String", Symbol.STRING );
    keywordsTable.put( "function", Symbol.FUNCTION );
    keywordsTable.put( "return", Symbol.RETURN );
    keywordsTable.put( "if", Symbol.IF );
    keywordsTable.put( "else", Symbol.ELSE );
    keywordsTable.put( "while", Symbol.WHILE );
    keywordsTable.put( "or", Symbol.OR );
    keywordsTable.put( "and", Symbol.AND );
    keywordsTable.put( "true", Symbol.TRUE );
    keywordsTable.put( "false", Symbol.FALSE );
    keywordsTable.put( "write", Symbol.WRITE );
    keywordsTable.put( "writeln", Symbol.WRITELN );
    keywordsTable.put( "readInt", Symbol.READINT );
    keywordsTable.put( "readString", Symbol.READSTRING );
  }

  //Metodo para pegar o proximo token da analise lexica
  public void nextToken() {
    char ch;

    //Ignorar espacos em branco, mudanca do cursor para o inicio
    //tabs e quebra de linha
    while ((ch = input[tokenPos]) == ' ' || ch == '\r' || ch == '\t' || ch == '\n') {
      if ( ch == '\n') {
        //Contar o numero de linhas ignoradas
        lineNumber++;
      }

      tokenPos++;
    }

    //Se o caracter atual é o '\0', significa fim do arquivo (EOF)
    if ( ch == '\0') {
      token = Symbol.EOF;
    } else {
      //Se nao é o fim do arquivo
      //Verifica se é um comentario de linha
      if ( input[tokenPos] == '/' && input[tokenPos + 1] == '/' ) {
        //Comentario de linha ignora tudo ate que haja:
        //Uma quebra de linha ou fim do arquivo de entrada
        while ( input[tokenPos] != '\0' && input[tokenPos] != '\n' ) {
          tokenPos++;
        }

        //Chama a funcao recursivamente para pegar o proximo token
        //Pois esse token era o de um comentario em linha.
        nextToken();
      } else if ( input[tokenPos] == '/' && input[tokenPos + 1] == '*' ) {
        //Verifica se é um comentario de bloco

        //Comentario de bloco ignora tudo ate que haja:
        //Fim do arquivo ou */
        while ( input[tokenPos] != '\0' && !(input[tokenPos] == '*' && input[tokenPos + 1] == '/')) {
          if(input[tokenPos] == '\n'){
            //Contar o numero de linhas ignoradas
            lineNumber++;
          }
          tokenPos++;
        }

        if(input[tokenPos] == '*' && input[tokenPos + 1] == '/'){
          tokenPos = tokenPos + 2;
        }

        //Chama a funcao recursivamente para pegar o proximo token
        //Pois esse token era o de um comentario em linha
        nextToken();
      } else {
        //Se nao for comentario, faz analise lexica

        if ( Character.isLetter( ch ) ) {
          //Pega um Id ou palavra reservada
          //Id é formado apenas por letras
          StringBuffer ident = new StringBuffer();

          //Pega o maior conjunto de letras possiveis(maior casamento)
          while ( Character.isLetter( input[tokenPos] ) ) {
            ident.append(input[tokenPos]);
            tokenPos++;
          }

          //Converte o maior casamento para uma string
          stringValue = ident.toString();

          //Utiliza essa string como entrada na hash
          Symbol value = keywordsTable.get(stringValue);

          //Se o retorno da Hash for null, a string nao esta na hash
          if ( value == null ) {
            //Nao é uma palavra reservada, logo é um Id
            token = Symbol.IDENT;
          } else {
            //Esta na hash, é uma palavra reservada
            token = value;
          }

          //Nao pode haver uma palavra e um numero em seguida
          if ( Character.isDigit(input[tokenPos]) ) {
            error.signal(stringValue + " followed by a number: " + input[tokenPos]);
          }
          while ( Character.isDigit( input[tokenPos] ) ) {
            tokenPos++;
          }
        } else if ( Character.isDigit( ch ) ) {
          //Pega um numero
          StringBuffer number = new StringBuffer();

          //Pega o maior conjunto de numeros possiveis(maior casamento)
          while ( Character.isDigit( input[tokenPos] ) ) {
            number.append(input[tokenPos]);
            tokenPos++;
          }
          token = Symbol.LITERALINT;

          try {
            //Converte o maior casamento para um inteiro
            numberValue = Integer.valueOf(number.toString()).intValue();
          } catch ( NumberFormatException e ) {
            //Emite erro se o numero nao for convertido corretamente
            error.signal("Number out of limits");
          }

          //Emite erro se o numero estiver fora dos limites permitidos
          if ( numberValue > MaxValueInteger ) {
            error.signal("Number out of limits");
          }

          //Nao pode haver um numero e uma letra em seguida
          if ( Character.isLetter( input[tokenPos] )) {
            error.signal(number.toString() + " followed by a letter: " + input[tokenPos]);
          }
          while ( Character.isLetter( input[tokenPos] ) ) {
            tokenPos++;
          }
        } else {
          //Pega simbolos

          //O token atual esta armazenado em ch,
          //Entao vai para a proxima posicao
          tokenPos++;

          //Verifica os possiveis simbolos
          switch ( ch ) {
            case '+' :
              token = Symbol.PLUS;
              break;
            case '-' :
              if( input[tokenPos] == '>'){
                tokenPos++;
                token = Symbol.ARROW;
              }
              else
                token = Symbol.MINUS;
              break;
            case '*' :
              token = Symbol.MULT;
              break;
            case '/' :
              token = Symbol.DIV;
              break;
            case '!' :
              if ( input[tokenPos] == '=' ) {
                tokenPos++;
                token = Symbol.NEQ;
              } else {
                error.signal("Invalid symbol: ’" + ch + "’");
              }
              break;
            case '<' :
              if ( input[tokenPos] == '=' ) {
                tokenPos++;
                token = Symbol.LE;
              } else {
                token = Symbol.LT;
              }
              break;
            case '>' :
              if ( input[tokenPos] == '=' ) {
                tokenPos++;
                token = Symbol.GE;
              } else {
                token = Symbol.GT;
              }
              break;
            case '{' :
              token = Symbol.LEFTBRACE;
              break;
            case '}' :
              token = Symbol.RIGHTBRACE;
              break;
            case '=' :
              if ( input[tokenPos] == '=' ) {
                tokenPos++;
                token = Symbol.EQ;
              } else {
                token = Symbol.ASSIGN;
              }
              break;
            case '(' :
              token = Symbol.LEFTPAR;
              break;
            case ')' :
              token = Symbol.RIGHTPAR;
              break;
            case ',' :
              token = Symbol.COMMA;
              break;
            case ';' :
              token = Symbol.SEMICOLON;
              break;
            case ':' :
              token = Symbol.COLON;
              break;
            case '"' :
              StringBuffer ident = new StringBuffer();

              //Pega o conjunto de caracteres, esse é valido se esta entre as aspas duplas " "
              //Esse conjunto é invalido se:
              //Houve quebra de linha ou fim do arquivo antes de fechar as aspas duplas
              while ( input[tokenPos] != '\0' && input[tokenPos] != '\n' && input[tokenPos] != '"') {
                ident.append(input[tokenPos]);
                tokenPos++;
              }

              if( input[tokenPos] == '"'){
                //Esta entre aspas duplas, valido
                token = Symbol.LITERALSTRING;

                //Converte o conjunto para uma string
                stringValue = ident.toString();
                tokenPos++;
              } else if( input[tokenPos] == '\n'){
                //Houve quebra de linha sem o fim das aspas duplas, invalido
                error.signal("\" expected");
                tokenPos++;
              } else if ( input[tokenPos] == '\0' ) {
                //Fim do arquivo sem o fim das aspas duplas, invalido
                error.signal("\" expected");
                nextToken();
              }
              break;
            default :
              error.signal("Invalid Character: ’" + ch + "’");
          }
        }
      }
    }
    lastTokenPos = tokenPos - 1;
  }

  //Retorna o numero da linha do ultimo token pego
  public int getLineNumber() {
    return lineNumber;
  }

  //Pega o conteudo da linha atual
  public String getCurrentLine() {
    int i = lastTokenPos;
    if ( i == 0 ) {
      i = 1;

    } else {
      if ( i >= input.length ) {
        i = input.length;
      }
    }
    StringBuffer line = new StringBuffer();

    //Volta do ultimo token ate o comeco da linha
    while ( i >= 1 && input[i] != '\n' ) {
      i--;
    }

    if ( input[i] == '\n' ) {
      i++;
    }

    //Percorre ate o fim da linha colocando todos os caracteres da linha
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


  //Valores do token atual
  public Symbol token;
  private String stringValue;
  private int numberValue;
  private int tokenPos;



  // input[lastTokenPos] is the last character of the last token
  private int lastTokenPos;

  // program given as input - source code
  private char []input;

  // number of current line. Starts with 1
  private int lineNumber;
  private CompilerError error;
  private static final int MinValueInteger = 0;
  private static final int MaxValueInteger = 2147483647;
}
