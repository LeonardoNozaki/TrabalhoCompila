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
import java.util.*;
import Lexer.*;

public class Write extends FuncCall {
  public Write(ArrayList<Expr> expr, String id) {
        this.expr = expr;
        this.id = id;
    }

    @Override
    public void genC(PW pw) {
	int size = this.expr.size();
 
	if (size > 0) {
		if ( id.equals(Symbol.WRITE.toString()) ) {
		   pw.out.print("printf (\"");
		   int count = 0;
		   for (Expr e : expr) {
		       if (e.getType() == Type.integerType)
		            pw.out.print("%d");
		       else
			    pw.out.print("%s");
		       
		       if (count != size-1)
		       		pw.out.print(" ");

		       count++;
		   }

		   pw.out.print("\", ");
		   count = 0;
		   for (Expr e : expr) {
			e.genC(pw);
			if (count != size-1)
				pw.out.print(", ");
			count++;
		   }
		   pw.out.print(")");

		} else {  // id = writeln
		  pw.out.print("printf (\"");
		  int count = 0;
		  for (Expr e : expr) {
		       if (e.getType() == Type.integerType)
		            pw.out.print("%d");
			  else
			    pw.out.print("%s");

		       if (count != size-1)
		       		pw.out.print(" ");

		       count++;
		   }

		   pw.out.print("\\r\\n\", ");
		   count = 0;
		   for (Expr e : expr) {
			e.genC(pw);
			if (count != size-1)
				pw.out.print(", ");
			count++;
		   }
		   pw.out.print(")");

              }
	}
    }

    private ArrayList<Expr> expr = new ArrayList<Expr>();
    private String id;

}



