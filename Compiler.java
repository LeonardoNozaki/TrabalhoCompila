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

import AST.*;
import java.util.*;
import java.lang.Character;
import Error.*;
import Lexer.*;
import java.io.*;
import AuxComp.SymbolTable;

public class Compiler {
  // compile must receive an input with an character less than
  // p_input.lenght
  public Program compile( char []input, PrintWriter outError, String nomeArq ) {
    symbolTable = new SymbolTable();
    error = new CompilerError( outError, nomeArq);
    lexer = new Lexer(input, error);
    error.setLexer(lexer);
    lexer.nextToken();
    return program();
  }

  private Program program() {
    /* Program ::= Func {Func} */

    ArrayList<Function> funcList = new ArrayList<Function>();
    funcList.add(func());

    while (lexer.token == Symbol.FUNCTION) {
      funcList.add(func());
    }

    //Se nao houver mais funcoes entao deve ser o fim do arquivo (EOF)
    Program program = new Program(funcList);
    if ( lexer.token != Symbol.EOF ) {
      error.signal("EOF expected, anything is wrong");
    }

    //Analise semantica, verificar se existe uma funcao chamada main no codigo
    if(symbolTable.getInGlobal("main") == null){
      error.signal("Source code must have a Function called main");
    }
    return program;
  }

