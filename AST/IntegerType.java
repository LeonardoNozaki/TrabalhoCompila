package AST;

public class IntegerType extends Type {
	public IntegerType() {
		super("Integer");
	}

	public String getCname() {
		return "int";
	}
}
