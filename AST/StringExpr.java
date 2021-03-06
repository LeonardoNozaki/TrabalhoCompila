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

public class StringExpr extends ExprLiteral {

	public StringExpr( String value ) {
		this.value = value;
	}

	public void genC( PW pw ) {
		pw.out.print("\"" + value + "\"");
	}

	public String getValue() {
		return value;
	}

	public Type getType() {
		return Type.stringType;
	}

  public boolean isId(){
    return false;
  }

  public boolean isFuncCall(){
    return false;
  }

	private String value;
}
