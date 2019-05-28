package AST;
import java.util.*;
import Lexer.*;

public class BooleanType extends Type {

	public BooleanType() { 
		super("Boolean");
	}
	
	public String getCname() {
		return "int";
	}
}
