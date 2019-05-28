package AST;

public class AssignExprStat extends Statement {
	public AssignExprStat( Expr left, Expr right ) {
		this.left = left;
		this.right = right;
	}

	public void genC( PW pw ) {  // NÃO SEI SE ISSO TÁ CERTO
		pw.print(left.getName());
		if ( right != null ) {
			pw.print(" = ");
			right.genC(pw);
			pw.out.println(";");
		}
	}

	private Expr left, right;
}
