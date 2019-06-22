/* ==========================================================================
 * Universidade Federal de São Carlos - Campus Sorocaba
 * Disciplina: Compiladores
 * Prof. Leticia Berto
 *
 * Trabalho - Análise Semântica (Fase 2)
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

  public CompilerError( PrintWriter out, String nomeArq) {
    this.nomeArq = nomeArq;
    this.flagC = true;
    this.contador = 0;
  }

  public void setLexer( Lexer lexer ) {
    this.lexer = lexer;
  }

  public void signal( String strMessage ) {
    contador++;
    if(flagC == false){
      System.out.println();
    }
/*
    \n<nome do arquivo>:<numero da linha de erro>:<mensagem de erro>\n<linha do codigo com erro>

    Em <nome do arquivo> nao é para conter o caminho do arquivo!
    Em <linha do codigo com erro> pode manter o padrao do compilador, de imprimir o codigo proximo ao
ponto onde o token estava no momento que o erro ocorreu.
    O compilador pode continuar lancando excecao java.lang.RuntimeException, mas a mensagem de erro deve
estar no formato citado.*/

    System.out.println(nomeArq + ": " + lexer.getLineNumber() + ": " + strMessage);
    System.out.println(lexer.getCurrentLine());

    flagC = false;
  }

  public boolean getFlagC(){
    return flagC;
  }

  public int getNumError(){
    return contador;
  }

  private boolean flagC;
  private Lexer lexer;
  private String nomeArq;
  private int contador;
}
