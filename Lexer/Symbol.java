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

package Lexer;

public enum Symbol {
	  EOF("eof"),
	  IDENT("Id"),
	  LEFTPAR("("),
	  RIGHTPAR(")"),
	  LEFTBRACE("{"),
	  RIGHTBRACE("}"),
	  ARROW("->"),
	  COMMA(","),
	  COLON(":"),
	  ASSIGN("="),
	  SEMICOLON(";"),
	  INT("Int"),
	  BOOLEAN("Boolean"),
	  STRING("String"),
	  LITERALINT("LiteralInt"),
	  LITERALSTRING("LiteralString"),
	  RETURN("return"),
	  VAR("var"),
	  IF("if"),
	  ELSE("else"),
	  WHILE("while"),
	  OR("or"),
	  AND ("and"),
	  LT("<"),
	  LE("<="),
	  GT(">"),
	  GE(">="),
	  EQ("=="),
	  NEQ("!="),
	  PLUS("+"),
	  MINUS("-"),
	  MULT("*"),
	  DIV("/"),
	  TRUE("true"),
	  FALSE("false"),
	  WRITE("write"),
	  WRITELN("writeln"),
	  READINT("readInt"),
      READSTRING("readString"),
	  FUNCTION("function");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;
}
