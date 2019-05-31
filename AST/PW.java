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

package AST;

import java.lang.System;

import java.io.*;


public class PW {

   public void add() {
      currentIndent += step;
   }
   public void sub() {
      currentIndent -= step;
   }

   public void set( PrintWriter out ) {
      this.out = out;
      currentIndent = 0;
   }

   public void set( int indent ) {
      currentIndent = indent;
   }

   public int get(){
        return this.currentIndent;
   }

   public void print( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.print(s);
   }

   public void println( String s ) {
      out.print( space.substring(0, currentIndent) );
      out.println(s);
   }

   int currentIndent = 0;
   /* there is a Java and a Green mode.
      indent in Java mode:
      3 6 9 12 15 ...
      indent in Green mode:
      3 6 9 12 15 ...
   */
   static public final int green = 0, java = 1;
   int mode = green;
   public int step = 3;
   public PrintWriter out;


   static final private String space = "                                                                                                        ";

}
