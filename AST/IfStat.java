package AST;

import java.util.*;

public class IfStat extends Statement {
	public IfStat( Expr expr, ArrayList<Statement> leftPart, ArrayList<Statement> rightPart ) {
		this.expr = expr;
		this.leftPart = leftPart;
		this.rightPart = rightPart;
	}

	public void genC( PW pw ) {
	}

	private Expr expr;
	private ArrayList<Statement> leftPart = new ArrayList<Statement>();
  private ArrayList<Statement> rightPart = new ArrayList<Statement>();
}
