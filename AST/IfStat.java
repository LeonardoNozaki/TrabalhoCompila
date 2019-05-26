package AST;

public class IfStatement extends Statement {
	public IfStatement( Expr expr, ArrayList<Statement> leftPart, ArrayList<Statement> rightPart ) {
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
