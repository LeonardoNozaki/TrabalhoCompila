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
	FUNCTION("function");

	Symbol(String name) {
		this.name = name;
	}

	public String toString() {
		return name;
	}

	private String name;
}
