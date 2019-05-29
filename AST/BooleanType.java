package AST;

public class BooleanType extends Type {

	public BooleanType() { 
		super("Boolean");
	}
	
	public String getCname() {
		return "int";
	}
}
