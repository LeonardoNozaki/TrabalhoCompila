import AST.*;
import java.util.*;
import java.lang.Character;
import Error.*;
import Lexer.*;
import java.io.*;

public class Compiler {
  // compile must receive an input with an character less than
  // p_input.lenght
  public Program compile( char []input, PrintWriter outError ) { // OK
    error = new CompilerError( outError );
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

    Program program = new Program(funcList);
    if ( lexer.token != Symbol.EOF ) {
      error.signal("EOF expected");
    }
    return program;
  }

  private Function func() {
    /* Func ::= "function" Id [ "(" ParamList ")" ] ["->" Type ] StatList */

    ArrayList<Variable> arrayParamList = null;
    Type type = null;
    ArrayList<Statement> arrayStatList = null;

    if (lexer.token != Symbol.FUNCTION) {
      error.signal("'function' expected");
    }

    lexer.nextToken();

    if (lexer.token != Symbol.IDENT) {
      error.signal("Function id expected");
    }

    // name of the identifier
    String id = lexer.getStringValue();
    lexer.nextToken();

    if (lexer.token == Symbol.LEFTPAR) {
      lexer.nextToken();
      arrayParamList = paramList();

      if (lexer.token != Symbol.RIGHTPAR)
        error.signal(") expected");

      lexer.nextToken();
    }

    if (lexer.token == Symbol.ARROW) {
      lexer.nextToken();
      type = type();
    }

    // don't need nextToken(), has in end of type()

    arrayStatList = statList();

    return new Function( id, arrayParamList, type, arrayStatList );
  }

  private ArrayList<Variable> paramList() {
    /* ParamList ::= ParamDec {"," ParamDec} */

    ArrayList<Variable> paramList = new ArrayList<Variable>();
    paramDec(paramList);

    while (lexer.token == Symbol.COMMA) {
      paramDec(paramList);
    }
    return paramList;
  }

  private void paramDec(ArrayList<Variable> paramList) {
    /* ParamDec ::= Id ":" Type */

    if (lexer.token != Symbol.IDENT) {
      error.signal("Id expected");
    }

    // name of the identifier
    String id = lexer.getStringValue();
    lexer.nextToken();

    if (lexer.token != Symbol.COLON) {
      error.signal(": expected");
    }
    lexer.nextToken();

    // get the type of the variable
    Type typeVar = type();  // end of type has nextToken()

    Variable v = new Variable(id, typeVar);

    paramList.add(v);  // inserts in the parameter list the variable object
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
        error.signal("Type expected");
        result = null;
    }

