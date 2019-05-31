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
