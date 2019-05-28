package AST;

public class AssignExprStat extends Statement {
	public AssignExprStat( Expr left, Expr right ) {
		this.left = left;
		this.right = right;
	}

	public void genC( PW pw ) {  // NÃO SEI SE ISSO TÁ CERTO
		this.left.genC(pw);
		if ( right != null ) {
			pw.print(" = ");
			this.right.genC(pw);
			pw.println(";");
		}
		else{
			pw.println(";");
		}
	}

	private Expr left, right;
}
