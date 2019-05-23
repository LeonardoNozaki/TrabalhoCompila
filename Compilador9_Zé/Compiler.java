import AST.*;
import java.util.*;
import java.lang.Character;
import Error.*;
import Lexer.*;
import java.io.*;

public class Compiler {
  // compile must receive an input with an character less than
  // p_input.lenght
  public Program compile( char []input, PrintWriter outError ) {
    symbolTable = new Hashtable<String, Variable>();
    error = new CompilerError( outError );
    lexer = new Lexer(input, error);
    error.setLexer(lexer);
    lexer.nextToken();
    return program();
  }
  private Program program() {

    // Program ::= [ "var" VarDecList ] CompositeStatement
    ArrayList<Variable> arrayVariable = null;

    if ( lexer.token == Symbol.VAR ) {
      lexer.nextToken();
      arrayVariable = varDecList();
    }

    Program program = new Program( arrayVariable, compositeStatement() );
    if ( lexer.token != Symbol.EOF ) {
      error.signal("EOF expected");
    }
    return program;
  }

  private StatementList compositeStatement() {

    // CompositeStatement ::= "begin" StatementList "end"
    // StatementList ::= | Statement ";" StatementList
    if ( lexer.token != Symbol.BEGIN ) {
      error.signal("BEGIN expected");
    }

    lexer.nextToken();
    StatementList sl = statementList();

    if ( lexer.token != Symbol.END ) {
      error.signal("\"end\" expected");
    }

    lexer.nextToken();
    return sl;
  }

  private StatementList statementList() {

    ArrayList<Statement> v = new ArrayList<Statement>();
    // statements always begin with an identifier, if, read or write

    while ( lexer.token == Symbol.IDENT || lexer.token == Symbol.IF || lexer.token == Symbol.READ ||
    lexer.token == Symbol.WRITE ) {
      v.add( statement() );
      if ( lexer.token != Symbol.SEMICOLON ) {
        error.signal("; expected");
      }
      lexer.nextToken();
    }

    return new StatementList(v);
  }

  private Statement statement() {
    /* Statement ::= AssignmentStatement | IfStatement | ReadStatement | WriteStatement */

    switch (lexer.token) {
      case IDENT :
        return assignmentStatement();
      case IF :
        return ifStatement();
      case READ :
        return readStatement();
      case WRITE :
        return writeStatement();
      default :

      // will never be executed
      error.signal("Statement expected");
    }

    return null;
  }

  private AssignmentStatement assignmentStatement() {

    String name = lexer.getStringValue();

    // is the variable in the symbol table ? Variables are inserted in the
    // symbol table when they are declared. It the variable is not there, it has
    // not been declared.

    Variable v = (Variable ) symbolTable.get(name);

    // was it in the symbol table ?
    if ( v == null ) {
      error.signal("Variable " + name + " was not declared");
    }
    lexer.nextToken();

    if ( lexer.token != Symbol.ASSIGN ) {
      error.signal("= expected");
    }
    lexer.nextToken();

    Expr right = orExpr();

    // semantic analysis
    // check if expression has the same type as variable

    if ( v.getType() != right.getType() ) {
      error.signal("Type error in assignment");
    }

    return new AssignmentStatement( v, right );
  }

  private IfStatement ifStatement() {

    lexer.nextToken();
    Expr e = orExpr();

    // semantic analysis
    // check if expression has type boolean
    if ( e.getType() != Type.booleanType ) {
      error.signal("Boolean type expected in if expression");
    }

    if ( lexer.token != Symbol.THEN ) {
      error.signal("then expected");
    }

    lexer.nextToken();
    StatementList thenPart = statementList();
    StatementList elsePart = null;

    if ( lexer.token == Symbol.ELSE ) {
      lexer.nextToken();
      elsePart = statementList();
    }

    if ( lexer.token != Symbol.ENDIF ) {
      error.signal("\"endif\" expected");
    }

    lexer.nextToken();
    return new IfStatement( e, thenPart, elsePart );
  }


