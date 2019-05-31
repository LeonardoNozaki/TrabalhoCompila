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

abstract public class Type {

	public Type( String name ) {
		this.name = name;
	}

	public static Type booleanType = new BooleanType();
	public static Type integerType = new IntegerType();
	public static Type stringType = new StringType();

	public String getName() {
		return name;
	}

	abstract public String getCname();
	private String name;
}