    lexer.nextToken();
    return result;
  }


  private ArrayList<Statement> statList() {
    /* StatList ::= "{” {Stat} ”}" */
    /* Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat */

    ArrayList<Statement> stmt = new ArrayList<Statement>(); // Statement is abstract class

    if (lexer.token != Symbol.LEFTBRACE)
      error.signal("{ expected");
    lexer.nextToken();

    while (lexer.token == Symbol.IDENT || lexer.token == Symbol.LITERALINT || lexer.token == Symbol.TRUE ||
            lexer.token == Symbol.FALSE || lexer.token == Symbol.LITERALSTRING || lexer.token == Symbol.RETURN ||
            lexer.token == Symbol.VAR || lexer.token == Symbol.IF || lexer.token == Symbol.WHILE) {
      stmt.add(stat());
    }

    if (lexer.token != Symbol.RIGHTBRACE)
      error.signal("} expected");

    lexer.nextToken();

    return stmt;
  }

  private Statement stat() {
    /* Stat ::= AssignExprStat | ReturnStat | VarDecStat | IfStat | WhileStat */

    switch (lexer.token) {
      case IDENT:      // all are terminals of the assign
      case LITERALINT:
      case TRUE:
      case FALSE:
      case LITERALSTRING:
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
    Expr left = expr();
    Expr right = null;

    if (lexer.token == Symbol.ASSIGN) {
      lexer.nextToken();
      right = expr();
    }

    if (lexer.token != Symbol.SEMICOLON)
      error.signal("; expected");
    lexer.nextToken();

    return new AssignExprStat( left, right );
  }

  private ReturnStat returnStat() {
    /* ReturnStat ::= "return" Expr ";" */
    if (lexer.token != Symbol.RETURN)
      error.signal("'return' expected");
    lexer.nextToken();

    Expr e = expr();

    if (lexer.token != Symbol.SEMICOLON)
      error.signal("; expected");

    lexer.nextToken();

    return new ReturnStat(e);
  }

  private IfStat ifStat() {
    // IfStat ::= "if" Expr StatList [ "else" StatList ]
    ArrayList<Statement> slLeft = new ArrayList<Statement>();
    ArrayList<Statement> slRight = new ArrayList<Statement>();

    if (lexer.token != Symbol.IF)
      error.signal("'if' expected");
    lexer.nextToken();

    Expr e = expr();
    slLeft = statList();

    if ( lexer.token == Symbol.ELSE ) {
      lexer.nextToken();
      slRight = statList();
    }
    return new IfStat( e, slLeft, slRight );
  }

  private VarDecStat varDecStat() {
    // VarDecStat ::= "var" Id ":" Type ";"

    if (lexer.token != Symbol.VAR)
      error.signal("'var' expected");

    lexer.nextToken();

    if (lexer.token != Symbol.IDENT)
      error.signal("Id expected");

    // name of the identifier
    String id = lexer.getStringValue();
    lexer.nextToken();

    if (lexer.token != Symbol.COLON)
      error.signal(": expected");

    lexer.nextToken();
    // get the type
    Type typeVar = type();

    if (lexer.token != Symbol.SEMICOLON)
      error.signal("; expected");
    lexer.nextToken();

    return new VarDecStat( id, typeVar );
  }


  private WhileStat whileStat() {
    // WhileStat ::= "while" Expr StatList
    ArrayList<Statement> sl = new ArrayList<Statement>();

    if (lexer.token != Symbol.WHILE)
      error.signal("'while' expected");

    lexer.nextToken();
    Expr e = expr();

    sl = statList();

    return new WhileStat( e, sl );
  }


  private Expr expr() {
    /* Expr ::= ExprAnd {”or” ExprAnd} */
    ArrayList<ExprAnd> expr = new ArrayList<ExprAnd>();
    expr.add(exprAnd());

    while (lexer.token == Symbol.OR) {
      lexer.nextToken();
      expr.add(exprAnd());
    }

    return new Expr( expr, Symbol.OR );
  }

  private ExprAnd exprAnd() {
    /* ExprAnd ::= ExprRel {”and” ExprRel} */
    ArrayList<ExprRel> expr = new ArrayList<ExprRel>();
    expr.add(exprRel());

    while (lexer.token == Symbol.AND) {
      lexer.nextToken();
      expr.add(exprRel());
    }

    return new ExprAnd( expr, Symbol.AND );
  }

  private ExprRel exprRel() {
    /* ExprRel ::= ExprAdd [ RelOp ExprAdd ] */

    ExprAdd left = exprAdd();
    ExprAdd right = null;
    Symbol op = null;

    /* RelOp ::= "<" | "<=" | ">" | ">=" | "==" | "!=" */
    if (lexer.token == Symbol.LT || lexer.token == Symbol.LE || lexer.token == Symbol.GT ||
        lexer.token == Symbol.GE || lexer.token == Symbol.EQ || lexer.token == Symbol.NEQ){
      op = lexer.token;
      lexer.nextToken();
      right = exprAdd();
    }

    return new ExprRel( left, right, op );
  }

  private ExprAdd exprAdd() {
    /* ExprAdd ::= ExprMult { (” + ” | ” − ”) ExprMult} */

    ArrayList<ExprMult> expr = new ArrayList<ExprMult>();
    expr.add(exprMult());
    Symbol op=null;

    while (lexer.token == Symbol.PLUS || lexer.token == Symbol.MINUS){
      op = lexer.token;
      lexer.nextToken();
      expr.add(exprMult());
    }

    return new ExprAdd( expr, op );
  }

  private ExprMult exprMult() {
    /* ExprMult ::= ExprUnary {(” ∗ ” | ”/”) ExprUnary} */

    ArrayList<ExprUnary> expr = new ArrayList<ExprUnary>();
    expr.add(exprUnary());
    Symbol op=null;

    while (lexer.token == Symbol.MULT || lexer.token == Symbol.DIV){
      op = lexer.token;
      lexer.nextToken();
      expr.add(exprUnary());
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

    return new ExprUnary(exprPrimary, op);
  }

  private ExprPrimary exprPrimary() {
    /* ExprPrimary ::= Id | FuncCall | ExprLiteral */

    switch (lexer.token) {
      case IDENT:
        String id = lexer.getStringValue();
        lexer.nextToken();
        if (lexer.token == Symbol.LEFTPAR)
          return funcCall(id);
        else
          return new StringExpr(id);
      default:
        return new ExprLiteral();
    }
  }

  private ExprLiteral exprLiteral() {
    /* ExprLiteral ::= LiteralInt | LiteralBoolean | LiteralString */
    switch (lexer.token) {
      case LITERALINT:
        int number = lexer.getNumberValue();
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

    // when funcCall is called token already is (
    if (lexer.token != Symbol.LEFTPAR)
      error.signal("( expected");

    lexer.nextToken();

    if (lexer.token == Symbol.IDENT || lexer.token == Symbol.LITERALINT || lexer.token == Symbol.FALSE ||
        lexer.token == Symbol.TRUE || lexer.token == Symbol.LITERALSTRING) {
      expr.add(expr());
      while (lexer.token == Symbol.COMMA){
        lexer.nextToken();
        expr.add(expr());
      }
    }

    if (lexer.token == Symbol.RIGHTPAR)
      error.signal(") expected");

    return new FuncCall( expr, id);
  }

  private Lexer lexer;
  private CompilerError error;
}