  private ReadStatement readStatement() {

    lexer.nextToken();
    if ( lexer.token != Symbol.LEFTPAR ) {
      error.signal("( expected");
    }

    lexer.nextToken();
    if ( lexer.token != Symbol.IDENT ) {
      error.signal("Identifier expected");
    }

    // semantic analysis
    // check if the variable was declared
    String name = lexer.getStringValue();
    Variable v = (Variable ) symbolTable.get(name);

    if ( v == null ) {
      error.signal("Variable " + name + " was not declared");
    }

    // semantic analysis
    // check if variable has type char or integer
    if ( v.getType() != Type.charType && v.getType() != Type.integerType ) {
      error.signal("Variable should have type char or integer");
    }

    lexer.nextToken();

    if ( lexer.token != Symbol.RIGHTPAR ) {
      error.signal(") expected");
    }

    lexer.nextToken();
    return new ReadStatement( v );
  }

  private WriteStatement writeStatement() {

    lexer.nextToken();
    if ( lexer.token != Symbol.LEFTPAR ) {
      error.signal("( expected");
    }

    lexer.nextToken();

    // expression may be of any type
    Expr e = orExpr();
    if ( lexer.token != Symbol.RIGHTPAR ) {
      error.signal(") expected");
    }

    lexer.nextToken();
    return new WriteStatement( e );
  }

  private ArrayList<Variable> varDecList() {
    // VarDecList ::= VarDecList2 { VarDecList2}

    ArrayList<Variable> varList = new ArrayList<Variable>();
    varDecList2(varList);

    while ( lexer.token == Symbol.IDENT ) {
      varDecList2(varList);
    }
    return varList;
  }

  private void varDecList2( ArrayList<Variable> varList ) {

    // VarDecList2 ::= Ident { ’,’ Ident } ’:’ Type ’;’
    ArrayList<Variable> lastVarList = new ArrayList<Variable>();

    while ( true ) {
      if ( lexer.token != Symbol.IDENT ) {
        error.signal("Identifier expected");
      }

      // name of the identifier
      String name = lexer.getStringValue();
      lexer.nextToken();

      // semantic analysis
      // if the name is in the symbol table, the variable is been declared twice.
      if ( symbolTable.get(name) != null ) {
        error.signal("Variable " + name + " has already been declared");
      }

      // variable does not have a type yet
      Variable v = new Variable(name);

      // inserts the variable in the symbol table. The name is the key and an
      // object of class Variable is the value. Hash tables store a pair (key, value)
      // retrieved by the key.
      symbolTable.put( name, v );

      // list of the last variables declared. They don’t have types yet
      lastVarList.add(v);

      if ( lexer.token == Symbol.COMMA ) {
        lexer.nextToken();

      } else {
        break;
      }
    }

    if ( lexer.token != Symbol.COLON ) {
      error.signal(": expected");
    }
    lexer.nextToken();

    // get the type
    Type typeVar = type();

    for ( Variable v : lastVarList ) {

      // add type to the variable
      v.setType(typeVar);

      // add variable to the list of variable
      varList.add(v);
    }

    if ( lexer.token != Symbol.SEMICOLON ) {
      error.signal("; expected");
    }

    lexer.nextToken();
  }

  private Type type() {
    Type result;

    switch ( lexer.token ) {
      case INTEGER :
        result = Type.integerType;
        break;
      case BOOLEAN :
        result = Type.booleanType;
        break;
      case CHAR :
        result = Type.charType;
        break;
      default :
        error.signal("Type expected");
        result = null;
    }

    lexer.nextToken();
    return result;
  }

  private Expr orExpr() {
  /* OrExpr ::= AndExpr [ "or" AndExpr ] */

    Expr left, right;
    left = andExpr();
    if ( lexer.token == Symbol.OR ) {
      lexer.nextToken();
      right = andExpr();

      // semantic analysis
      if ( left.getType() != Type.booleanType || right.getType() != Type.booleanType ) {
        error.signal("Expression of boolean type expected");
      }
      left = new CompositeExpr(left, Symbol.OR, right);
    }
    return left;
  }

  private Expr andExpr() {
    /* AndExpr ::= RelExpr [ "and" RelExpr ] */
    Expr left, right;
    left = relExpr();
    if ( lexer.token == Symbol.AND ) {
      lexer.nextToken();
      right = relExpr();
      // semantic analysis
      if ( left.getType() != Type.booleanType || right.getType() != Type.booleanType ) {
        error.signal("Expression of boolean type expected");
      }
      left = new CompositeExpr( left, Symbol.AND, right );
    }
    return left;
  }

