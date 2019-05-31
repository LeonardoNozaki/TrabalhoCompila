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

public class VarDecStat extends Statement {
  public VarDecStat( String name, Type type ) {
    this.name = name;
    this.type = type;
  }

  public void genC( PW pw ) {
    pw.println(this.type.getCname() + " " + this.name + ";");
  }

  private String name;
  private Type type;
}
