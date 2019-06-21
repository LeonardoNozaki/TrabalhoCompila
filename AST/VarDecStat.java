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
  public VarDecStat( String name, Type type, boolean parameter ) {
    this.name = name;
    this.type = type;
    this.parameter = parameter;
  }

  public void genC( PW pw ) {
    if(parameter){
      pw.print(this.type.getCname() + " " + this.name);
    }
    else{
      pw.println(this.type.getCname() + " " + this.name + ";");
    }
  }

  public VarDecStat( String name ) {
    this.name = name;
    this.parameter = false;
  }

  public void setType( Type type ) {
    this.type = type;
  }

  public String getName() {
      return name;
  }

  public Type getType() {
    return type;
  }

  private String name;
  private Type type;
  private boolean parameter;
}
