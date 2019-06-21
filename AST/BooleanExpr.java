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

public class BooleanExpr extends ExprLiteral {

	public BooleanExpr( boolean value ) {
		this.value = value;
	}

	public void genC( PW pw ) {
		pw.out.print( value ? "true" : "false" );
	}

	public Type getType() {
		return Type.booleanType;
	}

  public boolean isId(){
    return false;
  }

	public static BooleanExpr True = new BooleanExpr(true);
	public static BooleanExpr False = new BooleanExpr(false);

	private boolean value;
}
