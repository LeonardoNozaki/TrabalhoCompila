package Error;

import Lexer.*;
import java.io.*;

public class CompilerError {

  public CompilerError( PrintWriter out ) {
    // output of an error is done in out
    this.out = out;
    flagC = true;
  }

  public void setLexer( Lexer lexer ) {
    this.lexer = lexer;
  }

  public void signal( String strMessage ) {
    if(flagC == false){
      out.println();
    }
    out.println("Error at line " + lexer.getLineNumber() + ": ");
    out.println(lexer.getCurrentLine());
    out.println( strMessage );

    if ( out.checkError() ) {
      System.out.println("Error in signaling an error");
    }
    flagC = false;
  }

  public boolean getFlagC(){
    return flagC;
  }

  private boolean flagC;
  private Lexer lexer;
  PrintWriter out;
}
