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

public class ReturnStat extends Statement {

	public ReturnStat( Expr e ) {
		this.e = e;
	}

  public Expr getExpr(){
    return this.e;
  }

	public void genC( PW pw ) {
		pw.print("return ");
		this.e.genC(pw);
		pw.out.println(";");
	}

	private Expr e;
}