  private Function func() {
    /* Func ::= "function" Id [ "(" ParamList ")" ] ["->" Type ] StatList */
    returnTypeFunction = Type.voidType;
    ArrayList<VarDecStat> arrayParamList = null;
    Type type = null;
    ArrayList<Statement> arrayStatList = null;
    String id = "";
    Function s = null;
    int flag = 0;

    if (lexer.token != Symbol.FUNCTION) {
      if(lexer.token == Symbol.LITERALINT){
        error.signal("'function' expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("'function' expected before " + lexer.getStringValue());
      }
      else{
        error.signal("'function' expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    if(lexer.getKeyWords(lexer.token.toString())){
      error.signal(lexer.token + " is a keyword, not a identifier of function");
      lexer.nextToken();
    }
    else if (lexer.token != Symbol.IDENT) {
      if(lexer.token == Symbol.LITERALINT){
        error.signal("Function Id expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("Function Id expected before " + lexer.getStringValue());
      }
      else{
        error.signal("Function Id expected before " + lexer.token);
      }
    }
    else{
      //Analise semantica, funcao nao deve ter sido declarada anteriormente
      id = (String) lexer.getStringValue();
      returnIdFunction = id;
      s = (Function) symbolTable.getInGlobal(id);
      if(s != null){
        error.signal("Function '" + id + "' has already been declared");
        flag = 1; //Flag de controle, valor 1 indica que funcao ja existe com esse id
      }
      lexer.nextToken();
      //Analise sematica, main nao deve ter parametros nem retorno
      if ( id.compareTo("main") == 0 && lexer.token != Symbol.LEFTBRACE){
        error.signal("Function 'main' must be a parameterless and returnless function");
      }
    }

    if (lexer.token == Symbol.LEFTBRACE){
      //Se o token atual é {, entao nao tem parametros nem retorno
      //Adiciona o cabeçalho da funcao antes, para permitir chamada recursiva
      s = new Function( id, arrayParamList, type, null );
      symbolTable.putInGlobal( id, s );

      arrayStatList = statList();
      /*for (Statement x: arrayStatList){
        if (x instanceof ReturnStat) {
          error.signal("Function '" + id + "' must have no return");
        }
      }*/

      s = new Function( id, arrayParamList, type, arrayStatList );
      symbolTable.removeLocalIdent();
      return s;
    }
    else if (lexer.token == Symbol.LEFTPAR) {
      //Verifica os parametros
      lexer.nextToken();
      arrayParamList = paramList();

      if(lexer.token == Symbol.IDENT){
        error.signal(", expected before " + lexer.getStringValue());
      }
      else if (lexer.token != Symbol.RIGHTPAR){
        if(lexer.token == Symbol.LITERALINT){
          error.signal(") expected before " + lexer.getNumberValue());
        }
        else if(lexer.token == Symbol.LITERALSTRING){
          error.signal(") expected before " + lexer.getStringValue());
        }
        else{
          error.signal(") expected before " + lexer.token);
        }
      }
      else{
        lexer.nextToken();
      }
    }

    if (lexer.token == Symbol.LEFTBRACE){ // void
      //Se o token atual é {, entao nao tem retorno
      //Adiciona o cabeçalho da funcao antes, para permitir chamada recursiva
      s = new Function( id, arrayParamList, type, null );
      symbolTable.putInGlobal( id, s );

      arrayStatList = statList();
      /*for(Statement x: arrayStatList){
        if(x instanceof ReturnStat){
          error.signal("function '" + id + "' must have no return");
        }
      }*/

      s =  new Function( id, arrayParamList, type, arrayStatList );
      symbolTable.removeLocalIdent();
      return s;
    }
    else if (lexer.token == Symbol.ARROW) {
      //verifica o retorno
      lexer.nextToken();
      type = type();
      returnTypeFunction = type;
    }

    if (lexer.token == Symbol.LEFTBRACE){
      //Se o token atual é {, entao nao possui erros
      //Adiciona o cabeçalho da funcao antes, para permitir chamada recursiva
      s = new Function( id, arrayParamList, type, null );
      symbolTable.putInGlobal( id, s );

      boolean hasReturn = false;
      arrayStatList = statList();
      /*for(Statement x: arrayStatList){
        if(x instanceof ReturnStat){
          hasReturn = true;
          String typeReturn = ( (ReturnStat) x).getExpr().getTypeStringValue();
          if(typeReturn != null && type.equals(typeReturn)){
            error.signal("function " + id + " has different return type");
          }
        }
      }

      if (!hasReturn){
        error.signal("function " + id + " must have return");
      }*/

      s = new Function( id, arrayParamList, type, arrayStatList );
      symbolTable.removeLocalIdent();
      return s;
    }
    else{
      //Esta faltando algo no cabecalho da funcao
      if(lexer.token == Symbol.LITERALINT){
        error.signal("( or -> expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("( or -> expected before " + lexer.getStringValue());
      }
      else{
        error.signal("( or ->  expected before " + lexer.token);
      }
    }

    // don't need nextToken(), has in end of type()

    //Adiciona o cabeçalho da funcao antes, para permitir chamada recursiva
    s = new Function( id, arrayParamList, type, null );
    symbolTable.putInGlobal( id, s );

    arrayStatList = statList();
    /*boolean hasReturn = false;
    for(Statement x: arrayStatList){
      if(x instanceof ReturnStat){
        hasReturn = true;
        String typeReturn = ( (ReturnStat) x).getExpr().getTypeStringValue();
        if(typeReturn != null && type.equals(typeReturn)){
            error.signal("function " + id + " has different return type");
        }
      }
    }

    if (!hasReturn){
      error.signal("function " + id + " must have return");
    }*/

    s = new Function( id, arrayParamList, type, arrayStatList );
    symbolTable.removeLocalIdent();
    return s;
  }

  private ArrayList<VarDecStat> paramList() {
    /* ParamList ::= ParamDec {"," ParamDec} */

    symbolTable.add();//Acrescente um nivel na pilha de hash das variaveis locais

    ArrayList<VarDecStat> paramList = new ArrayList<VarDecStat>();
    paramDec(paramList);

    while (lexer.token == Symbol.COMMA) {
      lexer.nextToken();
      paramDec(paramList);
    }
    return paramList;
  }

  private void paramDec(ArrayList<VarDecStat> paramList) {
    /* ParamDec ::= Id ":" Type */
    String id = "";
    int flag = 0;

    if(lexer.getKeyWords(lexer.token.toString())){
      error.signal(lexer.token + " is a keyword, not a identifier of parameter");
      lexer.nextToken();
    }
    else if (lexer.token != Symbol.IDENT) {
      if(lexer.token == Symbol.LITERALINT){
        error.signal("Id expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("Id expected before " + lexer.getStringValue());
      }
      else{
        error.signal("Id expected before " + lexer.token);
      }
      flag = 1; //variavel sem id
    }
    else {
      id = lexer.getStringValue();
      lexer.nextToken();
    }

    if (lexer.token != Symbol.COLON) {
      if(lexer.token == Symbol.LITERALINT){
        error.signal(": expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal(": expected before " + lexer.getStringValue());
      }
      else{
        error.signal(": expected before " + lexer.token);
      }
    }
    else {
      lexer.nextToken();
    }

    // get the type of the variable
    Type typeVar = type();  // end of type has nextToken()

    VarDecStat v = new VarDecStat(id, typeVar, true);

    paramList.add(v);  // inserts in the parameter list the variable object

    //Analise semantica
    //Verifica se a variavel tem id e tipo valido
    if(flag == 0 && typeVar != null){
      //Verifica se a variavel ja foi declarada ou nao
      if(symbolTable.getInLocal(id) != null){
        error.signal("var " + id + " has already been declared");
      }
      else{
        symbolTable.putInLocal(id, v);
      }
    }
  }

  private Type type() {
    /* Type ::= "Int" | "Boolean" | "String" */

    Type result;

    switch ( lexer.token ) {
      case INT :
        result = Type.integerType;
        break;
      case BOOLEAN :
        result = Type.booleanType;
        break;
      case STRING :
        result = Type.stringType;
        break;
      default :  // only supports the above types
        error.signal("Type expected: Int, Boolean or String");
        result = Type.undefinedType; //Antes era resulta = null;
    }
    lexer.nextToken();
    return result;
  }

  private ArrayList<Statement> statList() {
    /* StatList ::= "{” {Stat} ”}" */
    /* Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat */

    ArrayList<Statement> stmt = new ArrayList<Statement>(); // Statement is abstract class

    if (lexer.token != Symbol.LEFTBRACE){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("{ expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("{ expected before " + lexer.getStringValue());
      }
      else{
        error.signal("{ expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }
    symbolTable.add(); //Acrescente um nivel na pilha de hash das variaveis locais

    while (lexer.token == Symbol.IDENT || lexer.token == Symbol.LITERALINT || lexer.token == Symbol.TRUE ||
            lexer.token == Symbol.FALSE || lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.RETURN ||
            lexer.token == Symbol.VAR || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE ||
	    lexer.token == Symbol.WRITE || lexer.token == Symbol.WRITELN) {
      stmt.add(stat());
    }
    symbolTable.sub(); //Remove um nivel na pilha de hash das variaveis locais



    if (lexer.token != Symbol.RIGHTBRACE){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("} expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("} expected before " + lexer.getStringValue());
      }
      else{
        error.signal("} expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    return stmt;
  }

  private Statement stat() {
    /* Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat */

    switch (lexer.token) {
      case IDENT:   // all are terminals of the assign
      case LITERALINT:
      case TRUE:
      case FALSE:
      case LITERALSTRING:
      case WRITE:
      case WRITELN:
        return assignExprStat();
      case RETURN:
        return returnStat();
      case VAR:
        return varDecStat();
      case IF:
        return ifStat();
      case WHILE:
        return whileStat();
      default :
        error.signal("Statement expected");
    }

    return null;
  }

  private AssignExprStat assignExprStat() {
    /* AssignExprStat ::= Expr [ "=" Expr ] ";" */
    String id = lexer.getStringValue();
    Expr left = expr();
    Expr right = null;
    int flag = 0;

    if (lexer.token == Symbol.ASSIGN) {
      flag = 1;  //flag == 1, tem uma atribuicao de forma correta
      if(left.isId() == false){ //Lado esquerdo nao é variavel
        error.signal("Id expected before " + lexer.token);
        flag = 0; //Atribuicao invalida, pq nao é um id antes da atribuicao
      }
      //Se lado esquerdo for variavel, ja é feito a verificacao de declaração la em baixo
      lexer.nextToken();
      if(lexer.token == Symbol.READINT){
        lexer.nextToken();
        VarDecStat vds = (VarDecStat) symbolTable.getInLocal(id);
        if(vds != null){
          if(vds.getType() != Type.integerType){
            error.signal("ReadInt returns Int Type, var " + id + " must be Int Type");
          }
        }
        read();
        return new AssignExprStat( left, right, 1);
      } else if(lexer.token == Symbol.READSTRING){
        lexer.nextToken();
        VarDecStat vds = (VarDecStat) symbolTable.getInLocal(id);
        if(vds != null){
          if(vds.getType() != Type.stringType){
            error.signal("ReadString returns String Type, var " + id + " must be String Type");
          }
        }
        read();
        return new AssignExprStat( left, right, 2);
      }

      right = expr();
      if(flag == 1 && right != null){
        if ( ! checkAssignment( left.getType(), right.getType() ) ){
          //error.signal("Type error in assignment");
          error.signal("Assignment: Function void Type or left side Type is different to right side Type");
        }
      }
      if (lexer.token != Symbol.SEMICOLON){
        if(lexer.token == Symbol.LITERALINT){
          error.signal("; expected before " + lexer.getNumberValue());
        }
        else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
          error.signal("; expected before " + lexer.getStringValue());
        }
        else{
          error.signal("; expected before " + lexer.token);
        }
      }
      else{
        lexer.nextToken();
      }

    } else if(lexer.token != Symbol.SEMICOLON){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("; or = expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("; or = expected before " + lexer.getStringValue());
      }
      else{
        error.signal("; or = expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }


    return new AssignExprStat( left, right, 0);
  }

  private boolean checkAssignment( Type varType, Type exprType ) {
    if ( varType == Type.undefinedType || exprType == Type.undefinedType )
      return true;
    else if(varType == Type.voidType || exprType == Type.voidType)
      return false;
    else
      return varType == exprType;
  }

  private boolean checkReturn(Type varType, Type exprType ) {
    if ( varType == Type.undefinedType || exprType == Type.undefinedType ){
      return true;
    }
    else{
      return varType == exprType;
    }
  }
  private ReturnStat returnStat() {
    /* ReturnStat ::= "return" Expr ";" */
    if (lexer.token != Symbol.RETURN){ //Teoricamente nunca vai mostrar essa msg
      error.signal("'return' expected before " + lexer.token);
    }
    else{
      lexer.nextToken();
    }

    Expr e = expr();
    if(returnTypeFunction == Type.voidType){
      error.signal("function '" + returnIdFunction + "' mustn’t have return");
    }
    else if(! checkReturn( returnTypeFunction, e.getType() )){
      error.signal("Return type is different to return Function Type");
    }
    if (lexer.token != Symbol.SEMICOLON){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("; expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("; expected before " + lexer.getStringValue());
      }
      else{
        error.signal("; expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }
    return new ReturnStat(e);
  }

  private IfStat ifStat() {
    // IfStat ::= "if" Expr StatList [ "else" StatList ]
    ArrayList<Statement> slLeft = new ArrayList<Statement>();
    ArrayList<Statement> slRight = new ArrayList<Statement>();

    if (lexer.token != Symbol.IF){
      error.signal("'if' expected");
    }
    else{
      lexer.nextToken();
    }

    Expr e = expr();
    //If e while precisam de expr boolean, entao pode usar a msm verificacao
    if ( ! checkWhileExpr(e.getType()) ){
      error.signal("Boolean expression expected");
    }

    slLeft = statList();

    if ( lexer.token == Symbol.ELSE ) {
      lexer.nextToken();
      slRight = statList();
    }

    return new IfStat( e, slLeft, slRight );
  }

  private VarDecStat varDecStat() {
    // VarDecStat ::= "var" Id ":" Type ";"

    if (lexer.token != Symbol.VAR){
      error.signal("'var' expected before " + lexer.token);
    }
    else{
      lexer.nextToken();
    }

    if(lexer.getKeyWords(lexer.token.toString())){
      error.signal(lexer.token + " is a keyword, not a identifier of variable");
      lexer.nextToken();
    }
    else if (lexer.token != Symbol.IDENT){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("Id expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("Id expected before " + lexer.getStringValue());
      }
      else{
        error.signal("Id expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }
    // name of the identifier
    String id = lexer.getStringValue();
    if(symbolTable.getInLocal(id) != null){
      error.signal("var " + id + " has already been declared");
    }

    if (lexer.token != Symbol.COLON){
      if(lexer.token == Symbol.LITERALINT){
        error.signal(": expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal(": expected before " + lexer.getStringValue());
      }
      else{
        error.signal(": expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }
    // get the type
    Type typeVar = type();

    if (lexer.token != Symbol.SEMICOLON){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("; expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("; expected before " + lexer.getStringValue());
      }
      else{
        error.signal("; expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    VarDecStat v = new VarDecStat( id, typeVar, false );

    symbolTable.putInLocal(id, v);

    return v;
  }


  private WhileStat whileStat() {
    // WhileStat ::= "while" Expr StatList
    ArrayList<Statement> sl = new ArrayList<Statement>();

    if (lexer.token != Symbol.WHILE){
      error.signal("'while' expected");
    }
    else{
      lexer.nextToken();
    }
    Expr e = expr();
    if ( ! checkWhileExpr(e.getType()) ){
      error.signal("Boolean expression expected");
    }

    sl = statList();

    return new WhileStat( e, sl );
  }

  private boolean checkWhileExpr( Type exprType ) {
    if ( exprType == Type.undefinedType || exprType == Type.booleanType )
      return true;
    else
      return false;
  }


  private Expr expr() {
    /* Expr ::= ExprAnd {”or” ExprAnd} */
    ArrayList<ExprAnd> expr = new ArrayList<ExprAnd>();
    ExprAnd right = null;

    ExprAnd left = exprAnd();
    Type type = left.getType();
    expr.add(left);

    while (lexer.token == Symbol.OR) {
      lexer.nextToken();
      right = exprAnd();
      expr.add(right);
      // analise semantica
      if ( ! checkBooleanExpr( type, right.getType() ) ){
        error.signal("Operation 'or' expects Boolean Type arguments, before " + lexer.token);
      }
    }

    return new Expr( expr );
  }

  private boolean checkBooleanExpr( Type left, Type right ) {
    if ( left == Type.undefinedType || right == Type.undefinedType )
      return true;
    else
      return left == Type.booleanType && right == Type.booleanType;
  }

  private ExprAnd exprAnd() {
    /* ExprAnd ::= ExprRel {”and” ExprRel} */
    ArrayList<ExprRel> expr = new ArrayList<ExprRel>();
    Type type;
    ExprRel right = null;

    ExprRel left = exprRel();
    type = left.getType();
    expr.add(left);

    while (lexer.token == Symbol.AND) {
      lexer.nextToken();
      right = exprRel();
      expr.add(right);
      // analise semantica
      if ( ! checkBooleanExpr( type, right.getType() ) ){
        error.signal("Operation 'and' expects Boolean Type arguments, before " + lexer.token);
      }
    }

    return new ExprAnd( expr );
  }

  private ExprRel exprRel() {
    /* ExprRel ::= ExprAdd [ RelOp ExprAdd ] */

    ExprAdd left = exprAdd();
    Type type = left.getType();
    ExprAdd right = null;
    Symbol op = null;

    /* RelOp ::= "<" | "<=" | ">" | ">=" | "==" | "!=" */
    if (lexer.token == Symbol.LT || lexer.token == Symbol.LE || lexer.token == Symbol.GT ||
        lexer.token == Symbol.GE || lexer.token == Symbol.EQ || lexer.token == Symbol.NEQ){
      op = lexer.token;
      lexer.nextToken();
      right = exprAdd();
      if ( ! checkRelExpr(type, right.getType() ) ){
        error.signal("Comparation of differents types or String/Void Type involved, before " + lexer.token);
      }
    }

    return new ExprRel( left, right, op);
  }

  private boolean checkRelExpr( Type left, Type right ) {
    if ( left == Type.undefinedType || right == Type.undefinedType )
      return true;
    else if ( left == Type.stringType || right == Type.stringType )
      return false;
    else if(left == Type.voidType || right == Type.voidType)
      return false;
    else
      return left == right;
  }

  private ExprAdd exprAdd() {
    /* ExprAdd ::= ExprMult { (” + ” | ” − ”) ExprMult} */

    ArrayList<ExprMult> expr = new ArrayList<ExprMult>();
    ArrayList<Symbol> op = new ArrayList<Symbol>();
    Type type;
    ExprMult right = null;

    ExprMult left = exprMult();
    type = left.getType();
    expr.add(left);

    while (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
      op.add(lexer.token);
      lexer.nextToken();
      right = exprMult();
      expr.add(right);
      if ( ! checkMathExpr( type, right.getType() ) ){
        error.signal("Operation '+' or '-' expects Interger Type arguments, before " + lexer.token);
      }
    }

    return new ExprAdd( expr, op);
  }

  private boolean checkMathExpr( Type left, Type right ) {
    boolean orLeft = left == Type.integerType || left == Type.undefinedType;
    boolean orRight = right == Type.integerType || right == Type.undefinedType;

    if ( left == Type.stringType || right == Type.stringType )
      return false;
    else if(left == Type.voidType || right == Type.voidType)
      return false;
    return orLeft && orRight;
  }

  private ExprMult exprMult() {
    /* ExprMult ::= ExprUnary {(” ∗ ” | ”/”) ExprUnary} */

    ArrayList<ExprUnary> expr = new ArrayList<ExprUnary>();
    ArrayList<Symbol> op = new ArrayList<Symbol>();
    Type type;
    ExprUnary right = null;

    ExprUnary left = exprUnary();
    type = left.getType();
    expr.add(left);

    while (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
      op.add(lexer.token);
      lexer.nextToken();
      right = exprUnary();
      expr.add(right);
      if ( ! checkMathExpr( type, right.getType() ) ){
        error.signal("Operation '*' or '/' expects Interger Type arguments, before " + lexer.token);
      }
    }

    return new ExprMult( expr, op );
  }

  private ExprUnary exprUnary() {
    /* ExprUnary ::= [ ( "+" | "-" ) ] ExprPrimary */

    Symbol op=null;
    if (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
      op = lexer.token;
      lexer.nextToken();
    }

    ExprPrimary exprPrimary = exprPrimary();
    Type type = exprPrimary.getType();

    // se teve operação então só pode ser int
    if (op != null){
      if ( type != Type.integerType ){
        error.signal("Integer Type expected before " + lexer.token);
      }
    }

    return new ExprUnary(exprPrimary, op);
  }

  private ExprPrimary exprPrimary() {
    /* ExprPrimary ::= Id | FuncCall | ExprLiteral */

    switch (lexer.token) {
      case IDENT:
      case WRITE:
      case WRITELN:
        String id = lexer.getStringValue();
        lexer.nextToken();
        if (lexer.token == Symbol.LEFTPAR){
          return funcCall(id);
        }
        else{
          VarDecStat v = (VarDecStat) symbolTable.getInLocal( id );
          if(v == null){
            error.signal("Variable " + id + " was not declared");
            VarDecStat v1 = new VarDecStat(id);
            return new VariableExpr(v1);
          }
          return new VariableExpr(v);
        }
      default:
        return exprLiteral();
    }
  }

  private ExprLiteral exprLiteral() {
    /* ExprLiteral ::= LiteralInt | LiteralBoolean | LiteralString */
    switch (lexer.token) {
      case LITERALINT:
        int number = lexer.getNumberValue();
	      String n = lexer.getStringValue();
        lexer.nextToken();
        return new NumberExpr(number);
     case TRUE :
        lexer.nextToken();
        return BooleanExpr.True;
      case FALSE :
        lexer.nextToken();
        return BooleanExpr.False;
      case LITERALSTRING:
        String s = lexer.getStringValue();
        lexer.nextToken();
        return new StringExpr(s);
      default :
        error.signal("ExprLiteral expected");
        return null;
    }
  }

  private FuncCall funcCall(String id) {
    /* FuncCall ::= Id "(" [ Expr {”, ”Expr} ] ")" */
    ArrayList<Expr> expr = new ArrayList<Expr>();
    ArrayList<Type> types = new ArrayList<Type>();
    Expr aux_expr;

    // when funcCall is called token already is (
    if (lexer.token != Symbol.LEFTPAR){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("( expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("( expected before " + lexer.getStringValue());
      }
      else{
        error.signal("( expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    if (lexer.token == Symbol.IDENT || lexer.token == Symbol.LITERALINT || lexer.token == Symbol.FALSE ||
        lexer.token == Symbol.TRUE || lexer.token == Symbol.LITERALSTRING) {
      aux_expr = expr();
      types.add(aux_expr.getType());
      expr.add(aux_expr);
      while (lexer.token == Symbol.COMMA){
        lexer.nextToken();
        aux_expr = expr();
        types.add(aux_expr.getType());
        expr.add(aux_expr);
      }
    }

    if (lexer.token != Symbol.RIGHTPAR){
      if(lexer.token == Symbol.LITERALINT){
        error.signal(") expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal(") expected before " + lexer.getStringValue());
      }
      else{
        error.signal(") expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    if (id.equals(Symbol.WRITE.toString()) || id.equals(Symbol.WRITELN.toString())){
      return new Write( expr, id );
    }


    Function f = (Function) symbolTable.getInGlobal(id);
    if(f != null){ //Funcao ja declarada
      if(f.getSize() != types.size()){
        error.signal("Number of parameters in '" + id + "' is incorrect");
      }
      for(int i = 0; i < types.size() && i < f.getSize(); i++){
        if(f.getTypeParameter(i) != types.get(i)){
          error.signal("Parameter " + (i+1) + " of '" + id + "' not matching function declaration");
        }
      }
      return new FuncCall( expr, id, f.getType());
    }
    else{
      error.signal("Function '" + id + "' was not declared");
    }

    return new FuncCall( expr, id, Type.undefinedType);
  }

  private void read() {
    /* readInt ::= readInt "(" ")" */
    /* readString ::= readString "(" ")" */

    // when funcCall is called token already is (
    if (lexer.token != Symbol.LEFTPAR){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("( expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("( expected before " + lexer.getStringValue());
      }
      else{
        error.signal("( expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    if (lexer.token != Symbol.RIGHTPAR){
      if(lexer.token == Symbol.LITERALINT){
        error.signal(") expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal(") expected before " + lexer.getStringValue());
      }
      else{
        error.signal(") expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }

    if (lexer.token != Symbol.SEMICOLON){
      if(lexer.token == Symbol.LITERALINT){
        error.signal("; expected before " + lexer.getNumberValue());
      }
      else if(lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.IDENT){
        error.signal("; expected before " + lexer.getStringValue());
      }
      else{
        error.signal("; expected before " + lexer.token);
      }
    }
    else{
      lexer.nextToken();
    }
  }

  public boolean getC(){
    return error.getFlagC();
  }

  public int getNumErrors(){
    return error.getNumError();
  }

  private SymbolTable symbolTable;
  private Lexer lexer;
  private CompilerError error;
  private Type returnTypeFunction;
  private String returnIdFunction;
}

