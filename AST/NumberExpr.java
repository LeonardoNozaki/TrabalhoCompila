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

package AST;

public class NumberExpr extends ExprLiteral {
	public NumberExpr( int value ) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void genC( PW pw ) {
		pw.out.print(value.toString());
	}

	public Type getType() {
		return Type.integerType;
	}

  public boolean isId(){
    return false;
  }

  public boolean isFuncCall(){
    return false;
  }

	private Integer value;
}
