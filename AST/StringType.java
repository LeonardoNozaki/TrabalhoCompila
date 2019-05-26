package AST;

public class StringType extends Type {

	public StringType() {
		super("String");
	}

	public String getCname() {  // TEM QUE ARRUMAR ISSO, STRING É VETOR DE CHAR
		return "char";
	}
}