  private Expr relExpr() {
    /* RelExpr ::= AddExpr [ RelOp AddExpr ] */
    Expr left, right;
    left = addExpr();
    Symbol op = lexer.token;

    if ( op == Symbol.EQ || op == Symbol.NEQ || op == Symbol.LE || op == Symbol.LT ||
    op == Symbol.GE || op == Symbol.GT ) {
      lexer.nextToken();
      right = addExpr();

      // semantic analysis
      if ( left.getType() != right.getType() ) {
        error.signal("Type error in expression");
      }
      left = new CompositeExpr( left, op, right );
    }
    return left;
  }

  private Expr addExpr() {
    /* AddExpr ::= MultExpr { AddOp MultExpr } */
    Symbol op;
    Expr left, right;
    left = multExpr();
    while ( (op = lexer.token) == Symbol.PLUS || op == Symbol.MINUS ) {
      lexer.nextToken();
      right = multExpr();

      // semantic analysis
      if ( left.getType() != Type.integerType || right.getType() != Type.integerType ) {
        error.signal("Expression of type integer expected");
      }
      left = new CompositeExpr( left, op, right );
    }
    return left;
  }


  private Expr multExpr() {

    /* MultExpr ::= SimpleExpr { MultOp SimpleExpr } */
    Expr left, right;
    left = simpleExpr();
    Symbol op;
    while ( (op = lexer.token) == Symbol.MULT || op == Symbol.DIV || op == Symbol.REMAINDER ) {
      lexer.nextToken();
      right = simpleExpr();
      // semantic analysis
      if ( left.getType() != Type.integerType || right.getType() != Type.integerType ) {
        error.signal("Expression of type integer expected");
      }
      left = new CompositeExpr( left, op, right );
    }
    return left;
  }

  private Expr simpleExpr() {

    /* SimpleExpr ::= Number | "true" | "false" | Character | ’(’ Expr ’)’ | "not" SimpleExpr | Variable */
    Expr e;

    // note we test the lexer.getToken() to decide which production to use
    switch ( lexer.token ) {
      case NUMBER :
        return number();
      case TRUE :
        lexer.nextToken();
        return BooleanExpr.True;
      case FALSE :
        lexer.nextToken();
        return BooleanExpr.False;
      case CHARACTER :

        // get the token with getToken.
        // then get the value of it, with has the type Object
        // convert the object to type Character using a cast
        // call method charValue to get the character inside the object
        char ch = lexer.getCharValue();
        lexer.nextToken();
        return new CharExpr(ch);
      case LEFTPAR :
        lexer.nextToken();
        e = orExpr();
        if ( lexer.token != Symbol.RIGHTPAR ) {
          error.signal(") expected");
        }
        lexer.nextToken();
        return e;
      case NOT :
        lexer.nextToken();
        e = orExpr();

        // semantic analysis
        if ( e.getType() != Type.booleanType ) {
          error.signal("Expression of type boolean expected");
        }
        return new UnaryExpr( e, Symbol.NOT );
      case PLUS :
        lexer.nextToken();
        e = orExpr();

        // semantic analysis
        if ( e.getType() != Type.integerType ) {
          error.signal("Expression of type integer expected");
        }
        return new UnaryExpr( e, Symbol.PLUS );
      case MINUS :
        lexer.nextToken();
        e = orExpr();

        // semantic analysis
        if ( e.getType() != Type.integerType ) {
          error.signal("Expression of type integer expected");
        }
        return new UnaryExpr( e, Symbol.MINUS );
      default :

        // an identifier
        if ( lexer.token != Symbol.IDENT ) {
          error.signal("Identifier expected");
        }

        String name = lexer.getStringValue();
        lexer.nextToken();
        Variable v = (Variable ) symbolTable.get( name );

        // semantic analysis
        // was the variable declared ?
        if ( v == null ) {
          error.signal("Variable " + name + " was not declared");
        }
        return new VariableExpr(v);
      }
    }

    private NumberExpr number() {
      NumberExpr e = null;

      // the number value is stored in lexer.getToken().value as an object of Integer.
      // Method intValue returns that value as an value of type int.
      int value = lexer.getNumberValue();
      lexer.nextToken();
      return new NumberExpr( value );
    }

    private Hashtable<String, Variable> symbolTable;
    private Lexer lexer;
    private CompilerError error;
}
