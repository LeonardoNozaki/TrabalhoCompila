package AST;

public class NumberExpr extends ExprLiteral {
	public NumberExpr( int value ) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}

	public void genC( PW pw ) {
		pw.out.print(value.toString());
	}

	public Type getType() {
		return Type.integerType;
	}

	private Integer value;
}
