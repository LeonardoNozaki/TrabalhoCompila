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

public class VariableExpr extends ExprPrimary {

  public VariableExpr( VarDecStat v ) {
    this.v = v;
  }

  public void genC( PW pw ) {
    pw.out.print( v.getName() );
  }

  public Type getType() {
    return v.getType();
  }

  public boolean isId(){
    return true;
  }

  private VarDecStat v;
}
