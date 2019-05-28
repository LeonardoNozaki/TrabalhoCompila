package AST;

public class StringExp extends ExprLiteral {

	public StringExpr( String value ) {
		this.value = value;
	}

	public void genC( PW pw ) {
		pw.out.print("’" + value + "’");
	}

	public char getValue() {
		return value;
	}

	public Type getType() {
		return Type.stringType;
	}

	private char value;
}
