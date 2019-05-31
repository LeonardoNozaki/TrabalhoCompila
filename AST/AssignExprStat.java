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

public class AssignExprStat extends Statement {
	public AssignExprStat( Expr left, Expr right ) {
		this.left = left;
		this.right = right;
	}

	public void genC( PW pw ) {  // NÃO SEI SE ISSO TÁ CERTO
		pw.print("");
		this.left.genC(pw);
		if ( right != null ) {
			pw.out.print(" = ");
			this.right.genC(pw);
			pw.out.println(";");
		}
		else{
			pw.out.println(";");
		}
	}

	private Expr left, right;
}
