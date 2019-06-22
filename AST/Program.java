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
import java.util.*;

public class Program {
	public Program( ArrayList<Function> funcList ) {
		this.funcList = funcList;
	}

	public void genC( PW pw ) {
		int i = 1;
		int tam = this.funcList.size();
		pw.out.println("#include <stdio.h>");
    pw.out.println("#include <stdbool.h>");
		pw.out.println("");
		this.funcList.get(0).genC(pw);
		pw.out.print("\n");
		while(i < tam){
			this.funcList.get(i).genC(pw);
			pw.out.print("\n");
			i++;
		}
	}

	private ArrayList<Function> funcList;
}
