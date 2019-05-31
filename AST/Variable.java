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

public class Variable {
	public Variable( String name, Type type ) {
		this.name = name;
		this.type = type;
	}

	public Variable( String name ) {
		this.name = name;
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

	public void genC(PW pw) {
		pw.print(this.type.getCname() + " " + this.name);
	}

	private String name;
	private Type type;
}
