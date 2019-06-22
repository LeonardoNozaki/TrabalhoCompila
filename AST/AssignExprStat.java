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

public class AssignExprStat extends Statement {
	public AssignExprStat( Expr left, Expr right, int read ) {
		this.left = left;
		this.right = right;
    this.read = read;
	}

	public void genC( PW pw ) {
    if(this.read == 1){ //ReadInt
      pw.print("scanf(\"%d\", &");
      this.left.genC(pw);
      pw.out.println(");");
    }
    else if(this.read == 2){ //ReadString
      pw.print("scanf(\"%s\", &");
      this.left.genC(pw);
      pw.out.println(");");
    }
    else{
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
	}

	private Expr left, right;
  private int read;
}
